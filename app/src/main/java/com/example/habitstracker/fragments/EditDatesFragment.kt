package com.example.habitstracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.habitstracker.EventObserver
import com.example.habitstracker.databinding.EditDatesDialogFragmentBinding
import com.example.habitstracker.ui.HistoryMap
import com.example.habitstracker.viewmodels.ActivityViewModel
import com.example.habitstracker.viewmodels.EditDatesViewModel
import java.time.LocalDate


class EditDatesFragment : Fragment() {

    private var _binding: EditDatesDialogFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditDatesViewModel by viewModels()
    private  val activityViewModel: ActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EditDatesDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeDate()
    }
    private fun createMap(){
        if (viewModel.dataSet != null){
            val historyMap = HistoryMap(requireContext().applicationContext,
                0, binding.container.height,
                viewModel.dataSet!!, true,
                { date: LocalDate -> viewModel.changeDataStatus(date) })
            binding.container.addView(historyMap, historyMap.getXstart().toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }


    var isFirstTime: Boolean = true

    private fun observeDate(){
        activityViewModel.itemId.observe(requireActivity(), Observer {
            if (isAdded){
                if (activityViewModel.getDates()!=null){
                    viewModel.init(
                        it,
                        requireContext().applicationContext,
                    )
                    //createMap()
                }
            }
        })
        viewModel.habitWDate.observe(viewLifecycleOwner, Observer{
            if (isAdded){
                if (it != null) {
                    binding.container.removeAllViews()
                    createMap()
                    if (isFirstTime){
                        binding.scrollView.postDelayed(
                            Runnable { binding.scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT) },
                            100L
                        )
                        isFirstTime = false
                    }
                }
            }
        })
        viewModel.navigateEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it as Int)
        })
    }
}