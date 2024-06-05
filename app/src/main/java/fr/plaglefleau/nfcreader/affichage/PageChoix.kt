package fr.plaglefleau.nfcreader.affichage

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.plaglefleau.nfcreader.API
import fr.plaglefleau.nfcreader.MainActivity
import fr.plaglefleau.nfcreader.response.EnvoieLoginPwd
import fr.plaglefleau.nfcreader.response.EnvoieLoginPwdResponse
import fr.plaglefleau.nfcreader.response.EnvoieTag
import fr.plaglefleau.nfcreader.affichage.responsePWDBool
import fr.plaglefleau.nfcreader.affichage.responseID

//import kotlinx.coroutines.flow.internal.NoOpContinuation.context

/*
data class EnvoieLoginPwdResponse(
    val PWDLoginBool: String,
    val id: String
)
*/


//val  response = EnvoieLoginPwdResponse("", "idState.value")
//@SuppressLint("StaticFieldLeak")
var debiteur : Boolean = false
var information :Boolean = false





@Composable
fun PageChoix() {

    val context = LocalContext.current
    var idState = responseID


    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Affiche les boutons de choix en fonction de l'ID
            when (responseID) {
                "1" -> {
                    Button(onClick = { flag = null
                        information = true

                        //val intent = Intent(context, MainActivity::class.java)
                        //context.startActivity(intent)
                        context.startActivity(Intent(context, MainActivity()::class.java ))
                                     }
                        , modifier = Modifier.padding(50.dp)
                    )
                    {
                        Text("Solde de la Carte")


                    }
                    Button(onClick = {

                        flag = null
                        debiteur = true
                        context.startActivity(Intent(context, MainActivity()::class.java ))}, modifier = Modifier.padding(50.dp)) {
                        Text("Bénévole Débiteur")
                    }
                    Button(
                        onClick = {
                            flag = false

                            context.startActivity(Intent(context, MainActivity()::class.java))
                            },
                        modifier = Modifier.padding(80.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red, // Changer la couleur
                            contentColor = Color.White // Changer la couleur
                        )
                    )
                    {
                        Text("Déconnexion")
                    }
                }
                "2" -> {
                    Button(onClick = {flag = null

                        context.startActivity(Intent(context, MainActivity()::class.java ))}, modifier = Modifier.padding(50.dp)) {
                        Text("Solde de la Carte")
                    }
                    Button(onClick = {

                        flag = null

                        context.startActivity(Intent(context, MainActivity()::class.java ))}, modifier = Modifier.padding(50.dp)) {
                        Text("Bénévole Débiteur")
                    }
                    Button(onClick = {flag = null

                        context.startActivity(Intent(context, MainActivity()::class.java ))}, modifier = Modifier.padding(50.dp)) {
                        Text("Bénévole Créditeur")
                    }
                    Button(
                        onClick = {
                            flag = false


                            context.startActivity(Intent(context, MainActivity()::class.java))
                                  },
                        modifier = Modifier.padding(80.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red, // Changer la couleur
                            contentColor = Color.White // Changer la couleur
                        )
                    )
                    {
                        Text("Déconnexion")
                    }
                }
                else -> {
                    Text("ID non reconnu: ${responseID}")
                }
            }
        }
    }
}



@Preview
@Composable
fun PageChoixPreview() {
    //val response = remember { EnvoieLoginPwdResponse("true", "2") } // Simuler une réponse avec un ID de 1
    //PageChoix(response = response)
}
