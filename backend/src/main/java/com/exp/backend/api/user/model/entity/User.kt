package com.exp.backend.api.user.model.entity

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
class User(var name: String,
           val email: String,
           val imgUrl: String?,
           val locale: String?,
           var stamps: HashMap<String, Int>? = HashMap(),
           var characterNo: Int? = 1,
           val titles: HashSet<String>? = HashSet(),
           val titleNo: Int? = null
)