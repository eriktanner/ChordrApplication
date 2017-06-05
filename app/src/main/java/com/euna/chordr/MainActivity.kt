package com.euna.chordr

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button


class MainActivity : AppCompatActivity() {


    private var bBack: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeFindByViews()
        initializeOnClickListeners()
    }


    private fun initializeFindByViews() {
        bBack = findViewById(R.id.bGoToLogin) as Button
    }


    private fun initializeOnClickListeners() {
        bBack!!.setOnClickListener {
            val PageTransition = Intent(this, LoginActivity::class.java)
            startActivity(PageTransition)
        }
    }
}
