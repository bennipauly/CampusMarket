package com.example.campusmarket.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.campusmarket.ui.addetail.AdDetailScreen
import com.example.campusmarket.ui.adlist.AdListScreen

// Der einzige Ort, an dem navigiert wird. Die Screens bekommen nur Callbacks
// und wissen nicht, wohin es danach geht.
@Composable
fun AppNavHost() {
    // rememberNavController merkt sich den Back-Stack auch über eine Drehung hinweg.
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AdList) {
        composable<AdList> {
            AdListScreen(
                onAdClick = { adId -> navController.navigate(AdDetail(adId)) }
            )
        }
        composable<AdDetail> { backStackEntry ->
            // toRoute() baut aus dem Back-Stack-Eintrag wieder unser AdDetail-Objekt.
            val detail: AdDetail = backStackEntry.toRoute()
            AdDetailScreen(
                adId = detail.adId,
                // navigateUp statt popBackStack: Ein doppelter Tipp auf "Zurück"
                // kann so nie die Startseite vom Stack werfen.
                onBack = { navController.navigateUp() }
            )
        }
    }
}
