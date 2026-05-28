package com.stigstream.tv.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.leanback.widget.Presenter
import com.stigstream.tv.R
import com.stigstream.tv.data.model.Movie

class MovieDetailsPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.presenter_movie_details, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val movie = item as Movie
        val view = viewHolder.view

        view.findViewById<TextView>(R.id.detail_title).text = movie.title
        view.findViewById<TextView>(R.id.detail_meta).text =
            buildString {
                append(movie.year)
                if (movie.duration.isNotEmpty()) append("  •  ${movie.duration}")
                if (movie.rating.isNotEmpty()) append("  •  ⭐ ${movie.rating}")
                if (movie.genre.isNotEmpty()) append("  •  ${movie.genre}")
                if (movie.quality.isNotEmpty()) append("  •  ${movie.quality}")
            }
        view.findViewById<TextView>(R.id.detail_description).text = movie.description
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {}
}
