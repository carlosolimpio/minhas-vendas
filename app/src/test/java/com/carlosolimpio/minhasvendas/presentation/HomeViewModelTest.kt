package com.carlosolimpio.minhasvendas.presentation

import androidx.lifecycle.Observer
import com.carlosolimpio.minhasvendas.BaseTest
import com.carlosolimpio.minhasvendas.domain.core.UiState
import com.carlosolimpio.minhasvendas.domain.order.Order
import com.carlosolimpio.minhasvendas.domain.order.OrderRepository
import com.carlosolimpio.minhasvendas.getOrAwaitValue
import com.carlosolimpio.minhasvendas.presentation.home.HomeViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
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
class HomeViewModelTest : BaseTest() {
    @MockK
    private lateinit var repository: OrderRepository
    private lateinit var viewModel: HomeViewModel

    override fun setUp() {
        super.setUp()
        viewModel = HomeViewModel(repository)
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
}
