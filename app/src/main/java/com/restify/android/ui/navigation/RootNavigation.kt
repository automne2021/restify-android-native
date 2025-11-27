import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambdaN
import androidx.core.splashscreen.SplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.restify.android.ui.screens.splash.SplashScreen
import com.restify.android.ui.screens.welcome.WelcomeScreen
import com.restify.android.ui.navigation.MainScreen
import com.restify.android.ui.navigation.RootScreen

@Composable
fun RootNavigationGraph() {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = RootScreen.Splash.route
    ) {
        // Splash Screen
        composable(
            route = RootScreen.Splash.route,
            exitTransition = { fadeOut() }
        ) {
            SplashScreen(
                onSplashFinished = {
                    rootNavController.navigate(RootScreen.Welcome.route) {
                        popUpTo(RootScreen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Welcome Screen
        composable(
            route = RootScreen.Welcome.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            WelcomeScreen(
                onGetStartedClick = {
                    rootNavController.navigate(RootScreen.Main.route) {
                        popUpTo(RootScreen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        // Main Screen
        composable(
            route = RootScreen.Main.route,
            enterTransition = { fadeIn() }
        ) {
            MainScreen()
        }
    }
}