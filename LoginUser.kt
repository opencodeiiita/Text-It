class LoginUser {

    private val users = mutableListOf<User>()

    fun login(username: String, password: String): Boolean {
        // Local login logic
        val user = users.find { it.username == username && it.password == password }
        return user != null
    }
}
