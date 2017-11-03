package fpmi.dev.gui.entities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import fpmi.dev.gui.MainApp;

public class AndEnginePacker {
	public String version = "1";
	public String file;
	public String type = "bitmap";
	public String width;
	public String height;
	public String pixelformat = "RGBA_8888";
	public String minfilter = "linear";
	public String magfilter = "linear";
	public String wrapt = "clamp";
	public String wraps = "clamp";
	public String premultiplyalpha = "false";
	
	public List<AndEngineItem> listItem;
	
	public static final String VERSION = "version";
	public static final String FILE = "file";
	public static final String TYPE = "type";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String PIXELFORMAT = "pixelformat";
	public static final String MINFILTER = "minfilter";
	public static final String MAGFILTER = "magfilter";
	public static final String WRAPT = "wrapt";
	public static final String WRAPS = "wraps";
	public static final String PREMULTIPLYALPHA = "premultiplyalpha";
	
	public static final String GENERATE_TEXTURE_CLASS = "TextureId";
	private MainApp mainApp;
	
	public AndEnginePacker(MainApp mainApp) {
		// TODO Auto-generated constructor stub
		this.listItem = new ArrayList<AndEngineItem>();
		this.mainApp = mainApp;
	}
	
	public void saveToXML(String xml) {
	    Document dom;

	    // instance of a DocumentBuilderFactory
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    try {
	        // use factory to get an instance of document builder
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        // create instance of DOM
	        dom = db.newDocument();

	        // create the root element
	        Element rootEle = dom.createElement("texture");
	        
	        // create data elements and place them under root
	        createNode(dom, rootEle);
	        rootEle.setAttribute(VERSION, version);
	        rootEle.setAttribute(FILE, file);
	        rootEle.setAttribute(TYPE, type);
	        rootEle.setAttribute(WIDTH, width);
	        rootEle.setAttribute(HEIGHT, height);
	        rootEle.setAttribute(PIXELFORMAT, pixelformat);
	        rootEle.setAttribute(MINFILTER, minfilter);
	        rootEle.setAttribute(MAGFILTER, magfilter);
	        rootEle.setAttribute(WRAPT, wrapt);
	        rootEle.setAttribute(WRAPS, wraps);
	        rootEle.setAttribute(PREMULTIPLYALPHA, premultiplyalpha);
	        dom.appendChild(rootEle);

	        try {
	            Transformer tr = TransformerFactory.newInstance().newTransformer();
	            tr.setOutputProperty(OutputKeys.INDENT, "yes");
	            tr.setOutputProperty(OutputKeys.METHOD, "xml");
	            tr.setOutputProperty(OutputKeys.VERSION, "1.0");
	            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

	            // send DOM to file
	            File file =  new File("out", xml);
	            JOptionPane.showMessageDialog(mainApp, file.getAbsolutePath());
	            tr.transform(new DOMSource(dom), 
	                                 new StreamResult(new FileOutputStream(file)));

	        } catch (TransformerException te) {
	            System.out.println(te.getMessage());
	        } catch (IOException ioe) {
	            System.out.println(ioe.getMessage());
	        }
	    } catch (ParserConfigurationException pce) {
	        System.out.println("UsersXML: Error trying to instantiate DocumentBuilder: " + pce);
	    }
	}
	
	private void createNode(Document dom, Element rootEle) {
		for(int i=0; i < this.listItem.size(); i++) {
			AndEngineItem andEngineItem = this.listItem.get(i);
			Element e = dom.createElement("textureregion");
			e.setAttribute(AndEngineItem.ID, andEngineItem.id);
			e.setAttribute(AndEngineItem.SRC, andEngineItem.src);
			e.setAttribute(AndEngineItem.X, andEngineItem.x);
			e.setAttribute(AndEngineItem.Y, andEngineItem.y);
			e.setAttribute(AndEngineItem.WIDTH, andEngineItem.width);
			e.setAttribute(AndEngineItem.HEIGHT, andEngineItem.height);
			e.setAttribute(AndEngineItem.ROTATED, andEngineItem.rotated);
			e.setAttribute(AndEngineItem.TRIMMED, andEngineItem.trimmed);
			e.setAttribute(AndEngineItem.SRCX, andEngineItem.srcx);
			e.setAttribute(AndEngineItem.SRCY, andEngineItem.srcy);
			e.setAttribute(AndEngineItem.SRCWIDTH, andEngineItem.srcwidth);
			e.setAttribute(AndEngineItem.SRCHEIGHT, andEngineItem.srcheight);
			
	        rootEle.appendChild(e);
		}
	}
	
	public void generateTextureClass() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("public class " + GENERATE_TEXTURE_CLASS + " {\n");
		for(int i=0; i < this.listItem.size(); i++) {
			AndEngineItem andEngineItem = this.listItem.get(i);
			String nameId = getNameId(andEngineItem.src, andEngineItem.id);
			buffer.append(nameId).append("\n");
		}
		buffer.append("}");
		BufferedWriter writer = null;
		
        try {
            File logFile = new File("out", GENERATE_TEXTURE_CLASS);

            // This will output the full path where the file will be written to...
            System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
		
	}
	
	private String getNameId(String src, String id) {
		String rs = src.replaceAll("[ ();.,!@#$%^&*]","_").toUpperCase() + "_" + "ID = " + id + ";";
		
		return "\t public static final int " + rs;
	}
}
