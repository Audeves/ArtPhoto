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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ActivityCrearCuenta : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val database = Firebase.database
    val myRef = database.getReference("usuarios")

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
            var telefono: String = edt_numero.text.toString().trim()
            var estado: String = edt_estado.text.toString().trim()
            var ciudad: String = edt_ciudad.text.toString().trim()
            var nombre: String = edt_nombre_completo.text.toString().trim()

            if (correo.isNullOrEmpty() || contraseña.isNullOrEmpty() || contraseña.isNullOrEmpty() ||
                usuario.isNullOrEmpty() || telefono.isNullOrEmpty() || estado.isNullOrEmpty()
                || ciudad.isNullOrEmpty() || nombre.isNullOrEmpty()){
                Toast.makeText(this,"favor de llenar los campos", Toast.LENGTH_SHORT).show()
            } else {

                // TODO: registro en firebase

                auth.createUserWithEmailAndPassword(correo, contraseña)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser

                            myRef.child(user?.uid.toString()).child("correo").setValue(correo)
                            myRef.child(user?.uid.toString()).child("nombreUsuario").setValue(usuario)
                            myRef.child(user?.uid.toString()).child("telefono").setValue(telefono)
                            myRef.child(user?.uid.toString()).child("estado").setValue(estado)
                            myRef.child(user?.uid.toString()).child("ciudad").setValue(ciudad)
                            myRef.child(user?.uid.toString()).child("nombre").setValue(nombre)
                            startActivity(intent)
                            Toast.makeText(baseContext, "Bienvenido $usuario",Toast.LENGTH_SHORT).show()
                        } else {
                            Log.w("crear cuenta", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Fallo de autenticacion.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}