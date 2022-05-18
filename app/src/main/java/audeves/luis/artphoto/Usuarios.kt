package audeves.luis.artphoto

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class Usuarios<T>(val uid:String, var ciudad:String, var correo:String, var esFotografo: Boolean, var estado:String, var imgPerfil:String, var nombre: String, var nombreUsuario:String, var telefono:String)