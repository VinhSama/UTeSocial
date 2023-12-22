package com.utesocial.android.remote.simpleCallAdapter

import com.utesocial.android.core.data.util.Common
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.remote.networkState.Error
import com.utesocial.android.remote.networkState.NetworkState
import com.utesocial.android.remote.networkState.Status
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class SimpleCall<R> constructor(
    private val call : Call<R>,
    ) {

    fun execute() : SimpleResponse<R?> {
        return try {
            responseSingle(call)
                .subscribeOn(Schedulers.io())
                .blockingGet()
        } catch (e : Exception) {
            SimpleResponse(
                NetworkState(
                    Status.FAILED,
                e.message?.let { Error.customError(it) })
            ) {
                null
            }
        }
    }
    fun process(disposable: CompositeDisposable, onStateChanged: OnStateChanged<R?>) {
        responseSingle(call)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                if(call.isExecuted){
                    call.cancel()
                }
            }
            .doOnSubscribe {
                onStateChanged.onChanged(SimpleResponse(NetworkState.LOADING){ null})
                disposable.add(disposable)
            }
            .subscribe(object : DisposableSingleObserver<SimpleResponse<R?>>() {
                override fun onSuccess(t: SimpleResponse<R?>) {
                    if (!isDisposed) {
                        onStateChanged.onChanged(t)
                        dispose()
                    }
                }

                override fun onError(e: Throwable) {
                    if (!isDisposed) {
                        val errorState = NetworkState(Status.FAILED, Error.UndefinedError)
                        onStateChanged.onChanged(SimpleResponse(errorState) { null })
                        dispose()
                    }
                }

            })
    }
    public interface OnStateChanged<R> {
        public fun onChanged(response: SimpleResponse<R>)
    }
    private fun responseSingle(call : Call<R>): Single<SimpleResponse<R?>> {
        return Single.create {
            call.enqueue(object : Callback<R> {
                override fun onResponse(call: Call<R>, response: Response<R>) {
                    val simpleResponse = SimpleResponse(NetworkState.LOADED) {
                        response.body()
                    }
                    if(!response.isSuccessful) {
                        val errorMessage = Common.getDetailMessageBody<String>(responseBody = response.errorBody(), "error") ?: response.message()
                        val error = Error.CustomError(errorMessage)
                        Debug.log("responseSingle", "error:$error")
                        simpleResponse.setNetworkState(networkState = NetworkState(Status.FAILED, error))
                    }
                    it.onSuccess(simpleResponse)
                }

                override fun onFailure(call: Call<R>, t: Throwable) {
                    val networkState = NetworkState(Status.FAILED, Common.errorHandler(call, t))
                    val simpleResponse = SimpleResponse<R?>(networkState){ null }
                    t.message?.let { it1 -> Debug.log("Simple:onFailure", it1) }
                    it.onSuccess(simpleResponse)
                }
            })
        }
    }
}