package com.kepler88d.icthack2app.model.data

import com.google.gson.GsonBuilder
import com.kepler88d.icthack2app.model.enumerations.ReplyStatus
import kotlinx.serialization.Serializable
import org.json.JSONObject

@Serializable
data class Reply(
    val id: Int,
    val text: String,
    val projectId: Int,
    val authorId: Int,
    val vacancy: String,
    val status: ReplyStatus
) {
    fun toJson(): JSONObject = JSONObject(toJsonString())
    private fun toJsonString(): String = GsonBuilder().create().toJson(this)

    companion object {
        fun fromJson(jsonObject: JSONObject): Reply =
            GsonBuilder().create().fromJson(jsonObject.toString(), Reply::class.java)

        fun fromJsonString(jsonString: String): Reply = Reply.fromJson(JSONObject(jsonString))
    }
}