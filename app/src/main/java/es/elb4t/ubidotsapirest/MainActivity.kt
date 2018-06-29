package es.elb4t.ubidotsapirest

import android.app.Activity
import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.GpioCallback
import com.google.android.things.pio.PeripheralManager
import java.io.IOException
import java.util.*


class MainActivity : Activity() {
    // IDs Udidots
    private val token = "A1E-webyg2T4lhBo9tP4XK06ns4axgeaDF"
    private val idIluminacion = "5b3409d3c03f97039f91d01c"
    private val idBoton = "5b340a12c03f9703266b2d93"

    private val PIN_BUTTON = "BCM23"
    private lateinit var mButtonGpio: Gpio
    private var buttonstatus = 0.0
    private var handler:Handler? = Handler()
    private var runnable:UpdateRunner? = UpdateRunner()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val service = PeripheralManager.getInstance()
        try {
            mButtonGpio = service.openGpio(PIN_BUTTON)
            mButtonGpio.setDirection(Gpio.DIRECTION_IN)
            mButtonGpio.setActiveType(Gpio.ACTIVE_LOW)
            mButtonGpio.setEdgeTriggerType(Gpio.EDGE_FALLING)
            mButtonGpio.registerGpioCallback(mCallback)
        } catch (e: IOException) {
            Log.e(TAG, "Error en PeripheralIO API", e)
        }
        handler?.post(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler = null
        runnable = null
        if (mButtonGpio != null) {
            mButtonGpio.unregisterGpioCallback(mCallback)
            try {
                mButtonGpio.close()
            } catch (e: IOException) {
                Log.e(TAG, "Error en PeripheralIO API", e)
            }
        }
    }

    // Callback para envío asíncrono de pulsación de botón
    private val mCallback = GpioCallback {
        Log.i(TAG, "Botón pulsado!")
        if (buttonstatus === 0.0) buttonstatus = 1.0 else buttonstatus = 0.0
        val boton = Data()
        boton.variable = idBoton
        boton.value = buttonstatus
        val message = object : ArrayList<Data>() {
            init {
                add(boton)
            }
        }
        UbiClient.getClient().sendData(message, token)
        true // Mantenemos el callback activo
    }

    // Envío síncrono (5 segundos) del valor del fotorresistor
    private inner class UpdateRunner : Runnable {
        override fun run() {
            readLDR()
            Log.i(TAG, "Ejecución de acción periódica")
            handler?.postDelayed(this, 5000)
        }
    }

    private fun readLDR() {
        val iluminacion = Data()
        val message = ArrayList<Data>()
        val rand = Random()
        val valor = rand.nextFloat() * 5.0f
        iluminacion.variable = idIluminacion
        iluminacion.value = valor.toDouble()
        message.add(iluminacion)
        UbiClient.getClient().sendData(message, token)
    }

}
