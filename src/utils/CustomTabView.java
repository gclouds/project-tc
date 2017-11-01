package utils;

import java.util.Map;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import truechip.TransactionLogger;

public class CustomTabView extends Tab {
	BorderPane mainPane =  new BorderPane();
	ToolBar topToolBar= new ToolBar();
	String logFilePath;
	ComboBox<String> fieldsList= new ComboBox<>();
	TextField searchTxt = new TextField();
	public CustomTabView(String logFilePath) throws Exception {
		super();
		this.logFilePath = logFilePath;
		Button refreshButton = new Button();
		refreshButton.setTooltip(new Tooltip("Reload"));
		Image image = new Image("resources/refresh2.gif",15,15,true,true);
		refreshButton.setGraphic(new ImageView(image));
		refreshButton.setOnMouseClicked(this::refresh);
		ToolBar tool= new ToolBar(refreshButton,new Label("Search:"),fieldsList,searchTxt);
		mainPane.setTop(tool);
		init();
		this.setContent(mainPane);
	}
	
	private void init() throws Exception {
		TransactionLogger logger = new TransactionLogger(logFilePath);
		TreeTableView<Map<String, Object>> centerListView = logger.getCenterListView();
		TableView secondTable = logger.getSecondTable();
		
		SplitPane centerContentPane = addTables(centerListView, secondTable);
		mainPane.setCenter(centerContentPane);
		searchTxt.setOnKeyPressed(event ->logger.search(event));
		centerListView.setOnMouseClicked(event->logger.listClicked(event));
		logger.setFieldsListValues(fieldsList);
		mainPane.setCenter(centerContentPane);
	}
	
	private void refresh(MouseEvent event) {
		try {
			init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private StackPane addCenterTable(TreeTableView<Map<String, Object>> centerListView) {
		StackPane mainTablePane = new StackPane();
		mainTablePane.getChildren().add(centerListView);
		return mainTablePane;
	}
	private StackPane addDetailedTable(TableView secondTable) {
		StackPane detailedTablePane = new StackPane();
		detailedTablePane.getChildren().add(secondTable);
		return detailedTablePane;
	}
	
	
	private SplitPane addTables(TreeTableView<Map<String, Object>> centerListView,TableView secondTable) {
		SplitPane centerPane = new SplitPane();
		StackPane centerTable = addCenterTable(centerListView);
		centerPane.getItems().add(centerTable);
		if(secondTable !=null){
			secondTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			StackPane detailedTable = addDetailedTable(secondTable);
			centerPane.getItems().add(detailedTable);
			centerPane.setDividerPositions(0.7f, 0.3f);
		}else{
			centerPane.setDividerPositions(1f);
		}
		return centerPane;
	}
	
}
