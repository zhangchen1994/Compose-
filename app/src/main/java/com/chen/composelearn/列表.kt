package com.chen.composelearn

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

val contactList = arrayListOf<String>(
    "apple", "ams", "amg", "bnj", "bss",
    "asm", "boss", "cat", "bac", "chh",
    "dfv","zhg","acdd","nbg","mnbg",
    "nbhm","css","html","java","jar"
)

@Composable
fun LazyColumnTest() {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 20.dp,vertical = 15.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ){
        item {
            Text(text = "LazyColumn ")
        }
        items(100) {index ->
            Text(text = "LazyColumn = $index")
        }
        item {
            Text(text = "Last item")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactsList() {
    val group = contactList.groupBy {
        it.subSequence(0,1).toString()
    }

    group.forEach {
        Log.e("zhangchen", it.key)
        Log.e("zhangchen", it.value.joinToString())
    }

    LazyColumn(content = {
        group.forEach {
            stickyHeader {
                RowTitle(content = it.key)
            }
            items(it.value){
                RowContent(content = it)
            }
        }
    }, verticalArrangement = Arrangement.spacedBy(8.dp))
}

@Composable
fun RowTitle(content:String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(color = Color.Gray)
        .padding(all = 5.dp)) {
        Text(text = content)
    }
}

@Composable
fun RowContent(content:String) {
    Card(modifier = Modifier
        .fillMaxWidth().clickable {

        }) {
        Text(text = content, modifier = Modifier.padding(8.dp))
    }
}