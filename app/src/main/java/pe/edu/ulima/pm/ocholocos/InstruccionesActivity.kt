package pe.edu.ulima.pm.ocholocos

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


class InstruccionesActivity: AppCompatActivity() {

    var imgBtnReturnToMain: ImageButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instrucciones)
        //capturar el boton imgBtnReturnToMain
        imgBtnReturnToMain = findViewById(R.id.imgBtnReturnToMain)
        //agregar el evento click al boton imgBtnReturnToMain
        imgBtnReturnToMain?.setOnClickListener {
            //regresar a la actividad principal
            finish()
        }
    }
}