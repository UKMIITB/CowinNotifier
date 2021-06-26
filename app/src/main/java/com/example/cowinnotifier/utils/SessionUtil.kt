package com.example.cowinnotifier.utils

import com.example.cowinnotifier.helper.AppConstants
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
            val vaccineResult = session.vaccine in vaccineList
            val ageLimitResult = session.min_age_limit == ageLimit
            val doseResult: Boolean = if (AppConstants.STRING_TO_DOSE_MAP[dose].equals("1st")) {
                session.available_capacity_dose1 > 0
            } else {
                session.available_capacity_dose2 > 0
            }
            return (vaccineResult && ageLimitResult && doseResult)
        }
    }
}