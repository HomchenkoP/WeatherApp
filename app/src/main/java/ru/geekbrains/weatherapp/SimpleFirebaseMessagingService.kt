package ru.geekbrains.weatherapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.*
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class SimpleFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val PUSH_KEY_TITLE = "title"
        private const val PUSH_KEY_MESSAGE = "message"
        private const val CHANNEL_ID = "channel_id"
        private const val NOTIFICATION_ID = 37

        fun logToken() {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("", "Неудалось получить FCM registration token", task.exception)
                    return@OnCompleteListener
                }
                val token = task.result
                Log.d("", "FCM registration token успешно получен : $token")
            })
                .addOnSuccessListener { token: String ->
                    if (!TextUtils.isEmpty(token)) {
                        Log.d("", "FCM token успешно получен : $token")
                    } else {
                        Log.w("", "FCM token не должен быть пустым")
                    }
                }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val remoteMessageData = remoteMessage.data
        if (remoteMessageData.isNotEmpty()) {
            handleDataMessage(remoteMessageData.toMap())
        } else if (remoteMessage.notification != null) {
            showNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
        }
    }

    private fun handleDataMessage(data: Map<String, String>) {
        val title = data[PUSH_KEY_TITLE]
        val message = data[PUSH_KEY_MESSAGE]
        if (!title.isNullOrBlank() && !message.isNullOrBlank()) {
            showNotification(title, message)
        }
    }

    private fun showNotification(title: String, message: String) {
        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
                setSmallIcon(R.drawable.ic_kotlin_icon)
                setContentTitle(title)
                setContentText(message)
                priority = NotificationCompat.PRIORITY_DEFAULT
            }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val name = "Channel name"
        val descriptionText = "Channel description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i("newToken", token)
        //Отправить токен на сервер
    }
}