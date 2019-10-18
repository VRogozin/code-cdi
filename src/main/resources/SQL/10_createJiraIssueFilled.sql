/*
10680	TNFDEV		ТН.КИС ЭХД
11880	BUNKER		ТН.ЭАД
12180	EATD		ТН.ЭАТД
13781	TNUZDO		ТН. ЮЗДО
13980	TNFRAMEWOR	ТН. Платформа
15680	TNENONEC	ТН Энерго-1С: интеграция
15780	TNASU		ТН. АСУ Закупки
15781	TNBDAD		ТН. БД АД
15782	TNBDVE		ТН. БД ВЭ


p.id in (13781, 13980)
p.id in (10680, 11880,12180,13781,13980,15680,15780,15781,15782)
p.id =10680
*/
drop temporary table if exists tmp_cppo_issueFilled;
create temporary table if not exists tmp_cppo_issueFilled (
    KEY (IssueID),
    KEY (ProjectID),
    KEY (ProjectCode),
    KEY (ProjectVersion),
    KEY (WorkRole)
) as
select
   'original' RecordType
   , t4.*
   , IFNULL(wl2.USER_ROLE,'н/д') WorkRole
   , SUBSTRING_INDEX(t4.ProjectCode, '.', 1) ProjectPrev
   , SUBSTRING_INDEX(t4.ProjectCode, '.', -1) ProjectSubst
