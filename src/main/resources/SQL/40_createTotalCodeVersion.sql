/*
Подсчет суммарных затрат в разрезе Группы проектов и версии
запускается после вызова JiraCollapsedToTable.sql
*/
drop temporary table if exists tmp_cppo_totalCodeVersion;
create temporary table if not exists tmp_cppo_totalCodeVersion (
	KEY (ProjectPrev) ,
    KEY (VersionID)
) as 
select 
	pc.ProjectPrev
    ,pc.VersionID
    ,pc.ProjectVersion
--    ,pc.WorkRole
   ,SUM(pc.TimeOrig) TimeOrig
   ,SUM(pc.TimeSpn) TimeSpn
   ,SUM(pc.TimeEst) TimeEst
from
	tmp_cppo_calculate pc
where 
	pc.ProjectVersion !='Без версии'
    and pc.ProjectSubst <> 'cppo'
    and pc.ProjectSubst <> 'other'
    
group by 
  pc.VersionID
  , pc.ProjectVersion
  , pc.ProjectPrev 
-- ,pc.WorkRole
order by pc.VersionID,pc.projectversion;

commit;