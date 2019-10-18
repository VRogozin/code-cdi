/*
Подсчет прцентных задач на в рамках версии по коду проекта по группам проектов
запускается после вызова createTotalCodeVersion.sql
*/
drop temporary table if exists tmp_cppo_procentCodeVersion;
create temporary table if not exists tmp_cppo_procentCodeVersion (
    KEY (ProjectPrev) ,
    KEY (VersionID)
) as
select 
	t1.ProjectPrev
  , t1.VersionID
  , t1.ProjectVersion
  , t1.Projectcode
  , TRUNCATE(if(tc_tcv.TimeSpn>0 , t1.TimeSpn/tc_tcv.TimeSpn, 0),4) ProzentRealize
  , t1.TimeOrig TimeOrig
  , t1.TimeSpn TimeSpn
  , t1.TimeEst TimeEst
 from
(
select 
   pc.ProjectPrev     
   , pc.VersionID
   , pc.ProjectVersion
   , pc.Projectcode
   , SUM(pc.TimeOrig) TimeOrig
   , SUM(pc.TimeSpn) TimeSpn
   , SUM(pc.TimeEst) TimeEst
from
	tmp_cppo_calculate pc
where 
	pc.ProjectVersion !='Без версии'
    and pc.ProjectSubst <> 'cppo'
    and pc.ProjectSubst <> 'other'
   
group by 
  pc.VersionID
  , pc.ProjectVersion
  , pc.projectcode
  , pc.ProjectPrev     
) t1 , tmp_cppo_totalCodeVersion tc_tcv
where
    tc_tcv.ProjectPrev=t1.ProjectPrev
    and tc_tcv.VersionID=t1.VersionID
;
commit;