from 
(
-- ===============================
-- t4
-- ===============================
select 
	jira_full.issue_id IssueID
	, jira_full.project_id ProjectID
  , IFNULL(jira_full.codeProject,
           ( select concat(IFNULL(
                               (select SmallCode from tmp_cppo_stat_project where PMNEM=jira_full.project_mnem),'cppo')
           ,'.other') PCode from dual )
    ) ProjectCode
    , jira_full.project_mnem ProjectMnem                    
    , jira_full.project_name ProjectName
    , IFNULL(jira_full.version_id, -1) VersionID
    , IFNULL(jira_full.vname,'Без версии') ProjectVersion
    , IFNULL(jira_full.versionStatus,'Planned') VersionStatus
    , jira_full.priority_str PriorityStr
    , jira_full.jissue_type jissuetype
    , jira_full.issue_num IssueNum
    , jira_full.jissue_status jissuestatus
    , trim(ifnull(au.lower_user_name,jira_full.jira_user)) Asigned
    , 0 TimeOrig
    , TRUNCATE((IFNULL(jira_full.timeworked,0)/3600),4)  TimeSpn
    , 0 TimeEst
    , IFNULL(jira_full.buildNumber,' ') BuildNum
    , concat('http://devservice.it.ru/dev/browse/',jira_full.issue_num) URL
--    , TRUNCATE((if ( t3.timeoriginalestimate=0 and ( t3.TIMEESTIMATE + t3.TIMESPENT) <>0, t3.TIMEESTIMATE + t3.TIMESPENT , t3.timeoriginalestimate) /3600 ),4) TimeOrig
--    , TRUNCATE((t3.TIMESPENT/3600),4)  TimeSpn
--    , TRUNCATE((t3.TIMEESTIMATE/3600),4) TimeEst
from
-- ===============================
-- выбираем задачи c детальной информацией и скорректированным пользователем
-- ===============================
(
select 
	jira_issues.issue_id
    , v_issues_vwcb.codeProject
    , v_issues_vwcb.version_id
    , v_issues_vwcb.vname
    , v_issues_vwcb.description
	, v_issues_vwcb.versionStatus
    , v_issues_vwcb.rolelevel
	, jira_issues.project_id
    , jira_issues.project_mnem
    , jira_issues.project_name
    , jira_issues.issue_num
    , jira_issues.jissue_type
    , jira_issues.jissue_status
    , jira_issues.priority_str
    , jira_issues.assigned
    , v_issues_vwcb.AUTHOR
    , ifnull(v_issues_vwcb.AUTHOR,jira_issues.assigned) jira_user
    , jira_issues.timeoriginalestimate
    , jira_issues.TIMESPENT
    , jira_issues.TIMEESTIMATE
    , v_issues_vwcb.timeworked
    , v_issues_vwcb.buildNumber
from
(
-- ===============================
--  выбираем задачи c детальной информацией
-- ===============================
select 
	ji.ID issue_id
	, p.id project_id
    , p.pkey project_mnem
    , p.pname project_name
    , concat(p.pkey, concat('-', ji.issuenum)) issue_num
    , jit.pname jissue_type
    , jist.pname jissue_status
    , prt.pname priority_str
    , ifnull(ji.ASSIGNEE, ji.CREATOR) assigned
    , IFNULL(ji.timeoriginalestimate,0) timeoriginalestimate
    , IFNULL(ji.TIMESPENT,0) TIMESPENT
    , IFNULL(ji.TIMEESTIMATE,0) TIMEESTIMATE
from project p
	,jiraissue ji
    ,issuestatus jist
    ,issuetype jit
    ,priority prt
where	
	p.id in (10680, 11880,12180,13781,13980,15680,15780,15781,15782,16180)
	and ji.PROJECT=p.id
    and ji.created> STR_TO_DATE('01,1,2017','%d,%m,%Y')    
	and jist.ID=ji.issuestatus
	and jit.ID=ji.issuetype
	and ji.PRIORITY=prt.ID    
-- ===============================
--  выбираем задачи c детальной информацией
-- ===============================
) jira_issues 
-- ===============================
--  соединяем c информацией о версиях , затратах коде проекта и сборки
-- ===============================
left outer join
(
-- ===============================
--  выбираем задачи с версией, затратами и кодом проекта и  номером сборки
-- ===============================
select 
  	  v_issues_version_workload_code.issue_id
    , v_issues_version_workload_code.codeProject
    , v_issues_version_workload_code.version_id
    , v_issues_version_workload_code.vname
    , v_issues_version_workload_code.description
	, v_issues_version_workload_code.versionStatus
    , v_issues_version_workload_code.AUTHOR
    , v_issues_version_workload_code.rolelevel
    , v_issues_version_workload_code.timeworked
    , v_issues_build.buildNumber
from
(
-- ===============================
--  выбираем задачи с версией, затратами и кодом проекта
-- ===============================
select 
  	v_issues_version_workload.issue_id
    , v_issues_code.codeProject
    , v_issues_version_workload.version_id
    , v_issues_version_workload.vname
    , v_issues_version_workload.description
	, v_issues_version_workload.versionStatus
    , v_issues_version_workload.AUTHOR
    , v_issues_version_workload.rolelevel
    , v_issues_version_workload.timeworked
from
(
-- ===============================
-- выбираем задачи, списанное время и версии
-- ===============================
select 
  	v_issues_workload.issue_id
    , v_issues_workload.version_id
    , v_version.vname
    , v_version.description
	, v_version.versionStatus
    , v_issues_workload.AUTHOR
    , v_issues_workload.rolelevel
    , v_issues_workload.timeworked
from
(
-- ===============================
-- выбираем все задачи с реальными затратами
-- ===============================
select 
	v_issues.issue_id
    , v_issues.version_id
    , v_workload.AUTHOR
 	, v_workload.rolelevel
	, v_workload.timeworked
from
(
-- ===============================
-- выбираем все задачи
-- ===============================
-- задачи с указанной версией 
--    если более чем одна берется максимальная
-- ===============================
select
    na.SOURCE_NODE_ID issue_id
    ,max(na.SINK_NODE_ID) version_id
from
    project p
	, jiraissue ji
	, nodeassociation na
where
	p.id in (10680, 11880,12180,13781,13980,15680,15780,15781,15782,16180) -- TUZDO, ТН.Платформа
	and ji.PROJECT=p.id
	and ji.id=na.SOURCE_NODE_ID
    and ji.created> STR_TO_DATE('01,1,2017','%d,%m,%Y')    
--  and ( ji.issuenum=11233)
	and na.SOURCE_NODE_ENTITY='Issue'
	and na.ASSOCIATION_TYPE='IssueFixVersion'
group by SOURCE_NODE_ID
-- ===============================
union all 
-- ===============================
-- соединяем с задачами без версий
-- ===============================
select 
	ji.ID issue_id
    , -1 version_id -- не указана версия
from project p 
	, jiraissue ji
where 
 	p.id in (10680, 11880,12180,13781,13980,15680,15780,15781,15782,16180)
 	and ji.PROJECT=p.id
--    and ( ji.issuenum=11233)
    and ji.created> STR_TO_DATE('01,1,2017','%d,%m,%Y')    
    and ji.id not in 
    (
		select distinct
			na.SOURCE_NODE_ID
		from
			project p
			, jiraissue ji
			, nodeassociation na
		where
			p.id in (10680, 11880,12180,13781,13980,15680,15780,15781,15782,16180) -- TUZDO, ТН.Платформа
			and ji.PROJECT=p.id
			and ji.id=na.SOURCE_NODE_ID
            and ji.created> STR_TO_DATE('01,1,2017','%d,%m,%Y')    
		--  and ( ji.issuenum=11233)
			and na.SOURCE_NODE_ENTITY='Issue'
			and na.ASSOCIATION_TYPE='IssueFixVersion'
	)
-- =============================
) v_issues
left outer join    
(    
-- =============================
-- соединяем со списанием затрат 
-- =============================
select 
      wl.issueid 
	, wl.AUTHOR
 	, wl.rolelevel
	, sum(wl.timeworked) timeworked
from project p 
	, jiraissue ji
	, worklog wl
where 
 	p.id in (10680, 11880,12180,13781,13980,15680,15780,15781,15782,16180)
 	and ji.PROJECT=p.id
 	and wl.issueid=ji.ID
    and ji.created> STR_TO_DATE('01,1,2017','%d,%m,%Y')
    and wl.created> STR_TO_DATE('01,1,2017','%d,%m,%Y')
group by 
    wl.issueid
	, wl.AUTHOR
 	, wl.rolelevel
-- =============================
) v_workload
on v_issues.issue_id = v_workload.issueid
-- ===============================
-- / выбираем все задачи с реальными затратами
-- ===============================
) v_issues_workload
-- ===============================
-- соединяем с версиями
-- ===============================
left outer join
(
-- ===============================
-- выбираем все версии
-- ===============================
select
	pv.id
	, pv.project
    , pv.vname
    , pv.description
	, if (pv.archived='true' ,'Archived', if (pv.released='true','Released',
		if(pv.startdate<now() , 'Started','Planned'))) as versionStatus
from
	projectversion pv
where
    pv.project in (10680, 11880,12180,13781,13980,15680,15780,15781,15782,16180)
order by pv.id desc
-- ===============================
-- / выбираем все версии
-- ===============================
) v_version
on v_issues_workload.version_id=v_version.id

-- ===============================
-- / выбираем задачи списанное время и версии
-- ===============================
) v_issues_version_workload
left outer join
-- ===============================
--  соединяем с кодом проекта 
-- ===============================
(
-- ===============================
--  выбираем задачи с кодом проекта 
-- ===============================
select
	cfv.ISSUE
    , cfo.customvalue codeProject
from
	project p
	,jiraissue ji
	,customfieldvalue cfv
	,customfield cf
	,customfieldoption cfo
where
	cf.cfname='Код проекта'
	and p.id in (10680, 11880,12180,13781,13980,15680,15780,15781,15782,16180)
	and ji.PROJECT=p.id
    and ji.created> STR_TO_DATE('01,1,2017','%d,%m,%Y')    
	and cfv.ISSUE=ji.ID
	and cf.ID=cfv.CUSTOMFIELD
	and cf.ID=cfo.CUSTOMFIELD
	and cfo.ID=cfv.STRINGVALUE
-- ===============================
-- / выбираем задачи с кодом проекта 
-- ===============================

) v_issues_code
on v_issues_version_workload.issue_id=v_issues_code.ISSUE
-- ===============================
-- / соединяем с кодом проекта 
-- ===============================
) v_issues_version_workload_code
left outer join 
-- ===============================
-- - соединяем с номером сборки 
-- ===============================
(
-- ===============================
-- выбираем задачи с номером сборки 
-- ===============================
select
	cfv.ISSUE
	, cfv.STRINGVALUE buildNumber
from
	project p
	, jiraissue ji
	, customfieldvalue cfv
	, customfield cf
where
	cf.cfname='Номер сборки'
    and p.id in (10680, 11880,12180,13781,13980,15680,15780,15781,15782,16180) -- TUZDO, ТН.Платформа
    and ji.PROJECT=p.id
    and ji.created> STR_TO_DATE('01,1,2017','%d,%m,%Y')
	and cfv.ISSUE=ji.ID
    and cf.ID=cfv.CUSTOMFIELD
-- ===============================
-- / выбираем задачи с номером сборки 
-- ===============================
) v_issues_build on v_issues_version_workload_code.issue_id=v_issues_build.ISSUE
-- ===============================
-- / соединяем с номером сборки 
-- ===============================
-- / выбираем задачи с версией, затратами и кодом проекта и  номером сборки
-- ===============================
) v_issues_vwcb
on jira_issues.issue_id =v_issues_vwcb.issue_id
) jira_full
-- ===============================
-- соединяем с пользователеи
-- ===============================
left outer join
app_user au 
on jira_full.jira_user=au.user_key
-- ===============================
-- / выбираем задачи c детальной информацией и скорректированным пользователем
-- ===============================

) t4 left outer join tmp_cppo_user_role wl2 on t4.Asigned=wl2.user_key;


commit;
