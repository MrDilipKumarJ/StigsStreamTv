package com.stigstream.tv.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.leanback.app.SearchSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.lifecycleScope
import com.stigstream.tv.R
import com.stigstream.tv.data.model.Movie
import com.stigstream.tv.data.repository.StigstreamRepository
import com.stigstream.tv.ui.detail.DetailActivity
import com.stigstream.tv.ui.home.MovieCardPresenter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.search_fragment, StigstreamSearchFragment())
                .commitNow()
        }
    }
}

class StigstreamSearchFragment : SearchSupportFragment(), SearchSupportFragment.SearchResultProvider {

    private val repository = StigstreamRepository()
    private lateinit var resultsAdapter: ArrayObjectAdapter
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultsAdapter = ArrayObjectAdapter(ListRowPresenter())
        setSearchResultProvider(this)
        setOnItemViewClickedListener { _, item, _, _ ->
            if (item is Movie) {
                val intent = Intent(requireActivity(), DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_MOVIE_ID, item.id)
                    putExtra(DetailActivity.EXTRA_MOVIE_TITLE, item.title)
                    putExtra(DetailActivity.EXTRA_MOVIE_POSTER, item.posterUrl)
                    putExtra(DetailActivity.EXTRA_MOVIE_BACKDROP, item.backdropUrl)
                    putExtra(DetailActivity.EXTRA_MOVIE_YEAR, item.year)
                    putExtra(DetailActivity.EXTRA_MOVIE_RATING, item.rating)
                    putExtra(DetailActivity.EXTRA_MOVIE_GENRE, item.genre)
                    putExtra(DetailActivity.EXTRA_MOVIE_DURATION, item.duration)
                    putExtra(DetailActivity.EXTRA_MOVIE_DESCRIPTION, item.description)
                    putExtra(DetailActivity.EXTRA_MOVIE_STREAM_URL, item.streamUrl)
                    putExtra(DetailActivity.EXTRA_MOVIE_QUALITY, item.quality)
                }
                startActivity(intent)
            }
        }
    }

    override fun getResultsAdapter(): ObjectAdapter = resultsAdapter

    override fun onQueryTextChange(newQuery: String): Boolean {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            delay(400) // debounce
            performSearch(newQuery)
        }
        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        lifecycleScope.launch { performSearch(query) }
        return true
    }

    private suspend fun performSearch(query: String) {
        resultsAdapter.clear()
        if (query.isBlank()) return

        val results = repository.search(query)
        if (results.isEmpty()) return

        val cardAdapter = ArrayObjectAdapter(MovieCardPresenter())
        results.forEach { cardAdapter.add(it) }
        resultsAdapter.add(ListRow(HeaderItem("Results for \"$query\""), cardAdapter))
    }
}
