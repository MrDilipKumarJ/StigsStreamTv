package com.stigstream.tv.ui.player

import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.stigstream.tv.R

class PlayerActivity : FragmentActivity() {

    companion object {
        const val EXTRA_STREAM_URL    = "stream_url"
        const val EXTRA_MOVIE_TITLE   = "movie_title"
        const val EXTRA_MOVIE_BACKDROP = "movie_backdrop"
    }

    private lateinit var playerView: PlayerView
    private lateinit var player: ExoPlayer
    private lateinit var titleView: TextView

    private var streamUrl: String = ""
    private var movieTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        streamUrl  = intent.getStringExtra(EXTRA_STREAM_URL) ?: ""
        movieTitle = intent.getStringExtra(EXTRA_MOVIE_TITLE) ?: ""

        playerView = findViewById(R.id.player_view)
        titleView  = findViewById(R.id.player_title)
        titleView.text = movieTitle

        setupPlayer()
    }

    private fun setupPlayer() {
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            playerView.player = exoPlayer

            // Build media item — supports HLS, DASH, MP4, and direct links
            val mediaItem = MediaItem.fromUri(Uri.parse(streamUrl.ifEmpty {
                // Fallback demo stream for testing
                "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"
            }))

            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true

            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_BUFFERING -> showBuffering(true)
                        Player.STATE_READY     -> showBuffering(false)
                        Player.STATE_ENDED     -> finish()
                        else -> {}
                    }
                }
            })
        }

        // TV remote controls: hide controller overlay by default, show on D-pad
        playerView.useController = true
        playerView.controllerAutoShow = true
        playerView.controllerHideOnTouch = true
        playerView.controllerShowTimeoutMs = 3000
    }

    private fun showBuffering(show: Boolean) {
        findViewById<View>(R.id.buffering_indicator).visibility =
            if (show) View.VISIBLE else View.GONE
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_DPAD_CENTER,
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> {
                if (player.isPlaying) player.pause() else player.play()
                true
            }
            KeyEvent.KEYCODE_MEDIA_FAST_FORWARD,
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                player.seekTo(player.currentPosition + 10_000)
                true
            }
            KeyEvent.KEYCODE_MEDIA_REWIND,
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                player.seekTo(maxOf(0, player.currentPosition - 10_000))
                true
            }
            KeyEvent.KEYCODE_MEDIA_STOP,
            KeyEvent.KEYCODE_BACK -> {
                finish()
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    override fun onResume() {
        super.onResume()
        player.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}
