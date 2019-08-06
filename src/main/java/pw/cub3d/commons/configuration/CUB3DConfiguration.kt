package pw.cub3d.commons.configuration

import android.content.Context
import org.json.JSONObject
import pw.cub3d.commons.CUB3
import kotlin.reflect.KProperty

class CUB3DConfiguration(val ctx: Context, val defaultConfigName: String = "config.json") {

    // Automaticaly load a config json resource if there is one
    private val configJson: JSONObject? = ctx.resources
        .getIdentifier(defaultConfigName, "raw", ctx.packageName)
        .takeIf { it > 0 }
        ?.let {
            JSONObject(ctx.resources.openRawResource(it).bufferedReader().readText())
        }

    operator fun <T> getValue(thisRef: Any?, property: KProperty<*>): T {
        println("$thisRef, thank you for delegating '${property.name}' to me!")

        // Try and load the value from the json config if it exists
        configJson?.let {
            if(configJson.has(property.name)) {
                return configJson.get(property.name) as T
            }
        }

        return "pk.eyJ1IjoiY3ViM2R1ayIsImEiOiJjanIzbnNndTUwd28wM3hxbXg0aWcxbnNmIn0.zXMx7BMPn18XxW46kYUvLQ" as T
    }

    operator fun <T> setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        println("$value has been assigned to '${property.name}' in $thisRef.")
    }
}

fun CUB3DConfiguration() = CUB3.getConfiguration()