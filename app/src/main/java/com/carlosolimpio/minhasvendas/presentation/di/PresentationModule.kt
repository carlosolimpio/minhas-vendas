package com.carlosolimpio.minhasvendas.presentation.di

import com.carlosolimpio.minhasvendas.domain.order.OrderRepository
import com.carlosolimpio.minhasvendas.presentation.OrderViewModel
import org.koin.dsl.module

private val viewModelModule = module {
    factory { OrderViewModel(get<OrderRepository>()) }
}

val presentationModule = module {
    includes(viewModelModule)
}
