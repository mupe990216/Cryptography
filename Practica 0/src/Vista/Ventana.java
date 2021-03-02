package Vista;

import Modelo.RSA;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Ventana extends JFrame implements ActionListener {

    public JPanel panel;
    public JButton btn1, btn2, btn3;
    public File archivo;
    public JTextField jtf_NombreArchivo;
    public RSA cifrador;

    public Ventana(RSA obj_critico) {
        cifrador = obj_critico;
        setSize(460, 650);//Tamaño Ventana (X,Y)
        setTitle("Practica 0 - Implementacion RSA");//Ponemos titulo a la ventana
        setResizable(false);//No podemos redimensionar la ventana        
        iniciaComponentes();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void iniciaComponentes() {
        creaPanel();
        creaComponentes();
        addListeners();
    }

    public void creaPanel() {
        panel = new JPanel();
        panel.setLayout(null); // Desactivamos el diseño por defecto para modificar a gusto
        panel.setBackground(new Color(135, 145, 225));
        this.getContentPane().add(panel); //Agregamos el panel a la ventana
    }

    public void creaComponentes() {
        btn1 = new JButton("Encrypt File");
        btn1.setBounds(50, 50, 350, 100);
        btn1.setBackground(new Color(160, 205, 240));
        btn1.setForeground(new Color(40, 40, 40));
        btn1.setFont(new Font("arial", 1, 22));
        panel.add(btn1);

        btn2 = new JButton("Decrypt File");
        btn2.setBounds(50, 200, 350, 100);
        btn2.setBackground(new Color(160, 205, 240));
        btn2.setForeground(new Color(40, 40, 40));
        btn2.setFont(new Font("arial", 1, 22));
        panel.add(btn2);

        btn3 = new JButton("Show Public Key");
        btn3.setBounds(50, 350, 350, 100);
        btn3.setBackground(new Color(160, 205, 240));
        btn3.setForeground(new Color(40, 40, 40));
        btn3.setFont(new Font("arial", 1, 22));
        panel.add(btn3);

        jtf_NombreArchivo = new JTextField();
        jtf_NombreArchivo.setBounds(50, 500, 350, 65);
        jtf_NombreArchivo.setFont(new Font("arial", 1, 22));
        jtf_NombreArchivo.setBackground(new Color(200, 215, 240));
        panel.add(jtf_NombreArchivo);
    }

    public void addListeners() {
        btn1.addActionListener(this);
        btn2.addActionListener(this);
        btn3.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == btn1) {
                Cifrar();
            }
            if (e.getSource() == btn2) {
                Descifrar();
            }
            if (e.getSource() == btn3) {
                jtf_NombreArchivo.setText("");
                jtf_NombreArchivo.setText(cifrador.k + "." + cifrador.n);
            }
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
    }

    /*
        Metodo Cifrar: Sirve para cifrar un mensaje que lo leera un destinatario
        Requisitos: Se necesita conocer la llave publica del destinatario
     */
    public void Cifrar() throws FileNotFoundException, IOException {
        //0) Recibir clave publica del destinatario
        String Clav_Pub = JOptionPane.showInputDialog("Ingresa La Clave Publica");
        String aux_k = "", aux_n = "";
        int bandera = 0;
        for (int i = 0; i < Clav_Pub.length(); i++) {
            if (Clav_Pub.charAt(i) == '.') {
                bandera = 1;
                i++;
            }
            if (bandera == 0) {
                aux_k += Clav_Pub.charAt(i);
            }else{
                aux_n += Clav_Pub.charAt(i);
            }            
        }        
        JOptionPane.showMessageDialog(null, "Clave publica: " + aux_k + "." +aux_n + "\n\nAhora Selecciona el archivo a cifrar :3");
        BigInteger k_otro,n_otro;
        k_otro = new BigInteger(aux_k);        
        n_otro = new BigInteger(aux_n);
        System.out.println(k_otro);
        System.out.println(n_otro);

        //1) Seleccionamos el archivo a cifrar
        String nombre_archivo = "", nom_temp = "";
        JFileChooser abreArchivo = new JFileChooser();
        abreArchivo.showOpenDialog(this);
        archivo = abreArchivo.getSelectedFile();
        nom_temp = archivo.getName();
        for (int i = 0; i < nom_temp.length(); i++) {
            if (nom_temp.charAt(i) == '.') {
                if (nom_temp.charAt(i + 1) == 't') {
                    if (nom_temp.charAt(i + 2) == 'x') {
                        if (nom_temp.charAt(i + 3) == 't') {
                            break;
                        }
                    }
                }
            } else {
                nombre_archivo += nom_temp.charAt(i);
            }
        }
        JOptionPane.showMessageDialog(null, "Archivo a cifrar: " + nombre_archivo);

        //2) Extraemos el contenido del archivo
        FileReader lector_archivo = new FileReader(archivo);
        BufferedReader flujo_lector = new BufferedReader(lector_archivo);
        String linea, mensaje = "";
        while ((linea = flujo_lector.readLine()) != null) {
            System.out.println(linea);
            mensaje += linea;
        }
        lector_archivo.close();

        //3) Ciframos el contenido
        int i;
        byte[] temp = new byte[1];
        byte[] digitos = mensaje.getBytes();
        BigInteger[] bigdigitos = new BigInteger[digitos.length];

        for (i = 0; i < bigdigitos.length; i++) {
            temp[0] = digitos[i];
            bigdigitos[i] = new BigInteger(temp);
        }

        BigInteger[] cifrado = new BigInteger[bigdigitos.length];

        for (i = 0; i < bigdigitos.length; i++) {
            cifrado[i] = bigdigitos[i].modPow(k_otro, n_otro);
        }

        String txtSalida = "";
        for (i = 0; i < cifrado.length; i++) {
            txtSalida += "" + cifrado[i].toString() + ".";
            if (i != cifrado.length - 1) {
                txtSalida += "";
            }
        }
        System.out.println(txtSalida);

        //4) Guardamos el contenido cifrado en un nuevo archivo
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter("./" + nombre_archivo + "_C" + ".txt");
            pw = new PrintWriter(fichero);
            pw.println(txtSalida);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                System.out.println("Error:" + e2.getMessage());
            }
        }
    }

    public void Descifrar() throws FileNotFoundException, IOException {
        //0) Mensaje descrifar
        JOptionPane.showMessageDialog(null, "Selecciona el archivo a Descifrar :3");

        //1) Seleccionamos el archivo a cifrar
        String nombre_archivo = "", nom_temp = "";
        JFileChooser abreArchivo = new JFileChooser();
        abreArchivo.showOpenDialog(this);
        archivo = abreArchivo.getSelectedFile();
        nom_temp = archivo.getName();
        for (int i = 0; i < nom_temp.length(); i++) {
            if (nom_temp.charAt(i) == '.') {
                if (nom_temp.charAt(i + 1) == 't') {
                    if (nom_temp.charAt(i + 2) == 'x') {
                        if (nom_temp.charAt(i + 3) == 't') {
                            break;
                        }
                    }
                }
            } else {
                nombre_archivo += nom_temp.charAt(i);
            }
        }
        JOptionPane.showMessageDialog(null, "Archivo a Descifrar: " + nombre_archivo);

        //2) Extraemos el contenido del archivo
        FileReader lector_archivo = new FileReader(archivo);
        BufferedReader flujo_lector = new BufferedReader(lector_archivo);
        String linea, mensaje = "";
        while ((linea = flujo_lector.readLine()) != null) {
            System.out.println(linea);
            mensaje += linea;
        }
        lector_archivo.close();

        //2.1) Transformamos el texto a un arreglo BigInteger
        int contador_puntos = 0;
        for (int i = 0; i < mensaje.length(); i++) {
            if (mensaje.charAt(i) == '.') {
                contador_puntos += 1;
            }
        }
        System.out.println("Puntos Encontrados: " + contador_puntos);

        BigInteger[] textoCifrado = new BigInteger[contador_puntos];
        contador_puntos = 0;
        String temporal = "";
        for (int i = 0; i < mensaje.length(); i++) {
            if (mensaje.charAt(i) == '.') {
                textoCifrado[contador_puntos] = new BigInteger(temporal);
                temporal = "";
                contador_puntos += 1;
            } else {
                temporal += mensaje.charAt(i);
            }
        }
        /*
            Imprimimos el arreglo BitInteger
        for (BigInteger obj : textoCifrado) {
            System.out.println(obj);
        }
         */

        //3) Descrifar
        BigInteger[] decifrado = new BigInteger[textoCifrado.length];

        for (int i = 0; i < decifrado.length; i++) {
            decifrado[i] = textoCifrado[i].modPow(cifrador.d, cifrador.n);
        }

        char[] charArray = new char[decifrado.length];

        for (int i = 0; i < charArray.length; i++) {
            charArray[i] = (char) (decifrado[i].intValue());
        }

        //Imprimimos el texto descifrado
        String txtSalida = "";
        for (char c : charArray) {
            System.out.print(c);
            txtSalida += c;
        }

        //4) Creamos archivo Descrifado
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter("./" + nombre_archivo + "_D" + ".txt");
            pw = new PrintWriter(fichero);
            pw.println(txtSalida);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                System.out.println("Error:" + e2.getMessage());
            }
        }

    }

    private String toString(String showInputDialog) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
