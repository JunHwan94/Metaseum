package com.exp.backend.common.auth

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.util.Utils
import java.util.*

class JwtUtil {
    companion object {
        val TOKEN_PREFIX = "Bearer "
        val HEADER_STRING = "Authorization"
        private val CLIENT_ID = "639252556477-ufrt3k3jqd2po4n3accd86e16bdbb09f.apps.googleusercontent.com"

        val transport = Utils.getDefaultTransport()
        val jsonFactory = Utils.getDefaultJsonFactory()

        fun getVerifier() =
            GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build()
    }
}