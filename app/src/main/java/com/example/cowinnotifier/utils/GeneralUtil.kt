package com.example.cowinnotifier.utils

class GeneralUtil {
    companion object {
        fun getDateFromDateString(date: String): String {
            return date.split("-")[0]
        }

        fun getMonthFromDateString(date: String): String {
            return getMonthStringFromInt(date.split("-")[1].toInt())
        }

        private fun getMonthStringFromInt(month: Int): String {
            return when (month) {
                1 -> "JAN"
                2 -> "FEB"
                3 -> "MAR"
                4 -> "APR"
                5 -> "MAY"
                6 -> "JUN"
                7 -> "JUL"
                8 -> "AUG"
                9 -> "SEP"
                10 -> "OCT"
                11 -> "NOV"
                12 -> "DEC"
                else -> "JAN"
            }
        }
    }
}