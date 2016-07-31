/**
 * 
 */
package ca.datamagic.mp3.ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;

import ca.datamagic.id3.dto.ID3TagDTO;
import ca.datamagic.mp3.dao.MP3FileDAO;
import ca.datamagic.mp3.dto.MP3FileDTO;
import ca.datamagic.musicbrainz.dto.ArtistCreditDTO;
import ca.datamagic.musicbrainz.dto.ArtistDTO;
import ca.datamagic.musicbrainz.dto.ReleaseDTO;

/**
 * @author Greg
 *
 */
public class Editor extends JApplet implements MusicPlayerEvents {
	private static final long serialVersionUID = 1L;
	private static final Logger _logger = LoggerFactory.getLogger(Editor.class);
	private static final String[] _columnNames = new String[] {
		"Path",
		"File",
		"LastModified",
		"Title",
		"Artist",
		"Genre",
		"Album",
		"Music Brainz Artist Id",
		"Music Brainz Disc Id",
		"Music Brainz Track Id",
		"Music Brainz Release Id",
		"ID3 Version",
		"HasBackup",
		"Dirty"
	};
	private static SimpleDateFormat _dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static JFrame _frame = null; 
	private MP3FileDAO _dao = null;
	private Preferences _preferences = null;
	private Hashtable<String, Integer> _columnIndexes = null;
	private DefaultTableModel _dataModel = null;
	private JMenuBar _menuBar = null;
	private JMenu _fileMenu = null;
	private JMenuItem _saveMenuItem = null;
	private JMenuItem _saveAllMenuItem = null;
	private JMenuItem _propertiesMenuItem = null;
	private JMenuItem _exitMenuItem = null;
	private JMenu _editMenu = null;
	private JMenuItem _undoMenuItem = null;
	private JMenuItem _findMenuItem = null;
	private JMenuItem _deleteMenuItem = null;
	private JMenuItem _deleteBackupMenuItem = null;
	private JMenuItem _musicBrainzLookupMenuItem = null;
	private JMenuItem _playSongMenuItem = null;
	private JMenu _helpMenu = null;
	private JMenuItem _helpContentsMenuItem = null;
	private JMenuItem _helpAboutMenuItem = null;
	private JTable _mp3Table = null;
	private JScrollPane _scrollPane = null;
	private JPanel _statusPanel = null; 
	private JLabel _statusLabel = null; 
	private MusicPlayer _player = null;
	private FindDialog _findDialog = null;
	
