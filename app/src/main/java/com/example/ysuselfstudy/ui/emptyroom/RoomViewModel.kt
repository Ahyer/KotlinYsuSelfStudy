package com.example.ysuselfstudy.ui.emptyroom

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.ysuselfstudy.data.EmptyRoom
import com.example.ysuselfstudy.logic.Repository
import com.example.ysuselfstudy.adapter.ExpandAdapte
import com.example.ysuselfstudy.logic.Dao
import java.util.ArrayList

class RoomViewModel(var time: MutableLiveData<String>) : ViewModel() {
    private val TAG = "RoomViewModel"

    private var biyingtemp = MutableLiveData<String>()

    var hello =
        "https://cn.bing.com/th?id=OHR.UnicornoftheSea_ZH-CN2949385175_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp"

    var uini1: ExpandAdapte.UnitData<String, String> =
        ExpandAdapte.UnitData("东校区", listOf("第一教学楼", "第二教学楼", "第三教学楼", "第四教学楼"))
    var uini2: ExpandAdapte.UnitData<String, String> =
        ExpandAdapte.UnitData("西校区", listOf("西区第一教学楼", "西区第二教学楼", "西区第三教学楼", "西区第五教学楼", "里仁教学楼"))

    val dirs = mutableListOf(uini1, uini2)

    private val getRoomNetAddtion = MutableLiveData<String>()

    val emptyRoom = ArrayList<EmptyRoom>()

    fun getRoom() {
        getRoomNetAddtion.value = getRoomNetAddtion.value
    }

    //这才是主角
    val emptyRoomLiveData = Transformations.switchMap(getRoomNetAddtion) { query ->
        Repository.getEmptyRoom()
    }

    fun getConditionRoom(condition: String) = Dao.getRoom("${time.value},${condition}")

    fun getBiying(){
        biyingtemp.value=biyingtemp.value
    }
    val urlBiying=Transformations.switchMap(biyingtemp){
        Repository.getBiying()
    }

}
