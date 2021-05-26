package com.example.cowinnotifier.utils

import com.example.cowinnotifier.helper.AppConstants

class FilterUtil {
    companion object {
        fun getAgeFilterValueFromPosition(position: Int): Long {
            return AppConstants.AGE_LIMIT_FILTER_MAP.getOrDefault(
                AppConstants.AGE_LIMIT_FILTER_ARRAY[position],
                0L
            )
        }

        fun getVaccineFilterValueFromPosition(position: Int): String {
            return AppConstants.VACCINE_FILTER_MAP.getOrDefault(
                AppConstants.VACCINE_FILTER_ARRAY[position],
                ""
            )
        }
    }
}