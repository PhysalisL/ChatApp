package model;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Yixiu Liu on 8/31/2016.
 */
public class Server {
    private static final int ANY_OPEN_PORT = 0;
    private static final int TEST_PORT = 49194;
    private static Server singletonServer;

    private volatile LinkedList<ClientHandler> clientHandlerList = new LinkedList<>();

    private ServerSocket serverSocket;
    private int portNum;

    public Server(){}

    public static Server getInstance(){
        if(singletonServer == null)
            singletonServer = new Server();
        return singletonServer;
    }

    public void startUp(){
        startUp(ANY_OPEN_PORT);
    }

    public void startUp(int portNumber){
        try {
            if(serverSocket != null)
                serverSocket.close();
            serverSocket = new ServerSocket(portNumber);
            openConnection();
        }catch(IOException e){
            e.printStackTrace();
            disconnectAll();
        }
    }
    /**
     * Creates a background thread that checks for incoming connections.
     * Each successful connection will create and assign a server-side socket to
     * handle that client. Remeber to terminate this thread by closing
     * server socket, else this thread will prevent the program from truly
     * closing.
     */
    private void openConnection(){
        Runnable listeningProcess = ()->{
            try {
                while(!serverSocket.isClosed()){
                    Socket clientCommunicatorSocket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(clientCommunicatorSocket);
                    clientHandlerList.add(clientHandler);
                }
            } catch (IOException e) {
                disconnectAll();
                e.printStackTrace();
            }
        };

        Thread connectionListener = new Thread(listeningProcess);
        connectionListener.start();
    }

    private synchronized void sendMessageToAllClients(Message message){
        for(ClientHandler clientHandler: clientHandlerList){
            clientHandler.outputMessage(message);
        }
    }

    /**
     * Close all the ClientHandler sockets, then close the server socket.
     * Call this to end all server side threads.
     */
    public void disconnectAll(){
        if(serverSocket == null)
            return;

        try {
            for(ClientHandler handler : clientHandlerList){
                handler.disconnect();
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getServerIP(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Unknown Host Exception";
        }
    }

    public int getPort(){
        return serverSocket.getLocalPort();
    }


    /**
     * Each ClientHandler is created to handle one Client connection. Receiving
     * and outputting information to Clients should be done through the ClientHandlers
     * that has been assigned to.
     */
    private class ClientHandler{
        private Socket handlerSocket;

        /**
         * Open up the input listener.
         *
         * @param clientCommunicatorSocket
         */
        private ClientHandler(Socket clientCommunicatorSocket){
            handlerSocket = clientCommunicatorSocket;
            openInputListener();
        }

        /**
         * Outputs an message object through the <code>outputstream</code> that leads to the client socket
         * @param message
         */
        private void outputMessage(Message message){
            try {
                if(!handlerSocket.isClosed()) {
                    ObjectOutputStream outStream = new ObjectOutputStream(new BufferedOutputStream(handlerSocket.getOutputStream()));
                    outStream.writeObject(message);
                    outStream.flush();
                }
            } catch (EOFException streamCloseEvent){
                disconnect();
            } catch (IOException e){
                disconnect();
            }
        }

        /**
         * Opens a thread in the background that continously checks for inputs. Once the input is recieved
         * it will access the <code>ClientHandlerList</code> on the server and
         * make each <code>ClientHandler</code> output the input message to their respective
         * <code>clients</code>.
         */
        private void openInputListener(){
            Thread listener = new Thread(()->{
                while(!handlerSocket.isClosed()) {
                    try {
                        ObjectInputStream inputStream = new ObjectInputStream(handlerSocket.getInputStream()); //HOLD BLOCK
                        Message message = (Message) inputStream.readObject();

                        sendMessageToAllClients(message);

                    } catch (EOFException streamCloseEvent){
                        disconnect();
                    } catch (IOException | ClassNotFoundException e) {
                        disconnect();
                    }
                }
            });
            listener.start();
        }

        /**
         * Returns the local port of the ClientHandler socket.
         * @return
         */
        private int getPort(){
            return handlerSocket.getLocalPort();
        }

        /**
         * Closes ClientHandler socket. Therefore terminating the listener thread.
         */
        private void disconnect(){
            if(handlerSocket == null) return;

            try {
                handlerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
