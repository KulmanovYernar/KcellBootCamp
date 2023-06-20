package kz.kcell.kcellbootcamp.presentation.moviesDetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kz.kcell.kcellbootcamp.BuildConfig
import kz.kcell.kcellbootcamp.R
import kz.kcell.kcellbootcamp.data.entities.Movie
import kz.kcell.kcellbootcamp.databinding.FragmentMovieDetailBinding
import kz.kcell.kcellbootcamp.utils.Constants.ARG_MOVIE_ID
import kz.kcell.kcellbootcamp.utils.Resource
import kz.kcell.kcellbootcamp.utils.loadImage
import kz.kcell.kcellbootcamp.utils.toast


@AndroidEntryPoint
class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {

    private val binding by viewBinding(FragmentMovieDetailBinding::bind)
    private val viewModel by viewModels<MovieDetailViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt(ARG_MOVIE_ID)?.let { viewModel.id.postValue(it) }
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.content.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    bindMovie(it.data)
                    binding.progressBar.visibility = View.GONE
                }

                Resource.Status.ERROR ->
                    it.message?.let { msg -> toast(requireContext(), msg) }

                Resource.Status.LOADING ->
                    binding.progressBar.visibility = View.VISIBLE
            }
        }
    }


    private fun bindMovie(movie: Movie?) = with(binding) {
        movie?.let {
            movieItemTitle.text = movie.title
            detailDesc.text = movie.overview
            // 12 Dec 1992
           // val formatter = DateTimeFormatterBuilder().appendPattern("dd-MMM-yyyy").toFormatter()


//            val formatter = DateTimeFormatter.ofPattern("yyyy MM dd")
//            val text = movie.releaseDate.format(formatter)
//            val parsedDate = LocalDate.parse(text, formatter)
            movieItemRelease.text = movie.releaseDate// "dd-MMM-yyyy"


            movieItemRatingbar.rating = movie.voteAverage.toFloat()
            movieItemPoster.loadImage(
                imageUrl = BuildConfig.IMAGE_URL + movie.posterPath,
                progressBar = imageProgressBar
            )
        }
    }
}