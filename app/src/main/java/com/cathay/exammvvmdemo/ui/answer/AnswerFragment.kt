package com.cathay.exammvvmdemo.ui.answer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.cathay.exammvvmdemo.R
import com.cathay.exammvvmdemo.base.ToolbarFragment
import com.cathay.exammvvmdemo.data.ClickType
import com.cathay.exammvvmdemo.databinding.FragmentAnswerBinding
import com.cathay.exammvvmdemo.ui.home.HomeFragment
import com.google.gson.Gson
import org.json.JSONArray
import java.util.zip.Inflater

class AnswerFragment : ToolbarFragment() {

    private lateinit var viewModel: AnswerViewModel
    private var position:Int = 0
    private lateinit var binding: FragmentAnswerBinding
    private val checkBoxs = mutableListOf<CheckBox>()
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
            ViewModelProvider(this, AnswerFactory(requireContext())).get(AnswerViewModel::class.java)
        binding = FragmentAnswerBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun Observe() {
        viewModel.liveExamInfo.observe(viewLifecycleOwner, {
            binding.topic.text = it.topic
            it.userAns?.apply {

                val array = split(",")

                val options = JSONArray(it.options)
                checkBoxs.mapIndexed { index, checkBox ->
                    checkBox.text = options.getString(index)
                    checkBox.isChecked = false
                }
                if (array.isNotEmpty()){
                    array.mapIndexed { index, s ->
                        if (s == "1" || s == "2" || s == "3" || s == "4"){
                            checkBoxs[index].isChecked = true
                        }
                    }
                }
            }
        })

        viewModel.checkListLive.observe(viewLifecycleOwner,{

        })

        viewModel.previousStateLive.observe(viewLifecycleOwner,{
            binding.previous.visibility = it
        })

        viewModel.nextStateLive.observe(viewLifecycleOwner,{
            binding.next.visibility = it
        })

        viewModel.saveLive.observe(viewLifecycleOwner,{

            when(it){
                ClickType.MINUS->{
                    viewModel.requestExam(--position)
                }
                ClickType.PLUS->{
                    viewModel.requestExam(++position)
                }
                ClickType.BACK->{
                    nav().navigateUp()
                }
            }
        })
    }

    override fun initView() {
        super.initView()
        setToolbarTitle("Answer")
        viewModel.requestExam(position)

        (0..3).map {
            val view  =  layoutInflater.inflate(R.layout.item_choise,null)
            binding.topicContainer.addView(view)
            val checkbox = view.findViewById<CheckBox>(R.id.check)
            val item = view.findViewById<TextView>(R.id.item)
            checkBoxs.add(checkbox)
            item.text = (it+1).toString()+"."
        }
        val data = mutableListOf<Boolean>()
        (0..3).map {
            data.add(false)
        }
        viewModel.initCheckList(data)

        binding.previous.visibility = if (position == 0) View.GONE else View.VISIBLE
        binding.next.visibility = if (position == 0) View.GONE else View.VISIBLE


    }

    override fun listen() {
        super.listen()
        checkBoxs.mapIndexed { index, checkBox ->
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                viewModel.updateCheckState(index)
            }
        }

        binding.next.setOnClickListener {
            viewModel.requestSaveAnswer(ClickType.PLUS)
        }

        binding.previous.setOnClickListener {
            viewModel.requestSaveAnswer(ClickType.MINUS)
        }

        setBackPress {
            viewModel.requestSaveAnswer(ClickType.BACK)
        }

        setNavigationHomeClickListen {
            viewModel.requestSaveAnswer(ClickType.BACK)
        }

    }
}