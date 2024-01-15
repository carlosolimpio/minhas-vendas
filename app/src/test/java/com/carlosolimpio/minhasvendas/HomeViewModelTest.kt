package com.carlosolimpio.minhasvendas

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.carlosolimpio.minhasvendas.domain.core.OrderNotFoundException
import com.carlosolimpio.minhasvendas.domain.core.Resource
import com.carlosolimpio.minhasvendas.domain.core.UiState
import com.carlosolimpio.minhasvendas.domain.order.Item
import com.carlosolimpio.minhasvendas.domain.order.Order
import com.carlosolimpio.minhasvendas.domain.order.OrderRepository
import com.carlosolimpio.minhasvendas.presentation.HomeViewModel
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class HomeViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val dispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var repository: OrderRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)

        MockKAnnotations.init(this)
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Test when fetching the orderId, the loading state is shown before success`() = runTest {
        val observer = mockk<Observer<UiState<Long>>> { coEvery { onChanged(any()) } just Runs }

        coEvery { repository.retrieveOrderId() } returns orderIdSuccessfulResponse

        viewModel.orderIdState.observeForever(observer)
        viewModel.fetchOrderId()

        verifySequence {
            observer.onChanged(UiState.Loading)
            observer.onChanged(UiState.Success(orderId))
        }
    }

    @Test
    fun `Test when fetching the orderId, the orderId is correct`() = runTest {
        val observer = mockk<Observer<UiState<Long>>> { coEvery { onChanged(any()) } just Runs }

        coEvery { repository.retrieveOrderId() } returns orderIdSuccessfulResponse

        viewModel.orderIdState.observeForever(observer)
        viewModel.fetchOrderId()

        verify { observer.onChanged(UiState.Success(orderId)) }
    }

    @Test
    fun `Test when fetching all orders, the loading state is shown before success`() = runTest {
        val observer = mockk<Observer<UiState<List<Order>>>> {
            coEvery { onChanged(any()) } just Runs
        }

        coEvery { repository.getAllOrders() } returns orderListSuccessfulResponse

        viewModel.orderListState.observeForever(observer)
        viewModel.fetchAllOrders()

        verifySequence {
            observer.onChanged(UiState.Loading)
            observer.onChanged(UiState.Success(orderList))
        }
    }

    @Test
    fun `Test when fetching all orders, the loading state is shown before not found`() = runTest {
        val observer = mockk<Observer<UiState<List<Order>>>> {
            coEvery { onChanged(any()) } just Runs
        }

        coEvery { repository.getAllOrders() } returns orderListNotFoundResponse

        viewModel.orderListState.observeForever(observer)
        viewModel.fetchAllOrders()

        verifySequence {
            observer.onChanged(UiState.Loading)
            observer.onChanged(UiState.NotFound(notFoundMessage))
        }
    }

    @Test
    fun `Test when fetching all orders, contains a valid order`() = runTest {
        val observer = mockk<Observer<UiState<List<Order>>>> {
            coEvery { onChanged(any()) } just Runs
        }

        coEvery { repository.getAllOrders() } returns orderListSuccessfulResponse

        viewModel.orderListState.observeForever(observer)
        viewModel.fetchAllOrders()

        dispatcher.scheduler.advanceUntilIdle()
        val value = viewModel.orderListState.getOrAwaitValue()

        assertEquals(12L, (value as UiState.Success).data[0].number)
        assertEquals("Floyd Mayweather Jr.", value.data[0].clientName)
        assertTrue(value.data[0].items.isNotEmpty())
    }

    @Test
    fun `Test when fetching all orders, when no order is found, the message is shown`() = runTest {
        val observer = mockk<Observer<UiState<List<Order>>>> {
            coEvery { onChanged(any()) } just Runs
        }

        coEvery { repository.getAllOrders() } returns orderListNotFoundResponse

        viewModel.orderListState.observeForever(observer)
        viewModel.fetchAllOrders()

        val value = viewModel.orderListState.getOrAwaitValue()

        assertEquals(notFoundMessage, (value as UiState.NotFound).message)
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
    }
}
