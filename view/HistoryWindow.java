package view;

import app.ChatClientApplication;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class HistoryWindow extends Window {
	private ListView<String> lvLogs, lvMessages;

	public HistoryWindow(ChatClientApplication app) {
		super();
		
		lvLogs = new ListView<>();
		lvLogs.getItems().addAll("Besz�lget�s: G�bor");
		
		lvMessages = new ListView<>();
		lvMessages.getItems().addAll("<G�bor> ggg ggg ggg gggg",
				"<Viktor> kkk k kkkkkk kk");
	}

	@Override
	public String getTitle() {
		return "Napl�";
	}

	@Override
	public Parent getView() {
		TabPane tabPane = new TabPane();
		
		HBox logsTab = new HBox(5);
		VBox leftBox = new VBox(5);
		leftBox.getChildren().addAll(lvLogs, new Button("T�rl�s"));
		logsTab.getChildren().addAll(leftBox, lvMessages);
		
		GridPane statsTab = new GridPane();
		statsTab.setHgap(10);
		statsTab.setVgap(5);
		statsTab.addRow(0,
				new Label("Besz�lget�sek:"),
				new Label("1"));
		statsTab.addRow(1,
				new Label("�zenetek:"),
				new Label("2"));
		statsTab.addRow(2,
				new Label("Legt�bb besz�lget�s:"),
				new Label("Viktor"));
		
		Tab t1 = new Tab("�zenetek");
		t1.setContent(logsTab);
		Tab t2 = new Tab("Statisztik�k");
		t2.setContent(statsTab);
		
		tabPane.getTabs().addAll(t1, t2);
		
		StackPane mainPane = new StackPane();
		mainPane.getChildren().add(tabPane);
		mainPane.paddingProperty().set(new Insets(5, 5, 5, 5));
	
		return mainPane;
	}

}