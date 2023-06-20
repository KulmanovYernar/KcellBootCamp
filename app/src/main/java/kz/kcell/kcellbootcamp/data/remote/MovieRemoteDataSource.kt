package kz.kcell.kcellbootcamp.data.remote



import kz.kcell.kcellbootcamp.BuildConfig
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
    private val movieApiService: MovieApiService
) : BaseDataSource() {

    suspend fun getMovies() = getResult { movieApiService.getPopularMovies(BuildConfig.API_KEY) }

    suspend fun getMovie(id: Int) = getResult { movieApiService.getMovie(id, BuildConfig.API_KEY) }
}