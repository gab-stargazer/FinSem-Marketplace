package com.lelestacia.finsem_market.domain.usecases

import com.lelestacia.finsem_market.data.model.UserDto
import com.lelestacia.finsem_market.domain.repository.ServiceRepository
import kotlinx.coroutines.flow.Flow

class ExploreUseCases(
    private val serviceRepository: ServiceRepository
) {

    fun getServices(): Flow<List<UserDto>> {
        return serviceRepository.getServices()
    }
}