package com.example.tapptic.ui.fragments

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.tapptic.R
import com.example.tapptic.adapters.DummiesAdapter
import com.example.tapptic.helpers.Status
import com.example.tapptic.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var dummiesAdapter: DummiesAdapter

    private val args: MainFragmentArgs by navArgs()
    private val viewModel: MainViewModel by viewModels()
    private var isTablet = false
    private var snackbar: Snackbar? = null
    private var orientation: Int = 0
    private var isPhoneInLandscape: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isTablet = requireContext().resources.getBoolean(R.bool.isTablet)
        orientation = resources.configuration.orientation

        isPhoneInLandscape = args.name?.isNotEmpty()!!

        return when {
            isTablet || isPhoneInLandscape -> {
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    inflater.inflate(R.layout.fragment_main_land, container, false)
                } else {
                    inflater.inflate(R.layout.fragment_main, container, false)
                }
            }
            else -> inflater.inflate(R.layout.fragment_main, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchData()
        setupRecyclerView()
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        viewModel.dummies.observe(
            viewLifecycleOwner, Observer {
                it?.getContentIfNotHandled()?.let { result ->
                    when (result.status) {
                        Status.ERROR -> {
                            view?.findViewById<View>(R.id.progressBar)?.apply {
                                visibility = View.INVISIBLE
                            }
                            snackbar = Snackbar.make(
                                requireView(),
                                getString(R.string.error_msg),
                                Snackbar.LENGTH_INDEFINITE
                            ).setAction(getString(R.string.retry)) {
                                viewModel.fetchData()
                            }.setActionTextColor(Color.RED)
                            snackbar?.show()
                        }
                        Status.LOADING -> {
                            snackbar?.dismiss()
                            view?.findViewById<View>(R.id.progressBar)?.apply {
                                visibility = View.VISIBLE
                            }
                        }
                        Status.SUCCESS -> {
                            snackbar?.dismiss()
                            view?.findViewById<View>(R.id.progressBar)?.apply {
                                visibility = View.INVISIBLE
                            }
                            dummiesAdapter.submitList(result.data)
                        }
                    }
                }
            }
        )
    }

    private fun setupRecyclerView() {
        view?.findViewById<RecyclerView>(R.id.rvDummies)?.apply {
            adapter = dummiesAdapter
            layoutManager = LinearLayoutManager(requireContext())

            if (isTablet && orientation == Configuration.ORIENTATION_LANDSCAPE || isPhoneInLandscape) {
                dummiesAdapter.setOnItemClickListener { position ->
                    val params = dummiesAdapter.currentList[position].name
                    val fragment = DetailsFragment.newInstance(params)
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragment, fragment)
                        .commit()
                }
            } else {
                dummiesAdapter.setOnItemClickListener { position ->
                    findNavController().navigate(
                        MainFragmentDirections.actionMainFragmentToDetailsFragment(
                            dummiesAdapter.currentList[position].name
                        )
                    )
                }
            }
        }
    }
}