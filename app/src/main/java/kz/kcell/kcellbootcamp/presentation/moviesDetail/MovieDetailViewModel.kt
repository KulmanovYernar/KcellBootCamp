package kz.kcell.kcellbootcamp.presentation.moviesDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.kcell.kcellbootcamp.data.entities.Movie
import kz.kcell.kcellbootcamp.data.local.MovieDao
import kz.kcell.kcellbootcamp.data.remote.MovieRemoteDataSource
import kz.kcell.kcellbootcamp.data.repository.MovieRepository
import kz.kcell.kcellbootcamp.utils.Resource
import javax.inject.Inject
@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository,
) : ViewModel() {

    val id = MutableLiveData<Int>()

    private val _content = id.switchMap {
        repository.getMovie(it)
    }
    val content: LiveData<Resource<Movie>> = _content
}