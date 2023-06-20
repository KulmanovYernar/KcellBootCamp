package kz.kcell.kcellbootcamp.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kz.kcell.kcellbootcamp.BuildConfig
import kz.kcell.kcellbootcamp.data.local.AppDatabase
import kz.kcell.kcellbootcamp.data.local.MovieDao
import kz.kcell.kcellbootcamp.data.remote.MovieApiService
import kz.kcell.kcellbootcamp.data.remote.MovieRemoteDataSource
import kz.kcell.kcellbootcamp.data.repository.MovieRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Singleton
    @Provides
    fun provideOkHttpClient(chuckerInterceptor: ChuckerInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(chuckerInterceptor)
            .build()

    @Singleton
    @Provides
    fun chuckerInterceptor(@ApplicationContext applicationContext: Context): ChuckerInterceptor =
        ChuckerInterceptor.Builder(applicationContext)
            .collector(ChuckerCollector(applicationContext))
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideMoviesService(retrofit: Retrofit): MovieApiService =
        retrofit.create(MovieApiService::class.java)

    @Singleton
    @Provides
    fun provideMovieRemoteDataSource(movieApiService: MovieApiService) =
        MovieRemoteDataSource(movieApiService)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideMovieDao(db: AppDatabase) = db.movieDao()

    @Singleton
    @Provides
    fun provideRepository(
        remoteDataSource: MovieRemoteDataSource,
        localDataSource: MovieDao
    ) = MovieRepository(remoteDataSource, localDataSource)
}