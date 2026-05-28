package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface AutoResponderDao {

    // --- RULES ---
    @Query("SELECT * FROM auto_reply_rules ORDER BY id DESC")
    fun getAllRules(): Flow<List<AutoReplyRule>>

    @Query("SELECT * FROM auto_reply_rules WHERE isActive = 1")
    fun getActiveRules(): Flow<List<AutoReplyRule>>

    @Query("SELECT * FROM auto_reply_rules WHERE isActive = 1")
    suspend fun getActiveRulesList(): List<AutoReplyRule>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRule(rule: AutoReplyRule): Long

    @Update
    suspend fun updateRule(rule: AutoReplyRule)

    @Delete
    suspend fun deleteRule(rule: AutoReplyRule)

    @Query("UPDATE auto_reply_rules SET usageCount = usageCount + 1 WHERE id = :id")
    suspend fun incrementRuleUsage(id: Int)


    // --- LOGS ---
    @Query("SELECT * FROM message_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<MessageLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: MessageLog): Long

    @Query("DELETE FROM message_logs")
    suspend fun clearLogs()


    // --- CONTACTS ---
    @Query("SELECT * FROM app_contacts ORDER BY name ASC")
    fun getAllContacts(): Flow<List<AppContact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: AppContact)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(contacts: List<AppContact>)

    @Delete
    suspend fun deleteContact(contact: AppContact)

    @Query("DELETE FROM app_contacts")
    suspend fun clearContacts()
}
