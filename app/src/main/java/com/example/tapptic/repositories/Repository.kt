package com.example.tapptic.repositories

import com.example.tapptic.entities.Dummy
import io.reactivex.Observable

interface Repository {

    fun fetchDetails(): Observable<List<Dummy>>

    fun fetchDataForSelectedDummyItem(item: String): Observable<Dummy>

}