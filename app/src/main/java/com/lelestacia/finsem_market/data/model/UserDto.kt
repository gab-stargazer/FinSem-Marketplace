package com.lelestacia.finsem_market.data.model

import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class UserDto(

    @Json(name = "name")
    @PropertyName("name")
    var name: String = "",

    @Json(name = "role")
    @PropertyName("role")
    var role: Int = 0,

    @Json(name = "email")
    @PropertyName("email")
    var email: String = "",

    @Json(name = "url_github")
    @PropertyName("url_github")
    var githubUrl: String? = null,

    @Json(name = "url_linkedin")
    @PropertyName("url_linkedin")
    var linkedinURL: String? = null,

    @Json(name = "whatsapp_number")
    @PropertyName("whatsapp_number")
    var whatsappNumber: String? = null
)
