package com.vm.vishunotesapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.vm.vishunotesapp.R
import com.vm.vishunotesapp.databinding.FragmentMainBinding
import com.vm.vishunotesapp.models.NoteResponse
import com.vm.vishunotesapp.networking.NotesApi
import com.vm.vishunotesapp.ui.viewmodel.NotesViewModel
import com.vm.vishunotesapp.utils.Constants.ARGS_NOTE
import com.vm.vishunotesapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val notesViewModel: NotesViewModel by viewModels()
    private lateinit var notesAdapter: NotesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        notesAdapter = NotesAdapter(::onNoteClicked)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.notesRecyclerView.layoutManager = LinearLayoutManager(context)
            //StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.notesRecyclerView.adapter = notesAdapter

        notesViewModel.getNotes()
        observeNotesLiveData()
        setupFabBtn()
    }

    private fun setupFabBtn() {
        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_noteFragment)
        }
    }

    private fun onNoteClicked(noteResponse: NoteResponse) {
        val bundle = Bundle()
        bundle.putString(ARGS_NOTE, Gson().toJson(noteResponse))
        findNavController().navigate(R.id.action_mainFragment_to_noteFragment, bundle)
    }

    private fun observeNotesLiveData() {
        notesViewModel.notesLiveData.observe(viewLifecycleOwner) {
            binding.notesProgressBar.isVisible = false
            when (it) {
                is NetworkResult.Loading -> {
                    binding.notesProgressBar.isVisible = true
                }
                is NetworkResult.Success -> {
                    notesAdapter.submitList(it.data)
                }
                is NetworkResult.Error -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}