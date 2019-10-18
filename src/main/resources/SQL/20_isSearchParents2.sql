drop temporary table if exists tmp_cppo_CorrectCodeIssue;
create temporary table if not exists tmp_cppo_CorrectCodeIssue (
    KEY (IssueID)
) as 
select 
	corCode.* 
from
( 
select 
	issfil.IssueID
    ,issfil.IssueNum
    ,issfil.ProjectCode
    , il.SOURCE
    , (select concat(p.pkey, concat('-', ji.issuenum)) 
		from jiraissue ji, project p
		where ji.id = il.SOURCE
		and ji.PROJECT=p.id 
      ) ParentIssueNum
    , IFNULL((select distinct cfo.customvalue    
						from             
                                customfield cf            
                                ,customfieldoption cfo           
                                ,customfieldvalue cfv            
						where               
								cf.cfname='Код проекта'             
                                and cf.ID=cfv.CUSTOMFIELD             
                                and cfv.ISSUE=il.SOURCE
                                and cfo.ID=cfv.STRINGVALUE   
	 ),'n/p') parentProjectCode
from 
	tmp_cppo_issueFilled issfil
    , issuelink il
    , issuelinktype ilt
where 
	(issfil.ProjectSubst='cppo' 
	or issfil.ProjectSubst='other')
	and issfil.IssueID=il.destination
	and il.linktype=ilt.id
	and ilt.linkname='Parent'
 
 ) corCode
 where 
 corCode.parentProjectCode !='n/p'
 and SUBSTRING_INDEX(corCode.parentProjectCode, '.', -1) != 'cppo'
 and SUBSTRING_INDEX(corCode.parentProjectCode, '.', -1) != 'other'
;

commit;