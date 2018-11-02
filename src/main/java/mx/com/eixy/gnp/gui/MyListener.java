package mx.com.eixy.gnp.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MyListener implements DocumentListener, ChangeListener, ActionListener, ItemListener {

	private Customizador customizador;

	public MyListener(Customizador customizador) {
		this.customizador = customizador;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		System.out.println(e);
		System.out.println(e.getType());
		customizador.documentListener(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		customizador.documentListener(e);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		customizador.tabChanged(e);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() instanceof JComboBox<?>) {
			customizador.comboChanged(e);
		}

		if (e.getSource() instanceof JButton) {
			customizador.buttonPressed(e);
		}

		if (e.getSource() instanceof JMenuItem) {
			customizador.editar(e);

		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		customizador.itemStateChanged(e);

	}
}