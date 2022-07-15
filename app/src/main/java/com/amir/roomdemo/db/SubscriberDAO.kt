package com.amir.roomdemo.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriberDAO {
    /*
    Why do we have this suspend modifier. Room doesn't support database access on the main thread. Because it might lock the UI for a long period of time.
    Therefore we need to execute these functions in a background thread. For that we can use async task, rxjava or executors.
    But the most advanced, easiest and efficient way is kotlin coroutine

    A suspending function is simply a function that can be paused and resumed at a later time.
     */
    /*
    OnConflictStrategy.REPLACE, This will try to insert the entity and, if there is an existing row that has the same ID value, it will delete the old entity and replace
    the row with the new entity
    if we set it as IGNORE, it will ignore the complicit.
      @Insert(onConflict = OnConflictStrategy.REPLACE)
     */
    @Insert
    suspend fun insertSubscriber(subscriber: SubscriberEntity): Long

    /*
    Some times for a verification we might need to get a return value .
    We can actually get the new rowId or rowIds as the return value of type long.
    @Insert
    suspend fun insertSubscriber(subscriber: SubscriberEntity): Long
    The type should be either long or an arrary or a list of long values If we are expecting to get
     the inserted row id as the return value of these functions,
        @Insert
    suspend fun insertSubscriber(subscriber: SubscriberEntity): List´<Long>
     */
    @Update
    suspend fun updateSubscriber(subscriber: SubscriberEntity)

    @Delete
    suspend fun deleteSubscriber(subscriber: SubscriberEntity)

    /*
    @Query annotation, room allows us to include a SQL query that would run when the function is called.
     Most importantly , this query will be verified at compile time by Room to ensure that it works well with the database.
     So there will be no database queries related run time errors. This is a major advantage of using room data persistence library.
     */
    @Query("delete from `subscriber_data_table`")
    suspend fun deleteAll()

    /*
    Room facilitates us to get data from a data base table as a Live data of list of entities .
    These queries are called Asynchronous queries. Because for these queries which has a LiveData as a return value , room always
    run them on a background thread by itself.
     */
    /*
    We didn’t add a suspend modifier for this function.Why is that?
    Bucause we don’s need to execute this function in a background thread using coroutines.
    Since this function returns LiveData, room library do its work from a background thread. So, this is our Data Access Object interface.
     */
    @Query("select * from `subscriber_data_table`")
     fun getAllSubscribers(): Flow<List<SubscriberEntity>>

}