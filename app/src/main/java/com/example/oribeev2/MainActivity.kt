package com.example.oribeev2

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
                    MyApp(SensorViewModel(this))

                }
            }

        }
        checkAuth(this)
    }
    private fun checkAuth(activity: Activity) {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 1
            )
        }
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 1
            )
        }
    }
}

@Composable
fun MyApp(viewModel: SensorViewModel) {

    Scaffold(topBar = {
        OriBeeTopBar(title = "Oribee 数据采集")
    }) {
        //title with tool kit
        Surface(
            modifier = Modifier.padding(horizontal = 12.dp),
            color = MaterialTheme.colors.background
        ) {

            MainContent(viewModel)
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
fun MainContent(model: SensorViewModel) {
    var id by rememberDataSaverState(key = "id", default = 1)
    var count by rememberDataSaverState(key = "count", default = 1)
    Column(
        modifier = Modifier
            .padding(vertical = 20.dp)
            .padding(top = 150.dp)
    ) {
        InfoRow(
            text = "人员序号：",
            decreaseCountHandler = { id -= 1 },
            increaseCountHandler = { id += 1 },
            count = id,
            countEditHandler = {
                id = it
            }
        )
        Divider()
        InfoRow(
            text = "采集序号：",
            count = count,
            decreaseCountHandler = { count -= 1 },
            increaseCountHandler = { count += 1 },
            countEditHandler = {
                count = it
            }
        )
        Divider()
        CollectButton(id, count, model)
    }

}

@Composable
fun ShowDialog(
    title: String,
    count: Int,
    countChangeHandler: (String) -> Unit,
    dismiss: () -> Unit,
    confirm: () -> Unit
) {

    AlertDialog(
        onDismissRequest = dismiss,
        title = {
            Text(text = title)
        },
        text = {
            OutlinedTextField(
                value = count.toString(),
                onValueChange = countChangeHandler,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(top = 5.dp)
            )
        },
        confirmButton = {
            Button(
                onClick = confirm
            ) {
                Text("确认")
            }
        }
    )


}

@Composable
fun CollectButton(id:Int, count: Int,model: SensorViewModel) {
    val isStart = rememberSaveable {
        mutableStateOf(false)
    }
    var isReset by rememberDataSaverState(key = "reset", default = false)
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Switch(checked = isReset, onCheckedChange = {isReset = !isReset},  modifier = Modifier.padding(start = 20.dp))
        Text(text = "采集前重置传感器")
        Spacer(modifier = Modifier.padding(horizontal = 20.dp))
        OutlinedButton(
            onClick = {
                if (isStart.value){
                    model.stop(id, count)
                } else {
                    if (isReset){
                        model.resetSensor()
                    }
                    model.start()

                }
                isStart.value = !isStart.value
            },
            modifier = Modifier
                .wrapContentWidth()
                .padding(vertical = 10.dp)
        ) {
            if (isStart.value){
                Text(text = "结束采集")
            } else {
                Text(text = "开始采集")
            }
        }

    }
}

@Composable
fun InfoRow(
    text: String,
    count: Int,
    decreaseCountHandler: () -> Unit,
    increaseCountHandler: () -> Unit,
    countEditHandler: (Int) -> Unit
) {
    val isDislogShown = rememberSaveable {
        mutableStateOf(false)
    }
    Surface {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal =  20.dp).padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier
                .weight(1f)
                .clickable {
                    isDislogShown.value = true
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
            if (isDislogShown.value) {
                ShowDialog(dismiss = {
                    isDislogShown.value = false
                }, confirm = {
                    isDislogShown.value = false
                }, title = text,
                    count = count,
                    countChangeHandler = {
                        countEditHandler(it.toIntOrNull() ?: 0)
                    }
                )
            }
        }
    }
}
