data class User(val username: String, val password: String)

class RegisterUser {

    private val users = mutableListOf<User>()

    fun register(username: String, password: String): Boolean {
        // Local registration logic
        if (users.none { it.username == username }) {
            users.add(User(username, password))
            return true
        }
        return false
    }
}
