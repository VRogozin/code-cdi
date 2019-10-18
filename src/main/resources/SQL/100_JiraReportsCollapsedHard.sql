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
drop temporary table if exists tmp_cppo_ProjectExecution;
create temporary table if not exists tmp_cppo_ProjectExecution(
	KEY (ProjectCode),
	KEY (ProjectVersion),
	KEY (WorkRole)
) as
	select
		iPrep.*
	from
		(select
			 t6.ProjectCode
			 , t6.ProjectID
			 , t6.ProjectMnem
			 , t6.VersionID
			 , t6.ProjectVersion
			 , t6.VersionStatus
			 , t6.WorkRole
			 , round(t6.TimeOrig,2) TimeOrig
			 , round(t6.TimeSpn,2) TimeSpn
			 , round(t6.TimeEst,2) TimeEst
			 , round(if (t6.VersionStatus in ("Archived","Released"), 100 ,
									 if((t6.TimeEst+t6.TimeSpn)>0 , (t6.TimeEst+t6.TimeSpn)/
												if(t6.TimeOrig>0, t6.TimeOrig, (t6.TimeEst+t6.TimeSpn))*100, 0)),2) ProzentRealize
			 , round(((t6.TimeEst+t6.TimeSpn) - t6.TimeOrig),2) DeltaRealize
			 , round(if(t6.TimeOrig>0 , (((t6.TimeEst+t6.TimeSpn) - t6.TimeOrig)/t6.TimeOrig)*100,0),2) ProzentDelta
		 from (
						select
							tCalc.ProjectCode
							, tCalc.ProjectID
							, tCalc.ProjectMnem
							, tCalc.VersionID
							, tCalc.ProjectVersion
							, tCalc.VersionStatus
							, tCalc.WorkRole
							, ifnull(pc.PlanOrig,TimeOrig) TimeOrig
							, tCalc.TimeSpn
							, tCalc.TimeEst
						from
							(
								select
									t5.ProjectCode
									, t5.ProjectID
									, t5.ProjectMnem
									, t5.VersionID
									, t5.ProjectVersion
									, t5.VersionStatus
									, t5.WorkRole
									, SUM(t5.TimeOrig) TimeOrig
									, SUM(t5.TimeSpn) TimeSpn
									, SUM(t5.TimeEst) TimeEst
								from
									tmp_cppo_issuePrepared t5
								where
									t5.ProjectVersion !='Без версии'
									and t5.WorkRole != 'н/д'
								group by
									t5.ProjectCode
									, t5.ProjectID
									, t5.ProjectMnem
									, t5.VersionID
									, t5.ProjectVersion
									, t5.VersionStatus
									, t5.WorkRole
							) tCalc left outer join tmp_cppo_PlanCode pc on
									tCalc.ProjectCode = pc.ProjectCode
									and tCalc.VersionID=pc.VersionID
									and tCalc.WorkRole=pc.WorkRole
					) t6
		 order by 1,3,5,7
		) iPrep;
commit;
/*
select 
   ProjectCode 
	 ,VersionID
	 ,ProjectVersion
	 ,VersionStatus
   ,WorkRole 
   ,TimeOrig
   ,TimeSpn
   ,TimeEst
   ,ProzentRealize
   ,DeltaRealize
   ,ProzentDelta
from 
	tmp_cppo_ProjectExecution
where
    ProjectCode = :pCode
    and ProjectVersion = :pVer
    and WorkRole = :wRole
*/