<?php

namespace App;

use Illuminate\Auth\Authenticatable;
use Illuminate\Contracts\Auth\Access\Authorizable as AuthorizableContract;
use Illuminate\Contracts\Auth\Authenticatable as AuthenticatableContract;
use Illuminate\Database\Eloquent\Model;
use Laravel\Lumen\Auth\Authorizable;

class Event extends Model implements AuthenticatableContract, AuthorizableContract
{
    use Authenticatable, Authorizable;

    protected $table = "event";

    protected $fillable = [
        'name','start','end','event_type_id','banner', 'description', 'place', 'address', 'event_status', 'event_paket_id','presence_type', 'event_ticket_price'
    ];

   public function team(){
   		return $this->hasMany('App\Team','id','event_id');
   }

   public function paket(){
   		return $this->belongsTo('App\Paket','event_paket_id','id');
   }
}
