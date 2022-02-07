package com.example.colorimagesearch.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.colorimagesearch.R
import com.example.colorimagesearch.adapter.ColorsListAdapter
import com.example.colorimagesearch.database.ContactDb
import com.example.colorimagesearch.database.FavoriteDao
import com.example.colorimagesearch.databinding.ActivityMainBinding
import com.example.colorimagesearch.viewmodels.ColorsListViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var mAdapter: ColorsListAdapter
    private lateinit var mViewModel: ColorsListViewModel
    private lateinit var mActivityBinding: ActivityMainBinding


    companion object {
        private const val CURRENT_LIST_SIZE = "CURRENT_LIST_SIZE"
        var daoContact: FavoriteDao? = null
        var serach_keyword = ""
        var mContext: Context? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        mContext = this@MainActivity
        mActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        var db: ContactDb = ContactDb.getDataBase(this@MainActivity)

        daoContact = db.daoContact()
        mViewModel = ViewModelProviders.of(this@MainActivity).get(ColorsListViewModel::class.java)

        mActivityBinding.viewModel = mViewModel
        mActivityBinding.lifecycleOwner = this

        mActivityBinding.button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                var searchKeyword = mActivityBinding.myEditText!!.text.toString()
                if (searchKeyword != "") {
                    if (searchKeyword!!.length >= 3 && searchKeyword!!.length <= 10) {
                        serach_keyword = searchKeyword
                        initializeRecyclerView()
                        initializeObservers()
                        mViewModel.fetchColorsFromServer(true)
                            .observe(this@MainActivity, Observer { kt ->
                                mAdapter.setData(kt)
                            })
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Please Enter at least 3 to 10 Characters",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Please Enter Image Search Text",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })


    }

    private fun initializeRecyclerView() {
        mAdapter = ColorsListAdapter()
        mActivityBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            adapter = mAdapter
        }

    }

    private fun initializeObservers() {

        mViewModel.fetchColorsFromServer(false).observe(this, Observer { kt ->
            mAdapter.notifyDataSetChanged()
            mAdapter.setData(kt)
        })
        mViewModel.mShowApiError.observe(this, Observer {
            AlertDialog.Builder(this).setMessage(it).show()
        })
        mViewModel.mShowProgressBar.observe(this, Observer { bt ->
            if (bt) {
                mActivityBinding.progressBar.visibility = View.VISIBLE
                mActivityBinding.recyclerView.visibility = View.GONE
                mActivityBinding.textViewEmpty.visibility = View.VISIBLE
            } else {
                mActivityBinding.progressBar.visibility = View.GONE
                mActivityBinding.recyclerView.visibility = View.VISIBLE
                mActivityBinding.textViewEmpty.visibility = View.GONE
            }
        })
        mViewModel.mShowNetworkError.observe(this, Observer {
            AlertDialog.Builder(this).setMessage(R.string.app_no_internet_msg).show()
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CURRENT_LIST_SIZE, mAdapter!!.itemCount)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}