<?php

namespace App;

use Illuminate\Auth\Authenticatable;
use Illuminate\Contracts\Auth\Access\Authorizable as AuthorizableContract;
use Illuminate\Contracts\Auth\Authenticatable as AuthenticatableContract;
use Illuminate\Database\Eloquent\Model;
use Laravel\Lumen\Auth\Authorizable;

class Participant extends Model implements AuthenticatableContract, AuthorizableContract
{
    use Authenticatable, Authorizable;

    protected $table = "participant";

    protected $fillable = [
        'event_id','user_id','kit','register','payment','payment_status',
        'participant_group_id'
    ];

    public function event(){
    	return $this->belongsTo('App\Event','event_id','id');
    }
   	
   	public function user(){
   		return $this->belonsTo('App\User','user_id','id');
   	}
}
