package edu.cs371m.reddit.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import edu.cs371m.reddit.MainActivity
import edu.cs371m.reddit.databinding.FragmentRvBinding
import androidx.recyclerview.widget.RecyclerView
import edu.cs371m.reddit.R
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.lifecycle.Observer

class Favorites: Fragment() {
    // XXX initialize viewModel
    private var _binding: FragmentRvBinding? = null
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: PostRowAdapter

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): Favorites {
            return Favorites()
        }
    }
    private fun setDisplayHomeAsUpEnabled(value : Boolean) {
        val mainActivity = requireActivity() as MainActivity
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(value)
    }

    private fun initRecyclerView(root: View) {
        val rv = root.findViewById<RecyclerView>(R.id.recyclerView)
        adapter = PostRowAdapter(viewModel)
        rv?.adapter = adapter
        rv?.layoutManager = LinearLayoutManager(context)
        val itemDecor = DividerItemDecoration(rv?.context, LinearLayoutManager.VERTICAL)
        rv?.addItemDecoration(itemDecor)
        val swipe = root.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipe?.isEnabled = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRvBinding.inflate(inflater)
        initRecyclerView(_binding!!.root)
        viewModel.observeLivingFav().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(javaClass.simpleName, "onViewCreated")
        setDisplayHomeAsUpEnabled(true)
        // XXX Write me
        // Setting itemAnimator = null on your recycler view might get rid of an annoying
        // flicker

        // Add to menu
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Menu is already inflated by main activity
            }
            // XXX Write me, onMenuItemSelected
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        // XXX Write me
        // Don't let back to home button stay when we exit favorites
        super.onDestroyView()
    }
}