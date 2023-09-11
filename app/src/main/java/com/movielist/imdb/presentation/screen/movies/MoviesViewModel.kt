package com.movielist.imdb.presentation.screen.movies

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.movielist.imdb.domain.data.Movie
import com.movielist.imdb.domain.data.ScreenState
import com.movielist.imdb.domain.usecase.GetMoviesUseCase
import com.movielist.imdb.presentation.viewmodel.BaseViewModel
import com.movielist.imdb.utils.Resource
import com.movielist.imdb.utils.flow.mutableEventFlow
import com.movielist.imdb.utils.pagination.DefaultPaginator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val useCase: GetMoviesUseCase
) : BaseViewModel() {

    private var totalPage by mutableStateOf(1)


    private val _toggleDarkMode = mutableEventFlow<Boolean>()
    val toggleDarkMode: SharedFlow<Boolean> = _toggleDarkMode

    var moviesState by mutableStateOf(ScreenState<Movie>())

    private val paginator = DefaultPaginator(
        initialPage = moviesState.page,
        onLoadUpdated = {
            moviesState = moviesState.copy(isLoading = it)
        },
        onRequest = { nextPage ->
            if (nextPage <= totalPage) {
                val response = useCase(nextPage)

                if (response is Resource.Success) {
                    return@DefaultPaginator response.data.results ?: emptyList()
                } else {
                    emptyList()
                }

            } else {
                return@DefaultPaginator emptyList()
            }
        },
        getNextPage = {
            moviesState.page + 1
        }
    ) { items, newPage ->
        moviesState = moviesState.copy(
            page = newPage,
            endReached = items.isEmpty()
        )
    }

    init {
        viewModelScope.launchCatching(
            block = {
                //first time load from DB if exist
                if (moviesState.page == 1) {
                    (useCase(1) as Resource.Success).data.results?.toMutableList()?.let {
                        moviesState = moviesState.copy(
                            items = it
                        )
                    }
                } else {
                    paginator.loadNextItems()
                }
            },
            onError = {
                moviesState = moviesState.copy(
                    error = it.errorMessage,
                    isLoading = false
                )
            }
        )
    }


    private val _goToMovieDetail = mutableEventFlow<Int>()
    val goToMovieDetail: SharedFlow<Int> = _goToMovieDetail

    fun onMovieClicked(it: Movie) {
        _goToMovieDetail.tryEmit(it.id)
    }

    fun onToggleDarkModeClicked(isDarkMode: Boolean) {
        _toggleDarkMode.tryEmit(isDarkMode.not())
    }

    fun onRetryClicked() {
        // Resetting sort order to fire new data request

    }

}