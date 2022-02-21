package com.example.oribeev2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.oribeev2.ui.theme.OribeeV2Theme
import com.funny.data_saver.core.DataSaverPreferences
import com.funny.data_saver.core.DataSaverPreferences.Companion.setContext
import com.funny.data_saver.core.LocalDataSaver
import com.funny.data_saver.core.rememberDataSaverState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataSaverPreferences = DataSaverPreferences().apply {
            setContext(context = applicationContext)
        }
        setContent {
            CompositionLocalProvider(LocalDataSaver provides dataSaverPreferences) {
                OribeeV2Theme {
                    // A surface container using the 'background' color from the theme
                    MyApp()

                }
            }

        }
    }
}

@Composable
fun MyApp() {

    Scaffold(topBar = {
        OriBeeTopBar(title = "Oribee 数据采集")
    }) {
        //title with tool kit
        Surface(
            modifier = Modifier.padding(horizontal = 12.dp),
            color = MaterialTheme.colors.background
        ) {

            MainContent()
        }
    }

}

@Composable
fun OriBeeTopBar(title: String) {
    TopAppBar(
        title = { Text(text = title) },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.MoreVert, contentDescription = null)
            }
        }
    )
}

@Composable
fun MainContent() {
    var id by rememberDataSaverState(key = "id", default = 1)
    var count by rememberDataSaverState(key = "count", default = 1)
    val isDislogShown = rememberSaveable {
        mutableStateOf(false)
    }

    Column(modifier = Modifier.padding(vertical = 20.dp)) {
        InfoRow(
            text = "人员序号：",
            decreaseCountHandler = { id -= 1 },
            increaseCountHandler = { id += 1 },
            count = id
        ) {
            isDislogShown.value = true
        }
        Divider()
        InfoRow(
            text = "采集序号：",
            count = count,
            decreaseCountHandler = { count -= 1 },
            increaseCountHandler = { count += 1 }
        ) {

        }
        Divider()
        CollectButton {

        }
        if (isDislogShown.value){
            showDialog(dismiss = {
                isDislogShown.value = false
            }, confirm = {
                isDislogShown.value = false
            })
        }
    }

}

@Composable
fun showDialog(dismiss: () -> Unit, confirm: () -> Unit) {

    AlertDialog(
        onDismissRequest = dismiss,
        title = {
            Text(text = "Dialog Title")
        },
        text = {
            Text("Here is a text ")
        },
        confirmButton = {
            Button(

                onClick = confirm) {
                Text("This is the Confirm Button")
            }
        },
        dismissButton = {
            Button(

                onClick = dismiss) {
                Text("This is the dismiss Button")
            }
        }
    )


}

@Composable
fun CollectButton(handler: () -> Unit = {}) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        color = MaterialTheme.colors.primary
    ) {
        OutlinedButton(
            onClick = {
                handler()
            },
            modifier = Modifier
                .wrapContentWidth()
                .padding(vertical = 10.dp)
        ) {
            Text(text = "开始采集")
        }

    }
}

@Composable
fun InfoRow(
    text: String,
    count: Int,
    decreaseCountHandler: () -> Unit,
    increaseCountHandler: () -> Unit,
    clickHandler: () -> Unit = {}
) {
    Surface(color = MaterialTheme.colors.primary) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier
                .weight(1f)
                .clickable {
                    clickHandler()
                }) {
                Text(text = text)
                Text(text = "$count")
            }

            OutlinedButton(
                onClick = decreaseCountHandler,
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text(text = "-1")
            }
            OutlinedButton(
                onClick = increaseCountHandler,
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text(text = "+1")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OribeeV2Theme {
        MyApp()
    }
}