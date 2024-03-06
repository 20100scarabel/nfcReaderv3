package fr.plaglefleau.nfcreader


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.plaglefleau.nfcreader.response.CarteBalanceResponse
//import fr.plaglefleau.nfcreader.response.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object API {


    private val AdresseURL = "http://10.0.0.130:3000/"
    private val gson : Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private val httpClient : OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    val api =
        Retrofit.Builder()
        .baseUrl(AdresseURL) //adresse du serveur d'application de maxime P
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiCashless::class.java)


    fun parseJsonToCarteBalanceResponse(jsonString: String): CarteBalanceResponse {
        return gson.fromJson(jsonString, CarteBalanceResponse::class.java)
    }




}
/*object API {
    private const val BASE_URL: String = "http://10.0.0.130:3000/"

    private val gson : Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private val httpClient : OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService : ApiService by lazy{
        retrofit.create(ApiService::class.java)
    }*/
//}