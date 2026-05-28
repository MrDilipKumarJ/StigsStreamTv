package com.stigstream.tv.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.stigstream.tv.R
import com.stigstream.tv.data.model.Movie
import com.stigstream.tv.ui.detail.DetailActivity
import com.stigstream.tv.ui.search.SearchActivity
import kotlinx.coroutines.launch

class MainFragment : BrowseSupportFragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var rowsAdapter: ArrayObjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        setupUIElements()
        setupListeners()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadRows()
    }

    private fun setupUIElements() {
        title = "STIGSTREAM"
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        // Brand colors
        brandColor = Color.parseColor("#E50914")
        searchAffordanceColor = Color.parseColor("#E50914")
    }

    private fun setupListeners() {
        onItemViewClickedListener = ItemViewClickedListener()
        onItemViewSelectedListener = ItemViewSelectedListener()
        setOnSearchClickedListener {
            startActivity(Intent(requireActivity(), SearchActivity::class.java))
        }
    }

    private fun loadRows() {
        rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        adapter = rowsAdapter

        lifecycleScope.launch {
            val rows = viewModel.getHomeRows()
            rows.forEach { contentRow ->
                val cardPresenter = MovieCardPresenter()
                val listRowAdapter = ArrayObjectAdapter(cardPresenter)
                contentRow.items.forEach { listRowAdapter.add(it) }
                val headerItem = HeaderItem(contentRow.title)
                rowsAdapter.add(ListRow(headerItem, listRowAdapter))
            }
        }
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row?
        ) {
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

    private inner class ItemViewSelectedListener : OnItemViewSelectedListener {
        override fun onItemSelected(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row?
        ) {
            // Could update background here based on selected item
        }
    }
}
