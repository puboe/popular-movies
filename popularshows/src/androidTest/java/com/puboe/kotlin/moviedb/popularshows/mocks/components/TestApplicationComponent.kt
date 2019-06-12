package com.puboe.kotlin.moviedb.popularshows.mocks.components

import com.puboe.kotlin.moviedb.popularshows.TestApplication
import com.puboe.kotlin.moviedb.popularshows.mocks.modules.EntitiesTestModule
import com.puboe.kotlin.moviedb.popularshows.mocks.modules.PopularShowsTestModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        PopularShowsTestModule::class,
        EntitiesTestModule::class
    ]
)
interface TestApplicationComponent : AndroidInjector<TestApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<TestApplication>() {
        abstract fun entitiesModule(entitiesModule: EntitiesTestModule): Builder
    }
}