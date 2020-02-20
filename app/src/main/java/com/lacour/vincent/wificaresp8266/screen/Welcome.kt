package com.lacour.vincent.wificaresp8266.screen

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import android.provider.Settings
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.lacour.vincent.wificaresp8266.R
import com.lacour.vincent.wificaresp8266.connector.CarConnector
import kotlinx.android.synthetic.main.welcome_activity.*
import com.lacour.vincent.wificaresp8266.constant.Constant
import com.lacour.vincent.wificaresp8266.storage.Preferences


//***
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat


import kotlinx.android.synthetic.main.button_control_activity.*
import retrofit2.Retrofit

class Welcome : AppCompatActivity() {
    //private val url = "http:192.168.1.6:8000"
   /// val url = "http://${Constant.IP_ADDRESS_ST0RAGE.default}:${Constant.PORT_STORAGEVIDEO.default}"
    //val url = "http://192.168.1.11:8001"
    //val urlAudio = "http://${Constant.IP_ADDRESS_ST0RAGE.default}:${Constant.PORT_STORAGEAUDIO.default}"
    //val urlAudio = "http://192.168.1.11:8000"

    //*
    private lateinit var carConnector: CarConnector
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_activity)
        setSupportActionBar(findViewById(R.id.toolbar_welcome))

        //*
        val preferences = Preferences(this@Welcome)
        val url = "http://${preferences.getIpAddress()}:${preferences.getPortVideo()}"
        val urlAudio = "http://${preferences.getIpAddress()}:${preferences.getPortAudio()}"

        carConnector = CarConnector(this@Welcome)

        arrow_downMain.setOnTouchListener { v: View, e: MotionEvent -> onTouchArrow(v, e) }
        arrow_upMain.setOnTouchListener { v: View, e: MotionEvent -> onTouchArrow(v, e) }
        arrow_leftMain.setOnTouchListener { v: View, e: MotionEvent -> onTouchArrow(v, e) }
        arrow_rightMain.setOnTouchListener { v: View, e: MotionEvent -> onTouchArrow(v, e) }

        arrow_up2Main.setOnTouchListener { v: View, e: MotionEvent -> onTouchAction(v, e) }
        arrow_right2Main.setOnTouchListener { v: View, e: MotionEvent -> onTouchAction(v, e) }
        arrow_left2Main.setOnTouchListener { v: View, e: MotionEvent -> onTouchAction(v, e) }
        arrow_down2Main.setOnTouchListener { v: View, e: MotionEvent -> onTouchAction(v, e) }

        arrow_right3Main.setOnTouchListener { v: View, e: MotionEvent -> onTouchAction(v, e) }
        arrow_left3Main.setOnTouchListener { v: View, e: MotionEvent -> onTouchAction(v, e) }
        arrow_down3Main.setOnTouchListener { v: View, e: MotionEvent -> onTouchAction(v, e) }


        //*



        temperature.setOnClickListener {
            val intent = Intent(this, Temperature::class.java)
            startActivity(intent)
            this@Welcome.overridePendingTransition(
                R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_left
            )
        }
        menu_button.setOnClickListener {
            val intent = Intent(this, ButtonControl::class.java)
            startActivity(intent)
            this@Welcome.overridePendingTransition(
                R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_left
            )
        }
        menu_accelerometer.setOnClickListener {
            val intent = Intent(this, AccelerometerControl::class.java)
            startActivity(intent)
            this@Welcome.overridePendingTransition(
                R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_left
            )
        }
        menu_voice.setOnClickListener {
            val launchIntent =
                packageManager.getLaunchIntentForPackage("com.rtpmic")
            if (launchIntent !=null){
                startActivity(launchIntent)
            }
        }
        //VIDEOAPP
        // Get the web view settings instance
        val settings = web_view.settings
        // Enable java script in web view
        settings.javaScriptEnabled = true
        // Enable and setup web view cache
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.setAppCachePath(cacheDir.path)
        // Enable zooming in web view
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = true
        // Zoom web view text
        settings.textZoom = 125
        // Enable disable images in web view
        settings.blockNetworkImage = false
        // Whether the WebView should load image resources
        settings.loadsImagesAutomatically = true
        // More web view settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.safeBrowsingEnabled = true  // api 26
        }
        //settings.pluginState = WebSettings.PluginState.ON
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.domStorageEnabled = true
        settings.setSupportMultipleWindows(true)
        settings.loadWithOverviewMode = true
        settings.allowContentAccess = true
        settings.setGeolocationEnabled(true)
        //settings.allowUniversalAccessFromFileURLs = true
        settings.allowFileAccess = true
        // WebView settings
        web_view.fitsSystemWindows = true
        web_view.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        web_view.webViewClient = object: WebViewClient(){
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                // Page loading started
                // Do something
                toast("Page loading.")

                // Enable disable back forward button
                // button_back.isEnabled = web_view.canGoBack()
                //button_forward.isEnabled = web_view.canGoForward()
            }

            override fun onPageFinished(view: WebView, url: String) {
                // Page loading finished
                // Display the loaded page title in a toast message
                toast("Page loaded: ${view.title}")

                // Enable disable back forward button
                // button_back.isEnabled = web_view.canGoBack()
                // button_forward.isEnabled = web_view.canGoForward()
                web_view.loadUrl("javascript:(function() { " +
                        "document.getElementById('pi').style.display='none'; "+
                        "document.getElementById('video').style.width='100%'; "+
                        "document.getElementById('video').style.height='100%'; "+
                        "})()");
            }
        }
        web_view.webChromeClient = object: WebChromeClient(){
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progress_bar.progress = newProgress
            }
        }
        button_load.setOnClickListener{
            // Load url in a web view
            web_view.loadUrl(url)
        }




        //AUDIOAPP
        // Get the web view settings instance
        val settings2 = web_viewAudio.settings
        // Enable java script in web view
        settings2.javaScriptEnabled = true
        // Enable and setup web view cache
        settings2.setAppCacheEnabled(true)
        settings2.cacheMode = WebSettings.LOAD_DEFAULT
        settings2.setAppCachePath(cacheDir.path)
        // Enable zooming in web view
        settings2.setSupportZoom(true)
        settings2.builtInZoomControls = true
        settings2.displayZoomControls = true
        // Zoom web view text
        settings2.textZoom = 125
        // Enable disable images in web view
        settings2.blockNetworkImage = false
        // Whether the WebView should load image resources
        settings2.loadsImagesAutomatically = true
        // More web view settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings2.safeBrowsingEnabled = true  // api 26
        }
        //settings.pluginState = WebSettings.PluginState.ON
        settings2.useWideViewPort = true
        settings2.loadWithOverviewMode = true
        settings2.javaScriptCanOpenWindowsAutomatically = true
        //settings.mediaPlaybackRequiresUserGesture = false
        // More optional settings, you can enable it by yourself
        settings2.domStorageEnabled = true
        settings2.setSupportMultipleWindows(true)
        settings2.loadWithOverviewMode = true
        settings2.allowContentAccess = true
        settings2.setGeolocationEnabled(true)
        //settings.allowUniversalAccessFromFileURLs = true
        settings2.allowFileAccess = true
        // WebView settings
        web_viewAudio.fitsSystemWindows = true
        /*
            if SDK version is greater of 19 then activate hardware acceleration
            otherwise activate software acceleration
        */
        web_viewAudio.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        // Set web view client
        web_viewAudio.webViewClient = object: WebViewClient(){
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                // Page loading started
                // Do something
                toast("Page Audio loading.")

                // Enable disable back forward button
                // button_back.isEnabled = web_view.canGoBack()
                //button_forward.isEnabled = web_view.canGoForward()
            }

            override fun onPageFinished(view: WebView, url: String) {
                // Page loading finished
                // Display the loaded page title in a toast message
                toast("Page loaded: ${view.title}")

                // Enable disable back forward button
                // button_back.isEnabled = web_view.canGoBack()
                // button_forward.isEnabled = web_view.canGoForward()
                web_view.loadUrl("javascript:(function() { " +
                        "document.getElementById('botonrojodd').style.height='100%'; "+
                        "})()");
            }
        }
        // Set web view chrome client
        web_viewAudio.webChromeClient = object: WebChromeClient(){
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progress_bar.progress = newProgress
            }
        }
        // Load button click listener
        audio.setOnClickListener{
      //      // Load url in a web view
            web_viewAudio.loadUrl(urlAudio)
        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_welcome_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.action_wifi -> {
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                startActivity(intent)
                this@Welcome.overridePendingTransition(
                    R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left
                )
                true
            }
            R.id.action_configuration -> {
                val intent = Intent(this, Configuration::class.java)
                startActivity(intent)
                this@Welcome.overridePendingTransition(
                    R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    //*

    private fun onTouchArrow(v: View, event: MotionEvent): Boolean {
        val isTouchDown = event.action == MotionEvent.ACTION_DOWN
        val isTouchUp = event.action == MotionEvent.ACTION_UP
        if (isTouchDown) {
            when (v.id) {

                R.id.arrow_downMain -> {
                    carConnector.moveBackward()
                    arrow_downMain.setBackgroundResource(R.drawable.arrow_down_pressed)
                }
                R.id.arrow_upMain -> {
                    carConnector.moveForward()
                    arrow_upMain.setBackgroundResource(R.drawable.arrow_up_pressed)
                }
                R.id.arrow_rightMain -> {
                    carConnector.turnRight()
                    arrow_rightMain.setBackgroundResource(R.drawable.arrow_right_pressed)
                }
                R.id.arrow_leftMain -> {
                    carConnector.turnLeft()
                    arrow_leftMain.setBackgroundResource(R.drawable.arrow_left_pressed)
                }
            }
            return true
        }
        if (isTouchUp) {
            carConnector.stopMoving()
            when (v.id) {
                R.id.arrow_upMain -> arrow_upMain.setBackgroundResource(R.drawable.arrow_up)
                R.id.arrow_downMain -> arrow_downMain.setBackgroundResource(R.drawable.arrow_down)
                R.id.arrow_rightMain -> arrow_rightMain.setBackgroundResource(R.drawable.arrow_right)
                R.id.arrow_leftMain -> arrow_leftMain.setBackgroundResource(R.drawable.arrow_left)
            }
            return true
        }
        return false
    }

    fun onTouchAction(v: View, event: MotionEvent): Boolean {
        val isTouchDown = event.action == MotionEvent.ACTION_DOWN
        val isTouchUp = event.action == MotionEvent.ACTION_UP

        if (isTouchDown) {
            //  val orangeColor = ContextCompat.getColor(this, R.color.colorOrange)

            //count++
            when (v.id) {
                R.id.arrow_up2Main -> {
                    carConnector.actionOne()
                    //arrow_up2.setTextColor(orangeColor)
                }
                R.id.arrow_right2Main-> {
                    carConnector.actionTwo()
                    //arrow_right2.setTextColor(orangeColor)
                }
                R.id.arrow_left2Main-> {
                    carConnector.actionThree()
                    // arrow_left2.setTextColor(orangeColor)
                }

                R.id.arrow_down2Main -> {
                    carConnector.actionFour()
                    //action_button_4.setTextColor(orangeColor)
                }
                R.id.arrow_right3Main -> {
                    carConnector.actionFour()
                    //action_button_4.setTextColor(orangeColor)
                }

                R.id.arrow_left3Main -> {
                    carConnector.actionFive()
                    //action_button_5.setTextColor(orangeColor)
                }
                R.id.arrow_down3Main -> {
                    carConnector.actionSix()
                    //action_button_6.setTextColor(orangeColor)
                }
            }
            return true
        }
        if (isTouchUp) {
            //val whiteColor = ContextCompat.getColor(this, R.color.colorWhite)
            when (v.id) {
                //  R.id.arrow_up2 -> arrow_up2.setTextColor(whiteColor)
                //R.id.action_button_2 -> action_button_2.setTextColor(whiteColor)
                //  R.id.action_button_3 -> action_button_3.setTextColor(whiteColor)
                //R.id.action_button_4 -> action_button_4.setTextColor(whiteColor)
                //R.id.action_button_5 -> action_button_5.setTextColor(whiteColor)
                //R.id.action_button_6 -> action_button_6.setTextColor(whiteColor)
                //R.id.action_button_7 -> action_button_7.setTextColor(whiteColor)
                //R.id.action_button_8 -> action_button_8.setTextColor(whiteColor)
            }
            return true
        }
        return false
    }








}
// Extension function to show toast message
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

