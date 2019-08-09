package pw.cub3d.commons.api

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import okhttp3.OkHttpClient
import pw.cub3d.commons.logging.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface StaticConfigService {
    @GET("app/ncl/static_config")
    fun getStaticConfig(): Call<StaticConfigResponse>
}

data class StaticConfigResponse(
    val version: Int,
    val url_arm: String?
)

object StaticConfigAPI {
    inline fun retrieveStaticConfig(crossinline callback: (StaticConfigResponse)->Unit) {
        val rf = Retrofit.Builder()
            .baseUrl("https://auth.cub3d.pw")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(OkHttpClient())
            .build()

        val service = rf.create(StaticConfigService::class.java)

        service.getStaticConfig().enqueue(object: Callback<StaticConfigResponse> {
            override fun onResponse(call: Call<StaticConfigResponse>, response: Response<StaticConfigResponse>) {
                if(response.isSuccessful) {
                    callback(response.body()!!)
                }
            }

            override fun onFailure(call: Call<StaticConfigResponse>, t: Throwable) {
                Log.d("Unable to retrieve static config from server")
            }
        })
    }

    fun downloadUpdateFile(it: StaticConfigResponse, ctx: Context) {
        val downloadManager = ctx.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val url = Uri.parse(it.url_arm ?: "")

        val request = DownloadManager.Request(url)
        request.setTitle("NCL updating")
        request.setDescription("Downloading")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setVisibleInDownloadsUi(false)

        downloadManager.enqueue(request)
    }
}