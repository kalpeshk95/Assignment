package com.example.assignment.ui.funds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.assignment.R
import com.example.assignment.databinding.FragmentFundsBinding

class FundsFragment : Fragment(R.layout.fragment_funds) {

    private var _binding: FragmentFundsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFundsBinding.inflate(inflater, container, false)
        return binding.root
    }

}