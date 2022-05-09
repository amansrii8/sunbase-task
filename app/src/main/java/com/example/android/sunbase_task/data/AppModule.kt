package com.example.android.sunbase_task.data


import android.content.Context
import com.example.android.sunbase_task.data.localdatasource.db.AppDataBase
import com.example.android.sunbase_task.data.localdatasource.db.dao.RemoteKeysDao
import com.example.android.sunbase_task.data.remotedatasource.PhotoService
import com.example.android.sunbase_task.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ronnie.allplayers.data.dao.PhotoDao
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    private val okHttpClient: OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()


    @Provides
    @Singleton
    fun providesDB(@ApplicationContext context: Context): AppDataBase {
        return AppDataBase.invoke(context)
    }

    @Singleton
    @Provides
    fun providesKeysDao(appDataBase: AppDataBase): RemoteKeysDao = appDataBase.remoteKeysDao

    @Singleton
    @Provides
    fun providesDao(appDataBase: AppDataBase): PhotoDao = appDataBase.photoDao

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun providePhotosApi(retrofit: Retrofit): PhotoService = retrofit.create(PhotoService::class.java)
}
