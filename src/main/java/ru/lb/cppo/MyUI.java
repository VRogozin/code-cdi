package ru.lb.cppo;

import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIUI;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.MasterDataUtils;
import ru.lb.cppo.backend.SettingsDataUtils;
import ru.lb.cppo.backend.beans.CodeAccountingBean;
import ru.lb.cppo.backend.beans.CodeJiraIssuesBean;
import ru.lb.cppo.backend.beans.JiraErrorMessageBean;
import ru.lb.cppo.backend.beans.JiraProjectBean;
import ru.lb.cppo.backend.beans.ProjectCodeBean;
import ru.lb.cppo.backend.beans.ProjectCodePlanBean;
import ru.lb.cppo.backend.beans.ProjectUserRoleBean;
import ru.lb.cppo.backend.beans.ProjectVersionBean;
import ru.lb.cppo.backend.beans.ReportRowBean;
import ru.lb.cppo.backend.data.CodeAccounting;
import ru.lb.cppo.backend.data.CodeJiraIssues;
import ru.lb.cppo.backend.data.JiraErrorMessage;
import ru.lb.cppo.backend.data.JiraProject;
import ru.lb.cppo.backend.data.ProjectCode;
import ru.lb.cppo.backend.data.ProjectCodePlan;
import ru.lb.cppo.backend.data.ProjectUserRole;
import ru.lb.cppo.backend.data.ProjectVersion;
import ru.lb.cppo.backend.data.ReportRow;
import ru.lb.cppo.backend.pojo.SprProjectVersion;
import ru.lb.cppo.backend.service.JiraDBService;
import ru.lb.cppo.backend.service.JiraProjectService;
import ru.lb.cppo.backend.service.ProjectCodePlanService;
import ru.lb.cppo.backend.service.ProjectCodeService;
import ru.lb.cppo.backend.service.ProjectUserRoleService;
import ru.lb.cppo.backend.service.ProjectVersionService;
import ru.lb.cppo.ui.CodeAccountingGrid;
import ru.lb.cppo.ui.CodeJiraIssuesGrid;
import ru.lb.cppo.ui.CodeProjectReportGrid;
import ru.lb.cppo.ui.JiraErrorMessageGrid;
import ru.lb.cppo.ui.SettingsLayout;
import ru.lb.cppo.util.ClientInfo;
import ru.lb.cppo.util.GenerateXLS;

import javax.inject.Inject;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.vaadin.ui.Notification.show;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@CDIUI("cdi-code-test")
//@Push
@SuppressWarnings("unchecked")
public class MyUI extends UI {

    private String fltCodeProject;
    private String fltVersionID;
    private String fltCodeVersion;
    private String fltWorkRole;
    /**
     * Для сохранения параметра, ответсвенного за отображение вкладки
     */
    private String fltList;

    private static final Logger log4jLogger = Logger.getLogger(MyUI.class);

    @Inject
    private Service service;
    @Inject
    private CodeJiraIssuesBean codeJiraIssuesBean;
    @Inject
    private CodeAccountingBean codeAccountingBean;
    @Inject
    private JiraErrorMessageBean jiraErrorMessageBean;
    @Inject
    private JiraProjectBean jiraProjectBean;
    @Inject
    private ProjectCodeBean projectCodeBean;
    @Inject
    private ProjectCodePlanBean projectCodePlanBean;
    @Inject
    private ProjectUserRoleBean projectUserRoleBean;
    @Inject
    private ProjectVersionBean projectVersionBean;
    @Inject
    private ReportRowBean reportRowBean;

    @Inject
    private JiraDBService jiraDBService;
    @Inject
    private JiraProjectService jiraProjectService;
    @Inject
    private ProjectCodeService projectCodeService;
    @Inject
    private ProjectCodePlanService projectCodePlanService;
    @Inject
    private ProjectVersionService projectVersionService;
    @Inject
    private ProjectUserRoleService projectUserRoleService;

    private VerticalLayout root;

    private Grid<ReportRow> masterGrid = new CodeProjectReportGrid();
    private Grid<CodeJiraIssues> additGrid = new CodeJiraIssuesGrid();
    private Grid<CodeAccounting> codeAccountingGrid = new CodeAccountingGrid();

    private Grid<JiraErrorMessage> jiraErrorMessageGrid = new JiraErrorMessageGrid();

