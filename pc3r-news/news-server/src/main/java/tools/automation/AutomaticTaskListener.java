package tools.automation;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import java.util.Timer;

import static tools.automation.AutomaticTask.startTimer;

@Slf4j
public class AutomaticTaskListener implements ServletContextListener {

    private Timer timer;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Start Automatic Task");
        timer = startTimer();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        timer.cancel();
        log.info("Destroy Automatic Task Schedule");
    }
}
