
package com.example.laravelapi.activity;

import android.content.Intent
import android.os.Bundle;
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.example.laravelapi.R;
import com.example.laravelapi.activity.tambahData.TambahMahasiswaActivity
import com.example.laravelapi.adapter.MahasiswaAdapter
import com.example.laravelapi.config.NetworkConfig
import com.example.laravelapi.databinding.ActivityMainBinding
import com.example.laravelapi.model.DataMahasiswa
import com.example.laravelapi.model.ResponseListMahasiswa
import com.mreza.laravelapi.activity.tambahData.TambahMahasiswaActivity
import com.mreza.laravelapi.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root);
        val btnTambah = binding.btnTambah
        btnTambah.setOnClickListener{
            val tambah = Intent(this, TambahMahasiswaActivity::class.java)
            startActivity(tambah)
        }
        val swipeRefresh = binding.swipeRefreshLayout
        swipeRefresh.setOnRefreshListener(this)

        val appbar =findViewById<androidx.appcompat.widget.Toolbar>(R.id.app_bar_search)
        setSupportActionBar(appbar)
        getMahasiswa()
    }

    private fun getMahasiswa() {
        NetworkConfig().getServices()
            .getMahasiswa()
            .enqueue(object: retrofit2.Callback<ResponseListMahasiswa>{
                @Override
                fun onResponse(
                    call: Call<ResponseListMahasiswa>,
                    response: Response<ResponseListMahasiswa>
                ) {
                    this@MainActivity.binding.progressIndicator.visibility = View.GONE
                    if (response.isSuccessful){
                        val receiveDatas = response.body()?.data
                        setToAdapter(receiveDatas)
                    }
                    binding.swipeRefreshLayout.isRefreshing = false
                }

                @Override
                fun onFailure(call: Call<ResponseListMahasiswa>, t: Throwable) {
                    this@MainActivity.binding.progressIndicator.visibility = View.GONE
                    Log.d("Retrofit onFailure: ", "onFailure: ${t.stackTrace}")

                    binding.swipeRefreshLayout.isRefreshing = false
                }

            } )
    }
    private fun cariMahasiswa(query: String?){
        this.binding.progressIndicator.visibility = View.GONE
        NetworkConfig().getServices()
            .cariMahasiswa(query)
            .enqueue(object :retrofit2.Callback<ResponseListMahasiswa>{
                @Override
                fun onResponse(
                    call: Call<ResponseListMahasiswa>,
                    response: Response<ResponseListMahasiswa>
                ) {
                    this@MainActivity.binding.progressIndicator.visibility = View.GONE
                    if (response.isSuccessful){
                        val receiveDatas = response.body()?.data
                        setToAdapter(receiveDatas)
                    }
                }

                @Override
                fun onFailure(call: Call<ResponseListMahasiswa>, t: Throwable) {
                    this@MainActivity.binding.progressIndicator.visibility = View.GONE
                    Log.d("Retrofit onFailure: ", "onFailure: ${t.stackTrace}")
                }

            })
    }

    @Override
    fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val cariItem = menu?.findItem(R.id.app_bar_search)
        val cariView : SearchView = cariItem?.actionView as SearchView
        cariView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                cariMahasiswa(query)
                return true
            }

            @Override
            fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
        return true
    }

    private fun setToAdapter(receiveDatas: List<DataMahasiswa?>?) {
        val adapter = MahasiswaAdapter(receiveDatas)
        val lm = LinearLayoutManager(this )
        this.binding.rvMahasiswa.layoutManager = lm
        this.binding.rvMahasiswa.itemAnimator = DefaultItemAnimator()
        this.binding.rvMahasiswa.adapter = adapter
    }

    @Override
    fun onRefresh() {
        getMahasiswa()
    }
}
