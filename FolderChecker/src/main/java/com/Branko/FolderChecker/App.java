package com.Branko.FolderChecker;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import javax.swing.JOptionPane;

public class App 
{
    public static void main( String[] args ) throws IOException, InterruptedException
    {
    		
    	
    	String location=JOptionPane.showInputDialog(null,"Please enter folder location");
    	
    	if(!location.isEmpty()) {
    	try {
    	
    	SystemTray tray = SystemTray.getSystemTray();
    	Image image = Toolkit.getDefaultToolkit().getImage("icon.png");
		PopupMenu trayPopupMenu = new PopupMenu();
		
		final TrayIcon trayIcon = new TrayIcon(image, "Folder watcher",trayPopupMenu);
		trayIcon.setImageAutoSize(true);
		
		MenuItem close = new MenuItem("Close");
	    close.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            System.exit(0);             
	        }
	    });
	    trayPopupMenu.add(close);
	    
		trayIcon.setToolTip("Folder watcher");
		
        try {
			tray.add(trayIcon);
			Path faxFolder = Paths.get(location);
	    	
			WatchService watchService = FileSystems.getDefault().newWatchService();
			faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
			boolean valid = true;
			do {
				WatchKey watchKey = watchService.take();

				for (WatchEvent event : watchKey.pollEvents()) {
					WatchEvent.Kind kind = event.kind();
					if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
						String fileName = event.context().toString();
						System.out.println("File Created:" + fileName);
						trayIcon.displayMessage("Neko je uploadovao fajl na share", fileName, MessageType.INFO);
					}
				}
				valid = watchKey.reset();

			} while (valid);	
			
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	}catch (Exception e){
    		
    		JOptionPane.showMessageDialog(null,"Folder not present, program will now exit");
    		System.exit(0);
    	}
    
        
    	}else {
    		
    		JOptionPane.showMessageDialog(null,"No data, program will now exit");
    		
    	}
    			
    	 		
    }
    
    
}


