package com.carlosolimpio.minhasvendas.data.di

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.carlosolimpio.minhasvendas.data.OrderRepositoryImpl
import com.carlosolimpio.minhasvendas.data.local.db.OrderDao
import com.carlosolimpio.minhasvendas.data.local.db.OrderRoomDatabase
import com.carlosolimpio.minhasvendas.domain.order.OrderRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val roomModule = module {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE order_table ADD COLUMN date TEXT")
        }
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            OrderRoomDatabase::class.java,
            "Orders Room Database"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    single<OrderDao> {
        val db = get<OrderRoomDatabase>()
        db.getOrdersDao()
    }
}

private val repositoryModule = module {
    factory<OrderRepository> { OrderRepositoryImpl(get()) }
}

val dataModule = module {
    includes(roomModule, repositoryModule)
}
