/**
 * 
 */
package ca.datamagic.musicbrainz.dto;

/**
 * @author Greg
 *
 */
public class AliasDTO {
	private String _alias = null;
	private String _sortName = null;
	
	public AliasDTO() {
	}

	public String getAlias() {
		return _alias;
	}
	
	public String getSortName() {
		return _sortName;
	}
	
	public void setAlias(String newVal) {
		_alias = newVal;
	}
	
	public void setSortName(String newVal) {
		_sortName = newVal;
	}
}
