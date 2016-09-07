package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Created by Yixiu Liu on 9/4/2016.
 */
public class ClientMenu extends Pane {
    private final Label title = new Label("Client Menu");

    private TextField ipTextField = new TextField();
    private TextField portTextField = new TextField();
    private Button connectButton = new Button("Connect");

    private final Label statusTitle = new Label("Currently connected to: ");
    private final Label ipLabel = new Label("IP: ");
    private Text ipText = new Text("N/A");
    private final Label portLabel = new Label("Port: ");
    private Text portText = new Text("N/A");

    private TextField nameField = new TextField();

    public ClientMenu(){
        init();
        customizeInit();
        layoutInit();
    }

    public String getIP(){
        return ipTextField.getText().trim();
    }

    public String getPort(){
        return portTextField.getText().trim();
    }

    public void setConnectButtonHandler(EventHandler<ActionEvent> handler){
        connectButton.setOnAction(handler);
    }

    public void setIpText(String newText){
        ipText.setText(newText);
    }

    public void setPortText(String newText){
        portText.setText(newText);
    }

    public void setPortText(int newText){
        portText.setText(""+newText);
    }

    public void disableMenu(){ this.setDisable(true); }

    public void enableMenu(){ this.setDisable(false);}

    public void disableNameField(){ nameField.setDisable(true); }

    public String getNickNameText(){ return nameField.getText(); }

    private void layoutInit(){
        title.setLayoutY(36);

        ipTextField.setLayoutY(77);
        portTextField.setLayoutY(118);
        connectButton.setLayoutY(159);

        statusTitle.setLayoutY(251);
        ipLabel.setLayoutY(280);
        ipText.setLayoutY(294);
        ipText.layoutXProperty().bind(ipLabel.layoutXProperty().add(50));
        portLabel.setLayoutY(307);
        portText.setLayoutY(322);
        portText.layoutXProperty().bind(portLabel.layoutXProperty().add(50));

        nameField.setLayoutY(0);
    }

    private void customizeInit(){
        ipTextField.setPromptText("Enter ip");
        portTextField.setPromptText("Enter port");
        nameField.setPromptText("Enter your name");

        /////////////////////////////////////////////////REMOVE TO CSS LATER
        ipText.setFill(Color.WHITE);
        portText.setFill(Color.WHITE);
    }

    private void init(){
        this.getChildren().add(title);
        this.getChildren().add(connectButton);
        this.getChildren().add(ipTextField);
        this.getChildren().add(portTextField);
        this.getChildren().add(statusTitle);
        this.getChildren().add(ipLabel);
        this.getChildren().add(ipText);
        this.getChildren().add(portLabel);
        this.getChildren().add(portText);

        this.getChildren().add(nameField);
    }

}
