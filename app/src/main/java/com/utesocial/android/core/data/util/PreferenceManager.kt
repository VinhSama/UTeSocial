package com.utesocial.android.core.data.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import java.io.Serializable
import java.lang.Exception

class PreferenceManager(context: Context) {

    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    val sharedPreferencesInstance: SharedPreferences
        get() = sharedPreferences
    init {
        var temp: SharedPreferences
        var masterKey: MasterKey? = null
        try {
            masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            temp = EncryptedSharedPreferences.create(
                context,
                context.packageName,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e : Exception) {
            e.message?.let { Debug.log("PreferenceManager", it) }
            temp = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        }
        sharedPreferences = temp
        this.editor = sharedPreferences.edit()
    }

    @Synchronized
    fun putString(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }
    @Synchronized
    fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }
    @Synchronized
    fun getString(key: String, defaultValue: String?): String? {
        return sharedPreferences.getString(key, defaultValue)
    }
    @Synchronized
    fun putInt(key: String, value: Int) {
        editor.putInt(key, value)
        editor.apply()
    }

    @Synchronized
    fun getInt(key: String): Int {
        return sharedPreferences.getInt(key, -1)
    }

    @Synchronized
    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    @Synchronized
    fun getLong(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    @Synchronized
    fun putLong(key: String, value: Long) {
        editor.putLong(key, value)
        editor.apply()
    }
    @Synchronized
    fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }
    @Synchronized
    fun putObject(key: String, o: Serializable) {
        val jsonData = GsonBuilder().create().toJson(o)
        editor.putString(key, jsonData)
        editor.apply()
    }
    @Synchronized
    fun <T> getObject(key: String, classOfT: Class<T>): T? {
        val data = getString(key, null)
        return data?.let {
            try {
                GsonBuilder().create().fromJson(it, classOfT)
            } catch (e: JsonSyntaxException) {
                null
            }
        }
    }
    @Synchronized
    fun remove(key: String) {
        editor.remove(key)
        editor.apply()
    }
    @Synchronized
    fun clear() {
        editor.clear()
        editor.apply()
    }
}