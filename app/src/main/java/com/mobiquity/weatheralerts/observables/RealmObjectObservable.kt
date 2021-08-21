package com.mobiquity.weatheralerts.observables

import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.realm.RealmChangeListener
import io.realm.RealmObject

class RealmObjectObservable<T : RealmObject> private constructor(private val realmObject: T)
    : ObservableOnSubscribe<T> {

    @Throws(Exception::class)
    override fun subscribe(emitter: ObservableEmitter<T>) {
        emitter.onNext(realmObject) // initial
        val changeListener = RealmChangeListener<T> { emitter.onNext(it) }
        realmObject.addChangeListener(changeListener)
        emitter.setCancellable {
            try {
                realmObject.removeChangeListener(changeListener)
            } catch (e: IllegalStateException) {
                if (e.message?.contains("already been closed") == false) {
                    Log.w("Realm", "IllegalStateException: $e")
                }
            }
        }
    }

    companion object {
        fun <T : RealmObject> from(realmObject: T): Observable<T> {
            return Observable.create(RealmObjectObservable(realmObject))
        }
    }
}
