package com.cartoons.kids.data.storage

import com.cartoons.kids.data.model.ChannelModel
import java.util.*
import kotlin.collections.ArrayList

class ChannelsDataBase {

    companion object {
        fun getChannelsList(): ArrayList<ChannelModel> {
            val channelsList = ArrayList<ChannelModel>()

            channelsList.add(ChannelModel("UCGAWwx_VrdiBawxxaQMBPug", "اناشيد الروضة")) //اناشيد الروضة
            channelsList.add(ChannelModel("UCsciiPEQCUN-uos-crZAsPg", "المصحف المعلم للأطفال"))
            channelsList.add(ChannelModel("UCFBZOFHWS6KzfUhPAEy1gAQ", "تعلم الرسم و التلوين"))
            channelsList.add(ChannelModel("UC8z0fVccUJ9jtYsj7LO5bQQ", "جنة الرسم"))
            channelsList.add(ChannelModel("UCpm9UA5N0Y9Kv1FJNtnNlJA", "ماشا و الدب "))
            channelsList.add(ChannelModel("UCEof7Z2iOP48t9a0HYoek7A", "افلام كرتون كيدو "))
            channelsList.add(ChannelModel("UCwCLmo14vG4luqGsYcYxKrQ", "سبيستون"))
            channelsList.add(ChannelModel("UCUkv4sGOZlpuNcCqFW5248A", "فتيات القوة"))
            channelsList.add(ChannelModel("UCib-d_VhrmQJ8Ako5_7c97g", "توم وجيري"))
            channelsList.add(ChannelModel("UCPcynnZcILjm0e2aaaTuCjw", "Talking Angela"))
            channelsList.add(ChannelModel("UCm3hAp1m1xlAz0ve_EKAo4g", "Talking Tom"))
            channelsList.add(ChannelModel("UCCkOlUJDMmxxMlfWr8NTQFw", "بن 10 | Ben 10"))
            channelsList.add(ChannelModel("UCazFScO30FKY3YoNNDfNY5g", "Arabian Fairy Tales"))


            return channelsList
        }

        fun getPlaylists(): ArrayList<String> {
            val playlists = ArrayList<String>()
            playlists.add("PLbwI0LE0f2MH2neOMmvmQJHdPgw_iuQO9")
            playlists.add("PLr7CMDV3cXkgsWy3GqvgInAsnbhV5bfFz")
            playlists.add("PLw1c55OigFnHrU1s8UiTOkhzNIjs5GC90")
            playlists.add("PLw1c55OigFnGBuLkAkQyOPswkvwjdna43")
            playlists.add("PL4eNR_m-ysh8NUcWTIqaRLWIxE6YUYj3z")
            playlists.add("PL4eNR_m-ysh9BtPfdmoJERpNVqVUlTrQC")
            playlists.add("PLXu9wyb91oZe9wPsdrtj-6QfGn18wjG_N")
            playlists.add("PLe8wTcTlN5139E140DqY8nJwpWW9w4FAl")
            playlists.add("PLDlWXWuh-hTmXnViToCoxF4E6V_w__3dH")
            playlists.add("PLtSeku8YKm82b1SlZKVw_r9Eqoilgxm2_")
            playlists.add("PLOOZsJiJTcmJWQgbUbtMz2xxA-sAGAcyo")
            playlists.add("PLBAafBybZT0c6EThAzx_LkGqLK5JhCs_I")
            playlists.add("PLI4r0W94axvgGrNBPcFaHfAZlVOmuHBaG")
            playlists.add("PLmhFT0q1_u4MbOZCg_x7mP7KvCKlP6LXp")

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

        fun getDefaultPlayList(): String {
            return getPlaylists()[randomIndex(
                getPlaylists().size - 1
            )]
        }
    }
}