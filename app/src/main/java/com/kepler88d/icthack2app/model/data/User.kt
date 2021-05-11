package com.kepler88d.icthack2app.model.data

import com.google.gson.GsonBuilder
import com.kepler88d.icthack2app.model.enumerations.UserSpecialization
import kotlinx.serialization.Serializable
import org.json.JSONObject

@Serializable
data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val password: String,
    val profileDescription: String,
    val rating: Float,
    val specialization: UserSpecialization,
    val githubProfileLink: String,
    val tgLink: String
) {
    fun fullName(): String = "$firstName $lastName"
    fun toJson(): JSONObject = JSONObject(toJsonString())
    private fun toJsonString(): String = GsonBuilder().create().toJson(this)

    companion object {
        fun fromJson(jsonObject: JSONObject): User =
            GsonBuilder().create().fromJson(jsonObject.toString(), User::class.java)

        fun fromJsonString(jsonString: String): User = fromJson(JSONObject(jsonString))
    }
}