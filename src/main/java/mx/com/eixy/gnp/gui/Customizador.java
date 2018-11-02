package mx.com.eixy.gnp.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharSet;
import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import mx.com.eixy.gnp.customizador.Adaptador;
import mx.com.eixy.gnp.customizador.Estibador;

import mx.com.eixy.gnp.modelo.Reemplazo;
import mx.com.eixy.utilities.swing.MyTextListener;
import mx.com.eixy.utilities.zos.ftp.Transfer;

import java.awt.event.ActionListener;

public class Customizador {

	private JFrame frmGnpCustomizador;
	private MyListener myListener;
	private ExecutorService executor;
	private String pulsoDestinos;
	private JTabbedPane jTabbedPaneOrigen;
	private JTabbedPane jTabbedPaneDestin;

	private JComboBox<String> comboBoxOrign;
	private String PREFIJO = "";
	private JComboBox<String> comboBoxDestn;
	private String DESTINO;

	private static Configurations configurations;
	public static String CONFIG_FILE = "transfer.properties";

	static {
		configurations = new Configurations();
	}

	public static final Map<String, List<String>> lOG = new HashMap<>();
	private JToolBar toolBar;
	

	public static void log(String origen, String registroBitacora) {
		System.out.println("origen: " + origen + " registroBit�cora " + registroBitacora);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		
		       
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					try {
						for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
							if ("Nimbus".equals(info.getName())) {
								UIManager.setLookAndFeel(info.getClassName());
								break;
							}
						}
					} catch (Exception e) {
						// If Nimbus is not available, fall back to
						// cross-platform
						try {
							UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
						} catch (Exception ex) {
							// not worth my time
						}
					}
					Customizador window = new Customizador();
					window.frmGnpCustomizador.pack();
					window.frmGnpCustomizador.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Customizador() {
		initialize();
		JMenuBar jmb = new JMenuBar();

		JMenu menu_1 = new JMenu("Configuración");
		jmb.add(menu_1);

		JMenuItem menuItem = new JMenuItem("Usuarios & Passwords");
		menuItem.addActionListener(myListener);
		menu_1.add(menuItem);

		JMenuItem menuItem_1 = new JMenuItem("Transformaciones");
		menuItem_1.addActionListener(myListener);
		menu_1.add(menuItem_1);
		
		JMenuItem mntmServidoresTipos = new JMenuItem("Servidores & Tipos");
		mntmServidoresTipos.addActionListener(myListener);
		menu_1.add(mntmServidoresTipos);
		frmGnpCustomizador.setJMenuBar(jmb);
		threadConfig();
	}

	private void threadConfig() {
		executor = Executors.newFixedThreadPool(5);

		/*
		 * executor.shutdown(); while (!executor.isTerminated()) { }
		 * System.out.println("Finished all threads");
		 */
	}

	void documentListener(DocumentEvent e) {

		String prefijo = "";
		try {
			Document d = e.getDocument();
			int l = d.getLength();
			prefijo += d.getText(0, l);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}

		this.pulsoDestinos = this.PREFIJO = prefijo;

		actualizaDestinos();
	}

	private void actualizaDestinos() {
		ReplazaDestinos task = new ReplazaDestinos(this.pulsoDestinos);
		task.execute();
	}

	/**
	 * 
	 * @param e
	 */

	public void tabChanged(ChangeEvent e) {

		JTabbedPane jtp = (JTabbedPane) e.getSource();
		int tab = jtp.getSelectedIndex();

		if (jtp == jTabbedPaneDestin) {
			jTabbedPaneOrigen.setSelectedIndex(tab);

		} else {
			jTabbedPaneDestin.setSelectedIndex(tab);
		}
	}

	public void cambioDeBiblioMembers(ChangeEvent event) {

		Component componente = (Component) event.getSource();

		Container tabbedPane = componente.getParent().getParent();
		System.out.println(tabbedPane.getName());

	}

	public void comboChanged(ActionEvent e) {
		System.out.println("nena " + e.getClass());
		JComboBox<String> jcb = (JComboBox<String>) e.getSource();
		//(String) jcb.getSelectedItem();
	}

