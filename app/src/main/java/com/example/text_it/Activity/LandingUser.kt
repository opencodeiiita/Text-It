package com.example.text_it.Activity


import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.text_it.R
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.CommonStatusCodes


class LandingUser : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_user)

        auth = Firebase.auth
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
                                            this@LandingUser,
                                            "Authentication Success.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        startActivity(
                                            android.content.Intent(
                                                this@LandingUser,
                                                MainActivity::class.java
                                            )
                                        )
                                    } else {
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
                .addOnSuccessListener(this@LandingUser,
                    OnSuccessListener<BeginSignInResult> { result ->
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                result.pendingIntent.intentSender
                            ).build()
                        )
                    })
                .addOnFailureListener(this@LandingUser,
                    OnFailureListener { e -> // No Google Accounts found. Just continue presenting the signed-out UI.
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
}