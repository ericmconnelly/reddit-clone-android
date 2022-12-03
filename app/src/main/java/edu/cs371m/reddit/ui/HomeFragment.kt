package edu.cs371m.reddit.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import edu.cs371m.reddit.databinding.FragmentRvBinding
import edu.cs371m.reddit.ui.subreddits.SubredditListAdapter
import edu.cs371m.reddit.ui.subreddits.Subreddits
import edu.cs371m.reddit.R
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import edu.cs371m.reddit.MainActivity


// XXX Write most of this file
class HomeFragment: Fragment() {
    // XXX initialize viewModel
    private var _binding: FragmentRvBinding? = null
    private val favoritesFragTag = "favoritesFragTag"
    private val subredditsFragTag = "subredditsFragTag"
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter : PostRowAdapter
    private lateinit var subreddits: Subreddits
    private lateinit var favorites: Favorites

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    // Set up the adapter
    private fun initAdapter(binding: FragmentRvBinding) : PostRowAdapter {
    }

    private fun notifyWhenFragmentForegrounded(postRowAdapter: PostRowAdapter) {
        // When we return to our fragment, notifyDataSetChanged
        // to pick up modifications to the favorites list.  Maybe do more.
    }

    private fun initSwipeLayout(swipe : SwipeRefreshLayout) {
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRvBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(javaClass.simpleName, "onViewCreated")
        // XXX Write me
    }

    private fun initSearch() {
        activity
            ?.findViewById<EditText>(R.id.actionSearch)
            ?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if(s.isEmpty()) (activity as MainActivity).hideKeyboard()
                    viewModel.setSearchTerm(s.toString())
                }
            })
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initSearch()
        initRecyclerView()
        viewModel.observeLivingPosts().observe(viewLifecycleOwner, Observer {
            requireActivity().swipeRefreshLayout.apply {
                isRefreshing = false
                setOnRefreshListener {
                    viewModel.netPosts()
                }
            }
            adapter.submitList(it)
            adapter.notifyDataSetChanged()

        })
        viewModel.observeTitle().observe(viewLifecycleOwner, {
            Log.d("title ", "$it")
            viewModel.setNewSubredit(it)
            viewModel.netPosts()
            requireActivity().actionTitle.text = "r/$it"
        })

        parentFragmentManager.addOnBackStackChangedListener {
            if(parentFragmentManager.backStackEntryCount == 0){
                Log.d("backStackchangedcalled", "called")
                adapter.notifyDataSetChanged()
            }
        }

        requireActivity().actionTitle.setOnClickListener {
            val subFrag = parentFragmentManager.findFragmentByTag(subredditsFragTag)
            val favFrag = parentFragmentManager.findFragmentByTag(favoritesFragTag)
            Log.d("subfrag", "$subFrag")
            Log.d("fav frag", "$favFrag")

            if (subFrag != null) {
                Log.d("....sub frag", "$subFrag")
            }
            if (subFrag == null) {
                Log.d("do", "process this")
                val favTitle = activity?.findViewById<ImageView>(R.id.actionFavorite)
                favTitle?.isClickable = false
                val title = activity?.findViewById<TextView>(R.id.actionTitle)
                title?.text = "Pick"
                parentFragmentManager.beginTransaction()
                    .add(R.id.main_frame, Subreddits.newInstance(), subredditsFragTag)
                    .addToBackStack("sub")
                    .commit()
            }

        }

        requireActivity().actionFavorite.setOnClickListener {
            val subFrag = parentFragmentManager.findFragmentByTag(subredditsFragTag)
            val favFrag = parentFragmentManager.findFragmentByTag(favoritesFragTag)
            //val favFrag = parentFragmentManager.findFragmentByTag(favoritesFragTag)
            Log.d("fav frag", "$favFrag")
            Log.d("subGraf", "$subFrag")
            if (favFrag != null) {
                Log.d("....fav frag", "$favFrag")
            }
            if (favFrag == null) {
                Log.d("do", "process this")
                val title = activity?.findViewById<TextView>(R.id.actionTitle)
                title?.isClickable = false
                title?.text = "Favorites"
                parentFragmentManager.beginTransaction()
                    .add(R.id.main_frame, Favorites.newInstance(), favoritesFragTag)
                    .addToBackStack("fav")
                    .commit()
            }
        }

        viewModel.observeTitle().observe(viewLifecycleOwner, {
            Log.d("title ", "$it")
            viewModel.setNewSubredit(it)
            viewModel.netPosts()
            requireActivity().actionTitle.text = "r/$it"
        })

    }
}