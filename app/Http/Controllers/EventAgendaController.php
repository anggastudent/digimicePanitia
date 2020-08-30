<?php

namespace App\Http\Controllers;
use Illuminate\Support\Facades\Hash;
use Illuminate\Http\Request;
use App\EventAgenda;
use App\ParticipantOrderHistory;
use App\Materi;
use App\Team;

class EventAgendaController extends Controller
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

    public function index(Request $request){
        $event_id = $request->input('event_id');
        $agenda = EventAgenda::where('event_session_event_id', $event_id)->orderBy('start', 'DESC')->get();

        $array = [];
       
        foreach ($agenda as $value) {
            $array[] = [
                'id' => $value->id,
                'name' => $value->name,
                'start' => $value->start,
                'end' => $value->end,
                'description' => $value->description,
                'session_id' => $value->event_session_id,
                'session' => $value->session->name

                
            ];
        }
        return response()->json($array);
    }

    public function search(Request $request){
        $search = $request->input('search');
        $event_id = $request->input('event_id');
        $agenda = EventAgenda::where('event_session_event_id', $event_id)->where('name','LIKE','%'.$search.'%')->get();

        $array = [];
       
        foreach ($agenda as $value) {
            $array[] = [
                'id' => $value->id,
                'name' => $value->name,
                'start' => $value->start,
                'end' => $value->end,
                'description' => $value->description,
                'session_id' => $value->event_session_id,
                'session' => $value->session->name

                
            ];
        }
        return response()->json($array);
    }

    // public function agenda(Request $request){
    //     $id_session = $request->input('id_event_session');
    //     $id_event = $request->input('id_event');
    //     $agenda = EventAgenda::where('event_session_id',$id_session)->where('event_session_event_id',$id_event)->get();

    //     return response()->json($agenda);
    // }

    public function create(Request $request){

        $input = $request->all();
        EventAgenda::create($input);
        return "berhasil";
    }

    public function edit($id){

        $agenda = EventAgenda::findOrFail($id);
        $array = [
            $agenda
        ];
        return response()->json($array);
    }

    public function update(Request $request, $id){
        $agenda = EventAgenda::findOrFail($id);
        $input = $request->all();
        $agenda->update($input);
        return "berhasil";
    }

    public function delete($id){
        
        if($materi = Materi::where('event_agenda_id',$id)->get()){
            foreach ($materi as $value) {
                unlink($value->url);
                $value->delete();    
            }
        }
        

        $agenda = EventAgenda::findOrFail($id);
        $agenda->delete();

        return "Berhasih hapus"; 
    }

     public function alarmAgenda(Request $request){
        $user_id = $request->input('user_id');
        $participant = ParticipantOrderHistory::where('participant_user_id',$user_id)->where('status',"PAID")->get();
        

        $array = [];
       
        foreach ($participant as $value) {
            $agenda = EventAgenda::where('event_session_event_id', $value->participant_event_id)->get();
            
            foreach ($agenda as $v) {

                $panitia = Team::where('event_id', $v->event_session_event_id)->first();

                 $array[] = [
                        'event' => $v->event->name,
                        'start' => $v->start,
                        'name' => $v->name,
                        'event_id' => $v->event_session_event_id,
                        'banner' => $v->event->banner,
                        'presence_type' => $v->event->presence_type,
                        'ticket' => $v->event->event_ticket_price,
                        'address' => $v->event->address,
                        'place' => $v->event->place,
                        'panitia' => $panitia->name_team,
                        'desc' => $v->event->description

                ];
            }

        }
        return response()->json($array);
    }

    //
}
