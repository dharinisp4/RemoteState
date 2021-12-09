package com.example.assignment.appModules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.example.assignment.apis.ApiInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun getApiClient(): Retrofit {
        val okHttpClient = OkHttpClient()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return Retrofit.Builder()
            .baseUrl("https://api.mystral.in/tt/mobile/logistics/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                okHttpClient.newBuilder()
                    .connectTimeout(2 * 60, TimeUnit.SECONDS)
                    .readTimeout(2 * 60, TimeUnit.SECONDS)
                    .writeTimeout(2 * 60, TimeUnit.SECONDS)
                    .build()
            ).build()

    }

    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)


}
