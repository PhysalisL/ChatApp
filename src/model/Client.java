package model;

import observerpattern.Observable;
import observerpattern.Observer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Yixiu Liu on 8/31/2016.
 */
public class Client implements Observable<Message> {
    public final int CONNECTION_TIMEOUT = 500;

    private volatile Socket socket;
    private Queue<Message> inbox;
    private String ip = "Not Connected To Any Server";
    private int port = -404;

    private String nickname = "";

    public Client(){
        socket = new Socket();
        inbox = new LinkedList<>();
    };

    /**
     * Connects the client to a server.
     * This does not open the client up for receiving information from server.
     * Call the <Code>openInputListener()</Code> method to start receiving information.
     *
     * @param hostIP
     * @param hostPort
     */
    public boolean connect(String hostIP, int hostPort){
        boolean success = false;
        try {
            socket.connect(new InetSocketAddress(hostIP, hostPort), CONNECTION_TIMEOUT);
            success = true;
            ip = hostIP;
            port = hostPort;
            openInputListener();
        } catch (IOException e) {
            disconnect();
        }
        return success;
    }

    public String getConnectedIP(){
        return ip;
    }

    public int getPort(){
        return port;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    /**
     * Out put an object through the <code>outputstream</code>
     * to the server if the socket is connected.
     *
     * @param message
     * Message to send to server
     *
     * @return
     * True if send operation is success. False otherwise
     */
    public boolean send(Message message){
        boolean success = false;
        message.setID(nickname);//////////////////////////////////////////////////////////////////////////////////////

        if(!socket.isClosed()){
            try {
                OutputStream outputPipe = socket.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(outputPipe));
                objectOutputStream.writeObject(message);
                objectOutputStream.flush();

                success = true;
            }catch(IOException e){
                System.out.printf("Send failed: %s\n", e.getMessage());
            }
        }
        return success;
    }

    /**
     * Closes the Client socket and make a new Socket.
     * This terminates the listener thread, but also set up a new Socket
     * ready to use.
     */
    public void disconnect(){
        try {
            socket.close();
        } catch (IOException e) {

        }
    }

    public boolean isConnected(){
        return socket.isConnected();
    }

    /**
     * Starts a thread that continously checks for incoming data
     * from the connected server until the client socket is closed.
     */
    public void openInputListener(){
        Thread inputListener = new Thread(new Runnable() {
            public void run() {
                while(!socket.isClosed()) {
                    try {
                        ObjectInputStream inPipe = new ObjectInputStream(socket.getInputStream());
                        Object input = inPipe.readObject();
                        Message incomingMessage = (Message)input;
                        addMessageToInbox(incomingMessage);

                        //notify observer
                        notifyObservers(incomingMessage);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        inputListener.start();
    }

    /**
     * Add a Message to the queue.
     * @param message
     */
    private synchronized void addMessageToInbox(Message message){
        inbox.add(message);
    }

    /**
     * @return
     * Head of the message queue
     */
    public synchronized Message dequeueMessage(){
        return inbox.poll();
    }

    /**
     * Return true if there are any Message in the inbox.
     * @return
     */
    public boolean hasPendingMessage(){
        return !inbox.isEmpty();
    }



    LinkedList<Observer<Message>> observerList = new LinkedList<>();
    @Override
    public void addObserver(Observer<Message> observer) {
        observerList.add(observer);
    }

    @Override
    public void notifyObservers(Message message) {
        for(Observer<Message> observer: observerList)
            observer.beNotified(message);
    }
}
