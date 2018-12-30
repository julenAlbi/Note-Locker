package com.example.julen.nativeappsproject.util

import android.arch.lifecycle.MutableLiveData
import android.databinding.BaseObservable
import android.databinding.Observable

/**
 * This class is used to overcome limitations of regular LiveData classes.
 * Regular LiveData will notify any observer when the value changes.
 * But when the LiveData wraps an object and you want to notify observers that the a property of
 * that object has changed, nothing happens.
 *
 * To overcome this limitation, the class inside the LiveData should extend BaseObservable,
 * and any property that should be observable should be annotated. The Crime class is a good example of this.
 *
 * This class extends the normal MutableLiveData class and adds trigger for when a property is changed.
 * See https://stackoverflow.com/questions/48020377/livedata-update-on-object-field-change
 */
class ObjectMutableLiveData<T : BaseObservable> : MutableLiveData<T>() {

    var callback: Observable.OnPropertyChangedCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable, propertyId: Int) {
            //Trigger LiveData observer on change of any property in object
            value = value

        }
    }


    override fun setValue(value: T?) {
        super.setValue(value)
        //listen to property changes
        value!!.addOnPropertyChangedCallback(callback)
    }


}