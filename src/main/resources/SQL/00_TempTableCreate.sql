create temporary table if not exists tmp_cppo_stat_project (
    PID DECIMAL(18,0) not null
    , PMNEM varchar(50) not null
    , PCode varchar(100) not null
    , SmallCode varchar(50)
);
create temporary table if not exists tmp_cppo_user_role (
  	USER_KEY VARCHAR(50) not null
    , USER_ROLE varchar(50) not null
);
create temporary table if not exists tmp_cppo_PlanCode (
    ProjectCode varchar(50) not null
    , VersionID DECIMAL(18,0)
    , ProjectVersion varchar(50) not null
    , WorkRole varchar(50) not null
    , PlanOrig DECIMAL(15,4)
    , VersionStatus varchar(50)
);
create temporary table if not exists tmp_cppo_ProjectExecution (
     ProjectCode varchar(50)
    ,ProjectID DECIMAL(18,0)
    ,ProjectMnem varchar(50)
    ,VersionID DECIMAL(18,0)
    ,ProjectVersion varchar(50)
    ,VersionStatus varchar(50)
    ,WorkRole varchar(50)
    ,TimeOrig DECIMAL(18,4)
    ,TimeSpn DECIMAL(18,4)
    ,TimeEst DECIMAL(18,4)
    ,ProzentRealize DECIMAL(18,4)
    ,DeltaRealize DECIMAL(18,4)
    ,ProzentDelta DECIMAL(18,4)
);
commit;