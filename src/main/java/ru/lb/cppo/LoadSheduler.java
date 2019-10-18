package ru.lb.cppo;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.MasterDataUtils;
import ru.lb.cppo.backend.beans.JiraProjectBean;
import ru.lb.cppo.backend.data.JiraProject;
import ru.lb.cppo.backend.service.JiraDBService;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.List;

@Startup
@Singleton
public class LoadSheduler {

    @Inject
    private MasterDataUtils masterDataOperator; // = new MasterDataUtils();
    @Inject
    private JiraProjectBean jiraProjectBean;

    @Inject
    private JiraDBService jiraDBService;

    private static final Logger log4jLogger = Logger.getLogger(MyUI.class);

    public LoadSheduler() { }

//    @Schedule(second="13", minute="13", hour="5", dayOfWeek="Mon-Fri", month="*", year="*")
    private void startLoadJiraSheduler() {
        log4jLogger.log(Level.INFO, "!=====================================================!");
        log4jLogger.log(Level.INFO, "------------- Старт шедулера -------------------");

        log4jLogger.log(Level.INFO,"Запущен перерасчет данных для формирования отчета");

        List<JiraProject> jpList = jiraProjectBean.getActiveRow();
        if(jpList.isEmpty()) {
            jiraDBService.initialSQL();
        } else {
            masterDataOperator.exportWorkSettings();
        }
        masterDataOperator.fillPGBase();
        log4jLogger.log(Level.INFO,"Запущена подгрузка справочных данных ");
        masterDataOperator.fillSettings();
        log4jLogger.log(Level.INFO, "------------- Стоп шедулера -------------------");
        log4jLogger.log(Level.INFO, "!=====================================================!");
    }

}
