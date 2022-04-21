package com.example.habitstracker.fragments

import android.database.ContentObserver
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.R
import com.example.habitstracker.adapters.HabitsAdapter
import com.example.habitstracker.databinding.MainFragmentBinding
import com.example.habitstracker.models.HabitWDate
import com.example.habitstracker.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.main_fragment.*


class MainFragment : Fragment() {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private var habitsRecyclerView: RecyclerView? = null
    var hadapter: HabitsAdapter? = null

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        initRecyclerView()
        observeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun observeData(){
        viewModel.habitsList.observeForever {
            Log.d("dLog", "size list in observer: " + it.size)
            hadapter = HabitsAdapter(it, onClick = { onClick() })
            habitsRecyclerView?.adapter = hadapter
        }
    }
    fun onClick(){
        findNavController().navigate(R.id.blankFragment)
    }

    private fun initRecyclerView() {
        habitsRecyclerView = binding.recyclerView
        habitsRecyclerView!!.layoutManager = LinearLayoutManager(context)
        Log.d("dLog", "initRecyclerView ")
    }
}