<?php

namespace App;

use Illuminate\Auth\Authenticatable;
use Illuminate\Contracts\Auth\Access\Authorizable as AuthorizableContract;
use Illuminate\Contracts\Auth\Authenticatable as AuthenticatableContract;
use Illuminate\Database\Eloquent\Model;
use Laravel\Lumen\Auth\Authorizable;

class Session extends Model implements AuthenticatableContract, AuthorizableContract
{
    use Authenticatable, Authorizable;

    protected $table = "event_session";

    protected $fillable = [
        'event_id','name', 'event_event_type_id','start'
    ];

    public function agenda(){
    	return $this->hasMany('App\EventAgenda', 'event_session_id','id');
    }
   
}
