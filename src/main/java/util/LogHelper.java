package util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

public class LogHelper {
    private static Log log;

    public static Log getLog() {
        if (log != null)
            return log;
        try {
            PropertyConfigurator.configure("/log4j.properties");
            log = LogFactory.getLog(Class.forName(LogHelper.class.getName()));
            log.info("初始化log成功");
        } catch (LogConfigurationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return log;
    }

    public static Log createLog(Class cl) {
        try {
            PropertyConfigurator.configure("/log4j.properties");
            return LogFactory.getLog(Class.forName(cl.getName()));
        } catch (LogConfigurationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
