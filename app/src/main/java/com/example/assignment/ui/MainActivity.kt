package com.example.assignment.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.assignment.R
import com.example.assignment.databinding.ActivityMainBinding
import com.example.assignment.ui.funds.FundsFragment
import com.example.assignment.ui.invest.InvestFragment
import com.example.assignment.ui.order.OrderFragment
import com.example.assignment.ui.portfolio.PortfolioFragment
import com.example.assignment.ui.watchlist.WatchlistFragment

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val fragments = mapOf(
        R.id.nav_watchlist to Pair(WatchlistFragment(), R.string.string_watchlist),
        R.id.nav_orders to Pair(OrderFragment(), R.string.string_orders),
        R.id.nav_portfolio to Pair(PortfolioFragment(), R.string.string_portfolio),
        R.id.nav_funds to Pair(FundsFragment(), R.string.string_funds),
        R.id.nav_invest to Pair(InvestFragment(), R.string.string_invest)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(binding.root)

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