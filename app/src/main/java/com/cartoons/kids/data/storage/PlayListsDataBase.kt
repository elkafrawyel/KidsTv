package com.cartoons.kids.data.storage

import com.cartoons.kids.data.model.ChannelModel
import java.util.*
import kotlin.collections.ArrayList

class ChannelsDataBase {

    companion object {
        fun getChannelsList(): ArrayList<ChannelModel> {
            val channelsList = ArrayList<ChannelModel>()

            channelsList.add(
                ChannelModel(
                    "UCGAWwx_VrdiBawxxaQMBPug",
                    "اناشيد الروضة"
                )
            ) //اناشيد الروضة
            channelsList.add(ChannelModel("UCsciiPEQCUN-uos-crZAsPg", "المصحف المعلم للأطفال"))
            channelsList.add(ChannelModel("UCFBZOFHWS6KzfUhPAEy1gAQ", "تعلم الرسم و التلوين"))
            channelsList.add(ChannelModel("UC8z0fVccUJ9jtYsj7LO5bQQ", "جنة الرسم"))
            channelsList.add(ChannelModel("UCpm9UA5N0Y9Kv1FJNtnNlJA", "ماشا و الدب "))
            channelsList.add(ChannelModel("UCazFScO30FKY3YoNNDfNY5g", "سندريلا"))
            channelsList.add(ChannelModel("UCEof7Z2iOP48t9a0HYoek7A", "افلام كرتون كيدو "))
            channelsList.add(ChannelModel("UCwCLmo14vG4luqGsYcYxKrQ", "سبيستون"))
            channelsList.add(ChannelModel("UCUkv4sGOZlpuNcCqFW5248A", "فتيات القوة"))
            channelsList.add(ChannelModel("UCib-d_VhrmQJ8Ako5_7c97g", "توم وجيري"))
            channelsList.add(ChannelModel("UCPcynnZcILjm0e2aaaTuCjw", "Talking Angela"))
            channelsList.add(ChannelModel("UCm3hAp1m1xlAz0ve_EKAo4g", "Talking Tom"))
            channelsList.add(ChannelModel("UCCkOlUJDMmxxMlfWr8NTQFw", "بن 10 | Ben 10"))


            return channelsList
        }

        fun getPlaylists(): ArrayList<ChannelModel> {
            val playlists = ArrayList<ChannelModel>()
            playlists.add(
                ChannelModel(
                    "PLEaGEZnOHpUNM9jjbOzSv3AWMU9D7UmLF",
                    "تعليم القرآن الكريم للأطفال"
                )
            )
            playlists.add(ChannelModel("PLofH5lzq31vwhbzPV9ImO1GiVTCkYJvPg", "جزء عم كامل للاطفال"))
            playlists.add(
                ChannelModel(
                    "PLEaGEZnOHpUOmHSHA0e7XEHKQwBHK6cDh",
                    "تعلم الكتابةوالقراءة باللغة العربية"
                )
            )
            playlists.add(
                ChannelModel(
                    "PLEaGEZnOHpUOfieeYb8RYr5NehXZ2FMKz",
                    "تعليم اللغة الانجليزية للأطفال"
                )
            )
            playlists.add(
                ChannelModel(
                    "PLEaGEZnOHpUP5UmuuTlQ9fpXHi6c-Qni_",
                    "الأرقام للأطفال باللغة العربية"
                )
            )

            return playlists
        }

        private fun randomIndex(max: Int): Int {
            return Random().nextInt(max - 0 + 1) + 0
        }

        fun getDefaultChannelId(): ChannelModel {
            return getChannelsList()[randomIndex(
                getChannelsList().size - 1
            )]
        }

        fun getDefaultPlayList(): ChannelModel {
            return getPlaylists()[randomIndex(
                getPlaylists().size - 1
            )]
        }
    }
}