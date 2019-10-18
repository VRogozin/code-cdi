package ru.lb.cppo.ui;

import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.contextmenu.GridContextMenu.GridContextMenuOpenListener.GridContextMenuOpenEvent;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.renderers.HtmlRenderer;
import ru.lb.cppo.backend.data.JiraErrorMessage;

public class JiraErrorMessageGrid extends Grid<JiraErrorMessage> {

    public GridContextMenu<JiraErrorMessage> gridMenu;
    public HeaderRow filteringHeader;

    public JiraErrorMessageGrid() {
        addColumn(JiraErrorMessage::getId).setCaption("#").setHidden(true);
        addColumn(JiraErrorMessage::getProjectCode).setCaption("ИД проекта").setExpandRatio(1);
        addColumn(JiraErrorMessage::getProjectName).setCaption("Шифр проекта").setExpandRatio(1);
        addColumn(JiraErrorMessage::getProjectVersion).setCaption("Версия").setExpandRatio(1);
        addColumn(this::htmlFormatURL, new HtmlRenderer()).setCaption("Номер задачи").setComparator((u1,u2) -> {
            return u1.getIssueNum().compareTo(u2.getIssueNum());
        });
        addColumn(JiraErrorMessage::getErrorCode).setCaption("Код ошибки").setExpandRatio(1);
        addColumn(JiraErrorMessage::getErrMessage).setCaption("Текст Ошибки").setExpandRatio(3);

        setSizeFull();
        setFrozenColumnCount(4);

        addContextMenu();
    }

    private String htmlFormatURL(JiraErrorMessage jiraErrorMessage) {
        //todo переделать на использование параметров
        String issuesURL = "http://devservice.it.ru/dev/browse/";
        String issueNum = jiraErrorMessage.getIssueNum();

        String urlCode ="<a class=\"v-link\" href=\""+ issuesURL + issueNum+ "\" target=\"blank\">"+issueNum+"</a>";

        return urlCode;
    }

    private void addContextMenu()
    {
        gridMenu = new GridContextMenu<>(this);
        gridMenu.addGridBodyContextMenuListener(this::updateGridBodyMenu);
        gridMenu.addGridHeaderContextMenuListener(this::updateGridHeaderMenu);
    }

    private void updateGridBodyMenu(GridContextMenuOpenEvent<JiraErrorMessage> event) {
        event.getContextMenu().removeItems();
        if (event.getItem() != null) {
            event.getContextMenu().addItem("Remove row", VaadinIcons.CLOSE, selectedItem -> {
//  -- add logics to Add row
            });
        } else {
            event.getContextMenu().addItem("Add row", VaadinIcons.PLUS, selectedItem -> {
//  -- add logics to remove
            });
        }
    }

    private void updateGridHeaderMenu(GridContextMenuOpenEvent<JiraErrorMessage> event) {
        event.getContextMenu().removeItems();
        if (event.getColumn() != null) {
            event.getContextMenu().addItem("Filter", selectedItem ->
            {
            });
//                this.sort((Grid.Column<JiraErrorMessage, ?>) event.getColumn(), SortDirection.ASCENDING));
            event.getContextMenu().addItem("Remove filter ", selectedItem ->
            {
            });
//                this.sort((Grid.Column<JiraErrorMessage, ?>) event.getColumn(), SortDirection.DESCENDING));
        } else {
            event.getContextMenu().addItem("menu is empty", null);
        }
    }

}
