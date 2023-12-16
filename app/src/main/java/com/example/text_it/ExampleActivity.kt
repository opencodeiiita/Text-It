import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit

class ExampleActivity : AppCompatActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.example_activity)

       if (savedInstanceState == null) {
           supportFragmentManager.commit {
               setReorderingAllowed(true)
               add<ChatsFragment>(R.id.fragment_container)
               add<CallsFragment>(R.id.fragment_container)
               add<ProfileFragment>(R.id.fragment_container)
           }
       }
   }
}
