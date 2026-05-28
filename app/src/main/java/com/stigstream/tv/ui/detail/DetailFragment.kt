package com.stigstream.tv.ui.detail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.app.DetailsSupportFragmentBackgroundController
import androidx.leanback.widget.*
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.stigstream.tv.R
import com.stigstream.tv.data.model.ContentType
import com.stigstream.tv.data.model.Movie
import com.stigstream.tv.ui.home.MovieCardPresenter
import com.stigstream.tv.ui.player.PlayerActivity

class DetailFragment : DetailsSupportFragment() {

    private lateinit var detailsBackground: DetailsSupportFragmentBackgroundController
    private lateinit var presenterSelector: ClassPresenterSelector
    private lateinit var adapter: ArrayObjectAdapter

    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Build Movie from intent extras
        val act = requireActivity()
        movie = Movie(
            id          = act.intent.getStringExtra(DetailActivity.EXTRA_MOVIE_ID) ?: "",
            title       = act.intent.getStringExtra(DetailActivity.EXTRA_MOVIE_TITLE) ?: "",
            posterUrl   = act.intent.getStringExtra(DetailActivity.EXTRA_MOVIE_POSTER) ?: "",
            backdropUrl = act.intent.getStringExtra(DetailActivity.EXTRA_MOVIE_BACKDROP) ?: "",
            year        = act.intent.getStringExtra(DetailActivity.EXTRA_MOVIE_YEAR) ?: "",
            rating      = act.intent.getStringExtra(DetailActivity.EXTRA_MOVIE_RATING) ?: "",
            genre       = act.intent.getStringExtra(DetailActivity.EXTRA_MOVIE_GENRE) ?: "",
            duration    = act.intent.getStringExtra(DetailActivity.EXTRA_MOVIE_DURATION) ?: "",
            description = act.intent.getStringExtra(DetailActivity.EXTRA_MOVIE_DESCRIPTION) ?: "",
            streamUrl   = act.intent.getStringExtra(DetailActivity.EXTRA_MOVIE_STREAM_URL) ?: "",
            quality     = act.intent.getStringExtra(DetailActivity.EXTRA_MOVIE_QUALITY) ?: "HD"
        )

        detailsBackground = DetailsSupportFragmentBackgroundController(this).apply {
            enableParallax()
        }

        presenterSelector = ClassPresenterSelector()
        adapter = ArrayObjectAdapter(presenterSelector)

        setupDetailsOverviewRow()
        setupRelatedRow()
        setupListeners()

        loadBackdrop()
    }

    private fun setupDetailsOverviewRow() {
        val row = DetailsOverviewRow(movie)

        // Load poster into detail overview
        Glide.with(requireActivity())
            .asBitmap()
            .load(movie.posterUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    row.imageDrawable = android.graphics.drawable.BitmapDrawable(resources, resource)

                    // Dynamic color from poster
                    Palette.from(resource).generate { palette ->
                        val swatch = palette?.vibrantSwatch ?: palette?.dominantSwatch
                        swatch?.let {
                            // Could update action button colors here
                        }
                    }
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
            })

        // Action buttons
        val actionsAdapter = ArrayObjectAdapter()
        actionsAdapter.add(Action(ACTION_WATCH,   "▶  Watch Now",  movie.quality))
        actionsAdapter.add(Action(ACTION_TRAILER, "🎬 Trailer",    ""))
        actionsAdapter.add(Action(ACTION_WISHLIST,"♥  Watchlist",  ""))
        row.actionsAdapter = actionsAdapter

        val detailsPresenter = FullWidthDetailsOverviewRowPresenter(MovieDetailsPresenter()).apply {
            backgroundColor = requireContext().getColor(R.color.detail_background)
            isParticipatingEntranceTransition = true
            alignmentMode = FullWidthDetailsOverviewRowPresenter.ALIGN_MODE_MIDDLE
        }

        presenterSelector.addClassPresenter(DetailsOverviewRow::class.java, detailsPresenter)
        adapter.add(row)
        this.adapter = adapter
    }

    private fun setupRelatedRow() {
        val relatedAdapter = ArrayObjectAdapter(MovieCardPresenter())
        // Add some placeholder related items - in production fetch from repo
        relatedAdapter.add(movie.copy(id = "related_1", title = "Related Movie 1"))
        relatedAdapter.add(movie.copy(id = "related_2", title = "Related Movie 2"))

        val header = HeaderItem("More Like This")
        adapter.add(ListRow(header, relatedAdapter))
        presenterSelector.addClassPresenter(ListRow::class.java, ListRowPresenter())
    }

    private fun setupListeners() {
        onActionClickedListener = OnActionClickedListener { action ->
            when (action.id) {
                ACTION_WATCH -> {
                    val intent = Intent(requireActivity(), PlayerActivity::class.java).apply {
                        putExtra(PlayerActivity.EXTRA_STREAM_URL, movie.streamUrl)
                        putExtra(PlayerActivity.EXTRA_MOVIE_TITLE, movie.title)
                        putExtra(PlayerActivity.EXTRA_MOVIE_BACKDROP, movie.backdropUrl)
                    }
                    startActivity(intent)
                }
                ACTION_TRAILER -> {
                    // Launch trailer
                    val intent = Intent(requireActivity(), PlayerActivity::class.java).apply {
                        putExtra(PlayerActivity.EXTRA_STREAM_URL, "https://www.youtube.com/watch?v=dQw4w9WgXcQ")
                        putExtra(PlayerActivity.EXTRA_MOVIE_TITLE, "${movie.title} - Trailer")
                        putExtra(PlayerActivity.EXTRA_MOVIE_BACKDROP, movie.backdropUrl)
                    }
                    startActivity(intent)
                }
                ACTION_WISHLIST -> {
                    // Toggle watchlist
                }
            }
        }
    }

    private fun loadBackdrop() {
        val backdropUrl = movie.backdropUrl.ifEmpty { movie.posterUrl }
        Glide.with(requireActivity())
            .asBitmap()
            .load(backdropUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    detailsBackground.coverBitmap = resource
                    adapter.notifyArrayItemRangeChanged(0, adapter.size())
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    companion object {
        const val ACTION_WATCH    = 1L
        const val ACTION_TRAILER  = 2L
        const val ACTION_WISHLIST = 3L
    }
}
