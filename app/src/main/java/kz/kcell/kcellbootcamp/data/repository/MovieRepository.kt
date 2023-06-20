package kz.kcell.kcellbootcamp.data.repository

import kz.kcell.kcellbootcamp.data.local.MovieDao
import kz.kcell.kcellbootcamp.data.remote.MovieRemoteDataSource
import kz.kcell.kcellbootcamp.utils.performGetOperation
import javax.inject.Inject

class  MovieRepository @Inject constructor(
    private val remoteDataSource: MovieRemoteDataSource,
    private val localDataSource: MovieDao
) {
    fun getMovies() = performGetOperation(
        databaseQuery = { localDataSource.getMovies() },
        networkCall = { remoteDataSource.getMovies() },
        saveCallResult = { localDataSource.insertAll(it.movies) }
    )

    fun getMovie(id: Int) = performGetOperation(
        databaseQuery = { localDataSource.getMovie(id) },
        networkCall = { remoteDataSource.getMovie(id) },
        saveCallResult = { localDataSource.insert(it) }
    )
}