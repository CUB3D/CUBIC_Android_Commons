package pw.cub3d.commons.api

import com.squareup.moshi.Json
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ICrashLogger {
    @POST("app/delitics/submit")
    fun submitCrashLog(@Body log: String): Call<LogResponse>
}

class LogResponse {
    @Json(name = "Status")
    var status: Int? = -100
}