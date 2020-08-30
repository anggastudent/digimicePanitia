<?php

namespace App\Http\Controllers;
use App\User;
use App\Team;
use Illuminate\Support\Facades\Hash;
use Illuminate\Http\Request;
use App\Provinsi;
use Illuminate\Support\Facades\Mail;


class AuthController extends Controller
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
  
    //Fungsi Login Panitia
    public function login(Request $request){
        
        $this->validate($request,[
            'email' => 'required',
            'password' => 'required'
        ]);

        $email = $request->input('email');
        $password = $request->input('password');

        if($user = User::where('email',$email)->where('role','lead eo')->where('status','activated')->first()){
            
            if(Hash::check($password,$user->password_hash)){

                $newToken = $this->getRandomString();

                $user->update([
                    'auth_key' => $newToken
                ]);

                $pesan = [
                    'message' => 'login success',
                    'code' => 200,
                    'result' => [
                        'token' => $newToken,
                        'role' => $user->role,
                        'user_id' => $user->id,
                        'email' => $user->email,
                        'name' => $user->name
                    ]
                ];

            }else{

                $pesan = [
                    'message' => 'login failed wrong password',
                    'code' => 401,
                    'result' => [
                        'token' => null,
                    ]
                ];

            }

            return response()->json($pesan,$pesan['code']);
            
        }
        
        if($user = User::where('email',$email)->where('role','eo')->where('status','activated')->first()){
            
            if(Hash::check($password,$user->password_hash)){

            $newToken = $this->getRandomString();

            $user->update([
                'auth_key' => $newToken
            ]);

            $pesan = [
                'message' => 'login success',
                'code' => 200,
                'result' => [
                    'token' => $newToken,
                    'role' => $user->role,
                    'user_id' => $user->id,
                    'email' => $user->email,
                    'name' => $user->name
                ]
            ];

        }else{

            $pesan = [
                'message' => 'login failed wrong password',
                'code' => 401,
                'result' => [
                    'token' => null,
                ]
            ];

        }

        return response()->json($pesan,$pesan['code']);
        }

        $pesan = [
                'message' => 'login panitia gagal',
                'code' => 401,
                'result' => [
                    'token' => 'null',
                ]
            ];
        return response()->json($pesan,$pesan['code']);
        
            
    }

    public function loginParticipant(Request $request){
        
        $this->validate($request,[
            'email' => 'required',
            'password' => 'required'
        ]);

        $email = $request->input('email');
        $password = $request->input('password');

        $user = User::where('email',$email)->where('role','participant')->where('status','activated')->first();
        if($user){
            if(!$user){

                $pesan = [
                    'message' => 'login peserta gagal',
                    'code' => 401,
                    'result' => [
                        'token' => 'null'
                    ]
                ];
                return response()->json($pesan,$pesan['code']);
            }

            if(Hash::check($password, $user->password_hash)){

                $newToken = $this->getRandomString();
                $user->update(['auth_key' => $newToken]);

                $result = [

                    'result' => [

                        'token' => $newToken,
                        'user_id' => $user->id,
                        'email' => $user->email,
                        'name' => $user->name,
                        'role' => $user->role,
                        'phone' => $user->phone,
                        'avatar' => $user->avatar,
                        'regencies_id' => $user->regencies_id
                    ]
                   
                ];

                return response()->json($result);
            }else{
                $pesan = [
                    'message' => 'Password tidak valid',
                    'code' => 401,
                    'result' => [
                        'token' => 'null'
                    ]
                ];
                return response()->json($pesan,$pesan['code']);
            }
        }

        $user_pemateri = User::where('email',$email)->where('role','speaker')->where('status','activated')->first();
        if($user_pemateri){


            if(!$user_pemateri){

                $pesan = [
                    'message' => 'login pemateri gagal',
                    'code' => 401,
                    'result' => [
                        'token' => 'null'
                    ]
                ];
                return response()->json($pesan,$pesan['code']);
            }

            if(Hash::check($password, $user_pemateri->password_hash)){

                $newToken = $this->getRandomString();
                $user_pemateri->update(['auth_key' => $newToken]);

                $result = [

                    'result' => [

                        'token' => $newToken,
                        'user_id' => $user_pemateri->id,
                        'email' => $user_pemateri->email,
                        'name' => $user_pemateri->name,
                        'role' => $user_pemateri->role,
                        'phone' => $user_pemateri->phone,
                        'avatar' => $user_pemateri->avatar,
                        'regencies_id' => $user_pemateri->regencies_id
                    ]
                    
                ];

                return response()->json($result);
            }else{
                $pesan = [
                    'message' => 'Password tidak valid',
                    'code' => 401,
                    'result' => [
                        'token' => 'null'
                    ]
                ];
                return response()->json($pesan,$pesan['code']);
            }
        }

        
    }

    public function provinsi(){
        $provinsi = Provinsi::all();
        $array = [];
        foreach ($provinsi as $value) {
            $array [] = [
                'id' => $value->id,
                'name' => $value->name,
                'kabupaten' => $value->kabupaten
            ];
        }
        return response($array);
    }

    //Fungsi Register participant
    public function register(Request $request){
        $this->validate($request,[
            'email' => 'required|unique:user',
            'password' => 'required|min:8'
        ]);

        $email = $request->input('email');
        $password = $request->input('password');
        $name = $request->input('name');
        $phone = $request->input('phone');
        $regencies_id = $request->input('regencies_id');

        $hasPassword = Hash::make($password);
        $token = $this->getRandomString();
        $data = [
            'email' => $email,
            'password_hash' => $hasPassword,
            'name' => $name,
            'role' => "participant",
            'phone' => $phone,
            'regencies_id' => $regencies_id,
            'avatar' => "upload/images/blank.jpg",
            'verification_token' => $token
        ];

        if(User::create($data)){

            $link = getenv('APP_URL')."/aktivasi/$token";

            $data = [
                'name' => $name,
                'body' => "Selamat akun anda berhasil dibuat silahkan melakukan aktivasi melalui link berikut ".$link
            ];

            Mail::send('email.mail', $data, function($message) use ($name, $email){

                $message->to($email, $name)->subject('Pemberitahuan Akun');
                $message->from('admin@apidigimice.me', 'digiMICE Panitia');
            });

            $pesan = [
                'message' => 'register success',
                'code' => 201
            ];
        }else{
            $pesan = [
                'message' => 'register gagal',
                'code' => 404
            ];
        }

        return response()->json($pesan,$pesan['code']);

    }

    //Fungsi Register panitia
    public function registerPanitia(Request $request){
        $this->validate($request,[
            'email' => 'required|unique:user',
            'password' => 'required|min:8'
        ]);

        $email = $request->input('email');
        $password = $request->input('password');
        $name = $request->input('name');
        $phone = $request->input('phone');
        $regencies_id = $request->input('regencies_id');

        $hasPassword = Hash::make($password);
        $token = $this->getRandomString();
        $data = [
            'email' => $email,
            'password_hash' => $hasPassword,
            'name' => $name,
            'role' => "lead eo",
            'phone' => $phone,
            'regencies_id' => $regencies_id,
            'avatar' => "upload/images/blank.jpg",
            'verification_token' => $token
        ];

        if(User::create($data)){

            $link = getenv('APP_URL')."/aktivasi/$token";

            $data = [
                'name' => $name,
                'body' => "Selamat akun anda berhasil dibuat silahkan melakukan aktivasi melalui link berikut ".$link
            ];

            Mail::send('email.mail', $data, function($message) use ($name, $email){

                $message->to($email, $name)->subject('Pemberitahuan Akun');
                $message->from('admin@apidigimice.me', 'digiMICE Panitia');
            });

            $pesan = [
                'message' => 'register success',
                'code' => 201
            ];
        }else{
            $pesan = [
                'message' => 'register gagal',
                'code' => 404
            ];
        }

        return response()->json($pesan,$pesan['code']);

    }

    public function addPemateri(Request $request){

        $name = $request->input('name');
        $email = $request->input('email');
        $password = $request->input('password');
        $no_telp = $request->input('no_telp');
        $regencies_id = $request->input('regencies_id');
        $event_id = $request->input('event_id');
        $password_hash = Hash::make($password);
        $token = $this->getRandomString();
        $data = [
            'name' => $name,
            'email' => $email,
            'password_hash' => $password_hash,
            'phone' => $no_telp,
            'role' => "speaker",
            'regencies_id' => $regencies_id,
            'avatar' => "upload/images/blank.jpg",
            'verification_token' => $token
        ];

        if($user = User::where('email',$email)->first()){
            return "Email sudah terdaftar";
        }else{

            $user = User::create($data);

            $link = getenv('APP_URL')."/aktivasi/$token";
            $data = [
                'name' => $name,

                'body' => "Selamat Akun Anda Sudah dibuat sebagai Pemateri Event. Silahkan melakukan aktivasi akun melalui link berikut $link",

                'title' => "Kemudian Silahkan login dengan : ",
                'email' => "Email : $email",
                "password" => "Password : $password",
                'note' => "NOTE : JANGAN SEBARKAN KE ORANG LAIN AKUN ANDA !!"
            ];

            Mail::send('email.mail', $data, function($message) use ($name, $email){

                $message->to($email, $name)->subject('Pemberitahuan Akun');
                $message->from('admin@apidigimice.me', 'digiMICE Panitia');
            });

            return "Berhasil ditambahkan";
        }

    }

    //Fungsi Random String
    public function getRandomString($panjang = 32){
        $karakter = '012345678dssd9abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
        $panjang_karakter = strlen($karakter);
        $randomString = '';
        for ($i = 0; $i < $panjang; $i++) {
            $randomString .= $karakter[rand(0, $panjang_karakter - 1)];
        }
        return $randomString;
    }

   
}
