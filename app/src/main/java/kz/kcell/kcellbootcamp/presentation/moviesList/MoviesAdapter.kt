package kz.kcell.kcellbootcamp.presentation.moviesList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kz.kcell.kcellbootcamp.BuildConfig
import kz.kcell.kcellbootcamp.data.entities.Movie
import kz.kcell.kcellbootcamp.databinding.MovieItemBinding
import kz.kcell.kcellbootcamp.utils.loadImage
import kz.kcell.kcellbootcamp.utils.orZero
import java.time.format.DateTimeFormatter


class MoviesAdapter(
    private val onClick: (item: Movie) -> Unit
) : ListAdapter<Movie, MoviesAdapter.MovieViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = MovieViewHolder(
        onClick,
        MovieItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: MoviesAdapter.MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(
        private val onClick: (item: Movie) -> Unit,
        private val binding: MovieItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SimpleDateFormat")
        fun bind(item: Movie) = with(binding) {
            root.setOnClickListener {
                onClick.invoke(item)
            }
            movieItemTitle.text = item.title
//            val formatter = DateTimeFormatter.ofPattern("dd MMM uuuu")
//            val localDateTime = LocalDateTime.parse(item.releaseDate)
//
//            val apiDateString = "2023-06-20T10:30:00"
//
//// Define the input date format
//            val inputFormat = DateTimeFormatter.ofPattern("yyyy-mm-dd")
//
//// Parse the API date string to a LocalDateTime object
//            val dateTime = LocalDateTime.parse(item.releaseDate, inputFormat)
//
//// Define the desired output format
//            val outputFormat = DateTimeFormatter.ofPattern("dd-mm-yyyy")
//
//// Format the date to the desired format
//            val formattedDate = dateTime.format(outputFormat)

            movieItemRelease.text = item.releaseDate.format(DateTimeFormatter.ofPattern("dd MMM uuuu"))
            movieItemRatingbar.rating = item.voteAverage.toFloat().orZero()
            movieItemPoster.loadImage(
                BuildConfig.IMAGE_URL + item.posterPath,
                progressBar
            )
        }
    }
}

class DiffUtilCallback : DiffUtil.ItemCallback<Movie>() {

    override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem

}

