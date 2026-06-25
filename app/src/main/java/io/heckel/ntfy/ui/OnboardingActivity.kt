package io.heckel.ntfy.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.button.MaterialButton
import io.heckel.ntfy.R
import io.heckel.ntfy.util.isDarkThemeOn

class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val darkMode = isDarkThemeOn(this)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = !darkMode

        findViewById<MaterialButton>(R.id.onboarding_btn_start).setOnClickListener {
            markOnboardingDone()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun markOnboardingDone() {
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_ONBOARDING_DONE, true)
            .apply()
    }

    companion object {
        private const val PREFS_NAME = "ntfy_onboarding"
        private const val KEY_ONBOARDING_DONE = "onboarding_done"

        fun isOnboardingDone(context: Context): Boolean {
            return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean(KEY_ONBOARDING_DONE, false)
        }
    }
}
