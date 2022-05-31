package com.example.oribeev2

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import androidx.lifecycle.ViewModel

class SensorViewModel(private val context: Context): ViewModel() {

    private var sensorBee: SensorBee =
        sensorBee(sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager) {
            frequency = 200
            sensorTypes(
                arrayOf(
                    Sensor.TYPE_ACCELEROMETER,
                    Sensor.TYPE_GYROSCOPE,
                    Sensor.TYPE_GAME_ROTATION_VECTOR,
                    Sensor.TYPE_ROTATION_VECTOR,
                    Sensor.TYPE_GYROSCOPE_UNCALIBRATED,
                    Sensor.TYPE_ORIENTATION,
                    Sensor.TYPE_MAGNETIC_FIELD,
                    Sensor.TYPE_GRAVITY,
                    Sensor.TYPE_LINEAR_ACCELERATION,
                )
            )
            registerSensors()

            addDataChangedListener {
                // no data would be shown, now
            }
        }
    fun start(){
        sensorBee.startRecord(0L)
    }

    fun resetSensor(){
        sensorBee.resetSensors()
    }

    private fun stop(filePath:String){
        sensorBee.stopRecordAndSave(filePath)
    }
    fun stop(id:Int, count:Int){
        val filePath = "${context.externalCacheDir}/IMU-$id-$count-${sensorBee.headingAngles} ${Build.MODEL}.csv"
        stop(filePath)
    }
}