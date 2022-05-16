package pe.edu.ulima.pm.ocholocos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GanadorActivity: AppCompatActivity() {
    private var mButJugarDeNuevo:   Button? = null
    private var tVwinner:           TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ganador)

        mButJugarDeNuevo = findViewById(R.id.mButJugarDeNuevo)
        tVwinner = findViewById(R.id.tVwinner)

        tVwinner?.text = "GANA ${intent.getStringExtra("GANADOR")}!"

        mButJugarDeNuevo!!.setOnClickListener {
            //Cambiar a la pantalla MainActivity
            val intent = Intent()
            intent.setClass(this, RegistroJugadoresActivity::class.java)
            startActivity(intent)
        }

    }

}