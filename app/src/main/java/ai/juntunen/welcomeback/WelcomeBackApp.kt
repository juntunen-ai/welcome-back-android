package ai.juntunen.welcomeback

import android.app.Application

/**
 * Application entry point. Singletons that need a context (LanguageManager,
 * persistence, AI services) are initialised lazily from here.
 */
class WelcomeBackApp : Application() {
    override fun onCreate() {
        super.onCreate()
        LanguageManager.init(applicationContext)
        ai.juntunen.welcomeback.services.NotificationService.createChannel(applicationContext)
    }
}
