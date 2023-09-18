package com.example.sclink.presentation.screens

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.sclink.R
import com.example.sclink.presentation.screens.foldersActivity.FoldersScreenActivity
import com.example.sclink.utils.Constants.SPLASH_SCREEN_DELAY_TIME

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val animator = ValueAnimator.ofInt(0, 1)
        animator.start()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, FoldersScreenActivity::class.java)
            startActivity(intent)
        }, SPLASH_SCREEN_DELAY_TIME)
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}