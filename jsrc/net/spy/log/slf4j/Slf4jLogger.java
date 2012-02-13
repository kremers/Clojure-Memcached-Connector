package net.spy.log.slf4j;

import net.spy.memcached.compat.log.AbstractLogger;
import net.spy.memcached.compat.log.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation using
 * <a href="http://www.slf4j.org/">SLF4J</a>.
 *
 * @author Michael Phan-Ba
 */
public class Slf4jLogger extends AbstractLogger {

	private Logger slf4jLogger;

	/**
	 * Get an instance of Slf4jLogger.
	 */
	public Slf4jLogger(String name) {
		super(name);
		slf4jLogger = LoggerFactory.getLogger(name);
	}

	/**
	 * True if the underlying logger would allow debug messages through.
	 */
	@Override public boolean isDebugEnabled() {
		return slf4jLogger.isDebugEnabled();
	}

	/**
	 * True if the underlying logger would allow info messages through.
	 */
	@Override public boolean isInfoEnabled() {
		return slf4jLogger.isInfoEnabled();
	}

	/**
	 * Wrapper around SLF4J.
	 *
	 * @param level net.spy.compat.log.AbstractLogger level.
	 * @param message object message
	 * @param e optional throwable
	 */
	@Override public void log(Level level, Object message, Throwable e) {
		switch (level) {
			case DEBUG:
				slf4jLogger.debug("{}", message, e);
				break;
			case INFO:
				slf4jLogger.info("{}", message, e);
				break;
			case WARN:
				slf4jLogger.warn("{}", message, e);
				break;
			case ERROR:
				slf4jLogger.error("{}", message, e);
				break;
			case FATAL:
				slf4jLogger.error("{}", message, e);
				break;
			default:
				slf4jLogger.error("Unhandled log level: {}", level);
				slf4jLogger.error("{}", message, e);
		}
	}

}
