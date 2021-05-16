package com.example.tapptic.ui.fragments

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.example.tapptic.R
import com.example.tapptic.databinding.FragmentDetailsBinding
import com.example.tapptic.helpers.Status
import com.example.tapptic.viewmodels.DetailsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var glide: RequestManager
    private val viewModel: DetailsViewModel by activityViewModels()
    private val args: DetailsFragmentArgs by navArgs()
    private var snackbar: Snackbar? = null
    private var isFirstTimeCreated = true

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY, args.name)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (savedInstanceState != null) {
            isFirstTimeCreated = false
        }
        if (!isFirstTimeCreated) {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                findNavController().navigate(
                    DetailsFragmentDirections.actionDetailsFragmentToMainFragment(args.name)
                )
            }
        }
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.name.isNotEmpty()) {
            viewModel.fetchDetailsForSelectedDummy(args.name)
        }
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        viewModel.dummiesDetails.observe(
            viewLifecycleOwner, Observer {
                it?.getContentIfNotHandled()?.let { result ->
                    when (result.status) {
                        Status.ERROR -> {
                            binding.progressBar.visibility = View.INVISIBLE
                            snackbar = Snackbar.make(
                                requireView(),
                                getString(R.string.error_msg),
                                Snackbar.LENGTH_INDEFINITE
                            ).setAction("RETRY") {
                                viewModel.fetchDetailsForSelectedDummy(args.name)
                            }.setActionTextColor(Color.RED)
                            snackbar?.show()
                        }
                        Status.LOADING -> {
                            snackbar?.dismiss()
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        Status.SUCCESS -> {
                            snackbar?.dismiss()
                            binding.progressBar.visibility = View.INVISIBLE
                            binding.tvItemNumber.text = result.data?.name
                            binding.tvText.text = result.data?.text
                            glide.load(result.data?.image).into(binding.iv)
                        }
                    }
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(name: String): DetailsFragment {
            return DetailsFragment().apply {
                arguments = Bundle().apply {
                    putString("name", name)
                }
            }
        }
        val KEY = "key"
    }
}