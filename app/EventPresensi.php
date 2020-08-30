<?php

namespace App;

use Illuminate\Auth\Authenticatable;
use Illuminate\Contracts\Auth\Access\Authorizable as AuthorizableContract;
use Illuminate\Contracts\Auth\Authenticatable as AuthenticatableContract;
use Illuminate\Database\Eloquent\Model;
use Laravel\Lumen\Auth\Authorizable;

class EventPresensi extends Model implements AuthenticatableContract, AuthorizableContract
{
    use Authenticatable, Authorizable;

    protected $table = "event_presensi";

    protected $fillable = [
        'participant_user_id','event_agenda_event_session_id','barcode','status',
        'event_agenda_event_session_event_id','event_agenda_event_session_event_event_type_id'
    ];

   public function participant(){
   	return $this->belongsTo('App\Participant','participant_user_id','user_id');
   }

   public function user(){
   	return $this->belongsTo('App\User','participant_user_id','id');
   }
}
