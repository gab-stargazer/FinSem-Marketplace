package com.lelestacia.finsem_market.data.model

import com.google.firebase.firestore.PropertyName

data class UserRegistrationDTO(

    @PropertyName("name")
    val name: String,

    @PropertyName("role")
    val role: Int,

    @PropertyName("email")
    val email: String,

    @PropertyName("url_github")
    val githubUrl: String? = null,

    @PropertyName("url_linkedin")
    val linkedinURL: String? = null,

    @PropertyName("whatsapp_number")
    val whatsappNumber: String? = null
)