    private ComboBox comboBoxProject= new ComboBox(); //"Выберите код проекта"
    private ComboBox comboBoxVersion= new ComboBox(); //"Выберите версию"
    private ComboBox comboBoxRole= new ComboBox(); //"Выберите роль в проекте"
    private final TextField url = new TextField();

    private Button executeButton = new Button(VaadinIcons.ARROW_FORWARD, e -> executeReport()); //  "Сформировать Отчет"
    private Button loadJiraTmpButton = new Button("Подгрузить данные", e -> loadJiraTemporary());
    private Button buttonMenu;
    private Label loadJiraDate = new Label();

    private Boolean settignsRunFlag = false;

    private TabSheet reportTabSheet = new TabSheet();
    private HorizontalLayout filterLayout;
    private SettingsLayout settingsLayout;

//    private long flushRows=25;
    private String URL_DEF="http://172.19.202.252:8080/cdi-code-test/cdi-code-test";

    @Inject
    private MasterDataUtils masterDataOperator; // = new MasterDataUtils();
    private SettingsDataUtils settingsDataOperator = new SettingsDataUtils();

    @Override
    protected void init(VaadinRequest request) {

        Map<String, String[]> mapparam = request.getParameterMap();
        this.fltCodeProject=request.getParameter("code");
        this.fltVersionID=request.getParameter("versionID");
        this.fltCodeVersion=request.getParameter("version");
        this.fltWorkRole=request.getParameter("role");
        this.fltList=request.getParameter("list");

        setLayout();

        updateFilterCode();
        updateFilterVersion();
        updateFilterRole();
        updateURLField();

        executeReport();
        loadSettingsData();
    }

    /**
     * загрузка справочных данных
     */
    private void runFillSettings() {
        masterDataOperator.fillSettings();
    }

    private void setLayout() {
        root = new VerticalLayout();
        root.setHeight(100.0f, Unit.PERCENTAGE);
        root.setWidth(100.0f,Unit.PERCENTAGE);
        root.setSizeFull();
        setContent(root);

        addHeader();
        addSettingsArea();
        addDataArea();
        addFooter();
    }

    private void addHeader() {
        filterLayout = new HorizontalLayout();
        filterLayout.setSizeFull();
        filterLayout.setMargin(false);

        comboBoxProject.setPlaceholder("Выберите код проекта");
        comboBoxProject.setSizeFull();
        comboBoxProject.setEmptySelectionAllowed(true);
        comboBoxProject.addValueChangeListener(e -> updateFilterVersion());

        comboBoxVersion.setPlaceholder("Выберите версию");
        comboBoxVersion.setSizeFull();
        comboBoxVersion.setEmptySelectionAllowed(true);
        comboBoxVersion.setScrollToSelectedItem(true);
        comboBoxVersion.addValueChangeListener(e -> updateFilterRole());

        comboBoxRole.setPlaceholder("Выберите роль в проекте");
        comboBoxRole.setSizeFull();
        comboBoxRole.setEmptySelectionAllowed(true);
        comboBoxRole.addValueChangeListener(e -> updateURLField());

        url.setPlaceholder("URL данного запроса");
        url.setSizeFull();
        url.setEnabled(false);

        executeButton.setDescription("Сформировать Отчет");
        filterLayout.addComponents(comboBoxProject,comboBoxVersion,comboBoxRole,executeButton,url);
        filterLayout.setExpandRatio(comboBoxProject,1.0f);
        filterLayout.setExpandRatio(comboBoxVersion,2.0f);
        filterLayout.setExpandRatio(comboBoxRole,1.0f);
        filterLayout.setExpandRatio(url,2.0f);

        HorizontalLayout commandLayout = new HorizontalLayout();
        commandLayout.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
        commandLayout.setWidth(100f,Unit.PERCENTAGE);
        commandLayout.setSizeFull();
        commandLayout.addComponent(loadJiraTmpButton);
        loadJiraTmpButton.setVisible(false);

        HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.setDefaultComponentAlignment(Alignment.BOTTOM_RIGHT);
        menuLayout.setSizeFull();
        menuLayout.setMargin(false);

        Button buttonFilter = new Button(VaadinIcons.FILTER);
        buttonFilter.addStyleNames("v-button-borderless", "v-button-large");

        buttonMenu = new Button(VaadinIcons.MENU);
        buttonMenu.addStyleNames("v-button-borderless", "v-button-large");
        buttonMenu.addListener(e->runSettings());
        buttonMenu.setEnabled(checkIP());

        Button buttonSetting = new Button(VaadinIcons.COG);
        buttonSetting.addStyleNames("v-button-borderless", "v-button-large");
        buttonSetting.addListener(e ->showClienInfo());
        //  заблокирована кнопка настроек
        buttonSetting.setEnabled(false);

        Button buttonPrint = new Button(VaadinIcons.PRINT);
        buttonPrint.addStyleNames("v-button-borderless", "v-button-large");
        buttonPrint.addListener(e -> toExcel());

        //  --  Ввыгрузка файла ошибок
        Button buttonPrintError = new Button(VaadinIcons.BUG);
        buttonPrintError.addStyleNames("v-button-borderless", "v-button-large");
        buttonPrintError.addListener(e -> errMsg2Print());

        menuLayout.addComponents(buttonFilter, buttonPrint, buttonPrintError, buttonSetting, buttonMenu);

        HorizontalLayout commandLineLayout = new HorizontalLayout();
        commandLineLayout.setHeight(30.0f, Unit.PIXELS);
        commandLineLayout.setWidth(100.0f, Unit.PERCENTAGE);
//        commandLineLayout.setSizeFull();
        commandLineLayout.setMargin(false);
        commandLineLayout.addComponentsAndExpand(filterLayout,commandLayout,menuLayout);
        commandLineLayout.setExpandRatio(filterLayout,6.0f);
        commandLineLayout.setExpandRatio(commandLayout,1.0f);
        commandLineLayout.setExpandRatio(menuLayout,1.0f);
        root.addComponent(commandLineLayout);
        root.setExpandRatio(commandLineLayout,1.0f);
    }