	public void buttonPressed(ActionEvent e) {

		String conexionOrigen = comboBoxOrign.getSelectedItem().toString();
		String conexionDestino = comboBoxDestn.getSelectedItem().toString();

		
			
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		PropertiesConfiguration pc = null;
		try {
			pc = configurations.properties(new File(CONFIG_FILE));

		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<String> tmpList = new ArrayList<>();

		frmGnpCustomizador = new JFrame();
		frmGnpCustomizador.setTitle("GNP - Customizador JCL");
		frmGnpCustomizador.setBounds(100, 100, 691, 480);
		frmGnpCustomizador.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGnpCustomizador.getContentPane().setLayout(new BorderLayout(0, 0));

		myListener = new MyListener(Customizador.this);

		JPanel panel = new JPanel();
		frmGnpCustomizador.getContentPane().add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		jTabbedPaneOrigen = new JTabbedPane(JTabbedPane.TOP);
		panel.add(jTabbedPaneOrigen);

		jTabbedPaneDestin = new JTabbedPane(JTabbedPane.TOP);
		panel.add(jTabbedPaneDestin);

		

		List<String> listaTipo = new ArrayList<>();
		listaTipo.addAll(pc.getList(String.class, "TIPO"));

		for (int i = 0; i < listaTipo.size(); i++) {
			String tipo = listaTipo.get(i);

			
		}

		jTabbedPaneOrigen.setSelectedIndex(0);
		jTabbedPaneOrigen.getTitleAt(0);

		jTabbedPaneDestin.setSelectedIndex(0);
		jTabbedPaneDestin.getTitleAt(0);

		JPanel panel_2 = new JPanel();
		frmGnpCustomizador.getContentPane().add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new BorderLayout(0, 0));

		JTextPane textPane = new JTextPane();
		textPane.setPreferredSize(new Dimension(12, 100));
		panel_2.add(textPane);

		toolBar = new JToolBar();
		frmGnpCustomizador.getContentPane().add(toolBar, BorderLayout.NORTH);

		JLabel label_1 = new JLabel("   ");
		toolBar.add(label_1);
		comboBoxOrign = new JComboBox<String>();
		comboBoxOrign.addItemListener(myListener);

		tmpList.addAll(pc.getList(String.class, "ORIGEN"));
		tmpList.get(0);

		comboBoxOrign.setModel(new DefaultComboBoxModel<String>(tmpList.toArray(new String[0])));

		toolBar.add(comboBoxOrign);

		JButton btnNewButton = new JButton("");
		toolBar.add(btnNewButton);
		setIcon(btnNewButton, "_icons/ftp_on_32.png");
		setDisabledIcon(btnNewButton, "_icons/ftp_off_32.png");
		comboBoxDestn = new JComboBox<String>();
		comboBoxDestn.addItemListener(myListener);

		tmpList.clear();
		tmpList.addAll(pc.getList(String.class, "DESTINO"));
		DESTINO = tmpList.get(0);

		comboBoxDestn.setModel(new DefaultComboBoxModel<String>(tmpList.toArray(new String[0])));
		toolBar.add(comboBoxDestn);
		JButton btnNewButton1 = new JButton("");
		toolBar.add(btnNewButton1);
		setIcon(btnNewButton1, "_icons/ftp_on_32.png");
		setDisabledIcon(btnNewButton1, "_icons/ftp_off_32.png");

		JLabel label = new JLabel("   ");
		toolBar.add(label);

		JLabel label_2 = new JLabel("   ");
		toolBar.add(label_2);

		JButton btnCustomiza = new JButton("Customiza");
		btnCustomiza.setIcon(null);
		btnCustomiza.setPreferredSize(new Dimension(110, 35));
		btnCustomiza.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		btnCustomiza.setVerticalTextPosition(SwingConstants.CENTER);
		btnCustomiza.setHorizontalTextPosition(SwingConstants.LEFT);
		setIcon(btnCustomiza, "_icons/process_on_32.png");
		setDisabledIcon(btnCustomiza, "_icons/process_off_32.png");
		toolBar.add(btnCustomiza);
		comboBoxOrign.addActionListener(myListener);
		btnCustomiza.addActionListener(myListener);

		System.out.println(System.getProperty("user.name"));

		jTabbedPaneOrigen.addChangeListener(myListener);
		jTabbedPaneDestin.addChangeListener(myListener);

	}

	private JPanel creaPanel(String tipo) {
		JPanel jPanel = new JPanel();
		jPanel.setName(tipo);

		JTextField jTextField = new JTextField();
		jTextField.setName(tipo);
		MyTextListener.addChangeListener(jTextField, event -> {
			Customizador.this.cambioDeBiblioMembers(event);
		});

		JTextArea jTextArea = new JTextArea();
		jTextArea.setName(tipo);
		MyTextListener.addChangeListener(jTextArea, event -> {
			Customizador.this.cambioDeBiblioMembers(event);
		});

		jPanel.setLayout(new BorderLayout());
		jPanel.add(jTextArea, BorderLayout.CENTER);
		jPanel.add(jTextField, BorderLayout.PAGE_START);

		

		return jPanel;
	}

