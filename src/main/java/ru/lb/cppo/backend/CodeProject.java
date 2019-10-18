package ru.lb.cppo.backend;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

// @Entity
public class CodeProject implements Serializable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigDecimal codeID;
    private String codeName;

    public CodeProject() {
    }

    public CodeProject(BigDecimal id, String codeName) {
        this.codeID = id;
        this.codeName = codeName;
    }

    public BigDecimal getCodeID() {
        return codeID;
    }

    public void setCodeID(BigDecimal codeID) {
        this.codeID = codeID;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);

        return getCodeName() + " | " + df.format(getCodeID());
    }
}