    public List<SprProjectVersion> getVersionList() {
        log4jLogger.log(Level.INFO, "Вызов списка версий ");

        List<ReportRow> reportRowsList = (List) reportRowBean.getAllRow();

        return getVersionListFromReportRow(reportRowsList);
    }

    public List<SprProjectVersion> getCollectedVersionList(String codeString) {

        log4jLogger.log(Level.INFO, "Вызов списка версий для кода проекта: " + codeString);

        List<ReportRow> reportRowsList = (List) reportRowBean.getRowByCode(codeString);

        return getVersionListFromReportRow(reportRowsList);
    }

    private List<SprProjectVersion> getVersionListFromReportRow(List<ReportRow> reportRowsList){

        List<SprProjectVersion> versionList = new ArrayList<SprProjectVersion>();

        if (!reportRowsList.isEmpty()) {
            Iterator<ReportRow> reportRowIterator = reportRowsList.iterator();
            while (reportRowIterator.hasNext()) {
                ReportRow reportRow = reportRowIterator.next();
                BigDecimal projectID = reportRow.getProjectID();
                String projectMnem = "";
                String projectDesc = "";
                if (projectID != null) {
                    JiraProject jiraProject = jiraProjectBean.getJiraProjectById(projectID);
                    projectMnem = jiraProject.getProjectName();
                    projectDesc = jiraProject.getProjectDescription();
                }
                SprProjectVersion spv =
                        new SprProjectVersion(reportRow.getVersionID()
                                , reportRow.getProjectVersion()
                                , projectMnem
                                , projectDesc);
                versionList.add(spv);
            }
        }

        if (versionList.isEmpty()) {
            return Collections.emptyList();
        } else {
            return versionList.stream().distinct().collect(Collectors.toList());
        }
    }

    private boolean checkIP() {
        String ipAdr = new ClientInfo().getClientIP();
        List<String> adrAllovedList = new ArrayList<>();
        adrAllovedList.add("172.30.160.230"); // Мой новый адрес после переезда
        adrAllovedList.add("172.30.148.211");
        adrAllovedList.add("172.30.148.113");
        adrAllovedList.add("172.30.150.203");
        adrAllovedList.add("172.30.148.226"); // Мария
        return adrAllovedList.stream().anyMatch(s -> s.equalsIgnoreCase(ipAdr));

//        return "172.30.148.113".equalsIgnoreCase(ipAdr);
    }

