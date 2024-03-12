package com.example.testtimetonic.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.testtimetonic.R

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginView(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Image(painter = painterResource(id = R.drawable.book_store),
                contentDescription = "appLogo",
                modifier = Modifier.size(360.dp))
            Spacer(modifier = Modifier.padding(16.dp))
            //EmailField(email,{viewModel.onLoginChanged(it, password)})
            Spacer(modifier = Modifier.padding(16.dp))
            //PasswordField(password, {viewModel.onLoginChanged(email, it)})
            Spacer(modifier = Modifier.padding(16.dp))
            //ForgotPassword(modifier = Modifier.align(alignment = Alignment.End))
            Spacer(modifier = Modifier.padding(16.dp))
            /*LoginButton(loginEnable = loginEnable) {
                coroutineScope.launch {
                    viewModel.onLoginSelected()
                }

             */
        }
}

