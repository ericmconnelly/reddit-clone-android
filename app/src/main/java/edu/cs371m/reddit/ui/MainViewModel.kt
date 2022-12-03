package edu.cs371m.reddit.ui


import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.*
import edu.cs371m.reddit.api.RedditApi
import edu.cs371m.reddit.api.RedditPost
import edu.cs371m.reddit.glide.Glide
import edu.cs371m.reddit.api.RedditPostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// XXX Much to write
class MainViewModel : ViewModel() {
    var title = MutableLiveData<String>()
    var searchTerm = MutableLiveData<String>()
    var subreddit = MutableLiveData<String>().apply {
        value = "aww"
    }
    // XXX Write netPosts/searchPosts
    private val redditApi = RedditApi.create()
    private val redditRepository = RedditPostRepository(redditApi)
    var getListOfPosts = MutableLiveData<List<RedditPost>>()
    var getSubredditsList = MutableLiveData<List<RedditPost>>()
    var favoritePostList =  MutableLiveData<List<RedditPost>>().apply {
        value = mutableListOf()
    }

    init {
        viewModelScope.launch {
            val subRedditValue = subreddit.value
            getListOfPosts.value = redditRepository.getPosts(subRedditValue.toString())
            getSubredditsList.value = redditRepository.getSubreddits()
        }
    }

    // Looks pointless, but if LiveData is set up properly, it will fetch posts
    // from the network
    fun repoFetch() {
        val fetch = subreddit.value
        subreddit.value = fetch
    }

    fun setNewSubredit(newOne: String){
        subreddit.value =  newOne
    }

    fun observeTitle(): LiveData<String> {
        return title
    }
    fun setTitle(newTitle: String) {
        title.value = newTitle
    }
    // The parsimonious among you will find that you can call this in exactly two places
    fun setTitleToSubreddit() {
        title.value = "r/${subreddit.value}"
    }


    fun filterPosts(): List<RedditPost>? {
        val searchTermValue = searchTerm.value!!
        return getListOfPosts.value?.filter {
            it.searchFor(searchTermValue)
        }
    }

    fun filterSubreddits(): List<RedditPost>? {
        val searchTermValue = searchTerm.value!!
        return getSubredditsList.value?.filter {
            it.searchForSub(searchTermValue)
        }
    }

    fun filterFav(): List<RedditPost>? {
        val searchTermValue = searchTerm.value!!
        return favoritePostList.value?.filter {
            it.searchFor(searchTermValue)
        }
    }

    //store favorite list here
    fun addFavoritePost(post: RedditPost){
        val list = favoritePostList.value?.toMutableList()
        list?.let{
            it.add(post)
            favoritePostList.value = it
        }
    }

    fun isFavarite(post: RedditPost): Boolean{
        return favoritePostList.value?.contains(post) ?: false
    }

    fun removeFavoritePost(post: RedditPost){
        val list = favoritePostList.value?.toMutableList()
        list?.let{
            it.remove(post)
            favoritePostList.value = it
        }
    }

    fun getPost(position: Int): RedditPost?{
        return getListOfPosts.value?.get(position)
    }

    fun observeFav(): LiveData<List<RedditPost>> {
        return favoritePostList
    }

    // XXX Write me, set, observe, deal with favorites
    fun netFetchImage(urlString: String, thumbnailURL: String,imageView: ImageView){
        Glide.glideFetch(urlString,thumbnailURL,imageView)
    }

    fun netPosts() {
        viewModelScope.launch(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            getListOfPosts.postValue(subreddit.value?.let { redditRepository.getPosts(it) })
        }
    }

    fun observerPosts():LiveData<List<RedditPost>>{
        return getListOfPosts
    }

    val livingPosts = MediatorLiveData<List<RedditPost>>().apply {
        addSource(searchTerm){
            value = filterPosts()}
        addSource(getListOfPosts){value = getListOfPosts.value}
        value = getListOfPosts.value
    }

    fun observeLivingPosts(): LiveData<List<RedditPost>>{
        return livingPosts
    }

    var liveSubreddits = MediatorLiveData<List<RedditPost>>().apply {
        addSource(searchTerm)  {
            value = filterSubreddits() }
        addSource(getSubredditsList){value = getSubredditsList.value}
        value = getSubredditsList.value
    }

    fun observeLivingSub(): LiveData<List<RedditPost>>{
        return liveSubreddits
    }

    var livingFavs: MediatorLiveData<List<RedditPost>> = MediatorLiveData<List<RedditPost>>().apply {
        addSource(searchTerm) {
            value = filterFav()
        }
        addSource(favoritePostList){value = favoritePostList.value }
        value = favoritePostList.value
    }

    fun observeLivingFav() : LiveData<List<RedditPost>>{
        return livingFavs
    }

    // Convenient place to put it as it is shared
    companion object {
        fun doOnePost(context: Context, redditPost: RedditPost) {
        }
    }
}