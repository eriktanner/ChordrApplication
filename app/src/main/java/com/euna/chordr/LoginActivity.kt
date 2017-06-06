package com.euna.chordr

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button


class LoginActivity : AppCompatActivity() {


    private var bBack: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initializeFindByViews()
        initializeOnClickListeners()
    }


    private fun initializeFindByViews() {
        bBack = findViewById(R.id.bGoToLogin) as Button
    }


    private fun initializeOnClickListeners() {
        bBack!!.setOnClickListener {
            val PageTransition = Intent(this, MainActivity::class.java)
            startActivity(PageTransition)
        }
    }
}
