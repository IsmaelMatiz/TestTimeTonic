package com.example.testtimetonic.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.testtimetonic.ui.theme.BDarkBlue
import com.example.testtimetonic.ui.theme.BDiffuseDarkBlue
import com.example.testtimetonic.ui.theme.BLightBlue

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LandingView(){
    val contacts =
    listOf<String>(
        "Max",
        "Giovani",
        "Lucas",
        "Alejandro",

    )

    val bookList = listOf<String>(
        "The Hitchhiker's Guide to the Galaxy",
        "Pride and Prejudice",
        "To Kill a Mockingbird",
        "The Lord of the Rings",
        "One Hundred Years of Solitude",
        "Frankenstein",
        "Their Eyes Were Watching God",
        "Dune",
    )

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Text(
                text = "Books",
                color = BLightBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp
            )

            DropdownContacts(title = "choose contact",
                itemList = contacts,
                onItemClick = {}
            )
        }
        Column {
            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp)){
                for (book in bookList){
                    item {
                        BookCard(book,"https://ximg.es/280x280/000/fff")
                    }
                }
            }
        }
    }

}

@Composable
fun BookCard(bookName: String, picUrl: String)
{
    Column(Modifier.padding(start = 5.dp, top = 5.dp)) {
        Column(
            modifier = Modifier.background(color = BDarkBlue, shape = RoundedCornerShape(20.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            AsyncImage(
                model = picUrl,
                contentDescription = "book logo",
                modifier = Modifier.size(180.dp)
                    .clip(RoundedCornerShape(topStart =  20.dp, topEnd = 20.dp))
            )
            Box(modifier = Modifier.width(180.dp).height(60.dp).padding(8.dp)){
                Text(
                    text = bookName,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownContacts(
                title : String,
               itemList : List<String>,
               onItemClick:(String) -> Unit
){

    var showDropdown  by remember { mutableStateOf(false) }
    var chosenOption by remember { mutableStateOf(title) }

    ExposedDropdownMenuBox(expanded = showDropdown,
        onExpandedChange = {showDropdown = it},
        modifier = Modifier.fillMaxWidth(0.7f)) {
        TextField(value = chosenOption,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = showDropdown)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = BDiffuseDarkBlue,
                focusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(topEnd =  20.dp, topStart = 20.dp),
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(expanded = showDropdown,
            onDismissRequest = { showDropdown = false }) {
            for (item in itemList){
                DropdownMenuItem(text = {
                    Text(text = item)
                }, onClick = {
                    onItemClick(item)
                    chosenOption = item
                    showDropdown = false
                })
            }
            }

    }
}
