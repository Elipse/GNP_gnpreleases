package soundsystem;

import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.text.Document;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import mx.com.eixy.utilities.swing.SwingUtils;

public class JTextFieldWrapper {

	protected EventListenerList listenerList = new EventListenerList();
	protected transient ChangeEvent changeEvent;

	private JTextField jTextField;

	/**
	 * Adds a <code>ChangeListener</code> to the button.
	 * 
	 * @param l
	 *            the listener to be added
	 */
	public void addChangeListener(ChangeListener l) {
		listenerList.add(ChangeListener.class, l);
	}

	/**
	 * Removes a ChangeListener from the button.
	 * 
	 * @param l
	 *            the listener to be removed
	 */
	public void removeChangeListener(ChangeListener l) {
		listenerList.remove(ChangeListener.class, l);
	}

	/**
	 * Returns an array of all the <code>ChangeListener</code>s added to this
	 * AbstractButton with addChangeListener().
	 *
	 * @return all of the <code>ChangeListener</code>s added or an empty array if no
	 *         listeners have been added
	 * @since 1.4
	 */
	public ChangeListener[] getChangeListeners() {
		return listenerList.getListeners(ChangeListener.class);
	}

	/**
	 * Notifies all listeners that have registered interest for notification on this
	 * event type. The event instance is lazily created.
	 * 
	 * @see EventListenerList
	 */
	protected void fireStateChanged() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				// Lazily create the event:
				if (changeEvent == null)
					changeEvent = new ChangeEvent(jTextField);
				((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
			}
		}
	}

	public void install(JTextField jTextField, Function<String, List<Entry<String, ImageIcon>>> busqueda) {
		this.jTextField = jTextField;
		System.out.println("-- " + jTextField.getParent());
		addDocumentListener();
		Observable.create(e -> JTextFieldWrapper.this.addChangeListener(event -> {
			JTextField jtf = (JTextField) event.getSource();
			e.onNext(jtf.getText());
		})).subscribe(onNext -> {
			System.out.println("ahi vmos " + onNext);
		});
	}

	private void addDocumentListener() {
		DocumentListener dl = new DocumentListener() {
			private int lastChange = 0, lastNotifiedChange = 0;

			@Override
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				lastChange++;
				EventQueue.invokeLater(() -> {
					if (lastNotifiedChange != lastChange) {
						lastNotifiedChange = lastChange;
						fireStateChanged();
					}
				});
			}
		};

		jTextField.addPropertyChangeListener("document", (PropertyChangeEvent e) -> {
			Document d1 = (Document) e.getOldValue();
			Document d2 = (Document) e.getNewValue();
			if (d1 != null)
				d1.removeDocumentListener(dl);
			if (d2 != null)
				d2.addDocumentListener(dl);
			dl.changedUpdate(null);
		});

		Document d = jTextField.getDocument();
		if (d != null)
			d.addDocumentListener(dl);
	}

}
