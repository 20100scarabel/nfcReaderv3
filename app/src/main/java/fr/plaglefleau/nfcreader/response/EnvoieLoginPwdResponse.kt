package fr.plaglefleau.nfcreader.response

import com.google.gson.annotations.SerializedName


data class EnvoieLoginPwdResponse(

    //@SerializedName("PDW") val PDW: String,
    @SerializedName("authentifie") val PWDLoginBool: String,
    @SerializedName("id_droit") val id: String


){
    override fun toString(): String {
        return "($PWDLoginBool) $($id)"
    }
}



