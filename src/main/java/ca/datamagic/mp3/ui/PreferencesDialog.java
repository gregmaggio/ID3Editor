/**
 * 
 */
package ca.datamagic.mp3.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * @author Greg
 *
 */
public class PreferencesDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final String[] _columnNames = new String[] { "Folder Name", "" };
	private static final ImageIcon _browseIcon = new ImageIcon(PreferencesDialog.class.getClassLoader().getResource("browse_small.png"));
	private static final ImageIcon _deleteIcon = new ImageIcon(PreferencesDialog.class.getClassLoader().getResource("delete_small.png"));
	private static final Logger _logger = LoggerFactory.getLogger(PreferencesDialog.class);
	private JFileChooser _fileChooser = null;
	private DefaultTableModel _dataModel = null;
	private JTable _musicFoldersTable = null;
	private JScrollPane _scrollPane = null;
	private JPanel _buttonPanel = null;
	private JButton _okButton = null;
	private JButton _cancelButton = null;
	private boolean _dirty = false;
	private boolean _cancel = false;
	private Preferences _preferences = null;
	
	public PreferencesDialog() {
		super();
		initialize();
	}
	
	public PreferencesDialog(Frame owner) {
		super(owner, true);
		initialize();
	}

	private void initialize() {
		setTitle("Preferences (double-click list to add new folder)");
		_fileChooser = new JFileChooser();
		_fileChooser.setMultiSelectionEnabled(false);
		_fileChooser.setDialogTitle("Select A Folder");
		_fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		_dataModel = new DefaultTableModel(_columnNames, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
            @Override
            public Class<? extends Object> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
		};
		
		_musicFoldersTable = new JTable(_dataModel);
		_musicFoldersTable.getColumnModel().getColumn(1).setCellRenderer(new ButtonCell());
		_musicFoldersTable.getColumnModel().getColumn(1).setCellEditor(new ButtonCell());
		_musicFoldersTable.getColumnModel().getColumn(1).setWidth(16);
		_musicFoldersTable.getColumnModel().getColumn(1).setPreferredWidth(16);
		_musicFoldersTable.getColumnModel().getColumn(1).setMinWidth(16);
		_musicFoldersTable.getColumnModel().getColumn(1).setMaxWidth(16);
		_musicFoldersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_musicFoldersTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = _musicFoldersTable.rowAtPoint(e.getPoint());
			    int col = _musicFoldersTable.columnAtPoint(e.getPoint());
				if ((row > -1) && (col == 1)) {
					String folderName = (String)_musicFoldersTable.getModel().getValueAt(row, 0);
					if ((folderName != null) && (folderName.length() > 0)) {
						_dataModel.removeRow(row);
						_dirty = true;
					} else {
						int result = _fileChooser.showOpenDialog(_musicFoldersTable);
						if (result == JFileChooser.APPROVE_OPTION) {
							_musicFoldersTable.getModel().setValueAt(_fileChooser.getSelectedFile().getAbsolutePath(), row, 0);
							_dirty = true;
						}
					}
					return;
				}
				if (e.getClickCount() == 2) {
					_dataModel.addRow(new Object[] { "", "" });
				}
			}
		});
		
		_scrollPane = new JScrollPane(_musicFoldersTable);
		_musicFoldersTable.setFillsViewportHeight(true);
		
		_okButton = new JButton("OK");
		_okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		_cancelButton = new JButton("Cancel");
		_cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (_dirty) {
					int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel?", "Preferences Changed", JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.YES_OPTION) {
						_cancel = true;
						setVisible(false);
					}
				} else {
					_cancel = true;
					setVisible(false);
				}
			}
		});
		
		_buttonPanel = new JPanel();
		_buttonPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		_buttonPanel.add(_cancelButton);
		_buttonPanel.add(_okButton);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.CENTER, _scrollPane);
		getContentPane().add(BorderLayout.SOUTH, _buttonPanel);
	}
	
	private class ButtonCell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
		private static final long serialVersionUID = 1L;
		private JLabel _label = null;

        ButtonCell() {
        	_label = new JLabel();
        	_label.setSize(16, 16);
        	_label.setPreferredSize(new Dimension(16, 16));
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        	String folderName = (String)table.getValueAt(row, 0);
        	if ((folderName != null) && (folderName.length() > 0)) {
        		_label.setIcon(_deleteIcon);
        	} else {
        		_label.setIcon(_browseIcon);
        	}
        	_logger.debug("buttonHeight: " + _label.getHeight());
        	_logger.debug("buttonWidth: " + _label.getWidth());
            return _label;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        	String folderName = (String)table.getValueAt(row, 0);
        	if ((folderName != null) && (folderName.length() > 0)) {
        		_label.setIcon(_deleteIcon);
        	} else {
        		_label.setIcon(_browseIcon);
        	}
        	_logger.debug("buttonHeight: " + _label.getHeight());
        	_logger.debug("buttonWidth: " + _label.getWidth());
            return _label;
        }
    }
	
	public boolean showDialog(Preferences preferences) {
		_preferences = preferences;
		while (_dataModel.getRowCount() > 0) {
			_dataModel.removeRow(0);
		}
		if (_preferences != null) {
			for (String folderName : _preferences.getMusicFolders()) {
				_dataModel.addRow(new Object[] { folderName, "" });
			}
		}		
		setSize(320, 240);
		Container parent = getParent();
		if (parent != null) {
			int left = 0;
			int top = 0;
			if (parent.getWidth() > 320) {
				left = (int)((parent.getWidth() - 320) / 2);
				top = (int)((parent.getHeight() - 240) / 2);
			}
			setLocation(left, top);
		}
		setModal(true);
		setVisible(true);
		if (!_cancel) {
			if (_dirty) {
				preferences.getMusicFolders().clear();
				for (int ii = 0; ii < _dataModel.getRowCount(); ii++) {
					String folderName = (String)_dataModel.getValueAt(ii, 0);
					if ((folderName != null) && (folderName.length() > 0)) {
						preferences.getMusicFolders().add(folderName);
					}
				}
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		try {
			DOMConfigurator.configure("src/main/resources/META-INF/log4j.cfg.xml");
			
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			Preferences preferences = new Preferences();
			PreferencesDialog dialog = new PreferencesDialog();
			dialog.setSize(320, 240);
			if (dialog.showDialog(preferences)) {
				System.out.println("Preferences Updated!");
			}
		} catch (Throwable t) {
			System.out.println("Exception: " + t.getMessage());
			t.printStackTrace();
		}
		System.exit(0);
	}
}
