package pw.cub3d.commons.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object SharedPrefs : SharedPreferences {
    override fun contains(key: String): Boolean {
        return prefs!!.contains(key)
    }

    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        return if(prefs != null) {
            prefs!!.getBoolean(key, defValue)
        } else {
            defValue
        }
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInt(key: String?, defValue: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAll(): MutableMap<String, *> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun edit(): SharedPreferences.Editor {
        return prefs!!.edit()
    }

    override fun getLong(key: String?, defValue: Long) = prefs!!.getLong(key, defValue)

    override fun getFloat(key: String?, defValue: Float): Float {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getString(key: String, defValue: String?): String? {
        return if(prefs != null) {
            prefs!!.getString(key, defValue)
        } else {
            defValue
        }
    }

    private var prefs: SharedPreferences? = null

    fun initialise(ctx: Context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(ctx)
    }
}