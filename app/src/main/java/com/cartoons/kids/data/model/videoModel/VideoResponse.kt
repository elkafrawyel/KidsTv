package com.cartoons.kids.data.model.videoModel

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class VideoResponse(
    @field:Json(name = "etag")
    val etag: String,
    @field:Json(name = "items")
    val videoItems: List<VideoItem>,
    @field:Json(name = "kind")
    val kind: String,
    @field:Json(name = "nextPageToken")
    val nextPageToken: String,
    @field:Json(name = "pageInfo")
    val pageInfo: PageInfo
)

data class VideoItem(
    @field:Json(name = "etag")
    val etag: String?,
    @field:Json(name = "id")
    val id: String?,
    @field:Json(name = "kind")
    val kind: String?,
    @field:Json(name = "snippet")
    val snippet: Snippet?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Snippet::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(etag)
        parcel.writeString(id)
        parcel.writeString(kind)
        parcel.writeParcelable(snippet, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VideoItem> {
        override fun createFromParcel(parcel: Parcel): VideoItem {
            return VideoItem(parcel)
        }

        override fun newArray(size: Int): Array<VideoItem?> {
            return arrayOfNulls(size)
        }
    }
}

data class Snippet(
    @field:Json(name = "channelId")
    val channelId: String?,
    @field:Json(name = "channelTitle")
    val channelTitle: String?,
    @field:Json(name = "description")
    val description: String?,
    @field:Json(name = "playlistId")
    val playlistId: String?,
    @field:Json(name = "position")
    val position: Int,
    @field:Json(name = "publishedAt")
    val publishedAt: String?,
    @field:Json(name = "resourceId")
    val resourceId: ResourceId?,
    @field:Json(name = "thumbnails")
    val thumbnails: Thumbnails?,
    @field:Json(name = "title")
    val title: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readParcelable(ResourceId::class.java.classLoader),
        parcel.readParcelable(Thumbnails::class.java.classLoader),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(channelId)
        parcel.writeString(channelTitle)
        parcel.writeString(description)
        parcel.writeString(playlistId)
        parcel.writeInt(position)
        parcel.writeString(publishedAt)
        parcel.writeParcelable(resourceId, flags)
        parcel.writeParcelable(thumbnails, flags)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Snippet> {
        override fun createFromParcel(parcel: Parcel): Snippet {
            return Snippet(parcel)
        }

        override fun newArray(size: Int): Array<Snippet?> {
            return arrayOfNulls(size)
        }
    }
}

data class ResourceId(
    @field:Json(name = "kind")
    val kind: String?,
    @field:Json(name = "videoId")
    val videoId: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(kind)
        parcel.writeString(videoId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResourceId> {
        override fun createFromParcel(parcel: Parcel): ResourceId {
            return ResourceId(parcel)
        }

        override fun newArray(size: Int): Array<ResourceId?> {
            return arrayOfNulls(size)
        }
    }
}

data class Thumbnails(
    @field:Json(name = "default")
    val default: Default?,
    @field:Json(name = "high")
    val high: High?,
    @field:Json(name = "medium")
    val medium: Medium?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Default::class.java.classLoader),
        parcel.readParcelable(High::class.java.classLoader),
        parcel.readParcelable(Medium::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(default, flags)
        parcel.writeParcelable(high, flags)
        parcel.writeParcelable(medium, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Thumbnails> {
        override fun createFromParcel(parcel: Parcel): Thumbnails {
            return Thumbnails(parcel)
        }

        override fun newArray(size: Int): Array<Thumbnails?> {
            return arrayOfNulls(size)
        }
    }
}

data class Default(
    @field:Json(name = "height")
    val height: Int,
    @field:Json(name = "url")
    val url: String?,
    @field:Json(name = "width")
    val width: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(height)
        parcel.writeString(url)
        parcel.writeInt(width)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Default> {
        override fun createFromParcel(parcel: Parcel): Default {
            return Default(parcel)
        }

        override fun newArray(size: Int): Array<Default?> {
            return arrayOfNulls(size)
        }
    }
}

data class High(
    @field:Json(name = "height")
    val height: Int,
    @field:Json(name = "url")
    val url: String?,
    @field:Json(name = "width")
    val width: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(height)
        parcel.writeString(url)
        parcel.writeInt(width)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<High> {
        override fun createFromParcel(parcel: Parcel): High {
            return High(parcel)
        }

        override fun newArray(size: Int): Array<High?> {
            return arrayOfNulls(size)
        }
    }
}

data class Medium(
    @field:Json(name = "height")
    val height: Int,
    @field:Json(name = "url")
    val url: String?,
    @field:Json(name = "width")
    val width: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(height)
        parcel.writeString(url)
        parcel.writeInt(width)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Medium> {
        override fun createFromParcel(parcel: Parcel): Medium {
            return Medium(parcel)
        }

        override fun newArray(size: Int): Array<Medium?> {
            return arrayOfNulls(size)
        }
    }
}

data class PageInfo(
    @field:Json(name = "resultsPerPage")
    val resultsPerPage: Int,
    @field:Json(name = "totalResults")
    val totalResults: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(resultsPerPage)
        parcel.writeInt(totalResults)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PageInfo> {
        override fun createFromParcel(parcel: Parcel): PageInfo {
            return PageInfo(parcel)
        }

        override fun newArray(size: Int): Array<PageInfo?> {
            return arrayOfNulls(size)
        }
    }
}