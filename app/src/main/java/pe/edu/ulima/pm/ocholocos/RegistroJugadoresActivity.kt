package pe.edu.ulima.pm.ocholocos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlin.properties.Delegates

class RegistroJugadoresActivity: AppCompatActivity() {

    //Se crean variables ediText para capturar los nomrbes de los jugadores
    private var eTjugadores= mutableListOf<EditText>()
    private var btnJugar: Button? = null
    private var cantidad_de_jugadores = 0

    //Se genera la vista del activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrojugadores)

        //Se captura el spinner
        var spPlayers: Spinner= findViewById(R.id.spPlayers)

        //Se capturan los ediText
        eTjugadores.add(findViewById(R.id.eTjugador1))
        eTjugadores.add(findViewById(R.id.eTjugador2))
        eTjugadores.add(findViewById(R.id.eTjugador3))
        eTjugadores.add(findViewById(R.id.eTjugador4))
        eTjugadores.add(findViewById(R.id.eTjugador5))

        for(e in eTjugadores){
            e.isVisible = false
        }

        //las posibles cantidades de jugadores
        val customList= listOf(2,3,4,5)
        val adapter= ArrayAdapter(this, R.layout.custom_simple_list, customList)
        spPlayers.adapter= adapter


        spPlayers.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            //Cuando un elemento del spinner sea seleccionado
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Se captura la cantidad de jugadores del spinner
                val cantidadJugadores = parent?.getItemAtPosition(position).toString().toInt()
                //Se muestran los ediText correspondientes
                for( i in 0 until cantidadJugadores)
                {
                    eTjugadores[i].isVisible = true
                }
                for (i in cantidadJugadores..4)
                {
                    eTjugadores[i].isVisible = false
                }
            }
        }


        //Se captura el boton jugar
        btnJugar = findViewById(R.id.btnJugar)

        btnJugar?.setOnClickListener {
            cantidad_de_jugadores = cantidadDeJugadoresVisibles()
            val intent= Intent()
            intent.setClass(this,MainActivity::class.java)// Intent configurado

            //Se crean almacenadores de jugadores
            val dataNombres = mutableListOf<Bundle>()
            for (c in 1..cantidad_de_jugadores)
            {
                //Se capturan los nombres de los jugadores
                val jugador = Bundle()
                jugador.putString("jugador$c",eTjugadores[c-1].text.toString())
                //Se agrega al array de jugadores
                dataNombres.add(jugador)

                intent.putExtras(dataNombres[c-1])
            }
            //Se carga la cantidad de jugadores
            val dataCantidad = Bundle()
            dataCantidad.putInt("cantidad_jugadores",cantidad_de_jugadores)
            Log.i(null,"cnt_jds = ${dataCantidad.getInt("cantidad_jugadores")}")
            intent.putExtras(dataCantidad)

            //Se inicia la actividad
            startActivity(intent)
        }
    }

    private fun cantidadDeJugadoresVisibles(): Int
    {
        //utilizar un for each
        var cantidad=0
        for(e in eTjugadores)
        {
            if(e.isVisible)
            {
                cantidad++
            }
        }
        return cantidad
    }
}