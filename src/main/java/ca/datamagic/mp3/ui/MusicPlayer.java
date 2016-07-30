/**
 * 
 */
package ca.datamagic.mp3.ui;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javazoom.jl.player.Player;

/**
 * @author Greg
 *
 */
public class MusicPlayer implements Runnable {
	private static Logger _logger = LogManager.getLogger(MusicPlayer.class);
	private boolean _running = false;
	private boolean _started = false;
	private boolean _stopped = false;
	private List<MusicPlayerEvents> _listeners = new ArrayList<MusicPlayerEvents>();
	private Player _player = null;
	private String _fileName = null;
	
	public MusicPlayer() {		
	}

	private void fireStarted() {
		for (int ii = 0; ii < _listeners.size(); ii++) {
			try {
				_listeners.get(ii).started(this);
			} catch (Throwable t) {
				_logger.warn("Exception", t);
			}
		}
	}
	
	private void fireCompleted() {
		for (int ii = 0; ii < _listeners.size(); ii++) {
			try {
				_listeners.get(ii).completed(this);
			} catch (Throwable t) {
				_logger.warn("Exception", t);
			}
		}
	}
	
	public void addListener(MusicPlayerEvents listener) {
		_listeners.add(listener);
	}
	
	public void removeListener(MusicPlayerEvents listener) {
		_listeners.remove(listener);
	}
	
	public String getFileName() {
		return _fileName;
	}
	
	public void setFileName(String newVal) {
		_fileName = newVal;
	}
	
	public boolean isRunning() {
		return _running;
	}
	
	public boolean hasStarted() {
		return _started;
	}
	
	public boolean hasStopped() {
		return _stopped;
	}
	
	public void start() {
		if (!_running) {
			if (!_started) {
				(new Thread(this)).start();
				_started = true;
			}
		}
	}
	
	public void stop() {
		if (_running) {
			if (!_stopped) {
				if (_player != null) {
					_player.close();
				}
				_stopped = true;
			}
		}
	}
	
	@Override
	public void run() {
		_running = true;
		fireStarted();
		try {
			_player = new Player(new FileInputStream(_fileName));
			_player.play();
		} catch (Throwable t) {
			_logger.error("Exception", t);
		}
		if (_player != null) {
			_player.close();
		}
		_player = null;
		_running = false;
		_started = false;
		_stopped = false;
		fireCompleted();
	}
}
