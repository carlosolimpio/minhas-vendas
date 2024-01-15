package com.carlosolimpio.minhasvendas.presentation.di

import com.carlosolimpio.minhasvendas.domain.order.OrderRepository
import com.carlosolimpio.minhasvendas.presentation.HomeViewModel
import com.carlosolimpio.minhasvendas.presentation.orderdetails.OrderDetailsViewModel
import org.koin.dsl.module

private val viewModelModule = module {
    factory { HomeViewModel(get<OrderRepository>()) }
    factory { OrderDetailsViewModel(get<OrderRepository>()) }
}

val presentationModule = module {
    includes(viewModelModule)
}
