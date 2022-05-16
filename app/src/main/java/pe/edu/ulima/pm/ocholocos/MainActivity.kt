package pe.edu.ulima.pm.ocholocos


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.iterator
import androidx.core.view.size
import pe.edu.ulima.pm.ocholocos.clase.Jugador
import pe.edu.ulima.pm.ocholocos.views.CardView

class MainActivity: AppCompatActivity()
{
    private val jugadores: MutableList<Jugador> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Se carga la vista del Layout main_activity
        setContentView(R.layout.main_activity)

        //Se obtiene la cantidad de jugadores
        val cant_jgds = intent.getIntExtra("cantidad_jugadores", 0)

        //Se crea un array de jugadores
        for (i in 1..cant_jgds)
        {
            jugadores.add(Jugador("${intent.getStringExtra("jugador$i")}"))
            repartir_cartas(jugadores[i-1])
        }

        //Se captura la carta Central y se le setea un valor aleatorio
        val central_card = findViewById<CardView>(R.id.central_card); central_card.randomCV()

        //Capturamos el textView
        val tVturnoJ:TextView=findViewById(R.id.tVturnoJ)

        //Se setea el texto con el jugador inicial
        tVturnoJ.text= "Turno de ${jugadores[0].nombre}"//jugador1

        //Se captura el table Row de la mesa
        val mazoActual: TableRow = findViewById(R.id.cartas_en_mano_jA)
        verMazoActual(mazoActual, jugadores[0].mazo)//se muestra la baraja del jugador 1

        //Se captura el boton coger carta
        val btnCogerCarta: Button = findViewById(R.id.btnCogerCarta)

        //Se captura el boton pasar
        val btnPasar: Button = findViewById(R.id.btnPasar)
        btnPasar.isVisible=false

        //Se captura el texto de Cantidad de cartas
        val eTcantidadCartas: TextView = findViewById(R.id.eTcantidadCartas)
        eTcantidadCartas.text=jugadores[0].getCantCartas().toString()



        fun juegoTerminado()
        {
            val intent = Intent()
            intent.setClass(this,GanadorActivity::class.java)// Intent configurado

            //Pasar el jugador ganador
            val data= Bundle()
            for (i in jugadores)
            {
                when (tVturnoJ.text) {
                    "Turno de ${i.nombre}" -> data.putString("GANADOR",i.nombre)
                }
            }

            intent.putExtras(data)
            startActivity(intent)
        }

        //Funcion para saltar turnos
        fun saltarTurno()
        {
            for(i in 0 until cant_jgds)
            {
                when {
                    //Para el jugador Final
                    tVturnoJ.text.contains(jugadores[cant_jgds-1].nombre) -> {
                        //se cambia el turno al jugador 1
                        tVturnoJ.text = "Turno de ${jugadores[0].nombre}"
                        //se muestra la baraja del jugador 1
                        verMazoActual(mazoActual, jugadores[0].mazo)
                        //se muestra la cantidad de cartas del jugador 1
                        eTcantidadCartas.text = jugadores[0].getCantCartas().toString()
                        break
                    }
                    //Para cualquier jugador
                    tVturnoJ.text.contains(jugadores[i].nombre) -> {
                        //se cambia el turno al jugador siguiente
                        tVturnoJ.text = "Turno de ${jugadores[i+1].nombre}"

                        //se muestra la baraja del jugador siguiente
                        verMazoActual(mazoActual, jugadores[i+1].mazo)

                        //se muestra la cantidad de cartas del siguiente jugador
                        eTcantidadCartas.text = jugadores[i+1].getCantCartas().toString()
                        break
                    }

                }
            }
        }

        //Funcion para actualizar las vistas
        fun actualizarVistas(j: Jugador)
        {
            //Se setea el texto de cantidad de cartas
            eTcantidadCartas.text = j.getCantCartas().toString()

            //Se actualiza la baraja del jugador
            verMazoActual(mazoActual,j.mazo)

        }

