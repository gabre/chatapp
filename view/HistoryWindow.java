package view;

import java.util.Map.Entry;

import model.Message;
import model.PrivateMessage;
import model.RoomMessage;
import model.Statistics;
import app.ChatClientApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class HistoryWindow extends Window {
	private static final int MAXIMAL_NUMBER_OF_BARS = 5;
	private ListView<Message> listviewLogs;
	private ListView<Statistics> stats;
	private ObservableList<Message> processedMessages = FXCollections.observableArrayList();
	private VBox statsTab;

	private XYChart<String, Number> barChart;
	
	public HistoryWindow(ChatClientApplication app) {
		super(app);
	}

	@Override
	public String getTitle() {
		return "Napló";
	}

	@Override
	public Parent getView() {
		TabPane tabPane = new TabPane();
		tabPane.tabClosingPolicyProperty().setValue(TabClosingPolicy.UNAVAILABLE);
		
		listviewLogs = new ListView<Message>(processedMessages);
		VBox logsTab = new VBox(5);
		HBox controls = new HBox(5);
		Button deleteBtn = new Button("Elem törlése");
		Button clearBtn = new Button("Összes törlése");
		deleteBtn.setMinSize(50, 20);
		clearBtn.setMinSize(50, 20);
		
		ToggleGroup filtering = new ToggleGroup();
		RadioButton rb1 = new RadioButton("Privát üzenetek");
		rb1.setToggleGroup(filtering);
		RadioButton rb2 = new RadioButton("Szobaüzenetek");
		rb2.setToggleGroup(filtering);
		RadioButton rb3 = new RadioButton("Összes");
		rb3.setToggleGroup(filtering);
		rb3.setSelected(true);
		
		deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				app.getModel();
				ObservableList<Message> selectedOne = listviewLogs.getSelectionModel()
						.getSelectedItems();
				if (selectedOne.size() == 0) {
					app.showMessage("Kérlek, válassz ki egy elemet a törléshez.");
				} else {
					Message selectedMessage = selectedOne.get(0);
					processedMessages.remove(selectedMessage);
					app.getModel().deleteChatMessage(selectedMessage);
				}
			}
		});
		clearBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				processedMessages.clear();
				app.getModel().deleteChatMessages();
			}
		});
		rb1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				processedMessages.clear();
				for (Message m : app.getModel().getMessages()) {
					if (m instanceof PrivateMessage) {
						processedMessages.add(m);
					}
				}
			}
		});
		rb2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				processedMessages.clear();
				for (Message m : app.getModel().getMessages()) {
					if (m instanceof RoomMessage) {
						processedMessages.add(m);
					}
				}
			}
		});
		rb3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				refreshLog();
			}
		});
		
		controls.getChildren().addAll(deleteBtn, clearBtn, rb1, rb2, rb3);
		logsTab.getChildren().addAll(listviewLogs, controls);
		VBox.setVgrow(listviewLogs, Priority.ALWAYS);
		statsTab = new VBox(5);
		stats = new ListView<Statistics>(app.getModel().getStatistics());
		
		Axis<String> xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        barChart = new BarChart<String, Number>(xAxis,yAxis);
        barChart.setTitle("Legjobb barátok");

		
		statsTab.getChildren().addAll(stats, barChart);
		
		Tab t1 = new Tab("Üzenetek");
		t1.setContent(logsTab);
		Tab t2 = new Tab("Statisztikák");
		t2.setContent(statsTab);
		
		tabPane.getTabs().addAll(t1, t2);
		
		StackPane mainPane = new StackPane();
		mainPane.getChildren().add(tabPane);
		mainPane.paddingProperty().set(new Insets(5, 5, 5, 5));
		mainPane.setPrefHeight(500);
		
		return mainPane;
	}
	
	public void refreshLog() {
		processedMessages.clear();
		processedMessages.addAll(app.getModel().getMessages());
	}
	
	public void refreshFriendsChart() {
		barChart.getData().clear();
		XYChart.Series<String, Number> series = new XYChart.Series<>();
	    series.setName("Legjobb barátok");
	    ObservableList<Entry<String, Integer>> bestFriends = app.getModel().getBestFriends();
	    int i = 0;
	    while(i < bestFriends.size() && i < MAXIMAL_NUMBER_OF_BARS) {
	    	Entry<String, Integer> entry = bestFriends.get(i);
	    	series.getData().add(new XYChart.Data<>(entry.getKey(), (Number)entry.getValue()));
	    	++ i;
	    }
	    barChart.getData().add(series);
	}

}
