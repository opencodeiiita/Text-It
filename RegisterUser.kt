import com.google.firebase.database.FirebaseDatabase

class RegisterUser {

    private val database = FirebaseDatabase.getInstance().reference

    fun register(username: String, password: String): Boolean {
        // Firebase registration logic
        val userReference = database.child("users").child(username)
        userReference.setValue(User(username, password))
        return true
    }
}
