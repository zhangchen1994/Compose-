package com.chen.composelearn

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.chen.composelearn.ui.theme.ComposeLearnTheme
import com.chen.composelearn.ui.theme.Purple200
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeLearnTheme {
                // A surface container using the 'background' color from the theme
                window.statusBarColor = Purple200.toArgb()
                Scaffold(
                    topBar = {
                        TopAppBar {
                            Text(text = "Compose")
                        }
                    },
                ) {
                    Column(modifier = Modifier.padding(all = 80.dp)) {
                        BarChartMinMax(dataPoints = listOf(3, 5, 7),
                            maxText = { Text(text = "MAX") },
                            minText = { Text(text = "MIN") })
                    }
                }
            }
        }
    }
}

data class Elevations(val card: Dp = 0.dp, val default: Dp = 0.dp)

val LocalElevationsaa = compositionLocalOf { Elevations() }

@Composable
fun Greeting(name: String) {
    var cc = remember {
        mutableStateOf(0)
    }

    val scaffoldState = rememberScaffoldState()
    Scaffold(scaffoldState = scaffoldState) {
        Column {
            Text(text = "Hello $name!")
            ClickCounter(clicks = cc.value, scaffoldState = scaffoldState) {
                cc.value++
            }
            ListComposable(
                myList = arrayListOf(
                    "1", "2", "3", "4", "5", "6"
                )
            )
            val context = LocalContext.current
            NamePicker(header = "zhangchen", names = arrayListOf("1", "2", "4"), onNameClicked = {
                Toast.makeText(context, "点击了 $it", Toast.LENGTH_SHORT).show()
            })
            MoviesScreen(scaffoldState = scaffoldState)
        }
    }
}

@Composable
fun MoviesScreen(scaffoldState: ScaffoldState) {
    val scope = rememberCoroutineScope()
    Button(onClick = {
        scope.launch {
            scaffoldState.snackbarHostState.showSnackbar(
                "消息"
            )
        }
    }) {
        Text(text = "点击")
    }
}

@Composable
fun HelloContent() {
    val values = remember {
        mutableStateOf("")
    }
    Column(modifier = Modifier.padding(20.dp)) {
        Text(
            text = "Hello!",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.h1
        )
        OutlinedTextField(value = values.value, onValueChange = {
            values.value = it
        },
            label = {
                Text(text = "name")
            }
        )
    }
}

@Composable
fun ClickCounter(clicks: Int, scaffoldState: ScaffoldState, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = "已经点击了 $clicks 次")

        if (clicks == 10) {
            LaunchedEffect(scaffoldState) {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "hello",
                    actionLabel = "Retry message",
                )
            }
        }
    }
}

@Composable
fun ListComposable(myList: List<String>) {
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        Column() {
            myList.forEach {
                Text(text = it)
            }
        }
        Text(text = "count = ${myList.size}")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeLearnTheme {
        Greeting("Android")
    }
}