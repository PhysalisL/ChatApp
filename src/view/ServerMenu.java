package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * Created by Yixiu Liu on 9/4/2016.
 */
public class ServerMenu extends Pane {
    private final Label title = new Label("Server Menu");
    private final Label serverInfoTitle = new Label("Server Info");

    private Button startServerButton = new Button("Setup Server");

    private final Label ipLabel = new Label("Server IP: ");
    private TextField ipText = new TextField("N/A");
    private final Label portLabel = new Label("Server Port: ");
    private TextField portText = new TextField("N/A");

    public ServerMenu(){
        init();
        customizeInit();
        layoutInit();
    }

    public void setStartServerButtonHandler(EventHandler<ActionEvent> handler){
        startServerButton.setOnAction(handler);
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

    public void disableStartServerButton(){
        startServerButton.setDisable(true);
    }

    public void enableStartServerButton(){
        startServerButton.setDisable(false);
    }

    private void layoutInit(){
        title.setLayoutY(36);

        startServerButton.setLayoutY(159);

        serverInfoTitle.setLayoutY(251);
        ipLabel.setLayoutY(280);
        ipText.layoutXProperty().bind(ipLabel.layoutXProperty().add(100));
        ipText.layoutYProperty().bind(ipLabel.layoutYProperty());
        portLabel.setLayoutY(307);
        portText.layoutXProperty().bind(portLabel.layoutXProperty().add(100));
        portText.layoutYProperty().bind(portLabel.layoutYProperty());
        portLabel.layoutYProperty().bind(ipLabel.layoutYProperty().add(50));

    }

    private void init(){
        //ipText.setDisable(true);
        //portText.setDisable(true);
        this.getChildren().add(title);
        this.getChildren().add(startServerButton);
        this.getChildren().add(ipLabel);
        this.getChildren().add(ipText);
        this.getChildren().add(portLabel);
        this.getChildren().add(portText);
        this.getChildren().add(serverInfoTitle);
    }

    private void customizeInit(){

    }
}
