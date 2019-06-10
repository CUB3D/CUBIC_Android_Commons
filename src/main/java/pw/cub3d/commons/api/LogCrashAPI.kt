package pw.cub3d.commons.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ICrashLogger {
    @POST("app/delitics/submit")
    fun submitCrashLog(@Body log: String): Call<LogResponse>
}

class LogResponse {
    @Expose
    @SerializedName("Status")
    var status: Int? = -100
}