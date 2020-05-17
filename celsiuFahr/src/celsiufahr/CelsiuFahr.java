/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package celsiufahr;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author fb
 */
public class CelsiuFahr {

    // Converte de Celsius para Fahrenheit
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        // args[0] protocolo de transporte TCP ou UDP
        // args[1] porta de transporte

        String protocoloTransporte = args[0];
        String portaTransporte = args[1];
        String mensagem_retorno = "";

        if (protocoloTransporte.equals("TCP")) {
            mensagem_retorno = servicoTCP(new Integer(portaTransporte));
        }

        if (protocoloTransporte.equals("UDP")) {
            mensagem_retorno = servicoUDP(new Integer(portaTransporte));
        }

    }

    public static String servicoUDP(int portaUDP) {
        String mensagem = "";

        DatagramSocket aSocket = null;
        try {
            int serverPort = portaUDP;
            // aSocket = new DatagramSocket(6789);
            aSocket = new DatagramSocket(serverPort);
            byte[] buffer = new byte[1000];
            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                // Implementa a recepção do parâmetro em celsius e submete à função de conversão para fahrenheit
                double celsius = new Double(new String(request.getData()));

                // Submete à função de conversão para Fahrenheit
                double fahrenheit = celsiusFahrenheit(celsius);
                byte[] retorno = new Double(fahrenheit).toString().getBytes();
                DatagramPacket reply = new DatagramPacket(retorno,
                        request.getLength(), request.getAddress(), request.getPort());
                aSocket.send(reply);
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
            mensagem = "Socket: " + e.getMessage();
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
            mensagem = "IO: " + e.getMessage();
        } finally {
            if (aSocket != null) {
                aSocket.close();
            }
        }

        return mensagem;
    }

    public static String servicoTCP(int portaTCP) {
        String mensagem = "";

        try {

            //int serverPort = 7896;
            int serverPort = portaTCP;
            ServerSocket listenSocket = new ServerSocket(serverPort);
            while (true) {
                Socket clientSocket = listenSocket.accept();

                Connection c = new Connection(clientSocket);
            }
        } catch (IOException e) {
            System.out.println("Listen :" + e.getMessage());
            mensagem = "Listen :" + e.getMessage();
        }

        return mensagem;
    }

    public static double celsiusFahrenheit(double celsius) {
        double f = (celsius * 1.8) + 32;
        return f;
    }

}

class Connection extends Thread {

    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;

    public Connection(Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());

            out = new DataOutputStream(clientSocket.getOutputStream());

            //out.write(fah);
            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    public void run() {
        try {                 // an echo server
            String data = in.readUTF();

            double celsius = new Double(data);

            double f = celsiusFahrenheit(celsius);

            out.writeUTF(new Double(f).toString());

        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                /* close failed */
            }
        }
    }

    public static double celsiusFahrenheit(double celsius) {
        double f = (celsius * 1.8) + 32;
        return f;
    }

}
