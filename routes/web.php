<?php

/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It is a breeze. Simply tell Lumen the URIs it should respond to
| and give it the Closure to call when that URI is requested.
|
*/

$router->get('/', function () use ($router) {
    return $router->app->version();
});

$router->post('/register','AuthController@register');
$router->post('/login','AuthController@login');
$router->get('/user/{id}','UsersController@index');
$router->get('/event','EventController@index');
$router->get('agenda','EventAgendaController@index');
$router->get('create-invoice','XenditController@createInvoice');
$router->get('get-invoice','XenditController@getInvoice');
$router->get('get-disbursement','XenditController@getDisbursement');
$router->get('create-disbursement','XenditController@createDisbursement');
$router->get('paket','PaketController@index');
$router->post('add-event','EventController@create');
$router->get('edit-event','EventController@edit');
$router->put('update-event/{id}','EventController@update');
$router->get('session/{id}','SessionController@index');
$router->post('add-agenda','EventAgendaController@create');
$router->get('edit-agenda/{id}','EventAgendaController@edit');
$router->put('update-agenda/{id}','EventAgendaController@update');
$router->post('add-panitia','UsersController@addPanitia');
$router->post('upload-materi','MateriController@upload');
$router->get('materi/{id}','MateriController@index');
$router->post('add-session','SessionController@create');
$router->put('edit-session/{id}','SessionController@edit');
$router->get('show-session/{id}','SessionController@show');
$router->get('provinsi','AuthController@provinsi');
$router->post('add-pemateri','UsersController@addPemateri');
$router->get('edit-user/{id}','UsersController@edit');
$router->put('update-user/{id}','UsersController@update');
$router->post('add-participant','EventPresensiController@addParticipant');
$router->post('set-qrcode','EventPresensiController@setQrCode');
$router->post('scan-qrcode','EventPresensiController@scanQrCode');
$router->get('rekapitulasi','EventPresensiController@rekapitulasi');
$router->post('callback-invoice/{id}','XenditController@callbackInvoice');
$router->post('callback-disbursement/{id}','XenditController@callbackDisbursement');
$router->get('pending/{id}','EventController@orderEventPending');
$router->get('expired/{id}','EventController@orderEventExpired');
$router->get('paid/{id}','EventController@orderEventPaid');
$router->get('search-event','EventController@search');
$router->get('search-session','SessionController@search');
$router->get('search-agenda','EventAgendaController@search');
$router->get('search-rekapitulasi','EventPresensiController@search');
$router->get('delete-session/{id}','SessionController@delete');
$router->get('delete-agenda/{id}','EventAgendaController@delete');
$router->get('delete-materi/{id}','MateriController@delete');
$router->get('expired-invoice/{id}','EventController@expiredEventPending');
$router->get('event-participant','EventController@eventParticipant');
$router->post('login-participant','AuthController@loginParticipant');
$router->get('my-event-participant/{id}','EventController@myEventParticipant');
$router->get('pending-participant/{id}','EventController@orderTicketParticipantPending');
$router->get('paid-participant/{id}','EventController@orderTicketParticipantPaid');
$router->get('expired-participant/{id}','EventController@orderTicketParticipantExpired');
$router->get('search-event-participant','EventController@searchEventParticipant');
$router->post('refund-ticket','EventController@refund');
$router->get('event-speaker','EventController@eventSpeaker');
$router->post('gabung-pemateri','UsersController@gabungPemateri');
$router->get('alarm-agenda','EventAgendaController@alarmAgenda');
$router->post('register-panitia','AuthController@registerPanitia');
$router->get('available-bank','XenditController@getBank');
$router->get('refund-list/{id}','EventController@refundList');
$router->get('email-panitia','EmailController@registerPanitia');
$router->get('aktivasi/{id}','EmailController@validasiEmailAkun');
$router->post('add-pemateri-participant','AuthController@addPemateri');
