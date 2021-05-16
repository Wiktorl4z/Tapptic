package com.example.tapptic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tapptic.entities.Dummy
import com.example.tapptic.helpers.Event
import com.example.tapptic.helpers.Resource
import com.example.tapptic.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val disposables: CompositeDisposable = CompositeDisposable()

    private val _dummiesDetails = MutableLiveData<Event<Resource<Dummy>>>()
    val dummiesDetails: LiveData<Event<Resource<Dummy>>> = _dummiesDetails

    fun fetchDetailsForSelectedDummy(item: String) {
        _dummiesDetails.postValue(Event(Resource.loading(null)))
        disposables.add(
            repository.fetchDataForSelectedDummyItem(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError)
        )
    }

    private fun onSuccess(respond: Dummy) {
        _dummiesDetails.postValue(Event(Resource.success(respond)))
    }

    private fun onError(t: Throwable) {
        t.message?.let {
            _dummiesDetails.postValue(Event(Resource.error(it,null)))
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}