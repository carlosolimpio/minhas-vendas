package com.carlosolimpio.minhasvendas.presentation

import androidx.lifecycle.Observer
import com.carlosolimpio.minhasvendas.BaseTest
import com.carlosolimpio.minhasvendas.domain.core.UiState
import com.carlosolimpio.minhasvendas.domain.order.Order
import com.carlosolimpio.minhasvendas.domain.order.OrderRepository
import com.carlosolimpio.minhasvendas.getOrAwaitValue
import com.carlosolimpio.minhasvendas.presentation.orderdetails.OrderDetailsViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class OrderDetailsViewModelTest : BaseTest() {

    @MockK
    private lateinit var repository: OrderRepository
    private lateinit var viewModel: OrderDetailsViewModel

    override fun setUp() {
        super.setUp()
        viewModel = OrderDetailsViewModel(repository)
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
    fun `Test when fetching an order by its id, the returned order is valid`() = runTest {
        val observer = mockk<Observer<UiState<Order>>> { coEvery { onChanged(any()) } just Runs }

        coEvery { repository.getOrderFromId(orderId) } returns orderSuccessfulResponse

        viewModel.orderState.observeForever(observer)
        viewModel.fetchOrderById(orderId)

//        dispatcher.scheduler.advanceUntilIdle()
        val value = viewModel.orderState.getOrAwaitValue()

        assertEquals(orderId, (value as UiState.Success).data.number)
        assertEquals("Floyd Mayweather Jr.", value.data.clientName)
        assertTrue(value.data.items.isNotEmpty())
    }
}