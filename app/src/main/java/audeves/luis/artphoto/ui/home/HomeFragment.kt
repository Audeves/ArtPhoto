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
        llenarPublicaciones()
        var adaptador = AdaptadorPublicacion(root.context,publicaciones)
        var listView: ListView = binding.listviewPubli
        listView.adapter = adaptador
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun llenarPublicaciones(){
        var contador: Int = 0
        //    Log.i("publicadoresID size:",publicadoresID.size.toString())
      //      while (contador <= publicadoresID.size && contador <=imagenesP.size && contador <= imagenes.size){
        //        myRef.child(publicadoresID[contador]).child("nombreUsuario").get().addOnSuccessListener {
        imagenes.forEach {
            val publi1 = Publicacion(contador,"publicador", imagenes[contador],R.drawable.annete_dos)
            publicaciones.add(publi1)
        }

                 //   val publi1 = Publicacion(contador,it.getValue().toString(), imagenes[contador],imagenes[contador])

          //         contador++
              //  }.addOnFailureListener {
              //      contador++
             //       Log.e("paso algo malo","chales",it.cause)
                //    Toast.makeText(context,"Huvo un error al desplegar publicaciones",Toast.LENGTH_SHORT).show()
              // }
         //   }

    }

    fun descargarPublicaciones(){

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
                sRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                    var bitmap: Bitmap = BitmapFactory.decodeByteArray(it,0,it.size)
                    imagenes.add(bitmap)
                }.addOnFailureListener {
                    Log.e("Fallo en convertir","rip",it)
                }

            }
        //    descargarImgPerfil()
        }.addOnFailureListener {
            Log.e("Fallo en aca","ripeo",it)
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