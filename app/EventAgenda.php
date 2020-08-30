<?php

namespace App;

use Illuminate\Auth\Authenticatable;
use Illuminate\Contracts\Auth\Access\Authorizable as AuthorizableContract;
use Illuminate\Contracts\Auth\Authenticatable as AuthenticatableContract;
use Illuminate\Database\Eloquent\Model;
use Laravel\Lumen\Auth\Authorizable;

class EventAgenda extends Model implements AuthenticatableContract, AuthorizableContract
{
    use Authenticatable, Authorizable;

    protected $table = "event_agenda";

    protected $fillable = [
        'name','description','start','end','event_session_id','event_session_event_id','event_session_event_event_type_id'
    ];

    public function session(){
    	return $this->belongsTo('App\Session','event_session_id','id');
    }

    public function event(){
    	return $this->belongsTo('App\Event', 'event_session_event_id', 'id');
    }
   
}
