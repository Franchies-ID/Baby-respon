package com.example.ui

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

object Locales {
    val en = mapOf(
        "dashboard" to "Dashboard",
        "rules" to "Keywords",
        "logs" to "Live Logs",
        "contacts" to "Contacts",
        "settings" to "Settings",
        "total_messages" to "Total Inbound Messages",
        "sent_replies" to "Auto-Replies Sent",
        "match_rate" to "Keywords Match Rate",
        "service_status" to "Responder Service",
        "active" to "ACTIVE",
        "inactive" to "DISABLED",
        "performance_chart" to "Analytics Performance",
        "recent_activity" to "Recent Activity Logs",
        "no_activity" to "No logs recorded yet. Use simulation form below to test!",
        "add_rule" to "Add Keyword Rule",
        "edit_rule" to "Edit Keyword Rule",
        "keyword" to "Keyword (Kata Kunci)",
        "reply_message" to "Reply Message",
        "category" to "Category / Grouping",
        "match_type" to "Match Option",
        "language" to "Message Language",
        "save" to "Save Rule Setting",
        "delete" to "Delete Rule",
        "search_logs" to "Filter message logs...",
        "clear_history" to "Clear Output Logs",
        "client_groups" to "Contact Category Groups",
        "sync_api" to "Sync Contacts via 3rd-Party API",
        "syncing" to "Syncing...",
        "sync_success" to "Successfully synced contacts from API!",
        "sync_failed" to "Sync failed, using offline data fallback",
        "simulate_msg" to "Simulate Incoming WhatsApp Notification",
        "simulate_btn" to "Simulate Inbound Message",
        "sender_name" to "Sender Name (eg. Bagas)",
        "sender_phone" to "Sender WA Number",
        "incoming_text" to "Message Content (try 'halo', 'info', or 'harga')",
        "webhook_settings" to "3rd-Party API Webhook Settings",
        "webhook_url" to "Webhook HTTP POST Destination URL",
        "webhook_enable" to "Enable Outbound Trigger Webhooks",
        "language_choice" to "Interface Language",
        "connected_3rd_party" to "Local & Sync Contacts Database",
        "matches_count" to "times matched",
        "no_rules" to "No custom autorespond keywords found. Click plus to add!",
        "whatsapp_auto_responder" to "WhatsApp Autoresponder",
        "status" to "Trigger Status",
        "matched" to "Matched Rule",
        "add_contact" to "Add Contact Card",
        "sync_endpoint" to "Contact Retrieval API Endpoint",
        "sync_now" to "Sync Contacts DB Now",
        "active_rules_perf" to "Keyword Hot List & Perform"
    )

    val id = mapOf(
        "dashboard" to "Dashboard",
        "rules" to "Kata Kunci",
        "logs" to "Riwayat Log",
        "contacts" to "Kontak",
        "settings" to "Pengaturan",
        "total_messages" to "Pesan Masuk Masuk",
        "sent_replies" to "Balasan Otomatis Terkirim",
        "match_rate" to "Rasio Cocok Kata Kunci",
        "service_status" to "Status Layanan Autorespond",
        "active" to "AKTIF",
        "inactive" to "NONAKTIF",
        "performance_chart" to "Statistik Performa Balasan",
        "recent_activity" to "Riwayat Transaksi Pesan Masuk",
        "no_activity" to "Belum ada log pesan. Gunakan simulasi di bawah untuk menguji!",
        "add_rule" to "Tambah Aturan Kata Kunci",
        "edit_rule" to "Edit Aturan Kata Kunci",
        "keyword" to "Kata Kunci (Kombinasi Huruf)",
        "reply_message" to "Pesan Balasan Otomatis",
        "category" to "Kategori Pengelompokan",
        "match_type" to "Tipe Pencocokan Kata",
        "language" to "Bahasa Penerima",
        "save" to "Simpan Aturan",
        "delete" to "Hapus Aturan",
        "search_logs" to "Cari log pesan...",
        "clear_history" to "Bersihkan Semua Log",
        "client_groups" to "Pengelompokan Kontak Pengguna",
        "sync_api" to "Hubungkan API Sinkronisasi Kontak",
        "syncing" to "Menyinkronkan...",
        "sync_success" to "Sukses menyinkronkan data kontak dari API!",
        "sync_failed" to "Koneksi API gagal, menggunakan data offline lokal",
        "simulate_msg" to "Simulasikan Pesan Masuk WhatsApp",
        "simulate_btn" to "Kirim Simulasi Pesan Masuk",
        "sender_name" to "Nama Pengirim (misal: Bagas)",
        "sender_phone" to "Nomor WA Pengirim",
        "incoming_text" to "Isi Pesan WhatsApp (coba 'halo', 'info', atau 'harga')",
        "webhook_settings" to "Integrasi Webhook API Pihak Ketiga",
        "webhook_url" to "URL Tujuan POST Webhook API",
        "webhook_enable" to "Aktifkan Kiriman Webhook Otomatis",
        "language_choice" to "Bahasa Menu Utama",
        "connected_3rd_party" to "Database Kontak (Sinkron API)",
        "matches_count" to "kali terpicu",
        "no_rules" to "Aturan kata kunci kosong. Klik tombol + untuk menambahkan!",
        "whatsapp_auto_responder" to "WhatsApp Autoresponder",
        "status" to "Status Balasan",
        "matched" to "Kecocokan Kata",
        "add_contact" to "Tambah Data Kontak",
        "sync_endpoint" to "Endpoint API Client Pihak Ketiga",
        "sync_now" to "Sinkronkan Kontak Sekarang",
        "active_rules_perf" to "Skor Aktivitas Aturan Kata Kunci"
    )

