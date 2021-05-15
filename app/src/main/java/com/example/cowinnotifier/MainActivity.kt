package com.example.cowinnotifier

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.cowinnotifier.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.loadStateList()
//        viewModel.loadDistrictList(1)

        viewModel.stateListLiveData.observe(this, Observer { it ->
            print(it.toString())
        })

//        viewModel.districtListLiveData.observe(this, Observer { it ->
//            print(it.toString())
//        })
    }
}