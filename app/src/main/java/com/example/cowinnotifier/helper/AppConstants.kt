package com.example.cowinnotifier.helper

class AppConstants {
    companion object {
        val DISTRICT_ID = "districtId"
        val DISTRICT_ID_POSITION = "districtIdPosition"
        val STATE_ID = "stateId"
        val STATE_ID_POSITION = "stateIdPosition"
        val PINCODE = "pincode"

        val SERVICE_REPEAT_INTERVAL = 15L  // minutes

        val NOTIFICATION_CHANNEL_NAME = "Slot Search Notification"
        val NOTIFICATION_CHANNEL_DESC =
            "This channel is used to show notification whenever there is a slot available"
        val NOTIFICATION_CHANNEL_ID = "101"

        val UNIQUE_WORK_NAME = "Cowin Search Work"

        val ALERT_DIALOG_MESSAGE =
            "Do you want to continue search in background and send notification when slots are available"
        val ALERT_DIALOG_TITLE = "Continue searching in background"
    }
}