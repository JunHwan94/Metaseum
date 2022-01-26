package com.exp.backend.api.guestboard.model.service

import com.exp.backend.api.guestboard.model.entity.GuestNote

interface GuestBoardService {
    fun findAll(): List<GuestNote>
    fun upsertByNo(guestNoteUrl: String): Int
}
