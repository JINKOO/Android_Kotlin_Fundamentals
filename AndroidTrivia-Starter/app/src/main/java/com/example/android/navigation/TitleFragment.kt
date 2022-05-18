package com.example.android.navigation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.navigation.databinding.FragmentTitleBinding

class TitleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(
            inflater,
            R.layout.fragment_title,
            container,
            false
        )

        binding.apply {
            playButton.setOnClickListener { view: View ->
                view.findNavController()
                    .navigate(TitleFragmentDirections.actionTitleFragmentToGameFragment())
            }

            rulesButton.setOnClickListener { view: View ->
                view.findNavController()
                    .navigate(TitleFragmentDirections.actionTitleFragmentToRulesFragment())
            }

            aboutButton.setOnClickListener { view: View ->
                view.findNavController()
                    .navigate(TitleFragmentDirections.actionTitleFragmentToAboutFragment())
            }
        }

        // options menu
        setHasOptionsMenu(true)

        Log.i(TAG, "onCreateView() ")
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG, "onAttach() ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate()")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated()")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume() ")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause() ")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView()")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy() ")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i(TAG, "onDetach()")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "TitleFragment"
    }
}