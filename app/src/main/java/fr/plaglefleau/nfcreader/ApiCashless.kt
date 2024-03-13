package fr.plaglefleau.nfcreader

import fr.plaglefleau.nfcreader.response.CarteBalanceResponse
import retrofit2.Response
import retrofit2.http.*


interface ApiCashless {
/*    @POST("api/data") //route pour recuperer le sold    //@Headers
    suspend fun getSoldeCarte(@Body tagID: String) : Response<CarteBalanceResponse>
    //envoie d'un String codeNFC
    //recupere un Double qui peut etre NULL + un String ^^*/

    @POST("api/data") //route pour recuperer le sold    //@Headers
    suspend fun getSoldeCarte(@Body tagID: String) : Response<CarteBalanceResponse>

    /*@GET("")
    suspend fun getBalance(@Path balance:):Response<CarteBalanceResponse>*/



}