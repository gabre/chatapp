package app;
 
import java.io.IOException;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Connection;
import model.Message;
import model.MessageVisitor;
import model.Model;
import model.PrivateMessage;
import model.RoomMessage;
import view.ConnectWindow;
import view.HistoryWindow;
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
	
	private HistoryWindow historyWin;
	private Stage historyStage;
	
	private Stage mainStage;
	private Model model;
	private Connection conn;
	
	// ENTRY POINT
    public static void main(String[] args) {
        launch(args);
    }

	@Override
    public void start(Stage stage) {
		// this part could be moved to the connecting part
		// 
		model = new Model();
		
        mainWin = new MainWindow(this);
        connectWin = new ConnectWindow(this);
        historyWin = new HistoryWindow(this);
        
        mainStage = createWin(stage, mainWin);
        historyStage = createWin(historyWin);
        connectStage = createWin(connectWin);

        connectStage.show();
        //stage.show();
        EventHandler<WindowEvent> exitAction = new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				if (conn != null) {
					conn.close();
				}
				System.exit(0);
			}
        };
        mainStage.setOnCloseRequest(exitAction);
        connectStage.setOnCloseRequest(exitAction);
        
        model.getMessages().addListener(new ListChangeListener<Message>() {
			@Override
			public void onChanged(ListChangeListener.Change<? extends Message> change) {
				while (change.next()) {
					if (change.getAddedSize() > 0) {
						for (Message msg : change.getAddedSubList()) {
							mainWin.onMessage(msg);
						}
					}
				}
			}
        });
    }
    
    public static Stage createWin(Window win) {
    	Stage stage = new Stage();
    	createWin(stage, win);
    	return stage;
    }
    
    public static Stage createWin(Stage stage, Window win) {
    	Scene scene = new Scene(win.getView());
        stage.setScene(scene);
        stage.setTitle(win.getTitle());
        return stage;
    }
    
	public void closeModal() {
		dialogStage.close();
	}

	public void onConnection(String address, String name) {
		try {
			conn = new Connection(address, 12345, name);
			new Thread(new ConnectionListener(name, conn, this)).start();
			model.startConnection(name);
			mainWin.setDisplayedUsername(model.getUserInfo().getUserName());
			mainStage.show();
			connectStage.hide();
		} catch (IOException e) {
			dialogStage = createWin(new ModalWindow("Hiba", "Nem siker�lt csatlakozni", this));
			dialogStage.show();
		}
	}

	public void onHistoryWindowOpened() {
		model.calculateStatistics();
		historyWin.refreshLog();
		historyWin.refreshFriendsChart();
		historyStage.show();	
	}
	
	public void onConnectionLost() {
		dialogStage = createWin(new ModalWindow("Hiba", "Megszakadt a kapcsolat.", this));
		dialogStage.show();
		mainStage.hide();
		connectStage.show();
		model.reset();
	}
	
	public void onServerError(String msg, boolean fatal) {
		dialogStage = createWin(new ModalWindow("Hiba", msg, this));
		dialogStage.show();
		if (fatal) {
			conn.close();
			mainStage.hide();
			connectStage.show();
			model.reset();
		}
	}

	public Model getModel() {
		return model;
	}

	public Connection getConn() {
		return conn;
	}

	public void send(Message msg) {
		msg.acceptVisitor(new MessageVisitor() {
			@Override
			public void visit(PrivateMessage message) {
				conn.sendPrivMsg(message.getToUserName(), message.getMessage());
				model.getMessages().add(message);
			}
			@Override
			public void visit(RoomMessage message) {
				conn.sendChanMessage(message.getRoomName(), message.getMessage());
				model.getMessages().add(message);
			}
		});
	}
	
	public void showMessage(String message)
	{
		ModalWindow mWin = new ModalWindow("Info", message, this);
		dialogStage = createWin(mWin);
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.showAndWait();
	}

}