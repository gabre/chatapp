package view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import app.ChatClientApplication;

public class MainWindow extends Window {
	private ListView<String> lvChannels, lvUsers, lvMessages;
	private TextField tfMessage;
	
	private TabPane messageTabs;

	public MainWindow(ChatClientApplication app) {
		super();
		this.app = app;
		
		lvChannels = new ListView<>();
		lvChannels.getItems().addAll("#alma", "#körte", "#csatorna1", "#ggg");
		
		lvUsers = new ListView<>();
		lvUsers.getItems().addAll("Dénes", "Gábor", "Krisztián", "Viktor");
		
		lvMessages = new ListView<>();
		lvMessages.getItems().addAll(
				"<Gábor> szia Viktor",
				"<Viktor> szia.");
		
		tfMessage = new TextField();
		
		messageTabs = new TabPane();
		Tab tab = new Tab();
		tab.setText("Gábor üzenete");
		tab.setContent(lvMessages);
		tab.setClosable(false);
		messageTabs.getTabs().add(tab);
	}

	@Override
	public String getTitle() {
		return new String("Chat Client");
	}

	@Override
	public Parent getView() {
		BorderPane mainPane = new BorderPane();
		
		messageTabs.setPrefSize(400, 400);
		mainPane.setCenter(messageTabs);
		
		VBox rightBox = new VBox(5);
		rightBox.getChildren().addAll(
				new Label("Csatornák"),
				lvChannels,
				new Label("Felhasználók"),
				lvUsers);
		rightBox.setPrefHeight(400);
		mainPane.setRight(rightBox);
		
		HBox bottomBox = new HBox(5);
		
		tfMessage.setMaxWidth(Double.MAX_VALUE);
		bottomBox.getChildren().addAll(
				tfMessage,
				new Button("Küldés"),
				new Button("Napló"));
		HBox.setHgrow(tfMessage, Priority.ALWAYS);
		mainPane.setBottom(bottomBox);
		
		BorderPane.setMargin(messageTabs, new Insets(0, 5, 0, 0));
		BorderPane.setMargin(bottomBox, new Insets(5, 0, 0, 0));
		
		mainPane.setPadding(new Insets(5));
		return mainPane;
	}

}
