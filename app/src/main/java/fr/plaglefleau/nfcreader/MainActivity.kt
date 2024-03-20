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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.databinding.DataBindingUtil
import fr.plaglefleau.nfcreader.databinding.ActivityMainBinding
import fr.plaglefleau.nfcreader.response.EnvoieTag
import fr.plaglefleau.nfcreader.ui.theme.NfcReaderTheme



class MainActivity : ComponentActivity() {

    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null
    private var tag:String = ""
    private val intentFiltersArray: Array<IntentFilter>? = null
    private val techListsArray: Array<Array<String>>? = null

    var carteBalance: Double? = null



    val tagValueState = mutableStateOf("")






    lateinit var binding: ActivityMainBinding







    @RequiresApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        /*GlobalScope.launch(Dispatchers.Main) {
            API.api.getSoldeCarte("046c3e6a546080")
        }*/


        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)

        //new android stuff
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
        }

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
                //ici tag value correspond à l id du tag
                Toast.makeText(this, "NFC tag detected: $tagValue", Toast.LENGTH_SHORT).show()
                tagValueState.value = tagValue


/*                GlobalScope.launch(Dispatchers.Main) {
                    val tagID = tagValue
                    val response = API.api.getSoldeCarte(tagID)
                    if(response!= null) {
                        if (response.isSuccessful && response.body() != null) {
                                Toast.makeText(this@MainActivity, response.body()!!.cardBalance.toString(), Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Log.d("Cashless_Log", "GetButton :\n${response.code()} : ${response.message()}")
                            Toast.makeText(this@MainActivity,"${response.code()} : ${response.message()}",Toast.LENGTH_LONG).show()
                        }
                    }

                }*/
                GlobalScope.launch(Dispatchers.Main) {
                try {
                    val TAG_ID = tagValue
                    //val response = API.api.getSoldeCarte(tagID)
                    val response = API.api.getSoldeCarte(EnvoieTag(TAG_ID))



                    //val response = API.api.getPostById(1)


                    if (response.isSuccessful && response.body() != null) {
                        //val content = response.body()
                        Toast.makeText(this@MainActivity, response.body()!!.cardBalance.toString(), Toast.LENGTH_SHORT).show()



                        //val balanceText = "Solde de la carte : ${carteBalance}, Réponse : ${carteBalance}"

                        //binding.textViewReponseBalance.setText(balanceText)


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


        Text(
            text = "Hello : $name! $tagValue   ",

            modifier = modifier
        )

    }



    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        NfcReaderTheme {
            Greeting("Android ")
        }
    }
}