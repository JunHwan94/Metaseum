package com.exp.backend.api.heritage.model.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "heritages")
class Heritage(
    val code: String,
    val name: String,
    val category: String,
    val grade: String,
    val era: String,
    val location: String,
    val manager: String,
    val desc: Info,
    val videoUrl: String?,
    val audioUrl: Info
)