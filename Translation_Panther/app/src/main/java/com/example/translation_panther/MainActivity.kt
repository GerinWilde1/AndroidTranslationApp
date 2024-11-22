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
    private lateinit var progressBar: ProgressBar
    private lateinit var etTranslate: EditText
    private lateinit var tvResult: TextView
    private lateinit var btnScan: Button
    private lateinit var btnTranslate: Button
    private lateinit var btnRead: Button
    private lateinit var imageView: ImageView
    private lateinit var fromSpinner: Spinner
    private val PICK_IMAGE_REQUEST_CODE = 123
    lateinit var recognizer: TextRecognizer
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

        etTranslate = findViewById(R.id.etTranslate)
        tvResult = findViewById(R.id.tvResult)
        btnScan = findViewById(R.id.btnScan)
        btnTranslate = findViewById(R.id.btnTranslate)
        btnRead = findViewById(R.id.btnRead)
        imageView = findViewById(R.id.image)

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
            "VIETNAMESE", "CHINESE", "others"
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

        btnScan.setOnClickListener {
            etTranslate.text.clear()
            if (fromLanguage != "others") {
                setupRecognizer(fromLanguage)
                openGallery()
            } else {
                Toast.makeText(this@MainActivity, "TO SCAN YOU HAVE TO CHOOSE A PARTICULAR LANGUAGE", Toast.LENGTH_LONG).show()
            }
        }

        btnTranslate.setOnClickListener {

            val textToTranslate = etTranslate.text.toString()
            if (textToTranslate.isNotEmpty()) {
                if (fromLanguage == "others") {

                    translateText(fromLanguage, toLanguage, textToTranslate)
                }
            else {
                Toast.makeText(this@MainActivity, "Please enter text to translate", Toast.LENGTH_SHORT).show()
            }
        }

        btnRead.setOnClickListener {
            val text = tvResult.text.toString()
            if (text.isEmpty()) {
                Toast.makeText(this@MainActivity, "No text to read", Toast.LENGTH_SHORT).show()
            } else {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }



    // Rest of the utility functions remain unchanged
}
