package com.exp.backend.api.heritage.model.service;

import com.exp.backend.api.heritage.model.entity.Heritage;

interface HeritageService {
    fun findAll(): List<Heritage>
    fun findByCode(code: String): Heritage?
}
