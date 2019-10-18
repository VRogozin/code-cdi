package ru.lb.cppo.ui;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.renderers.HtmlRenderer;
import ru.lb.cppo.backend.data.ProjectUserRole;

import java.util.ArrayList;
import java.util.List;

public class ProjectUserRoleGrid extends Grid<ProjectUserRole> {
    public ProjectUserRoleGrid() {

        List<String> roleList= new ArrayList<String>();
        roleList.add("-");
        roleList.add("Разработчик");
        roleList.add("Аналитик");
        roleList.add("Тестировщик");
        roleList.add("Тех.писатель");
        roleList.add("Администратор");

        CheckBox isShadowCheck = new CheckBox();
        NativeSelect roleSelect = new NativeSelect<String>("Выберите",roleList);
        roleSelect.setEmptySelectionAllowed(false);

        addColumn(ProjectUserRole::getJuserName).setCaption("Логин").setExpandRatio(2);
        addColumn(this::checkRoleStatus, new HtmlRenderer())
                .setCaption("Пользователь").setExpandRatio(2)
                .setComparator((p1, p2) -> {
            return p1.getUserDispName().compareTo(p2.getUserDispName());
        });
        addColumn(ProjectUserRole::getRoleName).setCaption("Роль").setExpandRatio(2)
        .setEditorComponent(roleSelect,ProjectUserRole::setRoleName);
        addColumn(ProjectUserRole::getActive).setCaption("Включен").setExpandRatio(1)
                .setEditorComponent(isShadowCheck, ProjectUserRole::setActive);

        getEditor().setEnabled(true);
    }

    private String checkRoleStatus(ProjectUserRole purRow){
        String color="#34495e";

        if(purRow.getRoleName().compareTo("-") == 0) {
            color="#7f8c8d";
        } else {
            if (purRow.getActive()) {
                color="#3498db";
            }
        }

        String dispNameString = "<span style=\"color: "
                + color
                + "\">"
                + purRow.getUserDispName()
                + "</span>";
        return dispNameString;
    }

}
