package com.example.habitstracker.common

import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDateTime
import java.time.ZoneOffset

class NotificationData(
    val id: Int,
    val title: String,
    val body: String,
    val drawable_id: Int,
    var date: LocalDateTime,
    var isRepeating: Boolean
) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        title = parcel.readString()!!,
        body = parcel.readString()!!,
        drawable_id = parcel.readInt(),
        date = LocalDateTime.ofEpochSecond(parcel.readLong(), 0, ZoneOffset.UTC),
        isRepeating = parcel.readBoolean()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(body)
        parcel.writeInt(drawable_id)
        parcel.writeLong(date.toEpochSecond(ZoneOffset.UTC))
        parcel.writeBoolean(isRepeating)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotificationData> {
        override fun createFromParcel(parcel: Parcel): NotificationData {
            return NotificationData(parcel)
        }

        override fun newArray(size: Int): Array<NotificationData?> {
            return arrayOfNulls(size)
        }
    }

}
