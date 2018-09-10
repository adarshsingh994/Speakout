package com.speakout


import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener, OnSeekBarChangeListener, OnClickListener {

    private var speakit : FloatingActionButton? = null
    private var seekPitch : SeekBar? = null
    private var toolBar : Toolbar? = null
    private var seekRate : SeekBar? = null
    private var tts: TextToSpeech? = null
    private var text : EditText? = null

    private var pitch = 1.0
    private var rate = 1.0
    private var isSpeakItButtonAnimating = false

    private val SPEAKITBUTTONVISIBILITYDELAYDURATION : Long = 50
    private val SPEAKITBUTTONHIDESHOWANIMATIONDURATION : Long = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initViews()

        loadActionBar()

        initTts()

        initListeners()
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            showSpeakItButton()
        }, SPEAKITBUTTONVISIBILITYDELAYDURATION)
    }

    override fun onDestroy() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        try{
            when(v!!.id){
                R.id.speak_out_button -> {
                    speakOut()
                }
            }

        }catch (exception : NullPointerException){
            Toast.makeText(this@MainActivity, "Error: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        try{
            when(seekBar!!.id){
                R.id.pitch_seek_bar -> {
                    pitch = progress.toDouble()
                }

                R.id.rate_seek_bar -> {
                    rate = progress.toDouble()
                }
            }
        }catch (exception : NullPointerException){
            Toast.makeText(this@MainActivity, "Error: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        try{
            when(seekBar!!.id){
                R.id.pitch_seek_bar -> {
                }

                R.id.rate_seek_bar -> {
                }
            }
        }catch (exception : NullPointerException){
            Toast.makeText(this@MainActivity, "Error: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        try{
            when(seekBar!!.id){
                R.id.pitch_seek_bar -> {
                    tts!!.setPitch(pitch.toFloat() / 50)
                }

                R.id.rate_seek_bar -> {
                    tts!!.setSpeechRate(rate.toFloat() / 50)
                }
            }
        }catch (exception : NullPointerException){
            Toast.makeText(this@MainActivity, "Error: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)
            when(result){
                TextToSpeech.LANG_MISSING_DATA -> {
                    Toast.makeText(this, "Language data missing", Toast.LENGTH_SHORT).show()
                }

                TextToSpeech.LANG_NOT_SUPPORTED -> {
                    Toast.makeText(this, "Language is not supported", Toast.LENGTH_SHORT).show()
                }

                TextToSpeech.LANG_AVAILABLE -> {
                }

                TextToSpeech.LANG_COUNTRY_AVAILABLE -> {
                    try{
                        speakit!!.isEnabled = true
                    }catch (exception : NullPointerException){
                        Toast.makeText(this@MainActivity, "Error: $exception", Toast.LENGTH_SHORT).show()
                    }
                }

                TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE -> {
                    try{
                        speakit!!.isEnabled = true
                    }catch (exception : NullPointerException){
                        Toast.makeText(this@MainActivity, "Error: $exception", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Initialization Failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        hideSpeakItButton()
        Handler().postDelayed({
            super.onBackPressed()
        }, 200)
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//         menuInflater.inflate(R.menu.main, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        val map = HashMap<String, String>()
//        tts!!.synthesizeToFile(text.text.toString(), map, Environment.getExternalStorageDirectory().toString() + "/" + text.text.toString().substring(0, 4) + "... " + ".mp3", 1)
//        map[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = tt
//        Toast.makeText(applicationContext, "Audio Saved To " + Environment.getExternalStorageDirectory(), Toast.LENGTH_LONG).show()
//
//        return super.onOptionsItemSelected(item)
//
//    }

    private fun loadActionBar(){
        setSupportActionBar(toolBar)

        try{
            toolBar!!.title = getString(R.string.app_name)
        }catch (exception : NullPointerException){
            Toast.makeText(this@MainActivity, "Error: $${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initListeners(){
        try{
            seekPitch!!.setOnSeekBarChangeListener(this)
        }catch (exception : NullPointerException){
            Toast.makeText(this@MainActivity, "Error: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
        }

        try{
            seekRate!!.setOnSeekBarChangeListener(this)
        }catch (exception : NullPointerException){
            Toast.makeText(this@MainActivity, "Error: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
        }

        try{
            speakit!!.setOnClickListener(this)
        }catch (exception : NullPointerException){
            Toast.makeText(this@MainActivity, "Error: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initViews(){
        text = findViewById(R.id.speak_text)
        speakit = findViewById(R.id.speak_out_button)
        seekPitch = findViewById(R.id.pitch_seek_bar)
        seekRate = findViewById(R.id.rate_seek_bar)
        toolBar = findViewById(R.id.main_toolbar)

        try{
            speakit!!.isEnabled = false
        }catch (exception : NullPointerException){
            Toast.makeText(this@MainActivity, "Error: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initTts(){
        tts = TextToSpeech(this, this)
    }

    private fun showSpeakItButton(){
        if(!isSpeakItButtonAnimating){
            try{
                var swipeUp = AnimationUtils.loadAnimation(this@MainActivity, R.anim.abc_slide_in_bottom)
                swipeUp.interpolator = DecelerateInterpolator()
                swipeUp.duration = SPEAKITBUTTONHIDESHOWANIMATIONDURATION
                swipeUp.setAnimationListener(object : Animation.AnimationListener{
                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        isSpeakItButtonAnimating = false
                    }

                    override fun onAnimationStart(animation: Animation?) {
                        speakit!!.visibility = View.VISIBLE
                        isSpeakItButtonAnimating = true
                    }

                })
                speakit!!.startAnimation(swipeUp)
            }catch (exception : NullPointerException){
                Toast.makeText(this@MainActivity, "Error: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hideSpeakItButton(){
        if(!isSpeakItButtonAnimating) {
            try{
                var swipeDown = AnimationUtils.loadAnimation(this@MainActivity, R.anim.abc_slide_out_bottom)
                swipeDown.interpolator = AccelerateInterpolator()
                swipeDown.duration = SPEAKITBUTTONHIDESHOWANIMATIONDURATION
                swipeDown.setAnimationListener(object : Animation.AnimationListener{
                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                    override fun onAnimationStart(animation: Animation?) {
                        isSpeakItButtonAnimating = true
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        speakit!!.visibility = View.GONE
                        isSpeakItButtonAnimating = false
                    }

                })
                speakit!!.startAnimation(swipeDown)
            }catch (exception : NullPointerException){
                Toast.makeText(this@MainActivity, "Errror: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun speakOut() {
        try{
            tts!!.speak(text!!.text.toString(), TextToSpeech.QUEUE_FLUSH, null, "text")
        }catch (exception : NullPointerException){
            Toast.makeText(this@MainActivity, "Error: $exception", Toast.LENGTH_SHORT).show()
        }

    }
}