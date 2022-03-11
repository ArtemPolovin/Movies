package com.example.movies.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.movies.R
import com.example.movies.databinding.FragmentExploreBinding
import com.example.movies.ui.MainActivity
import com.example.movies.ui.search_movie_by_name.SearchMovieByNameFragment
import com.example.movies.utils.KEY_OPEN_MOVIES_PAGE
import com.example.movies.utils.MINIMUM_SYMBOLS
import com.example.movies.utils.getNavigationResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@FlowPreview
@AndroidEntryPoint
class ExploreFragment : Fragment() {

    private lateinit var navController: NavController


    private val viewModel: ExploreViewModel by viewModels()

    private var _binding: FragmentExploreBinding? = null
    private val binding: FragmentExploreBinding
        get() =
            _binding ?: throw RuntimeException("FragmentExploreBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupToolbar()
        inputResultOfMovieSearching()
         //openScreenWithMovies()

       /* val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_explore_fragment) as NavHostFragment

        navController = navHostFragment.navController*/

      //  Navigation.findNavController(requireActivity(),R.id.nav_host_fragment).navigate()

    }

    private fun switchFragment(movieName: String) {

        val searchMovieByNameFragment =   SearchMovieByNameFragment.newInstanceSearchMovieByNameFragment(movieName)

        openScreenWithMovies(searchMovieByNameFragment)

        childFragmentManager.beginTransaction()
            .replace(
                R.id.nav_host_explore_fragment,
                searchMovieByNameFragment
            )
            .addToBackStack(null)
            .commit()
    }

    private fun resultFromSearchView(): StateFlow<String> {
        val query = MutableStateFlow("")

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { query.value = it }
                return true
            }

        })
        return query
    }


    private fun inputResultOfMovieSearching() {
        viewLifecycleOwner.lifecycleScope.launch {
            resultFromSearchView()
                .debounce(500)
                .filter { it.length >= MINIMUM_SYMBOLS }
                .distinctUntilChanged()
                .flowOn(Dispatchers.Default)
                .collect { result ->
                    switchFragment(result)
                }
        }
    }

    fun openScreenWithMovies(fragment: Fragment) {
       /* findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>("key")?.observe(viewLifecycleOwner) {result ->
            // Do something with the result.
        }*/

     /* fragment.getNavigationResult<Boolean>(KEY_OPEN_MOVIES_PAGE)?.observe(viewLifecycleOwner){
          println("mLog: boolean = $it")
          if(it)findNavController().navigate(ExploreFragmentDirections.actionExploreFragmentToMoviesFragment())
      }*/
    }

    private fun setupToolbar() {
        (requireActivity() as MainActivity).setupActionBar(binding.toolbar)
    }

}