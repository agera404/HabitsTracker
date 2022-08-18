package com.example.habitstracker.ui.showlocalcalendars

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.ui.adapters.LocalCalendarsAdapter
import com.example.habitstracker.databinding.FragmentShowLocalCalendarDialogBinding
import com.example.habitstracker.models.LocalCalendar
import com.example.habitstracker.ui.ActivityViewModel


class ShowLocalCalendarDialogFragment : DialogFragment() {

    private var recyclerView: RecyclerView? = null

    private var _binding: FragmentShowLocalCalendarDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShowLocalCalendarViewModel by viewModels()
    private val activityViewModel: ActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowLocalCalendarDialogBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.isCancelable = false
        viewModel.idHabit = activityViewModel.getItemId()
        //Запрашиваем права для чтения календаря
        //если права есть, отрисовываем календари
        requestPermissionLauncher.launch(Manifest.permission.READ_CALENDAR )
        setOnClockListeners()
    }
    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                ifPermissionGranted()
            } else {
                //findNavController().popBackStack()
            }
        }

    fun ifPermissionGranted() {
        //отрисовываем календари
        initRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClockListeners(){
        binding.notAddButton.setOnClickListener {
            findNavController().popBackStack()
            viewModel.setCalendarId(null)
        }
    }

    private fun initRecyclerView(){
        recyclerView = binding.recyclerView
        recyclerView?.adapter = LocalCalendarsAdapter(
            //получаем все доступные календари
            viewModel.getLocalCalendars(requireContext()) as MutableList<LocalCalendar?>,
            //setLocalCalendarId нужен для обработки нажатия по календарю
            //если кликнули по календарю,
            //отдаем id календаря во viewModel,
            //где viewModel создает новую связь habit - calendar в бд
            setLocalCalendarId = { id: Int -> viewModel.setCalendarId(id) },
            closeDialog = {findNavController().popBackStack()})
    }


}