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

     public function getUltimoGrupo(){
       $query = $this->db->query("SELECT idgrupo FROM grupo Order by idgrupo Desc limit 1"); 
       $row = $query->row(0);       
       return $row->idgrupo;     
    }

    public function getUltimoUsuario(){
       $query = $this->db->query("SELECT idusuario FROM usuario Order by idusuario Desc limit 1"); 
       $row = $query->row(0);       
       return $row->idusuario;     
    }
   
    public function insertarUsuario($data) {
        $this->db->insert('usuario', $data);
    }
    public function insertarMensaje($data) {
        $this->db->insert('mensaje', $data);
    }
    public function crearGrupo($data) {
        $this->db->insert('grupo', $data);      
    }

    public function addUserToGroup($data) {
        $this->db->insert('usuario_grupo', $data);
    }
    public function editaPerfil($id,$password) {
         $data = array(
            'password' => $password
         );
       $this->db->where('idusuario', $id); 
       return $this->db->update('usuario', $data);
    }
  
    public function listaUsuarios(){
       $data = $this->db->query("Select idusuario,nick from usuario");
        if ($data->num_rows() > 0) {
            $data = $data->result();
            return $data;
        } else {
            return FALSE;
        }
    }

    public function listarOpciones($idgrupo){
       $data = $this->db->query("SELECT idusuario,nick from usuario where idusuario NOT IN (SELECT idusuario from usuario_grupo where idgrupo='".$idgrupo."')");
        if ($data->num_rows() > 0) {
            $data = $data->result();
            return $data;
        } else {
            return FALSE;
        }
    }

     public function listaGrupos($idusuario){
        //*********OJO aca debe hacer el Join con la Table de usuario_grupo 
        //para que solo le aperezcan los grupos a los que pertecene el usuario Logueado************
       $data = $this->db->query("SELECT u.idgrupo, g.nombre from grupo g INNER JOIN usuario_grupo u ON u.idgrupo = g.idgrupo WHERE u.idusuario='".$idusuario ."'");
        if ($data->num_rows() > 0) {
            $data = $data->result();
            return $data;
        } else {
            return FALSE;
        }
    }
     public function listaAllGrupos(){
      
       $data = $this->db->query("SELECT * from grupo");
        if ($data->num_rows() > 0) {
            $data = $data->result();
            return $data;
        } else {
            return FALSE;
        }
    }
    public function listarMensajes($remitente, $destinatario){
       $data = $this->db->query("SELECT nick, cadena, fecha FROM usuario u INNER JOIN(SELECT * FROM mensaje WHERE destinatario = '".$destinatario ."' AND remitente = '".$remitente ."' UNION SELECT *  FROM mensaje WHERE destinatario = '".$remitente ."' AND remitente = '".$destinatario ."') m ON m.remitente = u.idusuario order by fecha");
        if ($data->num_rows() > 0) {
            $data = $data->result();
            return $data;
        } else {
            return FALSE;
        }
    }

    public function listarMensajesGrupo($grupo){
       $data = $this->db->query("SELECT nick, cadena, fecha FROM usuario u INNER JOIN(SELECT * FROM mensaje WHERE destinatario = '".$grupo ."') m ON m.remitente = u.idusuario order by fecha");
        if ($data->num_rows() > 0) {
            $data = $data->result();
            return $data;
        } else {
            return FALSE;
        }
    }
   
}
