package fr.plaglefleau.nfcreader.response

import retrofit2.http.Body

data class EnvoieTag(@Body val TAG: String, val MODIF_SOLDE: String)


