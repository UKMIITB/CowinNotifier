package com.example.cowinnotifier.utils

import com.example.cowinnotifier.model.Center
import com.example.cowinnotifier.model.Session

class SessionUtil {
    companion object {
        fun isValidSessionForNotificationPush(
            session: Session,
            ageLimit: Long,
            vaccineList: ArrayList<String>,
            dose: String
        ): Boolean {
            return (isValidSession(
                session,
                ageLimit,
                vaccineList,
                dose
            ))
        }

        fun filterCenterList(
            centerList: List<Center>,
            ageLimit: Long,
            vaccineList: ArrayList<String>,
            dose: String
        ): List<Center> {
            val filteredCenterList: MutableList<Center> = mutableListOf()

            for (eachCenter in centerList) {
                val sessionList: MutableList<Session> = mutableListOf()
                for (eachSession in eachCenter.sessions) {
                    if (isValidSession(eachSession, ageLimit, vaccineList, dose)) {
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
            vaccineList: ArrayList<String>,
            dose: String
        ): Boolean {
            val vaccineResult =
                if (vaccineList.isEmpty()) true else session.vaccine in vaccineList
            val ageLimitResult = if (ageLimit == 0L) true else session.min_age_limit == ageLimit
            //TODO add dose filter here
            return (vaccineResult && ageLimitResult)
        }
    }
}