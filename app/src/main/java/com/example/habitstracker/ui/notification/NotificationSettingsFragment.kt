package com.example.habitstracker.ui.notification

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.habitstracker.EventObserver
import com.example.habitstracker.R
import com.example.habitstracker.data.common.Notificator
import com.example.habitstracker.databinding.NotificationSettingsFragmentBinding
import com.example.habitstracker.ui.TimePickerFragment
import com.example.habitstracker.models.NotificationData
import com.example.habitstracker.ui.ActivityViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class NotificationSettingsFragment : Fragment() {


    private val viewModel: NotificationSettingsViewModel by viewModels()
    private val activityViewModel: ActivityViewModel by activityViewModels()

    private var _binding: NotificationSettingsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NotificationSettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()
        setListeners()
        setInfo()
    }

    private var fragmentContext: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    override fun onDetach() {
        super.onDetach()
        fragmentContext = null
    }

    private fun setInfo() {
        if (activityViewModel.getItemId() != null){
        //проверяем есть ли нотификация у habit,
        //если есть, заполняем поля
            viewModel.getNotificationInfo(activityViewModel.getItemId()!!)?.let { info ->
                binding.checkbox.isChecked = true
                binding.spinner.visibility = View.VISIBLE
                binding.selectTimeLl.visibility = View.VISIBLE
                binding.selectTimeButton.text = LocalTime.ofSecondOfDay(info.time)
                    .format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                return@setInfo
            }
        }
        //если нет, то прячем форму
        binding.checkbox.isChecked = false
        binding.spinner.visibility = View.GONE
        binding.selectTimeLl.visibility = View.GONE

    }
    private fun getItemId(): Long? {
        return activityViewModel.getItemId()
    }

    private fun setNotification(date: LocalDateTime) {
        val id = getItemId()
        val habitEntity = viewModel.getItem(id!!)
        if (habitEntity != null && fragmentContext != null) {
            Notificator(
                fragmentContext!!, NotificationData(
                    id.toInt(),
                    habitEntity.name,
                    "Hey! Have you done your habit?",
                    R.drawable.ic_notify_icon,
                    date,
                    true
                )
            ).createNotification()
        }
    }

    var localDateTime: LocalDateTime = LocalDateTime.now()

    private fun observeDate() {
        //время нотификации
         activityViewModel.selectedTime.observe(requireActivity(), Observer { _time ->
            if(isAdded){
                binding.selectTimeButton.text =
                    _time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                if (_time.isAfter(LocalTime.now())) {
                    localDateTime = _time.atDate(LocalDate.now())
                } else {
                    localDateTime = _time.atDate(LocalDate.now().plusDays(1))
                }
                //обновляем нотификацию
                //обновляем запись в бд
                viewModel.insertOrUpdateNotify(activityViewModel.getItemId()!!, localDateTime.toLocalTime())
                //устанавливаем нотификацию
                setNotification(localDateTime)
            }
        }
        )
        //следим за навигацией
        viewModel.navigateEvent.observe(viewLifecycleOwner, EventObserver { destination_id ->
            findNavController().navigate(destination_id as Int)
        })
    }

    private fun setListeners() {
        //вызываем тайм пикер по кнопке
        binding.selectTimeButton.setOnClickListener {
            val timePickerFragment = TimePickerFragment()
            timePickerFragment.show(parentFragmentManager,"timePicker")
        }
        //чекбокс
        binding.checkbox.setOnCheckedChangeListener { compoundButton, b ->
            //если вкл, показываем форму,
            //ставим обсерверы для данных формы
            //устанавливаем нотификацию
            if (compoundButton.isChecked) {
                binding.spinner.visibility = View.VISIBLE
                binding.selectTimeLl.visibility = View.VISIBLE
                observeDate()
                if (binding.selectTimeButton.text.isNullOrBlank()) {
                    binding.selectTimeButton.text =
                        LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                }
            } else {
            //если выкл
            //прячем форму
            //удаляем нотификацию
                binding.spinner.visibility = View.GONE
                binding.selectTimeLl.visibility = View.GONE
                if (activityViewModel.getItemId() != null && fragmentContext != null){
                    //удаляем из бд
                    viewModel.removeNotificationInfo(activityViewModel.getItemId()!!)
                    //удаляем нотификацию
                    Notificator(fragmentContext!!).removeNotification(
                        activityViewModel.getItemId()!!
                    )
                }
            }
        }
    }

    private fun initSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.notification_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }
    }

}