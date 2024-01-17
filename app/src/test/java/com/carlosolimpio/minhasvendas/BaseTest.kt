package com.carlosolimpio.minhasvendas

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.carlosolimpio.minhasvendas.domain.core.OrderNotFoundException
import com.carlosolimpio.minhasvendas.domain.core.Resource
import com.carlosolimpio.minhasvendas.domain.order.Item
import com.carlosolimpio.minhasvendas.domain.order.Order
import io.mockk.MockKAnnotations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
open class BaseTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    val dispatcher = StandardTestDispatcher()

    @Before
    open fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    //    @Test
    //    fun ``() = runTest {
    //
    //    }

    companion object {
        const val orderId = 123L
        val orderIdSuccessfulResponse = Resource.Success<Long>(orderId)

        val orderList = listOf(
            Order(
                12L,
                "Floyd Mayweather Jr.",
                listOf(
                    Item("boxing gloves", 10, 100.0),
                    Item("boxing shoes", 1, 500.0)
                )
            ),
            Order(
                13L,
                "Saúl Álvarez",
                listOf(Item("boxing shoes", 5, 200.0))
            ),
            Order(
                14L,
                "Manny Pacquiao",
                listOf(
                    Item("boxing wraps", 15, 10.0),
                    Item("boxing gloves", 2, 300.0),
                    Item("boxing shoes", 3, 400.0)
                )
            )
        )

        const val notFoundMessage = "error not found message"

        val orderListSuccessfulResponse = Resource.Success<List<Order>>(orderList)
        val orderListNotFoundResponse = Resource.Error<List<Order>>(
            null,
            OrderNotFoundException(notFoundMessage)
        )

        val orderSuccessfulResponse = Resource.Success<Order?>(
            Order(
                orderId,
                "Floyd Mayweather Jr.",
                listOf(
                    Item("boxing gloves", 10, 100.0),
                    Item("boxing shoes", 1, 500.0)
                )
            )
        )
    }
}