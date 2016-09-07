package view;

import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import observerpattern.Observer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import model.Message;

/**
 * Created by Yixiu Liu on 9/2/2016.
 */
public class ChatMenu extends Pane implements Observer<Message> {
    private static final double DEFAULT_WIDTH = 320;
    private static final double DEFAULT_HEIGHT = 400;

    private Button sendButton = new Button();
    private TextArea writingBox = new TextArea();
    private ListView clientList = new ListView();

    private ListView<Text> messageBox = new ListView<>();
    private ObservableList<Text> observableList = FXCollections.observableArrayList();

    public ChatMenu() {
        init();
        customizeInit();
        layoutInit();
    }

    /**
     * Converts the <code>Message</code> object to a <code>Text</code> object then
     * add it to the list which is observed by the GUI's listview.
     * Since this method updates the GUI thread, <code>Platform.runLater()</code>
     * is used to make the update in case that this method is called from
     * another thread.
     *
     * @param message
     */
    public void addMessageToViewlist(Message message) {
        Platform.runLater(() -> {
            Text text = new Text(message.toString());
            text.setWrappingWidth(messageBox.getWidth()-10);

            text.setFill(Color.WHITE);//REMOVE TO CSS LATER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

            observableList.add(text);
        });

        /*//This works but above is shorter
        Task viewlistUpdate = new Task() {
            @Override
            protected Object call() throws Exception {
                return null;
            }
        };
        viewlistUpdate.setOnSucceeded(event->{
            observableList.add(message);
        });
        new Thread(viewlistUpdate).start();
        */
    }

    public String getWriting() {
        return writingBox.getText();
    }

    public void clearWritingBox() {
        writingBox.clear();
    }

    public void disableMenu(){ this.setDisable(true); }

    public void enableMenu(){ this.setDisable(false);}

    public void scrollToBottom(){
        messageBox.scrollTo(messageBox.getItems().size()-1);
    }

    public void setSendButtonHandler(EventHandler<ActionEvent> handler) {
        sendButton.setOnAction(handler);
    }

    public void setWritingBoxKeyPressHandler(EventHandler<KeyEvent> handler) {
        writingBox.setOnKeyPressed(handler);
    }

    private void init() {
        messageBox.setItems(observableList);

        this.getChildren().add(messageBox);
        this.getChildren().add(sendButton);
        this.getChildren().add(writingBox);
        //this.getChildren().add(clientList);
    }

    private void customizeInit(){
        sendButton.setText("Send");
        writingBox.setPromptText("Enter a message...");
        writingBox.setWrapText(true);
    }

    private void layoutInit() {
        this.setPrefWidth(DEFAULT_WIDTH);
        this.setPrefHeight(DEFAULT_HEIGHT);

        sendButton.setLayoutX(220);
        sendButton.setLayoutY(250);
        sendButton.setPrefHeight(50);

        writingBox.setPrefWidth(200);
        writingBox.setPrefHeight(100);
        writingBox.setLayoutX(10);
        writingBox.setLayoutY(250);

        messageBox.setPrefWidth(300);
        messageBox.setPrefHeight(200);
        messageBox.setLayoutX(10);
        messageBox.setLayoutY(10);

        clientList.setPrefWidth(250);
        clientList.setPrefHeight(380);
        clientList.setLayoutX(480);
        clientList.setLayoutY(10);
    }

    @Override
    public void beNotified(Message message) {
        addMessageToViewlist(message);
    }
}