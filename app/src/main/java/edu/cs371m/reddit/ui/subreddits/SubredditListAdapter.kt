package edu.cs371m.reddit.ui.subreddits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.cs371m.reddit.api.RedditPost
import edu.cs371m.reddit.databinding.RowSubredditBinding
import edu.cs371m.reddit.glide.Glide
import edu.cs371m.reddit.ui.MainViewModel
import edu.cs371m.reddit.ui.PostRowAdapter
import edu.cs371m.reddit.R

// NB: Could probably unify with PostRowAdapter if we had two
// different VH and override getItemViewType
// https://medium.com/@droidbyme/android-recyclerview-with-multiple-view-type-multiple-view-holder-af798458763b
class SubredditListAdapter(private val viewModel: MainViewModel,
                           private val fragmentActivity: FragmentActivity )
    : ListAdapter<RedditPost, SubredditListAdapter.VH>(PostRowAdapter.RedditDiff()) {
    var subredditsList = listOf<RedditPost>()

    // ViewHolder pattern
    inner class VH(val rowSubredditBinding: RowSubredditBinding)
        : RecyclerView.ViewHolder(rowSubredditBinding.root) {

        // XXX Write me.
        // NB: This one-liner will exit the current fragment
        // fragmentActivity.supportFragmentManager.popBackStack()
        private var subRowPic = rowSubredditBinding.subRowPic
        private var subRowHeading =  rowSubredditBinding.subRowHeading
        private var subRowDetails = rowSubredditBinding.subRowDetails
        init {
            subRowHeading.setOnClickListener {
                viewModel.setTitle(subRowHeading.text.toString())
                (itemView.context as FragmentActivity).apply {
                    it.isClickable = true
                    supportFragmentManager.popBackStack()
                }
            }
        }
        fun bind(item: RedditPost){
            val url = item.iconURL
            if(url != null){
                viewModel.netFetchImage(url, url, subRowPic)
            }
            subRowHeading.text = item.displayName
            subRowDetails.text = item.publicDescription
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_subreddit, parent, false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(subredditsList[holder.adapterPosition])
    }

    override fun getItemCount(): Int {
        if (subredditsList != null) {
            return subredditsList.size
        }
        return 0
    }

    fun addAll(items: List<RedditPost>) {
        subredditsList = items
    }

    //EEE // XXX Write me
}
