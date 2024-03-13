package com.example.testtimetonic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testtimetonic.ModelViews.LoginVM
import com.example.testtimetonic.ui.login.LoginView
import com.example.testtimetonic.ui.theme.TestTimeTonicTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val loginViewModel = LoginVM(application)
            val navController = rememberNavController()

            TestTimeTonicTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = ConstantViews.LOGIN_VIEW.route){
                        composable(ConstantViews.LOGIN_VIEW.route){
                            LoginView(viewModel =  loginViewModel, context = baseContext,
                                navToLanding = {navController.navigate(ConstantViews.LANDING_VIEW.route)})
                        }
                        composable(ConstantViews.LANDING_VIEW.route){
                            Column {
                                //TODO Create the Landing Page
                                Text(text = "Landing page")
                            }
                        }
                    }
                }
            }
        }
    }

    enum class ConstantViews(val route: String){
        LOGIN_VIEW("LoginView"),
        LANDING_VIEW("LandingView")
    }
}
