package app;
 
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.MainWindow;
import view.ModalWindow;
import view.Window;
 
public class ChatClientApplication extends Application {
	// Model
	
	// View
	private MainWindow mainWin;
	private Stage dialogStage;
	

	
	// ENTRY POINT
    public static void main(String[] args) {
        launch(args);
    }

	@Override
    public void start(Stage stage) {
		/*
		try {
	        model = new Model();
		} catch (Exception ex) {
			System.exit(1);
		}*/

        mainWin = new MainWindow(this);
        
        createWin(stage, mainWin);

        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
			}
        });
    }
    

    // This method is used to "create windows" or something like that...
    // The first stage is created "by the platform" (according to the docs)
    private Stage createWin(Window win) {
    	Stage stage = new Stage();
    	createWin(stage, win);
    	return stage;
    }
    
    private Stage createWin(Stage stage, Window win) {
    	Scene scene = new Scene(win.getView());
        // Stylesheet could be added this way:
    	scene.getStylesheets().add("styles.css");
        stage.setScene(scene);
        stage.setTitle(win.getTitle());
        return stage;
    }
    
    // I think this creates a circle in the dependency graph which is a bad practice according to Clean Code
    //
	public void closeModal() {
		dialogStage.close();
	}

}