package com.example.vkapistart.commands

import com.example.vkapistart.models.VKUser
import com.vk.api.sdk.VKApiJSONResponseParser
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.exceptions.VKApiIllegalResponseException
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONException
import org.json.JSONObject

class VKUsersCommand(val id: Long): ApiCommand<List<VKUser>>() {
    override fun onExecute(manager: VKApiManager): List<VKUser> {

        val call = VKMethodCall.Builder()
            .method("users.get")
            .args("user_ids", id)
            .version(manager.config.version)
            .build()
        return manager.execute(call, ResponseApiParser())
    }

    companion object {
        const val CHUNK_LIMIT = 900
    }

    private class ResponseApiParser : VKApiJSONResponseParser<List<VKUser>> {
        override fun parse(responseJson: JSONObject): List<VKUser> {
            try {
                val ja = responseJson.getJSONArray("response")
                val r = ArrayList<VKUser>(ja.length())
                for (i in 0 until ja.length()) {
                    val user = VKUser.parse(ja.getJSONObject(i))
                    r.add(user)
                }
                return r
            } catch (ex: JSONException) {
                throw VKApiIllegalResponseException(ex)
            }
        }
    }
}