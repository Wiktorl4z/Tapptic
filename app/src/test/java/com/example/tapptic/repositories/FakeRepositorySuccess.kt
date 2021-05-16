package com.example.tapptic.repositories

import com.example.tapptic.entities.Dummy
import io.reactivex.Observable

class FakeRepositorySuccess : Repository {

    override fun fetchDetails(): Observable<List<Dummy>> {
        return Observable.just(listOf(Dummy("1", "url", "text")))
    }

    override fun fetchDataForSelectedDummyItem(item: String): Observable<Dummy> {
        return Observable.just(Dummy("1", "url", "text"))
    }
}