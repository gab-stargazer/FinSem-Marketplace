package com.lelestacia.finsem_market.ui.component

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lelestacia.finsem_market.data.model.UserDto
import com.lelestacia.finsem_market.ui.theme.FinalSemesterMarketplaceTheme
import com.lelestacia.finsem_market.util.launchUrlIntent
import com.lelestacia.finsem_market.util.launchWhatsappIntent
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Github
import compose.icons.fontawesomeicons.brands.Linkedin
import compose.icons.fontawesomeicons.brands.Whatsapp

@Composable
fun ServiceItemCard(
    user: UserDto,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            TextField(
                value = user.name,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(
                        text = "Name",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!user.githubUrl.isNullOrBlank()) {
                    IconButton(
                        onClick = {
                            try {
                                context.launchUrlIntent(user.githubUrl.orEmpty())
                            } catch (e: Exception) {
                                Log.e(TAG, "ServiceItemCard: Invalid URL Pattern")
                            }
                        }
                    ) {
                        Icon(
                            imageVector = FontAwesomeIcons.Brands.Github,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                if (!user.linkedinURL.isNullOrBlank()) {
                    IconButton(
                        onClick = {
                            try {
                                context.launchUrlIntent(user.linkedinURL.orEmpty())
                            } catch (e: Exception) {
                                Log.e(TAG, "ServiceItemCard: Invalid URL Pattern")
                            }
                        }
                    ) {
                        Icon(
                            imageVector = FontAwesomeIcons.Brands.Linkedin,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                if (!user.whatsappNumber.isNullOrBlank()) {
                    IconButton(
                        onClick = {
                            try {
                                context.launchWhatsappIntent(user.whatsappNumber.orEmpty())
                            }catch (e:Exception) {
                                Log.e(TAG, "ServiceItemCard: Invalid URL Pattern")
                            }
                        }
                    ) {
                        Icon(
                            imageVector = FontAwesomeIcons.Brands.Whatsapp,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewServiceItemCard() {
    FinalSemesterMarketplaceTheme {
        ServiceItemCard(
            user = UserDto(
                name = "Kamil Malik",
                role = 1,
                email = "lelestargazer@gmail.com",
                githubUrl = "https://github.com/gab-stargazer",
                linkedinURL = "https://www.linkedin.com/in/kamil-malik/",
                whatsappNumber = "62822696969"
            )
        )
    }
}