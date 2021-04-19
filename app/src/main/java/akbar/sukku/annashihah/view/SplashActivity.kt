package akbar.sukku.annashihah.view

import akbar.sukku.annashihah.R
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.os.HandlerCompat.postDelayed


@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        val motion = findViewById<MotionLayout>(R.id.motion_layout)
        motion.startLayoutAnimation()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Looper.myLooper()?.let {
                Handler(it).postDelayed({
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }, 1550)
            }

        } else {
            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 1550)
        }

    }
}