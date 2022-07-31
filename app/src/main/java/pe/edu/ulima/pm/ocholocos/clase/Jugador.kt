package pe.edu.ulima.pm.ocholocos.clase

import pe.edu.ulima.pm.ocholocos.views.CardView

class Jugador(var nombre:String)
{
    private var cant_cartas =0
    val mazo =mutableListOf<CardView>()

    var ha_jugado =false

    var asignarCartas=false//variable para saber si se le debe asignar 3 cartas a un jugador

    var cartas_asignadas=0//variable para saber cuantas cartas se le asignaran

    var volver_a_jugar=false //variable para saber si el jugador debe volver a jugar

    var saltos_acum=0//variable para saber cuantas veces se le asignaron 11

    var cant_12 = 0 //variable para saber cuantas veces se le asignaron 12
    var cant_13=0//variable para saber cuantas veces se le asignaron 13

    fun getCantCartas():Int
    {
        return cant_cartas
    }

    fun agregarCarta(c: CardView)
    {
        mazo.add(c)
        cant_cartas++
        cant_12=encontrar12()
        cant_13=encontrar13()
    }
    fun agregarAlinicio(c: CardView)
    {
        mazo.add(c)
        var cartaAux = c

        for (i in mazo.size-1 downTo 1)
        {
            mazo[i] = mazo[i-1]
        }
        mazo[0] = cartaAux
        cant_cartas++
        cant_12=encontrar12()
        cant_13=encontrar13()
    }
    fun eliminarCarta(c: CardView)
    {
        mazo.remove(c)
        cant_cartas--
        cant_12=encontrar12()
        cant_13=encontrar13()
    }

    fun encontrar12(): Int {
        var cont=0
        for (i in 0 until mazo.size)
        {
            if (mazo[i].getValor()==12)
            {
                cont++
            }
        }
        return cont
    }

    fun encontrar13():Int {
        var cont=0
        for (i in 0 until mazo.size)
        {
            if (mazo[i].getValor()==13)
            {
                cont++
            }
        }
        return cont
    }

}