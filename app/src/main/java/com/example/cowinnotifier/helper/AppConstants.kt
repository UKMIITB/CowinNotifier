package com.example.cowinnotifier.helper

class AppConstants {
    companion object {
        val DISTRICT_ID = "districtId"
        val STATE_ID = "stateId"
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

        val AGE_LIMIT_FILTER_ARRAY = arrayOf("None", "Age 18+", "Age 45+")
        val AGE_LIMIT_FILTER_MAP = mapOf("None" to 0L, "Age 18+" to 18L, "Age 45+" to 45L)
        val VACCINE_FILTER_ARRAY = arrayOf("None", "Covishield", "Covaxin", "Sputnik V")
        val VACCINE_FILTER_MAP = mapOf("None" to "", "Covishield" to "COVISHIELD", "Covaxin" to "COVAXIN", "Sputnik V" to "SPUTNIK V")

        val AGE_LIMIT = "ageLimit"
        val VACCINE = "vaccine"
    }
}