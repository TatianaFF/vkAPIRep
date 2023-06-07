package com.example.vkapistart.commands

import com.example.vkapistart.models.VKAudio
import com.example.vkapistart.models.VKUser
import com.vk.api.sdk.VKApiJSONResponseParser
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.auth.VKScope
import com.vk.api.sdk.exceptions.VKApiIllegalResponseException
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONException
import org.json.JSONObject

class VKAudioCommand(val id: Long): ApiCommand<List<VKAudio>>() {
    override fun onExecute(manager: VKApiManager): List<VKAudio> {
        val call = VKMethodCall.Builder()
            .method("users.getSubscriptions")
            .args("owner_id", id)
            .args("fields", "group")
            .version(manager.config.version)
            .build()
        return manager.execute(call, ResponseApiParser())
    }

    private class ResponseApiParser : VKApiJSONResponseParser<List<VKAudio>> {
        override fun parse(responseJson: JSONObject): List<VKAudio> {
            try {
                val ja = responseJson.getJSONArray("response")
                val r = ArrayList<VKAudio>(ja.length())
                for (i in 0 until ja.length()) {
                    val audio = VKAudio.parse(ja.getJSONObject(i))
                    r.add(audio)
                }
                return r
            } catch (ex: JSONException) {
                throw VKApiIllegalResponseException(ex)
            }
        }
    }
}