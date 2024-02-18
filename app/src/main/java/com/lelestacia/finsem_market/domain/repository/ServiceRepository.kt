package com.lelestacia.finsem_market.domain.repository

import com.lelestacia.finsem_market.data.model.UserDto
import kotlinx.coroutines.flow.Flow

interface ServiceRepository {
    fun getServices(): Flow<List<UserDto>>
}