package es.elb4t.ubidotsapirest

import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class UbiClient() {

    private var api: UbiAPI? = null
    private var retroClient: Retrofit

    init {
        retroClient = Retrofit.Builder()
                .baseUrl(UBI_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    companion object {
        private val TAG = UbiClient::class.java.simpleName
        private val UBI_BASE_URL = "http://things.ubidots.com/"
        private var client: UbiClient? = null

        fun getClient(): UbiClient {
            if (client != null) return client as UbiClient
            client = UbiClient()
            return client as UbiClient
        }
    }

    private fun getUbiClient(): UbiAPI {
        return retroClient.create(UbiAPI::class.java)
    }

    fun sendData(dList: ArrayList<Data>, token: String){
        api = client?.getUbiClient()
        val c = api?.sendValue(dList, token)
        c?.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d(TAG, "onResponse")
                Log.d(TAG, "Result: " + response.isSuccessful + " - " + response.message())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}