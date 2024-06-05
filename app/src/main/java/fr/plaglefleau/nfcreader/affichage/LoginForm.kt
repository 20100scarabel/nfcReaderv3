@file:OptIn(ExperimentalMaterial3Api::class)

package fr.plaglefleau.nfcreader.affichage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.plaglefleau.nfcreader.API
import fr.plaglefleau.nfcreader.ApiCashless
//import com.whitebatcodes.myloginapplication.MainActivity
//import com.whitebatcodes.myloginapplication.ui.theme.MyLoginApplicationTheme
import fr.plaglefleau.nfcreader.MainActivity
import fr.plaglefleau.nfcreader.response.EnvoieLoginPwd
import fr.plaglefleau.nfcreader.ui.theme.NfcReaderTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.properties.Delegates

lateinit var test : MainActivity
public var flag  : Boolean? = false
val PwdBool = mutableStateOf("")

public lateinit var responsePWDBool : String
public var responseID : String? = null
public lateinit var ipAddressAPI : String




@SuppressLint("SuspiciousIndentation")
@Composable
fun loginForm(mainactivity : MainActivity) {
    var loginacti: fr.plaglefleau.nfcreader.affichage.MainActivity
    var mainactivity = mainactivity

    Surface {

        var credentials by remember { mutableStateOf(Credentials()) }
        val context = LocalContext.current
        var testLogin:String
        var testPWD:String
       // test = MainActivity(flag)




        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)

        ) {




            LoginField(
                value = credentials.login,
                onChange = { data -> credentials = credentials.copy(login = data) },
                modifier = Modifier.fillMaxWidth()
            )

            PasswordField(
                value = credentials.pwd,
                onChange = { data -> credentials = credentials.copy(pwd = data) },
                submit = {
                    if (!checkCredentials(credentials, context)) credentials = Credentials()
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))

            IPAddressTextField(
                onChange = {},
                value = "" ,

            )

            //Spacer(modifier = Modifier.height(40.dp))
/*            Button(
                onClick = {
                    if (!checkCredentials(credentials, context)) credentials = Credentials()
                },
                enabled = credentials.isNotEmpty(),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }*/
            // plus

            Button(

                onClick = {


                    if (credentials.isNotEmpty()) {
                        GlobalScope.launch(Dispatchers.Main) {
                            try {
                                val testLogin = credentials.login
                                val testPWD = credentials.pwd

                                Log.d("Login", "testLogin1: $testLogin")
                                Log.d("PWD", "testPWD: $testPWD")

                                // Effectuer la requête réseau pour vérifier les informations d'identification


                                val responseloginPwd = API.api.getPWD(EnvoieLoginPwd(testLogin, testPWD))

                                API.aPE()

                                if (responseloginPwd.isSuccessful && responseloginPwd.body() != null) {
                                     responsePWDBool = responseloginPwd.body()!!.PWDLoginBool
                                    if (responsePWDBool == "false")
                                    Toast.makeText(mainactivity,"Error Occurred:", Toast.LENGTH_LONG).show()

                                    responseID = responseloginPwd.body()!!.id
                                    PwdBool.value = responsePWDBool
                                    Log.d("ResponseLogin", "responseLogin: $responsePWDBool")

                                    // Vérifiez les identifiants après la mise à jour de PwdBool
                                    if (checkCredentials(credentials, context)) {
                                        context.startActivity(Intent(context, MainActivity::class.java))
                                        (context as Activity).finish()
                                    } else {
                                        credentials = Credentials() // Réinitialiser les informations d'identification en cas d'erreur
                                    }
                                } else {
                                    //Toast.makeText(mainactivity,"Error Occurred:", Toast.LENGTH_LONG).show()
                                    Log.e("API Error", "Error Occurred: ${responseloginPwd.message()}")
                                }
                            } catch (e: Exception) {
                                Log.e("Exception", "Error Occurred: ${e.message}")
                            }
                        }
                    }
                },
                enabled = credentials.isNotEmpty(),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }

        }
    }
}

fun checkCredentials(creds: Credentials, context: Context): Boolean {
    test = MainActivity()

    if (PwdBool.value == "true") {
            //creds.pwd
        flag = true
       context.startActivity(Intent(context, MainActivity()::class.java))
          (context as Activity).finish()
        return true
    } else {
        Toast.makeText(context, "Wrong Credentials", Toast.LENGTH_SHORT).show()
        return false
    }
}

data class Credentials(
    var login: String = "",
    var pwd: String = "",
    var remember: Boolean = false
) {
    fun isNotEmpty(): Boolean {
        return login.isNotEmpty() && pwd.isNotEmpty()
    }
}




/*@Composable
fun LabeledCheckbox(
    label: String,
    onCheckChanged: () -> Unit,
    isChecked: Boolean
) {

    Row(
        Modifier
            .clickable(
                onClick = onCheckChanged
            )
            .padding(4.dp)
    ) {
        Checkbox(checked = isChecked, onCheckedChange = null)
        Spacer(Modifier.size(6.dp))
        Text(label)
    }
}*/


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Login",
    placeholder: String = "Enter your Login"

) {


    val focusManager = LocalFocusManager.current
    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Person,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }

    //ipAddress by remember { mutableStateOf("") }

    TextField(

        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = VisualTransformation.None
    )
}
@Composable
fun IPAddressTextField(value: Any?, onChange: Any?) {
    var ipAddress by remember { mutableStateOf("") }

    var isValid by remember { mutableStateOf(true) }


    fun validateIPAddress(ip: String): Boolean {
        val regex = Regex(
            "^http://((25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})\\.){3}(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2}):3000/\$",
        )
        return regex.matches(ip)
    }

    Column {
        TextField(
            value = ipAddress,
            onValueChange = {
                ipAddress = it
                ipAddressAPI = ipAddress
                isValid = validateIPAddress(it)
            },
            label = { Text("Enter IP Address") },
            isError = !isValid,
            visualTransformation = VisualTransformation.None,
            modifier = Modifier.fillMaxWidth()
        )
        if (!isValid) {
            Text(
                text = "Invalid IP address",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}



@Composable
fun PasswordField(

    value: String ,
    onChange: (String) -> Unit,
    submit: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Password",
    placeholder: String = "Enter your Password"
) {

    var isPasswordVisible by remember { mutableStateOf(false) }

    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Key,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }
    val trailingIcon = @Composable {
        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
            Icon(
                if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }


    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = { submit() }
        ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )




}
/*
@Composable
fun MyTextField(ipAddressState: MutableState<String>, onChange: (String) -> Unit) {
    TextField(
        value = ipAddressState.value,
        onValueChange = onChange,
        label = { Text("API IP Address") },
        modifier = Modifier,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { */
/* do something when done button is clicked *//*
 })
    )

}
*/



@Preview(showBackground = true, device = "id:Nexus One", showSystemUi = true)
@Composable
fun LoginFormPreview() {
    NfcReaderTheme {
        loginForm(MainActivity())
    }
}

@Preview(showBackground = true, device = "id:Nexus One", showSystemUi = true)
@Composable
fun LoginFormPreviewDark() {
    NfcReaderTheme(darkTheme = true) {
        loginForm(MainActivity())
    }
}


