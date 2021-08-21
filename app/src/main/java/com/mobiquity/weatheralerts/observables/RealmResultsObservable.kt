package com.mobiquity.weatheralerts.observables

import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.realm.RealmChangeListener
import io.realm.RealmObject
import io.realm.RealmResults

class RealmResultsObservable<T :
RealmObject> private constructor(private val realmResults: RealmResults<T>) :
    ObservableOnSubscribe<RealmResults<T>> {

    @Throws(Exception::class)
    override fun subscribe(emitter: ObservableEmitter<RealmResults<T>>) {
        emitter.onNext(realmResults) // initial
        val changeListener = RealmChangeListener<RealmResults<T>> {
            emitter.onNext(it)
        }
        realmResults.addChangeListener(changeListener)
        emitter.setCancellable {
            try {
                realmResults.removeChangeListener(changeListener)
            } catch (e: IllegalStateException) {
                if (e.message?.contains("already been closed") == false) {
                    Log.d("Realm", "IllegalStateException: $e")
                }
            }
        }
    }

    companion object {
        fun <T : RealmObject> from(realmResults: RealmResults<T>): Observable<RealmResults<T>> {
            return Observable.create(RealmResultsObservable(realmResults))
        }
    }
}
