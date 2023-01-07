package com.github.youssefagagg.typing;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import org.fxmisc.richtext.InlineCssTextArea;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;

import javafx.stage.FileChooser;


public class TypeController {
	
	@FXML
	private ScrollPane sp;
	
    @FXML
    private Menu recentMenu;

    @FXML
    private Label currentSpeed;
    private ArrayList<MenuItem> mi;
  
    
    private InlineCssTextArea area;
    
  
    private String text="This application to enhance your typing speed. "
			+ "when you type the color text will change to green if your typing is correct. "
			+ "otherwise the color will be red. you don't have to delete any letter just keep typing. "
			+ "your typing speed will appear in the bottom.";
    private int indexOfText=0;//current index of the string the user type
 
    private char currentChar;
    boolean startTyping=true;
    private long start;
   int speed;
   
    public void initialize() 
    {
    	currentSpeed.setText("start typing!");
    	area=new InlineCssTextArea(text);
    	mi=new ArrayList<MenuItem>();
    	area.setWrapText(true);
    	area.setEditable(false);
    	area.setStyle("-fx-font-weight: bold;-fx-font-size:20;");
    	area.setStyle(0, 1,"-rtfx-background-color: blue; ");
    	//area.setMaxWidth(Double.MAX_VALUE);
    	//area.setMaxHeight(Double.MAX_VALUE);
    	//area.setDisable(true);
    	openRecentFiles();
    	sp.setContent(area);
    	area.setFocusTraversable(false);
    	sp.setFitToWidth(true); 
    	sp.setFitToHeight(true);
    	
    	
    }

    @FXML
    void selectAbout(ActionEvent event) {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("About the app");
    	alert.setHeaderText("Look, I don't know lol");
    	alert.setContentText("some text her blabla!");
    	alert.show();
    	

    }

    @FXML
    void selectAddText(ActionEvent event) {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Add Text");
    	alert.setHeaderText("Look, add text here");
    	alert.setContentText("Could not find file blabla.txt!");
    	TextArea textArea = new TextArea();
    	
    	textArea.setWrapText(true);

    	textArea.setMaxWidth(Double.MAX_VALUE);
    	textArea.setMaxHeight(Double.MAX_VALUE);
    	

    	ScrollPane scroll = new ScrollPane();
    	scroll.setMaxWidth(Double.MAX_VALUE);
    	
    	scroll.setContent(textArea);

    	alert.getDialogPane().setContent(scroll);

    	Optional<ButtonType> result = alert.showAndWait();
    	if(result.get()==ButtonType.OK) 
    	{
    		String temp=textArea.getText();
    		if(!temp.isEmpty()) 
    		{
    			
    			text=temp.replaceAll(System.lineSeparator(), " ");
    			area.replaceText(text);
    			reStart();
    		}
    	}
    	

    }

    @FXML
    void selectClose(ActionEvent event) {
    	System.exit(0);

    }

