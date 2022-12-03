package edu.cs371m.reddit.api

import android.text.SpannableString
import com.google.gson.GsonBuilder
import edu.cs371m.reddit.MainActivity

class RedditPostRepository(private val redditApi: RedditApi) {
    // NB: This is for our testing.
    val gson = GsonBuilder().registerTypeAdapter(
            SpannableString::class.java, RedditApi.SpannableDeserializer()
        ).create()

    private fun unpackPosts(response: RedditApi.ListingResponse): List<RedditPost> {
        // XXX Write me.
        var redditPostList : List<RedditPost> = emptyList()
        response.data.children.map {
            redditPostList.toMutableList().add(it.data)
        }
        return redditPostList
    }

    suspend fun getPosts(subreddit: String): List<RedditPost> {
        if (MainActivity.globalDebug) {
            val response = gson.fromJson(
                MainActivity.jsonAww100,
                RedditApi.ListingResponse::class.java)
            return unpackPosts(response)
        } else {
            // XXX Write me.
            var listOfPost: List<RedditPost> = emptyList()
            redditApi.getPosts(subreddit).data.children.forEach {
                listOfPost += it.data
            }
            return listOfPost
        }
    }

    suspend fun getSubreddits(): List<RedditPost> {
        if (MainActivity.globalDebug) {
            val response = gson.fromJson(
                MainActivity.subreddit1,
                RedditApi.ListingResponse::class.java)
            return unpackPosts(response)
        } else {
            // XXX Write me.
            var list: List<RedditPost> = emptyList()
            redditApi.getSubreddits().data.children.forEach {
                list += it.data
            }
            return list
        }
    }
}
