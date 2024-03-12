package com.example.testtimetonic.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.testtimetonic.R
import com.example.testtimetonic.ui.theme.BDarkBlue
import com.example.testtimetonic.ui.theme.BDiffuseDarkBlue

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
                modifier = Modifier.size(300.dp))
            Spacer(modifier = Modifier.padding(16.dp))
            LoginField("email",LoginType.EMAIL,{/*TODO call the logic on the modelview*/})
            Spacer(modifier = Modifier.padding(16.dp))
            LoginField("Password",LoginType.PASSWORD,{/*TODO call the logic on the modelview*/})
            Spacer(modifier = Modifier.padding(16.dp))
            LoginButton(loginEnable = false/*TODO call the logic on the modelview*/) {
                /*TODO call the logic on the modelview*/
            }
    }
}

@Composable
fun LoginButton(loginEnable: Boolean, onLoginSelected: () -> Unit) {
    Row(Modifier.padding(15.dp)) {
        Button(
            onClick = { onLoginSelected() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BDarkBlue,
                disabledContainerColor = BDiffuseDarkBlue,
                contentColor = Color.White,
                disabledContentColor = Color.White
            ), enabled = loginEnable
        ) {
            Text(text = "Iniciar sesión")
        }
    }
}

@Composable
private fun LoginField(value: String, loginType: LoginType, onTextFieldChanged: (String) ->Unit){
    var keyboardType = when(loginType){
        LoginType.EMAIL -> KeyboardType.Email
        else -> KeyboardType.Password
    }
    Row(Modifier.padding(15.dp)) {
        TextField(
            value = value
            , onValueChange = {onTextFieldChanged(it)}
            , modifier = Modifier.fillMaxWidth()
            , placeholder = { Text(text = "Email")}
            , keyboardOptions = KeyboardOptions(keyboardType =  keyboardType)
            , singleLine = true
            , maxLines = 1
            , colors = TextFieldDefaults.colors(
                cursorColor =  Color(0xFF636262),
                unfocusedTextColor = Color(0xFF636262),
                unfocusedContainerColor = Color.White)
        )
    }
}

private enum class LoginType{
    EMAIL, PASSWORD
}