    private void runSettings() {
        if(settignsRunFlag){
            buttonMenu.setIcon(VaadinIcons.MENU);
            settignsRunFlag=false;
        } else {
            buttonMenu.setIcon(VaadinIcons.CLOSE);
            settignsRunFlag=true;
        }
        loadJiraTmpButton.setVisible(settignsRunFlag);
        settingsLayout.setVisible(settignsRunFlag);
        filterLayout.setEnabled(!settignsRunFlag);
        reportTabSheet.setVisible(!settignsRunFlag);

        if(settignsRunFlag){
            refreshSettings();
            settingsLayout.loadSettingsButton.addListener(e->runFillSettings());
        }
    }

    private void showClienInfo() {

        log4jLogger.log(Level.INFO, "get Client info ");
        final Window window = new Window("Информация о клиенте");
        window.setWidth(300.0f, Unit.PIXELS);
        window.setHeight(300.0f,Unit.PIXELS);
        final FormLayout content = new FormLayout();
        TextArea clientInfoArea = new TextArea();
        clientInfoArea.setValue(new ClientInfo().getClientInfo());
        clientInfoArea.setRows(10);
        clientInfoArea.setEnabled(false);
        clientInfoArea.setSizeFull();
        content.addComponent(clientInfoArea);
        content.setMargin(true);
        window.setContent(content);
        UI.getCurrent().addWindow(window);
    }

    private void addSettingsArea(){
        settingsLayout = new SettingsLayout();
        settingsLayout.setVisible(false);
        settingsLayout.setHeight(100.0f, Unit.PERCENTAGE);
        settingsLayout.setWidth(100.0f,Unit.PERCENTAGE);
        root.addComponent(settingsLayout);
        root.setExpandRatio(settingsLayout,14.0f);
    }

    private void addDataArea() {
        /* рабочая область все отчеты*/
        reportTabSheet.setHeight(100.0f, Unit.PERCENTAGE);
        reportTabSheet.setWidth(100.0f,Unit.PERCENTAGE);
        reportTabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        reportTabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

        /* таб основной отчет */
        reportTabSheet.addTab(masterGrid, "Сводный отчет");
        /* подробный отчет */
        reportTabSheet.addTab(additGrid, "Подробный отчет");
        /* финансовая оценка */
        reportTabSheet.addTab(codeAccountingGrid,"Финансовая оценка");
        /* список ошибок */
        reportTabSheet.addTab(jiraErrorMessageGrid, "Сведения об ошибках");
        root.addComponent(reportTabSheet);
        root.setExpandRatio(reportTabSheet,14.0f);
//        if (!fltList.isEmpty()){
//            reportTabSheet.setSelectedTab(codeAccountingGrid);
//        }
    }

    /**
     *
     */
    private void addFooter() {
        HorizontalLayout footerLayout = new HorizontalLayout();
        footerLayout.setHeight(30.0f,Unit.PIXELS);
        footerLayout.setWidth(100.0f,Unit.PERCENTAGE);
        Label copyRight = new Label(
                "ЦППО. Практика Транснефть. Логика Бизнеса.");
        footerLayout.addComponent(copyRight);
        footerLayout.setDefaultComponentAlignment(Alignment.BOTTOM_RIGHT);

        loadJiraDate.setValue("Дата пересчета :".concat(masterDataOperator.getLoadMySQLDateAsString()));
        footerLayout.addComponent(loadJiraDate);

        root.addComponent(footerLayout);
        root.setComponentAlignment(footerLayout,Alignment.BOTTOM_CENTER);
        root.setExpandRatio(footerLayout,1.0f);

    }

    private void loadJiraTemporary() {
        show("Запущен перерасчет данных для формирования отчета", Notification.Type.TRAY_NOTIFICATION);

        List<JiraProject> jpList = jiraProjectBean.getActiveRow();
        if(jpList.isEmpty()) {
            jiraDBService.initialSQL();
        } else {
            masterDataOperator.exportWorkSettings();
        }
        loadJiraTmpButton.setVisible(false);
        masterDataOperator.fillPGBase();
        show("Запущена подгрузка справочных данных ", Notification.Type.TRAY_NOTIFICATION);
        masterDataOperator.fillSettings();
        show("Запущен расчет данных", Notification.Type.TRAY_NOTIFICATION);
        executeReport();
        show("Данные обновлены.", Notification.Type.TRAY_NOTIFICATION);
        loadJiraDate.setValue("Дата пересчета : ".concat(masterDataOperator.getLoadMySQLDateAsString()));
    }

