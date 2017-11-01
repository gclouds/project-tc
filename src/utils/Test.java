package utils;

import java.awt.image.SampleModel;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import constants.ItemType;
import domain.TreeItem;
import domain.TrueChipLog;
import jdk.nashorn.internal.parser.JSONParser;

public class Test {

	public static void main(String[] args) {
		TrueChipLog sample = new TrueChipLog();
		List prim= new ArrayList<>();
		List seco= new ArrayList<>();
		prim.add("Direction");
		prim.add("Time");
		prim.add("Payload");
		seco.add("Packet Number");
		seco.add("Type");
		seco.add("Fmt");
		sample.setPrimaryTable(prim);
		sample.setSecondaryTable(seco);
		List<TreeItem> root= new ArrayList<>();
		TreeItem tree= new TreeItem();
		tree.setChild(null);
		tree.setType(ItemType.DEC);
		tree.setValue(1);
		tree.setKey("Packet Number");
		TreeItem child= new TreeItem();
		tree.setType(ItemType.NANO);
		tree.setValue(433687200822L);
		tree.setKey("Time");
		
		root.add(tree);
		root.add(child);
		//sample.setRoot(root);
		Gson gson = new Gson();
		String json = gson.toJson(sample);
		System.out.println(json);
	}

}
