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
import kotlin.properties.Delegates

class MainActivity: AppCompatActivity()
{
    lateinit var tVturnoJ:TextView
    lateinit var eTcantidadCartas: TextView
    lateinit var btnCogerCarta: Button
    lateinit var btnPasarTurno: Button
    lateinit var mazoActual: TableRow
    lateinit var carta_del_centro: CardView
    private val lista_jugadores: MutableList<Jugador> = mutableListOf()

    //Se obtiene la cantidad de jugadores
    var cant_jgds = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Se carga la vista del Layout main_activity
        setContentView(R.layout.main_activity)

        cant_jgds = intent.getIntExtra("cantidad_jugadores", 0)

        //Se crea una lista de jugadores
        for (i in 1..cant_jgds)
        {
            lista_jugadores.add(Jugador("${intent.getStringExtra("jugador$i")}"))
            repartir_cartas(lista_jugadores[i-1])
        }

        //Se captura la carta Central y se le setea un valor aleatorio
        carta_del_centro = findViewById(R.id.central_card);
        carta_del_centro.randomCV()
        carta_del_centro.setAttrs(11,"corazon") //probando los 11's

        //Capturamos el textView
        tVturnoJ = findViewById(R.id.tVturnoJ)

        //Se setea el texto con el jugador inicial
        tVturnoJ.text = "Turno de ${lista_jugadores[0].nombre}"//jugador1

        //Se captura el table Row de la mesa
        mazoActual = findViewById(R.id.cartas_en_mano_jA)
        verMazoActual(mazoActual, lista_jugadores[0].mazo)//se muestra la baraja del jugador 1

        //Se captura el boton coger carta
        btnCogerCarta = findViewById(R.id.btnCogerCarta)

        //Se captura el boton pasar
        btnPasarTurno = findViewById(R.id.btnPasar)
        btnPasarTurno.isVisible=false

        //Se captura el texto de Cantidad de cartas
        eTcantidadCartas = findViewById(R.id.eTcantidadCartas)
        eTcantidadCartas.text=lista_jugadores[0].getCantCartas().toString()

        //Se da la funcionalidad de tocar cartas
        tocarCarta()


        //Cuando se toca sobre una carta
        btnCogerCarta.setOnClickListener {
            for(i in 0 until cant_jgds)
            {
                when
                {
                    // PARA CUALQUIER JUGADOR
                    tVturnoJ.text.contains(lista_jugadores[i].nombre) ->
                    {
                        //Si el jugador aún no ha lanzado una carta
                        if (!lista_jugadores[i].ha_jugado)
                        {
                            //Se asigna una carta aleatoria al jugador
                            lista_jugadores[i].agregarAlinicio(cartaAleatoria())

                            actualizarVistas(lista_jugadores[i])

                            btnPasarTurno.isVisible=true //se muestra el boton pasar
                            btnCogerCarta.isVisible=false   //se desaparece el boton coger carta

                            break
                        }
                    }
                }
            }
            tocarCarta()
        }

