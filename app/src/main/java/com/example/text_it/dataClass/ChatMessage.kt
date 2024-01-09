import java.util.Date

data class ChatMessage(
    val message: String,
    val sender: Int, // 0 for me, 1 for them
    val date: Date,
)