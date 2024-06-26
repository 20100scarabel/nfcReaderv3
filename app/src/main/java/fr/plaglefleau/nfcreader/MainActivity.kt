package fr.plaglefleau.nfcreader

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.plaglefleau.nfcreader.affichage.*

import fr.plaglefleau.nfcreader.databinding.ActivityMainBinding
import fr.plaglefleau.nfcreader.response.EnvoieTag
import fr.plaglefleau.nfcreader.ui.theme.NfcReaderTheme
import fr.plaglefleau.nfcreader.affichage.PageChoix
import fr.plaglefleau.nfcreader.response.EnvoieJusteTAG
import fr.plaglefleau.nfcreader.response.ResponseJusteTAG


public class MainActivity() : ComponentActivity() {



    private var monIterator : MonIterator = MonIterator()
    private var isLoggedIn : Boolean = false

    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null
    private var tag:String = ""
    private val intentFiltersArray: Array<IntentFilter>? = null
    private val techListsArray: Array<Array<String>>? = null
    private var iterator2: Int = 0
    //public var solde : String =""

    var carteBalance: Double? = null


    val tagValueState = mutableStateOf("")
    val cardBalanceState = mutableStateOf("")
    var solde  = mutableStateOf("")
    //var modifSoldeValue by mutableStateOf("")





    lateinit var binding: ActivityMainBinding




    @RequiresApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        /*GlobalScope.launch(Dispatchers.Main) {
            API.api.getSoldeCarte("046c3e6a546080")
        }*/
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)

            setContent {
                NfcReaderTheme {
                    if (flag == null){
                        Greeting("")
                    }
                    else if (!flag!!) {
                        loginForm(this@MainActivity)
                    } else {
                       PageChoix()


                    }
                }
            }



/*
        setContent{
            NfcReaderTheme {
                LoginForm()
            }
        }
*/

