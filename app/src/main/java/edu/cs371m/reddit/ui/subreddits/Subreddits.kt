package edu.cs371m.reddit.ui.subreddits

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import edu.cs371m.reddit.databinding.FragmentRvBinding
import edu.cs371m.reddit.ui.MainViewModel
import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import edu.cs371m.reddit.R
import kotlinx.android.synthetic.main.action_bar.*
import kotlinx.android.synthetic.main.fragment_rv.*

class Subreddits : Fragment() {
    // XXX initialize viewModel
    private var _binding: FragmentRvBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: SubredditListAdapter

    companion object {
        fun newInstance(): Subreddits {
            return Subreddits()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.setTitle("Pick")
        _binding = FragmentRvBinding.inflate(inflater, container, false)
        return binding.root
    }
    @SuppressLint("SetTextI18n")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this){
            parentFragmentManager.popBackStack()
            Log.d("going back from sub", "back")
            val fav = activity?.findViewById<ImageView>(R.id.actionFavorite)
            fav?.isClickable = true
            val title = activity?.findViewById<TextView>(R.id.actionTitle)
            title?.text = "r/" + viewModel.subreddit.value
        }
    }
    private fun initRecyclerView(){
        val rv = view?.findViewById<RecyclerView>(R.id.recyclerView)
        adapter = SubredditListAdapter(viewModel)
        rv?.adapter = adapter
        rv?.layoutManager = LinearLayoutManager(context)
        val itemDecor = DividerItemDecoration(rv!!.context, LinearLayoutManager.VERTICAL)
        rv.addItemDecoration(itemDecor)
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return inflater.inflate(R.layout.fragment_rv, container, false)
//    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        viewModel.observeLivingSub().observe(viewLifecycleOwner, {
            adapter.addAll(it)
            adapter.notifyDataSetChanged()
        })

        val swipe = view?.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipe?.isEnabled = false

    }


    // XXX Write me, onViewCreated

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}