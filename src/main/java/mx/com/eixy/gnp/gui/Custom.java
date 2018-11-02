package mx.com.eixy.gnp.gui;

import static mx.com.eixy.gnpreleases.CustomConfig.FOLDER_TMP;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ch.qos.logback.core.util.FileUtil;
import mx.com.eixy.gnp.customizador.Adaptador;
import mx.com.eixy.gnp.customizador.Estibador;
import mx.com.eixy.gnp.modelo.Member;
import mx.com.eixy.utilities.swing.MyJTable;
import mx.com.eixy.utilities.swing.SwingKeys;
import mx.com.eixy.utilities.swing.SwingUtils;

@Component
public class Custom extends JFrame implements ChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField bibliotecaOrigen;
	private JTextField bibliotecaDestino;

	@Resource(name = "tiposDeElementos")
	public List<String> tipos;

	@Resource(name = "iconProceso")
	public ImageIcon iconProceso;

	@Autowired
	Estibador estibador;
	
	@Autowired
	Adaptador adaptador;
	
	//@Value("${user.dir}")
	@Value("#{'${user.dir}' + '\\'}")
	private String userDirectory;
	
	//@Value("${folder.reemplazos}")
	@Value("#{'${folder.reemplazos}' + '\\'}")
	private String folderReemplazos;

	@Value("#{'${folder.temporal}' + '\\'}")
	private String folderTemporal;

	
	
	private JComboBox<String> comboBoxTipo;
	private JComboBox<String> comboBoxServerOrigen;
	private JComboBox<String> comboBoxServerDestino;
	private JButton btnCustomizar;
	
	private JTextArea membersOrigen;
	private JTextArea membersDestino;
	private JTable table;
	private JTextArea console;
	
	private MyJTable<Member> jtc;

	/**
	 * Create the frame.
	 */
	public Custom() {
		setPreferredSize(new Dimension(950, 700));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 954, 592);
		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(10, 500));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JToolBar toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);

		btnCustomizar = new JButton("Customizar");
		btnCustomizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		toolBar.add(btnCustomizar);

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(950, 450));
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel_1.setPreferredSize(new Dimension(100, 200));
		panel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new GridLayout(1, 0, 0, 0));

		JPanel panel_3 = new JPanel();
		panel_3.setPreferredSize(new Dimension(500, 500));
		panel_1.add(panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[] { 100, 300, 0 };
		gbl_panel_3.rowHeights = new int[] { 30, 40, 40, 40, 40, 40, 30 };
		gbl_panel_3.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel_3.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel_3.setLayout(gbl_panel_3);

		JLabel lblNewLabel = new JLabel("New label");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 2;
		gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel_3.add(lblNewLabel, gbc_lblNewLabel);

		JLabel lblTipo = new JLabel("TIPO:");
		lblTipo.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblTipo = new GridBagConstraints();
		gbc_lblTipo.anchor = GridBagConstraints.WEST;
		gbc_lblTipo.insets = new Insets(0, 0, 5, 5);
		gbc_lblTipo.gridx = 0;
		gbc_lblTipo.gridy = 1;
		panel_3.add(lblTipo, gbc_lblTipo);

		comboBoxTipo = new JComboBox<>();
		GridBagConstraints gbc_comboBoxTipo = new GridBagConstraints();
		gbc_comboBoxTipo.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxTipo.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxTipo.gridx = 1;
		gbc_comboBoxTipo.gridy = 1;
		panel_3.add(comboBoxTipo, gbc_comboBoxTipo);

		JLabel lblAmbienteOrigen = new JLabel("AMBIENTE ORIGEN:");
		GridBagConstraints gbc_lblAmbienteOrigen = new GridBagConstraints();
		gbc_lblAmbienteOrigen.anchor = GridBagConstraints.WEST;
		gbc_lblAmbienteOrigen.insets = new Insets(0, 0, 5, 5);
		gbc_lblAmbienteOrigen.gridx = 0;
		gbc_lblAmbienteOrigen.gridy = 2;
		panel_3.add(lblAmbienteOrigen, gbc_lblAmbienteOrigen);

		comboBoxServerOrigen = new JComboBox<>();
		GridBagConstraints gbc_comboBoxServerOrigen = new GridBagConstraints();
		gbc_comboBoxServerOrigen.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxServerOrigen.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxServerOrigen.gridx = 1;
		gbc_comboBoxServerOrigen.gridy = 2;
		panel_3.add(comboBoxServerOrigen, gbc_comboBoxServerOrigen);

		JLabel lblAmbienteDestino = new JLabel("AMBIENTE DESTINO:");
		GridBagConstraints gbc_lblAmbienteDestino = new GridBagConstraints();
		gbc_lblAmbienteDestino.anchor = GridBagConstraints.WEST;
		gbc_lblAmbienteDestino.insets = new Insets(0, 0, 5, 5);
		gbc_lblAmbienteDestino.gridx = 0;
		gbc_lblAmbienteDestino.gridy = 3;
		panel_3.add(lblAmbienteDestino, gbc_lblAmbienteDestino);

		comboBoxServerDestino = new JComboBox<String>();
		GridBagConstraints gbc_comboBoxServerDestino = new GridBagConstraints();
		gbc_comboBoxServerDestino.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxServerDestino.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxServerDestino.gridx = 1;
		gbc_comboBoxServerDestino.gridy = 3;
		panel_3.add(comboBoxServerDestino, gbc_comboBoxServerDestino);

		JLabel lblBibliotecaOrigen = new JLabel("BIBLIOTECA ORIGEN:");
		GridBagConstraints gbc_lblBibliotecaOrigen = new GridBagConstraints();
		gbc_lblBibliotecaOrigen.anchor = GridBagConstraints.WEST;
		gbc_lblBibliotecaOrigen.insets = new Insets(0, 0, 5, 5);
		gbc_lblBibliotecaOrigen.gridx = 0;
		gbc_lblBibliotecaOrigen.gridy = 4;
		panel_3.add(lblBibliotecaOrigen, gbc_lblBibliotecaOrigen);

		bibliotecaOrigen = new JTextField();
		bibliotecaOrigen.setText("PNCQP.BTCH.PROCLIB");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 4;
		panel_3.add(bibliotecaOrigen, gbc_textField);
		bibliotecaOrigen.setColumns(10);

		JLabel lblBibliotecaDestino = new JLabel("BIBLIOTECA DESTINO:");
		GridBagConstraints gbc_lblBibliotecaDestino = new GridBagConstraints();
		gbc_lblBibliotecaDestino.anchor = GridBagConstraints.WEST;
		gbc_lblBibliotecaDestino.insets = new Insets(0, 0, 5, 5);
		gbc_lblBibliotecaDestino.gridx = 0;
		gbc_lblBibliotecaDestino.gridy = 5;
		panel_3.add(lblBibliotecaDestino, gbc_lblBibliotecaDestino);

		bibliotecaDestino = new JTextField();
		bibliotecaDestino.setText("PNCQP.BTCH.PROCLIB");
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 5;
		panel_3.add(bibliotecaDestino, gbc_textField_1);
		bibliotecaDestino.setColumns(10);

		JLabel lblNewLabel_3 = new JLabel("New label");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_3.gridwidth = 2;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 6;
		panel_3.add(lblNewLabel_3, gbc_lblNewLabel_3);

		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4);
		panel_4.setLayout(new GridLayout(1, 0, 0, 0));

		JPanel panel_5 = new JPanel();
		panel_5.setBorder(null);
		panel_4.add(panel_5);
		panel_5.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel_1 = new JLabel("       CARDS ORIGEN:");
		panel_5.add(lblNewLabel_1, BorderLayout.NORTH);

		membersOrigen = new JTextArea();
		membersOrigen.setFocusTraversalKeysEnabled(false);
		panel_5.add(membersOrigen, BorderLayout.CENTER);

		JLabel lblNewLabel_4 = new JLabel("");
		lblNewLabel_4.setPreferredSize(new Dimension(20, 16));
		panel_5.add(lblNewLabel_4, BorderLayout.WEST);

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(null);
		panel_4.add(panel_6);
		panel_6.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel_2 = new JLabel("       CARDS DESTINO:");
		panel_6.add(lblNewLabel_2, BorderLayout.NORTH);

		membersDestino = new JTextArea();
		membersDestino.setFocusTraversalKeysEnabled(false);
		panel_6.add(membersDestino, BorderLayout.CENTER);

		JLabel lblNewLabel_5 = new JLabel("");
		lblNewLabel_5.setPreferredSize(new Dimension(20, 16));
		panel_6.add(lblNewLabel_5, BorderLayout.WEST);

		JPanel panel_2 = new JPanel();
		panel_2.setPreferredSize(new Dimension(400, 0));
		panel.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		splitPane.setPreferredSize(new Dimension(231, 300));
		splitPane.setOneTouchExpandable(true);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panel_2.add(splitPane, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setMinimumSize(new Dimension(25, 200));
		scrollPane.setSize(new Dimension(0, 200));
		scrollPane.setPreferredSize(new Dimension(350, 200));
		splitPane.setLeftComponent(scrollPane);

		table = new JTable();
		table.setPreferredSize(new Dimension(350, 200));
		table.setPreferredScrollableViewportSize(new Dimension(250, 200));
		scrollPane.setViewportView(table);

		console = new JTextArea();
		console.setPreferredSize(new Dimension(12, 50));
		console.setText("k");
		splitPane.setRightComponent(console);
		
	}

	private void adicionaMembers() {
		
		adaptador.modificaRegla("").setVisible(true);

		String tipo = (String) comboBoxTipo.getSelectedItem();

		String origen = (String) comboBoxServerOrigen.getSelectedItem();
		String destino = (String) comboBoxServerDestino.getSelectedItem();

		String bibOrigen = bibliotecaOrigen.getText();
		String bibDestino = bibliotecaDestino.getText();

		String[] nombresOrigen = StringUtils.split(membersOrigen.getText());
		String[] nombresDestino = StringUtils.split(membersDestino.getText());

		for (int i = 0; i < nombresOrigen.length; i++) {

			String tmpOrigen = bibOrigen + "(" + nombresOrigen[i] + ")";
			String tmpDestino = bibDestino + "(" + nombresDestino[i] + ")";

			Member member = new Member();
			member.setTipo(tipo).setOrigen(origen).setDestino(destino).setNombreOrigen(tmpOrigen)
					.setNombreDestino(tmpDestino);
			jtc.add(member);
		}

		resizeColumnWidth(table);

	}

	private void customizar() {
		
		

		jtc.stream().forEach(member -> {
			try {
				String nombre = StringUtils.substringBetween(member.getNombreOrigen(), "(", ")");

				String localFileName = folderTemporal + nombre;

				File localFile = estibador.descarga(member.getOrigen(),member.getNombreOrigen(), localFileName);
				
				String contenido = FileUtils.readFileToString(localFile, Charset.forName("ISO-8859-1"));
				
				//adaptador.buscaYreemplaza(member, contenido);
				
				//estibador.carga(member.getOrigen(), member.getNombreDestino(), localModifiedFile);
				
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("pale " + e);
				member.setLog(e.getMessage());
				return;
			}
		});
	}

	public static void resizeColumnWidth(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();
		for (int column = 0; column < table.getColumnCount(); column++) {
			int width = 15; // Min width
			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer renderer = table.getCellRenderer(row, column);
				java.awt.Component comp = table.prepareRenderer(renderer, row, column);
				width = Math.max(comp.getPreferredSize().width + 1, width);
			}
			if (width > 300)
				width = 300;
			columnModel.getColumn(column).setPreferredWidth(width);
		}
	}

	private void changeFocus() {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
	}

	@PostConstruct
	public void initIt() {
		
		System.out.println("Ep$%&as " + userDirectory + " " + folderReemplazos + "");

		try {
			//adaptador.reemplazos();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JMenuBar jmb = new JMenuBar();
		JMenu jm1 = new JMenu("Configuración");
		JMenu jm2 = new JMenu("Ayuda");

		JSeparator js = new JSeparator();

		JMenuItem jmi = new JMenuItem("Transformaciones");
		JMenuItem jmi2 = new JMenuItem("Acerca de GNPCustomizador");

		jmi2.addActionListener(event -> {
			JOptionPane.showMessageDialog(Custom.this, "Powered e@m \n GNP", "Acerca de GNPCustomizador",
					JOptionPane.INFORMATION_MESSAGE);
		});

		jm1.add(jmi);
		jm2.add(js);
		jm2.add(jmi2);
		jmb.add(jm1);
		jmb.add(jm2);

		setJMenuBar(jmb);

		SwingKeys.addEscapeListener(this);
		
		SwingKeys.add(contentPane, JComponent.WHEN_IN_FOCUSED_WINDOW, "alt E", () -> adicionaMembers());
		SwingKeys.add(contentPane, JComponent.WHEN_IN_FOCUSED_WINDOW, "alt R", () -> customizar());
		SwingKeys.add(membersOrigen, JComponent.WHEN_FOCUSED, "TAB", () -> changeFocus());
		SwingKeys.add(membersDestino, JComponent.WHEN_FOCUSED, "TAB", () -> changeFocus());
		SwingKeys.add(table, JComponent.WHEN_FOCUSED, "TAB", () -> changeFocus());
		SwingKeys.add(console, JComponent.WHEN_FOCUSED, "TAB", () -> changeFocus());

		jtc = new MyJTable<>(table, Member.class);		
		

		SwingUtils.addTextChangeListener(bibliotecaOrigen, Custom.this);

		List<String> lista = new ArrayList<>();
		tipos.stream().forEach(element -> {
			lista.add(element);
		});

		llenaCombo(comboBoxTipo, lista);

		llenaCombo(comboBoxServerOrigen, estibador.servidores());
		llenaCombo(comboBoxServerDestino, estibador.servidores());

		btnCustomizar.setIcon(iconProceso);
	}

	@PreDestroy
	public void cleanUp() {
		System.out.println("Spring Container is destroy! Customer clean up");
	}

	private void llenaCombo(JComboBox<String> jComboBox, List<String> lista) {
		ComboBoxModel<String> cbm = new DefaultComboBoxModel<>(lista.toArray(new String[] {}));
		jComboBox.setModel(cbm);
	}

	public static void main(String[] args) {
		System.getProperties().entrySet().stream().forEach(entry -> {
			System.out.println("key: " + entry.getKey() + " value: " + entry.getValue());
		});

		System.getenv().entrySet().stream().forEach(entry -> {
			System.out.println("key: " + entry.getKey() + " value: " + entry.getValue());
		});
	}

	// soy el Obsevable
	@Override
	public void stateChanged(ChangeEvent e) {

		if (e.getSource() instanceof JTextField) {
			JTextField jtf = (JTextField) e.getSource();
			System.out.println("Mi texto es: " + jtf.getText() + " ...a buscar...");
		}
	}

	// soy el que hace la busqueda

	// soy el Observer

}
