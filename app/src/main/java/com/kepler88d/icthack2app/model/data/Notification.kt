package com.kepler88d.icthack2app.model.data

import com.google.gson.GsonBuilder
import com.kepler88d.icthack2app.model.enumerations.NotificationType
import kotlinx.serialization.Serializable
import org.json.JSONObject

@Serializable
data class Notification(
    val id: Int,
    val toUserId: Int,
    val title: String,
    val text: String,
    val type: NotificationType,
    val replyId: Int
) {
    fun toJson(): JSONObject = JSONObject(toJsonString())
    private fun toJsonString(): String = GsonBuilder().create().toJson(this)

    companion object {
        fun fromJson(jsonObject: JSONObject): Notification =
            GsonBuilder().create().fromJson(jsonObject.toString(), Notification::class.java)

        fun fromJsonString(jsonString: String): Notification = Notification.fromJson(JSONObject(jsonString))
    }
}