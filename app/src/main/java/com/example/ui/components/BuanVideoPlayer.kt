package com.example.ui.components

import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.view.Surface
import android.view.TextureView
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun OnboardingVideoPlayer(
    videoUrl: String,
    fallbackImageRes: Int,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    var isVideoReady by remember(videoUrl) { mutableStateOf(false) }
    var activeMediaPlayer by remember(videoUrl) { mutableStateOf<MediaPlayer?>(null) }

    LaunchedEffect(isActive, activeMediaPlayer, isVideoReady) {
        val player = activeMediaPlayer
        if (player != null && isVideoReady) {
            try {
                if (isActive) {
                    if (!player.isPlaying) {
                        player.start()
                    }
                } else {
                    if (player.isPlaying) {
                        player.pause()
                    }
                }
            } catch (e: Exception) {
                // Ignore player state exception
            }
        }
    }

    Box(modifier = modifier) {
        // High quality fallback / poster image (always visible until video renders, and on offline/error)
        Image(
            painter = painterResource(id = fallbackImageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Keyed on videoUrl to recreate player when URL changes (e.g. Day <-> Night)
        key(videoUrl) {
            AndroidView(
                factory = { ctx ->
                    val textureView = TextureView(ctx)
                    textureView.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                        private var mediaPlayer: MediaPlayer? = null
                        private var surface: Surface? = null

                        override fun onSurfaceTextureAvailable(st: SurfaceTexture, width: Int, height: Int) {
                            surface?.release()
                            val surf = Surface(st)
                            surface = surf
                            try {
                                val mp = MediaPlayer()
                                mediaPlayer = mp
                                mp.setSurface(surf)
                                mp.setDataSource(ctx, Uri.parse(videoUrl))
                                mp.isLooping = true
                                mp.setVolume(0f, 0f) // Silent autoplay for onboarding background video
                                mp.setAudioAttributes(
                                    AudioAttributes.Builder()
                                        .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                                        .setUsage(AudioAttributes.USAGE_MEDIA)
                                        .build()
                                )
                                mp.setOnPreparedListener { player ->
                                    activeMediaPlayer = player
                                    isVideoReady = true
                                    if (isActive) {
                                        try {
                                            player.start()
                                        } catch (e: Exception) {
                                            // Ignore
                                        }
                                    }
                                    adjustAspectRatio(textureView, width, height, player.videoWidth, player.videoHeight)
                                }
                                mp.setOnVideoSizeChangedListener { _, vWidth, vHeight ->
                                    adjustAspectRatio(textureView, width, height, vWidth, vHeight)
                                }
                                mp.setOnErrorListener { _, _, _ ->
                                    isVideoReady = false
                                    true // Handled error gracefully without popping system dialog
                                }
                                mp.prepareAsync()
                            } catch (e: Exception) {
                                isVideoReady = false
                            }
                        }

                        override fun onSurfaceTextureSizeChanged(st: SurfaceTexture, width: Int, height: Int) {
                            mediaPlayer?.let { mp ->
                                adjustAspectRatio(textureView, width, height, mp.videoWidth, mp.videoHeight)
                            }
                        }

                        override fun onSurfaceTextureDestroyed(st: SurfaceTexture): Boolean {
                            activeMediaPlayer = null
                            try {
                                mediaPlayer?.stop()
                                mediaPlayer?.reset()
                                mediaPlayer?.release()
                            } catch (e: Exception) {
                                // Ignore release errors
                            }
                            mediaPlayer = null
                            surface?.release()
                            surface = null
                            return true
                        }

                        override fun onSurfaceTextureUpdated(st: SurfaceTexture) {}

                        private fun adjustAspectRatio(view: TextureView, viewWidth: Int, viewHeight: Int, videoWidth: Int, videoHeight: Int) {
                            if (viewWidth <= 0 || viewHeight <= 0 || videoWidth <= 0 || videoHeight <= 0) return
                            val viewRatio = viewWidth.toFloat() / viewHeight
                            val videoRatio = videoWidth.toFloat() / videoHeight
                            val matrix = Matrix()
                            val scaleX: Float
                            val scaleY: Float
                            if (videoRatio > viewRatio) {
                                scaleX = videoRatio / viewRatio
                                scaleY = 1f
                            } else {
                                scaleX = 1f
                                scaleY = viewRatio / videoRatio
                            }
                            matrix.setScale(scaleX, scaleY, viewWidth / 2f, viewHeight / 2f)
                            view.setTransform(matrix)
                        }
                    }
                    textureView
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * High-performance full-bleed video player for home screen sliding banner with looping,
 * silent playback, aspect-ratio scaling (no letterboxing), and poster image fallback.
 */
@Composable
fun BuanBannerVideoPlayer(
    videoUrl: String,
    fallbackImageRes: Int? = null,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    var isVideoReady by remember(videoUrl) { mutableStateOf(false) }
    var activeMediaPlayer by remember(videoUrl) { mutableStateOf<MediaPlayer?>(null) }

    LaunchedEffect(isActive, activeMediaPlayer, isVideoReady) {
        val player = activeMediaPlayer
        if (player != null && isVideoReady) {
            try {
                if (isActive) {
                    if (!player.isPlaying) {
                        player.start()
                    }
                } else {
                    if (player.isPlaying) {
                        player.pause()
                    }
                }
            } catch (e: Exception) {
                // Ignore player state exception
            }
        }
    }

    Box(modifier = modifier) {
        // High quality fallback / poster image until video buffers and prepares
        if (fallbackImageRes != null) {
            Image(
                painter = painterResource(id = fallbackImageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.linearGradient(
                            listOf(
                                androidx.compose.ui.graphics.Color(0xFF0B1F3D),
                                androidx.compose.ui.graphics.Color(0xFF0F172A)
                            )
                        )
                    )
            )
        }

        key(videoUrl) {
            AndroidView(
                factory = { ctx ->
                    val textureView = TextureView(ctx)
                    textureView.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                        private var mediaPlayer: MediaPlayer? = null
                        private var surface: Surface? = null

                        override fun onSurfaceTextureAvailable(st: SurfaceTexture, width: Int, height: Int) {
                            surface?.release()
                            val surf = Surface(st)
                            surface = surf
                            try {
                                val mp = MediaPlayer()
                                mediaPlayer = mp
                                mp.setSurface(surf)
                                mp.setDataSource(ctx, Uri.parse(videoUrl))
                                mp.isLooping = true
                                mp.setVolume(0f, 0f) // Silent ambient autoplay
                                mp.setAudioAttributes(
                                    AudioAttributes.Builder()
                                        .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                                        .setUsage(AudioAttributes.USAGE_MEDIA)
                                        .build()
                                )
                                mp.setOnPreparedListener { player ->
                                    activeMediaPlayer = player
                                    isVideoReady = true
                                    if (isActive) {
                                        try {
                                            player.start()
                                        } catch (e: Exception) {
                                            // Ignore
                                        }
                                    }
                                    adjustAspectRatio(textureView, width, height, player.videoWidth, player.videoHeight)
                                }
                                mp.setOnCompletionListener { player ->
                                    try {
                                        player.start() // Loop seamlessly
                                    } catch (e: Exception) {
                                        // Ignore
                                    }
                                }
                                mp.setOnVideoSizeChangedListener { _, vWidth, vHeight ->
                                    adjustAspectRatio(textureView, width, height, vWidth, vHeight)
                                }
                                mp.setOnErrorListener { _, _, _ ->
                                    isVideoReady = false
                                    true
                                }
                                mp.prepareAsync()
                            } catch (e: Exception) {
                                isVideoReady = false
                            }
                        }

                        override fun onSurfaceTextureSizeChanged(st: SurfaceTexture, width: Int, height: Int) {
                            mediaPlayer?.let { mp ->
                                adjustAspectRatio(textureView, width, height, mp.videoWidth, mp.videoHeight)
                            }
                        }

                        override fun onSurfaceTextureDestroyed(st: SurfaceTexture): Boolean {
                            activeMediaPlayer = null
                            try {
                                mediaPlayer?.stop()
                                mediaPlayer?.reset()
                                mediaPlayer?.release()
                            } catch (e: Exception) {
                                // Ignore
                            }
                            mediaPlayer = null
                            surface?.release()
                            surface = null
                            return true
                        }

                        override fun onSurfaceTextureUpdated(st: SurfaceTexture) {}

                        private fun adjustAspectRatio(view: TextureView, viewWidth: Int, viewHeight: Int, videoWidth: Int, videoHeight: Int) {
                            if (viewWidth <= 0 || viewHeight <= 0 || videoWidth <= 0 || videoHeight <= 0) return
                            val viewRatio = viewWidth.toFloat() / viewHeight
                            val videoRatio = videoWidth.toFloat() / videoHeight
                            val matrix = Matrix()
                            val scaleX: Float
                            val scaleY: Float
                            if (videoRatio > viewRatio) {
                                scaleX = videoRatio / viewRatio
                                scaleY = 1f
                            } else {
                                scaleX = 1f
                                scaleY = viewRatio / videoRatio
                            }
                            matrix.setScale(scaleX, scaleY, viewWidth / 2f, viewHeight / 2f)
                            view.setTransform(matrix)
                        }
                    }
                    textureView
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
