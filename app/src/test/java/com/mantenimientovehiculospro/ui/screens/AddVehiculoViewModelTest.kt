package com.mantenimientovehiculospro.ui.screens

import android.app.Application
import com.mantenimientovehiculospro.data.local.UsuarioPreferences
import com.mantenimientovehiculospro.data.model.Vehiculo
import com.mantenimientovehiculospro.data.network.ApiService
import com.mantenimientovehiculospro.data.network.RetrofitProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddVehiculoViewModelTest {

    // --- MOCKS ---
    private val applicationMock: Application = mockk(relaxed = true)
    private val apiServiceMock: ApiService = mockk()

    // La clase que vamos a probar
    private lateinit var viewModel: AddVehiculoViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // 1. Mockear Singletons y Objetos Estáticos
        mockkObject(UsuarioPreferences)
        // --- CORRECCIÓN AQUÍ ---
        // Usamos coEvery en lugar de every porque obtenerUsuarioId es una 'suspend fun'
        coEvery { UsuarioPreferences.obtenerUsuarioId(any()) } returns 123L

        // Simulamos RetrofitProvider (este sí es normal, usamos every)
        mockkObject(RetrofitProvider)
        every { RetrofitProvider.instance } returns apiServiceMock

        // 2. Inicializar ViewModel
        viewModel = AddVehiculoViewModel(applicationMock)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    // --- PRUEBA 1: Verificar que el estado cambia al escribir ---
    @Test
    fun `onMarcaChange actualiza el estado del ViewModel`() = runTest(testDispatcher) {
        // Arrange
        val nuevaMarca = "Toyota"
        assertEquals("", viewModel.uiState.value.marca) // Estado inicial vacío

        // Act
        viewModel.onMarcaChange(nuevaMarca)

        // Assert
        assertEquals(nuevaMarca, viewModel.uiState.value.marca)
    }

    // --- PRUEBA 2: Verificar la lógica de Guardar Vehículo ---
    @Test
    fun `guardarVehiculo exitoso cambia vehiculoGuardado a true`() = runTest(testDispatcher) {
        // Arrange: Llenamos el formulario virtualmente
        viewModel.onMarcaChange("Mazda")
        viewModel.onModeloChange("3")
        viewModel.onAnioChange("2023")
        viewModel.onKilometrajeChange("5000")

        // Preparamos el vehículo que devolverá la API simulada
        val vehiculoEsperado = Vehiculo(
            id = 1, marca = "Mazda", modelo = "3", anio = 2023, kilometraje = 5000, propietarioId = 123L
        )

        // Entrenamos al mock de la API (también es suspend fun, usamos coEvery)
        coEvery { apiServiceMock.crearVehiculo(eq(123L), any()) } returns vehiculoEsperado

        // Act: Presionamos el botón "Guardar"
        viewModel.guardarVehiculo()

        // Avanzamos el tiempo para que la corrutina termine
        testScheduler.advanceUntilIdle()

        // Assert: Verificamos el estado final
        assertTrue(viewModel.uiState.value.vehiculoGuardado)
        assertEquals(null, viewModel.uiState.value.mensaje)
    }

    // --- PRUEBA 3: Verificar Validación de Campos Vacíos ---
    @Test
    fun `guardarVehiculo muestra error si faltan campos`() = runTest(testDispatcher) {
        // Arrange: Solo llenamos la marca
        viewModel.onMarcaChange("Chevrolet")

        // Act
        viewModel.guardarVehiculo()
        testScheduler.advanceUntilIdle()

        // Assert
        assertFalse(viewModel.uiState.value.vehiculoGuardado)
        assertEquals("Error: Por favor, completa todos los campos.", viewModel.uiState.value.mensaje)
    }
}