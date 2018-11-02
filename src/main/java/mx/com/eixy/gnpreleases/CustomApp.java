package mx.com.eixy.gnpreleases;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import mx.com.eixy.gnp.gui.Custom;

public class CustomApp {

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							System.out.println("niumbsis ");
							break;
						}
					}			
					
					System.out.println("Heop " + System.getProperty("user.dir"));
					
					//System.exit(0);
					
					AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CustomConfig.class);
					Custom custom = context.getBean(Custom.class);
					
					custom.pack();
					custom.setLocationRelativeTo(null);
					custom.setVisible(true);
					custom.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							context.close();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
}
