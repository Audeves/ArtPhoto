package audeves.luis.artphoto

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.lang.Exception


class ActivityCrearCuenta : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val database = Firebase.database
    val myRef = database.getReference("usuarios")
    lateinit var img_btn_subirfoto: ImageView
    val storage = Firebase.storage
    val PERM_IMG = 123
    val PICK_IMG =234

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)

        auth = Firebase.auth

        img_btn_subirfoto = findViewById(R.id.img_btn_subirfoto)
        img_btn_subirfoto.setOnClickListener{

            if(this.let { it1 ->
                    ContextCompat.checkSelfPermission(
                        it1,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                } == PackageManager.PERMISSION_GRANTED){
                //Toast.makeText(context, "ya tiene el permiso", Toast.LENGTH_SHORT).show()
                seleccionar_Imagen()
            }else{
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERM_IMG)
            }
        }
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


                auth.createUserWithEmailAndPassword(correo, contraseña)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            var nombreImg :String= "perfil/"+user?.uid.toString()+".jpg"


                            myRef.child(user?.uid.toString()).child("correo").setValue(correo)
                            myRef.child(user?.uid.toString()).child("nombreUsuario").setValue(usuario)
                            myRef.child(user?.uid.toString()).child("telefono").setValue(telefono)
                            myRef.child(user?.uid.toString()).child("estado").setValue(estado)
                            myRef.child(user?.uid.toString()).child("ciudad").setValue(ciudad)
                            myRef.child(user?.uid.toString()).child("nombre").setValue(nombre)
                            var esFotografo = false
                            myRef.child(user?.uid.toString()).child("esFotografo").setValue(esFotografo)

                            myRef.child((user?.uid.toString())).child("imgPerfil").setValue(nombreImg)
                            subirImagen(nombreImg)
                            Toast.makeText(baseContext, "Bienvenido $usuario",Toast.LENGTH_SHORT).show()
                            startActivity(intent)


                        } else {
                            Log.w("crear cuenta", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Fallo de autenticacion.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun subirImagen(nombreImg:String) {
        val storageRef = storage.reference

        val imageRef = storageRef.child(nombreImg)

        img_btn_subirfoto.isDrawingCacheEnabled = true
        img_btn_subirfoto.buildDrawingCache()
        val bitmap = (img_btn_subirfoto.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            Toast.makeText(baseContext,"Problema al subir la imagen",Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERM_IMG -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    seleccionar_Imagen()
                }else{
                    Toast.makeText(this, "no acepto permisos", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun seleccionar_Imagen() {
        val intent_imgs = Intent(Intent.ACTION_PICK)
        intent_imgs.type = "image/*"
        startActivityForResult(intent_imgs, PICK_IMG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == PICK_IMG){
            try{
                val img_uri = data?.data
                val img_stream = this.contentResolver?.openInputStream(img_uri!!)
                val img_bitmap = BitmapFactory.decodeStream(img_stream)
                img_btn_subirfoto.setImageBitmap(img_bitmap)
            }catch (e: Exception){
                e.printStackTrace()
                Toast.makeText(this, "algo fallo", Toast.LENGTH_SHORT).show()
            }
        }
    }


}