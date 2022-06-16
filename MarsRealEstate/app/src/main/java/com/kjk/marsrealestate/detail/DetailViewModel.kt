package com.kjk.marsrealestate.detail

import android.app.Application
import androidx.lifecycle.*
import com.kjk.marsrealestate.R
import com.kjk.marsrealestate.domain.MarsProperty

class DetailViewModel(
    marsProperty: MarsProperty,
    app: Application
) : AndroidViewModel(app) {

    /** list에서 넘어온, MarsProperty LiveData*/
    private val _property = MutableLiveData<MarsProperty>()
    val property: LiveData<MarsProperty>
        get() = _property

    /** Type을 보여주기 위한 LiveData*/
    val displayPropertyType: LiveData<String> = Transformations.map(property) {
        app.applicationContext.getString(
            R.string.display_type,
            app.applicationContext.getString(
                when (it.isRental) {
                    true -> R.string.type_rent
                    false -> R.string.type_sale
                }
            )
        )
    }

    /** price를 보여주기 위한 LiveData*/
    val displayPropertyPrice: LiveData<String> = Transformations.map(property) {
        app.applicationContext.getString(
            when(it.isRental) {
                true -> R.string.display_monthly_price
                false -> R.string.display_price
            }, it.price
        )
    }

    init {
        _property.value = marsProperty
    }
}