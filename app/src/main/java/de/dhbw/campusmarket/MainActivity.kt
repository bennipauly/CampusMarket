package de.dhbw.campusmarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import de.dhbw.campusmarket.ui.navigation.AppNavHost
import de.dhbw.campusmarket.ui.theme.CampusMarketTheme

// The only activity of the app. It only sets the theme and hands over to the NavHost.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusMarketTheme {
                AppNavHost()
            }
        }
    }
}
