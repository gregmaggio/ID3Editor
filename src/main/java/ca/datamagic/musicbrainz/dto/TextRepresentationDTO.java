/**
 * 
 */
package ca.datamagic.musicbrainz.dto;

import com.google.gson.annotations.SerializedName;

/**
 * @author Greg
 *
 */
public class TextRepresentationDTO {
	@SerializedName("language")
	private String _language = null;
	@SerializedName("script")
	private String _script = null;
	
	public String getLanguage() {
		return _language;
	}
	
	public String getScript() {
		return _script;
	}
	
	public void setLanguage(String newVal) {
		_language = newVal;
	}
	
	public void setScript(String newVal) {
		_script = newVal;
	}
}
