package com.example.julen.nativeappsproject.injection.module

import com.example.julen.nativeappsproject.util.BASE_URL
import com.example.julen.translations.network.TranslateAPI
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Module which provides all required dependencies for the network.
 *
 * Object: Singleton Instance see [The Kotlin reference](https://kotlinlang.org/docs/reference/object-declarations.html)
 * Retrofit: Library used for REST connections. See [The Retrofit reference](https://square.github.io/retrofit/)
 * What is Dependency Injection? See this [video](https://www.youtube.com/watch?v=IKD2-MAkXyQ)
 * Methods annotated with @Provides  informs Dagger that this method is the constructor
 */
@Module
object NetworkModule {


    /**
     * Provides the Metar Service implemenation
     * @param retrofit the retrofit object used to instantiate the service
     */
    @Provides
    internal fun provideTranslateApi(retrofit: Retrofit): TranslateAPI {
        return retrofit.create(TranslateAPI::class.java)
    }


    /**
     * Return the Retrofit object.
     */
    @Provides
    internal fun provideRetrofitInterface(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }

    /**
     * Returns the OkHttpClient
     */
    @Provides
    internal fun provideOkHttpClient(): OkHttpClient {
        //To debug Retrofit/OkHttp we can intercept the calls and log them.
        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
        }.build()
    }

}