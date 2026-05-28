package com.stigstream.tv.ui.home

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.stigstream.tv.R
import com.stigstream.tv.data.model.Movie

class MovieCardPresenter : Presenter() {

    companion object {
        private const val CARD_WIDTH = 220   // dp
        private const val CARD_HEIGHT = 330  // dp
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val cardView = ImageCardView(parent.context).apply {
            isFocusable = true
            isFocusableInTouchMode = true
            setMainImageDimensions(
                (CARD_WIDTH * parent.context.resources.displayMetrics.density).toInt(),
                (CARD_HEIGHT * parent.context.resources.displayMetrics.density).toInt()
            )
        }
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val movie = item as Movie
        val cardView = viewHolder.view as ImageCardView

        cardView.titleText = movie.title
        cardView.contentText = "${movie.year}  ${movie.rating}★  ${movie.quality}"
        cardView.setMainImageScaleType(android.widget.ImageView.ScaleType.CENTER_CROP)

        Glide.with(cardView.context)
            .load(movie.posterUrl)
            .centerCrop()
            .placeholder(R.drawable.placeholder_movie)
            .error(R.drawable.placeholder_movie)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?, model: Any?,
                    target: Target<Drawable>?, isFirstResource: Boolean
                ) = false

                override fun onResourceReady(
                    resource: Drawable?, model: Any?,
                    target: Target<Drawable>?, dataSource: DataSource?,
                    isFirstResource: Boolean
                ) = false
            })
            .into(cardView.mainImageView!!)
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val cardView = viewHolder.view as ImageCardView
        cardView.badgeImage = null
        cardView.mainImage = null
    }
}
