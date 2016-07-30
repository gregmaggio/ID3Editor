/**
 * 
 */
package ca.datamagic.mp3.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * @author Greg
 *
 */
public class FindDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private static Logger _logger = LogManager.getLogger(FindDialog.class);
	private JLabel _findLabel = null;
	private JTextField _findText = null;
	private JCheckBox _matchCase = null;
	private JButton _findButton = null;
	private JButton _cancelButton = null;
	private JTable _mp3Table = null;
	private int _currentRow = -1;
	private int _currentCol = -1;
	
	public FindDialog() {
		_findLabel = new JLabel("Find What:");
		_findLabel.setLocation(5, 9);
		_findLabel.setSize(69, 22);
		
		_findText = new JTextField();
		_findText.setLocation(73, 9);
		_findText.setSize(190, 22);
		
		_matchCase = new JCheckBox("Match case");
		_matchCase.setLocation(5, 68);
		_matchCase.setSize(120, 22);
		
		_findButton = new JButton("Find Next");
		_findButton.setLocation(274, 9);
		_findButton.setSize(80, 21);
		_findButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				findButtonActionPerformed(event);
			}
		});
		
		_cancelButton = new JButton("Cancel");
		_cancelButton.setLocation(274, 36);
		_cancelButton.setSize(80, 21);
		_cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
			}
		});
		
		setLayout(null);
		setSize(375, 150);
		setResizable(false);
		setTitle("Find");
		add(_findLabel);
		add(_findText);
		add(_matchCase);
		add(_findButton);
		add(_cancelButton);
	}

	private void findButtonActionPerformed(ActionEvent event) {
		if (_mp3Table != null) {
			String search = _findText.getText();
			if ((search != null) && (search.length() > 0)) {
				if (_matchCase.isSelected()) {
					_logger.debug(MessageFormat.format("Performing case sensitive search of {0}", search));
				} else {
					_logger.debug(MessageFormat.format("Performing case insensitive search of {0}", search));
				}
				if (_matchCase.isSelected()) {
					search = search.toLowerCase();
				}
				boolean found = false;
				if (_currentRow < 0) {
					_currentRow = 0;
					_currentCol = 0;
				} else {
					if (_currentCol < _mp3Table.getColumnCount()) {
						_currentCol++;
					} else {
						_currentRow++;
						_currentCol = 0;
					}
				}
				_logger.debug(MessageFormat.format("Starting search at {0}, {1}", _currentRow, _currentCol));
				for (int row = _currentRow; row < _mp3Table.getRowCount(); row++) {
					for (int col = _currentCol; col < _mp3Table.getColumnCount(); col++) {
						Object value = _mp3Table.getValueAt(row, col);
						if (value != null) {
							String textValue = value.toString();
							if (_matchCase.isSelected()) {
								if (textValue.contains(search)) {
									// Found One!
									found = true;
								}
							} else {
								if (textValue.toLowerCase().contains(search)) {
									// Found One!
									found = true;
								}
							}
						}
						_currentCol = col;
						if (found) {
							break;
						}
					}
					_currentRow = row;
					if (found) {
						break;
					}
					_currentCol = 0;
				}
				if (found) {
					_logger.debug(MessageFormat.format("Found a match at {0}, {1}", _currentRow, _currentCol));
					_mp3Table.changeSelection(_currentRow, _currentCol, false, false);
				} else {
					JOptionPane.showMessageDialog(null, "No match found!");
				}
			}
		}
	}
	
	public void show(JTable mp3Table) {
		_mp3Table = mp3Table;
		_currentRow = -1;
		_currentCol = -1;
		_findText.setText("");
		_findText.requestFocus();
		setVisible(true);
	}
	
	public static void main(String[]args) {
		try {
			SLF4JBridgeHandler.removeHandlersForRootLogger();
			SLF4JBridgeHandler.install();
			DOMConfigurator.configure(Editor.class.getClassLoader().getResource("log4j.cfg.xml"));
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			FindDialog dialog = new FindDialog();
			dialog.show(null);
			dialog.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent arg0) {
					System.exit(0);
				}
			});
		} catch (Throwable t) {
			System.out.println("Exception: " + t.getMessage());
			t.printStackTrace();
		}
	}
}
