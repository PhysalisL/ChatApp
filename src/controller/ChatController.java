package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Client;
import model.Message;
import model.Server;
import view.ChatMenu;
import view.ClientMenu;
import view.ServerMenu;

/**
 * Created by Yixiu Liu on 9/2/2016.
 */
public class ChatController extends Application{
    private final double WIDTH = 500;
    private final double HEIGHT = 500;

    public void start(Stage primaryStage) throws Exception {
        Accordion accordion = new Accordion();

        //chat
        ChatMenu chatMenu = new ChatMenu();
        chatMenu.addMessageToViewlist(new Message("System", "Hello and welcome to ChatApp!"));
        chatMenu.addMessageToViewlist(new Message("System", "Please proceed to Client or Server menu to begin!"));
        TitledPane chatTitledPane = new TitledPane();
        chatTitledPane.setContent(chatMenu);
        chatTitledPane.setText("Chat Menu");
        //chatTitledPane.setAnimated(false);

        //client
        ClientMenu clientMenu = new ClientMenu();
        TitledPane clientTitledPane = new TitledPane();
        clientTitledPane.setContent(clientMenu);
        clientTitledPane.setText("Client Menu");
        //clientTitledPane.setAnimated(false);

        //server
        ServerMenu serverMenu = new ServerMenu();
        TitledPane serverTitledPane = new TitledPane();
        serverTitledPane.setContent(serverMenu);
        serverTitledPane.setText("Server Menu");
        //serverTitledPane.setAnimated(false);

        //add to accordion
        accordion.getPanes().add(chatTitledPane);
        accordion.setExpandedPane(chatTitledPane);
        accordion.getPanes().add(clientTitledPane);
        accordion.getPanes().add(serverTitledPane);

        //set scene
        //Scene scene = new Scene(accordion, WIDTH, HEIGHT);
        Scene scene = new Scene(accordion);
        scene.getStylesheets().add("assets/AppStylesheet.css");

        //set models
        Server server = new Server();
        Client client = new Client();
        client.addObserver(chatMenu);

        //controls
        serverMenu.setStartServerButtonHandler(event->{
            server.startUp();
            serverMenu.setIpText(server.getServerIP());
            serverMenu.setPortText(server.getPort());
            serverMenu.disableStartServerButton();
        });
        clientMenu.setConnectButtonHandler(event->{
            try {
                String ip = clientMenu.getIP();
                int port = Integer.parseInt(clientMenu.getPort());
                boolean success = client.connect(ip, port);

                if(success) {
                    client.setNickname(clientMenu.getNickNameText());////////////////////////////////////////////
                    clientMenu.setIpText(client.getConnectedIP());
                    clientMenu.setPortText(client.getPort());
                    clientMenu.disableMenu();
                }
            }catch (NumberFormatException e){
                //type connection failed or something
                System.out.println("NUMBER FORMAT!");
            }

        });
        chatMenu.setSendButtonHandler(event->{
            boolean success = client.send(new Message(chatMenu.getWriting()));
            if(!success) chatMenu.addMessageToViewlist(new Message("System", "Send failed, you are not connected to a server yet!"));
            chatMenu.clearWritingBox();
            chatMenu.scrollToBottom();
        });
        chatMenu.setWritingBoxKeyPressHandler(event->{
            if(event.getCode() == KeyCode.ENTER){
                boolean success = client.send(new Message(chatMenu.getWriting()));
                if(!success) chatMenu.addMessageToViewlist(new Message("System", "Send failed"));
                chatMenu.clearWritingBox();
                chatMenu.scrollToBottom();
                event.consume();
            }
        });

        primaryStage.setOnCloseRequest(event->{
            client.disconnect();
            server.disconnectAll();
        });

        //set stage
        primaryStage.setTitle("Chat App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[]args){
        launch(args);
    }
}
