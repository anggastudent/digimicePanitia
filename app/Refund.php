<?php

namespace App;

use Illuminate\Auth\Authenticatable;
use Illuminate\Contracts\Auth\Access\Authorizable as AuthorizableContract;
use Illuminate\Contracts\Auth\Authenticatable as AuthenticatableContract;
use Illuminate\Database\Eloquent\Model;
use Laravel\Lumen\Auth\Authorizable;

class Refund extends Model implements AuthenticatableContract, AuthorizableContract
{
    use Authenticatable, Authorizable;

    protected $table = "participant_refund_order";

    protected $fillable = [
        'participant_event_id','status', 'participant_user_id','id_disbursement',
    ];

    public function eventRefund(){

    	return $this->belongsTo('App\Event','participant_event_id','id');
    }
    
   
}
