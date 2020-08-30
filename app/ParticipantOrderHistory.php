<?php

namespace App;

use Illuminate\Auth\Authenticatable;
use Illuminate\Contracts\Auth\Access\Authorizable as AuthorizableContract;
use Illuminate\Contracts\Auth\Authenticatable as AuthenticatableContract;
use Illuminate\Database\Eloquent\Model;
use Laravel\Lumen\Auth\Authorizable;

class ParticipantOrderHistory extends Model implements AuthenticatableContract, AuthorizableContract
{
    use Authenticatable, Authorizable;

    protected $table = "participant_order_history";

    protected $fillable = [
        'status','participant_event_id','id_invoice','participant_user_id'
    ];


    public function event(){
    	return $this->belongsTo('App\Event','participant_event_id','id');
    }

    public function user(){
   		return $this->belonsTo('App\User','participant_user_id','id');
   	}



   
}
