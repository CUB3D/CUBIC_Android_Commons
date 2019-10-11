package pw.cub3d.commons.configuration

import android.content.Context
import org.json.JSONObject
import pw.cub3d.commons.CUB3
import pw.cub3d.commons.api.ConfigurationAPI
import pw.cub3d.commons.logging.Log
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KProperty

class CUB3DConfiguration(val ctx: Context, private val defaultConfigName: String = "config") {

    private val configValues = ConcurrentHashMap<String, Any>()

    fun init() {
        // Automatically load a config json resource if there is one
        ctx.resources
            .getIdentifier(defaultConfigName, "raw", ctx.packageName)
            .takeIf { it > 0 }
            ?.let {
                loadFromJson(JSONObject(ctx.resources.openRawResource(it).bufferedReader().readText()))
            }

        // Load any cached online config
        val cacheFile = File(CUB3.getConfig().dataDirectory, "OnlineConfigCache.json")

        if(cacheFile.exists()) {
            loadFromJson(JSONObject(cacheFile.bufferedReader().readText()))
        }

        // Try to fetch a network config, if we have a network connection
        if(CUB3.isNetworkAvailable()) {
            ConfigurationAPI.getConfig {

                // Save the new config to cache
                cacheFile.writeText(it)

                // Load the new config
                loadFromJson(JSONObject(it))
            }
        }
    }

    fun loadFromJson(json: JSONObject) {
        json.keys().forEach { key ->
            configValues[key] = json.get(key)
        }
    }

    operator fun <T> getValue(thisRef: Any?, property: KProperty<*>): T {

        if(configValues.containsKey(property.name)) {
            return configValues[property.name] as T
        }

        Log.e("No property called '${property.name}' was found")

        return null as T
    }

    operator fun <T> setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        println("$value has been assigned to '${property.name}' in $thisRef.")
    }

    fun getString(name: String, default: String? = null): String? = if(configValues.containsKey(name)) {
        configValues[name] as String?
    } else {
        default
    }

    fun getMap(name: String): Map<String, String> = getString(name, "")!!
            .split(";")
            .map {
                val parts = it.split("=")
                parts[0] to parts[1]
            }.toMap()

    fun getArray(name: String): Array<String> = getString(name, "")!!
        .split(";")
        .toTypedArray()
}

fun CUB3DConfiguration() = CUB3.getConfiguration()