package Modelo;
import Vista.Ventana;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
    Instituto Politecnico Nacional
    Escuela Superior de Computo
    Cryptography
    Practica 0
    Implementar RSA con archivos de texto
    Muñoz Primero Elias 
    Ramirez Morales Erick Hazel
 */
public class main {

    public static void main(String args[]) {
        RSA Obj = new RSA();
        Ventana Practica0 = new Ventana(Obj);
        Practica0.setVisible(true);                
    }
}
