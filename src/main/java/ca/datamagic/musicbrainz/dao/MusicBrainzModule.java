/**
 * 
 */
package ca.datamagic.musicbrainz.dao;

import ca.datamagic.interceptor.RetryInterceptor;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;

/**
 * @author Greg
 *
 */
public class MusicBrainzModule extends AbstractModule {
	private static Injector _injector = null;
	private static MusicBrainzDAO _dao = null;
	
	static {
		_injector = Guice.createInjector(new MusicBrainzModule());
		_dao = _injector.getInstance(MusicBrainzDAO.class);
	}
    
	public static Injector getInjector() {
		return _injector;
	}
	
	public static MusicBrainzDAO getDAO() {
		return _dao;
	}
	
	@Override
	protected void configure() {
		bindInterceptor(Matchers.any(), Matchers.any(), new RetryInterceptor());
		bind(MusicBrainzDAO.class).to(MusicBrainzDAOImpl.class);
	}
}
