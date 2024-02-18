package com.lelestacia.finsem_market.ui.screen.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.finsem_market.data.model.UserDto
import com.lelestacia.finsem_market.domain.usecases.ExploreUseCases
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExploreViewModel(
    private val useCases: ExploreUseCases
) : ViewModel() {

    private val _services: MutableStateFlow<List<UserDto>> =
        MutableStateFlow(emptyList())
    val services: StateFlow<List<UserDto>> =
        _services.asStateFlow()

    private val _eventMessage: Channel<String> = Channel()
    val eventMessage: ReceiveChannel<String> = _eventMessage

    private fun getServices() = viewModelScope.launch {
        useCases.getServices()
            .catch { t ->
                _eventMessage.send(t.message.orEmpty())
            }
            .collectLatest {
                _services.update { it }
            }
    }

    init {
        getServices()
    }
}