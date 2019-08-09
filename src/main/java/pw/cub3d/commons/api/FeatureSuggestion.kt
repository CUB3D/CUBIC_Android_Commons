package pw.cub3d.commons.api

import android.util.Base64
import com.squareup.moshi.Json
import okhttp3.OkHttpClient
import pw.cub3d.commons.logging.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.nio.charset.Charset


interface FeatureSuggestionService {
    @POST("app/ncl/featureSuggest")
    fun sendFeatureSuggestion(@Body suggestion: String): Call<FeatureSuggestionResponse>
}

class FeatureSuggestionResponse {
    @Json(name = "Status")
    var status: Int = 0
}

object FeatureSuggestionAPI {
    inline fun sendFeatureSuggestion(suggestion: String, crossinline callback: (FeatureSuggestionResponse)->Unit) {
        Log.d("Sending suggestion")
        val rf = Retrofit.Builder()
            .baseUrl("https://auth.cub3d.pw")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(OkHttpClient())
            .build()

        val service = rf.create(FeatureSuggestionService::class.java)

        val data = String(Base64.encode(suggestion.toByteArray(Charset.forName("UTF-8")), Base64.URL_SAFE or Base64.NO_WRAP), Charset.forName("UTF-8"))

        service.sendFeatureSuggestion(data).enqueue(object: Callback<FeatureSuggestionResponse> {
            override fun onResponse(call: Call<FeatureSuggestionResponse>, response: Response<FeatureSuggestionResponse>) {
                Log.d("Got callback")
                if(response.isSuccessful) {
                    callback(response.body()!!)
                } else {
                    Log.d("Unable to submit feature suggestion: ${response.code()} ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<FeatureSuggestionResponse>, t: Throwable) {
                Log.d("Unable to submit feature suggestion: ${t.localizedMessage}")
            }
        })
    }
}