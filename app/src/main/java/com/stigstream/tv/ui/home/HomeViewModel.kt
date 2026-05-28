package com.stigstream.tv.ui.home

import androidx.lifecycle.ViewModel
import com.stigstream.tv.data.model.ContentRow
import com.stigstream.tv.data.repository.StigstreamRepository

class HomeViewModel : ViewModel() {
    private val repository = StigstreamRepository()

    suspend fun getHomeRows(): List<ContentRow> = repository.getHomeRows()
}
