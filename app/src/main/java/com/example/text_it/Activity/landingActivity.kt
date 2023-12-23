package com.example.text_it.Activity


import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.text_it.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class landingActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
//    private lateinit var oneTapClient: SignInClient
//    private lateinit var signInRequest: BeginSignInRequest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_user)

        auth = Firebase.auth
//        oneTapClient = Identity.getSignInClient(this)
    window.setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )

        val signupButton: Button = findViewById(R.id.signupButton)
        val loginButton: TextView = findViewById(R.id.textViewLogin)
        val googleButton: ImageButton = findViewById(R.id.btnGoogle)

//        val launcher = registerForActivityResult<IntentSenderRequest, ActivityResult>(
//            ActivityResultContracts.StartIntentSenderForResult()
//        ) { result: ActivityResult ->
//            if (result.resultCode == RESULT_OK) {
//                try {
////                    val credential =
//////                        oneTapClient.getSignInCredentialFromIntent(result.data)
////                    val idToken = credential.googleIdToken
//                    if (idToken != null) {
//                        val firebaseCredential =
//                            GoogleAuthProvider.getCredential(idToken, null)
//                        auth.signInWithCredential(firebaseCredential)
//                            .addOnCompleteListener(this,
//                                OnCompleteListener<AuthResult?> { task ->
//                                    if (task.isSuccessful) {
//                                        // Sign in success, update UI with the signed-in user's information
//                                        Log.d(TAG, "signInWithCredential:success")
//                                        val user: FirebaseUser? = auth.currentUser
//                                        Toast.makeText(
//                                            this@landingActivity,
//                                            "Authentication Success.",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                        startActivity(
//                                            android.content.Intent(
//                                                this@landingActivity,
//                                                MainActivity::class.java
//                                            )
//                                        )
//                                    } else {
//                                        // If sign in fails, display a message to the user.
//                                        Log.w(
//                                            TAG,
//                                            "signInWithCredential:failure",
//                                            task.exception
//                                        )
//                                    }
//                                })
//                    }
//                } catch (e: ApiException) {
//                //
//                }
//            }
//        }

//        googleButton.setOnClickListener {
//            signInRequest = BeginSignInRequest.builder()
//                .setGoogleIdTokenRequestOptions(
//                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                        .setSupported(true)
//                        .setServerClientId(getString(R.string.web_client_id))
//                        .setFilterByAuthorizedAccounts(false)
//                        .build()
//                )
//                .setAutoSelectEnabled(true)
//                .build()
//            oneTapClient.beginSignIn(signInRequest)
//                .addOnSuccessListener(this@landingActivity,
//                    OnSuccessListener<BeginSignInResult> { result ->
//                        launcher.launch(
//                            IntentSenderRequest.Builder(
//                                result.pendingIntent.intentSender
//                            ).build()
//                        )
//                    })
//                .addOnFailureListener(this@landingActivity,
//                    OnFailureListener { e -> // No Google Accounts found. Just continue presenting the signed-out UI.
//                        Log.d(TAG, e.localizedMessage)
//                    })
//        }

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