package com.exp.backend.api.guestboard.model.entity

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "guestboard")
class GuestNote(val guestNoteNo: Int, val guestNoteUrl: String)