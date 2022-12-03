package edu.cs371m.reddit.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.cs371m.reddit.R
import edu.cs371m.reddit.api.RedditPost
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import edu.cs371m.reddit.databinding.RowPostBinding
import edu.cs371m.reddit.glide.Glide

/**
 * Created by witchel on 8/25/2019
 */

// https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter
// Slick adapter that provides submitList, so you don't worry about how to update
// the list, you just submit a new one when you want to change the list and the
// Diff class computes the smallest set of changes that need to happen.
// NB: Both the old and new lists must both be in memory at the same time.
// So you can copy the old list, change it into a new list, then submit the new list.
//
// You can call adapterPosition to get the index of the selected item
class PostRowAdapter(private val viewModel: MainViewModel)
    : ListAdapter<RedditPost, PostRowAdapter.VH>(RedditDiff()) {
    class RedditDiff : DiffUtil.ItemCallback<RedditPost>() {
        companion object {
            val postTitle = "title"
            val postSelfText = "selfText"
            val postImageUrl = "imageURL"
            val postThumURL = "thumURL"
        }

        override fun areItemsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
            return oldItem.key == newItem.key
        }
        override fun areContentsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
            return RedditPost.spannableStringsEqual(oldItem.title, newItem.title) &&
                    RedditPost.spannableStringsEqual(oldItem.selfText, newItem.selfText) &&
                    RedditPost.spannableStringsEqual(oldItem.publicDescription, newItem.publicDescription) &&
                    RedditPost.spannableStringsEqual(oldItem.displayName, newItem.displayName)

        }
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        var title = view.findViewById<TextView>(R.id.title)
        var image = view.findViewById<ImageView>(R.id.image)
        var selfText = view.findViewById<TextView>(R.id.selfText)
        var score = view.findViewById<TextView>(R.id.score)
        var comments = view.findViewById<TextView>(R.id.comments)
        var rowFav = view.findViewById<ImageView>(R.id.rowFav)


        init {
            title.setOnClickListener {
            }
            rowFav.setOnClickListener {
                val position = adapterPosition
                if (viewModel.isFavarite(getItem(position))) {
                    rowFav.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                    viewModel.removeFavoritePost(getItem(position))
                } else {
                    rowFav.setImageResource(R.drawable.ic_favorite_black_24dp)
                    viewModel.addFavoritePost(getItem(position))
                }
            }
        }

        fun bind(post: RedditPost) {
            title.text = post.title
            viewModel.netFetchImage(post.imageURL, post.thumbnailURL, image)
            selfText.text = post.selfText
            score.text = post.score.toString()
            comments.text = post.commentCount.toString()
            if (viewModel.isFavarite(post)) {
                rowFav.setImageResource(R.drawable.ic_favorite_black_24dp)
            } else {
                rowFav.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            }

        }
    }
}