    fun get(key: String, lang: String): String {
        return if (lang == "id") {
            id[key] ?: en[key] ?: key
        } else {
            en[key] ?: key
        }
    }
}

class AutoResponderViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AutoResponderRepository

    // Flows observed by Compose (Using StateIn for safety)
    val rules: StateFlow<List<AutoReplyRule>>
    val logs: StateFlow<List<MessageLog>>
    val contacts: StateFlow<List<AppContact>>

    // Live App Preferences/Configuration states
    val isServiceEnabled = mutableStateOf(true)
    val currentLanguage = mutableStateOf("id") // default Bahasa Indonesia
    val mockApiEndpoint = mutableStateOf("")
    val isWebhookEnabled = mutableStateOf(false)
    val webHookUrl = mutableStateOf("")

    // API Sync Progress Tracker
    val isSyncingContacts = mutableStateOf(false)
    val lastSyncStatusMessage = mutableStateOf<String?>(null)

    init {
        val database = AppDatabase.getDatabase(application)
        repository = AutoResponderRepository(database.dao(), application)

        isServiceEnabled.value = repository.isServiceEnabled()
        currentLanguage.value = repository.getLanguageCode()
        webHookUrl.value = repository.getWebhookUrl()
        isWebhookEnabled.value = repository.isWebhookEnabled()

        rules = repository.allRules.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        logs = repository.allLogs.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        contacts = repository.allContacts.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Seed initial rules & logs on background startup
        viewModelScope.launch {
            repository.seedInitialDataIfNecessary()
        }
    }

    // --- TRANSLATION HELPER ---
    fun translate(key: String): String {
        return Locales.get(key, currentLanguage.value)
    }

    // --- SERVICE ACTION CONTROLS ---
    fun toggleService(enabled: Boolean) {
        isServiceEnabled.value = enabled
        repository.setServiceEnabled(enabled)
    }

    fun changeLanguage(langCode: String) {
        currentLanguage.value = langCode
        repository.setLanguageCode(langCode)
    }

    fun updateWebhookSettings(enabled: Boolean, url: String) {
        isWebhookEnabled.value = enabled
        webHookUrl.value = url
        repository.setWebhookEnabled(enabled)
        repository.setWebhookUrl(url)
    }

    // --- RULE LOGIC CRUD ---
    fun addOrUpdateRule(rule: AutoReplyRule) {
        viewModelScope.launch {
            if (rule.id == 0) {
                repository.insertRule(rule)
            } else {
                repository.updateRule(rule)
            }
        }
    }

    fun deleteRule(rule: AutoReplyRule) {
        viewModelScope.launch {
            repository.deleteRule(rule)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearLogs()
        }
    }

    // --- CONTACT OPERATIONS & THIRD-PARTY INTEGRATION ---
    fun addContact(contact: AppContact) {
        viewModelScope.launch {
            repository.insertContact(contact)
        }
    }

    fun deleteContact(contact: AppContact) {
        viewModelScope.launch {
            repository.deleteContact(contact)
        }
    }

    fun syncContactsFromThirdParty() {
        viewModelScope.launch {
            isSyncingContacts.value = true
            lastSyncStatusMessage.value = null
            
            val result = repository.fetchExternalContacts(mockApiEndpoint.value)
            isSyncingContacts.value = false
            
            if (result.isSuccess) {
                lastSyncStatusMessage.value = translate("sync_success")
            } else {
                lastSyncStatusMessage.value = "${translate("sync_failed")}: ${result.exceptionOrNull()?.message}"
            }
        }
    }

    // --- INBOUND SIMULATION TRIGGER ---
    fun simulateIncomingWhatsAppMessage(
        senderName: String,
        senderNumber: String,
        messageContent: String
    ) {
        viewModelScope.launch {
            // Trim inputs
            val name = senderName.ifBlank { "Client Bagas" }
            val phone = senderNumber.ifBlank { "+6281010102020" }
            val text = messageContent.ifBlank { "Halo, mau tanya info layanan." }

            // Process message into the autoresponder logic
            repository.processIncomingMessage(phone, name, text)
        }
    }
}
