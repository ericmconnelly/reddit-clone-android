package edu.cs371m.reddit.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import edu.cs371m.reddit.R
import edu.cs371m.reddit.glide.Glide
import kotlinx.android.synthetic.main.one_post.*

class OnePost : AppCompatActivity() {
    companion object {
        val postTitle = "title"
        val postSelfText = "selfText"
        val postImageUrl = "imageURL"
        val postThumURL= "thumURL"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.one_post)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val bundle : Bundle? = intent.extras
        val onePostTitle = bundle!!.getString(postTitle, "hello")
        val onePostText = bundle!!.getString(postSelfText)
        val onePostImageUrl = bundle!!.getString(postImageUrl)
        val onePostThUrl = bundle!!.getString(postThumURL)
        val name = bundle!!.getString("name")
        Log.d("title appear? ", "$onePostTitle")
        Log.d("self text ? ", "$onePostText")
        if(onePostTitle.count() <= 30){
            onePost_Title.text = onePostTitle
        } else{
            onePost_Title.text = onePostTitle.substring(0,30) + "..."
        }

        onePost_selfText.text = onePostText
        onePost_selfText.movementMethod = ScrollingMovementMethod()
        if(onePostImageUrl !=null && onePostThUrl != null){
            fetchImage(onePostImageUrl, onePostThUrl, onePost_selfImage)
        }
    }

    private fun fetchImage(urlString: String, thumbnailURL: String, imageView: ImageView){
        Glide.glideFetch(urlString,thumbnailURL,imageView)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == android.R.id.home) {
            finish()
            true
        } else super.onOptionsItemSelected(item)
    }

}