/*
Добавление записей "разделенных" в пропроциях между версиями
*/
drop temporary table if exists tmp_cppo_issueSplitted;
create temporary table if not exists tmp_cppo_issueSplitted (
    KEY (IssueID),
    KEY (ProjectID),
    KEY (ProjectCode), 
    KEY (ProjectVersion),
	KEY (WorkRole)
) as 
select 
    'splitted' RecordType
	  ,issfil.IssueID
    ,issfil.ProjectID
    ,pcv.ProjectCode
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
    ,truncate((issfil.TimeOrig*pcv.ProzentRealize),4) TimeOrig
    ,truncate((issfil.TimeSpn*pcv.ProzentRealize),4) TimeSpn
    ,truncate((issfil.TimeEst*pcv.ProzentRealize),4) TimeEst
    ,issfil.BuildNum
    ,issfil.URL
    ,issfil.WorkRole
    ,issfil.ProjectPrev
    ,issfil.ProjectSubst
from 
	tmp_cppo_issueFilled issfil inner join
    tmp_cppo_procentCodeVersion pcv on (issfil.ProjectPrev=pcv.ProjectPrev and 
    issfil.VersionID=pcv.VersionID)
where 
	issfil.ProjectVersion !='Без версии'
    and (issfil.ProjectSubst='cppo' or issfil.ProjectSubst='other')
    and issfil.IssueID not in (select IssueID from tmp_cppo_CorrectCodeIssue)
;

commit;
