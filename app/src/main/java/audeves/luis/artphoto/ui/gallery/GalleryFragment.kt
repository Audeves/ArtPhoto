package audeves.luis.artphoto.ui.gallery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import audeves.luis.artphoto.R
import audeves.luis.artphoto.Usuarios
import audeves.luis.artphoto.databinding.FragmentGalleryBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class GalleryFragment : Fragment() {
    lateinit var ejemplo_icono: ImageView
    lateinit var img_subirFoto: ImageView
    private var _binding: FragmentGalleryBinding? = null
    private var auth = Firebase.auth
    private var usuario = auth.currentUser
    val database = Firebase.database
    val myRef = database.getReference("usuarios")
    val myPublicacionRef = database.getReference("publicaciones")
    val storage = Firebase.storage
    var nombreImg :String =""
    val PERM_IMG = 123
    val PICK_IMG =234
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        ejemplo_icono = binding.imageView3
        img_subirFoto = binding.imageView4
        ejemplo_icono.setOnClickListener{
            if(context?.let { it1 ->
                    ContextCompat.checkSelfPermission(
                        it1,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                } == PackageManager.PERMISSION_GRANTED){
            //Toast.makeText(context, "ya tiene el permiso", Toast.LENGTH_SHORT).show()
                seleccionar_Imagen(false)
            }else{
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERM_IMG)
            }
        }
        val textView: TextView = binding.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        img_subirFoto.setOnClickListener{
            if(context?.let { it1 ->
                    ContextCompat.checkSelfPermission(
                        it1,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                } == PackageManager.PERMISSION_GRANTED){
                //Toast.makeText(context, "ya tiene el permiso", Toast.LENGTH_SHORT).show()
                seleccionar_Imagen(true)
            }else{
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERM_IMG)
            }
        }

        if (usuario != null){

            myRef.child(usuario!!.uid)
            val textGallery: TextView = root.findViewById(R.id.text_gallery)

            myRef.child(usuario!!.uid).child("nombreUsuario").get().addOnSuccessListener {
                ///Toast.makeText(context,it.value.toString(),Toast.LENGTH_SHORT).show()
                textGallery.setText(it.getValue().toString())
              //  Log.i("firebase", "Got value ${it.value}")
            }.addOnFailureListener{
                //Log.e("firebase", "Error getting data", it)
            }
            myRef.child(usuario!!.uid).child("imgPerfil").get().addOnSuccessListener {

               descargarImagen()
            }.addOnFailureListener{
               // Toast.makeText(context,"rip",Toast.LENGTH_SHORT).show()
                //Log.e("firebase", "Error getting data", it)
            }

            myRef.child(usuario!!.uid).child("esFotografo").get().addOnSuccessListener{
                    if (it.getValue() == true){
                        img_subirFoto.visibility = View.VISIBLE
                    }else{
                        img_subirFoto.visibility = View.GONE
                    }
            }.addOnFailureListener{

            }
          //  myRef.addValueEventListener(usuarioListener)
        }
        val textViewGal: TextView = root.findViewById(R.id.textView5)
        textViewGal.setText("")
        val imageView5: ImageView = root.findViewById(R.id.imageView5)
        imageView5.visibility = View.GONE
        return root

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERM_IMG -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    seleccionar_Imagen(false)
                }else{
                    Toast.makeText(context, "no acepto permisos", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun seleccionar_Imagen(esPublicacion:Boolean) {
        val intent_imgs = Intent(Intent.ACTION_PICK)
        intent_imgs.type = "image/*"
        if (esPublicacion){
            val df = SimpleDateFormat("yyyyMMdd_HHmmss")
            val fecha = Date()
            nombreImg = "publicaciones/"+usuario!!.uid+"_"+df.format(fecha)+".jpg"
            myPublicacionRef.child(usuario!!.uid.toString()).child("imgPublicacion").setValue(nombreImg)
            startActivityForResult(intent_imgs,PICK_IMG+2)
        }else{
            nombreImg = "perfil/"+usuario!!.uid+".jpg"
            startActivityForResult(intent_imgs, PICK_IMG)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == PICK_IMG){
            try{
                val img_uri = data?.data
                val img_stream = context?.contentResolver?.openInputStream(img_uri!!)
                val img_bitmap = BitmapFactory.decodeStream(img_stream)
                ejemplo_icono.setImageBitmap(img_bitmap)
                subirImagen(ejemplo_icono)
            }catch (e: Exception){
                e.printStackTrace()
                Toast.makeText(context, "algo fallo", Toast.LENGTH_SHORT).show()
            }
        }else{
             try {
                val img_uri = data?.data
                val img_stream = context?.contentResolver?.openInputStream(img_uri!!)
                val img_bitmap = BitmapFactory.decodeStream(img_stream)
                img_subirFoto.setImageBitmap(img_bitmap)
                subirImagen(img_subirFoto)
            }catch (e:Exception){
                e.printStackTrace()
                Toast.makeText(context, "algo fallo", Toast.LENGTH_SHORT).show()
           }
        }
    }

    private fun subirImagen(imagen:ImageView) {
        val storageRef = storage.reference

        val imageRef = storageRef.child(nombreImg)

        imagen.isDrawingCacheEnabled = true
        imagen.buildDrawingCache()
        val bitmap = (imagen.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            Toast.makeText(context,"Problema al subir la imagen",Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            Toast.makeText(context,"Se subio la imagen",Toast.LENGTH_SHORT).show()
        }

    }

    private fun descargarImagen(){
        val storageRef = storage.reference
        nombreImg = "perfil/"+usuario!!.uid+".jpg"
        val imageref = storageRef.child(nombreImg)
        val ONE_MEGABYTE: Long = 1024 * 1024
        imageref.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            var bitmap:Bitmap = BitmapFactory.decodeByteArray(it,0,it.size)

           ejemplo_icono.setImageBitmap(bitmap)
        }.addOnFailureListener {
            Toast.makeText(context,"No se consiguio la imagen",Toast.LENGTH_SHORT).show()
            // Handle any errors
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}