package com.example.text_it.Activity

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.text_it.R
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginUser : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    private lateinit var callbackManager: CallbackManager
    private lateinit var email: EditText
    private lateinit var password: EditText


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        if(currentUser != null)
        {
            startActivity(
                android.content.Intent(
                    this, baseHomeActivity::class.java
                )
            )
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)
        callbackManager = CallbackManager.Factory.create()

        auth = Firebase.auth
        email = findViewById(R.id.editTextEmail)
        password = findViewById(R.id.editTextPassword)

        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Log.d("Success", "Success")

                handleFacebookAccessToken(result.accessToken)

            }

            override fun onCancel() {
                Toast.makeText(this@LoginUser, "Login Cancel", Toast.LENGTH_SHORT).show()
                Log.d("cancel", "Lets go")
                Firebase.auth.signOut()

            }

            override fun onError(error: FacebookException) {
                Toast.makeText(this@LoginUser, error.message, Toast.LENGTH_LONG).show()
                Log.e(ContentValues.TAG, "Its definitely an error")
            }
        })




        val btnFbLogin:ImageButton = findViewById<ImageButton>(R.id.btnFacebook)

        btnFbLogin.setOnClickListener {
            LoginManager.getInstance()
                .logInWithReadPermissions(this@LoginUser, listOf("email", "public_profile"))
        }


        val loginBut: Button = findViewById(R.id.buttonLogin)
        val backBut: ImageButton = findViewById(R.id.backButton)
        val googleButton: ImageButton = findViewById(R.id.btnGoogle)

        auth = Firebase.auth
        oneTapClient = Identity.getSignInClient(this)

        val launcher = registerForActivityResult<IntentSenderRequest, ActivityResult>(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                try {
                    val credential =
                        oneTapClient.getSignInCredentialFromIntent(result.data)
                    val idToken = credential.googleIdToken
                    if (idToken != null) {
                        val firebaseCredential =
                            GoogleAuthProvider.getCredential(idToken, null)
                        auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener(this,
                                OnCompleteListener<AuthResult?> { task ->
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(ContentValues.TAG, "signInWithCredential:success")
                                        val user: FirebaseUser? = auth.currentUser
                                        Toast.makeText(
                                            this@LoginUser,
                                            "Authentication Success.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        startActivity(
                                            Intent(
                                                this@LoginUser,
                                                baseHomeActivity::class.java
                                            )
                                        )
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(
                                            ContentValues.TAG,
                                            "signInWithCredential:failure",
                                            task.exception
                                        )
                                    }
                                })
                    }
                } catch (e: ApiException) {
                    //
                }
            }
        }

        googleButton.setOnClickListener {
            signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build()
                )
                .setAutoSelectEnabled(true)
                .build()
            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this@LoginUser,
                    OnSuccessListener<BeginSignInResult> { result ->
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                result.pendingIntent.intentSender
                            ).build()
                        )
                    })
                .addOnFailureListener(this@LoginUser,
                    OnFailureListener { e -> // No Google Accounts found. Just continue presenting the signed-out UI.
                        Log.d(ContentValues.TAG, e.localizedMessage)
                    })
        }

        loginBut.setOnClickListener {
            val emailStr = email.text.toString()
            val passwordStr = password.text.toString()
            var doLogin = true
            if (emailStr == "" || passwordStr == "") {
                Toast.makeText(
                    baseContext, "Please fill in all fields",
                    Toast.LENGTH_SHORT
                ).show()
                doLogin = false
            }

            if(doLogin) {
                auth.signInWithEmailAndPassword(emailStr, passwordStr)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(ContentValues.TAG, "signInWithEmail:success")
                            val verify = auth.currentUser?.isEmailVerified
                            if(verify==true){
                                val user = auth.currentUser
                                startActivity(
                                    android.content.Intent(
                                        this, baseHomeActivity::class.java
                                    )
                                )
                            }
                            else{
                                Toast.makeText(this,"Please verify your email!",Toast.LENGTH_SHORT).show()
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                            if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(
                                    baseContext,
                                    "Incorrect Email or password!",
                                    Toast.LENGTH_SHORT,
                                ).show()

                            } else {
                                Toast.makeText(
                                    baseContext,
                                    "Authentication failed!",
                                    Toast.LENGTH_SHORT,
                                ).show()
                                startActivity(
                                    android.content.Intent(
                                        this, RegisterUser::class.java
                                    )
                                )
                            }
                        }
                    }
            }

        }


        backBut.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
    private fun handleFacebookAccessToken(token: AccessToken) {

        val tak = token.token
        Log.d(ContentValues.TAG, "handleFacebookAccessToken:$tak")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential:success")
                    val user = auth.currentUser

                    startActivity(
                        android.content.Intent(
                            this, baseHomeActivity::class.java
                        )
                    )
//
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
//                    updateUI(null)
                }
            }
    }
}