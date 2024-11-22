package com.example.translation_panther


import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.*
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var textToSpeech: TextToSpeech

    private lateinit var Translate: EditText
    private lateinit var Result: TextView

    private lateinit var btnTranslate: Button
    private lateinit var btnRead: Button

    private lateinit var fromSpinner: Spinner


    val latinScript = Data.LatinScript
    val chineseLanguages = Data.chineseLanguages
    val devanagariLanguages = Data.devanagariLanguages
    val japaneseLanguages = Data.japaneseLanguages
    val koreanLanguages = Data.koreanLanguage
    val languages = Data.languages

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        Translate = findViewById(R.id.Translator)
        Result = findViewById(R.id.Result)
        btnTranslate = findViewById(R.id.buttonTranslate)
        btnRead = findViewById(R.id.buttonRead)


        val toSpinner: Spinner = findViewById(R.id.TOspinner)
        fromSpinner = findViewById(R.id.FROMSpinner)


        textToSpeech = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech.language = Locale.getDefault()
            } else {
                Toast.makeText(this@MainActivity, "Text-to-speech initialization failed", Toast.LENGTH_SHORT).show()
            }
        }

        val options = arrayOf("AFRIKAANS", "ARABIC", "BELARUSIAN", "BULGARIAN", "BENGALI", "CATALAN", "CZECH", "WELSH", "DANISH", "GERMAN",
            "GREEK", "ENGLISH", "ESPERANTO", "SPANISH", "ESTONIAN", "PERSIAN", "FINNISH", "FRENCH", "IRISH", "GALICIAN",
            "GUJARATI", "HEBREW", "HINDI", "CROATIAN", "HAITIAN", "HUNGARIAN", "INDONESIAN", "ICELANDIC", "ITALIAN",
            "JAPANESE", "GEORGIAN", "KANNADA", "KOREAN", "LITHUANIAN", "LATVIAN", "MACEDONIAN", "MARATHI", "MALAY",
            "MALTESE", "DUTCH", "NORWEGIAN", "POLISH", "PORTUGUESE", "ROMANIAN", "RUSSIAN", "SLOVAK", "SLOVENIAN",
            "ALBANIAN", "SWEDISH", "SWAHILI", "TAMIL", "TELUGU", "THAI", "TAGALOG", "TURKISH", "UKRAINIAN", "URDU",
            "VIETNAMESE", "CHINESE"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        fromSpinner.adapter = adapter
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        toSpinner.adapter = adapter2

        var fromLanguage = ""
        var toLanguage = ""

        fromSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                fromLanguage = options[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        toSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                toLanguage = languages[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        fun translateText(from: String, to: String, text: String) {

            val f = when(from)
            {
                "ENGLISH" -> TranslateLanguage.ENGLISH
                "GERMAN" -> TranslateLanguage.GERMAN
                "HINDI" -> TranslateLanguage.HINDI
                "MARATHI" -> TranslateLanguage.MARATHI
                "AFRIKAANS" -> TranslateLanguage.AFRIKAANS
                "ARABIC" -> TranslateLanguage.ARABIC
                "BELARUSIAN" -> TranslateLanguage.BELARUSIAN
                "BULGARIAN" -> TranslateLanguage.BULGARIAN
                "BENGALI" -> TranslateLanguage.BENGALI
                "CATALAN" -> TranslateLanguage.CATALAN
                "CZECH" -> TranslateLanguage.CZECH
                "WELSH" -> TranslateLanguage.WELSH
                "DANISH" -> TranslateLanguage.DANISH
                "GREEK" -> TranslateLanguage.GREEK
                "ESPERANTO" -> TranslateLanguage.ESPERANTO
                "SPANISH" -> TranslateLanguage.SPANISH
                "ESTONIAN" -> TranslateLanguage.ESTONIAN
                "PERSIAN" -> TranslateLanguage.PERSIAN
                "FINNISH" -> TranslateLanguage.FINNISH
                "FRENCH" -> TranslateLanguage.FRENCH
                "IRISH" -> TranslateLanguage.IRISH
                "GALICIAN" -> TranslateLanguage.GALICIAN
                "GUJARATI" -> TranslateLanguage.GUJARATI
                "HEBREW" -> TranslateLanguage.HEBREW
                "CROATIAN" -> TranslateLanguage.CROATIAN
                "HAITIAN" -> TranslateLanguage.HAITIAN_CREOLE
                "HUNGARIAN" -> TranslateLanguage.HUNGARIAN
                "INDONESIAN" -> TranslateLanguage.INDONESIAN
                "ICELANDIC" -> TranslateLanguage.ICELANDIC
                "ITALIAN" -> TranslateLanguage.ITALIAN
                "JAPANESE" -> TranslateLanguage.JAPANESE
                "GEORGIAN" -> TranslateLanguage.GEORGIAN
                "KANNADA" -> TranslateLanguage.KANNADA
                "KOREAN" -> TranslateLanguage.KOREAN
                "LITHUANIAN" -> TranslateLanguage.LITHUANIAN
                "LATVIAN" -> TranslateLanguage.LATVIAN
                "MACEDONIAN" -> TranslateLanguage.MACEDONIAN
                "MALAY" -> TranslateLanguage.MALAY
                "MALTESE" -> TranslateLanguage.MALTESE
                "DUTCH" -> TranslateLanguage.DUTCH
                "NORWEGIAN" -> TranslateLanguage.NORWEGIAN
                "POLISH" -> TranslateLanguage.POLISH
                "PORTUGUESE" -> TranslateLanguage.PORTUGUESE
                "ROMANIAN" -> TranslateLanguage.ROMANIAN
                "RUSSIAN" -> TranslateLanguage.RUSSIAN
                "SLOVAK" -> TranslateLanguage.SLOVAK
                "SLOVENIAN" -> TranslateLanguage.SLOVENIAN
                "ALBANIAN" -> TranslateLanguage.ALBANIAN
                "SWEDISH" -> TranslateLanguage.SWEDISH
                "SWAHILI" -> TranslateLanguage.SWAHILI
                "TAMIL" -> TranslateLanguage.TAMIL
                "TELUGU" -> TranslateLanguage.TELUGU
                "THAI" -> TranslateLanguage.THAI
                "TAGALOG" -> TranslateLanguage.TAGALOG
                "TURKISH" -> TranslateLanguage.TURKISH
                "UKRAINIAN" -> TranslateLanguage.UKRAINIAN
                "URDU" -> TranslateLanguage.URDU
                "VIETNAMESE" -> TranslateLanguage.VIETNAMESE
                "CHINESE" -> TranslateLanguage.CHINESE
                else -> {
                    TranslateLanguage.ENGLISH
                }
            }
            val t = when (to) {
                "ENGLISH" -> TranslateLanguage.ENGLISH
                "GERMAN" -> TranslateLanguage.GERMAN
                "HINDI" -> TranslateLanguage.HINDI
                "MARATHI" -> TranslateLanguage.MARATHI
                "AFRIKAANS" -> TranslateLanguage.AFRIKAANS
                "ARABIC" -> TranslateLanguage.ARABIC
                "BELARUSIAN" -> TranslateLanguage.BELARUSIAN
                "BULGARIAN" -> TranslateLanguage.BULGARIAN
                "BENGALI" -> TranslateLanguage.BENGALI
                "CATALAN" -> TranslateLanguage.CATALAN
                "CZECH" -> TranslateLanguage.CZECH
                "WELSH" -> TranslateLanguage.WELSH
                "DANISH" -> TranslateLanguage.DANISH
                "GREEK" -> TranslateLanguage.GREEK
                "ESPERANTO" -> TranslateLanguage.ESPERANTO
                "SPANISH" -> TranslateLanguage.SPANISH
                "ESTONIAN" -> TranslateLanguage.ESTONIAN
                "PERSIAN" -> TranslateLanguage.PERSIAN
                "FINNISH" -> TranslateLanguage.FINNISH
                "FRENCH" -> TranslateLanguage.FRENCH
                "IRISH" -> TranslateLanguage.IRISH
                "GALICIAN" -> TranslateLanguage.GALICIAN
                "GUJARATI" -> TranslateLanguage.GUJARATI
                "HEBREW" -> TranslateLanguage.HEBREW
                "CROATIAN" -> TranslateLanguage.CROATIAN
                "HAITIAN" -> TranslateLanguage.HAITIAN_CREOLE
                "HUNGARIAN" -> TranslateLanguage.HUNGARIAN
                "INDONESIAN" -> TranslateLanguage.INDONESIAN
                "ICELANDIC" -> TranslateLanguage.ICELANDIC
                "ITALIAN" -> TranslateLanguage.ITALIAN
                "JAPANESE" -> TranslateLanguage.JAPANESE
                "GEORGIAN" -> TranslateLanguage.GEORGIAN
                "KANNADA" -> TranslateLanguage.KANNADA
                "KOREAN" -> TranslateLanguage.KOREAN
                "LITHUANIAN" -> TranslateLanguage.LITHUANIAN
                "LATVIAN" -> TranslateLanguage.LATVIAN
                "MACEDONIAN" -> TranslateLanguage.MACEDONIAN
                "MALAY" -> TranslateLanguage.MALAY
                "MALTESE" -> TranslateLanguage.MALTESE
                "DUTCH" -> TranslateLanguage.DUTCH
                "NORWEGIAN" -> TranslateLanguage.NORWEGIAN
                "POLISH" -> TranslateLanguage.POLISH
                "PORTUGUESE" -> TranslateLanguage.PORTUGUESE
                "ROMANIAN" -> TranslateLanguage.ROMANIAN
                "RUSSIAN" -> TranslateLanguage.RUSSIAN
                "SLOVAK" -> TranslateLanguage.SLOVAK
                "SLOVENIAN" -> TranslateLanguage.SLOVENIAN
                "ALBANIAN" -> TranslateLanguage.ALBANIAN
                "SWEDISH" -> TranslateLanguage.SWEDISH
                "SWAHILI" -> TranslateLanguage.SWAHILI
                "TAMIL" -> TranslateLanguage.TAMIL
                "TELUGU" -> TranslateLanguage.TELUGU
                "THAI" -> TranslateLanguage.THAI
                "TAGALOG" -> TranslateLanguage.TAGALOG
                "TURKISH" -> TranslateLanguage.TURKISH
                "UKRAINIAN" -> TranslateLanguage.UKRAINIAN
                "URDU" -> TranslateLanguage.URDU
                "VIETNAMESE" -> TranslateLanguage.VIETNAMESE
                "CHINESE" -> TranslateLanguage.CHINESE
                else -> TranslateLanguage.ENGLISH // Default language if not found
            }


            val options = TranslatorOptions.Builder()
                .setSourceLanguage(f)
                .setTargetLanguage(t)
                .build()
            val translator: Translator = Translation.getClient(options)
            val conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()



            translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    translator.translate(text)
                        .addOnSuccessListener { translatedText ->
                            Result.text = translatedText
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Translation failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Model download failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }

        btnTranslate.setOnClickListener {

            val textToTranslate = Translate.text.toString()
            if (textToTranslate.isNotEmpty()) {
                translateText(fromLanguage, toLanguage, textToTranslate)
            }
            else {
                Toast.makeText(this@MainActivity, "Please enter text to translate", Toast.LENGTH_SHORT).show()
            }
        }

        btnRead.setOnClickListener {
            val text = Result.text.toString()
            if (text.isEmpty()) {
                Toast.makeText(this@MainActivity, "No text to read", Toast.LENGTH_SHORT).show()
            } else {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }




}

