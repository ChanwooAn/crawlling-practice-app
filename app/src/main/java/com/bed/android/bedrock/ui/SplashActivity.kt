package com.bed.android.bedrock.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bed.android.bedrock.R
import com.bed.android.bedrock.di.Injector
import com.bed.android.bedrock.ui.guide.GuideActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        CoroutineScope(Dispatchers.Main).launch {
            delay(1200L)

            val sharedPreferences = Injector.getSharedPreferences()

            sharedPreferences.getString("isFirst", null)?.let {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } ?: run {
                val editor = sharedPreferences.edit()
                editor.putString("isFirst", "true")
                startActivity(Intent(this@SplashActivity, GuideActivity::class.java))
                editor.commit()
            }

            finish()
        }
    }

}
