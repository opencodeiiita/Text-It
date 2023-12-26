package com.example.text_it.Activity


import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class page1 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    private lateinit var callbackManager: CallbackManager


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


        callbackManager = CallbackManager.Factory.create()

        auth = Firebase.auth
        oneTapClient = Identity.getSignInClient(this)
    window.setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )


        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Log.d("Success", "Success")

                handleFacebookAccessToken(result.accessToken)

            }

            override fun onCancel() {
                Toast.makeText(this@page1, "Login Cancel", Toast.LENGTH_SHORT).show()
                Log.d("cancel", "Lets go")
                Firebase.auth.signOut()

            }

            override fun onError(error: FacebookException) {
                Toast.makeText(this@page1, error.message, Toast.LENGTH_LONG).show()
                Log.e(TAG, "Its definitely an error")
            }
        })


        setContentView(R.layout.activity_landing_user)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )


        val btnFbLogin:ImageButton = findViewById<ImageButton>(R.id.btnFacebook)

        btnFbLogin.setOnClickListener {
            LoginManager.getInstance()
                .logInWithReadPermissions(this@page1, listOf("email", "public_profile"))
        }



        oneTapClient = Identity.getSignInClient(this)

        val signupButton: Button = findViewById(R.id.signupButton)
        val loginButton: TextView = findViewById(R.id.textViewLogin)
        val googleButton: ImageButton = findViewById(R.id.btnGoogle)
        
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
                                        Log.d(TAG, "signInWithCredential:success")
                                        val user: FirebaseUser? = auth.currentUser
                                        Toast.makeText(
                                            this@page1,
                                            "Authentication Success.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        startActivity(
                                            Intent(
                                                this@page1,
                                                baseHomeActivity::class.java
                                            )
                                        )
                                    } else {
                                        Toast.makeText(this, "failure",Toast.LENGTH_SHORT).show()

                                        // If sign in fails, display a message to the user.
                                        Log.w(
                                            TAG,
                                            "signInWithCredential:failure",
                                            task.exception
                                        )
                                    }
                                })
                    }
                } catch (e: ApiException) {
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
                .addOnSuccessListener(this@page1,
                    OnSuccessListener<BeginSignInResult> { result ->
                        Toast.makeText(this, "gooo",Toast.LENGTH_SHORT).show()

                        launcher.launch(
                            IntentSenderRequest.Builder(
                                result.pendingIntent.intentSender
                            ).build()
                        )
                    })
                .addOnFailureListener(this@page1,
                    OnFailureListener { e -> // No Google Accounts found. Just continue presenting the signed-out UI.
                        Toast.makeText(this, e.localizedMessage,Toast.LENGTH_SHORT).show()
                        Log.d(TAG, e.localizedMessage)
                    })
        }

        signupButton.setOnClickListener {
            startActivity(
                android.content.Intent(
                    this, RegisterUser::class.java
                )
            )
        }

        loginButton.setOnClickListener {
            startActivity(
                android.content.Intent(
                    this, LoginUser::class.java
                )
            )
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
    private fun handleFacebookAccessToken(token: AccessToken) {

        val tak = token.token
        Log.d(TAG, "handleFacebookAccessToken:$tak")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser

                    startActivity(
                        android.content.Intent(
                            this, baseHomeActivity::class.java
                        )
                    )
//
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
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