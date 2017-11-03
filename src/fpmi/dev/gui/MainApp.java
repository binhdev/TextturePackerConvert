package fpmi.dev.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fpmi.dev.gui.entities.AndEnginePacker;
import fpmi.dev.gui.entities.Convert;
import fpmi.dev.gui.entities.LibGDXItem;
import fpmi.dev.gui.entities.LibGDXPacker;

public class MainApp extends JFrame {
	private static final long serialVersionUID = 1L;
	private final static String TITLE = "TexturePacker Convert - Please file does't have empty line";
	private final static String BTN_CHOOSE_FILE = "Choose File";
	
	private final int STEP_PACKER = 4;
	private final int STEP_ITEM = 7;
	
	final JFileChooser fc = new JFileChooser();
	private JPanel panel;
	private GridLayout gridLayout;

	private JButton btnChooseFile;
	private JTextField textWidth;
	private JTextField textHeight;
	
	public MainApp() {
		// TODO Auto-generated constructor stub
		initGUI();
	}
	
	private void initGUI() {
		setSize(600, 180);
		setTitle(TITLE);
		setVisible(true);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - getHeight()) / 2);
		setLocation(x, y);
		panel = new JPanel();
		add(panel);
		gridLayout = new GridLayout(3, 2);
		panel.setLayout(gridLayout);		
		
		panel.add(new JLabel("Image's Width"));
		textWidth = new JTextField();
		panel.add(textWidth);
		
		panel.add(new JLabel("Image's Height"));
		textHeight = new JTextField();
		panel.add(textHeight);
		
		btnChooseFile = new JButton(BTN_CHOOSE_FILE);
		btnChooseFile.setSize(200,32);
		btnChooseFile.setLocation(100/2, 150/2);
		btnChooseFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				createDirectoryOutput();
				int width = 0;
				int height = 0;
				try {
					width = Integer.parseInt(textWidth.getText());
					height = Integer.parseInt(textHeight.getText());
				}catch(Exception exp) {
					JOptionPane.showMessageDialog(MainApp.this, "Please input width and height");
					return;
				}
				int returnVal = fc.showOpenDialog(MainApp.this);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            LibGDXPacker gdxPacker = readLibGDXPacker(file);
		            AndEnginePacker andEnginePacker = Convert.convertFromLibGdxPackerItem(MainApp.this, gdxPacker);
		            andEnginePacker.width = String.valueOf(width);
		            andEnginePacker.height = String.valueOf(height);
		            andEnginePacker.saveToXML("packer.xml");
		            andEnginePacker.generateTextureClass();
		            
		            System.out.println("Opening: " + file.getName());
		        } else {
		            System.out.println("No file");
		        }
			}
		});
		
		panel.add(btnChooseFile);
		revalidate(); 
		repaint();
	}
	
	private LibGDXPacker readLibGDXPacker(File file) {
		int step = 0;
	    LibGDXPacker packer = new LibGDXPacker();
	    LibGDXItem item = null;
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       step++;
		       String [] rp;
		       if(step <= STEP_PACKER) {
		    	   switch (step) {
						case 1:
							packer.file = line.trim();
							break;
						case 2:
							rp = line.trim().split(":");
							packer.format = rp[1].trim();
							break;
						case 3:
							rp = line.trim().split(":");
							packer.filter = rp[1].trim();
							break;
						case 4:
							rp = line.trim().split(":");
							packer.repeat = rp[1].trim();
							break;
		    	   }
		       }else {
		    	   switch ((step - STEP_PACKER) % STEP_ITEM) {
					case 1:
						item = new LibGDXItem();
						packer.listItem.add(item);
						item.file = line.trim();
						break;
					case 2:
						rp = line.trim().split(":");
						item.rotate = rp[1].trim();
						break;
					case 3:
						rp = line.trim().split(":");
						item.xy = rp[1];
						break;
					case 4:
						rp = line.trim().split(":");
						item.size = rp[1].trim();
						break;
					case 5:
						rp = line.trim().split(":");
						item.orig = rp[1].trim();
						break;
					case 6:
						rp = line.trim().split(":");
						item.offset = rp[1].trim();
						break;
					case 0:
						item.index = line.trim();
						break;
		    	   }
		       }
		    }
		}catch (Exception e) {
			// TODO: handle exception
		}
		return packer;
	}
	
	private void createDirectoryOutput() {
		File theDir = new File("out");		
		// if the directory does not exist, create it
		if (!theDir.exists()) {
		    System.out.println("creating directory: " + theDir.getName());
		    boolean result = false;		
		    try{
		        theDir.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		        //handle it
		    }        
		    if(result) {    
		        System.out.println("DIR created");  
		    }
		}

	}
	
	public static void main(String[] args) {
		new MainApp();
	}
}
