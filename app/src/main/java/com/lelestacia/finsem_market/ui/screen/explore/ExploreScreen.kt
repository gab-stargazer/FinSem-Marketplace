package com.lelestacia.finsem_market.ui.screen.explore

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lelestacia.finsem_market.data.model.UserDto
import com.lelestacia.finsem_market.ui.component.ServiceItemCard

@Composable
fun ExploreScreen(
    services: List<UserDto>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(
            vertical = 8.dp
        ),
        modifier = modifier.fillMaxSize()
    ) {
        items(items = services) { service ->
            ServiceItemCard(user = service)
        }
    }
}