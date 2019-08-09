package pw.cub3d.commons.configuration

import android.annotation.SuppressLint
import android.content.Context
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.json.JSONObject
import pw.cub3d.commons.CUB3
import pw.cub3d.commons.api.ConfigurationAPI
import pw.cub3d.commons.logging.Log
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KProperty

class CUB3DConfiguration(val ctx: Context, val defaultConfigName: String = "config") {

    val configValues = ConcurrentHashMap<String, Any>()

    init {
        // Automatically load a config json resource if there is one
        ctx.resources
            .getIdentifier(defaultConfigName, "raw", ctx.packageName)
            .takeIf { it > 0 }
            ?.let {
                loadFromJson(JSONObject(ctx.resources.openRawResource(it).bufferedReader().readText()))
            }

        // Try to fetch a network config
         ConfiguratigstonAPI.getConfig()
    }

    fun loadFromJson(json: JSONObject) {
        json.keys().forEach { key ->
            configValues[key] = json.get(key)
        }
    }

    @SuppressLint("NewApi")
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
}

fun CUB3DConfiguration() = CUB3.getConfiguration()