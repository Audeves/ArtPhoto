package audeves.luis.artphoto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ActivityContact : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_perfil)
        var btnContactar = findViewById<Button>(R.id.button)
        btnContactar.setOnClickListener {
            val intent: Intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

        }
    }
}