package com.example.ysuselfstudy.adapter

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ysuselfstudy.YsuSelfStudyApplication
import com.example.ysuselfstudy.data.Course
import com.example.ysuselfstudy.databinding.CourseItemLayoutBinding
import com.example.ysuselfstudy.logic.getPixelsFromDp
import com.example.ysuselfstudy.ui.classschedule.CourseDetailDialogFragment


/**
 * @author  Ahyer
 * @date  2020/4/13 18:26
 * @version 1.0
 */
class CourseAdapter(val mData: List<Course>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "CourseAdapter"
    private lateinit var listener: OnclickListener

    inner class CourseViewHolder(var binding: CourseItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var binding =
            CourseItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val px = getPixelsFromDp(viewType * 60)
        binding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            px
        )
        val courseViewHolder = CourseViewHolder(binding)
        return courseViewHolder
    }

    override fun getItemViewType(position: Int) = mData[position].continued


    override fun getItemCount() = mData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as CourseViewHolder



        if (!mData[position].courseName.equals("")) {
            holder.binding.courseText.setBackgroundColor(mData[position].color)
            holder.binding.courseText.background.alpha = 200
            holder.itemView.setOnClickListener {
                listener.OnItemClickListener(mData[position])
            }
        }
        holder.binding.courseText.setText(mData[position].courseName + "\n" + mData[position].position)
    }

    interface OnclickListener {
        fun OnItemClickListener(course:Course)
    }

    fun setOnClickListener(listener: OnclickListener) {
        this.listener = listener
    }


}