package com.example.data.mapers

import com.example.data.apimodels.auth.LoginErrorApiModel
import com.fasterxml.jackson.databind.ObjectMapper
import org.json.JSONObject

class ErrorLoginMapper {

    // This function takes json login error object and takes error message from  json object
    fun mapApiErrorMessageToString(loginErrorJson: String): String {
        val jsonObject = JSONObject(loginErrorJson)
        return jsonObject.getString("status_message")
    }
}