package com.movielist.imdb.domain.usecase

import com.movielist.imdb.domain.repository.MoviesRepo
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val repo: MoviesRepo) {
    operator suspend fun invoke(pageNumber: Int) = repo.getRemoteMovies(pageNumber)
}