	public Editor() throws Exception {
		_player = new MusicPlayer();
		_player.addListener(this);
		
		_columnIndexes = new Hashtable<String, Integer>();
		for (int ii = 0; ii < _columnNames.length; ii++) {
			_columnIndexes.put(_columnNames[ii], ii);
		}
		_dao = new MP3FileDAO();
		_preferences = new Preferences();
		_preferences.load();
		_menuBar = new JMenuBar();
		
		_fileMenu = new JMenu("File");
		_fileMenu.setMnemonic(KeyEvent.VK_F);
		
		_saveMenuItem = new JMenuItem("Save");
		_saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		_saveMenuItem.setMnemonic(KeyEvent.VK_S);
		_saveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				saveMenuItemActionPerformed(event);
			}
		});
		
		_saveAllMenuItem = new JMenuItem("Save All");
		_saveAllMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
		_saveAllMenuItem.setMnemonic(KeyEvent.VK_A);
		_saveAllMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				saveAllMenuItemActionPerformed(event);
			}
		});
		
		_propertiesMenuItem = new JMenuItem("Properties");
		_propertiesMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, ActionEvent.ALT_MASK));
		_propertiesMenuItem.setMnemonic(KeyEvent.VK_P);
		_propertiesMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				propertiesMenuItemActionPerformed(event);
			}
		});
		
		_exitMenuItem = new JMenuItem("Exit");
		_exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		_exitMenuItem.setMnemonic(KeyEvent.VK_X);
		_exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				exitMenuItemActionPerformed(event);
			}
		});
		
		_fileMenu.add(_saveMenuItem);
		_fileMenu.add(_saveAllMenuItem);
		_fileMenu.addSeparator();
		_fileMenu.add(_propertiesMenuItem);
		_fileMenu.addSeparator();
		_fileMenu.add(_exitMenuItem);
		
		_editMenu = new JMenu("Edit");
		_editMenu.setMnemonic(KeyEvent.VK_E);
		
		_undoMenuItem = new JMenuItem("Undo");
		_undoMenuItem.setMnemonic(KeyEvent.VK_U);
		_undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		_undoMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				undoMenuItemActionPerformed(event);
			}
		});
		
		_findMenuItem = new JMenuItem("Find");
		_findMenuItem.setMnemonic(KeyEvent.VK_F);
		_findMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		_findMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				findMenuItemActionPerformed(event);
			}
		});
		
		_deleteMenuItem = new JMenuItem("Delete");
		_deleteMenuItem.setMnemonic(KeyEvent.VK_D);
		_deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		_deleteMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				deleteMenuItemActionPerformed(event);
			}
		});
		
		_deleteBackupMenuItem = new JMenuItem("Delete Backup");
		_deleteBackupMenuItem.setMnemonic(KeyEvent.VK_B);
		_deleteBackupMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, ActionEvent.ALT_MASK));
		_deleteBackupMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				deleteBackupMenuItemActionPerformed(event);
			}
		});
		
		_musicBrainzLookupMenuItem = new JMenuItem("Lookup");
		_musicBrainzLookupMenuItem.setMnemonic(KeyEvent.VK_L);
		_musicBrainzLookupMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		_musicBrainzLookupMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				musicBrainzLookupMenuItemActionPerformed(event);
			}
		});
		
		_playSongMenuItem = new JMenuItem("Play");
		_playSongMenuItem.setMnemonic(KeyEvent.VK_P);
		_playSongMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		_playSongMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				playSongMenuItemActionPerformed(event);
			}
		});
		
		_editMenu.add(_undoMenuItem);
		_editMenu.addSeparator();
		_editMenu.add(_findMenuItem);
		_editMenu.addSeparator();
		_editMenu.add(_deleteMenuItem);
		_editMenu.add(_deleteBackupMenuItem);
		_editMenu.addSeparator();
		_editMenu.add(_musicBrainzLookupMenuItem);
		_editMenu.addSeparator();
		_editMenu.add(_playSongMenuItem);
		
		_helpMenu = new JMenu("Help");
		_helpMenu.setMnemonic('H');
		
		_helpContentsMenuItem = new JMenuItem("Contents");
		_helpContentsMenuItem.setMnemonic(KeyEvent.VK_C);
		_helpContentsMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				helpContentsMenuItemActionPerformed(event);
			}
		});
		
		_helpAboutMenuItem = new JMenuItem("About");
		_helpAboutMenuItem.setMnemonic(KeyEvent.VK_A);
		_helpAboutMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				helpAboutMenuItemActionPerformed(event);
			}
		});
		
		_helpMenu.add(_helpContentsMenuItem);
		_helpMenu.add(_helpAboutMenuItem);
		
		_menuBar.add(_fileMenu);
		_menuBar.add(_editMenu);
		_menuBar.add(_helpMenu);
		
		_dataModel = new DefaultTableModel(_columnNames, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				String columnName = _columnNames[column];
				if (columnName.compareToIgnoreCase("Path") == 0)
					return false;
				if (columnName.compareToIgnoreCase("File") == 0)
					return false;
				if (columnName.compareToIgnoreCase("LastModified") == 0)
					return false;
				if (columnName.compareToIgnoreCase("HasBackup") == 0)
					return false;
				if (columnName.compareToIgnoreCase("Dirty") == 0)
					return false;
				return true;
			}
            @Override
            public Class<? extends Object> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
		};
		_mp3Table = new JTable(_dataModel);
		_mp3Table.getColumn("ID3 Version").setCellEditor(new ID3TableCellEditor());
		_mp3Table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_mp3Table.setColumnSelectionAllowed(true);
		_mp3Table.setRowSelectionAllowed(true);
	    
		new TableCellListener(_mp3Table, new AbstractAction() {			
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				TableCellListener tcl = (TableCellListener)e.getSource();
				if (tcl.getOldValue() != tcl.getNewValue()) {
					_dataModel.setValueAt(Boolean.TRUE, tcl.getRow(), _columnIndexes.get("Dirty"));
				}
			}
		});
		
		_scrollPane = new JScrollPane(_mp3Table);
		_mp3Table.setFillsViewportHeight(true);
		
		_statusPanel = new JPanel();
		_statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		_statusPanel.setPreferredSize(new Dimension(getWidth(), 16));
		_statusPanel.setLayout(new BoxLayout(_statusPanel, BoxLayout.X_AXIS));
		_statusLabel = new JLabel("");
		_statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		_statusPanel.add(_statusLabel);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.CENTER, _scrollPane);
		getContentPane().add(BorderLayout.SOUTH, _statusPanel);
		setJMenuBar(_menuBar);
		
		(new Thread(new Runnable() {
			@Override
			public void run() {
				loadFiles();
			}
		})).start();
		
		_findDialog = new FindDialog();
	}

	private void loadFiles() {
		showLoading();
		try {
			while (_dataModel.getRowCount() > 0) {
				_dataModel.removeRow(0);
			}
			if (_preferences.getMusicFolders() != null) {
				for (String musicFolder : _preferences.getMusicFolders()) {
					loadFiles(new File(musicFolder));
				}
			}
		} catch (Throwable t) {
			_logger.warn("Exception", t);
		}
		showIdle();
	}
	
	private void loadFiles(File folder) {
		if (folder.exists()) {
			_statusLabel.setText(MessageFormat.format("Processing {0}...", folder.getName()));
			File[] files = folder.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					if (file.getName().toLowerCase().contains(".mp3") && !file.getName().toLowerCase().contains(".bak")) {
						try {
							// Load the MP3 file
							Calendar lastModified = Calendar.getInstance();
							lastModified.setTimeInMillis(file.lastModified());
							MP3FileDTO mp3File = _dao.loadFile(file);
							boolean hasBackup = _dao.hasBackupFile(mp3File);
							_dataModel.addRow(new Object[] {
								file.getAbsolutePath(),
								file.getName(),
								_dateTimeFormat.format(lastModified.getTime()),
								mp3File.getID3Tag().getTitle(),
								mp3File.getID3Tag().getArtist(),
								mp3File.getID3Tag().getGenre(),
								mp3File.getID3Tag().getAlbum(),
								mp3File.getID3Tag().getMusicBrainzArtistId(),
								mp3File.getID3Tag().getMusicBrainzDiscId(),
								mp3File.getID3Tag().getMusicBrainzTrackId(),
								mp3File.getID3Tag().getMusicBrainzReleaseId(),
								mp3File.getID3Tag().getID3Version(),
								hasBackup,
								Boolean.FALSE
							});
						} catch (Throwable t) {
							_logger.warn("Exception", t);
						}
					}
				}
				if (file.isDirectory()) {
					loadFiles(file);
				}
			}
		}
	}
	
	private void saveFiles() {
		showLoading();
		try {
			MP3FileDAO dao = new MP3FileDAO();
			for (int ii = 0; ii < _dataModel.getRowCount(); ii++) {
				_statusLabel.setText(MessageFormat.format("Processing {0}/{1}...", ii + 1, _dataModel.getRowCount()));
				Boolean dirty = (Boolean)_dataModel.getValueAt(ii, _columnIndexes.get("Dirty"));
				if (dirty) {
					MP3FileDTO mp3File = new MP3FileDTO();
					mp3File.setFile(new File((String)_dataModel.getValueAt(ii, _columnIndexes.get("Path"))));
					ID3TagDTO id3Tag = new ID3TagDTO();
					id3Tag.setTitle((String)_dataModel.getValueAt(ii, _columnIndexes.get("Title")));
					id3Tag.setArtist((String)_dataModel.getValueAt(ii, _columnIndexes.get("Artist")));
					id3Tag.setGenre((String)_dataModel.getValueAt(ii, _columnIndexes.get("Genre")));
					id3Tag.setAlbum((String)_dataModel.getValueAt(ii, _columnIndexes.get("Album")));
					id3Tag.setMusicBrainzArtistId((String)_dataModel.getValueAt(ii, _columnIndexes.get("Music Brainz Artist Id")));
					id3Tag.setMusicBrainzDiscId((String)_dataModel.getValueAt(ii, _columnIndexes.get("Music Brainz Disc Id")));
					id3Tag.setMusicBrainzTrackId((String)_dataModel.getValueAt(ii, _columnIndexes.get("Music Brainz Track Id")));
					id3Tag.setMusicBrainzReleaseId((String)_dataModel.getValueAt(ii, _columnIndexes.get("Music Brainz Release Id")));
					mp3File.setID3Tag(id3Tag);
					String id3Version = (String)_dataModel.getValueAt(ii, _columnIndexes.get("ID3 Version"));
					dao.saveFile(mp3File, id3Version);
					boolean hasBackup = dao.hasBackupFile(mp3File);
					_dataModel.setValueAt(hasBackup, ii, _columnIndexes.get("HasBackup"));
					_dataModel.setValueAt(Boolean.FALSE, ii, _columnIndexes.get("Dirty"));
					Calendar lastModified = Calendar.getInstance();
					lastModified.setTimeInMillis((new File((String)_dataModel.getValueAt(ii, _columnIndexes.get("Path")))).lastModified());
					_dataModel.setValueAt(_dateTimeFormat.format(lastModified.getTime()), ii, _columnIndexes.get("LastModified"));
				}
			}
		} catch (Throwable t) {
			_logger.warn("Exception", t);
		}
		showIdle();
	}
	
	private void showPlaying() {
		_statusLabel.setText("Playing " + (new File(_player.getFileName())).getName());
		if (_frame != null) {
			_frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		} else {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}
	}
	
	private void showLoading() {
		_statusLabel.setText("Processing...");
		if (_frame != null) {
			_frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		} else {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}
	}
	
	private void showIdle() {
		_statusLabel.setText("Idle.");
		if (_frame != null) {
			_frame.setCursor(Cursor.getDefaultCursor());
		} else {
			setCursor(Cursor.getDefaultCursor());
		}
	}
	
	private void saveMenuItemActionPerformed(ActionEvent event) {
		try {
			int selectedRow = _mp3Table.getSelectedRow();
			if (selectedRow > -1) {
				Boolean dirty = (Boolean)_dataModel.getValueAt(selectedRow, _columnIndexes.get("Dirty"));
				if (dirty) {
					MP3FileDTO mp3File = new MP3FileDTO();
					mp3File.setFile(new File((String)_dataModel.getValueAt(selectedRow, _columnIndexes.get("Path"))));
					ID3TagDTO id3Tag = new ID3TagDTO();
					id3Tag.setTitle((String)_dataModel.getValueAt(selectedRow, _columnIndexes.get("Title")));
					id3Tag.setArtist((String)_dataModel.getValueAt(selectedRow, _columnIndexes.get("Artist")));
					id3Tag.setGenre((String)_dataModel.getValueAt(selectedRow, _columnIndexes.get("Genre")));
					id3Tag.setAlbum((String)_dataModel.getValueAt(selectedRow, _columnIndexes.get("Album")));
					id3Tag.setMusicBrainzArtistId((String)_dataModel.getValueAt(selectedRow, _columnIndexes.get("Music Brainz Artist Id")));
					id3Tag.setMusicBrainzDiscId((String)_dataModel.getValueAt(selectedRow, _columnIndexes.get("Music Brainz Disc Id")));
					id3Tag.setMusicBrainzTrackId((String)_dataModel.getValueAt(selectedRow, _columnIndexes.get("Music Brainz Track Id")));
					id3Tag.setMusicBrainzReleaseId((String)_dataModel.getValueAt(selectedRow, _columnIndexes.get("Music Brainz Release Id")));
					mp3File.setID3Tag(id3Tag);
					String id3Version = (String)_dataModel.getValueAt(selectedRow, _columnIndexes.get("ID3 Version"));
					MP3FileDAO dao = new MP3FileDAO();
					dao.saveFile(mp3File, id3Version);
					boolean hasBackup = dao.hasBackupFile(mp3File);
					_dataModel.setValueAt(hasBackup, selectedRow, _columnIndexes.get("HasBackup"));
					_dataModel.setValueAt(Boolean.FALSE, selectedRow, _columnIndexes.get("Dirty"));
					Calendar lastModified = Calendar.getInstance();
					lastModified.setTimeInMillis((new File((String)_dataModel.getValueAt(selectedRow, _columnIndexes.get("Path")))).lastModified());
					_dataModel.setValueAt(_dateTimeFormat.format(lastModified.getTime()), selectedRow, _columnIndexes.get("LastModified"));
				}
			}
		} catch (Throwable t) {
			_logger.error("Exception", t);
			JOptionPane.showMessageDialog(null, t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void saveAllMenuItemActionPerformed(ActionEvent event) {
		try {
			(new Thread(new Runnable() {
				@Override
				public void run() {
					saveFiles();
				}
			})).start();
		} catch (Throwable t) {
			_logger.error("Exception", t);
			JOptionPane.showMessageDialog(null, t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void propertiesMenuItemActionPerformed(ActionEvent event) {
		try {
			PreferencesDialog preferencesDialog = new PreferencesDialog(_frame);
			if (preferencesDialog.showDialog(_preferences)) {
				loadFiles();
				_preferences.save();
			}
		} catch (Throwable t) {
			_logger.error("Exception", t);
			JOptionPane.showMessageDialog(null, t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void exitMenuItemActionPerformed(ActionEvent event) {
		System.exit(0);
	}
	
	private void undoMenuItemActionPerformed(ActionEvent event) {
		try {
			int selectedRow = _mp3Table.getSelectedRow();
			if (selectedRow > -1) {
				MP3FileDAO dao = new MP3FileDAO();
				MP3FileDTO mp3File = new MP3FileDTO();
				mp3File.setFile(new File((String)_dataModel.getValueAt(selectedRow, _columnIndexes.get("Path"))));
				if (dao.hasBackupFile(mp3File)) {
					dao.rollbackFile(mp3File);
					Calendar lastModified = Calendar.getInstance();
					lastModified.setTimeInMillis((new File((String)_dataModel.getValueAt(selectedRow, _columnIndexes.get("Path")))).lastModified());
					_dataModel.setValueAt(_dateTimeFormat.format(lastModified.getTime()), selectedRow, _columnIndexes.get("LastModified"));
				}
			}
		} catch (Throwable t) {
			_logger.warn("Exception", t);
			JOptionPane.showMessageDialog(null, t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void findMenuItemActionPerformed(ActionEvent event) {
		try {
			if (!_findDialog.isVisible()) {
				_findDialog.show(_mp3Table);
			}
		} catch (Throwable t) {
			_logger.error("Exception", t);
			JOptionPane.showMessageDialog(null, t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void deleteMenuItemActionPerformed(ActionEvent event) {
		try {
			int selectedRow = _mp3Table.getSelectedRow();
			if (selectedRow > -1) {
				if (JOptionPane.showConfirmDialog(null, "Deleted files are gone forever! Are you sure you want to do this?", "Confirm File Deletion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					String path = (String)_dataModel.getValueAt(selectedRow, _columnIndexes.get("Path"));
					MP3FileDAO dao = new MP3FileDAO();
					MP3FileDTO mp3File = new MP3FileDTO();
					mp3File.setFile(new File(path));
					if (dao.hasBackupFile(mp3File)) {
						dao.deleteBackupFile(mp3File);
					}
					dao.deleteFile(mp3File);
					_dataModel.removeRow(selectedRow);
					JOptionPane.showMessageDialog(null, "Deleted!");
				}
			}
		} catch (Throwable t) {
			_logger.error("Exception", t);
			JOptionPane.showMessageDialog(null, t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void deleteBackupMenuItemActionPerformed(ActionEvent event) {
		try {
			int selectedRow = _mp3Table.getSelectedRow();
			if (selectedRow > -1) {
				String path = (String)_dataModel.getValueAt(selectedRow, _columnIndexes.get("Path"));
				MP3FileDAO dao = new MP3FileDAO();
				MP3FileDTO mp3File = new MP3FileDTO();
				mp3File.setFile(new File(path));
				if (dao.hasBackupFile(mp3File)) {
					if (JOptionPane.showConfirmDialog(null, "You cannot rollback this file once you delete the backup file! Are you sure you have played the song and want to do this?", "Confirm Backup Deletion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						dao.deleteBackupFile(mp3File);
						_dataModel.setValueAt(Boolean.FALSE, selectedRow, _columnIndexes.get("HasBackup"));
						JOptionPane.showMessageDialog(null, "Deleted!");
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Exception", t);
			JOptionPane.showMessageDialog(null, t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void musicBrainzLookupMenuItemActionPerformed(ActionEvent event) {
		try {
			int selectedRow = _mp3Table.getSelectedRow();
			if (selectedRow > -1) {
				MusicBrainzDialog dialog = new MusicBrainzDialog(_frame);
				String title = (String)_dataModel.getValueAt(selectedRow, _columnIndexes.get("Title"));
				if (dialog.showDialog(title)) {
					ReleaseDTO release = dialog.getSelectedRelease();
					List<ArtistCreditDTO> artistCredits = release.getArtistCredits();
					if ((artistCredits != null) && (artistCredits.size() > 0)) {
						ArtistCreditDTO artistCredit = artistCredits.get(0);
						if (artistCredit.getArtist() != null) {
							ArtistDTO artist = artistCredit.getArtist();
							String id = "";
							String name = "";
							if (artist.getId() != null) {
								id = artist.getId();
							}
							if (artist.getName() != null) {
								name = artist.getName();
							}
							_dataModel.setValueAt(name, selectedRow, _columnIndexes.get("Artist"));
							_dataModel.setValueAt(id, selectedRow, _columnIndexes.get("Music Brainz Artist Id"));
						}
					}
					_dataModel.setValueAt(release.getTitle(), selectedRow, _columnIndexes.get("Title"));
					_dataModel.setValueAt(release.getId(), selectedRow, _columnIndexes.get("Music Brainz Release Id"));
				}
			}
		} catch (Throwable t) {
			_logger.error("Exception", t);
			JOptionPane.showMessageDialog(null, t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void playSongMenuItemActionPerformed(ActionEvent event) {
		try {
			if (_player.isRunning()) {
				_player.stop();
			} else {
				int selectedRow = _mp3Table.getSelectedRow();
				if (selectedRow > -1) {
					_player.setFileName((String)_dataModel.getValueAt(selectedRow, _columnIndexes.get("Path")));
					_player.start();
				}
			}
		} catch (Throwable t) {
			_logger.error("Exception", t);
			JOptionPane.showMessageDialog(null, t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void helpContentsMenuItemActionPerformed(ActionEvent event) {
	}
	
	private void helpAboutMenuItemActionPerformed(ActionEvent event) {
	}
	
	@Override
	public void started(MusicPlayer player) {
		_playSongMenuItem.setText("Stop");
		showPlaying();
	}

	@Override
	public void completed(MusicPlayer player) {
		_playSongMenuItem.setText("Play");
		showIdle();
	}
	
	public static void main(String[] args) {
		try {
			SLF4JBridgeHandler.removeHandlersForRootLogger();
			SLF4JBridgeHandler.install();
			
			DOMConfigurator.configure(Editor.class.getClassLoader().getResource("log4j.cfg.xml"));
			
			Enumeration<Object> keys = System.getProperties().keys();
			while (keys.hasMoreElements()) {
				String key = (String)keys.nextElement();
				String value = System.getProperty(key);
				_logger.debug("property['" + key + "'] => " + value);
			}
			
			LookAndFeelInfo[] lookAndFeels = UIManager.getInstalledLookAndFeels();
			for (LookAndFeelInfo lookAndFeel : lookAndFeels) {
				_logger.debug(lookAndFeel.getName() + " => " + lookAndFeel.getClassName());
			}
			
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			_frame = new JFrame("ID3 Editor (Use ID3 Version 2.3 for the most support on most devices)");
			_frame.setSize(640, 480);
			_frame.getContentPane().setLayout(new BorderLayout());
			_frame.getContentPane().add(BorderLayout.CENTER, new Editor());
			_frame.pack();
			_frame.setVisible(true);
			_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (Throwable t) {
			System.out.println("Exception: " + t.getMessage());
			t.printStackTrace();
		}
	}
}
