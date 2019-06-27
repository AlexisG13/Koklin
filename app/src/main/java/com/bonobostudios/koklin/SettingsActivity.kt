package com.bonobostudios.koklin

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        tv_pp.setMovementMethod(LinkMovementMethod.getInstance())
    }
}
