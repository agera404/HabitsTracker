package com.example.habitstracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.habitstracker.R
import com.example.habitstracker.databinding.CreateNewHabitDialogFragmentBinding
import com.example.habitstracker.viewmodels.ActivityViewModel
import com.example.habitstracker.viewmodels.CreateNewHabitDialogViewModel

class CreateNewHabitDialogFragment : DialogFragment() {

    private val viewModel: CreateNewHabitDialogViewModel by viewModels()
    private val activityViewModel: ActivityViewModel by activityViewModels()
    private var _binding: CreateNewHabitDialogFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CreateNewHabitDialogFragmentBinding.inflate(inflater, container, false)
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
        this.isCancelable = false
        binding.saveButton.setOnClickListener { saveOnClickButton() }
        binding.backButton.setOnClickListener { backOnClickButton() }
        attachNotificationFragment()


    }

    private fun attachNotificationFragment(){
        val notificationFragment = NotificationSettingsFragment()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.notification_fragment_container_2, notificationFragment).commit()
    }
    private fun saveOnClickButton(){
        val name: String? = binding.nameEdittext.text.toString()
        if (name != null){
            viewModel.insertNewHabit(name).observe(viewLifecycleOwner, Observer {
                activityViewModel.setItemId(it)
            })
            val toast = Toast.makeText(context, "The new habit was created successfully", Toast.LENGTH_LONG)
            toast.show()
            backOnClickButton()
        }else{
            val toast = Toast.makeText(context, "A name of a habit is empty!", Toast.LENGTH_LONG)
            toast.show()
        }

    }
    private fun backOnClickButton(){
        findNavController().navigateUp()
    }

}