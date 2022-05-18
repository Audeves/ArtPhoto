package audeves.luis.artphoto.ui.gallery

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.contentValuesOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import audeves.luis.artphoto.R
import audeves.luis.artphoto.databinding.FragmentGalleryBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import java.lang.Exception


class GalleryFragment : Fragment() {
    lateinit var ejemplo_icono: ImageView;
    private var _binding: FragmentGalleryBinding? = null
    private var auth = Firebase.auth
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
        ejemplo_icono.setOnClickListener{
            if(context?.let { it1 ->
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
        val textView: TextView = binding.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        val usuario = auth.currentUser
        if (usuario != null){
            val textGallery: TextView = root.findViewById(R.id.text_gallery)
            textGallery.setText(usuario.email)
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
                    seleccionar_Imagen()
                }else{
                    Toast.makeText(context, "no acepto permisos", Toast.LENGTH_SHORT).show()
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
                val img_stream = context?.contentResolver?.openInputStream(img_uri!!)
                val img_bitmap = BitmapFactory.decodeStream(img_stream)
                ejemplo_icono.setImageBitmap(img_bitmap)
            }catch (e: Exception){
                e.printStackTrace()
                Toast.makeText(context, "algo fallo", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}