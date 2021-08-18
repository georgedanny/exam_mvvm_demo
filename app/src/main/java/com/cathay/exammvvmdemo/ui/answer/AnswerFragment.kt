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
    private lateinit var binding: FragmentAnswerBinding
    private var position:Int = 0
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
        //init viewMode factory
        viewModel =
            ViewModelProvider(this, AnswerFactory(requireContext())).get(AnswerViewModel::class.java)

        //init data binding
        binding = FragmentAnswerBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun Observe() {
        //observe exam data
        viewModel.liveExamInfo.observe(viewLifecycleOwner, {
            val options = JSONArray(it.options)
            binding.topic.text = it.topic
            checkBoxs.clear()
            binding.topicContainer.removeAllViews()
            val data = mutableListOf<Boolean>()
            (0 until options.length()).map {
                val view  =  layoutInflater.inflate(R.layout.item_choise,null)
                binding.topicContainer.addView(view)
                val checkbox = view.findViewById<CheckBox>(R.id.check)
                val item = view.findViewById<TextView>(R.id.item)
                checkBoxs.add(checkbox)
                item.text = (it+1).toString()+"."
                data.add(it,false)
            }

            viewModel.initCheckList(data)
            setCheckBoxListen()

            it.userAns?.apply {
                val array = split(",")
                checkBoxs.mapIndexed { index, checkBox ->
                    checkBox.text = options.getString(index)
                }

                if (array.isNotEmpty()){
                    array.mapIndexed { index, s ->
                        Log.d("Ryan","s = $s")
                        if (s == "1" || s == "2" || s == "3" || s == "4"){
                            checkBoxs[index].isChecked = true
                        }
                    }
                }
            }
        })

        //observe previous button state
        viewModel.previousStateLive.observe(viewLifecycleOwner,{
            binding.previous.visibility = it
        })

        //observe next button state
        viewModel.nextStateLive.observe(viewLifecycleOwner,{
            binding.next.visibility = it
        })

        //observe save answer finish
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

        binding.previous.visibility = if (position == 0) View.GONE else View.VISIBLE
        binding.next.visibility = if (position == 0) View.GONE else View.VISIBLE


    }

    private fun setCheckBoxListen(){
        checkBoxs.mapIndexed { index, checkBox ->
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                viewModel.updateCheckState(index)
            }
        }
    }

    override fun listen() {
        super.listen()
        setCheckBoxListen()

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