    @FXML
    void selectOpenFile(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();

		fileChooser.getExtensionFilters().addAll(
		     new FileChooser.ExtensionFilter("Text Files", "*.txt")
		    
		);

      
            File selectedFile = fileChooser.showOpenDialog(null);
            if(selectedFile!=null) {
            	MenuItem menuitem=reOpened(selectedFile.getAbsolutePath());
            	if(menuitem==null) {
            try(Scanner in=new Scanner(Paths.get(selectedFile.getAbsolutePath())))
            {
            	String temp="";
            	while(in.hasNext()) 
            	{
            		temp+=in.nextLine()+" ";
            	}
            	if(!temp.isEmpty()) 
            	{
            		MenuItem m=new MenuItem(selectedFile.getName());
            		m.setUserData(selectedFile);
            		m.setOnAction(new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent e) {
							// TODO Auto-generated method stub
							MenuItem m=(MenuItem) e.getSource();
							
	            			addTextToTextArea(m);
						}
					});
            		mi.add(0, m);
            		saveOpendFiles();
            		recentMenu.getItems().clear();
            		recentMenu.getItems().addAll(mi);
        			text=temp.trim();
        			area.replaceText(text);
        			reStart();
            	}
            }catch(Exception e) {}
            
            }else {
            		reArrang(menuitem);
            		addTextToTextArea(menuitem);
            		
            	}
            }

    }

    @FXML
    void selectRestart(ActionEvent event) {
    	reStart();
    }
    @FXML
    void onKeyTyped(KeyEvent event) {
    	currentChar=text.charAt(indexOfText);
    	if(startTyping) 
    	{
    		start=System.currentTimeMillis()/1000;
    		startTyping=false;
    		 Thread t=new Thread(new YourSpeed());
    		 t.setDaemon(true);
   	      	 t.start();
    	}
    	
    	
    	
    	if(currentChar==event.getCharacter().charAt(0)) 
    	{
    		indexOfText++;
    		 area.setStyle(0, indexOfText,"-rtfx-background-color: green; ");
    		 if(indexOfText!=text.length()) 
    		 area.setStyle(indexOfText, indexOfText+1,"-rtfx-background-color: blue; ");
    		
    		
    		
    	}else {
    		area.setStyle(0, indexOfText,"-rtfx-background-color: green; ");
    		area.setStyle(indexOfText, indexOfText+1,"-rtfx-background-color: red; ");
    		
    	}
    	
    	if(indexOfText==text.length()) 
    	{
    		
    		reStart();
    		Alert alert = new Alert(AlertType.INFORMATION);
        	alert.setTitle("Your Speed");
        	alert.setHeaderText("Look, I don't know lol");
        	alert.setContentText("Your speed="+speed+"words per minute");
        	alert.show();
    		
    	}
    	System.out.println(start);
    	System.out.println(System.currentTimeMillis()/1000-start);
    

    }

	private void reStart() {
		// TODO Auto-generated method stub
		startTyping=true;
		indexOfText=0;
		area.setStyle(0, text.length(),"-rtfx-background-color: white; ");
		area.setStyle(0, 1,"-rtfx-background-color: blue; ");
		
		currentSpeed.setText("start typing!");
		
	}
	private void saveOpendFiles() 
	{
		try {
 		   	//File name=s;
			File f=new File(System.getProperty("user.home")+"/Documents/filesopen.txt");
    		 f.setReadable(true);
 		   	 FileWriter fw=new FileWriter(f);
 		   	 
 		   	// out.useProtocolVersion(ObjectOutputStream.PROTOCOL_VERSION_2);
 		   	 for(int i=mi.size()-1;i>=0;i--) {
 		   		 File file=(File)mi.get(i).getUserData();
 		   		 String s=file.getAbsolutePath();
 		   		 
 		   		 fw.write(s);
 		   		 fw.write(System.lineSeparator());
 		   		 
 		   	 }
 		   	 fw.flush();
 		   	 fw.close();
 		   	 }catch(IOException ex){
 		   		 System.out.println(ex);
 		   	 }
        
	}
	private void openRecentFiles() 
	{
		try(Scanner in=new Scanner(Paths.get(System.getProperty("user.home")+"/Documents/filesopen.txt")))
        {
			while(in.hasNext()) 
			{
				File f=new File(in.nextLine());
				MenuItem m=new MenuItem(f.getName());
        		m.setUserData(f);
        		m.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent e) {
						// TODO Auto-generated method stub
						MenuItem m=(MenuItem) e.getSource();
						reArrang(m);
            			addTextToTextArea(m);
					}
				});
        		mi.add(0, m);
        		recentMenu.getItems().clear();
        		recentMenu.getItems().addAll(mi);
			}
        }catch(Exception e) {}
		
	}
	private void reArrang(MenuItem m) {
		
		mi.remove(m);
		mi.add(0, m);
		recentMenu.getItems().clear();
		recentMenu.getItems().addAll(mi);
		saveOpendFiles();
		
		
		
	}
	private MenuItem reOpened(String pass) {
		
		for(MenuItem m:mi) 
		{
			File f=(File)m.getUserData();
			if(f.getAbsolutePath().equals(pass))
				return m;
		}
		return null;
		
	}
   private void addTextToTextArea(MenuItem m) {
	   File file = (File) m.getUserData();
		 System.out.println(file.getName());
          if(file!=null) {
          try(Scanner in1=new Scanner(Paths.get(file.getAbsolutePath())))
          {
          	String temp="";
          	while(in1.hasNext()) 
          	{
          		temp+=in1.nextLine()+" ";
          	}
          	if(!temp.isEmpty()) 
          	{
      			text=temp.trim();
      			area.replaceText(text);
      			reStart();
          	}
          }catch(Exception ee) {ee.printStackTrace();}}
	
}
   class YourSpeed implements Runnable
   {
	   @Override
	   public void run() 
	   {
		   while (true) 
		   {
			   if(startTyping)break;
			   try {
				   
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   if(indexOfText!=0) {
			    	 speed= (int) (60.0/(System.currentTimeMillis()/1000-start)*indexOfText)/4;
			   }
			    	 Platform.runLater(
							  () -> {
								  if(!startTyping)
								  currentSpeed.setText("Your speed="+speed+"words per minute");
					
							  });
			    	
			    	
			   
			   
		   }
	   }
	   
   }


}
