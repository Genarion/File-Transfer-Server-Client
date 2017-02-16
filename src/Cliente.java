
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

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

    public static void main(String[] args) {

        Socket s;
        PrintStream p;
        BufferedReader b;

        String host = "localhost";
        int port = 8888;
        String respuesta;

        //Referencia a la entrada por consola (System.in)
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        try {

            //Creo una conexion al socket servidor
            s = new Socket(host, port);

            //Creo las referencias al canal de escritura y lectura del socket
            p = new PrintStream(s.getOutputStream());
            b = new BufferedReader(new InputStreamReader(s.getInputStream()));

            //Espero la respuesta por el canal de lectura
            respuesta = b.readLine();
            System.out.println(respuesta);

            System.out.println("Introduce el indice: ");
            //Escribo en el canal de escritura del socket
            p.println(in.readLine());

            byte[] contents = new byte[10000];

            //Initialize the FileOutputStream to the output file's full path.
            FileOutputStream fos = new FileOutputStream("prueba.txt");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            InputStream is = s.getInputStream();

            //No of bytes read in one read() call
            int bytesRead = 0;

            while ((bytesRead = is.read(contents)) != -1) {
                bos.write(contents, 0, bytesRead);
            }

            bos.flush();
            s.close();
            p.close();
            b.close();
            
            System.out.println("fichero guardado");
        } catch (UnknownHostException e) {
            System.out.println("No puedo conectarme a " + host + ":" + port);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error de E/S en " + host + ":" + port);
        }
    }
}
