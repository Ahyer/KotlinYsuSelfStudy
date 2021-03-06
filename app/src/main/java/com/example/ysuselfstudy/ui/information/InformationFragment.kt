package com.example.ysuselfstudy.ui.information

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.ysuselfstudy.R
import com.example.ysuselfstudy.YsuSelfStudyApplication
import com.example.ysuselfstudy.adapter.InformationAdapter
import com.example.ysuselfstudy.data.Information
import com.example.ysuselfstudy.databinding.InformFragmentBinding
import com.example.ysuselfstudy.logic.showToast

class InformationFragment : Fragment() {
    private val TAG = "InformationFragment"
    protected var isFirst = true

    companion object {
        fun newInstance() = InformationFragment()
    }

    private lateinit var viewModel: InformationViewModel
    private var mData = ArrayList<Information>()
    private lateinit var adapter: InformationAdapter
    private lateinit var binding: InformFragmentBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.inform_fragment, container, false)
        navController = findNavController()
        adapter = InformationAdapter(mData, navController)
        binding.infoRecy.adapter = adapter
        binding.infoRecy.addItemDecoration(
            DividerItemDecoration(
                YsuSelfStudyApplication.context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.infoRecy.layoutManager = LinearLayoutManager(YsuSelfStudyApplication.context)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(InformationViewModel::class.java)
        viewModel.getInforList()
        viewModel.listOfInformation.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                "通知公告出问题了。。".showToast()
            } else {
                mData.clear()
                mData.addAll(it)
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.myListInfor.observe(viewLifecycleOwner, Observer {
            if (it != null && isFirst) {
                isFirst = false
                YsuSelfStudyApplication.myinform.addAll(it)
            }

        })

    }

}
