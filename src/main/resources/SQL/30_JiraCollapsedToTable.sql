/*
Подсчет суммарных затрат в разрезе Код проекта, версия, роль
запускается после вызова createJiraIssueFilled.sql
*/
drop temporary table if exists tmp_cppo_calculate;
create temporary table if not exists tmp_cppo_calculate (
	KEY (ProjectCode) 
) as 
select 
   t5.ProjectCode 
   ,t5.ProjectVersion
   ,t5.VersionID
   ,t5.WorkRole
   , SUBSTRING_INDEX(t5.ProjectCode, '.', 1) ProjectPrev
   , SUBSTRING_INDEX(t5.ProjectCode, '.', -1) ProjectSubst
   ,SUM(t5.TimeOrig) TimeOrig
   ,SUM(t5.TimeSpn) TimeSpn
   ,SUM(t5.TimeEst) TimeEst
from (
	select 
        ifNull(tCor.parentProjectCode,tFil.ProjectCode) ProjectCode
 		,tFil.ProjectVersion
    ,tFil.VersionID
    ,tFil.WorkRole
		,tFil.TimeOrig
		,tFil.TimeSpn
		,tFil.TimeEst
   from
    tmp_cppo_issueFilled tFil left outer join tmp_cppo_CorrectCodeIssue tCor
    on tFil.IssueID=tCor.IssueID
    ) t5
group by 
	 t5.ProjectCode
   ,t5.VersionID
   ,t5.ProjectVersion
   ,t5.WorkRole;
   
commit;  
