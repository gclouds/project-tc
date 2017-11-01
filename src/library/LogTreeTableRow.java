package library;

import java.util.Map;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableRow;
import utils.StringComparator;

public class LogTreeTableRow extends TreeTableRow<Map<String, Object>> {

	@Override
	protected void updateItem(Map<String, Object> item, boolean empty) {
		
		if (empty) {
        } else {
        	setContextMenu(((LogTreeItem)getTreeItem()).getContextMenu());
        	if(findErrors((LogTreeItem)getTreeItem())) {
    			setStyle("-fx-control-inner-background: #ff3333");
    		}else {
    			// clear background
                setStyle(null);
    		}
        }
		
		super.updateItem(item, empty);
	}
	
	
	boolean findErrors(LogTreeItem item){
		boolean temp = false;
		boolean containsKey = item.getValue().containsKey("ERROR_ID");
		int length = StringComparator.escapeNullToString(item.getValue().get("ERROR_ID")).length();
		if(containsKey && length>1) {
			temp =true;
		}else if(!item.isLeaf()) {
			for(TreeItem childItem:item.getChildren()) {
				if(findErrors((LogTreeItem)childItem)) temp=true;
			}
		}
		return temp;
	}
}
