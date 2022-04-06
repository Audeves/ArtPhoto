package audeves.luis.artphoto

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class AdaptadorPublicacion: BaseAdapter {
    lateinit var context: Context
    var publicaciones: ArrayList<Publicacion> = ArrayList()

    constructor(context: Context, publicaciones: ArrayList<Publicacion>){
        this.context = context
        this.publicaciones = publicaciones
    }

    override fun getCount(): Int {
        return publicaciones.size
    }

    override fun getItem(p0: Int): Any {
        return publicaciones[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val inflador = LayoutInflater.from(context)
        var vista = inflador.inflate(R.layout.publicacion_view,null)

        var iv_img: ImageView = vista.findViewById(R.id.iv_publicacion)
        var iv_imgPerfil: ImageView = vista.findViewById(R.id.iv_imgperfil)
        var tv_publicador: TextView = vista.findViewById(R.id.tv_publicador)

        var publi: Publicacion = publicaciones[p0]

        tv_publicador.setText(publi.nombrePublicador)
        iv_imgPerfil.setImageResource(publi.iconoPerfil)
        iv_img.setImageResource(publi.fotoPublicacion)
       // Toast.makeText(context,"ayuda",Toast.LENGTH_SHORT).show()
  //      vista.setOnClickListener{
  //          val intent: Intent = Intent(context,PeliculaActivity::class.java)
 //           intent.putExtra("nombre",publi.nombre)
 //           intent.putExtra("desc",publi.sinopsis)
  //          intent.putExtra("img",publi.img)
  //          context.startActivity(intent)

  //      }
        return vista

    }
}