package com.amir.roomdemo.db
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
Here we can mention the database table
when room creates a table corresponds to this data class.

If we do not give any name like this, room will just use the class name “SubscriberEntity” as the tablename
 */
@Entity(tableName = "subscriber_data_table")
data class SubscriberEntity(
/*
We can also use “@ColumnInfo” annotation to Specify the name of the corresponding column in the table.
if you want it to be different from the name of the member variable. Giving separate names for the database table

than just using the names of the data class is the best practice. Because in a larger project we might need to use this same data class for other tasks, and with other libraries.
 */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="subscriber_id")
    var id:Int,
    @ColumnInfo(name="subscriber_name")
    var name:String,
    @ColumnInfo(name="subscriber_email")
    var email:String
)
