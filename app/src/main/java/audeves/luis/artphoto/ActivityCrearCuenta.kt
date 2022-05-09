package audeves.luis.artphoto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ActivityCrearCuenta : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)

        auth = Firebase.auth

        val edt_nombre_completo: EditText = findViewById(R.id.edt_nombre)
        val edt_nombre_usuario: EditText = findViewById(R.id.edt_nombre_usuario)
        val edt_correo: EditText = findViewById(R.id.edt_correo)
        val edt_contraseña: EditText = findViewById(R.id.edt_contraseña)
        val edt_numero: EditText = findViewById(R.id.edt_numero_celular)
        val edt_estado: EditText = findViewById(R.id.edt_estado)
        val edt_ciudad: EditText = findViewById(R.id.edt_ciudad)
        val btn_crear_cuenta: Button = findViewById(R.id.btn_crear_cuenta)



        btn_crear_cuenta.setOnClickListener {
            val intent: Intent = Intent(this, MainActivity::class.java)

            var correo: String = edt_correo.text.toString().trim()
            var contraseña: String = edt_contraseña.text.toString().trim()
            var usuario: String = edt_nombre_usuario.text.toString().trim()

            if (correo.isNullOrEmpty() || contraseña.isNullOrEmpty()){
                Toast.makeText(this,"favor de llenar los campos", Toast.LENGTH_SHORT).show()
            } else {

                // TODO: registro en firebase

                auth.createUserWithEmailAndPassword(correo, contraseña)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            startActivity(intent)
                            Toast.makeText(baseContext, "Bienvenido $usuario",Toast.LENGTH_SHORT).show()
                        } else {
                            Log.w("crear cuenta", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()

                        }
                    }
            }
        }
    }
}