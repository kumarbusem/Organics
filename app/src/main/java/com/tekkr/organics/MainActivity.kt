package com.tekkr.organics

import android.app.Activity
import android.content.*
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.tekkr.data.dataSources.definitions.DataSourceFirestore
import com.tekkr.data.dataSources.definitions.DataSourceSharedPreferences
import com.tekkr.data.dataSources.repos.RepoFirestore
import com.tekkr.data.dataSources.repos.RepoSharedPreferences
import com.tekkr.organics.common.*
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.partial_blocked_version.view.*


class MainActivity : AppCompatActivity(), PaymentResultWithDataListener {

    var paymentListener: PaymentListener? = null
    var smsListener: SMSListener? = null
    protected val repoFirestore: DataSourceFirestore by lazy { RepoFirestore() }
    protected val repoPrefs: DataSourceSharedPreferences by lazy { RepoSharedPreferences() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getAppSettings()

        setAppUpdteButtons()

        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/Metropolis-Regular.otf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                ).build()
        )
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsVerificationReceiver, intentFilter)

    }

    private fun getAppSettings() {

        repoFirestore.getAppSettings { settings ->
            Log.e("APP SETTINGS::", settings.toString())
            if (settings == null) return@getAppSettings

            if (settings.blockVersionUpTo!! > BuildConfig.VERSION_NAME) {
                plBlockedVersion.show()
                plBlockedVersion.tvHeading.text =
                    "Unsupported App Version\n${BuildConfig.VERSION_NAME}"

            } else {
                plBlockedVersion.hide()
            }

            if (settings.underMaintenance == true) {
                plUnderMaintenance.show()
            } else {
                plUnderMaintenance.hide()
            }

        }
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isNetworkAvailable(this@MainActivity)) plNoInternet.hide()
            else plNoInternet.show()
        }
    }

    override fun onStart() {
        super.onStart()
        val networkIntentFilter = IntentFilter(CONNECTION)
        this.registerReceiver(networkReceiver, networkIntentFilter)

        if (isNetworkAvailable(this@MainActivity)) plNoInternet.hide()
        else plNoInternet.show()
    }

    override fun onStop() {
        super.onStop()
        this.unregisterReceiver(networkReceiver)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }


    private fun setAppUpdteButtons() {
        val downloadUrl =
            "https://drive.google.com/file/d/1BLsnDUxep_wVvKKQegzFUGQTA_RWy9zv/view?usp=sharing"
        plBlockedVersion.btnUpdatePlaystore.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(downloadUrl)
            )
            startActivity(browserIntent)
        }
        plBlockedVersion.btnUpdate.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(downloadUrl)
            )
            startActivity(browserIntent)
        }

    }

    fun setPaymentsListener(listener: PaymentListener) {
        paymentListener = listener
    }

    fun setSMSListener(listener: SMSListener) {
        smsListener = listener
    }

    interface PaymentListener {
        fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?)
        fun onPaymentError(errorCode: Int, errorDescription: String?, paymentData: PaymentData?)
    }

    interface SMSListener {
        fun onOTPReceived(otp: String)
        fun onPhoneNumberReceived(phone: String)
    }

    override fun onPaymentSuccess(rzpPaymentId: String?, paymentData: PaymentData?) {
        paymentListener?.onPaymentSuccess(rzpPaymentId, paymentData)
    }

    override fun onPaymentError(
        errorCode: Int,
        errorDescription: String?,
        paymentData: PaymentData?
    ) {
        paymentListener?.onPaymentError(errorCode, errorDescription, paymentData)
    }

    fun getPhoneNumber() {
        val hintRequest = HintRequest.Builder().setPhoneNumberIdentifierSupported(true)
            .build()
        val credentialsClient = Credentials.getClient(this)
        val intent = credentialsClient.getHintPickerIntent(hintRequest)
        startIntentSenderForResult(intent.intentSender, CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0)
    }


    private val smsVerificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                val extras = intent.extras
                val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

                when (smsRetrieverStatus.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        val consentIntent =
                            extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                        try {
                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST)
                        } catch (e: ActivityNotFoundException) {
                            Log.e("SMS ERROR::", e.printStackTrace().toString())
                        }
                    }
                    CommonStatusCodes.TIMEOUT -> {
                        // Time out occurred, handle the error.
                    }
                }
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SMS_CONSENT_REQUEST ->
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                    Log.e("SMS SUCCESS::", message.toString())
                    smsListener?.onOTPReceived(message.getOtp())
                } else {
                    Log.e("SMS denied::", "Enter manually")
                }
            CREDENTIAL_PICKER_REQUEST ->
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val credential = data.getParcelableExtra<Credential>(Credential.EXTRA_KEY)
                    Log.e("PHONE Number::", credential?.id.toString())
                    smsListener?.onPhoneNumberReceived(credential?.id.toString().replaceFirst("+91", ""))
                }
        }
    }


    companion object {
        const val CONNECTION = "android.net.conn.CONNECTIVITY_CHANGE"
        const val GPS = "android.location.PROVIDERS_CHANGED"
        private val SMS_CONSENT_REQUEST = 2
        private val CREDENTIAL_PICKER_REQUEST = 1
    }

}


