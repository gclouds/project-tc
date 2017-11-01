package truechip;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import library.LogTreeItem;
import logger.DataList;
import logger.TCLogger;
import models.DetailedLogs;
import utils.ReadLogger;

/**
 * @author gauravsi
 *
 */
public class TransactionLogger {
	public final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TransactionLogger.class);
	List<String> listOfKeysDetailed;
	String logFilePath;
	private TableView secondTable;
	private TreeTableView<Map<String, Object>> centerListView;
	BorderPane listView = null;
	LogTreeItem rootItem;
	Collection<String> logAttributeKeys;
	ComboBox<String> fieldsList;

	public TransactionLogger(String logFilePath) throws Exception {
		this.logFilePath = logFilePath;
		initLog();
	}

	@FXML
	public void listClicked(MouseEvent event) {
		TreeItem selectedItem = (TreeItem) centerListView.getSelectionModel().getSelectedItem();
		ObservableList selectedIndex = centerListView.getSelectionModel().getSelectedCells();
		if (selectedIndex.size() > 0) {
			showSecondaryTable((Map<String, Object>) selectedItem.getValue());
		}
	}

	public void showSecondaryTable(Map<String, Object> value) {
		try {
			secondTable.getItems().clear();
			for (Entry<String, Object> keySet : value.entrySet()) {
				if (listOfKeysDetailed.contains(keySet.getKey())) {
					DetailedLogs detailedLogs = new DetailedLogs(keySet.getKey(), (String) keySet.getValue());
					secondTable.getItems().add(detailedLogs);
				}
			}
			secondTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		} catch (Exception e) {
			log.error(e);
		}
	}

	public TreeItem<Map<String, Object>> addChildren(LogTreeItem parent, DataList dataList) {
		// TreeItem<GenericLogModel> childTreeItem = new TreeItem<>(model);
		LogTreeItem childTreeItem = new LogTreeItem(dataList.getData(), this);
		//
		if (dataList.hasChild()) {
			for (DataList childDataList : dataList.getChild()) {
				childTreeItem.getChildren().add(addChildren(childTreeItem, childDataList));
			}
		}
		return childTreeItem;
	}

	private void initLog() throws Exception {
		try {
			File tempFile = new File(logFilePath);
			ReadLogger logger = new ReadLogger(tempFile);
			log.info("truechip.TransactionLogger.selectFile() map:");
			List<String[]> extractedData = TCLogger.extractData(tempFile);
			TCLogger logData = new TCLogger(extractedData);
			List<DataList> tableData = logData.getLeftSideTableData();
			rootItem = new LogTreeItem();
			for (int i = 0; i < tableData.size(); i++) {
				log.info("addding tableData: " + tableData.get(i).getData());
				DataList dataList = tableData.get(i);
				Map<String, Object> model = tableData.get(i).getData();
				TreeItem<Map<String, Object>> subItem = addChildren(rootItem, dataList);
				rootItem.getChildren().add(subItem);
			}
			centerListView = logger.getTLeftHandSideTable();
			centerListView.setRoot(rootItem);
			centerListView.setShowRoot(false);
			centerListView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
			centerListView.refresh();
			// setting second table
			secondTable = logger.getDetailedTable();
			listOfKeysDetailed = logger.getListOfKeysDetailed();
			logAttributeKeys = logData.getHeaderMap().values();
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	public void search(KeyEvent event) {
		String filter = ((TextField) event.getSource()).getText();
		if (filter.isEmpty()) {
			centerListView.setRoot(rootItem);
		} else {
			LogTreeItem filteredRoot = new LogTreeItem();

			if (fieldsList.getValue() != null) {
				log.info("seariching in field: " + fieldsList.getValue().toString());
				filter(rootItem, fieldsList.getValue().toString(), filter, filteredRoot);
			} else {
				filter(rootItem, filter, filteredRoot);
			}
			centerListView.setRoot(filteredRoot);
		}

	}

	private void filter(LogTreeItem root, String filter, LogTreeItem filteredRoot) {
		for (TreeItem<Map<String, Object>> child : root.getChildren()) {
			LogTreeItem filteredChild = new LogTreeItem(child.getValue(), this);
			filteredChild.setExpanded(true);
			filter((LogTreeItem) child, filter, filteredChild);
			if (!filteredChild.getChildren().isEmpty() || isMatch(filteredChild.getValue(), filter)) {
				log.info(filteredChild.getValue() + " matches.");
				filteredRoot.getChildren().add(filteredChild);
			}
		}
	}

	private void filter(LogTreeItem root, String field, String filter, LogTreeItem filteredRoot) {
		for (TreeItem<Map<String, Object>> child : root.getChildren()) {
			LogTreeItem filteredChild = new LogTreeItem(child.getValue(), this);
			filteredChild.setExpanded(true);
			filter((LogTreeItem) child, filter, filteredChild);
			if (!filteredChild.getChildren().isEmpty() || isMatch(filteredChild.getValue(), filter, field)) {
				log.info(filteredChild.getValue() + " matches.");
				filteredRoot.getChildren().add(filteredChild);
			}
		}
	}

	private boolean isMatch(Map<String, Object> value, String filter) {
		log.info(value);
		return value.values().stream().anyMatch((Object o) -> o.toString().contains(filter));
	}

	private boolean isMatch(Map<String, Object> value, String filter, String key) {
		try {
			String text = value.get(key).toString();
			if (text.contains(filter))
				return true;
		} catch (Exception e) {
			log.error(e);
		}
		return false;
	}

	public TableView getSecondTable() {
		return secondTable;
	}

	public TreeTableView<Map<String, Object>> getCenterListView() {
		return centerListView;
	}

	public void setFieldsListValues(ComboBox<String> fieldsList) {
		fieldsList.getItems().addAll(logAttributeKeys);
		this.fieldsList = fieldsList;
	}
}
