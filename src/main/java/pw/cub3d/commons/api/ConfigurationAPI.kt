package pw.cub3d.commons.api

import org.json.JSONObject
import pw.cub3d.commons.CUB3
import pw.cub3d.commons.configuration.CUB3DConfiguration
import pw.cub3d.commons.identification.DeviceIdentification
import pw.cub3d.commons.logging.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ConfigurationService {
    @GET("/api/config/{project_id}/{device_id}")
    fun getConfig(
        @Path("project_id") projectId: String,
        @Path("device_id") deviceid: String
    ): Call<String>
}

object ConfigurationAPI {
    private const val CONFIGURATION_URL = "https://config.cub3d.pw/"

    val service: ConfigurationService = Retrofit.Builder()
        .baseUrl(CONFIGURATION_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(ConfigurationService::class.java)

    fun getConfig() {
        service.getConfig("0d1a7dae-1780-4cd2-abcc-307eff8ec734", DeviceIdentification.getDeviceID()).enqueue(object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("Unable to retrieve config")
                Log.throwable(t)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    Log.d("Got remote config ${response.body()!!}")
                    CUB3.getConfiguration().loadFromJson(JSONObject(response.body()!!))
                } else {
                    Log.e("Unable to retrieve config: ${response.message()}")
                }
            }
        })
    }
}
