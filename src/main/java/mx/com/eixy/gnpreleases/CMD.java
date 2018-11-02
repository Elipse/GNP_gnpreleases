package mx.com.eixy.gnpreleases;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class CMD {
	
	public static void main(String[] args) throws InterruptedException, IOException {
		String[] command =
		    { 
		        "cmd",
		    };
		    Process p = Runtime.getRuntime().exec(command);
		    new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
		    new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
		    PrintWriter stdin = new PrintWriter(p.getOutputStream());
		    stdin.println("\"D:\\Users\\ELIALVA\\Desktop\\GNP Custmizador\\workbench\\WinMerge\\WinMergeU.exe\" file1.txt file2.txt /e");
		    //String s = "D:\\Users\\ELIALVA\\Desktop\\EPAZOTE.jar";
		    //stdin.println("java -jar " + s);
		    // write any other commands you want here
		    stdin.close();
		    int returnCode = p.waitFor();
		    System.out.println("Return code = " + returnCode);
	}	
}
