/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package torre;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author isabela.barros
 */
public class Torre {

    // Lista guarda a assinatura e localização de todos os serviços
    public static ArrayList<String> servicos;

    // recorte
    // ArrayList<String> paginasamarelas = new ArrayList();
    // Exemplo de registro de serviço na torre
    // Código a construir.
    // paginasamarelas.add("temperaturaCelsiusFahrenheit;100;232;127.0.0.1;TCP;10544");
    /* @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        // args[0] protocolo de transporte TCP ou UDP
        // args[1] porta de transporte

        // String que guarda a assinatura de um serviço e sua localização 
        servicos = new ArrayList();

        // A torre receberá dois tipos de mensagens:
        // 1. Uma mensagem de um cliente querendo consultar um serviço . Esta mensagem se chamará consulta
        // 2. Uma mensagem de um serviço querendo se registrar na torre . Essa mensgem envolve a assinatura
        // do serviço e a sua localização . Um serviço ao se registrar o fará alimentando a lista serviços
        // Essa mensagem será do tipo registro

        String protocoloTransporte = args[0];
        String portaTransporte = args[1];
        String mensagem_retorno = "";

        if (protocoloTransporte.equals("TCP")) {
            mensagem_retorno = servicoTCP(new Integer(portaTransporte));
        }

        if (protocoloTransporte.equals("=UDP")) {
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
                // recebe a mensagem da rede e a armazena no objeto request
                aSocket.receive(request);

                // A mensagem recebida no objeto request pode ser de dois tipos:
                // 1. consulta
                // 2. registro
                String mensagemRecebida = "";

                mensagemRecebida = new String(request.getData());

                // Exemplo mensgemRecebida do tipo consulta
                // consulta&nomeServico&parametro1&paramentro2&paramentro3&parametroN
                // Na mensagem do tipo consulta será consultada a lista servicos para
                // informar ao cliente a localização na rede do serviço consultado
                // Exemplo mensagemRecebida do tipo registro
                // registro&nomeServico&ip&tipoProtocoloTransporte&portaTransporte
                String tipoMensagem = mensagemRecebida.substring(0, 8);

                System.out.println(tipoMensagem);

                if (tipoMensagem.equals("registro")) {

                    // Registra na lista servicos o nome do servico, ip, tipo de porta de transporte e porta de transporte
                    // Monta a String a ser registrada na lista serviços
                    // Exemplo: nomeServico&Ip&tipoProtocoloTransporte&portaProtocoloTransporte
                    // Monsta Mensagem de registro do serviço que vai ser adicionado na lista servicos
                    int ultimoIndice = 0;
                    ultimoIndice = 55;

                    System.out.println(ultimoIndice);

                    String mensagemRegistro = mensagemRecebida.substring(9, ultimoIndice);

                    System.out.println(mensagemRegistro);

                    servicos.add(mensagemRegistro);

                    System.out.println(new String(servicos.get(0)));

                }

                // Monta o objeto reply que será enviado de volta para quem fez o envio da mensagem na rede.
                DatagramPacket reply = new DatagramPacket(request.getData(),
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
                    System.out.println(servicos);

            
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
            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    public void run() {
        try {                 // an echo server
            String data = in.readUTF();
            out.writeUTF(data);
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

}
