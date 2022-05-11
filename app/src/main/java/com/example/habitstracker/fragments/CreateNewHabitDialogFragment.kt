package com.example.habitstracker.fragments

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.habitstracker.databinding.CreateNewHabitDialogFragmentBinding
import com.example.habitstracker.viewmodels.CreateNewHabitDialogViewModel

class CreateNewHabitDialogFragment : DialogFragment() {

    private lateinit var viewModel: CreateNewHabitDialogViewModel
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
        viewModel = ViewModelProvider(this).get(CreateNewHabitDialogViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveButton.setOnClickListener { saveOnClickButton() }
        binding.cancelButton.setOnClickListener { cancelOnClickButton() }
    }
    private fun saveOnClickButton(){
        val name: String? = binding.nameEdittext.text.toString()
        if (name != null){
            viewModel.insertNewHabit(name)
            val toast = Toast.makeText(context, "New habit was created successfully", Toast.LENGTH_LONG)
            toast.show()
            onDestroyView()
        }else{
            val toast = Toast.makeText(context, "The name of habit is empty!", Toast.LENGTH_LONG)
            toast.show()
        }

    }
    private fun cancelOnClickButton(){
        onDestroyView()
    }

}