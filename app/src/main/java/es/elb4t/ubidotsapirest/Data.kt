package es.elb4t.ubidotsapirest

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Data {
    @SerializedName("variable")
    @Expose
    var variable: String? = null
    @SerializedName("value")
    @Expose
    var value: Double? = null
}