	private void setIcon(JButton btnNewButton, String imagePath) {
		File ftp_off = new File(imagePath);
		BufferedImage image = null;
		try {
			
			image = ImageIO.read(ftp_off);
		} catch (IOException e) {
			System.out.println("A leer " + ftp_off);
			e.printStackTrace();
		}
		btnNewButton.setIcon(new ImageIcon(image));
	}

	private void setDisabledIcon(JButton btnNewButton, String imagePath) {
		File ftp_off = new File(imagePath);
		BufferedImage image = null;
		try {
			image = ImageIO.read(ftp_off);
		} catch (IOException e) {
			e.printStackTrace();
		}
		btnNewButton.setDisabledIcon(new ImageIcon(image));
	}

	private class ReplazaDestinos implements Runnable {

		private volatile Object pulso;
		private volatile String prefijo;
		private volatile Map<String, String[]> paquetes;

		// TODO a link to GNPCustomizador???
		private ReplazaDestinos(Object pulso) {
			this.pulso = pulso;
			this.paquetes = new HashMap<>();
		}

		public void execute() {
			prepare();
			Customizador.this.executor.execute(this);
		}

		protected void prepare() {
			this.prefijo = Customizador.this.PREFIJO;

			Component[] componentes = jTabbedPaneOrigen.getComponents();
			for (Component component : componentes) {
				if (component instanceof JTextArea) {
					JTextArea jta = (JTextArea) component;
					String[] lineas = StringUtils.split(jta.getText(), "\r\n");
					this.paquetes.put(jta.getName(), lineas);
				}
			}
		}

		@Override
		public void run() {

			this.paquetes.entrySet().forEach(t -> {
				for (int i = 0; i < t.getValue().length; i++) {
					t.getValue()[i] = ReplazaDestinos.this.prefijo + t.getValue()[i];
				}
			});

			EventQueue.invokeLater(() -> {
				updateUI(this.paquetes);
			});
		}

		protected void updateUI(Map<String, String[]> paquetes) {
			if (this.pulso == Customizador.this.pulsoDestinos) {
				Customizador.this.pulsoDestinos = null;

				paquetes.entrySet().forEach(t -> {
					Component[] components = Customizador.this.jTabbedPaneDestin.getComponents();
					for (Component component : components) {
						if (component.getName().equals(t.getKey())) {
							String[] members = t.getValue();
							for (int i = 0; i < members.length; i++) {
							}
						}
					}
				});
			}
		}
	}

	public void jTextAreaChanged(DocumentEvent e) {
		actualizaDestinos();
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == 1) {
			System.out.println("es " + e.getItem() + " e " + e.getStateChange());
		}

	}

	public void editar(ActionEvent e) {
		
		if (e.getSource() instanceof JMenuItem) {
			JMenuItem menuItem = (JMenuItem) e.getSource();
			String opcion = menuItem.getText();
			if (opcion.equals("Usuarios & Passwords")) {
				File f = new File("mainframe.ftp.properties");
				Editor.editar(f);
				return;
			}
			if (opcion.equals("Servidores & Tipos")) {
				File f = new File("transfer.properties");
				boolean estatus = Editor.editar(f);
				
				if(estatus) {
					JOptionPane.showMessageDialog(null, "Debe reiniciar la aplicación para ver los cambios.");
					try {
						Runtime rt = Runtime.getRuntime();
					    try {
					        rt.exec(new String[]{"cmd.exe","/c","start java -jar"});

					    } catch (IOException ex) {
					        // TODO Auto-generated catch block
					        ex.printStackTrace();
					    }

						//restartApplication();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				return;
			}
			System.out.println("el name es " + menuItem.getText());
		}
		
		System.out.println("evento viene de " + e.getSource());
		String origen = comboBoxOrign.getSelectedItem().toString();
		String destino = comboBoxDestn.getSelectedItem().toString();
		
		int indice = jTabbedPaneOrigen.getSelectedIndex();
		String tipo = jTabbedPaneOrigen.getTitleAt(indice);
		
		File f = new File("_reglasDeCustomizacion/" + origen + "-" + destino + "-" + tipo + ".json");
		
		System.out.println("el file " + f.getName());
		
		Editor.editar(f);	
		
	}
	
	public void restartApplication() throws URISyntaxException, IOException
	{
	  final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
	  final File currentJar = new File(Customizador.class.getProtectionDomain().getCodeSource().getLocation().toURI());

	  /* is it a jar file? */
	  if(!currentJar.getName().endsWith(".jar"))
	    return;

	  /* Build command: java -jar application.jar */
	  final ArrayList<String> command = new ArrayList<String>();
	  command.add(javaBin);
	  command.add("-jar");
	  command.add(currentJar.getPath());

	  final ProcessBuilder builder = new ProcessBuilder(command);
	  builder.start();
	  System.exit(0);
	}

}