    private void updateFilterCode(){

        List<String> codeList = projectCodePlanBean.getCodeList();
        comboBoxProject.setItems(codeList);
        comboBoxProject.setSelectedItem(null);
        if (fltCodeProject !=null) {
            comboBoxProject.setSelectedItem(fltCodeProject.toString());
        }
        updateURLField();
    }

    private void updateFilterVersion() {

        List<SprProjectVersion> sprProjectVersionList;
        SprProjectVersion fltVersion = null;

        String code = (String) comboBoxProject.getValue();

        log4jLogger.log(Level.INFO, "Update filter version by :" +code+": value");

        if (code == null) {
            code = "";
            sprProjectVersionList = getVersionList();
        } else {
            sprProjectVersionList = getCollectedVersionList(code);
        }

        comboBoxVersion.setItems(sprProjectVersionList);
        comboBoxVersion.setItemCaptionGenerator(new ItemCaptionGenerator<SprProjectVersion>() {
            @Override
            public String apply(SprProjectVersion pv) {
                return pv.getProjectName().concat("-").concat(pv.getVersionName());
            }
        });

        if (fltVersionID !=null && !fltVersionID.isEmpty() ) {
            fltVersion = sprProjectVersionList
                    .stream()
                    .filter(sprProjectVersion -> sprProjectVersion.getVersionIdAsString().equalsIgnoreCase(fltVersionID))
                    .findAny()
                    .orElse(null);
        } else {
            if (fltCodeVersion !=null && !fltCodeVersion.isEmpty() ) {
                fltVersion = sprProjectVersionList
                        .stream()
                        .filter(sprProjectVersion -> sprProjectVersion.getVersionName().equalsIgnoreCase(fltCodeVersion))
                        .findAny()
                        .orElse(null);
            }
        }

        if (fltVersion !=null ) {
            comboBoxVersion.setSelectedItem(fltVersion);
        } else {
            comboBoxVersion.setSelectedItem(null);
        }

        updateURLField();
    }

    private void updateFilterRole(){

        String version="";

        String code = (String)comboBoxProject.getValue();
        // String version = (String)comboBoxVersion.getValue();
        SprProjectVersion pvChoice = (SprProjectVersion) comboBoxVersion.getValue();
        if (pvChoice != null) {
            version = pvChoice.getVersionName();
        }

        if (code==null) {
            code ="";
        }
        if (version==null) {
            version="";
        }

        List<String> roleList = projectCodePlanBean.getRoleListByCodeVersion(
                code, version);
        comboBoxRole.setItems(roleList);
        if (fltWorkRole !=null ) {
            comboBoxRole.setSelectedItem(fltWorkRole.toString());
        } else {
            comboBoxRole.setSelectedItem(null);
        }
        updateURLField();
    }

    private void updateURLField(){

        String version="";
        String versionID="";
        Boolean flagAmp = false;
        String urlString = URL_DEF;

        String code = (String)comboBoxProject.getValue();
        SprProjectVersion pvChoice = (SprProjectVersion) comboBoxVersion.getValue();

        if (pvChoice != null) {
            version = pvChoice.getVersionName();
            versionID = pvChoice.getVersionIdAsString();
        }

        String role = (String)comboBoxRole.getValue();

        if ((code != null && !code.isEmpty()) ||
                (version != null && !version.isEmpty()) ||
                    role != null && !role.isEmpty() ) {
            urlString=urlString.concat("/?");

            if (code != null && !code.isEmpty() ) {
                flagAmp=true;
                urlString=urlString.concat("code=").concat(code);
            }

            if (versionID !=null && !versionID.isEmpty()) {
                if (flagAmp) {
                    urlString = urlString.concat("&");
                }
                urlString = urlString.concat("versionID=").concat(versionID);
                flagAmp = true;
            } else {
                if (version != null && !version.isEmpty()) {
                    if (flagAmp) {
                        urlString = urlString.concat("&");
                    }
                    urlString = urlString.concat("version=").concat(version);
                    flagAmp = true;
                }
            }

            if (role != null && !role.isEmpty()) {
                if (flagAmp) {
                    urlString=urlString.concat("&");
                }
                urlString=urlString.concat("role=").concat(role);
                flagAmp=true;
            }
        }

        url.setValue(urlString);
    }

