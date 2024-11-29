package com.example.assignment.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.assignment.R
import com.example.assignment.databinding.ActivityMainBinding
import com.example.assignment.ui.empty.EmptyFragment
import com.example.assignment.ui.portfolio.PortfolioFragment

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val fragments = mapOf(
        R.id.nav_watchlist to Pair(EmptyFragment(), R.string.string_watchlist),
        R.id.nav_orders to Pair(EmptyFragment(), R.string.string_orders),
        R.id.nav_portfolio to Pair(PortfolioFragment(), R.string.string_portfolio),
        R.id.nav_funds to Pair(EmptyFragment(), R.string.string_funds),
        R.id.nav_invest to Pair(EmptyFragment(), R.string.string_invest)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            handleNavigation(item.itemId)
            true
        }
        binding.bottomNavigation.selectedItemId = R.id.nav_portfolio
    }

    private fun handleNavigation(itemId: Int) {
        val (fragment, titleRes) = fragments[itemId] ?: fragments[R.id.nav_portfolio]!!
        val title = getString(titleRes)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment?.javaClass != fragment.javaClass) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }

        binding.lblTitle.text = title
    }
}