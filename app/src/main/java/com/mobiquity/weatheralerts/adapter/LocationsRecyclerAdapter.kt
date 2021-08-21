package com.mobiquity.weatheralerts.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.view.RxView
import com.mobiquity.weatheralerts.R
import com.mobiquity.weatheralerts.logs.WALog
import com.mobiquity.weatheralerts.model.Location
import com.mobiquity.weatheralerts.observables.RealmResultsObservable
import io.reactivex.subjects.PublishSubject
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmRecyclerViewAdapter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LocationsRecyclerAdapter(val context: Context,
data:OrderedRealmCollection<Location>) : RealmRecyclerViewAdapter<Location,
        LocationsRecyclerAdapter.LocationTimeLineViewHolder>(data,true) {

    val clickSubject = PublishSubject.create<Location>()
    val clickSubjectObservable: io.reactivex.Observable<Location>?
        get() = clickSubject.share()

    val log: Logger by lazy { LoggerFactory.getLogger(LocationsRecyclerAdapter::class.java) }

    inner class LocationTimeLineViewHolder(val view:View):RecyclerView.ViewHolder(view) {
        var data: Location? = null

    }

    @SuppressLint("CheckResult")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationTimeLineViewHolder {
        val locationItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.location_row,parent,false)

        val viewHolder = LocationTimeLineViewHolder(locationItem)

        RxView.clicks(locationItem)
            .takeUntil(RxView.detaches(parent))
            .map<Location> {
                viewHolder.data as Location
            }
            .subscribe({
                clickSubject.onNext(it)
                WALog.info("",logger = log," CLicked Item Position ${it}")
            },{
                WALog.info("",logger = log," CLicked Item Position Error ${it}")
            })

      /*  RxView.touches(locationItem)
            .map { viewHolder.data as Location }
            .subscribe(clickSubject)*/

        return viewHolder
    }

    override fun getItemId(index: Int): Long {
        return data?.get(index)?.id?.hashCode()?.toLong() ?: return super.getItemId(index)
    }

    override fun getItem(index: Int): Location? {
        return data!![index]
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: LocationTimeLineViewHolder, position: Int) {

        val location =data?.get(position)

        holder.data = location

        Realm.getDefaultInstance().use { it ->
            RealmResultsObservable.from(
                it
                    .where(Location::class.java)
                    .findAllAsync()
            ).takeUntil(RxView.detaches(holder.itemView))
                .subscribe {results ->
                    if(results.size > 0){
                        val imageViewLocation =
                            holder.view.findViewById<AppCompatImageView>(R.id.location_image_view)
                        imageViewLocation.setImageResource(R.drawable.ic_baseline_add_location_24)
                        val imageViewDelete =
                            holder.view.findViewById<AppCompatImageView>(R.id.delete_location)
                        imageViewDelete.setImageResource(R.drawable.ic_baseline_delete_forever_24)

                        val arrayListOfLocationObjects: List<Location> =
                            it.copyFromRealm(results)

                        val locationTextView =
                            holder.view.findViewById<AppCompatTextView>(R.id.location_text_view)
                        var locationName :String? = null
                        var totalNumberOfLocations: Int = arrayListOfLocationObjects.size

                        for(i in results.indices){
                            locationName = arrayListOfLocationObjects.get(position).mLocality
                            WALog.info("",logger = log,"onBindViewHolder " +
                                    "   Location Name ${locationName}")
                        }
                        locationTextView.text = locationName
                    }
                }

        }
    }

    fun setLocation() {
        notifyDataSetChanged()
    }
}