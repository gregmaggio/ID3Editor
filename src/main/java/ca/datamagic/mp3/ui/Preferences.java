/**
 * 
 */
package ca.datamagic.mp3.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Greg
 *
 */
public class Preferences {
	private static final String SETTINGSPATH = ".id3editor";
	private static final String FILENAME = "preferences.properties";
	private List<String> _musicFolders = new ArrayList<String>();
	
	public Preferences() {
		String userHome = System.getProperty("user.home");
		if ((userHome != null) && (userHome.length() > 0)) {
			String musicFolder = MessageFormat.format("{0}/Music", userHome);
			if ((new File(musicFolder)).exists()) {
				_musicFolders.add(musicFolder);
			}
		}
	}

	public List<String> getMusicFolders() {
		return _musicFolders;
	}
	
	public void load() throws Exception {
		InputStream input = null;
		try {
			String userHome = System.getProperty("user.home");
			if ((userHome != null) && (userHome.length() > 0)) {
				String settingsFolder = MessageFormat.format("{0}/{1}", userHome, SETTINGSPATH);
				String settingsFileName = MessageFormat.format("{0}/{1}", settingsFolder, FILENAME);
				if ((new File(settingsFileName)).exists()) {
					input = new FileInputStream(settingsFileName);
					Properties properties = new Properties();
					properties.load(input);
					Integer musicFoldersLength = Integer.parseInt((String)properties.get("music.folders.length"));
					List<String> musicFolders = new ArrayList<String>();
					for (int ii = 0; ii < musicFoldersLength.intValue(); ii++) {
						String folderName = (String)properties.get(MessageFormat.format("music.folder.{0}", ii));
						if ((new File(folderName)).exists()) {
							musicFolders.add(folderName);
						}
					}
					_musicFolders = musicFolders;
				}
			}
		} finally {
			if (input != null) {
				input.close();
			}
		}
	}
	
	public void save() throws Exception {
		OutputStream output = null;
		try {
			String userHome = System.getProperty("user.home");
			if ((userHome == null) || (userHome.length() < 1)) {
				throw new Exception("No user home path. Check your system settings.");
			}
			String settingsFolder = MessageFormat.format("{0}/{1}", userHome, SETTINGSPATH);
			String settingsFileName = MessageFormat.format("{0}/{1}", settingsFolder, FILENAME);
			File settingsFolderFile = new File(settingsFolder);
			if (!settingsFolderFile.exists()) {
				settingsFolderFile.mkdir();
			}
			Properties properties = new Properties();
			properties.put("music.folders.length", Integer.toString(_musicFolders.size()));
			for (int ii = 0; ii < _musicFolders.size(); ii++) {
				properties.put(MessageFormat.format("music.folder.{0}", ii), _musicFolders.get(ii));
			}
			output = new FileOutputStream(settingsFileName);
			properties.store(output, "ca.datamagic.mp3.ui preferences");
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}
}
