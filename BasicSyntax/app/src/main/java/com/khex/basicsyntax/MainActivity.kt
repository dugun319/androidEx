package com.khex.basicsyntax

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val myName  = "Smith"
        var myAge   = 44
        myAge       += 1

        var out     = 0
        val strike  = 3
        if(strike < 2) {
            out += 1
        } else {
            out = -10
        }



//        Log.d("BasicSyntax", "Print Out Log with Log.d ")
        Log.d("BasicSyntax", "myName = $myName & $myName(공백), myAge = $myAge, out = ${out+1}, out = $out+1")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}