package fr.plaglefleau.nfcreader.response

import retrofit2.http.Body


data class EEnvoieLoginPwd(@Body val LOGIN: String, val PASSWORD: String)
