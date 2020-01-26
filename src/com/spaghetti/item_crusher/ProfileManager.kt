package com.spaghetti.item_crusher

import com.spaghetti.item_crusher.entities.Profile
import org.rspeer.script.Script
import com.google.gson.GsonBuilder
import java.io.*
import com.google.gson.reflect.TypeToken
import org.rspeer.ui.Log


object ProfileManager {

    private val basePath = Script.getDataDirectory().toString()
    private val scriptDataPath = File.separator.let { basePath + it + SCRIPT_AUTHOR + it + SCRIPT_NAME + it }
    private val profilePath = scriptDataPath + "profile.json"
    private val PROFILE_TYPE = object: TypeToken<Profile>() { }.type

    init {
        val dataDir = File(scriptDataPath)
        dataDir.mkdirs()

        if (!File(profilePath).exists()) save(Profile())
    }

    fun save(profile: Profile) {
        try {
            FileWriter(profilePath).use { writer ->
                val gson = GsonBuilder().create()
                gson.toJson(profile, writer)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun load(): Profile? {
        try {
            FileReader(profilePath).use {
                val gson = GsonBuilder().create()
                return gson.fromJson(it, PROFILE_TYPE)
            }
        } catch (e: IOException) {
            Log.info("Failed to load profile.")
            return null
        }
    }

}