/*
Добавление записей "разделенных" в пропроциях между версиями
*/
drop temporary table if exists tmp_cppo_issueCorrected;
create temporary table if not exists tmp_cppo_issueCorrected (
    KEY (IssueID),
    KEY (ProjectID),
    KEY (ProjectCode), 
    KEY (ProjectVersion),
	KEY (WorkRole)
) as 
select 
    'corrected' RecordType
	,issfil.IssueID
    ,issfil.ProjectID
    ,tCor.parentProjectCode ProjectCode
    ,issfil.ProjectMnem
    ,issfil.ProjectName
    ,issfil.VersionID
    ,issfil.ProjectVersion
    ,issfil.VersionStatus
    ,issfil.PriorityStr
    ,issfil.jissuetype
    ,issfil.IssueNum
    ,issFil.jIssueStatus
    ,issfil.Asigned
    ,issfil.TimeOrig
    ,issfil.TimeSpn
    ,issfil.TimeEst
    ,issfil.BuildNum
    ,issfil.URL
    ,issfil.WorkRole
    ,issfil.ProjectPrev
    ,issfil.ProjectSubst
from 
	tmp_cppo_issueFilled issfil,tmp_cppo_CorrectCodeIssue tCor
where 
	TCor.IssueID=issFil.IssueID
;

commit;