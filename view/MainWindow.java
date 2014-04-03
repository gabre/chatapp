package view;

import app.ChatClientApplication;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class MainWindow extends Window {
		
	private TextField newProjectNameField;
	private StackPane centerPane;

	public MainWindow(ChatClientApplication app) {
		super();
		this.app = app;
	}

	@Override
	public String getTitle() {
		return new String("Project Manager / Calendar");
	}

	@Override
	public Parent getView() {
		BorderPane mainPane = new BorderPane();

		centerPane = new StackPane();
		centerPane.setPrefSize(640, 360);
		
		mainPane.setCenter(centerPane);
		
		BorderPane.setMargin(centerPane, new Insets(0, 0, 0, 5));
		
		mainPane.setPadding(new Insets(5));
		return mainPane;
	}

}
