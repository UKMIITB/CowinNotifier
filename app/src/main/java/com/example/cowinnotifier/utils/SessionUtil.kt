package com.example.cowinnotifier.utils

import com.example.cowinnotifier.model.Center
import com.example.cowinnotifier.model.Session

class SessionUtil {
    companion object {
        fun isValidSessionForNotificationPush(
            session: Session,
            ageLimit: Long,
            vaccineFilter: String
        ): Boolean {
            return (isValidSession(
                session,
                ageLimit,
                vaccineFilter
            ) && session.available_capacity > 0)
        }

        fun filterCenterList(
            centerList: List<Center>,
            ageLimit: Long,
            vaccineFilter: String
        ): List<Center> {
            val filteredCenterList: MutableList<Center> = mutableListOf()

            for (eachCenter in centerList) {
                val sessionList: MutableList<Session> = mutableListOf()
                for (eachSession in eachCenter.sessions) {
                    if (isValidSession(eachSession, ageLimit, vaccineFilter)) {
                        sessionList.add(eachSession)
                    }
                }
                if (sessionList.size != 0) {
                    filteredCenterList.add(Center(eachCenter.name, eachCenter.address, sessionList))
                }
            }

            return filteredCenterList
        }

        private fun isValidSession(
            session: Session,
            ageLimit: Long,
            vaccineFilter: String
        ): Boolean {
            val vaccineResult =
                if (vaccineFilter.isEmpty()) true else session.vaccine == vaccineFilter
            val ageLimitResult = if (ageLimit == 0L) true else session.min_age_limit == ageLimit
            return (vaccineResult && ageLimitResult)
        }
    }
}