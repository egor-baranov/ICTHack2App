package com.kepler88d.icthack2app.model.data

import com.google.gson.GsonBuilder
import kotlinx.serialization.Serializable
import org.json.JSONObject


@Serializable
data class Project(
    val id: Int,
    val name: String,
    val description: String,
    val replyIdList: MutableList<Int>,
    val githubProjectLink: String,
    val ownerId: Int
) {
    fun toJson(): JSONObject = JSONObject(toJsonString())
    private fun toJsonString(): String = GsonBuilder().create().toJson(this)

    companion object {
        fun fromJson(jsonObject: JSONObject): Project =
            GsonBuilder().create().fromJson(jsonObject.toString(), Project::class.java)

        fun fromJsonString(jsonString: String): User = User.fromJson(JSONObject(jsonString))
    }
}