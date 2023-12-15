import com.google.firebase.database.*

class LoginUser {

    private val database = FirebaseDatabase.getInstance().reference

    fun login(username: String, password: String, callback: (Boolean) -> Unit) {
        // Firebase login logic
        val userReference = database.child("users").child(username)

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)

                if (user != null && user.password == password) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
                callback(false)
            }
        })
    }
}
