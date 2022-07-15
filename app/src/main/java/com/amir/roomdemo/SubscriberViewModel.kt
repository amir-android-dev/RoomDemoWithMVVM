package com.amir.roomdemo

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.amir.roomdemo.db.SubscriberEntity
import com.amir.roomdemo.db.SubscriberRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/*1
  To use functions defined in the repository class,we need to have a reference to SubscriberRepository.
   */
class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel() {



    /*2
    for the subscribers name and email define two MutableLiveData
    update 2021 we dont need to anotate with Bindable
     */
    //@Bindable
    val inputName = MutableLiveData<String>()

    val inputEmail = MutableLiveData<String>()


    val saveOrUpdateButtonText = MutableLiveData<String>()

    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    init {
        // 3 we do this because we want to change the text dynamically
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Delete All"
    }

    //4
    fun saveOrUpdate() {
        val name = inputName.value!!
        val email = inputEmail.value!!
        insert(SubscriberEntity(0,name,email))
        inputName.value = ""
        inputEmail.value =""
    }

    fun clearAllOrDelete() {
        clearAll()
    }

    //    fun insert(subscriberEntity: SubscriberEntity){
//        viewModelScope.launch {
//           repository.insert(subscriberEntity)
//        }
//    }
  fun insert(subscriberEntity: SubscriberEntity): Job = viewModelScope.launch {
        repository.insert(subscriberEntity)
    }

    fun update(subscriberEntity: SubscriberEntity): Job = viewModelScope.launch {
        repository.update(subscriberEntity)
    }

    fun delete(subscriberEntity: SubscriberEntity): Job = viewModelScope.launch {
        repository.delete(subscriberEntity)
    }

    fun clearAll(): Job = viewModelScope.launch {
        repository.deleteAll()
    }

    fun getSaveSubscibers()= liveData {
        repository.subscribers.collect{
            emit(it)
        }
    }
}