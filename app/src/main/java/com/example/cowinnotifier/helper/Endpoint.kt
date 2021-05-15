package com.example.cowinnotifier.helper

class Endpoint {
    companion object {
        const val BASE_URL = "https://cdn-api.co-vin.in/api"
        const val GET_STATES_URL = "/v2/admin/location/states"
        const val GET_DISTRICTS_URL = "/v2/admin/location/districts/"
        const val CALENDAR_BY_PIN_URL = "/v2/appointment/sessions/public/calendarByPin"
        const val CALENDAR_BY_DISTRICT_URL = "/v2/appointment/sessions/public/calendarByDistrict"
    }
}