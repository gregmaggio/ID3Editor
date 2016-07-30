/**
 * 
 */
package ca.datamagic.mp3.ui;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import ca.datamagic.musicbrainz.dao.MusicBrainzModule;
import ca.datamagic.musicbrainz.dto.ArtistCreditDTO;
import ca.datamagic.musicbrainz.dto.ReleaseDTO;
import ca.datamagic.musicbrainz.dto.ReleaseListDTO;

/**
 * @author Greg
 *
 */
public class MusicBrainzDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final String[] _columnNames = new String[] { "Score", "ID", "Title", "Artist" };
	private static final Logger _logger = LoggerFactory.getLogger(MusicBrainzDialog.class);
	private JPanel _searchPanel = null;
	private JTextField _searchText = null;
	private JButton _searchButton = null;
	private DefaultTableModel _dataModel = null;
	private JTable _musicBrainzTable = null;
	private JScrollPane _scrollPane = null;
	private JPanel _buttonPanel = null;
	private JButton _okButton = null;
	private JButton _cancelButton = null;
	private boolean _cancel = false;
	private List<ReleaseDTO> _releases = null;
	private ReleaseDTO _selectedRelease = null;
	
	public MusicBrainzDialog() {
		super();
		initialize();
	}
	
	public MusicBrainzDialog(Frame owner) {
		super(owner, true);
		initialize();
	}
	
	public List<ReleaseDTO> getReleases() {
		return _releases;
	}
	
	public ReleaseDTO getSelectedRelease() {
		return _selectedRelease;
	}
	
	private void initialize() {
		setTitle("Music Brainz Search");
		_searchPanel = new JPanel();
		_searchText = new JTextField("", 25);
		_searchButton = new JButton("Search...");
		_searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String searchText = _searchText.getText();
				if ((searchText == null) || (searchText.length() < 1)) {
					JOptionPane.showMessageDialog(null, "Please enter the search text", "Search Text Needed", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				doSearch(searchText);
			}
		});
		_searchPanel.add(_searchText);
		_searchPanel.add(_searchButton);
		
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
		
		_musicBrainzTable = new JTable(_dataModel);
		_musicBrainzTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_scrollPane = new JScrollPane(_musicBrainzTable);
		_musicBrainzTable.setFillsViewportHeight(true);
		
		_okButton = new JButton("OK");
		_okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (_musicBrainzTable.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(null, "Please select a row", "Selection Needed", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				_selectedRelease = _releases.get(_musicBrainzTable.getSelectedRow());
				setVisible(false);
			}
		});
		_cancelButton = new JButton("Cancel");
		_cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				_cancel = true;
				setVisible(false);
			}
		});
		
		_buttonPanel = new JPanel();
		_buttonPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		_buttonPanel.add(_cancelButton);
		_buttonPanel.add(_okButton);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.NORTH, _searchPanel);
		getContentPane().add(BorderLayout.CENTER, _scrollPane);
		getContentPane().add(BorderLayout.SOUTH, _buttonPanel);
	}
	
	private void showLoading() {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}
	
	private void showIdle() {
		setCursor(Cursor.getDefaultCursor());
	}
	
	private void doSearch(String searchText) {
		try {			
			showLoading();
			while (_dataModel.getRowCount() > 0) {
				_dataModel.removeRow(0);
			}
			ReleaseListDTO releaseList = MusicBrainzModule.getDAO().query(searchText);
			if (releaseList != null) {
				List<ReleaseDTO> releases = releaseList.getReleases();
				if (releases != null) {
					for (int ii = 0; ii < releases.size(); ii++) {
						ReleaseDTO release = releases.get(ii);
						String score = Integer.toString(release.getScore().intValue());
						String id = release.getId();
						String title = release.getTitle();
						List<ArtistCreditDTO> artistCredits = release.getArtistCredits();
						if ((artistCredits != null) && (artistCredits.size() > 0)) {
							ArtistCreditDTO artistCredit = artistCredits.get(0);
							String artistName = "";
							if (artistCredit.getArtist() != null) {
								if (artistCredit.getArtist().getName() != null) {
									artistName = artistCredit.getArtist().getName();
								}
							}
							_dataModel.addRow(new Object[] {
									score, id, title, artistName 
								});
						}
					}
					_releases = releases;
				}
			}
		} catch (Throwable t) {
			_logger.error("Error", t);
		} finally {
			showIdle();
		}
	}
	
	public boolean showDialog(String search) {
		_searchText.setText(search);
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
			return true;
		}
		return false;
	}
}
