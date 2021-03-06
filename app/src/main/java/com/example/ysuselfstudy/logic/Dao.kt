package com.example.ysuselfstudy.logic

import android.os.Build
import android.util.Log
import com.example.ysuselfstudy.YsuSelfStudyApplication
import com.example.ysuselfstudy.data.*
import org.litepal.LitePal
import org.litepal.LitePal.findBySQL

/**
 * @author  Ahyer
 * @date  2020/4/13 15:05
 * @version 1.0
 */
object Dao {
    private val TAG = "Dao"

    /**
     * 保存学生的个人信息
     */
    fun saveStu(num: String, password: String, gbkName: String) {
        val user = User(number = num, eduPassword = password, gbkName = gbkName)
        if (!isStuEmpty()) {
            //如果不相等
            val savedStu = getStu()
            if (savedStu.number == num) {
                LitePal.deleteAll(User::class.java)
            } else {
                //相等,更新
                savedStu.eduPassword = password
                savedStu.number = num
                savedStu.gbkName = gbkName
                savedStu.save()
                return
            }
        }
        user.save()
    }

    /**
     * 获取唯一的用户
     */
    fun getStu(): User = LitePal.findFirst(User::class.java)


    /**
     *返回用户是否为空
     */
    fun isStuEmpty() = LitePal.count(User::class.java) == 0


    /**
     * 返回必应首页
     */
    fun getBiying(): String = LitePal.findFirst(BiyingPic::class.java).url


    /**
     * 删除首页图片
     */
    fun deletePic() = LitePal.deleteAll(BiyingPic::class.java)

    /**
     * 删除当天所有的空教室
     */
    fun deleteRoom() = LitePal.deleteAll(EmptyRoom::class.java)


    /**
     * 获取教室
     */
    fun saveRoom(list: List<EmptyRoom>) = LitePal.saveAll(list)


    /**
     * 获取符合条件的教室
     */
    fun getRoom(conditon: String): ArrayList<EmptyRoom> {
        val split = conditon.split(",")
        //前面为时间，后面为地址
        var time = RoomAnalysis.obj(split[0])//切割时间，找的是起始和终点

        //SQL语句的动态显式。
        var sql =
            " select room,nums,location from EmptyRoom where location = '${split[1].trim()}' and time=${time[0]} "

        var i: Int = time[0] + 2
        while (i <= time[1]) {
            sql += "intersect select room,nums,location from EmptyRoom where location = '${split[1].trim()}' and time=${i} "
            i += 2
        }

        val cc = findBySQL(sql)
        var roomList = ArrayList<EmptyRoom>()
        if (cc.moveToFirst()) {
            do {
                val name = cc.getString(cc.getColumnIndex("room"))
                val numbers = cc.getString(cc.getColumnIndex("nums"))
                val location = cc.getString(cc.getColumnIndex("location"))
                val ee = EmptyRoom(room = name, nums = numbers, location = location)
                roomList.add(ee)
            } while (cc.moveToNext())
        }
        cc.close()
        return roomList
    }

    /**
     * 保存本学期所有的课程
     */
    fun saveAllCourse(list: List<Course>) = LitePal.saveAll(list)


    /**
     * 删除本学期所有的课
     */
    fun deleteAllCourse() = LitePal.deleteAll(Course::class.java)


    /**
     * 查看课程表的数据库是否为空
     */
    fun isCourseEmpty(): Boolean = LitePal.count(Course::class.java) == 0

    fun isRoomEmpty(): Boolean = LitePal.count(EmptyRoom::class.java) == 0

    fun isBiying(): Boolean = LitePal.count(BiyingPic::class.java) == 0


    fun keepQQLogin(): Boolean = if (LitePal.count(QQ::class.java) != 0) {
        val qqUser = getQQ()
        YsuSelfStudyApplication.tencent.setAccessToken(qqUser.accessToken, qqUser.expires)
        YsuSelfStudyApplication.tencent.openId = qqUser.openID
        true
    } else false


    fun getQQ() = LitePal.findFirst(QQ::class.java)


    fun deleteQQ() =
        LitePal.deleteAll(QQ::class.java)


    /**
     * 返回本周的课程
     */
    fun getWeekClass(week: Int = -1): ArrayList<Course> {
        val list = ArrayList<Course>()//声明一个样表
        repeat(84) { list.add(Course()) }
        //这一步有问题，不会是这样的。
        val nowWeek = if (week == -1) getWeek() else week

        val weekList = ArrayList<Course>()
        var findAll = LitePal.findAll(Course::class.java)
        //如果不是本周的，就取消
        for (course in findAll) {
            if (nowWeek < course.startWeek || nowWeek > course.endWeek) {
                continue
            }
            if (course.isSingleDoubel) {
                if (nowWeek % 2 == 0 && course.isOdd || nowWeek % 2 == 1 && !course.isOdd) {
                    continue
                }
            }
            weekList.add(course)
        }


        //在样表中分离各个课程
        for (course in weekList) {
            var temp = 1
            while (temp <= course.continued) {
                list[(course.beginNode - 1) * 7 + course.dayOfWeek - 1 + (temp - 1) * 7] =
                    course.clone()
                temp++
            }
        }

        //合并课程，设置删除标记位
        for (i in 83 downTo 0) {
            if (list[i].dayOfWeek != 0) {
                if (i - 7 >= 0) {
                    if (list[i].equals(list[i - 7])) {
                        list[i].delete = true
                        list[i - 7].continued += list[i].continued
                    }
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.removeIf { t: Course -> t.delete == true }
            return list
        } else {
            var result = ArrayList<Course>()
            for (course in list) {
                if (!course.delete) {
                    result.add(course)
                }
            }
            return result
        }


    }
}