package com.ari.curve.data.dao

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String = "",
    val img_url : String = "",
    val username: String = ""
)