        //Cuando se toca el boton pasar
        btnPasarTurno.setOnClickListener()
        {
            btnPasarTurno.isVisible=false    //se desaparece el boton pasar
            btnCogerCarta.isVisible=true    //se muestra el boton coger carta

            for (i in 0 until cant_jgds) {
                when {
                    //SI ES TURNO DEL ULTIMO JUGADOR
                    tVturnoJ.text.contains(lista_jugadores[cant_jgds-1].nombre)->{

                        //Si el jugador 1 tiene cartas por asignar
                        if(lista_jugadores[0].asignarCartas) {
                            //Si se encuentra un 13 en el mazo de este jugador
                            if (lista_jugadores[0].encontrar13()>0 || lista_jugadores[0].encontrar12()>0){
                                //Se habilita la opción de asignar cartas al siguiente jugador
                                lista_jugadores[1].asignarCartas=true
                                //Se le traslada el valor del atributo cartas_asignadas
                                lista_jugadores[1].cartas_asignadas+=lista_jugadores[0].cartas_asignadas

                                lista_jugadores[0].cartas_asignadas=0
                                lista_jugadores[0].asignarCartas=false
                            }
                            //Si no se encuentra un 13 en el mazo del jugador 1
                            else{
                                //Se le trasladan las cartas acumuladas
                                for(j in 1..lista_jugadores[0].cartas_asignadas){
                                    lista_jugadores[0].agregarCarta(cartaAleatoria())
                                }
                                lista_jugadores[0].cartas_asignadas=0
                                lista_jugadores[0].asignarCartas=false
                            }
                        }

                        val cnt_saltos=lista_jugadores[cant_jgds-1].saltos_acum

                        lista_jugadores[cant_jgds-1].ha_jugado = false       //se declara el jugador actual como no jugado
                        lista_jugadores[cant_jgds-1].saltos_acum=0

                        //si la cantidad de jugadores es igual a 2
                        if(cant_jgds==2){
                            if(cnt_saltos==0) saltarTurno()
                        }else {
                            //Saltar el turno la cantidad de veces que se le asigne al jugador
                            (0..cnt_saltos).forEach { _ ->
                                saltarTurno()
                            }
                        }

                        break
                    }

                    // SI ES TURNO DE CUALQUIER  OTRO JUGADOR
                    tVturnoJ.text.contains(lista_jugadores[i].nombre) -> {
                        when(cant_jgds){
                            2->{
                                //Si el jugador siguiente tiene cartas por agregar
                                if(lista_jugadores[1].asignarCartas) {
                                    //Si se encuentra un 13 en el mazo de ese jugador
                                    if (lista_jugadores[1].encontrar13()>0){
                                        //Se habilita la opción de asignar cartas al subsiguiente jugador
                                        lista_jugadores[0].asignarCartas=true
                                        //Se le trasladan las cartas acumuladas del jugador previo
                                        lista_jugadores[0].cartas_asignadas+=lista_jugadores[1].cartas_asignadas

                                        //Se deshabilita la opcion de asignar cartas
                                        lista_jugadores[1].asignarCartas=false

                                        //Se resetean las cartas asignadas
                                        lista_jugadores[1].cartas_asignadas=0

                                    }

                                    //Si no se encuentra un 13 en el mazo del siguiente jugador
                                    else{
                                        //Se le trasladan las cartas acumuladas del jugador previo
                                        for(j in 0 until lista_jugadores[1].cartas_asignadas){
                                            lista_jugadores[1].agregarCarta(cartaAleatoria())
                                        }
                                        lista_jugadores[1].cartas_asignadas=0
                                        lista_jugadores[1].asignarCartas=false
                                    }
                                }
                                val cnt_saltos=lista_jugadores[i].saltos_acum
                                lista_jugadores[i].ha_jugado = false
                                lista_jugadores[i].saltos_acum=0
                                //Saltar el turno la cantidad de veces que se le asigne al jugador
                                if(cnt_saltos==0){
                                    saltarTurno()
                                }
                                break
                            }
                            else->{
                                //Si el jugador siguiente tiene cartas por agregar
                                if(lista_jugadores[i+1].asignarCartas) {
                                    //Si se encuentra un 13 en el mazo de ese jugador
                                    if (lista_jugadores[i+1].encontrar13()>0 || lista_jugadores[0].encontrar12()>0){
                                        //Se habilita la opción de asignar cartas al subsiguiente jugador
                                        lista_jugadores[i+2].asignarCartas=true
                                        //Se le trasladan las cartas acumuladas del jugador previo
                                        lista_jugadores[i+2].cartas_asignadas+=lista_jugadores[i+1].cartas_asignadas

                                        //Se deshabilita la opcion de asignar cartas
                                        lista_jugadores[i+1].asignarCartas=false
                                        //Se resetean las cartas asignadas
                                        lista_jugadores[i+1].cartas_asignadas=0
                                    }

                                    //Si no se encuentra un 13 en el mazo del siguiente jugador
                                    else{
                                        //Se le trasladan las cartas acumuladas del jugador previo
                                        for(j in 0 until lista_jugadores[i+1].cartas_asignadas){
                                            lista_jugadores[i+1].agregarCarta(cartaAleatoria())
                                        }
                                        lista_jugadores[i+1].cartas_asignadas=0
                                        lista_jugadores[i+1].asignarCartas=false
                                    }
                                }
                                val cnt_saltos=lista_jugadores[i].saltos_acum
                                lista_jugadores[i].ha_jugado = false
                                lista_jugadores[i].saltos_acum=0

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
        //Retornar verdadero si hay alguna otra carta en la mano igual a la lanzada
        return mazo.any { carta.getValor() == it.getValor() }
    }

    //Funcion para crear 8 cartas aleatorias y asignarlas a un jugador
    private fun repartir_cartas(j : Jugador)
    {
        val newCard2: CardView= cartaAleatoria()
        newCard2.setAttrs(12,"trebol")
        j.agregarCarta(newCard2)
        val newCard3: CardView= cartaAleatoria()
        newCard3.setAttrs(12,"corazon")
        j.agregarCarta(newCard3)

        for (i in 0..1)
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

    //Funcion para terminar el juego
    fun juegoTerminado()
    {
        val intent = Intent()
        intent.setClass(this,GanadorActivity::class.java)// Intent configurado

        //Pasar el jugador ganador
        val data= Bundle()
        for (i in lista_jugadores)
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
                tVturnoJ.text.contains(lista_jugadores[cant_jgds-1].nombre) -> {
                    //se cambia el turno al jugador 1
                    tVturnoJ.text = "Turno de ${lista_jugadores[0].nombre}"
                    //se muestra la baraja del jugador 1
                    verMazoActual(mazoActual, lista_jugadores[0].mazo)
                    //se muestra la cantidad de cartas del jugador 1
                    eTcantidadCartas.text = lista_jugadores[0].getCantCartas().toString()
                    Log.i(null,  "ha_jugado -> jugador 1: ${lista_jugadores[0].ha_jugado}\n"+
                                            "ha_jugado -> jugador ultimo: ${lista_jugadores[cant_jgds-1].ha_jugado} ")
                    break
                }
                //Para cualquier jugador
                tVturnoJ.text.contains(lista_jugadores[i].nombre) -> {
                    //se cambia el turno al jugador siguiente
                    tVturnoJ.text = "Turno de ${lista_jugadores[i+1].nombre}"

                    //se muestra la baraja del jugador siguiente
                    verMazoActual(mazoActual, lista_jugadores[i+1].mazo)

                    //se muestra la cantidad de cartas del siguiente jugador
                    eTcantidadCartas.text = lista_jugadores[i+1].getCantCartas().toString()
                    Log.i(null,  "ha_jugado -> jugador 1: ${lista_jugadores[0].ha_jugado}\n"+
                            "ha_jugado -> jugador ultimo: ${lista_jugadores[cant_jgds-1].ha_jugado} ")
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
        carta_del_centro.setAttrs(carta.getValor(),carta.getPalo())
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
                        if((c1.getValor() == carta_del_centro.getValor()) ||
                            (c1.getPalo() == carta_del_centro.getPalo())
                        )
                        {
                            //Se busca al jugador actual
                            for(j in 0 until cant_jgds)
                            {
                                //SI AÚN NO HA LANZADO UNA CARTA
                                when
                                {
                                    //SI ES TURNO DE UN JUGADOR
                                    tVturnoJ.text.contains(lista_jugadores[j].nombre) ->
                                        if (!lista_jugadores[j].ha_jugado)//Se verifica si ha jugado
                                        {
                                            //Se evalúa el valor de la carta tocada
                                            evaluarCartaTocada(c1, j)

                                            //Se verifica el atributo volver a jugar
                                            if (verificarVolverAjugar(j, c1)) continue

                                            //--------------------------------------------------

                                            //Cuando la cantidad de cartas es 0 hacer algo y cuando es 1 hacer otra cosa
                                            when{
                                                lista_jugadores[j].getCantCartas() == 0 -> {
                                                    //se termina el juego
                                                    juegoTerminado()
                                                    break
                                                }
                                                lista_jugadores[j].getCantCartas() == 1 -> {
                                                    //Se muestra un aviso
                                                    Toast.makeText(this,
                                                        "¡${lista_jugadores[j].nombre} tiene una carta!",
                                                        Toast.LENGTH_LONG).show()
                                                }
                                            }

                                            //Si se tiene una carta igual a la lanzada
                                            if (verificarOtraCartaIgual(c1, j)) continue
                                            break
                                        }
                                        else{
                                            continue
                                        }
                                    else->
                                        continue

                                }
                            }

                        }

                        //Si la carta no es igual a la central en valor o palo
                        else
                        {
                            //Se busca el jugador actual
                            verificarValorCarta()
                        }
                    }
                }
            }
            true
        }
    }

    private fun evaluarCartaTocada(c1: CardView, j: Int) {
        when (c1.getValor()) {
            //Si la carta tocada es un 11
            11 -> {
                lista_jugadores[j].saltos_acum++
            }

            12 -> {
                when (j) {
                    cant_jgds - 1 -> {
                        lista_jugadores[0].asignarCartas = true
                        lista_jugadores[0].cartas_asignadas += 2

                    }
                    else -> {
                        lista_jugadores[j + 1].asignarCartas = true
                        lista_jugadores[j + 1].cartas_asignadas += 2
                    }
                }
            }
            //Si la carta tocada es un 13
            13 -> {
                when (j) {
                    cant_jgds - 1 -> {
                        lista_jugadores[0].asignarCartas = true
                        lista_jugadores[0].cartas_asignadas += 3

                    }
                    else -> {
                        lista_jugadores[j + 1].asignarCartas = true
                        lista_jugadores[j + 1].cartas_asignadas += 3
                    }
                }
            }


        }
    }

    private fun verificarValorCarta() {
        for (j in 0 until cant_jgds) {
            when {
                // Cuando se encuentra al jugador actual
                tVturnoJ.text.contains(lista_jugadores[j].nombre) -> {
                    //Si es que este no ha jugado
                    if (lista_jugadores[j].ha_jugado) {
                        continue
                    } else {
                        Toast.makeText(this,
                            "No puedes coger esa carta",
                            Toast.LENGTH_SHORT).show()
                        break
                    }
                }
            }
        }
    }

    private fun verificarOtraCartaIgual(
        c1: CardView,
        j: Int,
    ): Boolean {
        if (otraCartaIgual(c1, lista_jugadores[j].mazo)) {
            //Si carta tocada = carta central
            if (c1.getValor() == carta_del_centro.getValor()) {
                //Se setea el atributo volver a jugar
                lista_jugadores[j].volver_a_jugar = true

                //Se muestra un mensaje
                Toast.makeText(this,
                    "Se puede repetir el turno",
                    Toast.LENGTH_SHORT).show()

                //Se oculta el boton pasar
                btnPasarTurno.isVisible = false

                //Se da la funcionalidad de tocar carta
                tocarCarta()
            } else {
                return true
            }
        } else {
            //se setea el atributo ha_jugado
            lista_jugadores[j].ha_jugado = true

            //se desaparece el boton coger carta
            btnCogerCarta.isVisible = false

            //se muestra el boton pasar
            btnPasarTurno.isVisible = true
        }
        return false
    }

    private fun verificarVolverAjugar(
        j: Int,
        c1: CardView,
    ): Boolean {
        if (lista_jugadores[j].volver_a_jugar) {
            //Se setea el atributo volver a jugar como false
            lista_jugadores[j].volver_a_jugar = false

            // Si carta tocada = carta central en valor
            if (c1.getValor() == carta_del_centro.getValor()) {
                //Se lanza la carta
                lanzarCarta(lista_jugadores[j], c1)
            } else {
                return true
            }
        }

        //Si no se esta volviendo a jugar
        else {

            //Se lanza la carta
            lanzarCarta(lista_jugadores[j], c1)
        }
        return false
    }
}


