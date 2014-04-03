package view;

import app.ChatClientApplication;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;

public class ModalWindow extends Window {

	private String text;
	private String title;
	
	public ModalWindow(String title, String text, ChatClientApplication app) {
		super();
		this.title = title;
		this.text = text;
		this.app = app;
	}
	
	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public Parent getView() {
		Button ok = new Button("Ok.");
		ok.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				closeModal();
			}
		});
		return VBoxBuilder.create().
			    children(new Text(text), ViewUtils.getVPlaceHolder(10), ok).
			    alignment(Pos.CENTER).padding(new Insets(5)).build();
	}

	private void closeModal() {
		app.closeModal();		
	}

}
