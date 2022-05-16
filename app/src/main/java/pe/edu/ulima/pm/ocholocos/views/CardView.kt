package pe.edu.ulima.pm.ocholocos.views


import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import pe.edu.ulima.pm.ocholocos.R
import kotlin.random.Random

class CardView(context: Context, attrs: AttributeSet?): AppCompatImageView(context, attrs)
{
    private var simbol:String=""
    private var value: Int=0
    private var s: Boolean=false

    fun getPalo(): String {
        return simbol
    }
    fun getValor(): Int {
        return value
    }
    fun getS(): Boolean {
        return s
    }

    fun setAttrs(x:Int, y:String)
    {
        simbol=y
        value=x

        val dR: Int = when(y+"_"+x){
            "corazon_1" -> R.drawable.corazon_1
            "corazon_2" -> R.drawable.corazon_2
            "corazon_3" -> R.drawable.corazon_3
            "corazon_4" -> R.drawable.corazon_4
            "corazon_5" -> R.drawable.corazon_5
            "corazon_6" -> R.drawable.corazon_6
            "corazon_7" -> R.drawable.corazon_7
            "corazon_8" -> R.drawable.corazon_8
            "corazon_9" -> R.drawable.corazon_9
            "corazon_10" -> R.drawable.corazon_10
            "corazon_11" -> R.drawable.corazon_11
            "corazon_12" -> R.drawable.corazon_12
            "corazon_13" -> R.drawable.corazon_13
            "espada_1" -> R.drawable.espada_1
            "espada_2" -> R.drawable.espada_2
            "espada_3" -> R.drawable.espada_3
            "espada_4" -> R.drawable.espada_4
            "espada_5" -> R.drawable.espada_5
            "espada_6" -> R.drawable.espada_6
            "espada_7" -> R.drawable.espada_7
            "espada_8" -> R.drawable.espada_8
            "espada_9" -> R.drawable.espada_9
            "espada_10" -> R.drawable.espada_10
            "espada_11" -> R.drawable.espada_11
            "espada_12" -> R.drawable.espada_12
            "espada_13" -> R.drawable.espada_13
            "rombo_1" -> R.drawable.rombo_1
            "rombo_2" -> R.drawable.rombo_2
            "rombo_3" -> R.drawable.rombo_3
            "rombo_4" -> R.drawable.rombo_4
            "rombo_5" -> R.drawable.rombo_5
            "rombo_6" -> R.drawable.rombo_6
            "rombo_7" -> R.drawable.rombo_7
            "rombo_8" -> R.drawable.rombo_8
            "rombo_9" -> R.drawable.rombo_9
            "rombo_10" -> R.drawable.rombo_10
            "rombo_11" -> R.drawable.rombo_11
            "rombo_12" -> R.drawable.rombo_12
            "rombo_13" -> R.drawable.rombo_13
            "trebol_1" -> R.drawable.trebol_1
            "trebol_2" -> R.drawable.trebol_2
            "trebol_3" -> R.drawable.trebol_3
            "trebol_4" -> R.drawable.trebol_4
            "trebol_5" -> R.drawable.trebol_5
            "trebol_6" -> R.drawable.trebol_6
            "trebol_7" -> R.drawable.trebol_7
            "trebol_8" -> R.drawable.trebol_8
            "trebol_9" -> R.drawable.trebol_9
            "trebol_10" -> R.drawable.trebol_10
            "trebol_11" -> R.drawable.trebol_11
            "trebol_12" -> R.drawable.trebol_12
            "trebol_13" -> R.drawable.trebol_13
            else -> {0}
        }
        this.setImageResource(dR)
    }

    //Esta funcion permite obtener una carta aleatoria
    fun randomCV()
    {
        val listaPalos = listOf("corazon", "espada", "rombo", "trebol")//listas auxiliares
        val listaNumeros = listOf(1,2,3,4,5,6,7,8,9,10,11,12,13)

        val palo= Random.nextInt(0,listaPalos.size)//numeros aleatorios
        val valor= Random.nextInt(0,listaNumeros.size)

        this.simbol=listaPalos[palo]//se almacena el valor del atributo de la instancia
        this.value=listaNumeros[valor]

        val dR: Int = when(listaPalos[palo] + "_" + listaNumeros[valor]){
            "corazon_1" -> R.drawable.corazon_1
            "corazon_2" -> R.drawable.corazon_2
            "corazon_3" -> R.drawable.corazon_3
            "corazon_4" -> R.drawable.corazon_4
            "corazon_5" -> R.drawable.corazon_5
            "corazon_6" -> R.drawable.corazon_6
            "corazon_7" -> R.drawable.corazon_7
            "corazon_8" -> R.drawable.corazon_8
            "corazon_9" -> R.drawable.corazon_9
            "corazon_10" -> R.drawable.corazon_10
            "corazon_11" -> R.drawable.corazon_11
            "corazon_12" -> R.drawable.corazon_12
            "corazon_13" -> R.drawable.corazon_13
            "espada_1" -> R.drawable.espada_1
            "espada_2" -> R.drawable.espada_2
            "espada_3" -> R.drawable.espada_3
            "espada_4" -> R.drawable.espada_4
            "espada_5" -> R.drawable.espada_5
            "espada_6" -> R.drawable.espada_6
            "espada_7" -> R.drawable.espada_7
            "espada_8" -> R.drawable.espada_8
            "espada_9" -> R.drawable.espada_9
            "espada_10" -> R.drawable.espada_10
            "espada_11" -> R.drawable.espada_11
            "espada_12" -> R.drawable.espada_12
            "espada_13" -> R.drawable.espada_13
            "rombo_1" -> R.drawable.rombo_1
            "rombo_2" -> R.drawable.rombo_2
            "rombo_3" -> R.drawable.rombo_3
            "rombo_4" -> R.drawable.rombo_4
            "rombo_5" -> R.drawable.rombo_5
            "rombo_6" -> R.drawable.rombo_6
            "rombo_7" -> R.drawable.rombo_7
            "rombo_8" -> R.drawable.rombo_8
            "rombo_9" -> R.drawable.rombo_9
            "rombo_10" -> R.drawable.rombo_10
            "rombo_11" -> R.drawable.rombo_11
            "rombo_12" -> R.drawable.rombo_12
            "rombo_13" -> R.drawable.rombo_13
            "trebol_1" -> R.drawable.trebol_1
            "trebol_2" -> R.drawable.trebol_2
            "trebol_3" -> R.drawable.trebol_3
            "trebol_4" -> R.drawable.trebol_4
            "trebol_5" -> R.drawable.trebol_5
            "trebol_6" -> R.drawable.trebol_6
            "trebol_7" -> R.drawable.trebol_7
            "trebol_8" -> R.drawable.trebol_8
            "trebol_9" -> R.drawable.trebol_9
            "trebol_10" -> R.drawable.trebol_10
            "trebol_11" -> R.drawable.trebol_11
            "trebol_12" -> R.drawable.trebol_12
            "trebol_13" -> R.drawable.trebol_13
            else -> {0}
        }

        this.setImageResource(dR)
    }


    override fun onTouchEvent(event: MotionEvent?):Boolean {
        s=true
        Log.i("MainActivity", "el s de la carta pulsada es: $s")
        return super.onTouchEvent(event)
    }
}