package ru.lb.cppo.backend.data;

public enum VersionStatusEnum {
    RELEASED("Released"), ARCHIVED("Archived"), STARTED("Started"), PLANNED("Planned");

    final String name;

    VersionStatusEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    //From String method will return you the Enum for the provided input string
    public static VersionStatusEnum fromString(String parameterName) {
        if (parameterName != null) {
            for (VersionStatusEnum objType : VersionStatusEnum.values()) {
                if (parameterName.equalsIgnoreCase(objType.name)) {
                    return objType;
                }
            }
        }
        //возвращать значение по умолчанию в случае если не найдено
        return PLANNED;
    }

}