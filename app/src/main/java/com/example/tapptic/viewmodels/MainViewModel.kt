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
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val disposables: CompositeDisposable = CompositeDisposable()

    private val _dummies = MutableLiveData<Event<Resource<List<Dummy>>>>()
    val dummies: LiveData<Event<Resource<List<Dummy>>>> = _dummies

    fun fetchData() {
        _dummies.postValue(Event(Resource.loading(null)))
        disposables.add(
            repository.fetchDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError)
        )
    }

    private fun onSuccess(respond: List<Dummy>) {
        _dummies.postValue(Event(Resource.success(respond)))
    }

    private fun onError(t: Throwable) {
        t.message?.let {
            _dummies.postValue(Event(Resource.error(it, null)))
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}