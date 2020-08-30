<?php

namespace App\Http\Controllers;

require '../vendor/autoload.php';

use Xendit\Xendit;
use App\Event;
use App\Team;
use App\Session;
use App\ParticipantOrderHistory;
use App\EventOrderHistory;
use App\Refund;
use App\User;
use App\Speaker;
use App\Participant;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Mail;


class EventController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
       $this->middleware("login");

    }
    
    //

    public function index(Request $request){
        
        $user_id = $request->input('user_id');
        //$team = Team::where('user_id', $user_id)->orderBy('id','DESC')->get();
        $team = Team::where('user_id',$user_id)->orderByDesc(
            Event::select('start')->whereColumn('id','team.event_id')->orderBy('start', 'DESC')
        )->get();
        $array = [];

        foreach ($team as $value) {
            if($value->event->event_status == "true"){
                $array[] = [
                    'id' => $value->event->id,
                    'name' => $value->event->name,
                    'start' => $value->event->start,
                    'end' => $value->event->end,
                    'place' => $value->event->place,
                    'address' => $value->event->address,
                    'banner' => $value->event->banner,
                    'presence_type' => $value->event->presence_type
                
                ];
            }
            
        }
        
        
        return  response()->json($array);
    }

    public function search(Request $request){
        $user_id = $request->input('user_id');
        $team = Team::where('user_id', $user_id)->orderBy('id','DESC')->get();
        $search = $request->input('search');
        $array = [];


        foreach ($team as $value) {

            $mysearch = strtolower($search);
            $data = strtolower($value->event->name);

            $proses_search = strchr($data, $mysearch);
            
            if($value->event->event_status == "true" && $proses_search){
                $array[] = [
                    'id' => $value->event->id,
                    'name' => $value->event->name,
                    'start' => $value->event->start,
                    'end' => $value->event->end,
                    'place' => $value->event->place,
                    'address' => $value->event->address,
                    'banner' => $value->event->banner,
                    'presence_type' => $value->event->presence_type
                
                ];
            }
            
        }
        
        
        return  response()->json($array);
    }

    public function create(Request $request){

        
        $name = $request->input('name');
        $start = $request->input('start');
        $end = $request->input('end');
        $event_type_id = $request->input('event_type_id');
        $banner = $request->input('banner');
        $description = $request->input('description');
        $place = $request->input('place');
        $address = $request->input('address');
        $event_status = $request->input('event_status');
        $event_paket_id = $request->input('event_paket_id');
        $presence_type = $request->input('presence_type');
        $event_ticket_price = $request->input('event_ticket_price');
        $name_session = $request->input('name_session');
        $name_packet = $request->input('name_packet');
        $price_packet = $request->input('price_packet');
        
        $user_id = $request->input('user_id');
        $team_role = $request->input('team_role');
        $name_team = $request->input('name_team');
        $email = $request->input('email');
        $start_session = $request->input('start_session');

        if(trim($banner) == ''){
            $file = "upload/images/blank.jpg";

            $data = [

                'name' => $name,
                'start' => $start,
                'end' => $end,
                'event_type_id' => $event_type_id,
                'banner' => $file,
                'description' => $description,
                'place' => $place,
                'address' => $address,
                'event_status' => $event_status,
                'event_paket_id' => $event_paket_id,
                'presence_type' => $presence_type,
                'event_ticket_price' => $event_ticket_price,

            ];
        }else{

            $target_dir = "upload/images";
            
            if(!file_exists($target_dir)){
                mkdir($target_dir, 0777, true);
            }
            
            $file = $target_dir."/image".time().".jpeg";
            $ifp = fopen($file, "wb"); 

            $data2 = explode(',', $banner);

            fwrite($ifp, base64_decode($data2[0])); 
            fclose($ifp);

            $data = [

                'name' => $name,
                'start' => $start,
                'end' => $end,
                'event_type_id' => $event_type_id,
                'banner' => $file,
                'description' => $description,
                'place' => $place,
                'address' => $address,
                'event_status' => $event_status,
                'event_paket_id' => $event_paket_id,
                'presence_type' => $presence_type,
                'event_ticket_price' => $event_ticket_price,

            ];
        }

         
        

        $event = Event::create($data);

        $team = Team::where('event_id',$event->id)->first();

        $edit = [
            'user_id' => $user_id,
            'team_role' => $team_role,
            'name_team' => $name_team
        ];

        $team->update($edit);
        
        Session::create([
            'name' => $name_session,
            'start' => $start_session,
            'event_event_type_id' => $event_type_id,
            'event_id' => $event->id
        ]);

        if(!$price_packet == "0"){
            Xendit::setApiKey(getenv('SECRET_API_KEY'));
            $params = [
              'external_id' => $name_packet,
              'payer_email' => $email,
              'description' => 'Pembuatan Invoice untuk pembelian '.$name_packet.' '.$name,
              'amount' => $price_packet,
              'should_send_email' => true
            ];

            $createInvoice = \Xendit\Invoice::create($params);

            $data3 = [
                'status' => "PENDING",
                'event_id' => $event->id,
                'event_paket_event_id' => $event_paket_id,
                'id_invoice' => $createInvoice['id'],
                'user_id' => $user_id
            ];

            EventOrderHistory::create($data3);
        }else{

             $data3 = [
                'status' => "PAID",
                'event_id' => $event->id,
                'event_paket_event_id' => $event_paket_id,
                'id_invoice' => "no invoice",
                'user_id' => $user_id
            ];

            EventOrderHistory::create($data3);

            $user = User::where('id', $user_id)->first();
            $namePanitia = $user->name;

            $dataEmail = [
                'name' => $namePanitia,
                'body' => "Selamat Event Paket Gratis $name Anda Berhasil ditambahkan"
            ];

            Mail::send('email.sukses', $dataEmail, function($message) use ($namePanitia, $email){

                $message->to($email, $namePanitia)->subject('Pemberitahuan Event');
                $message->from('admin@apidigimice.me', 'digiMICE Panitia');
            });
                 
            


        }
        
        return "Berhasil ditambahkan";

    }

    public function edit(Request $request){

        $user_id = $request->input('user_id');
        $event_id = $request->input('event_id');

        $event = Event::findOrFail($event_id);
        $team = Team::where('event_id', $event_id)->where('team_role','lead eo')->where('event_id',$event_id)->first();

        $array[] = [
            'name' => $event->name,
            'description' => $event->description,
            'place' => $event->place,
            'address' => $event->address,
            'start' => $event->start,
            'banner' => $event->banner,
            'end' => $event->end,
            'price' => $event->paket->price,
            'ticket' => $event->event_ticket_price,
            'panitia' => $team->name_team ?? ""
            
        ];
        
        
        return response()->json($array);
    }

    public function update(Request $request, $id){

        $event = Event::findOrFail($id);

        $user_id = $request->input('user_id');
        $team = Team::where('event_id',$event->id)->where('user_id',$user_id)->first();

        $name = $request->input('name');
        $description = $request->input('description');
        $place = $request->input('place');
        $address = $request->input('address');
        $start = $request->input('start');
        $end = $request->input('end');
        $banner = $request->input('banner');
        $event_ticket_price = $request->input('event_ticket_price');
        $panitia = $request->input('panitia');

        
        if(trim($banner == '')){
            
            $data = [
                'name' => $name,
                'description' => $description,
                'place' => $place,
                'address' => $address,
                'start' => $start,
                'end' => $end,
                'event_ticket_price' => $event_ticket_price

            ];


            $event->update($data);

            $team->update(['name_team' => $panitia]);
            
        }else{
            $target_dir = "upload/images";
        
            if(!file_exists($target_dir)){
                mkdir($target_dir, 0777, true);
            }
            
            if(!$event->banner=="upload/images/blank.jpg"){
                unlink($event->banner);
            }
            
            
            $file = $target_dir."/image".time().".jpeg";
            $ifp = fopen($file, "wb"); 

            $data2 = explode(',', $banner);

            fwrite($ifp, base64_decode($data2[0])); 
            fclose($ifp); 

            $data = [
                'name' => $name,
                'description' => $description,
                'place' => $place,
                'address' => $address,
                'start' => $start,
                'end' => $end,
                'banner' => $file,
                'event_ticket_price' => $event_ticket_price

            ];

            $event->update($data);
            $team->update(['name_team' => $panitia]);

        }
        

        return "sukses";
    }

    public function orderEventPending($id){

        $order_event = EventOrderHistory::where('user_id',$id)->where('status',"PENDING")->orderBy('id','DESC')->get();
        Xendit::setApiKey(getenv('SECRET_API_KEY'));
        $array = [];
        foreach ($order_event as $value) {

            $id = $value->id_invoice;
            $getInvoice = \Xendit\Invoice::retrieve($id);

            $array[] = [
                'id_invoice' => $value->id_invoice,
                'name_event' => $value->event->name,
                'status' => $value->status,
                'name_paket' => $value->paket->name,
                'price' => $value->paket->price,
                'max_participant' => $value->paket->max_participant,
                'expired' => $getInvoice['expiry_date'],
                'url' => $getInvoice['invoice_url'],
                
                
            ];
        }
        return $array;

    }

    public function expiredEventPending($id){

        Xendit::setApiKey(getenv('SECRET_API_KEY'));
        $expireInvoice = \Xendit\Invoice::expireInvoice($id);
        
        return "Berhasil dibatalkan";

    }

    public function orderEventExpired($id){
        $order_event = EventOrderHistory::where('user_id',$id)->where('status',"EXPIRED")->orderBy('id','DESC')->get();
        
        Xendit::setApiKey(getenv('SECRET_API_KEY'));
        $array = [];
        foreach ($order_event as $value) {

            $id = $value->id_invoice;
            $getInvoice = \Xendit\Invoice::retrieve($id);

            $array[] = [
                'id_invoice' => $value->id_invoice,
                'name_event' => $value->event->name,
                'status' => $value->status,
                'name_paket' => $value->paket->name,
                'price' => $value->paket->price,
                'max_participant' => $value->paket->max_participant,
                'expired' => $getInvoice['expiry_date'],
                'url' => $getInvoice['invoice_url']
            ];
        }
        return $array;

    }

    public function orderEventPaid($id){
        $order_event = EventOrderHistory::where('user_id',$id)->where('status',"PAID")->orderBy('id','DESC')->get();
        
        Xendit::setApiKey(getenv('SECRET_API_KEY'));
        $array = [];
        foreach ($order_event as $value) {

            $id = $value->id_invoice;
            
            if($id != "no invoice"){
                $getInvoice = \Xendit\Invoice::retrieve($id);

                $array[] = [
                    
                    'id_invoice' => $value->id_invoice,
                    'name_event' => $value->event->name,
                    'status' => $value->status,
                    'name_paket' => $value->paket->name,
                    'price' => $value->paket->price,
                    'max_participant' => $value->paket->max_participant,
                    'paid_at' => $getInvoice['paid_at'],
                    'url' => $getInvoice['invoice_url']
                ];
            }else{
                $array[] = [
                        
                    'id_invoice' => $value->id_invoice,
                    'name_event' => $value->event->name,
                    'status' => $value->status,
                    'name_paket' => $value->paket->name,
                    'price' => $value->paket->price,
                    'max_participant' => $value->paket->max_participant,
                    'paid_at' => $value->created_at,
                    'url' => ""
                ];   
            }
            
        }
        return $array;
    }

    public function eventParticipant(){
        $event = Event::where('event_status','true')->orderBy('start','DESC')->get();

        $array = [];

        foreach ($event as $value) {
           $panitia = Team::where('event_id',$value->id)->first();
           $count_participant = Participant::where('event_id',$value->id)->count();
           $array [] = [
                'panitia' => $panitia->name_team,
                'id' => $value->id,
                'judul' => $value->name,
                'start' => $value->start,
                'end' => $value->end,
                'banner' => $value->banner,
                'ticket' => $value->event_ticket_price,
                'event_type_id' => $value->event_type_id,
                'desc' => $value->description,
                'place' => $value->place,
                'address' => $value->address,
                'presence_type' => $value->presence_type,
                'max_participant' => $value->paket->max_participant,
                'participant' => $count_participant
           ];
        }

        return $array;
    }

    public function searchEventParticipant(Request $request){
            $search = $request->input('search');
            $event = Event::where('event_status','true')->orderBy('start','DESC')->get();

            $array = [];

            foreach ($event as $value) {
                $mysearch = strtolower($search);
                $data = strtolower($value->name);

                $proses_search = strchr($data, $mysearch);

                if($proses_search) {
                     $panitia = Team::where('event_id',$value->id)->first();
                     $array [] = [
                        'panitia' => $panitia->name_team,
                        'id' => $value->id,
                        'judul' => $value->name,
                        'start' => $value->start,
                        'end' => $value->end,
                        'banner' => $value->banner,
                        'ticket' => $value->event_ticket_price,
                        'event_type_id' => $value->event_type_id,
                        'desc' => $value->description,
                        'place' => $value->place,
                        'address' => $value->address,
                        'presence_type' => $value->presence_type
                    ];

                }
            }
            return $array;

    }


    public function myEventParticipant($id){
        $participant = ParticipantOrderHistory::where('participant_user_id',$id)->where('status',"PAID")->orderBy('id','DESC')->get();
        
        $array = [];

        foreach ($participant as $value) {
            $panitia = Team::where('event_id', $value->participant_event_id)->first();
            $array [] = [
                'panitia' => $panitia->name_team,
                'id' => $value->event->id,
                'judul' => $value->event->name,
                'start' => $value->event->start,
                'end' => $value->event->end,
                'banner' => $value->event->banner,
                'ticket' => $value->event->event_ticket_price,
                'event_type_id' => $value->event->event_type_id,
                'desc' => $value->event->description,
                'place' => $value->event->place,
                'address' => $value->event->address,
                'presence_type' => $value->event->presence_type
            ];
                 
        }
        return $array;
    }

    public function orderTicketParticipantPending($id){

        $order_ticket = ParticipantOrderHistory::where('participant_user_id',$id)->where('status',"PENDING")->orderBy('id','DESC')->get();
        Xendit::setApiKey(getenv('SECRET_API_KEY'));
        $array = [];
        foreach ($order_ticket as $value) {

            $id = $value->id_invoice;
            $getInvoice = \Xendit\Invoice::retrieve($id);

            $array[] = [
                'id_invoice' => $value->id_invoice,
                'name_event' => $value->event->name,
                'price' => $value->event->event_ticket_price,
                'date' => $getInvoice['expiry_date'],
                'url' => $getInvoice['invoice_url'],
                
                
            ];
        }
        return $array;

    }

    public function orderTicketParticipantExpired($id){
        $order_ticket = ParticipantOrderHistory::where('participant_user_id',$id)->where('status',"EXPIRED")->orderBy('id','DESC')->get();
        
        Xendit::setApiKey(getenv('SECRET_API_KEY'));
        $array = [];
        foreach ($order_ticket as $value) {

            $id = $value->id_invoice;
            $getInvoice = \Xendit\Invoice::retrieve($id);

            $array[] = [
               'id_invoice' => $value->id_invoice,
                'name_event' => $value->event->name,
                'price' => $value->event->event_ticket_price,
                'date' => $getInvoice['expiry_date'],
                'url' => $getInvoice['invoice_url'],
            ];
        }
        return $array;

    }

    public function orderTicketParticipantPaid($id){
        $order_ticket = ParticipantOrderHistory::where('participant_user_id',$id)->where('status',"PAID")->orderBy('id','DESC')->get();
        
        Xendit::setApiKey(getenv('SECRET_API_KEY'));
        $array = [];
        foreach ($order_ticket as $value) {

            $id = $value->id_invoice;
            
            if($id != "no invoice"){
                $getInvoice = \Xendit\Invoice::retrieve($id);

                $array[] = [
                    
                    'id_invoice' => $value->id_invoice,
                    'name_event' => $value->event->name,
                    'event_id' => $value->participant_event_id,
                    'price' => $value->event->event_ticket_price,
                    'date' => $getInvoice['paid_at'],
                    'url' => $getInvoice['invoice_url'],
                ];
            }else{
                $array[] = [
                        
                    'id_invoice' => $value->id_invoice,
                    'name_event' => $value->event->name,
                    'price' => $value->event->event_ticket_price,
                    'date' => $value->paid_at,
                    'url' => "",
                ];   
            }
            
        }
        return $array;
    }

    public function refund(Request $request){
        $amount = $request->input('amount');
        $bank_code = $request->input('bank_code');
        $bank_account = $request->input('bank_account');
        $name = $request->input('name');
        $email = $request->input('email');
        $name_event = $request->input('name_event');
        $user_id = $request->input('user_id');
        $event_id = $request->input('event_id');

        Xendit::setApiKey(getenv('SECRET_API_KEY'));

        $params = [
          'external_id' => 'pengembalian-'.time(),
          'amount' => $amount,
          'bank_code' => $bank_code,
          'account_holder_name' => $name,
          'account_number' => $bank_account,
          'description' => 'Pengembalian Dana Tiket '.$name_event,
          'email_to' => [$email],
          'X-IDEMPOTENCY-KEY'
        ];
      $createDisbursements = \Xendit\Disbursements::create($params);

      $data = [
        'status' => "Pending",
        'id_disbursement' => $createDisbursements['id'],
        'participant_user_id' => $user_id,
        'participant_event_id' => $event_id,

      ];
      Refund::create($data);

      return $createDisbursements;
    }

    public function eventSpeaker(Request $request){
        $user_id = $request->input('user_id');
        $pemateri = Speaker::where('user_id', $user_id)->orderBy('id','DESC')->get();
        
        $array = [];

        foreach ($pemateri as $value) {
            $panitia = Team::where('event_id', $value->event_id)->first();

            if($value->event->event_status == "true"){
                $array[] = [
                    'panitia' => $panitia->name_team,
                    'id' => $value->event->id,
                    'judul' => $value->event->name,
                    'start' => $value->event->start,
                    'end' => $value->event->end,
                    'banner' => $value->event->banner,
                    'ticket' => $value->event->event_ticket_price,
                    'event_type_id' => $value->event->event_type_id,
                    'desc' => $value->event->description,
                    'place' => $value->event->place,
                    'address' => $value->event->address,
                    'presence_type' => $value->event->presence_type
                
                ];
            }
            
        }
        
        
        return  response()->json($array);
    }

    public function refundList($id){

        Xendit::setApiKey(getenv('SECRET_API_KEY'));

        $refund = Refund::where('participant_user_id',$id)->orderBy('id','DESC')->get();

        $array = [];

        foreach ($refund as $value) {
            $id = $value->id_disbursement;
            $getDisbursements = \Xendit\Disbursements::retrieve($id);
            $array [] = [

                'id_disbursement' => $id,
                'name_event' => $value->eventRefund->name ?? "Tidak ada",
                'amount' => $getDisbursements['amount'],
                'status' => $value->status,
                'date' => $value->created_at
            ];
        }

        return $array;
    }
}
