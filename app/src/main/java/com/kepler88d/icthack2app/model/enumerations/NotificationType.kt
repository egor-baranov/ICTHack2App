package com.kepler88d.icthack2app.model.enumerations

import kotlinx.serialization.Serializable

@Serializable
enum class NotificationType {
    NEW_REPLY, ACCEPTED_REPLY, DENIED_REPLY, WAIT_REPLY
}