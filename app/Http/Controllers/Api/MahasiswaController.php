<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Resources\MahasiswaResource;
use App\Models\Mahasiswa;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class MahasiswaController extends Controller
{
    //get all mahasiswa
    public function index()
    {
        $mahasiswa = Mahasiswa::all();

        return new MahasiswaResource(true, 'List Data Mahasiswa', $mahasiswa);
    }
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(),[
        'namamahasiswa'           =>'required|string|max:255',
        'nim'                     =>'required|string|max:11|unique:mahasiswas',
        'alamat'                  =>'required|string|max:255',
        'gender'                  =>'required|string|max:255',
        'agama'                   =>'required|string|max:50',
        'usia'                    =>'required|string|max:3',
        'image'                   =>'required|image|mimes:jpeg,png,jpg,gif,svg',
        ],
    [
        'namamahasiswa.required'  => "NAMA HARUS DIISI",
        'nim.required'            => "NIM HARUS DIISI",
        'nim.unique'              => "NIM TIDAK BOLEH SAMA",
        'nim.max'                 => "NIM TIDAK BOLEH LEBIH DARI 11 DIGIT",
        'alamat.required'         => "ALAMAT HARUS DIISI",
        'gender.required'         => "GENDER HARUS DIISI",
        'agama.required'          => "AGAMA HARUS DIISI",
        'usia.required'           => "USIA HARUS DIISI",
        'image.required'          => "IMAGE HARUS DIISI",
        'usia.max'                => "USIA TIDAK LEBIH DARI 3 DIGIT",
    ]);
    
    //upload image
    $image = $request->file('image');
    $image->storeAs('public/mahasiswa', $image->hashName());

    if($validator->fails()){
        $response["ERROR"]   = TRUE;
        $response["SUCCESS"] = 0;
        $response["MESSAGE"] = $validator->errors()->first();
    }
        $mahasiswa = new Mahasiswa;
        $mahasiswa->namamahasiswa = $request->namamahasiswa;
        $mahasiswa->nim           = $request->nim;
        $mahasiswa->alamat        = $request->alamat;
        $mahasiswa->gender        = $request->gender;
        $mahasiswa->agama         = $request->agama;
        $mahasiswa->usia          = $request->usia;
        $mahasiswa->image         = $image->hashName();
        $data  = $mahasiswa->save();
    

    if($data !== false)
    {
      $response["error"]   = FALSE;
      $response["success"] = 1;
      $response["message"] = "DATA BERHASIL DISIMPAN";
    }else
    {
        $response["error"]   = TRUE;
        $response["success"] = 0;
        $response["message"] = "DATA GAGAL DISIMPAN";
    }
    echo json_encode($response);
   }
   public function cari(Request $request)
   {
    $mahasiswa =  Mahasiswa::where('namamahasiswa', 'like', '%'.$request ->cari.'%')->get();

    return new MahasiswaResource(true, 'List Data Mahasiswa', $mahasiswa);
   }
}