        //funcion para lanzar una carta
        fun lanzarCarta(j: Jugador, carta: CardView){
            //Se elimina la carta del jugador
            j.eliminarCarta(carta)
            actualizarVistas(j)
            //Se setea el valor de la carta central
            central_card.setAttrs(carta.getValor(),carta.getPalo())
        }

        //Funcion para interactuar con una carta
        fun tocarCarta()
        {
            //Se busca la carta tocada
            for(i in mazoActual) i.setOnTouchListener { v, event ->
                if (event != null) {
                    when (event.action) {
                        MotionEvent.ACTION_UP ->
                        {
                            val c1 = v as CardView//se captura la carta

                            //Si la carta tocada es igual a la carta central en palo o valor
                            if(c1.getValor()==central_card.getValor() ||
                                c1.getPalo()==central_card.getPalo())
                            {
                                //Se busca al jugador actual
                                for(j in 0 until cant_jgds)
                                {
                                    when
                                    {
                                        //SI ES TURNO DE UN JUGADOR
                                        tVturnoJ.text.contains(jugadores[j].nombre) ->
                                        {
                                            //SI AÚN NO HA LANZADO UNA CARTA
                                            if (!jugadores[j].ha_jugado)
                                            {
                                                //Se evalúa el valor de la carta tocada
                                                when (c1.getValor())
                                                {
                                                    //Si la carta tocada es un 11
                                                    11 -> {
                                                        jugadores[j].saltos_acum++
                                                    }

                                                    //Si la carta tocada es un 13
                                                    13 -> {
                                                        when(j){
                                                            cant_jgds-1 -> {
                                                                jugadores[0].asignarCartas=true
                                                                jugadores[0].cartas_asignadas+=3

                                                            }
                                                            else -> {
                                                                jugadores[j+1].asignarCartas=true
                                                                jugadores[j+1].cartas_asignadas+=3
                                                            }
                                                        }
                                                    }
                                                    /*
                                                    12 -> {
                                                        jugadores[0].asignarCartas = true
                                                        jugadores[0].cartas_asignadas += 2
                                                    }*/

                                                }

                                                //Se verifica el atributo volver a jugar
                                                if (jugadores[j].volver_a_jugar)
                                                {
                                                    //Se setea el atributo volver a jugar como false
                                                    jugadores[j].volver_a_jugar = false

                                                    // Si carta tocada = carta central en valor
                                                    if (c1.getValor() == central_card.getValor())
                                                    {
                                                        //Se lanza la carta
                                                        lanzarCarta(jugadores[j], c1)
                                                    }
                                                    else{
                                                        continue
                                                    }
                                                }

                                                //Si no se esta volviendo a jugar
                                                else {

                                                    //Se lanza la carta
                                                    lanzarCarta(jugadores[j], c1)
                                                }

                                                //--------------------------------------------------

                                                //Si la cantidad de cartas es 1
                                                if (jugadores[j].getCantCartas() == 1)
                                                {
                                                    //Se muestra un aviso
                                                    Toast.makeText(this,
                                                        "¡${jugadores[j].nombre} tiene una carta!",
                                                        Toast.LENGTH_LONG).show()
                                                }

                                                //Si la cantidad de cartas es 0
                                                if (jugadores[j].getCantCartas() == 0)
                                                {
                                                    //se termina el juego
                                                    juegoTerminado()
                                                    break
                                                }

                                                //Si se tiene una carta igual a la lanzada
                                                if (otraCartaIgual(c1, jugadores[j].mazo))
                                                {
                                                    //Si carta tocada = carta central
                                                    if (c1.getValor() == central_card.getValor())
                                                    {
                                                        //Se setea el atributo volver a jugar
                                                        jugadores[j].volver_a_jugar = true

                                                        //Se muestra un mensaje
                                                        Toast.makeText(this,
                                                            "Se puede repetir el turno",
                                                            Toast.LENGTH_SHORT).show()

                                                        //Se oculta el boton pasar
                                                        btnPasar.isVisible = false

                                                        //Se da la funcionalidad de tocar carta
                                                        tocarCarta()
                                                    }
                                                    else{
                                                        continue
                                                    }
                                                }
                                                else {
                                                    //se setea el atributo ha_jugado
                                                    jugadores[j].ha_jugado = true

                                                    //se desaparece el boton coger carta
                                                    btnCogerCarta.isVisible = false

                                                    //se muestra el boton pasar
                                                    btnPasar.isVisible = true
                                                }
                                                break
                                            }
                                            else{
                                                continue
                                            }
                                        }
                                        else->
                                        {
                                            continue
                                        }

                                    }
                                }

                            }

                            //Si la carta no es igual a la central en valor o palo
                            else
                            {
                                //Se busca el jugador actual
                                for(j in 0 until cant_jgds)
                                {
                                    when
                                    {
                                        // Cuando se encuentra al jugador actual
                                        tVturnoJ.text.contains(jugadores[j].nombre) ->
                                        {
                                            //Si es que este no ha jugado
                                            if (!jugadores[j].ha_jugado)
                                            {
                                                Toast.makeText(this,
                                                    "No puedes coger esa carta",
                                                    Toast.LENGTH_SHORT).show()
                                                break
                                            }
                                            else{
                                                continue
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                true
            }
        }


        //Se da la funcionalidad de tocar cartas
        tocarCarta()


        //Cuando se toca el boton coger carta
        btnCogerCarta.setOnClickListener {
            for(i in 0 until cant_jgds)
            {
                when
                {
                    // PARA CUALQUIER JUGADOR
                    tVturnoJ.text.contains(jugadores[i].nombre) ->
                    {
                        //Si el jugador aún no ha lanzado una carta
                        if (!jugadores[i].ha_jugado)
                        {
                            //Se asigna una carta aleatoria al jugador
                            jugadores[i].agregarAlinicio(cartaAleatoria())

                            actualizarVistas(jugadores[i])

                            btnPasar.isVisible=true //se muestra el boton pasar
                            btnCogerCarta.isVisible=false   //se desaparece el boton coger carta

                            break
                        }
                    }
                }
            }
            tocarCarta()
        }


        //Cuando se toca el boton pasar
        btnPasar.setOnClickListener()
        {
            btnPasar.isVisible=false    //se desaparece el boton pasar
            btnCogerCarta.isVisible=true    //se muestra el boton coger carta

            for (i in 0 until cant_jgds) {
                when {
                    //SI ES TURNO DEL ULTIMO JUGADOR
                    tVturnoJ.text.contains(jugadores[cant_jgds-1].nombre)->{

                        //Si el jugador 1 tiene cartas por asignar
                        if(jugadores[0].asignarCartas) {
                            //Si se encuentra un 13 en el mazo de este jugador
                            if (jugadores[0].encontrar13()>0){
                                //Se habilita la opción de asignar cartas al siguiente jugador
                                jugadores[1].asignarCartas=true
                                //Se le traslada el valor del atributo cartas_asignadas
                                jugadores[1].cartas_asignadas+=jugadores[0].cartas_asignadas

                                jugadores[0].cartas_asignadas=0
                                jugadores[0].asignarCartas=false
                            }
                            //Si no se encuentra un 13 en el mazo del jugador 1
                            else{
                                //Se le trasladan las cartas acumuladas
                                for(j in 1..jugadores[0].cartas_asignadas){
                                    jugadores[0].agregarCarta(cartaAleatoria())
                                }
                                jugadores[0].cartas_asignadas=0
                                jugadores[0].asignarCartas=false
                            }
                        }
                        val cnt_cartas=jugadores[i].cartas_asignadas

                        jugadores[i].ha_jugado = false       //se declara el jugador actual como no jugado
                        jugadores[i].saltos_acum=0
                        //Saltar el turno la cantidad de veces que se le asigne al jugador
                        for(j in 0..cnt_cartas) {
                            saltarTurno()
                        }


                        break
                    }

                    // SI ES TURNO DE CUALQUIER  OTRO JUGADOR
                    tVturnoJ.text.contains(jugadores[i].nombre) -> {
                        when(cant_jgds){
                            2->{
                                //Si el jugador siguiente tiene cartas por agregar
                                if(jugadores[1].asignarCartas) {
                                    //Si se encuentra un 13 en el mazo de ese jugador
                                    if (jugadores[1].encontrar13()>0){
                                        //Se habilita la opción de asignar cartas al subsiguiente jugador
                                        jugadores[0].asignarCartas=true
                                        //Se le trasladan las cartas acumuladas del jugador previo
                                        jugadores[0].cartas_asignadas+=jugadores[1].cartas_asignadas

                                        //Se deshabilita la opcion de asignar cartas
                                        jugadores[1].asignarCartas=false

                                        //Se resetean las cartas asignadas
                                        jugadores[1].cartas_asignadas=0

                                    }

                                    //Si no se encuentra un 13 en el mazo del siguiente jugador
                                    else{
                                        //Se le trasladan las cartas acumuladas del jugador previo
                                        for(j in 0 until jugadores[1].cartas_asignadas){
                                            jugadores[1].agregarCarta(cartaAleatoria())
                                        }
                                        jugadores[1].cartas_asignadas=0
                                        jugadores[1].asignarCartas=false
                                    }
                                }
                                val cnt_cartas=jugadores[i].saltos_acum
                                jugadores[i].ha_jugado = false
                                jugadores[i].saltos_acum=0
                                //Saltar el turno la cantidad de veces que se le asigne al jugador
                                for(j in 0..cnt_cartas) {
                                    saltarTurno()
                                }


                                break
                            }
                            else->{
                                //Si el jugador siguiente tiene cartas por agregar
                                if(jugadores[i+1].asignarCartas) {
                                    //Si se encuentra un 13 en el mazo de ese jugador
                                    if (jugadores[i+1].encontrar13()>0){
                                        //Se habilita la opción de asignar cartas al subsiguiente jugador
                                        jugadores[i+2].asignarCartas=true
                                        //Se le trasladan las cartas acumuladas del jugador previo
                                        jugadores[i+2].cartas_asignadas+=jugadores[i+1].cartas_asignadas

                                        //Se deshabilita la opcion de asignar cartas
                                        jugadores[i+1].asignarCartas=false
                                        //Se resetean las cartas asignadas
                                        jugadores[i+1].cartas_asignadas=0
                                    }

                                    //Si no se encuentra un 13 en el mazo del siguiente jugador
                                    else{
                                        //Se le trasladan las cartas acumuladas del jugador previo
                                        for(j in 0 until jugadores[i+1].cartas_asignadas){
                                            jugadores[i+1].agregarCarta(cartaAleatoria())
                                        }
                                        jugadores[i+1].cartas_asignadas=0
                                        jugadores[i+1].asignarCartas=false
                                    }
                                }
                                val cnt_saltos=jugadores[i].saltos_acum
                                jugadores[i].ha_jugado = false
                                jugadores[i].saltos_acum=0

                                //Saltar el turno la cantidad de veces que se le asigne al jugador
                                for(j in 0..cnt_saltos) {
                                    saltarTurno()
                                }

                                break
                            }
                        }


                    }
                }
            }
            tocarCarta()
        }
    }

    private fun otraCartaIgual(carta:CardView, mazo: MutableList<CardView>):Boolean{
        for (i in mazo) {
            if (carta.getValor() == i.getValor() ) {
                return true
            }
        }
        return false
    }

    //Funcion para crear 8 cartas aleatorias y asignarlas a un jugador
    private fun repartir_cartas(j : Jugador)
    {
        for (i in 1..8)
        {
            val newCard: CardView= cartaAleatoria()
            j.agregarCarta(newCard)
        }
    }

    //Funcion para crear una carta aleatoria
    private fun cartaAleatoria():CardView
    {
        val newCard = CardView(this,null)
        newCard.randomCV()
        return newCard
    }

    //Funcion para mostrar la baraja del jugador actual
    private fun verMazoActual(t: TableRow, c : MutableList<CardView>)
    {
        t.removeAllViews()
        for(i in c)
        {
            t.addView(i)
        }
    }
}


