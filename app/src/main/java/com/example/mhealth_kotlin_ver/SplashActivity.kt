package com.example.mhealth_kotlin_ver

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity  : AppCompatActivity() {
    private val imageview: ImageView by lazy {
        findViewById(R.id.SplashImageView)
    }
    private lateinit var changeImageRunnable: Runnable

    private val imageList = listOf(
        R.drawable.ic_walk_1,
        R.drawable.ic_walk_2,
        R.drawable.ic_walk_3
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler()

        // 이미지 변경 작업을 실행할 Runnable
        changeImageRunnable = Runnable {
            val currentImage = imageview.tag as? Int ?: 0
            val nextImage = (currentImage + 1) % imageList.size
            imageview.setImageResource(imageList[nextImage])
            imageview.tag = nextImage
            handler.postDelayed(changeImageRunnable, IMAGE_DURATION)
        }

        Handler().postDelayed({
            val intent = Intent(this, PersonalAcitivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }, DURATION)

        handler.postDelayed(changeImageRunnable, IMAGE_DURATION)
    }

    companion object {
        const val DURATION : Long = 3000
        const val IMAGE_DURATION: Long = 500
    }

}