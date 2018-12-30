package com.example.julen.nativeappsproject.model

import com.squareup.moshi.Json

/**
 * Data class representing the note that is going to be reveived in JSON format from the sever.
 *
 * @constructor  Sets all the properties of the post.
 * @property code The code that the server sends us.
 * @property lang The translation direction. You can set it in either of the following ways:
 *          -As a pair of language codes separated by a hyphen (“from”-“to”). For example, en-ru indicates translating from English to Russian.
 *          -As the target language code (for example, ru). In this case, the service tries to detect the source language automatically.
 * @property text The text to translate.
 */
data class NoteTranslate(@field:Json(name = "code")var code : Int,
                         @field:Json(name = "lang")var lang:  String,
                         @field:Json(name = "text")var text : List<String> )