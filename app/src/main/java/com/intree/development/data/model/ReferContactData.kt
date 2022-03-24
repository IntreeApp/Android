package com.intree.development.data.model

import android.os.Parcel
import android.os.Parcelable

data class ReferContactData(
    val name: String,
    var phone: String,
    var isChecked: Boolean,
    var statusApprovedRefer: Boolean? = null,
    val isNotData: Boolean = false,
    var photoUri: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        photoUri = parcel.readString() ?: "",
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(phone)
        parcel.writeByte(if (isChecked) 1 else 0)
        parcel.writeValue(statusApprovedRefer)
        parcel.writeValue(photoUri)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReferContactData> {
        override fun createFromParcel(parcel: Parcel): ReferContactData {
            return ReferContactData(parcel)
        }

        override fun newArray(size: Int): Array<ReferContactData?> {
            return arrayOfNulls(size)
        }
    }
}