/*        //new android stuff
        setContent {
            NfcReaderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }*/


        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        // Check if NFC is available on the device
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not supported on this device", Toast.LENGTH_SHORT).show()
            return
        }

        //start waiting to a nfc tag
        pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_IMMUTABLE
        )




    }

    override fun onResume() {
        super.onResume()
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE
        )
        val intentFilters = arrayOf<IntentFilter>(
            IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED),
            IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED),
            IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        )
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null)
    }

    override fun onPause() {
        super.onPause()
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcAdapter.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == NfcAdapter.ACTION_TAG_DISCOVERED) {
            val tag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)
            } else {
                intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            }
            tag?.id?.let {
                val tagValue = it.toHexString()
                //val tagModif = TextFielSolds().toString()
                //ici tag value correspond à l id du tag
                Toast.makeText(this, "NFC tag detected: $tagValue", Toast.LENGTH_SHORT).show()
                tagValueState.value = tagValue


                GlobalScope.launch(Dispatchers.Main) {
                    val tagID = tagValue
                    val response = API.api.getSoldeOnly(EnvoieJusteTAG(tagID))
                    if(response!= null) {
                        if (response.isSuccessful && response.body() != null) {
                               solde.value = response.body()!!.responseJusteTAG
                                Toast.makeText(this@MainActivity, response.body()!!.responseJusteTAG, Toast.LENGTH_SHORT).show()
                        }
                        else {
                            //Log.d("Cashless_Log", "GetButton :\n${response.code()} : ${response.message()}")
                            Toast.makeText(this@MainActivity,"${response.code()} : ${response.message()}",Toast.LENGTH_LONG).show()
                        }
                    }

                }
                /*GlobalScope.launch(Dispatchers.Main) {
                try {
                    val TAG_ID = tagValue
                    val modifSolde = modifSoldeValue



                    //val response = API.api.getSoldeCarte(tagID)
                    val response = API.api.getSoldeCarte(EnvoieTag(TAG_ID ,modifSolde ))
                    //val response = API.api.getPostById(1)

                    if (response.isSuccessful && response.body() != null) {
                        //val content = response.body()
                        Toast.makeText(this@MainActivity, response.body()!!.cardBalance.toString(), Toast.LENGTH_SHORT).show()


                        //binding.textViewTest.setText(carteBalance.toString())
                        var balanceText = "Solde de la carte : ${carteBalance}, Réponse : ${carteBalance}"
                        var valeur = response.body()!!.cardBalance.toString()
                        //binding.textViewReponseBalance.text = carteBalance?.toString() ?: "N/A"
                        //val texview = findViewById<TextView>(R.id.textViewReponseBalance)
                        //binding.textViewReponseBalance.setText(${carteBalance}.toString())
                        cardBalanceState.value = valeur


                        //binding.textViewReponseBalance.setText(CarteBalanceResponse(carteBalance.toString()))





                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Error Occurred: ${response.message()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } catch (e: Exception) {
                    Toast.makeText(
                        this@MainActivity,
                        "Error Occurred: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
*/


            }
        }


    }


    private fun ByteArray.toHexString(): String {
        val hexChars = "0123456789ABCDEF"
        val result = StringBuilder(size * 2)

        map { byte ->
            val value = byte.toInt()
            val hexChar1 = hexChars[value shr 4 and 0x0F]
            val hexChar2 = hexChars[value and 0x0F]
            result.append(hexChar1)
            result.append(hexChar2)
        }

        return result.toString()
    }



    //new android stuff
    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        val tagValue = tagValueState.value
        val cardBalanceState = cardBalanceState.value
        val solde = solde

        Column(
            modifier = modifier
                .padding(20.dp)  // Padding autour de la colonne pour éviter que les éléments soient collés aux bords de l'écran
                .fillMaxWidth()  // Remplir toute la largeur disponible
        ){
/*            Text(
                text = "Hello : $name! $tagValue $cardBalanceState  ",
                modifier = modifier
            )*/

            var MODIF_SOLDE by remember { mutableStateOf("") }

            TextField(
                value = "ID du tag = " + tagValue,
                onValueChange = {tagValue
                },
                modifier = Modifier
                    .padding(30.dp),
                label = { Text("Tagvalue") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(30.dp))

            var AncienSolde by remember { mutableStateOf("") }

            TextField(

                value = solde.value,
                onValueChange = { newText -> solde.value
                },
                modifier = Modifier
                    .padding(30.dp),
                label = { Text("Solde de la Carte") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(30.dp))

            TextField(
                value = MODIF_SOLDE,
                onValueChange = { newText ->
                    MODIF_SOLDE = newText
                },
                modifier = Modifier
                    .padding(30.dp),
                label = { Text("Enter only digits") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(80.dp))
            Button(
                onClick = {
//                    val modifSoldeValue = MODIF_SOLDE
//                     if (MODIF_SOLDE.isNotEmpty() && tagValueState.value.isNotEmpty()){
//
//                     }



//                    var temp = MODIF_SOLDE.toCharArray()
//                    if(temp[0] == '-'){
//                        MODIF_SOLDE = temp[1] + temp[temp.size]
//
//                    }

                    /*if (solde.value.toInt() < a.toInt()){
                        Toast.makeText(this@MainActivity, "solde négatif", Toast.LENGTH_SHORT).show()

                    }else*/
                    when {
                        MODIF_SOLDE.isEmpty() -> {
                            Toast.makeText(this@MainActivity, "Solde vide", Toast.LENGTH_SHORT).show()
                        }
                        tagValueState.value.isEmpty() -> {
                            Toast.makeText(this@MainActivity, "Scanner une carte", Toast.LENGTH_SHORT).show()
                        }


                        else -> {
                            val modifSoldeValue = MODIF_SOLDE
                            GlobalScope.launch(Dispatchers.Main) {
                                try {
                                    val TAG_ID = tagValue
                                    val modifSolde = modifSoldeValue


                                    val response = API.api.getSoldeCarte(EnvoieTag(TAG_ID ,MODIF_SOLDE ))
                                    if (response.isSuccessful && response.body() != null) {
                                        Toast.makeText(this@MainActivity, "Solde Modifier", Toast.LENGTH_SHORT).show()
                                        var balanceText = "Solde de la carte : ${carteBalance}, Réponse : ${carteBalance}"
                                        var valeur = response.body()!!.cardBalance.toString()
                                        //cardBalanceState.value = valeur

                                    } else {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Error Occurred: ${response.message()}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Error Occurred: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }




                        }
                    }
                },

                modifier = Modifier.padding(10.dp )
                    .fillMaxWidth()
            ) {
                Text("Confirmer")
            }

/*            Spacer(modifier = Modifier.height(90.dp))
            TextField(
                value = PwdBool.value,
                onValueChange = { newValue ->
                    PwdBool.value = newValue
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                label = { Text("Enter text here") }
            )*/

            Button(
                onClick = {
                    flag = true
                    this@MainActivity.startActivity(Intent(this@MainActivity, MainActivity()::class.java))
                },

                modifier = Modifier.padding(30.dp).fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red, // Changer la couleur
                    contentColor = Color.White // Changer la couleur
                )
            )
            {
                Text("Retour")
            }
        }

        }




    }
    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        NfcReaderTheme {
            Greeting("android")
            
        }


    }







/*    @Preview
    @Composable
    fun PreviewButton() {
        MyButton(onClick = {})
    }*/



    /*@Preview(showBackground = true)
    @Composable
    fun MyButtonPreview() {
        MyButton(onClick = {})
    }*/




