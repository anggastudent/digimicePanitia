<?php

namespace App;

use Illuminate\Auth\Authenticatable;
use Illuminate\Contracts\Auth\Access\Authorizable as AuthorizableContract;
use Illuminate\Contracts\Auth\Authenticatable as AuthenticatableContract;
use Illuminate\Database\Eloquent\Model;
use Laravel\Lumen\Auth\Authorizable;

class Team extends Model implements AuthenticatableContract, AuthorizableContract
{
    use Authenticatable, Authorizable;

    protected $table = "team";

    protected $fillable = [
        'user_id','team_role','name_team', 'event_id'
    ];

		    
    public function user(){
    	return $this->belongsTo('App\User','user_id','id');
    }

    public function event(){
    	return $this->belongsTo('App\Event', 'event_id','id');
    }
   
}
