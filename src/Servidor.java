
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mario
 */
public class Servidor extends Thread {

    protected Socket sc;

    public Servidor(Socket socketCliente) {
        this.sc = socketCliente;
    }

    public void run() {

        PrintStream p; //Canal de escritura
        BufferedReader b; //Canal de Lectura

        String mensaje;

        try {
            //Obtengo una referencia a los canales de escritura y lectura del socket cliente
            b = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            p = new PrintStream(sc.getOutputStream());

            File f = new File("ficheros");
            ArrayList<File> files = new ArrayList<File>(Arrays.asList(f.listFiles()));
            //Escribo en canal de escritura el mismo mensaje recibido
            p.println(files.toString());

            //Leo lo que escribio el socket cliente en el canal de lectura
            mensaje = b.readLine();
            System.out.println(mensaje);

            //Enviar fichero
            FileInputStream fis = new FileInputStream(files.get(Integer.parseInt(mensaje)));
            BufferedInputStream bis = new BufferedInputStream(fis);

            //Get socket's output stream
            OutputStream os = sc.getOutputStream();

            //Read File Contents into contents array 
            byte[] contents;
            long fileLength = files.get(Integer.parseInt(mensaje)).length();
            long current = 0;

            long start = System.nanoTime();
            while (current != fileLength) {
                int size = 10000;
                if (fileLength - current >= size) {
                    current += size;
                } else {
                    size = (int) (fileLength - current);
                    current = fileLength;
                }
                contents = new byte[size];
                bis.read(contents, 0, size);
                os.write(contents);
            }

            os.flush();
            //File transfer done. Close the socket connection!
            
            System.out.println("Archivo enviado correctamente");

            p.close();
            b.close();

            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No puedo crear el socket");
        }
    }
}
