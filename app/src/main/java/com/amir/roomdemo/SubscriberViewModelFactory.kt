package com.amir.roomdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amir.roomdemo.db.SubscriberRepository
import java.lang.IllegalArgumentException

class SubscriberViewModelFactory(private val repository: SubscriberRepository) :
    ViewModelProvider.Factory {
    /*
    This next code part is just boilerplate code we use for every view model factory.
    I just copied it form our previous View model factory example and changed the view model name and the constructor parameter name
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      //  return super.create(modelClass)
        if (modelClass.isAssignableFrom(SubscriberViewModel::class.java)) {
            return SubscriberViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}