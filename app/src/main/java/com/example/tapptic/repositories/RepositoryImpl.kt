package com.example.tapptic.repositories

import com.example.tapptic.entities.Dummy
import com.example.tapptic.remote.TappticApi
import io.reactivex.Observable
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: TappticApi
) : Repository {

    override fun fetchDetails(): Observable<List<Dummy>> {
        return api.fetchData()
    }

    override fun fetchDataForSelectedDummyItem(item: String): Observable<Dummy> {
        return api.fetchDataForSelectedDummyItem(item)
    }

}