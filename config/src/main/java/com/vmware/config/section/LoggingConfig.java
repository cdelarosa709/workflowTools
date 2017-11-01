package com.vmware.config.section;

import com.vmware.config.ConfigurableProperty;
import com.vmware.util.logging.LogLevel;

public class LoggingConfig {

    @ConfigurableProperty(commandLine = "-t,--trace", help = "Sets log level to trace")
    public boolean traceLogLevel;

    @ConfigurableProperty(commandLine = "-d,--debug", help = "Sets log level to debug")
    public boolean debugLogLevel;

    @ConfigurableProperty(commandLine = "-l,--log,--log-level", help = "Sets log level to any of the following, ERROR,INFO,DEBUG,TRACE")
    public String logLevel;

    public LogLevel determineLogLevel() {
        if (traceLogLevel) {
            logLevel = LogLevel.TRACE.name();
        } else if (debugLogLevel) {
            logLevel = LogLevel.DEBUG.name();
        }
        return LogLevel.valueOf(logLevel);
    }
}
