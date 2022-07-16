package com.amir.roomdemo.db

/*
we add an instance of SubscriberDAO interface as a constructor parameter for this class. Because
we are going to call to the functions of the DAO from the repository.
 */
class SubscriberRepository(private val dao: SubscriberDAO) {
    /*
    As we discussed during the previous lesson there is no requirement to call to this getAllSubscribers function from a background thread.
    Because room library automatically process these functions with live data as a return type in a background thread.
    All other DAO functions should be called from a background thread.
     */
    val subscribers = dao.getAllSubscribers()

    suspend fun insert(subscriberEntity: SubscriberEntity): Long{
      return dao.insertSubscriber(subscriberEntity)
    }

    suspend fun update(subscriberEntity: SubscriberEntity):Int{
        return dao.updateSubscriber(subscriberEntity)
    }
    suspend fun delete(subscriberEntity: SubscriberEntity){
        dao.deleteSubscriber(subscriberEntity)
    }
    suspend fun deleteAll(){
        dao.deleteAll()
    }

}