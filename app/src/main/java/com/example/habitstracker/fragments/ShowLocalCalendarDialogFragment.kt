package com.example.habitstracker.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.adapters.LocalCalendarsAdapter
import com.example.habitstracker.databinding.FragmentShowLocalCalendarDialogBinding
import com.example.habitstracker.models.LocalCalendar
import com.example.habitstracker.viewmodels.ActivityViewModel
import com.example.habitstracker.viewmodels.ShowLocalCalendarViewModel
import com.google.android.material.snackbar.Snackbar


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
        viewModel.habit_id = activityViewModel.getItemId()

        requestPermissionLauncher.launch(Manifest.permission.READ_CALENDAR, )

    }
    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                ifPermissionGranted()
            } else {
                // Объяснить как дать permission через настройки
            }
        }

    fun ifPermissionGranted() {
        initRecyclerView()
        setOnClockListeners()
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
            viewModel.getLocalCalendars(requireContext()) as MutableList<LocalCalendar?>,
            setLocalCalendarId = { id: Int -> viewModel.setCalendarId(id) },
            closeDialog = {findNavController().popBackStack()})
    }


}