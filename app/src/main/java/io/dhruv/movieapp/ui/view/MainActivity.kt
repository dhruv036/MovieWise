package io.dhruv.movieapp.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.dhruv.movieapp.ui.theme.MovieAppTheme
import io.dhruv.movieapp.ui.viewmodel.MainViewModel
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MovieAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = ListScreen){
                    composable<ListScreen> {
                        MovieListScreen(viewModel){
                            navController.navigate(DetailScreen(it))
                        }
                    }
                    composable<DetailScreen> {
                        val args = it.toRoute<DetailScreen>()
                       MovieDetailScreen(viewModel,args.movie){
                           navController.popBackStack(ListScreen, false)
                       }
                    }
                }
            }
        }
    }
}

@Serializable
object ListScreen

@Serializable
data class DetailScreen(
    val movie : Int
)
