package com.example.vkapistart

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.vkapistart.commands.VKAudioCommand
import com.example.vkapistart.commands.VKUsersCommand
import com.example.vkapistart.models.VKAudio
import com.example.vkapistart.models.VKUser
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import java.io.BufferedReader
import java.io.InputStreamReader


class AuthActivity : AppCompatActivity() {
    private val myId = 237598125L
    private val VaryaId = 192120841L
    private val randomChel = 154699155L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)


        authLauncher.launch(arrayListOf(VKScope.WALL, VKScope.PHOTOS, VKScope.FRIENDS, VKScope.AUDIO))
    }

    private val authLauncher = VK.login(this) { result : VKAuthenticationResult ->
        when (result) {
            is VKAuthenticationResult.Success -> {
                showToast("Success")

                showResultPythonScript()
            }
            is VKAuthenticationResult.Failed -> {
                showToast("Failed")
            }
        }
    }

    private fun get10NamesArtists() {

    }

    private fun showResultPythonScript() {
        try {
            if (!Python.isStarted()) {
                Python.start(AndroidPlatform(this))
            }

            val nameArtist = "My Darkest Days"
            val py = Python.getInstance()
            val pyo = py.getModule("pythonScript")
            val obj = pyo.callAttr("scriptGetGenresOfNameArtist", nameArtist)
            showLog(obj.toString())
        } catch (e: Exception) {
            showLog(e.message.toString())
        }



    }

    private fun showUser() {
        VK.execute(VKUsersCommand(myId), object: VKApiCallback<List<VKUser>> {
            override fun success(result: List<VKUser>) {
                showLog(result.toString())
            }
            override fun fail(error: Exception) {
                showLog(error.toString())
            }
        })
    }

    fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    fun showLog(message: String) {
        Log.e("RESULTtag", message)
    }
}