package de.dhbw.campusmarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import de.dhbw.campusmarket.ui.navigation.AppNavHost
import de.dhbw.campusmarket.ui.theme.CampusMarketTheme

// Einzige Activity der App. Sie setzt nur das Theme und übergibt an den NavHost.
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
