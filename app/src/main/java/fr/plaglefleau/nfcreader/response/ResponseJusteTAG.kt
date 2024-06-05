package fr.plaglefleau.nfcreader.response

import com.google.gson.annotations.SerializedName


data class ResponseJusteTAG(

    //@SerializedName("PDW") val PDW: String,
    @SerializedName("AncienSolde") val responseJusteTAG: String,



){
    override fun toString(): String {
        return "($responseJusteTAG)"
    }
}

