
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mario
 */
public class Cliente {

    static String host = "localhost";
    public String respuesta;
    static int port = 8888;
    public static JFVista vista = new JFVista();
    public static Socket s;
    public PrintStream p;
    public BufferedReader b;

    public Cliente() throws Exception {

        this.s = new Socket(host, port);
        this.p = new PrintStream(s.getOutputStream());
        this.b = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.vista.setVisible(true);

        //Espero la respuesta por el canal de lectura
         this.respuesta = b.readLine();
        System.out.println("aa " + respuesta);
        String[] listadoArchivos = respuesta.split(",");
        vista.getjComboBoxLista().removeAllItems();
        for (String st : listadoArchivos) {
            st = st.replace("[", "");
            st = st.replace("]", "");
            st = st.trim();
            vista.getjComboBoxLista().addItem(st);
        }

        vista.getjButton1().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    s = new Socket(host, port);
                    //Creo las referencias al canal de escritura y lectura del socket
                    PrintStream pa = new PrintStream(s.getOutputStream());
                    BufferedReader ba = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    //Escribo en el canal de escritura del socket
                    pa.println(vista.getjComboBoxLista().getSelectedIndex());

                    byte[] contents = new byte[10000];

                    //Initialize the FileOutputStream to the output file's full path.
                    System.out.println("Archivo seleccionado " + vista.getjComboBoxLista().getSelectedItem().toString().replace("ficheros\\", ""));
                    FileOutputStream fos = new FileOutputStream(vista.getjComboBoxLista().getSelectedItem().toString().replace("ficheros\\", ""));
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    InputStream is = s.getInputStream();

                    //No of bytes read in one read() call
                    int bytesRead = 0;

                    while ((bytesRead = is.read(contents)) != -1) {
                        bos.write(contents, 0, bytesRead);
                    }

                    bos.flush();

                    //la linea es variable respuesta
                    //archivo es vista.getjComboBoxLista().getSelectedItem().toString().replace("ficheros\\", "")
//                    Scanner fileScanner = new Scanner(vista.getjComboBoxLista().getSelectedItem().toString().replace("ficheros\\", ""));
//                    fileScanner.nextLine();
//                    FileWriter fileStream = new FileWriter(vista.getjComboBoxLista().getSelectedItem().toString().replace("ficheros\\", ""));
//                    BufferedWriter out = new BufferedWriter(fileStream);
//                    while (fileScanner.hasNextLine()) {
//                        String next = fileScanner.nextLine();
//                        if (next.equals(respuesta)) 
//                            out.newLine();
//                        else {
//                            out.write(next);
//                        }
//                        out.newLine();
//                    }
//                    out.close();
                    

                    System.out.println("fichero guardado");
                    pa.close();
                    ba.close();
                    s.close();
                } catch (IOException se) {
                    se.printStackTrace();
                    System.out.println("Error de E/S en " + host + ":" + port);
                }

            }
        });
    }

    public static void main(String[] args) {
        try {
            new Cliente();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
