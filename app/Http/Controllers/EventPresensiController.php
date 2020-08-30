<?php

namespace App\Http\Controllers;
require '../vendor/autoload.php';

use Xendit\Xendit;
use App\EventPresensi;
use App\Participant;
use App\Kabupaten;
use App\Event;
use App\ParticipantOrderHistory;
use App\Session;
use App\User;
use Illuminate\Support\Facades\Hash;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Mail;

class EventPresensiController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        //
        $this->middleware('login');
    }

    //
    public function index(){

    }

    public function addParticipant(Request $request){

        $event_id = $request->input('event_id');
        $user_id = $request->input('user_id');
        $payment = $request->input('payment');
        $participant_group_id = $request->input('participant_group_id');
        $payment_status = $request->input('payment_status');

        $event = Event::where('id',$event_id)->first();
        $session = Session::where('event_id',$event_id)->get('id');
        $user = User::where('id',$user_id)->first();
        
        $cek_participant = Participant::where('user_id',$user_id)->where('event_id',$event_id)->first();
        if($cek_participant){
            return "Akun ini sudah terdaftar";

        }else{

            $data = [
                'event_id' => $event_id,
                'user_id' => $user_id,
                'kit' => "Belum",
                'register' => "Belum",
                'payment' => $payment,
                'payment_status' => $payment_status,
                'participant_group_id' => $participant_group_id
            ];

            $participant = Participant::create($data);

            $barcode = $this->getRandomString(8)."-MOB-";
            
            foreach($session as $value){

                $data2 = [
                    'barcode' => $barcode,
                    'status' => "Belum Hadir",
                    'participant_user_id' => $user_id,
                    'event_agenda_event_session_id' => $value->id,
                    'event_agenda_event_session_event_id' => $event_id,
                    'event_agenda_event_session_event_event_type_id' => "3"
                ];
                EventPresensi::create($data2);
                
            }


            if(!$payment == 0){
                Xendit::setApiKey(getenv('SECRET_API_KEY'));

                $params = [
                  'external_id' => $event->name,
                  'payer_email' => $user->email,
                  'description' => 'Pembuatan Invoice untuk pembelian tiket '.$event->name,
                  'amount' => $payment,
                  'should_send_email' => true
                ];

                $createInvoice = \Xendit\Invoice::create($params);

                $data3 = [
                    'status' => "PENDING",
                    'participant_event_id' => $event_id,
                    'participant_user_id' => $user_id,
                    'id_invoice' => $createInvoice['id']
                ];

                ParticipantOrderHistory::create($data3);

            }else{

                $data3 = [
                    'status' => "PAID",
                    'participant_event_id' => $event_id,
                    'participant_user_id' => $user_id,
                    'id_invoice' => "no invoice"
                ];

                ParticipantOrderHistory::create($data3);
                
                $name = $user->name;
                $email = $user->email;
                
                $data = [
                    'name' => $name,
                    'body' => "Selamat Anda Sudah Bergabung di Event $event->name"
                ];

                Mail::send('email.sukses', $data, function($message) use ($name, $email){

                    $message->to($email, $name)->subject('Pemberitahuan Event');
                    $message->from('admin@apidigimice.me', 'digiMICE Panitia');
                });
            }

            return "Berhasil tambah participant";

        }

    }

    public function setQrCode(Request $request){
        
        $email = $request->input('email');
        $kode_qr = $request->input('kode_qr');
        $event_id = $request->input('event_id');
        $session_id = $request->input('session_id');

        $session = Session::where('event_id',$event_id)->get('id');

        $user = User::where('email',$email)->where('role',"participant")->first();

        if($user){

            $user_id = $user->id;

            if($cek_qr = EventPresensi::where('barcode',$kode_qr)->where('event_agenda_event_session_id',$session_id)->where('participant_user_id',$user_id)->first()){

                return "QR Code Sudah terdaftar";
            }

            foreach ($session as $value) {
                if($presensi = EventPresensi::where('participant_user_id', $user_id)->where('event_agenda_event_session_id',$value->id)->first()){
                    $data = [
                    'barcode' => $kode_qr
                ];

                    $presensi->update($data);
                }else{
                    return "Email belum mendaftar event";
                }
                

                
            }


            $absen = EventPresensi::where('participant_user_id',$user_id)->where('event_agenda_event_session_id',$session_id);
            if($absen){
                 $data2 = [
                    'status' => "Hadir"
                ];
                
                $absen->update($data2);

            return "Berhasil set Email $email QR Code $kode_qr";
        }else{
            return "Email belum mendaftar event";

        }

           
        }
        else{
            return "Email belum terdaftar sebagai peserta";
        }
        

    }

    public function scanQrCode(Request $request){
        $qr_code = $request->input('qr_code');
        $session_id = $request->input('session_id');
        $event_id = $request->input('event_id');
        
        if($cek_absen = EventPresensi::where('barcode', $qr_code)->where('event_agenda_event_session_id', $session_id)->where('status', "Hadir")->first()){
            return $cek_absen->user->name. " Sudah Absen";
        }
        if($presensi = EventPresensi::where('barcode', $qr_code)->where('event_agenda_event_session_id',$session_id)->first()){
            $data = [
                'status' => "Hadir" 
            ];

           $presensi->update($data);

            $participant = Participant::where('user_id',$presensi->participant_user_id)->where('event_id',$event_id)->first();

            $data2 = [
                'kit' => "Sudah",
                'register' => "Sudah"
            ];

            $participant->update($data2);
            //return $presensi;
            return $presensi->user->name ." Berhasil Absen";
        }else{
            return "QR Code salah";
        }

        
    }

    public function rekapitulasi(Request $request){
        $event_id = $request->input('event_id');
        $session_id = $request->input('session_id');

        $presensi = EventPresensi::where('event_agenda_event_session_id',$session_id)->where('event_agenda_event_session_event_id',$event_id)->get();
        
        $array = [];
        
        foreach ($presensi as $value) {
            $kabupaten = Kabupaten::findOrFail($value->user->regencies_id);
            $participant = Participant::where('user_id',$value->participant_user_id)->where('event_id',$event_id)->first();
            $array[] = [
                'name' => $value->user->name,
                'email' => $value->user->email,
                'phone' => $value->user->phone,
                'rekap' => $value->status,
                'payment_status' => $participant->payment_status,
                'provinsi' => $kabupaten->provinsi->name
            ];
        }
        return $array;
    }

    public function search(Request $request){
        $search = $request->input('search');
        $event_id = $request->input('event_id');
        $session_id = $request->input('session_id');

        $presensi = EventPresensi::where('event_agenda_event_session_id',$session_id)->where('event_agenda_event_session_event_id',$event_id)->get();
        
        $array = [];
        
        foreach ($presensi as $value) {
            
            $kabupaten = Kabupaten::findOrFail($value->user->regencies_id);
            $mysearch = strtolower($search);
            $data = strtolower($value->user->name);

            $proses_search = strchr($data, $mysearch);
            
            if($proses_search){
               
                $array[] = [
                'name' => $value->user->name,
                'email' => $value->user->email,
                'phone' => $value->user->phone,
                'rekap' => $value->status,
                'payment_status' => $value->participant->payment_status,
                'provinsi' => $kabupaten->provinsi->name
            ];
            }
            
        }
        return $array;
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
