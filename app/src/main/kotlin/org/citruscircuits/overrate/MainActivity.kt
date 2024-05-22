package org.citruscircuits.overrate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import org.citruscircuits.overrate.ui.NavGraphs
import org.citruscircuits.overrate.ui.theme.OverRateTheme

/**
 * The app's main activity.
 */
class MainActivity : ComponentActivity() {
    /**
     * The activity's [ViewModel].
     */
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // use custom splash screen
        installSplashScreen()
        super.onCreate(savedInstanceState)
        // full screen
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // load the app state
        viewModel.load(context = applicationContext)
        // show content
        setContent {
            // theme
            OverRateTheme {
                // background
                Surface(modifier = Modifier.fillMaxSize()) {
                    // navigation
                    DestinationsNavHost(
                        navGraph = NavGraphs.main,
                        // supply view model to destinations
                        dependenciesContainerBuilder = { dependency(viewModel) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
