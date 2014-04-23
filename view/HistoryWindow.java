package view;

import java.util.Map.Entry;

import model.Message;
import model.Statistics;
import app.ChatClientApplication;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class HistoryWindow extends Window {
	private ListView<Message> listviewLogs;
	private ListView<Statistics> stats;
	private VBox statsTab;

	private XYChart barChart;
	
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
		
		listviewLogs = new ListView<>(app.getModel().getMessages());
		VBox logsTab = new VBox(5);
		HBox controls = new HBox(5);
		Button deleteBtn = new Button("Delete entry");
		Button clearBtn = new Button("Clear");
		deleteBtn.setMinSize(50, 20);
		clearBtn.setMinSize(50, 20);
		
		ToggleGroup filtering = new ToggleGroup();
		RadioButton rb1 = new RadioButton("Private messages");
		rb1.setToggleGroup(filtering);
		rb1.setSelected(true);
		RadioButton rb2 = new RadioButton("Room messages");
		rb2.setToggleGroup(filtering);
		RadioButton rb3 = new RadioButton("All");
		rb3.setToggleGroup(filtering);
		
		deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				app.getModel();
				ObservableList<Message> selectedOne = listviewLogs.getSelectionModel()
						.getSelectedItems();
				if (selectedOne.size() == 0) {
					app.showMessage("Please, select an item to delete.");
				} else {
					Message selectedMessage = (Message) selectedOne.get(0);
					app.getModel().deleteChatEvent(selectedMessage);
				}
			}
		});
		clearBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				app.getModel().deleteChatEvents();
			}
		});
		rb1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				app.getModel().filterPrivates();
			}
		});
		rb2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				app.getModel().filterRoomMessages();
			}
		});
		rb3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				app.getModel().applyNoFilter();
			}
		});
		
		controls.getChildren().addAll(deleteBtn, clearBtn, rb1, rb2, rb3);
		logsTab.getChildren().addAll(listviewLogs, controls);
		statsTab = new VBox(5);
		stats = new ListView<Statistics>(app.getModel().getStatistics());
		
		Axis<String> xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        barChart = new BarChart<String,Number>(xAxis,yAxis);
        barChart.setTitle("Best friends");

		
		statsTab.getChildren().addAll(stats, barChart);
		
		Tab t1 = new Tab("Üzenetek");
		t1.setContent(logsTab);
		Tab t2 = new Tab("Statisztikák");
		t2.setContent(statsTab);
		
		tabPane.getTabs().addAll(t1, t2);
		
		StackPane mainPane = new StackPane();
		mainPane.getChildren().add(tabPane);
		mainPane.paddingProperty().set(new Insets(5, 5, 5, 5));
	
		return mainPane;
	}
	
	public void refreshFriendsChart() {
		XYChart.Series series = new XYChart.Series();
	    series.setName("Best friends");
	    ObservableList<Entry<String, Integer>> bestFriends = app.getModel().getBestFriends();
	    for(Entry<String, Integer> entry : bestFriends) {
	    	series.getData().add(new XYChart.Data<>(entry.getValue(), entry.getKey()));
	    }
	    barChart.getData().add(series);
	}

}
