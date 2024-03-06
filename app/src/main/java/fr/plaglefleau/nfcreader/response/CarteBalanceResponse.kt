package fr.plaglefleau.nfcreader.response

import com.google.gson.annotations.SerializedName
data class CarteBalanceResponse(
    @SerializedName("cardBalance")
    val cardBalance: Double? = null,
    @SerializedName("responseString")
    val responseString: String = ""

) {
    override fun toString(): String {
        return "($cardBalance) $responseString \n"
    }
}