<?php

namespace App\Http\Middleware;

use Closure;
use App\User;

class LoginMiddleware
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure  $next
     * @return mixed
     */
    public function handle($request, Closure $next)
    {
        if($request->input('token')){
            $check = User::where('auth_key', $request->input('token'))->first();
            if(!$check){
                return response('Token tidak valid',401);
            }else{
                return $next($request);
            }
        }else{
            return response('Silahkan masukkan token',401);
        }
    }
}
