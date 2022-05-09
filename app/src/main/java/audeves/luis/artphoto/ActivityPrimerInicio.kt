package audeves.luis.artphoto

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ActivityPrimerInicio : AppCompatActivity() {
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primer_inicio)

        var btnInicioSesion = findViewById<Button>(R.id.btn_iniciar_sesion)
        btnInicioSesion.setOnClickListener {
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
        var btnCrear = findViewById<Button>(R.id.btn_crear_cuenta)
        btnCrear.setOnClickListener {

            val intent: Intent = Intent(this, ActivityCrearCuenta::class.java)
            startActivity(intent)

        }
    checkUser()
    }
    private fun checkUser(){
        val currentUser =auth.currentUser
        if(currentUser !=null){
            val intent: Intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}