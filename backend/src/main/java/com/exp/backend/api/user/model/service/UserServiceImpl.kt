package com.exp.backend.api.user.model.service

import com.exp.backend.api.user.model.entity.User
import com.exp.backend.api.user.model.request.UserReq
import com.exp.backend.common.auth.JwtUtil
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import org.bson.Document
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.collections.HashMap

@Service
open class UserServiceImpl constructor(private val mongoTemplate: MongoTemplate) : UserService{
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val STAMP_TITLE = "지화자"

    override fun checkUser(idTokenString: String): Map<String, Any> {
        val idToken = getIdToken(idTokenString)
        var created = false
        var verified = false
        val user: User
        val result = HashMap<String, Any>()
        if(idToken != null) {
            user = getNewUser(idToken)

            val userInDB: User? = findByEmail(user.email)
            if (userInDB == null) {
                logger.debug("${user.email} 사용자 없음")
                created = insertUser(user)
            }else {
                logger.debug("${user.email} 사용자 있음")
                user.name = userInDB.name
                user.stamps = userInDB.stamps
                user.characterNo = userInDB.characterNo
            }
            verified = true
            result["user"] = user
        }

        return result.apply{
            this["created"] = created
            this["verified"] = verified
        }
    }

    @Transactional
    override fun insertUser(user: User): Boolean {
        return mongoTemplate.db.getCollection("users")
            .insertOne(
                Document().append("name", user.name)
                    .append("email", user.email)
                    .append("stamps", user.stamps)
            )
            .wasAcknowledged()
    }

    private fun getNewUser(idToken: GoogleIdToken): User{
        val payload = idToken.payload
        val email = payload.email
        val locale = payload["locale"] as String
        val name = payload["name"] as String
        val imgUrl = payload["picture"] as String
        return User(name, email, imgUrl, locale, characterNo = 0)
    }

    private fun getIdToken(idTokenString: String): GoogleIdToken? {
        return JwtUtil.getVerifier().verify(idTokenString)
    }

    private fun getEmailQuery(email: String): Query {
        return Query.query(Criteria.where("email").`is`(email))
    }

    @Transactional
    override fun findByEmail(email: String): User? {
        logger.debug("getUserByEmail 실행")
        val user = mongoTemplate.findOne(
            getEmailQuery(email),
            User::class.java
        )
        logger.debug("사용자 $user")
        logger.debug("이름 ${user?.name}")
        return user
    }

    override fun isUserExists(email: String): Boolean {
        return mongoTemplate.exists(
            getEmailQuery(email),
            User::class.java
        )
    }

    @Transactional
    override fun addStamp(email: String, stamp: Int): HashMap<String, Int> {
        val user = findByEmail(email)!!
        user.stamps?.set(stamp.toString(), 1)
        val update = Update().set("stamps", user.stamps)
        if(user.stamps?.size == 5) {
            user.titles?.add(STAMP_TITLE)
            update.set("titles", user.titles)
        }
        mongoTemplate.updateFirst(
            getEmailQuery(email),
            update,
            User::class.java
        )
        return user.stamps!!
    }

    @Transactional
    override fun updateUserInfo(email:String, userReq: UserReq): User? {
        mongoTemplate.updateFirst(
            getEmailQuery(email),
            Update().set("name", userReq.name)
                .set("characterNo", userReq.characterNo)
                .set("titleNo", userReq.titleNo),
            User::class.java
        )
        return findByEmail(email)
    }

    @Transactional
    override fun addUserTitle(email: String, title: String): Boolean {
        val set = mongoTemplate.findOne(getEmailQuery(email), User::class.java)?.titles
        set?.add(title)
        return mongoTemplate.updateFirst(
            getEmailQuery(email),
            Update().set("titles", set),
            User::class.java
        ).wasAcknowledged()
    }
}