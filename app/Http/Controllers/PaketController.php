<?php

namespace App\Http\Controllers;
use App\Paket;
class PaketController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        //
        $this->middleware("login");

    }

    public function index(){
        $paket = Paket::all();
        return response($paket);
    }
    //
}
