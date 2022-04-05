package audeves.luis.artphoto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import audeves.luis.artphoto.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {
    lateinit var iniciarSesion: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iniciarSesion= findViewById(R.id.button)
        iniciarSesion.setOnClickListener {
           // Toast.makeText(this,"Prueba",Toast.LENGTH_LONG).show()
            abrirInicio()

        }
    }

    fun abrirInicio(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}