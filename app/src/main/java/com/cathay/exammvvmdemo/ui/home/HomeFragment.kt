package com.cathay.exammvvmdemo.ui.home

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cathay.exammvvmdemo.R
import com.cathay.exammvvmdemo.base.BaseFragmnet
import com.cathay.exammvvmdemo.base.ToolbarFragment
import com.cathay.exammvvmdemo.data.database.AppDataBase
import com.cathay.exammvvmdemo.data.database.RoomDao
import com.cathay.exammvvmdemo.data.entity.ExamEntity
import com.cathay.exammvvmdemo.data.repo.ExamRepo
import com.cathay.exammvvmdemo.databinding.FragmentHomeBinding
import com.cathay.exammvvmdemo.utils.DataUtil
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.ResourceBundle.getBundle
import kotlin.concurrent.thread

class HomeFragment : ToolbarFragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private var data = mutableListOf<ExamEntity>()
    private var examAdapter: ExamAdapter? = null

    companion object {
        const val POSITION = "POSITION"
    }

    override fun initContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this, HomeFactory(requireContext())).get(HomeViewModel::class.java)

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun Observe() {
        //observe exam data
        homeViewModel.liveExamInfo.observe(viewLifecycleOwner, {
            examAdapter?.setData(it as MutableList<ExamEntity>)
        })
    }

    override fun initView() {
        super.initView()
        setToolbarTitle("Home")
        hideBackArrow()
        examAdapter = ExamAdapter(data)
        binding.recycleView.adapter = examAdapter

    }

    override fun listen() {
        super.listen()
        examAdapter?.setOnItemClickListener { i, view, any ->
            val bundle = Bundle()
            bundle.putInt(POSITION, i)
            nav().navigate(R.id.action_navigation_home_to_navigation_answer, bundle)

        }
        binding.submit.setOnClickListener {
            nav().navigate(R.id.action_navigation_home_to_navigation_result)
        }

        setBackPress { requireActivity().finish() }

    }


    override fun onResume() {
        super.onResume()
        homeViewModel.requestExams()
    }


}