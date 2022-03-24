package com.intree.development.util

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayList

fun getPhoneCodeByCountryISO(context: Context, countryCode: String): String? {
    try {
        val file: InputStream = context.resources.assets.open("PhoneCountries.txt")
        val reader = BufferedReader(InputStreamReader(file))
        var line: String? = reader.readLine()
        while (line != null) {
            line = reader.readLine()
            val subCodes: List<String> = if (line == null || line.isEmpty()) ArrayList() else line.split(";")
            subCodes.forEach {
                if (it == countryCode) {
                    Log.d("==== CODE CHECK", "FOUND CODE INFO: $line")
//                    reader.close()
//                    file.close()
                    return subCodes[0]
                }
            }
        }
    } catch (ioe: IOException) {
        ioe.printStackTrace()
        return null
    }
    return null
}

fun getCountryISOByPhoneCode(context: Context, phoneCode: String): String? {
    try {
        val file: InputStream = context.resources.assets.open("PhoneCountries.txt")
        val reader = BufferedReader(InputStreamReader(file))
        var line: String? = reader.readLine()
        while (line != null) {
            line = reader.readLine()
            val subCodes: List<String> = if (line == null || line.isEmpty()) ArrayList() else line.split(";")
            subCodes.forEach {
                if (it == phoneCode) {
                    Log.d("==== AUTH", "FOUND CODE INFO: $line")
//                    reader.close()
//                    file.close()
                    return subCodes[1].toUpperCase(Locale.getDefault())
                }
            }
        }
    } catch (ioe: IOException) {
        ioe.printStackTrace()
        return null
    }
    return null
}