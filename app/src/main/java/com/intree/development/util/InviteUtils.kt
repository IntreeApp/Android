package com.intree.development.util

import android.annotation.SuppressLint
import android.content.*
import android.database.Cursor
import android.provider.ContactsContract
import com.intree.development.data.model.ReferContactData

object ReferUtils {
    private const val CONTACT_ID: String = ContactsContract.Contacts._ID
    private const val CONTACT_PHOTO: String = ContactsContract.Contacts.PHOTO_URI
    private const val DISPLAY_NAME: String = ContactsContract.Contacts.DISPLAY_NAME
    private const val HAS_PHONE_NUMBER: String = ContactsContract.Contacts.HAS_PHONE_NUMBER
    private const val PHONE_NUMBER: String = ContactsContract.CommonDataKinds.Phone.NUMBER
    private const val PHONE_CONTACT_ID: String =
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID

    fun getInitials(name: String): String {
        if (name.isEmpty()) return ""
        val initials = StringBuilder()
        name.split(" ").forEach initialsGenerator@{
            if (it.isBlank()) return@initialsGenerator
            if (initials.length > 1) return@initialsGenerator
            initials.append(it.first())
        }
        return initials.toString()
    }

    @SuppressLint("Range")
    fun getContacts(context: Context): List<ReferContactData>? {
        val cr: ContentResolver = context.contentResolver
        val pCur: Cursor? = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(PHONE_NUMBER, PHONE_CONTACT_ID),
            null,
            null,
            null
        )
        val eCur: Cursor? = cr.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID
            ),
            null,
            null,
            null
        )
        if (pCur != null) {
            if (pCur.count > 0) {
                val phones: HashMap<Int, ArrayList<String>> = HashMap()
                while (pCur.moveToNext()) {
                    val contactId: Int = pCur.getInt(pCur.getColumnIndex(PHONE_CONTACT_ID))
                    var curPhones: ArrayList<String> = ArrayList()
                    if (phones.containsKey(contactId)) {
                        curPhones = phones[contactId]!!
                    }
                    val phone =
                        pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    if (phone.length >= 6) {
                        curPhones.add(phone)
                    }
                    phones[contactId] = curPhones
                }

                val cur: Cursor? = cr.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    arrayOf(
                        CONTACT_ID,
                        DISPLAY_NAME,
                        HAS_PHONE_NUMBER,
                        CONTACT_PHOTO
                    ),
                    "$HAS_PHONE_NUMBER > 0",
                    null,
                    "$DISPLAY_NAME ASC"
                )
                if (cur != null) {
                    if (cur.count > 0) {
                        val contacts: ArrayList<ReferContactData> = ArrayList()
                        while (cur.moveToNext()) {
                            val id: Int = cur.getInt(cur.getColumnIndex(CONTACT_ID))
                            if (phones.containsKey(id)) {
                                phones[id]?.forEach {
                                    contacts.add(
                                        ReferContactData(
                                            cur.getString(cur.getColumnIndex(DISPLAY_NAME))
                                                .let { name -> if (name == it) "" else name },
                                            it,
                                            false,
                                            photoUri = cur.getString(
                                                cur.getColumnIndex(
                                                    CONTACT_PHOTO
                                                )
                                            ) ?: ""
                                        )
                                    )
                                }
                            }
                        }
                        return contacts
                    }
                    cur.close()
                }
            }
            pCur.close()
            eCur?.close()
        }
        return null
    }

}
