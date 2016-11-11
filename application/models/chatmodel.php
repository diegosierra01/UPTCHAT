<?php

class chatmodel extends CI_Model {

    function __construct() {
        parent::__construct();
        $this->load->database();
    }
    public function authUser($nick,$password){
       $data = $this->db->query("SELECT * from usuario WHERE nick = '".$nick ."' AND password = '".$password."' ");
        if ($data->num_rows() > 0) {
            $data = $data->result();
            return $data;
        } else {
            return FALSE;
        }
    }
     public function getUltimo(){
       $query = $this->db->query("SELECT id FROM usuario Order by id Desc limit 1"); 
       $row = $query->row(0);       
       return $row->id;     
    }
   
    public function insertarUsuario($data) {
        $this->db->insert('usuario', $data);
    }
    public function insertarMensaje($data) {
        $this->db->insert('mensaje', $data);
    }
    public function editaPerfil($id,$password) {
         $data = array(
            'password' => $password
         );
       $this->db->where('id', $id); 
       return $this->db->update('usuario', $data);
    }
  
    public function listaUsuarios(){
       $data = $this->db->query("Select id,nick from usuario");
        if ($data->num_rows() > 0) {
            $data = $data->result();
            return $data;
        } else {
            return FALSE;
        }
    }
    public function listarMensajes($destinatario){
        //SELECT cadena,DATE_FORMAT(fecha, '%H:%I:%S')as fecha  FROM mensaje WHERE destinatario = 'U2'
       $data = $this->db->query("SELECT cadena, hora  FROM mensaje WHERE destinatario = '".$destinatario ."' ");
        if ($data->num_rows() > 0) {
            $data = $data->result();
            return $data;
        } else {
            return FALSE;
        }
    }
    

}