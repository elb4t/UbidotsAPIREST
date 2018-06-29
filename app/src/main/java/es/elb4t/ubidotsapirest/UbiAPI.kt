package es.elb4t.ubidotsapirest

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query


interface UbiAPI {
    @POST("/api/v1.6/collections/values")
    fun sendValue(
            @Body dataList: ArrayList<Data>,
            @Query("token") token: String
    ): Call<ResponseBody>
}