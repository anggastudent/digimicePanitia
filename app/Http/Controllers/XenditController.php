<?php

namespace App\Http\Controllers;

require '../vendor/autoload.php';
use Xendit\Xendit;
use App\EventOrderHistory;
use App\ParticipantOrderHistory;
use App\Participant;
use App\Event;
use App\Session;
use App\EventPresensi;
use App\Team;
use App\User;
use App\Refund;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Mail;


class XenditController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        //
        Xendit::setApiKey(getenv('SECRET_API_KEY'));
    }

    public function createInvoice(){
      
      Xendit::setApiKey(getenv('SECRET_API_KEY'));
      $params = [
          'external_id' => 'tes_api_8',
          'payer_email' => 'anggadwisaputra83@gmail.com',
          'description' => 'Nyoba aja',
          'amount' => 10000,
          'should_send_email' => true
      ];

      $createInvoice = \Xendit\Invoice::create($params);

      // if($createInvoice){
      //   $data = [
      //       'status' => "PENDING",
      //       'participant_event_id' => "1",
      //       'participant_user_id' => "1280",
      //       'id_invoice' => $createInvoice['id']

      //   ];

      //   OrderHistory::create($data);
      // }

      return $array = [$createInvoice];

      // $options['secret_api_key'] = 'xnd_development_rpOvltDUNxwYd4DbJ8czoEJiPKU3AwgeMQsfW6KxNLuJYJvTDmiiJBCeKNaZ0';
      // $xenditPHPClient = new \XenditClient\XenditPHPClient($options);

      // $external_id = 'tes_api_5';
      // $amount = 50000;
      // $payer_email = 'a123aku@gmail.com';
      // $description = 'Tes membayar';

      // $response = $xenditPHPClient->createInvoice($external_id, $amount, $payer_email, $description);
      // $array = [
      //     $response
      // ];
      // return $array;
 
    }

    public function getInvoice(){
      
      $id = "5f4a02f55c2c214034bcf9c8";
      $getInvoice = \Xendit\Invoice::retrieve($id);
      return $array = [$getInvoice];

      // $options['secret_api_key'] = 'xnd_development_rpOvltDUNxwYd4DbJ8czoEJiPKU3AwgeMQsfW6KxNLuJYJvTDmiiJBCeKNaZ0';
      // $xenditPHPClient = new \XenditClient\XenditPHPClient($options);

      //   $invoice_id = '5ea41a101c9cf11118cbf0dd';

      //   $response = $xenditPHPClient->getInvoice($invoice_id);
        
      //   $array = [
      //     $response
      //   ];
      // return $array;
    }

    public function expiredInvoice(){
      $id = "5ea455591c9cf11118cbf228";
      $expireInvoice = \Xendit\Invoice::expireInvoice($id);
      return $expireInvoice;
    }

    public function createDisbursement(){

      $params = [
          'external_id' => 'disb-12345678',
          'amount' => 90000,
          'bank_code' => 'MANDIRI',
          'account_holder_name' => 'Rizky',
          'account_number' => '7654321',
          'description' => 'Disbursement from Example',
          'email_to' => ['a123aku@gmail.com'],
          'X-IDEMPOTENCY-KEY'
      ];
      $createDisbursements = \Xendit\Disbursements::create($params);


      return $createDisbursements;
      // $options['secret_api_key'] = 'xnd_development_rpOvltDUNxwYd4DbJ8czoEJiPKU3AwgeMQsfW6KxNLuJYJvTDmiiJBCeKNaZ0';
      // $xenditPHPClient = new \XenditClient\XenditPHPClient($options);

      // $external_id = 'demo_14754597758172';
      // $amount = 800000;
      // $bank_code = 'BCA';
      // $account_holder_name = 'Angga Dwi';
      // $account_number = '1231241231';
      // $disbursement_options['description'] = 'tess api';

      // $response = $xenditPHPClient->createDisbursement($external_id, $amount, $bank_code, $account_holder_name, $account_number, $disbursement_options);
      
      // $array = [
      //     $response
      // ];
      // return $array;
    }

    public function getBank(){
      $getDisbursementsBanks = \Xendit\Disbursements::getAvailableBanks();
      return $getDisbursementsBanks;
    }

    public function getDisbursement(){
      $id = "5f126ae11c2bf00017214fc3";
      $getDisbursements = \Xendit\Disbursements::retrieve($id);
      return $getDisbursements;
      
      // $options['secret_api_key'] = 'xnd_development_rpOvltDUNxwYd4DbJ8czoEJiPKU3AwgeMQsfW6KxNLuJYJvTDmiiJBCeKNaZ0';
      // $xenditPHPClient = new \XenditClient\XenditPHPClient($options);
      
      // $disbursement_id = '5e4009b1b1be6b0020c63e9e';

      // $response = $xenditPHPClient->getDisbursement($disbursement_id);
      
      // $array = [
      //     $response
      // ];
      // return $array;
    }

    public function callbackInvoice($id){
       if ($_SERVER["REQUEST_METHOD"] === "POST" && "d03c8015b391f898985d59818b68971f3d3d8b7eb8a15cd7ba3715e17e6ce52b" == $id) 
       {
           
          $data = file_get_contents("php://input");
          $parse = json_decode($data); 
          $invoice_id = $parse->id;
          $status = $parse->status;

          if($event_order_history = EventOrderHistory::where('id_invoice',$invoice_id)->first()){
            
            $name = $event_order_history->user->name;
            $email = $event_order_history->user->email;

            $event_order_history->update(['status' => $status]);

            $event = Event::where('id',$event_order_history->event_id)->first();
            $name_paket_event = $event->paket->name;
            $name_event = $event->name;

            if($status == "PAID"){

              $event->update(['event_status' => "true"]);

              $dataEmail = [
                'name' => $name,
                'body' => "Selamat Event $name_paket_event $name_event Anda Berhasil ditambahkan"
              ];

              Mail::send('email.sukses', $dataEmail, function($message) use ($name, $email){

                  $message->to($email, $name)->subject('Pemberitahuan Event');
                  $message->from('admin@apidigimice.me', 'digiMICE Panitia');
              });

            }

          }

          if($participant_order_history = ParticipantOrderHistory::where('id_invoice',$invoice_id)->first()){

              // 

              $participant_order_history->update(['status' => $status]);

              $participant = Participant::where('user_id',$participant_order_history->participant_user_id)->where('event_id',$participant_order_history->participant_event_id)->first();

              $user = User::where('id',$participant->user_id)->first();
              $event = Event::where('id', $participant->event_id)->first();
              
              if($status == "PAID"){

                $participant->update(['payment_status' => "Lunas"]);

                $name = $user->name;
                $email = $user->email;
                $name_event = $event->name;

                $datas = [
                  'name' => $name,
                  'body' => "Selamat Anda Sudah Bergabung di Event $name_event"
                ];

                Mail::send('email.sukses', $datas, function($message) use ($name, $email){

                    $message->to($email, $name)->subject('Pemberitahuan Event');
                    $message->from('admin@apidigimice.me', 'digiMICE Panitia');
                });
              }

          }
          
      } else {
          return "Akses ditolak";
      }

    }

     public function callbackDisbursement($id){
       if ($_SERVER["REQUEST_METHOD"] === "POST" && "d03c8015b391f898985d59818b68971f3d3d8b7eb8a15cd7ba3715e17e6ce52b" == $id) {
          $data = file_get_contents("php://input");
          $parse = json_decode($data);
          $id_disbursement = $parse->id;
          $status = $parse->status;

          $refund = Refund::where('id_disbursement',$id_disbursement)->first();
          if($refund){
            $refund->update(['status' => $status]);

            $participant = Participant::where('user_id',$refund->participant_user_id)->where('event_id',$refund->participant_event_id)->first();

            $order_participant = ParticipantOrderHistory::where('participant_user_id',$refund->participant_user_id)->where('participant_event_id', $refund->participant_event_id)->first();

            $session = Session::where('event_id',$refund->participant_event_id)->get('id');

              if($status == "COMPLETED"){
                // $participant->update(['payment_status' => "Belum Lunas"]);
                // $order_participant->update(['status' => "Refund"]);
                $participant->delete();
                $order_participant->delete();

                foreach($session as $value){
                  
                  $presensi = EventPresensi::where('participant_user_id',$refund->participant_user_id)->where('event_agenda_event_session_event_id', $refund->participant_event_id)->where('event_agenda_event_session_id', $value->id)->first();
                  
                  $presensi->delete();
                
                }
              }
          } 
          

      } else {
          return "Akses ditolak";
      }

    }

    //
}
