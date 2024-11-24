package com.example.assignment.ui.invest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.assignment.R
import com.example.assignment.databinding.FragmentInvestBinding

class InvestFragment : Fragment(R.layout.fragment_invest) {

    private var _binding: FragmentInvestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvestBinding.inflate(inflater, container, false)
        return binding.root
    }

}