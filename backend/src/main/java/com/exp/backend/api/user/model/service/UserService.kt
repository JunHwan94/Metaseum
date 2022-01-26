package com.exp.backend.api.user.model.service

import com.exp.backend.api.user.model.entity.User
import com.exp.backend.api.user.model.request.UserReq

interface UserService {
    fun checkUser(idTokenString: String): Map<String, Any>
    fun findByEmail(email: String): User?
    fun isUserExists(email: String): Boolean
    fun addStamp(email:String, stamp: Int): HashMap<String, Int>
    fun updateUserInfo(email:String, userReq: UserReq): User?
    fun insertUser(user: User): Boolean
    fun addUserTitle(email: String, title: String): Boolean
}