    /**
     * Выгрузка основного отчета в Excel
     */
    private void toExcel() {

        ListDataProvider<ReportRow> listProvider=(ListDataProvider<ReportRow>) masterGrid.getDataProvider();
        ArrayList<ReportRow> jpList=(ArrayList<ReportRow>) listProvider.getItems();
        ListDataProvider<CodeJiraIssues> codeJiraIssuesListDataProvider=(ListDataProvider<CodeJiraIssues>) additGrid.getDataProvider();
        ArrayList<CodeJiraIssues> codeJiraIssuesArrayList=(ArrayList<CodeJiraIssues>) codeJiraIssuesListDataProvider.getItems();

        if (jpList != null && !jpList.isEmpty()) {
            log4jLogger.log(Level.INFO, "Call toXLS for "+jpList.size() +" rows");

            Button xlsButton = new Button("Расчет отчета ...");
            xlsButton.setEnabled(false);
            xlsButton.setDisableOnClick(true);
            Window xlsButtonWindow = new Window();

            HorizontalLayout content = new HorizontalLayout();
            content.setMargin(true);
            content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            content.addComponent(xlsButton);

            xlsButtonWindow.setCaption("Расчет и скачивание файла XLS");
            xlsButtonWindow.setWidth(300, Unit.PIXELS);
            xlsButtonWindow.center();
            xlsButtonWindow.setModal(true);
            xlsButtonWindow.setResizable(false);
            xlsButtonWindow.setDraggable(false);
            xlsButtonWindow.setContent(content);

            UI.getCurrent().addWindow(xlsButtonWindow);

            GenerateXLS toXls = new GenerateXLS();
            File xlsFile = toXls.generateMultiPageReport(jpList,codeJiraIssuesArrayList);
            if (xlsFile!=null && xlsFile.exists()) {
                FileResource zip = new FileResource(xlsFile);
                FileDownloader fd = new FileDownloader(zip);
                fd.extend(xlsButton);
                xlsFile.deleteOnExit();
                xlsButton.setCaption("Скачать файл");
                xlsButton.setEnabled(true);
            } else {
                log4jLogger.log(Level.ERROR, "Error download xls file ");
                xlsButton.setIcon(VaadinIcons.ALARM);
                xlsButton.setCaption("Ошибка при расчете файла отчета.");
            }
        }
    }

    /**
     * Функция отчета по ошибкам и вызов
     */
    private void errMsg2Print() {
        ListDataProvider<JiraErrorMessage> listProvider=(ListDataProvider<JiraErrorMessage>) jiraErrorMessageGrid.getDataProvider();
        ArrayList<JiraErrorMessage> jpList=(ArrayList<JiraErrorMessage>) listProvider.getItems();

        if (jpList != null && !jpList.isEmpty()) {
            log4jLogger.log(Level.INFO, "Call toXLS for "+jpList.size() +" rows");

            Button xlsButton = new Button("Расчет отчета по ошибкам ...");
            xlsButton.setEnabled(false);
            xlsButton.setDisableOnClick(true);
            Window xlsButtonWindow = new Window();

            HorizontalLayout content = new HorizontalLayout();
            content.setMargin(true);
            content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            content.addComponent(xlsButton);

            xlsButtonWindow.setCaption("Расчет и скачивание файла ошибок");
            xlsButtonWindow.setWidth(300, Unit.PIXELS);
            xlsButtonWindow.center();
            xlsButtonWindow.setModal(true);
            xlsButtonWindow.setResizable(false);
            xlsButtonWindow.setDraggable(false);
            xlsButtonWindow.setContent(content);

            UI.getCurrent().addWindow(xlsButtonWindow);

            GenerateXLS toXls = new GenerateXLS();
            File xlsFile = toXls.generateErrorReport(jpList);
            if (xlsFile!=null && xlsFile.exists()) {
                FileResource zip = new FileResource(xlsFile);
                FileDownloader fd = new FileDownloader(zip);
                fd.extend(xlsButton);
                xlsFile.deleteOnExit();
                xlsButton.setCaption("Скачать файл");
                xlsButton.setEnabled(true);
            } else {
                log4jLogger.log(Level.ERROR, "Error download Error message xls file ");
                xlsButton.setIcon(VaadinIcons.ALARM);
                xlsButton.setCaption("Ошибка при расчете файла ошибок.");
            }
        }
    }

