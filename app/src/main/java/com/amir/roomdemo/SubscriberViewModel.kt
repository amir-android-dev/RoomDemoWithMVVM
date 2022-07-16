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
    }

    fun update(subscriberEntity: SubscriberEntity): Job = viewModelScope.launch {
        repository.update(subscriberEntity)
        inputName.value = null
        inputEmail.value = null
        isUpdatedOrDelete=false
        subscriberEntityToUpdateOrDelete=subscriberEntity
        saveOrUpdateButtonText.value="Save"
        clearAllOrDeleteButtonText.value="Clear All"
    }

    fun delete(subscriberEntity: SubscriberEntity): Job = viewModelScope.launch {
        repository.delete(subscriberEntity)
        inputName.value = null
        inputEmail.value = null
        isUpdatedOrDelete = false
        subscriberEntityToUpdateOrDelete = subscriberEntity
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun clearAll(): Job = viewModelScope.launch {
        repository.deleteAll()
    }

    fun getSaveSubscibers() = liveData {
        repository.subscribers.collect {
            emit(it)
        }
    }
}