package soundsystem;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {

public static void main(String[] args) {
		
		ApplicationContext context = new AnnotationConfigApplicationContext(CDPlayerConfig.class);		
		CDPlayerTestDos cdpt = context.getBean(CDPlayerTestDos.class);		
		System.out.println("cd..." + cdpt.cd);
	}
	
}
