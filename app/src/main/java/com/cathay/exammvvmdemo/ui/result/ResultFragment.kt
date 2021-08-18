package com.cathay.exammvvmdemo.ui.result

import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.cathay.exammvvmdemo.R
import com.cathay.exammvvmdemo.base.BaseFragmnet
import com.cathay.exammvvmdemo.base.ToolbarFragment
import com.cathay.exammvvmdemo.data.database.AppDataBase
import com.cathay.exammvvmdemo.data.entity.ExamEntity
import com.cathay.exammvvmdemo.databinding.FragmentResultBinding
import com.cathay.exammvvmdemo.ui.home.ExamAdapter
import com.cathay.exammvvmdemo.ui.home.HomeFragment
import com.cathay.exammvvmdemo.utils.DataUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResultFragment : ToolbarFragment() {

    private lateinit var viewModel: ResultViewModel
    private var examAdapter: ExamAdapter? = null
    private lateinit var binding: FragmentResultBinding
    private var data = mutableListOf<ExamEntity>()

    override fun initContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(
                this,
                ResultFactory(requireContext())
            ).get(ResultViewModel::class.java)
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun Observe() {
        //observe exam data
        viewModel.liveExamInfo.observe(viewLifecycleOwner, {

            examAdapter?.setData(it as MutableList<ExamEntity>, true)

            var score = 0
            it.map { exam ->
                if (DataUtil.sortOutData(exam.userAns) == DataUtil.sortOutData(exam.ans)) {
                    score += 10
                }
            }
            binding.score.text = getString(R.string.score, score)
        })

        //observe Reset DB finish
        viewModel.liveResetTable.observe(viewLifecycleOwner, {
            nav().navigateUp()
        })
    }

    override fun initView() {
        super.initView()
        setToolbarTitle("Result")
        examAdapter = ExamAdapter(data)
        binding.recycleView.adapter = examAdapter
        viewModel.requestExams()
    }

    override fun listen() {
        super.listen()
        examAdapter?.setOnItemClickListener { i, view, any ->
            val bundle = Bundle()
            bundle.putInt(HomeFragment.POSITION, i)
            nav().navigate(R.id.action_navigation_result_to_navigation_result_detail, bundle)
        }

        setNavigationHomeClickListen {
            showAlert()
        }

        setBackPress {
            showAlert()
        }
    }

    private fun showAlert() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.msg))
            .setTitle(getString(R.string.title))
            .setNegativeButton(
                R.string.cancel
            ) { dialog, which ->

            }
            .setPositiveButton(R.string.confirm) { dialog, which ->
                viewModel.requestReset()
            }
            .show()
    }
}