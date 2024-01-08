import java.util.Date

data class ChatMessage(
    val message: String,
    val sender: String,
    val date: Date,
)