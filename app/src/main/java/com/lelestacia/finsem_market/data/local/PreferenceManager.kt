package com.lelestacia.finsem_market.data.local

import android.content.Context
import androidx.core.content.edit
import com.lelestacia.finsem_market.data.model.UserDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class PreferenceManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("finsem_pref", Context.MODE_PRIVATE)

    var user: UserDto?
        get() {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val adapter = moshi.adapter(UserDto::class.java)
            return try {
                adapter.fromJson(sharedPreferences.getString("user", null).orEmpty())
            } catch (e: Exception) {
                null
            }
        }
        set(value) {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val adapter = moshi.adapter(UserDto::class.java)
            sharedPreferences.edit {
                putString("user", adapter.toJson(value))
                apply()
            }
        }

    fun clearPreferences() {
        sharedPreferences.edit().clear().apply()
    }
}