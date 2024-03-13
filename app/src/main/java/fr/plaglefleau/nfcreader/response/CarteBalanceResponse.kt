package fr.plaglefleau.nfcreader.response

import androidx.compose.material3.TextField
import androidx.compose.ui.text.input.TextFieldValue
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
data class CarteBalanceResponse(



    @SerializedName("cardBalance")
    var cardBalance: Double?,
    @SerializedName("responseString")
    var responseString: String

) {
    override fun toString(): String {

        //this.cardBalance = cardBalance
        //this.responseString = responseString

        return "($cardBalance) $responseString \n"




    }




}

