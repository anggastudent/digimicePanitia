<?php

namespace App\Http\Controllers;
use Illuminate\Support\Facades\Mail;
use Illuminate\Http\Request;
use App\User;

     
class EmailController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        //
    }

    public function registerPanitia(){

        $name = "Angga";
        $email = "a123aku@gmail.com";
        $data = [
            'name' => $name,
            'body' => "Selamat Akun Anda Aktif"
        ];

        Mail::send('email.sukses', $data, function($message) use ($name, $email){

            $message->to($email, $name)->subject('Pemberitahuan Akun');
            $message->from('admin@apidigimice.me', 'digiMICE Panitia');
        });

        return "berhasil";
    }

    public function validasiEmailAkun($id){

        $user = User::where('verification_token', $id)->where('status','deactivated')->first();

        if($user){

            $user->update(['status' => "activated"]);
        
            $name = $user->name;
            $email = $user->email;
            $body = "Selamat Akun Anda Sudah Aktif. Silahkan Login ke Aplikasi";
            $data = [
                'name' => $name,
                'body' => "Selamat Akun Anda Sudah Aktif. Silahkan Login ke Aplikasi"
            ];

            Mail::send('email.sukses', $data, function($message) use ($name, $email){

                $message->to($email, $name)->subject('Pemberitahuan Akun');
                $message->from('admin@apidigimice.me', 'digiMICE Panitia');
            });

            return view('email.validasi', compact('body'));

        }else{
            
            $body = "Maaf halaman ini sudah kadaluarsa !!";

            return view('email.validasi', compact('body'));
        }
        
    }

    //
}
