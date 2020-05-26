package com.vicomo.currencyconverter.dagger

import android.content.Context
import com.vicomo.currencyconverter.views.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepoModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun bindContext(context: Context) : Builder
        fun build(): AppComponent
    }
}