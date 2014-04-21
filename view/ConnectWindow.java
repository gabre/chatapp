package view;

import app.ChatClientApplication;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class ConnectWindow extends Window {
	private TextField tfAddress, tfName;
	
	private ChatClientApplication app;
	
	public ConnectWindow(ChatClientApplication app) {
		super(app);
		this.app = app;
		
		tfAddress = new TextField();
		tfName = new TextField();
	}

	@Override
	public String getTitle() {
		return "Csatlakozás";
	}

	@Override
	public Parent getView() {
		Button btnConnect = new Button("Csatlakozás");
		btnConnect.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				app.onConnection(tfAddress.getText(), tfName.getText());
			}
		});
		
		GridPane grid = new GridPane();
		grid.addRow(0, new Label("Cím:"), tfAddress);
		grid.addRow(1, new Label("Felhasználónév:"), tfName);
		grid.addRow(2,  btnConnect);
		grid.setPadding(new Insets(5, 5, 5, 5));
		
		return grid;
	}

}
