package com.example.habitstracker.ui.createnewhabit

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
import com.example.habitstracker.ui.notification.NotificationSettingsFragment
import com.example.habitstracker.ui.ActivityViewModel

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
        val name: String = binding.nameEdittext.text.toString()
        if (name.isNotBlank()){
            viewModel.insertNewHabit(name).observe(viewLifecycleOwner, Observer {
                activityViewModel.setItemId(it)
            })
            backOnClickButton()
        }else{
            binding.nameEdittext.background = context?.resources?.getDrawable(R.drawable.edit_text_red_border)
            binding.nameEdittext.requestFocus()
            val toast = Toast.makeText(context, context?.resources?.getString(R.string.error), Toast.LENGTH_LONG)
            toast.show()
        }

    }
    private fun backOnClickButton(){
        findNavController().navigateUp()
    }

}