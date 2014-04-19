package view;

import app.ChatClientApplication;
import javafx.scene.Parent;

public abstract class Window {
	protected ChatClientApplication app;
	
	public abstract String getTitle();
	public abstract Parent getView();
	
	Window(ChatClientApplication app) {
		this.app = app;
	}
}
