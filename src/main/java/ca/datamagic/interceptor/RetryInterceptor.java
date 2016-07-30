/**
 * 
 */
package ca.datamagic.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @author Greg
 *
 */
public class RetryInterceptor implements MethodInterceptor {
	private static Logger _logger = LogManager.getLogger(RetryInterceptor.class);
	private static int _maxTries = 5;
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		for (int tries = 0; tries < _maxTries; tries++) {
			try {
				return invocation.proceed();
			} catch (Throwable t) {
				if (tries == (_maxTries - 1)) {
					throw t;
				}
				_logger.warn("Exception", t);
			}
		}
		return null;
	}

}
