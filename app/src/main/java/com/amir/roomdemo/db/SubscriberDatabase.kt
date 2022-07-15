package com.amir.roomdemo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
/*here we need to provide the list of entity classes
   we have just one entity class
 */
//then the version of database
@Database(entities = [SubscriberEntity::class], version = 1)
abstract class SubscriberDatabase: RoomDatabase() {

    //declaring an abstract reference for DAO interface
    /*
    we have only one entity class and a corresponding DAO interface.
    If we had more entity classes, we would have listed them all here and defined the refereces for corresponding
    DAOs here.
     */
    abstract val subscriberDAO:SubscriberDAO

    /*
    we can just create a new object and use this database class in other places of the project. But that is not the best practice.
    Usually, we should only use one instance of a Room database for the entire app. To avoid unexpected errors
    and performance issues, we should not let multiple instances of database opening at the same time.
    Therefore we create a singleton
     */
    /*
    In Kotlin we create singletons as companion objects. companion object. Then, the reference to the SubscriberDatabase
    annotate this with @Volatile annotation.
    Volatile annotation makes the field immediately made visible to other threads.
     Don’t worry about this boilerplate code part. You don’t have to remember this. You can copy paste this code part to all your room
     database classes and change the class name and the database name. fun getInstance parameter is Context.
     */
    companion object{
        @Volatile
        private var INSTANCE: SubscriberDatabase? =null
        //it returns our database
        fun getInstance(context: Context):SubscriberDatabase{
            synchronized(this){
                var instance = INSTANCE

                if(instance==null){
                    /*
                    Here we need to provide the applicationContext as the context
                     */
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SubscriberDatabase::class.java,
                        "subscriber_data_database"
                    ).build()
                }
                return instance
            }
        }
    }


}

