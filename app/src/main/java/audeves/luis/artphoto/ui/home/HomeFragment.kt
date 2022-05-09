package audeves.luis.artphoto.ui.home

import android.content.ClipData
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import audeves.luis.artphoto.AdaptadorPublicacion
import audeves.luis.artphoto.Publicacion
import audeves.luis.artphoto.R
import audeves.luis.artphoto.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    var publicaciones: ArrayList<Publicacion> = ArrayList()
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
        //Toast.makeText(binding.root.context,"llenando",Toast.LENGTH_SHORT).show()
        val publi1 = Publicacion(1,"Annete", R.drawable.annete_uno,R.drawable.perfil_goenix)
        val publi2 = Publicacion(2,"Sharon", R.drawable.annete_dos,R.drawable.perfil_sharon)
        val publi3 = Publicacion(3,"Devon Lane", R.drawable.annete_tres,R.drawable.perfil_goenix)

        publicaciones.add(publi1)
        publicaciones.add(publi2)
        publicaciones.add(publi3)

    }
}