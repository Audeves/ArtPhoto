package audeves.luis.artphoto.ui.home

import android.content.ClipData
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import audeves.luis.artphoto.AdaptadorPublicacion
import audeves.luis.artphoto.Publicacion
import audeves.luis.artphoto.R
import audeves.luis.artphoto.databinding.FragmentHomeBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlin.math.log

class HomeFragment : Fragment() {
    val storage = Firebase.storage
    val database = Firebase.database
    val myRef = database.getReference("usuarios")
    val storageRef = storage.reference
    private var _binding: FragmentHomeBinding? = null
    var imagenes: ArrayList<Bitmap> = ArrayList()
    var imagenesP: ArrayList<Bitmap> = ArrayList()
    var publicaciones: ArrayList<Publicacion> = ArrayList()
    var publicadoresID: ArrayList<String> = ArrayList()

    // This property is only valid between onCreateView and
    // onDestroyView.

    lateinit var listView: ListView
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //   val textView: TextView = binding.textHome
        //    homeViewModel.text.observe(viewLifecycleOwner) {
        //        textView.text = it
        //   }
        descargarPublicaciones()
        //  descargarImgPerfil()


        //Thread.sleep(10000)
        //Toast.makeText(context, "aqui", Toast.LENGTH_SHORT).show()
        var publicaciones: ArrayList<Publicacion> = ArrayList()
        var adaptador = AdaptadorPublicacion(root.context,publicaciones)
        listView = binding.listviewPubli
        listView.adapter = adaptador

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
    }

    fun llenarPublicaciones(){
        var contador: Int = 0
        imagenes.forEach {
            val publi1 = Publicacion(contador,"publicador", it,R.drawable.annete_dos)
            publicaciones.add(publi1)
        }

        var adaptador = AdaptadorPublicacion(requireContext(),publicaciones)
        listView = binding.listviewPubli
        listView.adapter = adaptador

    }

    fun descargarPublicaciones(){
        var contador: Int = 0
        val imageref = storageRef.child("publicaciones")
        val ONE_MEGABYTE: Long = 1024 * 1024
        imageref.listAll().addOnSuccessListener { lResult ->
            var publicaciones =lResult.items
            publicaciones.forEach { sRef ->
                var link = sRef.toString()
                var nombreImg =link.substringAfter("s/","no se encontro")
                var idPublicador = nombreImg.substringBefore("_","nan")
                publicadoresID.add(idPublicador)
                Log.i("contadoresId se agrega",publicadoresID.size.toString())
              //  Toast.makeText(context, nombreImg, Toast.LENGTH_SHORT).show()
                sRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                    var bitmap: Bitmap = BitmapFactory.decodeByteArray(it,0,it.size)
                    imagenes.add(bitmap)
                  //  Toast.makeText(context, "se agrego una", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Log.e("Fallo en convertir","rip",it)
                }

            }
        }.addOnFailureListener {
            Log.e("Fallo en aca","ripeo",it)
        }.addOnCompleteListener{
            llenarPublicaciones()
        }

    }
    fun descargarImgPerfil(){
        val profileRef = storageRef
        val ONE_MEGABYTE: Long = 1024 * 1024
        var nombreImg :String
        var imagenIndividual : StorageReference
        for (id in publicadoresID){
            nombreImg = "perfil/"+id+".jpg"

            imagenIndividual = profileRef.child(nombreImg)
            imagenIndividual.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                var bitmapPerfil: Bitmap = BitmapFactory.decodeByteArray(it,0,it.size)
                imagenesP.add(bitmapPerfil)

            }.addOnFailureListener {
                Log.i("explote","explote parte 2")
            }
        }
        llenarPublicaciones()
        if (imagenesP.isEmpty()){
            Log.i("ta vacio","no hay nada")
        } else{
            Log.i("si hay imagenes","kiuvo")
        }


    }
}