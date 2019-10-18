
drop temporary table if exists tmp_cppo_issuePrepared;
create temporary table if not exists tmp_cppo_issuePrepared (
    KEY (IssueID),
    KEY (ProjectID),
    KEY (ProjectCode), 
    KEY (ProjectVersion),
	KEY (WorkRole)
) as 
select 
	iPrep.* 
from
(
select 
	iOrig.*
from 
	tmp_cppo_issueFilled iOrig
where
    iOrig.ProjectSubst<>'cppo' 
	and iOrig.ProjectSubst<>'other'
    
union all

select 
	iSpl.* 
from	
	tmp_cppo_issueSplitted iSpl
    
union all

select 
	iCor.* 
from	
	tmp_cppo_issueCorrected iCor
    
) iPrep;

commit;