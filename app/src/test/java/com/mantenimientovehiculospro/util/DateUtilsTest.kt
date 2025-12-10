package com.mantenimientovehiculospro.util

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Clase de pruebas unitarias para DateUtils.kt.
 * Prueba la función de extensión String.formatearFechaVisual().
 */
class DateUtilsTest {

    // Caso de Prueba 1: Fecha válida en formato yyyy-MM-dd
    @Test
    fun `formatearFechaVisual convierte formato yyyy-MM-dd a dd-MM-yyyy correctamente`() {
        // 1. Arrange (Preparar)
        // Esta es la fecha como la guarda tu backend (yyyy-MM-dd)
        val fechaEntradaBackend = "2024-12-31"
        // Esto es como quieres que se vea en la pantalla (dd-MM-yyyy)
        val resultadoEsperado = "31-12-2024"

        // 2. Act (Ejecutar)
        // Llamamos a la función de extensión sobre el String de entrada
        val resultadoObtenido = fechaEntradaBackend.formatearFechaVisual()

        // 3. Assert (Verificar)
        assertEquals(
            "La fecha no se formateó al formato visual esperado", // Mensaje si falla
            resultadoEsperado,
            resultadoObtenido
        )
    }

    // Caso de Prueba 2: Fecha inválida o texto que no es fecha
    @Test
    fun `formatearFechaVisual devuelve el mismo texto si la entrada no es una fecha valida`() {
        // 1. Arrange & 2. Act
        val entradaInvalida = "Sin fecha asignada"
        val resultadoObtenido = entradaInvalida.formatearFechaVisual()

        // 3. Assert
        // Según tu código, si hay error, devuelve el mismo texto.
        assertEquals(entradaInvalida, resultadoObtenido)
    }
}