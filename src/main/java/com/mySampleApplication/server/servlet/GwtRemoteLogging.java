package com.mySampleApplication.server.servlet;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.google.gwt.logging.server.StackTraceDeobfuscator;
import com.google.gwt.logging.shared.RemoteLoggingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * The Class GwtRemoteLogging.
 */
@SuppressWarnings("serial")
public class GwtRemoteLogging extends RemoteServiceServlet implements RemoteLoggingService {

    /**
     * The Constant logger.
     */
    private StackTraceDeobfuscator deobfuscator = null;
    private final static Logger logger = Logger.getLogger(GwtRemoteLogging.class.getName());

    @Override
    public void init(ServletConfig config) throws ServletException {
        PropertyConfigurator.configure(getClass().getResource("/com/mySampleApplication/log4j.properties"));
        super.init(config);
    }

    /**
     * Logs a Log Record which has been serialized using GWT RPC on the server.
     *
     * @return either an error message, or null if logging is successful.
     */
    public final String logOnServer(LogRecord lr) {
        try {
            if (lr.getLevel().equals(Level.SEVERE)) {
                logger.error(lr.getMessage(), lr.getThrown());
            } else if (lr.getLevel().equals(Level.INFO)) {
                logger.info(lr.getMessage(), lr.getThrown());
            } else if (lr.getLevel().equals(Level.WARNING)) {
                logger.warn(lr.getMessage(), lr.getThrown());
            } else if (lr.getLevel().equals(Level.FINE)) {
                logger.debug(lr.getMessage(), lr.getThrown());
            } else {
                logger.trace(lr.getMessage(), lr.getThrown());
            }
        } catch (Exception e) {
            logger.error("Remote logging failed", e);
            return "Remote logging failed, check stack trace for details.";
        }
        return null;
    }

    /**
     * By default, this service does not do any deobfuscation. In order to do server side
     * deobfuscation, you must copy the symbolMaps files to a directory visible to the server and
     * set the directory using this method.
     *
     * @param symbolMapsDir
     */
    public void setSymbolMapsDirectory(String symbolMapsDir) {
        if (deobfuscator == null) {
            deobfuscator = new StackTraceDeobfuscator(symbolMapsDir);
        } else {
            deobfuscator.setSymbolMapsDirectory(symbolMapsDir);
        }
    }
}