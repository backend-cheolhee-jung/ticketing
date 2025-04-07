package com.example.component

import com.example.util.model.Mail

interface MailSender {
    suspend fun send(mail: Mail)
}