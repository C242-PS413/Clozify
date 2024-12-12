package com.c242ps413.clozify.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.c242ps413.clozify.data.databases.favorite.Favorite
import com.c242ps413.clozify.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private val FavoriteViewModel: FavoriteViewModel by viewModels { FavoriteViewModelFactory(requireActivity().application) }
    private lateinit var adapter: FavoriteAdapter

    companion object {
        private const val TAG = "FavoriteFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        // Tambahkan aksi klik favorit
        adapter = FavoriteAdapter(
            object : FavoriteAdapter.OnItemClickListener {
                override fun onItemClick(favorite: Favorite) {
                    // Handle item click, jika diperlukan
                }
            },
            onFavoriteClick = { favorite ->
                // Langsung hapus item favorit
                FavoriteViewModel.delete(favorite)
                Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show()
            }
        )

        binding.rvfavorite.layoutManager = LinearLayoutManager(requireContext())
        binding.rvfavorite.adapter = adapter
    }


    private fun observeViewModel() {
        // Observe data dari ViewModel
        FavoriteViewModel.getAllFavorites().observe(viewLifecycleOwner, Observer { favoriteEvents ->
            Log.d(TAG, "Data favorite events observed: ${favoriteEvents.size} items") // Debug log
            // Update list di adapter
            adapter.submitList(favoriteEvents)
        })
    }
}