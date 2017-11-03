package fpmi.dev.gui.entities;

public class AndEngineItem {
	public String id;
	public String src;
	public String x;
	public String y;
	public String width;
	public String height;
	public String rotated;
	public String trimmed = "false";
	public String srcx = "0";
	public String srcy = "0";
	public String srcwidth;
	public String srcheight;
	
	public static final String ID = "id";
	public static final String SRC = "src";
	public static final String X = "x";
	public static final String Y = "y";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String ROTATED = "rotated";
	public static final String TRIMMED = "trimmed";
	public static final String SRCX = "srcx";
	public static final String SRCY = "srcy";
	public static final String SRCWIDTH = "srcwidth";
	public static final String SRCHEIGHT = "srcheight";
	
	public void setId(String id) {
		this.id = id.trim();
	}
	
	public void setSrc(String file) {
		this.src = file.trim();
	}
	
	public void setXY(String xy) {
		String rp[] = xy.split(",");
		this.x = rp[0].trim();
		this.y = rp[1].trim();
	}
	
	public void setSize(String size) {
		String rp[] = size.split(",");
		this.width = rp[0].trim();
		this.height = rp[1].trim();
		this.srcwidth = this.width;
		this.srcheight = this.height;
		
	}
	
	public void setOffset(String offset) {
		String rp[] = offset.split(",");
		this.srcx = rp[0].trim();
		this.srcy = rp[1].trim();		
	}
	
	public void setRotated(String rotate) {
		this.rotated = rotate.trim();
	}
}
