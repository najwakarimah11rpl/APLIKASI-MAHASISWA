package com.mreza.laravelapi.activity.tambahData;
import android.os.Bundle;
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.laravelapi.R;
import com.example.laravelapi.config.NetworkConfig
import com.example.laravelapi.databinding.ActivityMainBinding
import com.example.laravelapi.databinding.ActivityTambahMahasiswaBinding
import com.example.laravelapi.model.SubmitMahasiswa
import com.mreza.laravelapi.databinding.ActivityTambahMahasiswaBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TambahMahasiswaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTambahMahasiswaBinding

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root);

        val items = listOf("Islam maasyaallah", "Kristen", "Hindu", "Buddha", "Konghucu")
        val adapter = ArrayAdapter(this, R.layout.list_agama, items)
        binding.dropdownAgama.setAdapter(adapter)
        binding.saveButton.setOnClickListener{
            saveData()
        }

    }

    private fun saveData() {
        val agama =binding.dropdownAgama.text.toString()
        val rbLaki =binding.rbLaki
        val rbPerempuan =binding.rbPerempuan

        val gender = if(rbLaki.isChecked){
            rbLaki.text.toString()
        }else{
            rbPerempuan.text.toString()
        }
        val namalengkap =binding.editTextName.text.toString()
        val nim =binding.etNim.text.toString()
        val alamat =binding.etAlamat.text.toString()
        val usia =binding.etUsia.text.toString()

        val retrofit = NetworkConfig().getServices()
        if(!(namalengkap.isEmpty() && nim.isEmpty() && alamat.isEmpty() && gender.isEmpty() && agama.isEmpty() && usia.isEmpty())){
            retrofit.postMahasiswa(namalengkap, nim, alamat, gender, agama, usia)
                .enqueue(object:Callback<SubmitMahasiswa>{
                    @Override
                    fun onResponse(
                        call: Call<SubmitMahasiswa>,
                        response: Response<SubmitMahasiswa>
                    ) {
                        if (response.isSuccessful){
                            val hasil = response.body()
                            Toast.makeText(applicationContext, hasil!!.message, Toast.LENGTH_SHORT).show()
                            namalengkap.isEmpty()
                            alamat.isEmpty()
                            gender.isEmpty()
                            agama.isEmpty()
                            usia.isEmpty()
                        }

                    }

                    @Override
                    fun onFailure(call: Call<SubmitMahasiswa>, t: Throwable) {
                        Toast.makeText(applicationContext, "GAGAL MENYIMPAN: ${t.message}",
                            Toast.LENGTH_SHORT).show()

                    }

                })

        }else{
            Toast.makeText(applicationContext, "TIDAK BOLEH KOSONG",
                Toast.LENGTH_SHORT).show()
        }



    }
}
