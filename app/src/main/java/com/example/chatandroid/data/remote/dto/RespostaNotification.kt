package com.example.chatandroid.data.remote.dto

data class RespostaNotification(
    val canonical_ids: Int,
    val failure: Int,
    val multicast_id: Long,
    val results: List<Result>,
    val success: Int
)