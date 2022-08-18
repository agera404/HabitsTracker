package com.example.habitstracker.ui.habit

import android.Manifest
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.habitstracker.EventObserver
import com.example.habitstracker.R
import com.example.habitstracker.databinding.HabitFragmentBinding
import com.example.habitstracker.ui.notification.NotificationSettingsFragment
import com.example.habitstracker.ui.HistoryMap
import com.example.habitstracker.ui.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class HabitFragment : Fragment() {

    private val viewModel: HabitViewModel by viewModels()
    private val activityViewModel: ActivityViewModel by activityViewModels()

    private var _binding: HabitFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HabitFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setItem()//передаем id_habit во viewModel и вытаскиваем habit_with_dates для отрисовки
        observeData() // подписываемся для обнавления ui
        setDatesMap() // отрисовываем history map
        setOnClickListeners()
        addTextChangedListeners()
        attachNotificationFragment()


    }



    private fun attachNotificationFragment(){
        val notificationFragment = NotificationSettingsFragment()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.notification_fragment_container, notificationFragment).commit()
    }

    fun TextView.applyWithDisabledTextWatcher(textWatcher: TextWatcher, codeBlock: () -> Unit) {
        this.removeTextChangedListener(textWatcher)
        codeBlock()
        this.addTextChangedListener(textWatcher)
    }

    private val textWatcherForName = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            val name = binding.habitName.getText().toString()
            if (name.length > 0) {
                viewModel.setHabitName(name)
            }
        }
    }


    private fun addTextChangedListeners() {
        binding.habitName.addTextChangedListener(textWatcherForName)
    }


    private fun setItem() {
        val itemId = activityViewModel.getItemId()
        viewModel.setItem(itemId!!)
    }

    private fun setOnClickListeners() {
        binding.editDatesButton.setOnClickListener {
            viewModel.navigateToEditDates()
            activityViewModel.setItemId(viewModel.idHabit!!)
            activityViewModel.setDates(viewModel.listOfDates)
        }
        binding.addToCalendarButton.setOnClickListener { viewModel.navigateToShowLocalCalendars() }
    }

    val requestPermissionLauncher  =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showCalendars()
            } else {
                Toast.makeText(requireContext(),context?.resources?.getString(R.string.no_permission), Toast.LENGTH_SHORT)
            }
        }
    private var showCalendars: ()->Unit = {
        var text: String = ""
        if (viewModel.calendarName != null){
            text = viewModel.calendarName!!
        }else{
            text = context?.resources?.getString(R.string.dont_add_to_calendar_button)!!
        }
        binding.addToCalendarButton.text = text
    }
    private fun observeData() {
        //навигация
        viewModel.navigateEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it as Int)
        })

        viewModel.habitWDate.observe(viewLifecycleOwner, Observer {
            //если habit_with_dates получен из БД
            if (it != null) {
                //чистим history_map
                binding.historyMap.removeAllViews()
                //отрисовываем history_map снова
                setDatesMap()
                //устанавливаем название привычки в editText
                binding.habitName.apply {
                    applyWithDisabledTextWatcher(textWatcherForName) {
                        setText(it.habitName)
                        setSelection(it.habitName.length)
                    }
                }
                //загружаем календарь из БД, если он есть
                requestPermissionLauncher.launch(Manifest.permission.WRITE_CALENDAR)
            }
        })
    }

    private fun setDatesMap() {
        if (viewModel.habitWDate.value != null){
            val historyMap = HistoryMap(requireContext().applicationContext,
            binding.historyMap.width, binding.historyMap.height,
            viewModel.habitWDate.value!!.listOfDates, false, {date: LocalDate ->  viewModel.changeDataStatus(date)})
            binding.historyMap.addView(historyMap)
        }

    }
}