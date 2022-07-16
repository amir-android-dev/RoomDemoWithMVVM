package com.amir.roomdemo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amir.roomdemo.databinding.ActivityMainBinding
import com.amir.roomdemo.db.SubscriberDAO
import com.amir.roomdemo.db.SubscriberDatabase
import com.amir.roomdemo.db.SubscriberEntity
import com.amir.roomdemo.db.SubscriberRepository
import kotlinx.coroutines.flow.observeOn

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var subscriberViewModel: SubscriberViewModel

    /*
    Since we need to refer to this adapter instance, let’s define it as a class level variable.
     */
    private lateinit var adapter: RvAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
/*
To create a SubscriberViewModelFactory instance we need to pass a dao instance as an argument.
 */
        val dao = SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)
        subscriberViewModel = ViewModelProvider(this, factory).get(SubscriberViewModel::class.java)
        binding.mViewModel = subscriberViewModel
        //Since, we are intending use livedata with data binding, we need to provide a lifecycle owner.
        binding.lifecycleOwner = this

        initRecyclerView()
        subscriberViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initRecyclerView() {
        binding.subscriberRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RvAdapter({ selectedItem: SubscriberEntity -> listItemClicked(selectedItem) })
        binding.subscriberRecyclerView.adapter = adapter

        displaySubscribersList()
    }

    //create a function to observe the list of subscribers’ data in the database table
    @SuppressLint("NotifyDataSetChanged")
    private fun displaySubscribersList() {
        subscriberViewModel.getSaveSubscibers().observe(this, Observer {
            Log.i("My Tag", it.toString())
            adapter.setList(it)
            //So, we must tell recycler view , that there is a new update.
            adapter.notifyDataSetChanged()
            //passing the function as an argument
//            binding.subscriberRecyclerView.adapter =
//                RvAdapter(it, { selectedItem: SubscriberEntity -> listItemClicked(selectedItem) })
        })
    }

    private fun listItemClicked(subscriberEntity: SubscriberEntity) {
        //Toast.makeText(this,"selected name is ${subscriberEntity.name}",Toast.LENGTH_SHORT).show()
        subscriberViewModel.initUpdateAndDelete(subscriberEntity)

    }
}