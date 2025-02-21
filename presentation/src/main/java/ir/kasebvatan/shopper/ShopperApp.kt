package ir.kasebvatan.shopper

import android.app.Application
import ir.kasebvatan.data.di.dataModule
import ir.kasebvatan.domain.di.domainModule
import ir.kasebvatan.shopper.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ShopperApp : Application() {


    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ShopperApp)
            modules(
                listOf(
                    presentationModule,
                    dataModule,
                    domainModule
                )
            )
        }

    }

}