package com.aming.orm.spi.log;


import com.aming.orm.spi.utils.AClassUtil;
import com.aming.orm.spi.utils.AStrFormatter;

public class ALog4Provider {

    public enum LogType {APPACHECOMMONS, HUTOOL, CONSOLE}

    ;


    private static LogType logType;


    static {
        if ( AClassUtil.isPresent("org.apache.commons.logging.LogFactory", ApacheCommonsLog.class.getClassLoader())) {
            ALog4Provider.setLogType(LogType.APPACHECOMMONS);
        } else if (AClassUtil.isPresent("cn.hutool.log.LogFactory", HutoolLog.class.getClassLoader())) {
            ALog4Provider.setLogType(LogType.HUTOOL);
        } else {
            ALog4Provider.setLogType(LogType.CONSOLE);
        }
    }


    public static void setLogType(LogType logType2) {
        logType = logType2;
    }


    public static ALoger getLoger(String name)   {
        switch (logType) {
            case APPACHECOMMONS:
                return ApacheCommonsLog.createLog(name);
            case HUTOOL:
                return HutoolLog.createLog(name);
            default:
                return null;
        }
    }


    private static class ApacheCommonsLog implements ALoger {

        private String tarName;

        private org.apache.commons.logging.Log logger;

        private ApacheCommonsLog(String name) {
            this.tarName = name;
            this.logger = org.apache.commons.logging.LogFactory.getLog(this.tarName);
        }

        public static ALoger createLog(String name) {
            return new ApacheCommonsLog(name);
        }


        @Override
        public boolean isFatalEnabled() {
            return logger.isFatalEnabled();
        }

        @Override
        public boolean isErrorEnabled() {
            return logger.isErrorEnabled();
        }

        @Override
        public boolean isWarnEnabled() {
            return logger.isWarnEnabled();
        }

        @Override
        public boolean isInfoEnabled() {
            return logger.isInfoEnabled();
        }

        @Override
        public boolean isDebugEnabled() {
            return logger.isDebugEnabled();
        }

        @Override
        public boolean isTraceEnabled() {
            return logger.isTraceEnabled();
        }

        @Override
        public void fatal(String format, Object... arguments) {
            logger.fatal(AStrFormatter.format(format, arguments));
        }

        @Override
        public void fatal(Throwable t) {
            logger.fatal("", t);

        }

        @Override
        public void fatal(Throwable t, String format, Object... arguments) {
            logger.fatal(AStrFormatter.format(format, arguments), t);

        }

        @Override
        public void error(String format, Object... arguments) {
            logger.error(AStrFormatter.format(format, arguments));

        }

        @Override
        public void error(Throwable t) {
            logger.error("", t);

        }

        @Override
        public void error(Throwable t, String format, Object... arguments) {
            logger.error(AStrFormatter.format(format, arguments), t);

        }

        @Override
        public void warn(String format, Object... arguments) {
            logger.warn(AStrFormatter.format(format, arguments));

        }

        @Override
        public void warn(Throwable t) {
            logger.warn("", t);

        }

        @Override
        public void warn(Throwable t, String format, Object... arguments) {
            logger.warn(AStrFormatter.format(format, arguments), t);

        }

        @Override
        public void info(String format, Object... arguments) {
            logger.info(AStrFormatter.format(format, arguments));
        }

        @Override
        public void info(Throwable t) {
            logger.info("", t);

        }

        @Override
        public void info(Throwable t, String format, Object... arguments) {
            logger.info(AStrFormatter.format(format, arguments), t);

        }

        @Override
        public void debug(String format, Object... arguments) {
            logger.debug(AStrFormatter.format(format, arguments));
        }

        @Override
        public void debug(Throwable t) {
            logger.debug("", t);

        }

        @Override
        public void debug(Throwable t, String format, Object... arguments) {
            logger.debug(AStrFormatter.format(format, arguments), t);

        }

        @Override
        public void trace(String format, Object... arguments) {
            logger.trace(AStrFormatter.format(format, arguments));
        }

        @Override
        public void trace(Throwable t) {
            logger.trace("", t);

        }

        @Override
        public void trace(Throwable t, String format, Object... arguments) {
            logger.trace(AStrFormatter.format(format, arguments), t);

        }

    }


    private static class HutoolLog implements ALoger {


        private String tarName;

        private cn.hutool.log.Log logger;

        private HutoolLog(String name) {
            this.tarName = name;
            this.logger = cn.hutool.log.LogFactory.get(tarName);
        }

        public static ALoger createLog(String name) {
            return new HutoolLog(name);
        }


        @Override
        public boolean isFatalEnabled() {
            return logger.isEnabled(cn.hutool.log.level.Level.FATAL);
        }

        @Override
        public boolean isErrorEnabled() {
            return logger.isErrorEnabled();
        }

        @Override
        public boolean isWarnEnabled() {
            return logger.isWarnEnabled();
        }

        @Override
        public boolean isInfoEnabled() {
            return logger.isInfoEnabled();
        }

        @Override
        public boolean isDebugEnabled() {
            return logger.isDebugEnabled();
        }

        @Override
        public boolean isTraceEnabled() {
            return logger.isTraceEnabled();
        }

        @Override
        public void fatal(String format, Object... arguments) {
            logger.error(format, arguments);
        }

        @Override
        public void fatal(Throwable t) {
            logger.error(t);
        }

        @Override
        public void fatal(Throwable t, String format, Object... arguments) {
            logger.error(t, format, arguments);
        }

        @Override
        public void error(String format, Object... arguments) {
            logger.error(format, arguments);

        }

        @Override
        public void error(Throwable t) {
            logger.error(t);

        }

        @Override
        public void error(Throwable t, String format, Object... arguments) {
            logger.error(t, format, arguments);

        }

        @Override
        public void warn(String format, Object... arguments) {
            logger.warn(format, arguments);

        }

        @Override
        public void warn(Throwable t) {
            logger.warn(t);

        }

        @Override
        public void warn(Throwable t, String format, Object... arguments) {
            logger.warn(t, format, arguments);

        }

        @Override
        public void info(String format, Object... arguments) {
            logger.info(format, arguments);

        }

        @Override
        public void info(Throwable t) {
            logger.info(t);

        }

        @Override
        public void info(Throwable t, String format, Object... arguments) {
            logger.info(t, format, arguments);

        }

        @Override
        public void debug(String format, Object... arguments) {
            logger.debug(format, arguments);

        }

        @Override
        public void debug(Throwable t) {
            logger.debug(t);

        }

        @Override
        public void debug(Throwable t, String format, Object... arguments) {
            logger.debug(t, format, arguments);

        }

        @Override
        public void trace(String format, Object... arguments) {
            logger.trace(format, arguments);

        }

        @Override
        public void trace(Throwable t) {
            logger.trace(t);

        }

        @Override
        public void trace(Throwable t, String format, Object... arguments) {
            logger.trace(t, format, arguments);

        }

    }

}
