/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpcliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 *
 * @author isabela.barros
 */
public class TCPCliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        // args[0] ip
        // args[1] protocolo de transporte TCP ou UDP
        // args[2] porta de transporte
        // args[3] mensagem
        String ip = args[0];
        String protocoloTransporte = args[1];
        String portaTransporte = args[2];
        String mensagem = args[3];

        String retorno = "";

        if (protocoloTransporte.equals("TCP")) {
            retorno = clienteTCP(ip, new Integer(portaTransporte), mensagem);
        }

        if (protocoloTransporte.equals("UDP")) {
            retorno = clienteUDP(ip, new Integer(portaTransporte), mensagem);
        }
    }

    public static String clienteUDP(String ip, int portaUDP, String mensagem) {
        String retorno = "";

        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            byte[] m = mensagem.getBytes();
            InetAddress aHost = InetAddress.getByName(ip);

            int serverPort = portaUDP; // recebendo a porta udp
            DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
            aSocket.send(request);
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            System.out.println("Reply: " + new String(reply.getData()));
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
            retorno = "Socket: " + e.getMessage();
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
            retorno = "IO: " + e.getMessage();
        } finally {
            if (aSocket != null) {
                aSocket.close();
            }
        }

        return retorno;
    }

    public static String clienteTCP(String ip, int portaTCP, String mensagem) {
        String retorno = "";

        Socket s = null;
        try {

            int serverPort = portaTCP; // Porta TCP do serviço
            s = new Socket(ip, serverPort);
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out
                    = new DataOutputStream(s.getOutputStream());
            out.writeUTF(mensagem);        // UTF is a string encoding; see Sec 4.3
            String data = in.readUTF();
            System.out.println("Received: " + data);
        } catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
            retorno = "Sock:" + e.getMessage();
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
            retorno = "EOF:" + e.getMessage();
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
            retorno = "IO:" + e.getMessage();
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    /* close failed */
                }
            }
        }
        System.out.println(retorno);
        return retorno;
       
    }

}
