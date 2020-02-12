package com.cartoons.kids.data.storage

import java.util.*
import kotlin.collections.ArrayList

class ChannelsDataBase {

    companion object {
        fun getChannelsList(): ArrayList<String> {
            val channelsList = ArrayList<String>()


            channelsList.add("UCGAWwx_VrdiBawxxaQMBPug") //اناشيد الروضة
            channelsList.add("UCwCLmo14vG4luqGsYcYxKrQ")
            channelsList.add("UCUkv4sGOZlpuNcCqFW5248A")
            channelsList.add("UCib-d_VhrmQJ8Ako5_7c97g")
            channelsList.add("UCPcynnZcILjm0e2aaaTuCjw")
            channelsList.add("UCm3hAp1m1xlAz0ve_EKAo4g")
            channelsList.add("UCCkOlUJDMmxxMlfWr8NTQFw")
            channelsList.add("UCFBZOFHWS6KzfUhPAEy1gAQ")
            channelsList.add("UCazFScO30FKY3YoNNDfNY5g")
            channelsList.add("UCsciiPEQCUN-uos-crZAsPg")
            channelsList.add("UCpm9UA5N0Y9Kv1FJNtnNlJA")
            channelsList.add("UCEof7Z2iOP48t9a0HYoek7A")
            channelsList.add("UC8z0fVccUJ9jtYsj7LO5bQQ")

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

        fun getDefaultChannelId(): String {
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