package com.example.brayan.uptchat;

public class Usuario {
        String id;
        String nick;

    public Usuario() {
    }

    public Usuario(String id, String nick) {
            super();
            this.id = id;
            this.nick = nick;
    }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }
    }