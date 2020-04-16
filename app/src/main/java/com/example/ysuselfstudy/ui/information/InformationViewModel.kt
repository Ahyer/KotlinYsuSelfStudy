package com.example.ysuselfstudy.ui.information

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.ysuselfstudy.logic.Repository

class InformationViewModel : ViewModel() {
    private var info = MutableLiveData<String>()

    var listOfInformation = Transformations.switchMap(info) { it ->
        Repository.getInformation()
    }


    fun getInforList() {
        info.value = info.value
    }

}