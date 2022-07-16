package com.amir.roomdemo

import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.amir.roomdemo.db.SubscriberEntity
import com.amir.roomdemo.db.SubscriberRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel() {

    //defining a mutbaleLiveData for the status message
    //we use this variable for string msg
    //but we are using our created event class as a wrapper. therfore the type should be an event of strings
   private val statusMessage = MutableLiveData<Event<String>>()
    //creating a getter for this liveData
    val message: LiveData<Event<String>>
    get() = statusMessage


    val inputName = MutableLiveData<String?>()

    val inputEmail = MutableLiveData<String?>()
    private var isUpdatedOrDelete = false
    private lateinit var subscriberEntityToUpdateOrDelete: SubscriberEntity


    val saveOrUpdateButtonText = MutableLiveData<String>()

    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    init {
        // 3 we do this because we want to change the text dynamically
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Delete All"
    }

    //4
    fun saveOrUpdate() {
        if (isUpdatedOrDelete) {
            subscriberEntityToUpdateOrDelete.name = inputName.value!!
            subscriberEntityToUpdateOrDelete.email=inputEmail.value!!
            update(subscriberEntityToUpdateOrDelete)
        } else {
            val name = inputName.value!!
            val email = inputEmail.value!!
            insert(SubscriberEntity(0, name, email))
            inputName.value = ""
            inputEmail.value = ""
        }

    }

    fun clearAllOrDelete() {
        if (isUpdatedOrDelete) {
            delete(subscriberEntityToUpdateOrDelete)
        } else {
            clearAll()
        }

    }

    fun initUpdateAndDelete(subscriberEntity: SubscriberEntity) {
        inputName.value = subscriberEntity.name
        inputEmail.value = subscriberEntity.email
        isUpdatedOrDelete = true
        subscriberEntityToUpdateOrDelete = subscriberEntity
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }

    //    fun insert(subscriberEntity: SubscriberEntity){
//        viewModelScope.launch {
//           repository.insert(subscriberEntity)
//        }
//    }
    fun insert(subscriberEntity: SubscriberEntity): Job = viewModelScope.launch {
        repository.insert(subscriberEntity)
        statusMessage.value= Event("Subscriber inserted successfully")
    }

    fun update(subscriberEntity: SubscriberEntity): Job = viewModelScope.launch {
        repository.update(subscriberEntity)
        inputName.value = null
        inputEmail.value = null
        isUpdatedOrDelete=false
        subscriberEntityToUpdateOrDelete=subscriberEntity
        saveOrUpdateButtonText.value="Save"
        clearAllOrDeleteButtonText.value="Clear All"
        statusMessage.value=Event("Subscriber updated successfully")
    }

    fun delete(subscriberEntity: SubscriberEntity): Job = viewModelScope.launch {
        repository.delete(subscriberEntity)
        inputName.value = null
        inputEmail.value = null
        isUpdatedOrDelete = false
        subscriberEntityToUpdateOrDelete = subscriberEntity
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
        statusMessage.value=Event("Subscriber deleted successfully")
    }

    fun clearAll(): Job = viewModelScope.launch {
        repository.deleteAll()
        statusMessage.value=Event("All Subscribers deleted successfully")
    }

    fun getSaveSubscibers() = liveData {
        repository.subscribers.collect {
            emit(it)
        }
    }
}