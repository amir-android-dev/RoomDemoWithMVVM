package com.amir.roomdemo

import android.util.Patterns
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

        if(inputName.value==null){
            statusMessage.value = Event("Please Enter name")
        }else if(inputEmail.value==null){
            statusMessage.value = Event("Please Enter Email")
        }else if(!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()){
            statusMessage.value = Event("Please Enter the correct Email address")
        }else{
            if (isUpdatedOrDelete) {
                subscriberEntityToUpdateOrDelete.name = inputName.value!!
                subscriberEntityToUpdateOrDelete.email = inputEmail.value!!
                update(subscriberEntityToUpdateOrDelete)
            } else {
                val name = inputName.value!!
                val email = inputEmail.value!!
                insert(SubscriberEntity(0, name, email))
                inputName.value = ""
                inputEmail.value = ""
            }
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
        val newRowId: Long = repository.insert(subscriberEntity)
        if (newRowId > -1) {
            statusMessage.value = Event("Subscriber inserted successfully $newRowId")
        } else {
            statusMessage.value = Event("Error Occurred")
        }

    }

    fun update(subscriberEntity: SubscriberEntity): Job = viewModelScope.launch {
        var noOfRows: Int = repository.update(subscriberEntity)
        if(noOfRows>0) {
            inputName.value = null
            inputEmail.value = null
            isUpdatedOrDelete = false
            subscriberEntityToUpdateOrDelete = subscriberEntity
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRows updated successfully")
        }else{
            statusMessage.value = Event("Error Occurred")
        }
    }

    fun delete(subscriberEntity: SubscriberEntity): Job = viewModelScope.launch {
       val noOfRowsDeleted= repository.delete(subscriberEntity)
        if (noOfRowsDeleted>0) {
            inputName.value = null
            inputEmail.value = null
            isUpdatedOrDelete = false
            subscriberEntityToUpdateOrDelete = subscriberEntity
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRowsDeleted Row deleted successfully")
        }else{
            statusMessage.value = Event("Error Occurred")
        }
    }

    fun clearAll(): Job = viewModelScope.launch {
        val noOfRowsDeleted= repository.deleteAll()
        if (noOfRowsDeleted>0) {
            statusMessage.value = Event("$noOfRowsDeleted deleted successfully")
        }else{
            statusMessage.value = Event("Error Occurred")
        }
    }

    fun getSaveSubscibers() = liveData {
        repository.subscribers.collect {
            emit(it)
        }
    }
}