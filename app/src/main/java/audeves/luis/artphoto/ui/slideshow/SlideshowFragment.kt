package audeves.luis.artphoto.ui.slideshow

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import audeves.luis.artphoto.R
import audeves.luis.artphoto.databinding.FragmentSlideshowBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private var auth = Firebase.auth
    val database = Firebase.database
    val myRef = database.getReference("usuarios")
    private lateinit var btn_unirse:Button
    private lateinit var tv_usuario: TextView
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        btn_unirse = binding.botonUnirse
        tv_usuario = binding.tvSerUsuario

        val usuario = auth.currentUser
        btn_unirse.setOnClickListener {
            if (usuario!=null){
                volverseFotografo(usuario)
            }
        }

        tv_usuario.setOnClickListener {
            if (usuario!=null){
                volverseUsuarioComun(usuario)
            }
        }




        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun volverseFotografo(usuario:FirebaseUser){
        val alertDialog: AlertDialog?= this?.let{
            val builder = AlertDialog.Builder(context)
            builder.apply {
                setPositiveButton(
                    R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        var esAdmin = true
                        myRef.child(usuario.uid).child("esFotografo").setValue(esAdmin)
                        Toast.makeText(context,"Su cuenta ha sido actualizada",Toast.LENGTH_SHORT).show()
                    })
                setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->

                    })
            }
            builder?.setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title)

            builder.create()
        }
        alertDialog?.show()
    }
    fun volverseUsuarioComun(usuario:FirebaseUser){
        val alertDialog: AlertDialog?= this?.let{
            val builder = AlertDialog.Builder(context)
            builder.apply {
                setPositiveButton(
                    R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        var esAdmin = false
                        myRef.child(usuario.uid).child("esFotografo").setValue(esAdmin)
                        Toast.makeText(context,"Su cuenta ha sido actualizada",Toast.LENGTH_SHORT).show()
                    })
                setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->

                    })
            }
            builder?.setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title)

            builder.create()
        }
        alertDialog?.show()
    }
}