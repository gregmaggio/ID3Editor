/**
 * 
 */
package ca.datamagic.musicbrainz.dao;

import ca.datamagic.musicbrainz.dto.ReleaseListDTO;

/**
 * @author Greg
 *
 */
public interface MusicBrainzDAO {
	public ReleaseListDTO query(String queryText) throws Exception;
}
