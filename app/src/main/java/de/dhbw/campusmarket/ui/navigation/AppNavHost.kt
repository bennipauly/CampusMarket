package de.dhbw.campusmarket.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import de.dhbw.campusmarket.ui.addetail.AdDetailRoute
import de.dhbw.campusmarket.ui.adform.AdFormRoute
import de.dhbw.campusmarket.ui.adlist.AdListRoute

// Der einzige Ort, an dem navigiert wird. Die Screens bekommen nur Callbacks
// und wissen nicht, wohin es danach geht.
@Composable
fun AppNavHost() {
    // rememberNavController merkt sich den Back-Stack auch über eine Drehung hinweg.
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AdList) {
        composable<AdList> {
            AdListRoute(
                onAdClick = { adId -> navController.navigate(AdDetail(adId)) },
                onAddClick = { navController.navigate(AdForm) }
            )
        }
        composable<AdDetail> { backStackEntry ->
            // toRoute() baut aus dem Back-Stack-Eintrag wieder unser AdDetail-Objekt.
            val detail: AdDetail = backStackEntry.toRoute()
            AdDetailRoute(
                adId = detail.adId,
                // navigateUp statt popBackStack: Ein doppelter Tipp auf "Zurück"
                // kann so nie die Startseite vom Stack werfen.
                onBack = { navController.navigateUp() }
            )
        }
        composable<AdForm> {
            // Nach dem Speichern zurück zur Liste, dort steht die neue Anzeige oben.
            AdFormRoute(
                onBack = { navController.navigateUp() },
                onSaved = { navController.navigateUp() }
            )
        }
    }
}
