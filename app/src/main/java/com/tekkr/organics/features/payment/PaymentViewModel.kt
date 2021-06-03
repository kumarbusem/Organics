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
import com.tekkr.data.models.RazorPayResponse
import com.tekkr.data.models.SimpleResponse
import com.tekkr.organics.common.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import java.net.SocketTimeoutException

class PaymentViewModel(context: Application) : BaseViewModel(context) {

    val obsOrder: MutableLiveData<Order> = MutableLiveData()
    val obsPaymentVerify: MutableLiveData<SimpleResponse> = MutableLiveData()
    val obsRazorPayStatus: MutableLiveData<RazorPayResponse> = MutableLiveData()

    init {
        getOrder()
    }

    private fun getOrder() {
        obsOrder.postValue(repoPrefs.getSelectedOrder())
    }

    fun verifyPayment() {

        val json = JSONObject()
        json.put("order", obsOrder.value?.id)
        json.put("razorpay_payment_id", obsRazorPayStatus.value?.paymentData?.paymentId.toString())
        json.put("razorpay_order_id", obsOrder.value?.razorpay_order_id.toString())
        json.put("razorpay_signature", obsRazorPayStatus.value?.paymentData?.signature.toString())

        Log.e("VERIFY BODY::", json.toString())

        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json.toString())
        ioScope.launch {
            try {
                repoBasic.verifyPayment(requestBody) {
                    obsPaymentVerify.postValue(it)
                }
            } catch (e: ApiException) {
                obsPaymentVerify.postValue(SimpleResponse(SimpleResponse.STATUS_FAILED, e.message.toString()))
            } catch (e: SocketTimeoutException) {
                obsPaymentVerify.postValue(SimpleResponse(SimpleResponse.STATUS_FAILED, "Slow Network, Socket Timeout"))
            } catch (e: RiderLoginException) {
                repoPrefs.clearLoggedInUser()
                isUserLogout.postValue(true)
            } catch (e: Exception) {
                obsPaymentVerify.postValue(SimpleResponse(SimpleResponse.STATUS_FAILED, "Slow Network, Socket Timeout"))
                e.printStackTrace()
                if (e.message.toString().contains("Unable to resolve"))
                obsPaymentVerify.postValue(SimpleResponse(SimpleResponse.STATUS_FAILED, "Network Issue\nUnable to resolve host"))
                else obsPaymentVerify.postValue(SimpleResponse(SimpleResponse.STATUS_FAILED, e.message.toString()))
            }
        }
    }
}