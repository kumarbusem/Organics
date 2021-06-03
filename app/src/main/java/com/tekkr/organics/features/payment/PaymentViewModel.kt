package com.tekkr.organics.features.payment

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.razorpay.PaymentData
import com.tekkr.data.internal.common.ApiException
import com.tekkr.data.internal.common.RiderLoginException
import com.tekkr.data.models.Order
import com.tekkr.data.models.PaymentError
import com.tekkr.data.models.PaymentVerifyResponse
import com.tekkr.organics.common.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.internal.trimSubstring
import org.json.JSONObject
import java.net.SocketTimeoutException

class PaymentViewModel(context: Application) : BaseViewModel(context) {

    val obsOrder: MutableLiveData<Order> = MutableLiveData()
    val obsPaymentVerifyResponse: MutableLiveData<PaymentVerifyResponse> = MutableLiveData()

    init {
        getOrder()
    }

    private fun getOrder() {
        obsOrder.postValue(repoPrefs.getSelectedOrder())
    }

    fun onVerifyPayment(status: String, paymentData: PaymentData?, errorCode: String, errorDescription: String) {

        Log.e("PAYMENT::", "$status")

        val json = JSONObject()
        json.put("order", obsOrder.value?.id)
        json.put("razorpay_payment_id", paymentData?.paymentId.toString())
        json.put("razorpay_order_id", obsOrder.value?.razorpay_order_id.toString())

        if(status == PaymentVerifyResponse.STATUS_SUCCESS)
            json.put("razorpay_signature", paymentData?.signature.toString())
        if(status == PaymentVerifyResponse.STATUS_FAILED){

            val error = Gson().fromJson<PaymentError>(errorDescription, object : TypeToken<PaymentError>() {}.type)

            json.put("failed_code", error.error.code)
            json.put("failed_description", error.error.description)
            json.put("failed_source", error.error.source)
            json.put("failed_step", error.error.step)
            json.put("failed_reason", error.error.reason)
        }

        Log.e("VERIFY BODY::", json.toString())
        Log.e("VERIFY BODY::", json.toString())
        Log.e("VERIFY BODY::", json.toString())

        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json.toString())

        ioScope.launch {
            try {
                repoBasic.verifyPayment(requestBody) {
                    obsPaymentVerifyResponse.postValue(it)
                    obsProgressDialog.postValue(false)
                }
            } catch (e: ApiException) {
                obsPaymentVerifyResponse.postValue(null)
            } catch (e: SocketTimeoutException) {
                obsPaymentVerifyResponse.postValue(null)
            } catch (e: RiderLoginException) {
                repoPrefs.clearLoggedInUser()
                isUserLogout.postValue(true)
                obsPaymentVerifyResponse.postValue(null)
            } catch (e: Exception) {
                obsPaymentVerifyResponse.postValue(null)
                e.printStackTrace()
                if (e.message.toString().contains("Unable to resolve")) obsMessage.postValue("Network Issue\nUnable to resolve host")
                else obsMessage.postValue(e.message)
            }

        }


    }

}