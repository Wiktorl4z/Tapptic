package com.example.tapptic.repositories

import com.example.tapptic.entities.Dummy
import io.reactivex.Observable

class FakeRepositoryFail : Repository {

    override fun fetchDetails(): Observable<List<Dummy>> {
        return Observable.error(Error(Exception()))
    }

    override fun fetchDataForSelectedDummyItem(item: String): Observable<Dummy> {
        return Observable.just(Dummy("1", "url", "text"))
    }
}