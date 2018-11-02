package soundsystem;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Chetos extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Chetos frame = new Chetos();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Chetos() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setName("Pirruris");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(5, 5, 424, 14);
		contentPane.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(5, 30, 236, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		
		JTextFieldWrapper jTextFieldWrapper = new JTextFieldWrapper();
		jTextFieldWrapper.install(textField, termino -> busqueda(termino));
	}
	
	
	public List<Entry<String,ImageIcon>> busqueda(String termino) {
		//Aquí se busca y se llena la lista
		return null;
	}
	
	
}
