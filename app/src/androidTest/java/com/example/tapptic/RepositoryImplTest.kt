package com.example.tapptic

import com.example.tapptic.entities.Dummy
import com.example.tapptic.repositories.RepositoryImpl
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class RepositoryImplTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: RepositoryImpl

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun readSampleSuccessJsonFile() {
        val reader = MockResponseFileReader("success_response_for_details.json")
        assertThat(reader.content).isNotEmpty()
    }

    @Test
    fun readSampleFailJsonFile() {
        val reader = MockResponseFileReader("fail_response.json")
        assertThat(reader.content).isNotEmpty()
    }

    @Test
    fun testIfRespondForFetchDetailsExist() {
        // Trigger
        val testObserver: TestObserver<List<Dummy>> = repository.fetchDetails().test()

        // Validation
        assertThat(testObserver.values()[0]).isNotEmpty()

        // Clean up
        testObserver.dispose()
    }

    @Test
    fun testIfRespondForFetchDetailsIsNotCorrect() {
        val mockedRespond =
            parseMockedStringToDummy(MockResponseFileReader("fail_response.json").content)

        // Trigger
        val testObserver: TestObserver<List<Dummy>> = repository.fetchDetails().test()

        // Validation
        assertThat(testObserver.values()[0]).doesNotContain(mockedRespond)

        // Clean up
        testObserver.dispose()
    }

    @Test
    fun testIfRespondForFetchDetailsIsCorrect() {
        val mockedRespond =
            parseMockedJSONtoList(MockResponseFileReader("success_response_for_details.json").content)

        // Trigger
        val testObserver: TestObserver<List<Dummy>> = repository.fetchDetails().test()

        // Validation
        assertThat(testObserver.values()[0]).contains(mockedRespond[0])

        // Clean up
        testObserver.dispose()
    }

    @Test
    fun testIffetchDataForSelectedDummyItemIsCorrect() {
        val mockedRespond =
            parseMockedStringToDummy(MockResponseFileReader("success_response_for_selected_item.json").content)

        // Trigger
        val testObserver: TestObserver<Dummy> =
            repository.fetchDataForSelectedDummyItem("1").test()

        // Validation
        assertThat(testObserver.values()[0]).isEqualTo(mockedRespond)

        // Clean up
        testObserver.dispose()
    }

    private fun parseMockedStringToDummy(mockResponse: String): Dummy {
        return Gson().fromJson(mockResponse, Dummy::class.java)
    }

    private fun parseMockedJSONtoList(mockResponse: String): List<Dummy> {
        val sType = object : TypeToken<List<Dummy>>() {}.type
        return Gson().fromJson<List<Dummy>>(mockResponse, sType)
    }

}