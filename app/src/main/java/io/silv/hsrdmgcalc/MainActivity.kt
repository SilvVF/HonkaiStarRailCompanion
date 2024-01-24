package io.silv.hsrdmgcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import io.silv.hsrdmgcalc.theme.HsrDmgCalcTheme
import io.silv.hsrdmgcalc.ui.home.HomeScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            HsrDmgCalcTheme {
                Navigator(HomeScreen) {
                    FadeTransition(it)
                }
            }
        }
    }
}


