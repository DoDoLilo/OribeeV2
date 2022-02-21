package com.example.oribeev2

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ConfigInfo(var id: Int, var count:Int, var resetSensorBeforeCollecting: Boolean) : Parcelable