package com.intree.development.util

import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import com.google.gson.Gson
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*


object UrlBuilder {
    private const val mainInviteUrl = "https://invite.intree.com"
    fun getInviteUrl(uid: String): String {

        val currentTimeMillis = System.currentTimeMillis()
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
        val token = dateFormatter.format(currentTimeMillis)

//        val uidHash = HashMap<String, String>()
//        uidHash["id"] = uid
//        uidHash["token"] = token
        val decrypt ="vqdsWuzqJghqN7x4Vtn3oA8Ds4xEB+DkqMfiLqLk6anc5yKxKrK9RiFHDXqbR+BSXSDRO53y24o6FTAzcHQ856T3Bw3kLUYmbasvGXYzTcxQX6LVvTMPjGLdjA4m"
        val id = AESCipher(
            "teamAwesomeUnify",
            "unifyAwesomeTeam"
        ).encrypt("RqgJUlydyhZxRGuvyNLZRzJs4Le2")

        val tokenEncr = AESCipher(
            "teamAwesomeUnify",
            "unifyAwesomeTeam"
        ).encrypt(token)

//        val encodedString = Gson().toJson(uidHash)
//        val ee = JSONObject(encodedString).toString()

        val builtUri: Uri = Uri.parse(mainInviteUrl)
            .buildUpon()
            .appendQueryParameter("id", id+tokenEncr)
            .build()

       // return builtUri.toString()
        return AESCipher(
            "teamAwesomeUnify",
            "unifyAwesomeTeam").decrypt(decrypt).toString()
    }

}