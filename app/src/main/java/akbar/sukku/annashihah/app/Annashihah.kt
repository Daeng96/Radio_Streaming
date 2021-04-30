package akbar.sukku.annashihah.app

import akbar.sukku.annashihah.R
import akbar.sukku.annashihah.utils.ThemeHelper
import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Annashihah : Application() {

    override fun onCreate() {
        super.onCreate()
        val sharedPreferences : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val theme = sharedPreferences.getString(getString(R.string.theme_key), "system_theme") as String
        ThemeHelper().applyTheme(theme)
    }
}