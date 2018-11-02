package mx.com.eixy.gnp.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.commons.io.FileUtils;

public class Editor {

	private static JTextArea jTextArea;
	private static JPanel panel;

	static {
		creaPanel();
	}

	private static void creaPanel() {
		panel = new JPanel();
		JScrollPane scrollpane = new JScrollPane();
		jTextArea = new JTextArea();
		scrollpane = new JScrollPane(jTextArea);
		panel.add(scrollpane);
		scrollpane.getViewport().add(jTextArea);
		scrollpane.setPreferredSize(new Dimension(700, 466));
	}

	public static boolean editar(File texto) {

		try {
			String tmpString = "";

			if (texto.exists()) {

				tmpString = FileUtils.readFileToString(texto, Charset.forName("ISO-8859-1"));
			}

			jTextArea.setText(tmpString);

			int result = JOptionPane.showConfirmDialog(null, panel, texto.getName(), JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE);

			if (result == JOptionPane.OK_OPTION) {
				FileUtils.writeStringToFile(texto, jTextArea.getText(), Charset.forName("ISO-8859-1"));
				return true;
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
