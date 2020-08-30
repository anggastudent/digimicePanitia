<?php

namespace App\Http\Controllers;
use App\User;
use App\Team;
use App\Provinsi;
use App\Event;
use App\Pemateri;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Mail;
use Illuminate\Support\Facades\Hash;

class UsersController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        //
        $this->middleware("login");
    }

    //Fungsi Index
    public function index($id){
        $user = User::where('id',$id)->first();

        $array [] = [
            'name' => $user->name,
            'email' => $user->email,
            'role' => $user->role,
            'avatar' => $user->avatar
        ];
        return response($array);

    }

    public function addPanitia(Request $request){

        $email = $request->input('email');
        $event_id = $request->input('event_id');
        $name_team = $request->input('name_team');

        $user = User::where('email', $email)->first();
        

        if($user){

            $team = Team::where('user_id',$user->id)->where('event_id', $event_id)->first();
            if($user && $team){
                return "email sudah terdaftar";
                
            }else{
                $data = [
                    'user_id' => $user->id,
                    'event_id' => $event_id,
                    'team_role' => "eo",
                    'name_team' => $name_team
                ];

                Team::create($data);
                $token = $this->getRandomString();
                $user->update([
                    'role' => "eo",
                    'verification_token' => $token,
                    'status' => 'deactivated'
                ]);

                $event = Event::where('id', $event_id)->first();

                $link = getenv('APP_URL')."/aktivasi/$token";
                $name = $user->name;
                $data = [
                    'name' => $name,
                    'body' => "Selamat Akun Anda sudah dibuat Sebagai Anggota Panitia Event $event->name. Silahkan melakukan aktivasi akun melalui link berikut $link "
                ];

                Mail::send('email.mail', $data, function($message) use ($name, $email){

                    $message->to($email, $name)->subject('Pemberitahuan Akun');
                    $message->from('admin@apidigimice.me', 'digiMICE Panitia');
                });

                return "berhasil";
            }
            

        }else{

            $user = User::where('email',$email)->first();

            if($user){
                return "email sudah terdaftar";
                
            }else{

                //return "email belum terdaftar";
                $token = $this->getRandomString();
                $name = "Anggota Panitia";

                $create_user = User::create([
                    'name' => $name,
                    'email' => $email,
                    'password_hash' => Hash::make("11111111"),
                    'role' => "eo",
                    'phone' => "123",
                    'avatar' => "upload/images/blank.jpg",
                    'verification_token' => $token,
                    'regencies_id' => "3510"

                ]);

                $data = [
                    'user_id' => $create_user->id,
                    'event_id' => $event_id,
                    'team_role' => "eo",
                    'name_team' => $name_team
                ];

                Team::create($data);

                $event = Event::where('id', $event_id)->first();

                $link = getenv('APP_URL')."/aktivasi/$token";
                $data = [
                    'name' => $name,
                    'body' => "Selamat Akun Anda sudah dibuat Sebagai Anggota Panitia Event $event->name. Silahkan melakukan aktivasi akun melalui link berikut $link ",
                    'title' => "Kemudian Silahkan login dengan : ",
                    'email' => "Email : $email",
                    "password" => "Password : 11111111",
                    'note' => "NOTE : JANGAN SEBARKAN KE ORANG LAIN AKUN ANDA !!"
                ];

                Mail::send('email.mail', $data, function($message) use ($name, $email){

                    $message->to($email, $name)->subject('Pemberitahuan Akun');
                    $message->from('admin@apidigimice.me', 'digiMICE Panitia');
                });

                return "Berhasil";
            }
            
        }

        
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

            $data2 = [
                'user_id' => $user->id,
                'event_id' => $event_id
            ];

            Pemateri::create($data2);

            $event = Event::where('id', $event_id)->first();

            $link = getenv('APP_URL')."/aktivasi/$token";
            $data = [
                'name' => $name,

                'body' => "Selamat Akun Anda Sudah dibuat sebagai Pemateri Event $event->name. Silahkan melakukan aktivasi akun melalui link berikut $link",

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


    public function edit($id){
        $user = User::findOrFail($id);
        
        $array = [
            $user
        ];

        return response($array);
    }

    public function update(Request $request, $id){
        
        $user = User::findOrFail($id);
        
        $name = $request->input('name');
        $avatar = $request->input('avatar');
        $old_password = $request->input('old_password');
        $new_password = $request->input('new_password');

        if(trim($avatar) == ''){

        }else{

            $target_dir = "upload/images";
        
            if(!file_exists($target_dir)){

                mkdir($target_dir, 0777, true);
            }
            if (!$user->avatar=="upload/images/blank.jpg") {
                unlink($user->avatar);
            }
            
            
            
            $file = $target_dir."/image".time().".jpeg";
            $ifp = fopen($file, "wb"); 

            $data2 = explode(',', $avatar);

            fwrite($ifp, base64_decode($data2[0])); 
            fclose($ifp); 

            $data = [

                'name' => $name,
                'avatar' => $file
                                    
            ];

            $user->update($data);

            return "berhasil";

            
        }

        if(trim($old_password) == '' && trim($new_password) == ''){

            $old_password = $request->except('old_password');
            $new_password = $request->except('new_password');

        }else{

            if(Hash::check($old_password, $user->password_hash)){

                $password = Hash::make($new_password);

                $data = [

                    'name' => $name,
                    'password_hash' => $password
                                        
                ];

                $user->update($data);

                return "berhasil";

            }else{

                return "Password Lama Tidak Cocok";
            }
        }

        $data = [

            'name' => $name
                         
        ];

        if($user->update($data)){

            return "berhasil";
        }

    }

    public function gabungPemateri(Request $request){

        $user_id = $request->input('user_id');
        $event_id = $request->input('event_id');

        if($pemateri = Pemateri::where('user_id',$user_id)->where('event_id',$event_id)->first()){
            return "Pemateri sudah tergabung";
        }else{
            $data2 = [
                'user_id' => $user_id,
                'event_id' => $event_id
            ];

            Pemateri::create($data2);

            return "Pemateri berhasil bergabung";
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
