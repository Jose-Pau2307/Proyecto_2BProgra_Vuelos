
package com.mycompany.proyecto2b_sistema_vuelos;

import vista.frmLogin;


public class Proyecto2B_Sistema_Vuelos {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO SISTEMA DE VUELOS FIS ===");
        
        // Crear y mostrar el formulario de login
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                frmLogin login = new frmLogin();
                login.setVisible(true);
                System.out.println("Formulario de login abierto");
                
               
            }
        });
    }
}
