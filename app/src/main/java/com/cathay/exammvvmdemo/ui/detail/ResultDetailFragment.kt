package com.cathay.exammvvmdemo.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cathay.exammvvmdemo.R
import com.cathay.exammvvmdemo.base.ToolbarFragment
import com.cathay.exammvvmdemo.databinding.FragmentResultBinding
import com.cathay.exammvvmdemo.databinding.FragmentResultDetailBinding
import com.cathay.exammvvmdemo.ui.home.ExamAdapter
import com.cathay.exammvvmdemo.ui.home.HomeFragment
import com.cathay.exammvvmdemo.ui.result.ResultViewModel
import com.cathay.exammvvmdemo.utils.DataUtil
import org.json.JSONArray

class ResultDetailFragment : ToolbarFragment() {
    private lateinit var viewModel: ResultDetailViewModel
    private lateinit var binding: FragmentResultDetailBinding
    private val checkBoxs = mutableListOf<CheckBox>()
    private var position: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            position = getInt(HomeFragment.POSITION, 0)
        }
    }

    override fun initContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(
                this,
                ResultDetailFactory(requireContext())
            ).get(ResultDetailViewModel::class.java)
        binding = FragmentResultDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun Observe() {
        viewModel.liveExamInfo.observe(viewLifecycleOwner, {
            val options = JSONArray(it.options)
            binding.topic.text = it.topic
            checkBoxs.clear()
            binding.topicContainer.removeAllViews()

            (0 until options.length()).map {
                val view  =  layoutInflater.inflate(R.layout.item_choise,null)
                binding.topicContainer.addView(view)
                val checkbox = view.findViewById<CheckBox>(R.id.check)
                val item = view.findViewById<TextView>(R.id.item)
                checkbox.isEnabled = false
                checkBoxs.add(checkbox)
                item.text = (it+1).toString()+"."
            }

            it.userAns?.apply {
                val array = split(",")
                checkBoxs.mapIndexed { index, checkBox ->
                    checkBox.text = options.getString(index)
                    checkBox.isChecked = false
                }
                if (array.isNotEmpty()) {
                    array.mapIndexed { index, s ->
                        if (s == "1" || s == "2" || s == "3" || s == "4") {
                            checkBoxs[index].isChecked = true
                        }
                    }
                }

                if (DataUtil.sortOutData(it.ans) == DataUtil.sortOutData(it.userAns)){
                    binding.answer.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
                }else{
                    binding.answer.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))
                }
                Log.d("Ryan","XXX = ${getString(R.string.answer,it.ans.toString())}")
                binding.answer.text = getString(R.string.answer,it.ans.toString())

            }
        })

        viewModel.previousStateLive.observe(viewLifecycleOwner, {
            binding.previous.visibility = it
        })

        viewModel.nextStateLive.observe(viewLifecycleOwner, {
            binding.next.visibility = it
        })
    }

    override fun initView() {
        super.initView()
        setToolbarTitle("Result Detail")
        viewModel.requestExam(position)
    }

    override fun listen() {
        super.listen()
        binding.next.setOnClickListener {
            viewModel.requestExam(++position)
        }

        binding.previous.setOnClickListener {
            viewModel.requestExam(--position)
        }
    }
}