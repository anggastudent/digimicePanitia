<?php

namespace App;

use Illuminate\Auth\Authenticatable;
use Illuminate\Contracts\Auth\Access\Authorizable as AuthorizableContract;
use Illuminate\Contracts\Auth\Authenticatable as AuthenticatableContract;
use Illuminate\Database\Eloquent\Model;
use Laravel\Lumen\Auth\Authorizable;

class Provinsi extends Model implements AuthenticatableContract, AuthorizableContract
{
    use Authenticatable, Authorizable;

    protected $table = "provinces";

    // protected $fillable = [
    //     'event_id','name', 'event_event_type_id'
    // ];

    public function kabupaten(){
    	return $this->hasMany('App\Kabupaten', 'province_id','id');
    }
   
}
