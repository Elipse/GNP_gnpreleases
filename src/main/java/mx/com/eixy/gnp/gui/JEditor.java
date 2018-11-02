package mx.com.eixy.gnp.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;

@Component
public class JEditor extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();
	private JButton okButton;
	private JButton cancelButton;

	private final EventListenerList listenerList = new EventListenerList();
	ChangeEvent fooEvent = null;
	private JTabbedPane tabbedPane;

	public void addChangeListener(ChangeListener listener) {
		listenerList.add(ChangeListener.class, listener);
	}

	public void removeChangeListener(ChangeListener listener) {
		listenerList.remove(ChangeListener.class, listener);
	}

	// Notify all listeners that have registered interest for
	// notification on this event type. The event instance
	// is lazily created using the parameters passed into
	// the fire method.
	protected void fireStateChanged() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				// Lazily create the event:
				if (fooEvent == null)
					fooEvent = new ChangeEvent(this);
				((ChangeListener) listeners[i + 1]).stateChanged(fooEvent);
			}
		}
	}

	/**
	 * Create the dialog.
	 */
	public JEditor() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			contentPanel.add(tabbedPane, BorderLayout.CENTER);

		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	@PostConstruct
	public void configure() {
		okButton.addActionListener(event -> JEditor.this.fireStateChanged());
	}

	public void edita(String fileName) throws IOException {

		JTextArea jTextArea = vistas.get(fileName);

		if (jTextArea != null) {
			seleccionaJTextArea(tabbedPane, jTextArea);
		} else {
			jTextArea = agregaJTextArea(fileName, tabbedPane);

			String texto = FileUtils.readFileToString(new File(fileName), Charset.forName("ISO-8859-1"));

			agregaTexto(jTextArea, texto);
			
			vistas.put(fileName, jTextArea);
		}

		setVisible(true);
	}

	Map<String, JTextArea> vistas = new HashMap<>();

	private static void seleccionaJTextArea(JTabbedPane jTabbedPane, JTextArea jTextArea) {
		int indice = jTabbedPane.indexOfTabComponent(jTextArea);
		jTabbedPane.setSelectedIndex(indice);
	}

	private static JTextArea agregaJTextArea(String fileName, JTabbedPane jTabbedPane) {

		Path path = Paths.get(fileName);

		JPanel jPanel = new JPanel(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane();
		JTextArea textArea = new JTextArea();

		scrollPane.setViewportView(textArea);

		jPanel.add(scrollPane, BorderLayout.CENTER);

		jTabbedPane.addTab(path.getFileName().toString(), null, jPanel, path.toString());

		return textArea;
	}

	private static void agregaTexto(JTextArea jTextArea, String texto) {
		jTextArea.setText(texto);
		jTextArea.setCaretPosition(texto.length());
		jTextArea.grabFocus();
	}

	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			JEditor dialog = new JEditor();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			dialog.edita("D:\\Users\\ELIALVA\\Desktop\\texto.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
