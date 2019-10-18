package ru.lb.cppo.ui;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.data.*;

public class SettingsLayout extends HorizontalLayout {

    private static final Logger log4jLogger = Logger.getLogger(SettingsLayout.class);
    private final VerticalLayout menu =new VerticalLayout();
    private final VerticalLayout content = new VerticalLayout();
    private int activeComponentID = 1;
    final public Grid<ProjectCode> projectCodeGrid = new ProjectCodeGrid();
    final public Grid<JiraProject> jiraProjectGrid = new JiraProjectGrid();
    final public Grid<ProjectVersion> projectVersionGrid = new ProjectVersionGrid();
    final public Grid<ProjectUserRole> projectUserRoleGrid = new ProjectUserRoleGrid();
    final public Grid<ProjectCodePlan> projectPlanGrid = new ProjectCodePlanGrid();
    final public Grid<CodeAccounting> codeAccountingEditGrid = new CodeAccountingGrid();
            // Grid(ProjectCodePlan.class);
    public Button loadSettingsButton = new Button("Cправочные данные");

    public SettingsLayout(){
        super();
        init();
    }

    public void init(){
        fillMenu();
        fillContent();
        this.setSpacing(false);
        this.setMargin(false);
        this.setHeight(100.0f, Unit.PERCENTAGE);
        this.setWidth(100.0f,Unit.PERCENTAGE);

        this.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        content.setHeight(100.0f, Unit.PERCENTAGE);
        content.setWidth(100.0f,Unit.PERCENTAGE);
        content.setSizeFull();
        this.addComponents(content,menu);
        this.setExpandRatio(menu,1);
        this.setExpandRatio(content,4);
    }

    private void fillMenu() {
        Button buttonCode=new Button("Код проекта");
        buttonCode.setSizeFull();
        buttonCode.setStyleName("friendly");
        buttonCode.addListener(e->switchLayout(1));
        Button buttonProject=new Button("Проекты Jira");
        buttonProject.setSizeFull();
        buttonProject.addListener(e->switchLayout(2));
        Button buttonVersion=new Button("Версии Jira");
        buttonVersion.setSizeFull();
        buttonVersion.addListener(e->switchLayout(3));
        Button buttonRole=new Button("Роли в проекте");
        buttonRole.setSizeFull();
        buttonRole.addListener(e->switchLayout(4));
        Button buttonPlan=new Button("Плановые затраты");
        buttonPlan.setSizeFull();
        buttonPlan.addListener(e->switchLayout(5));
        Button buttonAccounting=new Button("Финансовая оценка");
        buttonAccounting.setSizeFull();
        buttonAccounting.addListener(e->switchLayout(6));
        menu.setMargin(false);
        MarginInfo mi= new MarginInfo(false,false,false,true);
        menu.setMargin(mi);
        menu.addComponents(buttonCode,buttonProject,buttonVersion,
                buttonRole,buttonPlan,buttonAccounting);
        loadSettingsButton.setSizeFull();

        Label emptyLabel = new Label("");
        emptyLabel.setHeight("10em");
        menu.addComponent(emptyLabel);

        menu.addComponent(loadSettingsButton);
    }

    private void fillContent() {
        final VerticalLayout projectCode = new VerticalLayout();
        final HorizontalLayout projectCodeMenu = new HorizontalLayout();
        projectCodeGrid.setHeight(100.0f, Unit.PERCENTAGE);
        projectCodeGrid.setWidth(100.0f,Unit.PERCENTAGE);
        projectCodeGrid.setSizeFull();
        projectCodeMenu.setMargin(true);
        projectCode.addComponents(projectCodeGrid, projectCodeMenu);

        final VerticalLayout jiraProject = new VerticalLayout();
        final HorizontalLayout jiraProjectMenu = new HorizontalLayout();
        jiraProjectGrid.setHeight(100.0f, Unit.PERCENTAGE);
        jiraProjectGrid.setWidth(100.0f,Unit.PERCENTAGE);
        jiraProjectGrid.setSizeFull();
        jiraProjectMenu.setMargin(true);
        jiraProject.addComponents(jiraProjectGrid, jiraProjectMenu);

        final VerticalLayout projectVersion = new VerticalLayout();
        final HorizontalLayout projectVersionMenu = new HorizontalLayout();
        projectVersionGrid.setHeight(100.0f, Unit.PERCENTAGE);
        projectVersionGrid.setWidth(100.0f,Unit.PERCENTAGE);
        projectVersionMenu.setMargin(true);
        projectVersion.addComponents(projectVersionGrid,projectVersionMenu);

        final VerticalLayout projectUserRole = new VerticalLayout();
        final HorizontalLayout projectUserRoleMenu = new HorizontalLayout();
        projectUserRoleGrid.setHeight(100.0f, Unit.PERCENTAGE);
        projectUserRoleGrid.setWidth(100.0f,Unit.PERCENTAGE);
        projectUserRoleMenu.setMargin(true);
        projectUserRole.addComponents(projectUserRoleGrid,projectUserRoleMenu);

        final VerticalLayout projectPlan = new VerticalLayout();
        final HorizontalLayout projectPlanMenu = new HorizontalLayout();
        projectPlanGrid.setHeight(100.0f, Unit.PERCENTAGE);
        projectPlanGrid.setWidth(100.0f,Unit.PERCENTAGE);
        projectPlanMenu.setMargin(true);
        projectPlan.addComponents(projectPlanGrid, projectPlanMenu);

        final VerticalLayout codeAccLayout = new VerticalLayout();
        final HorizontalLayout codeAccMenu = new HorizontalLayout();
        codeAccountingEditGrid.setHeight(100.0f, Unit.PERCENTAGE);
        codeAccountingEditGrid.setWidth(100.0f,Unit.PERCENTAGE);
        codeAccountingEditGrid.getEditor().setEnabled(true);
        codeAccMenu.setMargin(true);
        codeAccLayout.addComponents(codeAccountingEditGrid, codeAccMenu);

        initSettingsLayout(projectCode,true);
        initSettingsLayout(jiraProject,false);
        initSettingsLayout(projectVersion,false);
        initSettingsLayout(projectUserRole,false);
        initSettingsLayout(projectPlan,false);
        initSettingsLayout(codeAccLayout,false);

        initSettingsLayout(content,true);

        content.addComponents(projectCode,jiraProject,projectVersion,projectUserRole,projectPlan,codeAccLayout);
    }

    private void initSettingsLayout(VerticalLayout co, Boolean visible){
        co.setSpacing(false);
        co.setMargin(false);
        co.setHeight(100.0f, Unit.PERCENTAGE);
        co.setWidth(100.0f,Unit.PERCENTAGE);
        co.setVisible(visible);
    }

    private void switchLayout(int componentID) {
        int countComponents=content.getComponentCount();
        if (componentID != activeComponentID) {
            if (countComponents>0 && componentID<=countComponents) {
                menu.getComponent(componentID-1).setStyleName("friendly");
                menu.getComponent(activeComponentID-1).removeStyleName("friendly");
                content.getComponent(activeComponentID-1).setVisible(false);
                activeComponentID = componentID;
                content.getComponent(componentID-1).setVisible(true);
            }
//            show("Выбрано ", menu.getComponent(componentID-1).getCaption() +" "+ countComponents, Notification.Type.TRAY_NOTIFICATION);
        }
    }

}
