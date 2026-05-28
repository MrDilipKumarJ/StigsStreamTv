package com.stigstream.tv.ui.detail

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.stigstream.tv.R

class DetailActivity : FragmentActivity() {

    companion object {
        const val EXTRA_MOVIE_ID          = "movie_id"
        const val EXTRA_MOVIE_TITLE       = "movie_title"
        const val EXTRA_MOVIE_POSTER      = "movie_poster"
        const val EXTRA_MOVIE_BACKDROP    = "movie_backdrop"
        const val EXTRA_MOVIE_YEAR        = "movie_year"
        const val EXTRA_MOVIE_RATING      = "movie_rating"
        const val EXTRA_MOVIE_GENRE       = "movie_genre"
        const val EXTRA_MOVIE_DURATION    = "movie_duration"
        const val EXTRA_MOVIE_DESCRIPTION = "movie_description"
        const val EXTRA_MOVIE_STREAM_URL  = "movie_stream_url"
        const val EXTRA_MOVIE_QUALITY     = "movie_quality"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.detail_fragment, DetailFragment())
                .commitNow()
        }
    }
}
