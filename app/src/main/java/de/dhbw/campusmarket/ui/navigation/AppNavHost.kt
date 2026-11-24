package de.dhbw.campusmarket.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import de.dhbw.campusmarket.ui.addetail.AdDetailRoute
import de.dhbw.campusmarket.ui.adform.AdFormRoute
import de.dhbw.campusmarket.ui.adlist.AdListRoute
import de.dhbw.campusmarket.ui.photoviewer.PhotoViewerRoute

// The only place where navigation happens. The screens only get callbacks
// and do not know where to go next.
@Composable
fun AppNavHost() {
    // rememberNavController keeps the back stack across a rotation.
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AdList) {
        composable<AdList> {
            AdListRoute(
                onAdClick = { adId -> navController.navigate(AdDetail(adId)) },
                onAddClick = { navController.navigate(AdForm) }
            )
        }
        composable<AdDetail> { backStackEntry ->
            // toRoute() turns the back stack entry back into our AdDetail object.
            val detail: AdDetail = backStackEntry.toRoute()
            AdDetailRoute(
                adId = detail.adId,
                // navigateUp instead of popBackStack: a double tap on "back"
                // can never remove the start screen from the stack this way.
                onBack = { navController.navigateUp() },
                onPhotoClick = { navController.navigate(PhotoViewer(detail.adId)) },
                onEditClick = { navController.navigate(AdEdit(detail.adId)) }
            )
        }
        composable<PhotoViewer> { backStackEntry ->
            val photo: PhotoViewer = backStackEntry.toRoute()
            PhotoViewerRoute(
                adId = photo.adId,
                onBack = { navController.navigateUp() }
            )
        }
        composable<AdForm> {
            // After saving, back to the list, where the new ad is at the top.
            AdFormRoute(
                adId = null,
                onBack = { navController.navigateUp() },
                onSaved = { navController.navigateUp() }
            )
        }
        composable<AdEdit> { backStackEntry ->
            val edit: AdEdit = backStackEntry.toRoute()
            // After saving, back to the detail screen. It reads from Room and immediately shows
            // the changed values.
            AdFormRoute(
                adId = edit.adId,
                onBack = { navController.navigateUp() },
                onSaved = { navController.navigateUp() }
            )
        }
    }
}
