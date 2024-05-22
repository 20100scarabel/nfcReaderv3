@file:OptIn(ExperimentalMaterial3Api::class)

package fr.plaglefleau.nfcreader.affichage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.plaglefleau.nfcreader.API
//import com.whitebatcodes.myloginapplication.MainActivity
//import com.whitebatcodes.myloginapplication.ui.theme.MyLoginApplicationTheme
import fr.plaglefleau.nfcreader.MainActivity
import fr.plaglefleau.nfcreader.response.EEnvoieLoginPwd
import fr.plaglefleau.nfcreader.response.EnvoieTag
import fr.plaglefleau.nfcreader.ui.theme.NfcReaderTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

lateinit var test : MainActivity
public var flag  : Boolean = false
val PwdBool = mutableStateOf("")


@Composable
fun loginForm() {
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
            LabeledCheckbox(
                label = "Remember Me",
                onCheckChanged = {
                    credentials = credentials.copy(remember = !credentials.remember)
                },
                isChecked = credentials.remember
            )
            Spacer(modifier = Modifier.height(40.dp))
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
                    if (checkCredentials(credentials, context)) {
                        // Naviguer vers la fonction Greeting si les informations d'identification sont correctes
                        //Greeting(credentials.login)
                        //val apiService = RetrofitClient.createService(ApiService::class.java)
                        //val apiService = API.api.getPWD(EnvoieLoginPwd("",""))
                        //val envoieLoginPwd = EnvoieLoginPwd("Pdw", "Login")
                        //val envoieLoginPdw = API.api.getPWD(EnvoieLoginPwd(credentials.pwd, credentials.login) )

                        GlobalScope.launch(Dispatchers.Main) {

                            try {
                                testLogin = credentials.login
                                //Log.d(testLogin, "testlogin1")
                                testPWD = credentials.pwd
                                //Log.d(testPWD, "testpwd")
                                val responseloginPwd = API.api.getPWD(EEnvoieLoginPwd(testLogin,testPWD))

                                if (responseloginPwd.isSuccessful && responseloginPwd.body() != null) {
                                    //Toast.makeText(this@MainActivity, response.body()!!.cardBalance.toString(), Toast.LENGTH_SHORT).show()
                                    var ResponsePWDBool = responseloginPwd.body()!!.PWDLoginBool
                                    PwdBool.value = ResponsePWDBool;
                                    Log.d(ResponsePWDBool,"responseLogin")
                                } else {
                                    /*Toast.makeText(
                                        this@MainActivity,
                                        "Error Occurred: ${response.message()}",
                                        Toast.LENGTH_LONG
                                    ).show()*/
                                }

                            } catch (e: Exception) {
                                /*Toast.makeText(
                                    this@MainActivity,
                                    "Error Occurred: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()*/
                            }
                        }






                    } else {
                        credentials = Credentials() // Réinitialiser les informations d'identification en cas d'erreur
                    }
                },
                enabled = credentials.isNotEmpty(),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")

            }// plus

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


@Composable
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
}

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



@Preview(showBackground = true, device = "id:Nexus One", showSystemUi = true)
@Composable
fun LoginFormPreview() {
    NfcReaderTheme {
        loginForm()
    }
}

@Preview(showBackground = true, device = "id:Nexus One", showSystemUi = true)
@Composable
fun LoginFormPreviewDark() {
    NfcReaderTheme(darkTheme = true) {
        loginForm()
    }
}

