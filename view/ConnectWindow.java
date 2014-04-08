package view;

import app.ChatClientApplication;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ConnectWindow extends Window {
	private TextField tfAddress;
	
	private ChatClientApplication app;
	
	public ConnectWindow(ChatClientApplication app) {
		super();
		this.app = app;
		
		tfAddress = new TextField();
	}

	@Override
	public String getTitle() {
		return "Csatlakoz�s";
	}

	@Override
	public Parent getView() {
		Button btnConnect = new Button("Csatlakoz�s");
		btnConnect.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				app.onConnection(tfAddress.getText());
			}
		});
		
		HBox box = new HBox(5);
		box.getChildren().addAll(new Label("C�m:"), tfAddress, btnConnect);
		box.setPadding(new Insets(5, 5, 5, 5));
		
		return box;
	}

}
