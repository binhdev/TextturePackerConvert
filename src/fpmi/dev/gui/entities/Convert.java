package fpmi.dev.gui.entities;

import fpmi.dev.gui.MainApp;

public class Convert {
	public static AndEnginePacker convertFromLibGdxPackerItem(MainApp mainApp,LibGDXPacker gdxPacker) {
		AndEnginePacker andEnginePacker = new AndEnginePacker(mainApp);
		andEnginePacker.file = gdxPacker.file;
		for(int i = 0; i < gdxPacker.listItem.size(); i++) {
			LibGDXItem gdxItem = gdxPacker.listItem.get(i);
			AndEngineItem andEngineItem = new AndEngineItem();
			andEngineItem.setId(String.valueOf(i));	
			andEngineItem.setSrc(gdxItem.file);	
			andEngineItem.setXY(gdxItem.xy);	
			andEngineItem.setSize(gdxItem.size);	
			andEngineItem.setOffset(gdxItem.offset);	
			andEngineItem.setRotated(gdxItem.rotate);	
			andEnginePacker.listItem.add(andEngineItem);
		}
		return andEnginePacker;
	}
}
