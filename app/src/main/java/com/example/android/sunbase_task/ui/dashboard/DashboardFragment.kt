package com.example.android.sunbase_task.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.sunbase_task.R
import com.example.android.sunbase_task.databinding.FragmentDashboardBinding
import com.example.android.sunbase_task.ui.dashboard.adapter.DashboardPhotoAdapter
import com.example.android.sunbase_task.ui.dashboard.adapter.DashboardPhotoLoadingStateAdapter
import com.example.android.sunbase_task.utils.RecyclerViewItemDecoration
import com.example.android.sunbase_task.utils.isOnline
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private val viewModel: DashboardViewModel by viewModels()
    private lateinit var binding: FragmentDashboardBinding
    private val adapter =
        DashboardPhotoAdapter { name: String -> snackBarClickedPhoto(name) }

    private var searchJob: Job? = null

    private lateinit var linearLayoutManager: LinearLayoutManager

    @ExperimentalPagingApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dashboard, container, false
        )

        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }

        binding.buttonSeach.setOnClickListener {
            if (isOnline(this.context)) {
                if (!binding.edittextSearch.text.equals("")) {
                    setUpAdapter()
                    startSearchJob(binding.edittextSearch.text.toString())
                } else {
                    Toast.makeText(this.context, "Please type something to search item.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this.context, "Please connect to the internet.", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    @ExperimentalPagingApi
    private fun startSearchJob(search: String = getString(R.string.default_search)) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchPhotoLiveData(search).observe(viewLifecycleOwner, {
                adapter.submitData(lifecycle, it)
            })
        }
    }

    private fun snackBarClickedPhoto(name: String) {
        val parentLayout = binding.root.findViewById<View>(android.R.id.content)
        Snackbar.make(parentLayout, name, Snackbar.LENGTH_LONG)
            .show()
    }

    private fun setUpAdapter() {

        binding.recyclerview.apply {
            layoutManager = GridLayoutManager(this.context, 2, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            addItemDecoration(RecyclerViewItemDecoration())
        }
        binding.recyclerview.adapter = adapter.withLoadStateFooter(
            footer = DashboardPhotoLoadingStateAdapter { retry() }
        )

        adapter.addLoadStateListener { loadState ->

            if (loadState.mediator?.refresh is LoadState.Loading) {

                if (adapter.snapshot().isEmpty()) {
                    binding.progress.isVisible = false
                }

            } else {
                binding.progress.isVisible = false
                binding.swipeRefreshLayout.isRefreshing = false

                val error = when {
                    loadState.mediator?.prepend is LoadState.Error -> loadState.mediator?.prepend as LoadState.Error
                    loadState.mediator?.append is LoadState.Error -> loadState.mediator?.append as LoadState.Error
                    loadState.mediator?.refresh is LoadState.Error -> loadState.mediator?.refresh as LoadState.Error

                    else -> null
                }
                error?.let {
                    if (adapter.snapshot().isEmpty()) {
                    }
                }
            }
        }

    }

    private fun retry() {
        adapter.retry()
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}





