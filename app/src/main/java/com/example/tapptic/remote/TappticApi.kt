package com.example.tapptic.remote

import com.example.tapptic.entities.Dummy
import com.example.tapptic.helpers.UrlManager.URL_CASE
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface TappticApi {

    @GET(URL_CASE)
    fun fetchData(): Observable<List<Dummy>>

    @GET(URL_CASE)
    fun fetchDataForSelectedDummyItem(@Query("name") item: String): Observable<Dummy>

}