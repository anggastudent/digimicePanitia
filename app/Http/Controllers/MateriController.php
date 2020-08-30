<?php

namespace App\Http\Controllers;
use Illuminate\Http\Request;
use App\Materi;
use App\EventAgenda;

class MateriController extends Controller
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
        $materi = Materi::where('event_agenda_id', $id)->get();
        return $materi;
    }

    public function delete($id){
        $materi = Materi::findOrFail($id);
        unlink($materi->url);
        $materi->delete();
        return "Berhasil Hapus";
    }

    // public function update(Request $request,$id){
    //     $name = $request->input('name');
    //     $materi = Materi::findOrFail($id);
    //     $materi->update(['name' => $name]);
    //     return "Berhasil";
    // }

    public function upload(Request $request){

        $pdf = $request->file('pdf');       
        $name = $request->input('name');
        $event_id = $request->input('event_id');
        $event_agenda_id = $request->input('event_agenda_id');
       

        $target_dir = "upload/pdf";
        // if(!file_exists($target_dir)){
        //     mkdir($target_dir, 0777, true);
        // }

        // $file = $target_dir."/".$name.time().".pdf";

        // $fopen = fopen($file, "wb");
        // $data_pdf = explode(',', $pdf);
        // fwrite($fopen, base64_decode($data_pdf[0]));
        // fclose($fopen);

        if($pdf){
            
            $file = time(). $pdf->getClientOriginalName();
            $pdf->move($target_dir, $file);
            
            $data = [
                'name' => $name,
                'url' => $target_dir."/".$file,
                'event_id' => $event_id,
                'event_agenda_id' => $event_agenda_id,
                'event_event_type_id' => "3"
            ];

            Materi::create($data);

            return "berhasil";
        }
      
    }

    //
}
