package com.example.android.sunbase_task.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.android.sunbase_task.databinding.FragmentHomeBinding
import com.example.android.sunbase_task.ui.home.adapter.PhotoAdapter
import com.example.android.sunbase_task.ui.home.adapter.PhotoLoadingStateAdapter
import com.example.android.sunbase_task.utils.RecyclerViewItemDecoration
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private val adapter =
        PhotoAdapter { name: String -> snackBarClickedPhoto(name) }

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
            R.layout.fragment_home, container, false
        )

        setUpAdapter()
        startSearchJob()
        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }

        return binding.root
    }


    @ExperimentalPagingApi
    private fun startSearchJob() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchPhotos(getString(R.string.default_search))
                .collectLatest {
                    adapter.submitData(it)
                }
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
            footer = PhotoLoadingStateAdapter { retry() }
        )

        adapter.addLoadStateListener { loadState ->

            if (loadState.mediator?.refresh is LoadState.Loading) {

                if (adapter.snapshot().isEmpty()) {
                    binding.progress.isVisible = true
                }
              //  binding.errorTxt.isVisible = false
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
                       /* binding.errorTxt.isVisible = true
                        binding.errorTxt.text = it.error.localizedMessage*/
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