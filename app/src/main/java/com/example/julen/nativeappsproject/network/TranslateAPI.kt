package com.example.julen.translations.network

import com.example.julen.nativeappsproject.model.NoteTranslate
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Query


/**
 * The interface which provides methods to get result of the Yandex server
 */
interface TranslateAPI {

    /**
     * Get the translation from the API
     */
    @POST("translate")
    fun translateRequest(
        @Query("key") key: String,
        @Query("text") text: String,
        @Query("lang") language: String
    ): Observable<NoteTranslate>
}
