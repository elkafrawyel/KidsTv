package com.cartoons.kids.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Video")
data class VideoDB(
    @PrimaryKey val id: String,
    val name: String,
    val image: String,
    var playlistId: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeString(playlistId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VideoDB> {
        override fun createFromParcel(parcel: Parcel): VideoDB {
            return VideoDB(parcel)
        }

        override fun newArray(size: Int): Array<VideoDB?> {
            return arrayOfNulls(size)
        }
    }
}