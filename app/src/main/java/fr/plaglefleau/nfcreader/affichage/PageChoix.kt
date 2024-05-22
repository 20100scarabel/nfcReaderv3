package fr.plaglefleau.nfcreader.affichage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class EnvoieLoginPwdResponse(
    val PWDLoginBool: String,
    val id: String
)

@Composable
fun PageChoix(response: EnvoieLoginPwdResponse) {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Affiche les boutons de choix en fonction de l'ID
            when (response.id) {
                "1" -> {
                    Button(onClick = { /* handle action */ }, modifier = Modifier.padding(80.dp)) {
                        Text("Sold de la Carte")
                    }
                    Button(onClick = { /* handle action */ }, modifier = Modifier.padding(80.dp)) {
                        Text("Bénévole Débiteur")
                    }
                }
                "2" -> {
                    Button(onClick = { /* handle action */ }, modifier = Modifier.padding(50.dp)) {
                        Text("Sold de la Carte")
                    }
                    Button(onClick = { /* handle action */ }, modifier = Modifier.padding(50.dp)) {
                        Text("Bénévole Débiteur")
                    }
                    Button(onClick = { /* handle action */ }, modifier = Modifier.padding(50.dp)) {
                        Text("Bénévole Créditeur")
                    }
                }
                else -> {
                    Text("ID non reconnu: ${response.id}")
                }
            }
        }
    }
}

@Preview
@Composable
fun PageChoixPreview() {
    val response = remember { EnvoieLoginPwdResponse("true", "2") } // Simuler une réponse avec un ID de 1
    PageChoix(response = response)
}
