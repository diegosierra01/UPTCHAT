<?php

defined('BASEPATH') OR exit('No direct script access allowed');

class chat extends CI_Controller {

    function __construct() {
        parent::__construct();
        $this->load->helper('url');
        $this->load->model("chatmodel");
    }

    public function newId() {
        
       $cadena =  $this->chatmodel->getUltimo();
       $numero = intval(preg_replace('/[^0-9]+/', '', $cadena), 10);
       $semiID = $numero + 1;
       $idAuto = 'U'.$semiID;       
       return $idAuto;   
    }

    public function encriptar($cadena){
        $key='';  
        $encrypted = base64_encode(mcrypt_encrypt(MCRYPT_RIJNDAEL_256, md5($key), $cadena, MCRYPT_MODE_CBC, md5(md5($key))));
        return $encrypted; 

    }
    
    public function desencriptar($cadena) {
            $key = ''; 
            $decrypted = rtrim(mcrypt_decrypt(MCRYPT_RIJNDAEL_256, md5($key), base64_decode($cadena), MCRYPT_MODE_CBC, md5(md5($key))), "\0");
            return $decrypted; 
    }

    public function registrar() {
     
        $data['id'] = $this->newId();
        $data['nick'] = $this->input->post("nick");
        $data['password'] = $this->input->post("password");
       
        $this->chatmodel->insertarUsuario($data);
        echo "Registrado exitosamente.";    
    }
   
    public function autenticar() {
        $nick = $this->input->get("nick");
        $password = $this->input->get("password");
        $user = $this->chatmodel->authUser($nick,$password);
        echo json_encode(array('usuario' => $user));
    }
    
    public function enviarMensaje() {
        $data['cadena'] = $this->input->post("cadena");
        $data['remitente'] = $this->input->post("remitente");
        $data['destinatario'] = $this->input->post("destinatario");
        $data['fecha'] = $this->input->post("fecha");
        $data['hora'] = $this->input->post("hora");
       
        $this->chatmodel->insertarMensaje($data);
        echo "Registrado exitosamente.";
        
    }
    
    public function editarPerfil(){       
        $id = $this->input->post('id');
        $password = $this->input->post("password");
        $this->chatmodel->editaPerfil($id,$password);
        echo "Cambio exitoso.";
    }
    
    public function listarUsuarios() {
       
        $users = $this->chatmodel->listaUsuarios();
        echo json_encode(array('usuarios' => $users));
    }
    
    public function listarGrupos() {
       
        $grupos = $this->chatmodel->listaGrupos();
        echo json_encode(array('grupos' => $grupos));
    }

    public function listarMensajes() {
        $destinatario = $this->input->get("destinatario");
        $remitente = $this->input->get("remitente");
        $mensajes= $this->chatmodel->listarMensajes($remitente, $destinatario);
        echo json_encode(array('mensajes' => $mensajes));
    }

    public function listarMensajesGrupo(){
        $grupo = $this->input->get("remitente");
        $mensajes= $this->chatmodel->listarMensajesGrupo($grupo);
        echo json_encode(array('mensajes' => $mensajes));
    }
    
    public function crearGrupo(){
        $data['nombre'] = $this->input->post("nombre");
        $this->chatmodel->crearGrupo($data);
        echo "Grupo registrado exitosamente.";

    }
    
    public function addUserToGroup(){
        $data['id_grupo'] = $this->input->post("id_grupo");
        $usuarios = $this->input->post("usuarios");
        $seleccionados = json_decode($usuarios);
        foreach ($seleccionados as $indice => $valor) {
            $data['id_usuario'] = $valor;
            $this->chatmodel->addUserToGroup($data);
        }
        echo "Usuarios añadidos correctamente.";

    }
}
