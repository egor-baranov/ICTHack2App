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
    val tags: MutableList<String>,
    val githubProjectLink: String,
    val ownerId: Int,
    val contributorListId: List<Int>,
    val vacancy: Map<String, Int>,
    val freeVacancy: Map<String, Int>
) {
    fun toJson(): JSONObject = JSONObject(toJsonString())
    private fun toJsonString(): String = GsonBuilder().create().toJson(this)

    companion object {
        private fun fromJson(jsonObject: JSONObject): Project =
            GsonBuilder().create().fromJson(jsonObject.toString(), Project::class.java)

        fun fromJsonString(jsonString: String): Project = fromJson(JSONObject(jsonString))
    }
}