    /**
     * Выполнение основного расчета на основании входных фильтров
     */
    private void executeReport() {

        Collection<ReportRow> reportRows;
        Collection<CodeJiraIssues> codeJiraIssues;
        Collection<CodeAccounting> codeAccountings;
        Collection<JiraErrorMessage> jiraErrorMessages;

        String version="";
        BigDecimal versionID= BigDecimal.ZERO;

        String code = (String)comboBoxProject.getValue();
//        String version = (String)comboBoxVersion.getValue();

        SprProjectVersion pvChoice = (SprProjectVersion)comboBoxVersion.getValue();
        if (pvChoice != null) {
            version = pvChoice.getVersionName();
            versionID =pvChoice.getVersionId();
        }

        String role = (String)comboBoxRole.getValue();

        if (code==null) {
            code ="";
        }
        if (version==null || version.isEmpty()) {
            version="";
        }
        if (role==null) {
            role="";
        }

        show("Запущено формирование отчета по коду проекта:",
                code, Notification.Type.TRAY_NOTIFICATION);
//        reportRows = reportRowBean.getFilteredReportRow(code,version,role);
        reportRows = reportRowBean.getFilteredReportRow(code,versionID,role);
//        codeJiraIssues= codeJiraIssuesBean.getFilteredJiraIssues(code,version,role);
        codeJiraIssues= codeJiraIssuesBean.getFilteredJiraIssues(code,versionID,role);
        codeAccountings=codeAccountingBean.getFilteredCodeAccounting(code,version);
//        codeAccountings=codeAccountingBean.getFilteredCodeAccounting(code,versionID);
        jiraErrorMessages=jiraErrorMessageBean.getFilteredJiraErrorMessages(code,version);
//        jiraErrorMessages=jiraErrorMessageBean.getFilteredJiraErrorMessages(code,versionID);

        masterGrid.setItems(reportRows);
        additGrid.setItems(codeJiraIssues);
        codeAccountingGrid.setItems(codeAccountings);
        jiraErrorMessageGrid.setItems(jiraErrorMessages);
    }

    /**
     * Загрузка данных для экранов настроек
     */
    private void loadSettingsData() {
        Collection<ProjectCode> projectCodes;
        Collection<JiraProject> jiraProjects;
        Collection<ProjectVersion> projectVersions;
        Collection<ProjectUserRole> projectUserRoles;
        Collection<ProjectCodePlan> projectCodePlans;
        Collection<CodeAccounting> codeAccountings;

        projectCodes = projectCodeBean.getAllRow();
        jiraProjects = jiraProjectBean.getAllRow();
        projectVersions = projectVersionBean.getAllRow();
        projectUserRoles = projectUserRoleBean.getAllRow();
        projectCodePlans = projectCodePlanBean.getAllRow();
        codeAccountings = codeAccountingBean.getAllRow();

        settingsLayout.projectCodeGrid.setItems(projectCodes);
        settingsLayout.jiraProjectGrid.setItems(jiraProjects);
        settingsLayout.projectVersionGrid.setItems(projectVersions);
        settingsLayout.projectUserRoleGrid.setItems(projectUserRoles);
        settingsLayout.projectPlanGrid.setItems(projectCodePlans);
        settingsLayout.codeAccountingEditGrid.setItems(codeAccountings);
    }

    private void refreshMasterData() {
        log4jLogger.log(Level.INFO, "!=====================================================!");
        log4jLogger.log(Level.INFO, "------------- Refresh Master Screen-------------------");
        log4jLogger.log(Level.INFO, "!=====================================================!");
        masterGrid.getDataProvider().refreshAll();
        additGrid.getDataProvider().refreshAll();
        codeAccountingGrid.getDataProvider().refreshAll();
        jiraErrorMessageGrid.getDataProvider().refreshAll();
    }

    /**
     * Обновление мастер экранов
     */
    private void refreshSettings() {
        log4jLogger.log(Level.INFO, "!=====================================================!");
        log4jLogger.log(Level.INFO, "------------- Refresh Settings -----------------------");
        log4jLogger.log(Level.INFO, "!=====================================================!");
        settingsLayout.projectCodeGrid.getDataProvider().refreshAll();
        settingsLayout.jiraProjectGrid.getDataProvider().refreshAll();
        settingsLayout.projectVersionGrid.getDataProvider().refreshAll();
        settingsLayout.projectUserRoleGrid.getDataProvider().refreshAll();
        settingsLayout.projectPlanGrid.getDataProvider().refreshAll();
        settingsLayout.codeAccountingEditGrid.getDataProvider().refreshAll();
    }

}
