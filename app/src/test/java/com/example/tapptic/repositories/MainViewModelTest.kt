package com.example.tapptic.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tapptic.entities.Dummy
import com.example.tapptic.helpers.Status
import com.example.tapptic.viewmodels.MainViewModel
import com.google.common.truth.Truth.assertThat
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class MainViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModelSuccess: MainViewModel

    private lateinit var viewModelFail: MainViewModel

    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        viewModelSuccess = MainViewModel(FakeRepositorySuccess())
        viewModelFail = MainViewModel(FakeRepositoryFail())
    }

    @Test
    fun `when is success response for fetchData then status success should be thrown`() {
        viewModelSuccess.fetchData()

        val value = viewModelSuccess.dummies.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `when is error response for fetchData then status error should be thrown`() {
        viewModelFail.fetchData()

        val value = viewModelFail.dummies.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `when invoke fetchData then observe valid dummies in liveData `() {
        val dummy = Dummy("1", "url", "text")

        viewModelSuccess.fetchData()

        val value = viewModelSuccess.dummies.getOrAwaitValueTest()

        assertThat(value.peekContent().data!![0]).isEqualTo(dummy)
    }

}