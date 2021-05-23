package com.example.cowinnotifier.utils

import com.example.cowinnotifier.model.Session

class SessionUtil {
    companion object {
        fun isValidSession(session:Session):Boolean {
            return session.available_capacity > 0
        }
    }
}