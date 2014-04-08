package app;
 
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.ConnectWindow;
import view.MainWindow;
import view.ModalWindow;
import view.Window;
 
public class ChatClientApplication extends Application {
	// Model
	
	// View
	private MainWindow mainWin;
	private Stage dialogStage;
	
	private ConnectWindow connectWin;
	private Stage connectStage;
	
	private Stage mainStage;
	

	
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
        connectWin = new ConnectWindow(this);
        
        mainStage = createWin(stage, mainWin);
        connectStage = createWin(connectWin);

        connectStage.show();
        //stage.show();
        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
			}
        });
    }
    
    // This method is used to "create windows" or something like that...
    // The first stage is created "by the platform" (according to the docs)
    public static Stage createWin(Window win) {
    	Stage stage = new Stage();
    	createWin(stage, win);
    	return stage;
    }
    
    public static Stage createWin(Stage stage, Window win) {
    	Scene scene = new Scene(win.getView());
        // Stylesheet could be added this way:
        stage.setScene(scene);
        stage.setTitle(win.getTitle());
        return stage;
    }
    
    // I think this creates a circle in the dependency graph which is a bad practice according to Clean Code
    //
	public void closeModal() {
		dialogStage.close();
	}

	public void onConnection(String text) {
		mainStage.show();
		connectStage.hide();
	}

}