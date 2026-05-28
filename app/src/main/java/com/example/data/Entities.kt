package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auto_reply_rules")
data class AutoReplyRule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val keyword: String,
    val replyMessage: String,
    val category: String = "General",
    val language: String = "id", // "id" or "en"
    val isActive: Boolean = true,
    val matchType: String = "CONTAINS", // "EXACT", "CONTAINS", "STARTS_WITH"
    val usageCount: Int = 0
)

@Entity(tableName = "message_logs")
data class MessageLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val senderNumber: String,
    val senderName: String,
    val incomingText: String,
    val replyText: String?,
    val matchedKeyword: String?,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "SENT", // "SENT", "FAILED", "IGNORED"
    val groupCategory: String = "General"
)

@Entity(tableName = "app_contacts")
data class AppContact(
    @PrimaryKey val phoneNumber: String,
    val name: String,
    val groupName: String = "Unassigned", // "VIP", "Leads", "Regular"
    val lastInteraction: Long = System.currentTimeMillis(),
    val syncStatus: String = "SYNCED" // "SYNCED", "PENDING", "FAILED"
)
