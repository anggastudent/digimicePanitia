<?php

namespace App\Http\Controllers;
use App\Session;
use App\EventAgenda;
use App\EventPresensi;
use App\Materi;
use Illuminate\Http\Request;
class SessionController extends Controller
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

    public function index($id){
        
        $session = Session::where('event_id',$id)->orderBy('start','ASC')->get();

        $array = [];
        $qr_code = "";

        foreach ($session as $value) {
            $agenda = EventAgenda::where('event_session_id',$value->id)->orderBy('start','ASC')->get();
            $presensi = EventPresensi::where('event_agenda_event_session_id',$value->id)->where('event_agenda_event_session_event_id',$value->event_id)->first();
            
            if($presensi){
                $qr_code = $presensi->barcode;

            }

            $array[] = [
                'id' => $value->id,
                'name' => $value->name,
                'start' => $value->start,
                'qr_code' => $qr_code ?? "MOBILE-PRESENCE",
                'agenda' => $agenda
            ];
        }
        
        return response($array);
    }

    public function search(Request $request){
        $event_id = $request->input('event_id');
        $search = $request->input('search');
        $session = Session::where('event_id',$event_id)->where('name','LIKE','%'.$search.'%')->get();
        $array = [];
        foreach ($session as $value) {
            $array[] = [
                'id' => $value->id,
                'name' => $value->name,
                'start' => $value->start,
            ];
        }
        
        return response($array);
    }

    public function create(Request $request){

        $input = $request->all();

        Session::create($input);

        return "berhasil";
    }

    public function edit(Request $request, $id){
        $session = Session::findOrFail($id);
        $input = $request->all();
        $session->update($input);

        return "berhasil";
    }

    public function show($id){
        $array = [];
        $session = Session::findOrFail($id);
        $array = [
            $session
        ];
        return $array;
    }

    public function delete($id){
        
        if($agenda = EventAgenda::where('event_session_id',$id)->get()){
            
            foreach ($agenda as $value) {

                if($materi = Materi::where('event_agenda_id',$value->id)->get()){
                    foreach ($materi as $value) {
                        unlink($value->url);
                        $value->delete();    
                    }
                }
                $value->delete();
            }
        }
        
        $session = Session::findOrFail($id);
        $session->delete();
        return "Berhasil hapus";
    }
    //
}
