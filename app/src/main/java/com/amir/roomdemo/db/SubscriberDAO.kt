package com.amir.roomdemo.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriberDAO {
/*
can return a long value, which is the new rowId for the inserted item.If the parameter is an array or a collection,
 it should return an array of Long values or a list of Long values.
 */
    @Insert
    suspend fun insertSubscriber(subscriber: SubscriberEntity): Long


    @Update
    suspend fun updateSubscriber(subscriber: SubscriberEntity):Int

    @Delete
    suspend fun deleteSubscriber(subscriber: SubscriberEntity):Int


    @Query("delete from `subscriber_data_table`")
    suspend fun deleteAll():Int


    @Query("select * from `subscriber_data_table`")
     fun getAllSubscribers(): Flow<List<SubscriberEntity>>

}