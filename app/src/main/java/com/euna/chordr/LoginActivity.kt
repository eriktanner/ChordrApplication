package com.euna.chordr

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button



/**
 * This Class handles everything pertaining to playing
 * sound files
 */
class LoginActivity : AppCompatActivity() {



    private var GoToMainButton: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initializeFindByViews()
        initializeOnClickListeners()
    }


    private fun initializeFindByViews() {
        GoToMainButton = findViewById(R.id.bGoToMainActivity) as Button

    }


    private fun initializeOnClickListeners() {
        GoToMainButton!!.setOnClickListener {
            val PageTransition = Intent(this, MainActivity::class.java)
            startActivity(PageTransition)
        }

    }
}