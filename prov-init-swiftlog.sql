create table script_run (
	script_run_id	   varchar(256) primary key,
	log_filename       varchar(2048),
	hostname	   varchar(256),
	script_run_dir	   varchar(2048),
        swift_version      varchar(16),
        cog_version        varchar(16),
        final_state        varchar(32),
        start_time         numeric,
        duration           numeric,
        script_filename    varchar(2048)
);

create table application_execution (
	application_execution_id		varchar(256) primary key,
        script_run_id   			references script_run(script_run_id),
	execution_site				varchar(256),
	start_time				numeric,
	duration				numeric,
	work_directory				varchar(256)
);

create table argument (
	application_execution_id	varchar(256) references application_execution (id),
	name				varchar(256),
	value				varchar(256),
	primary key (application_execution_id, name, value)
);	

create table resource_usage (
       application_execution_id		varchar(256) primary key references application_execution (id),
       real_secs	       		numeric,
       kernel_secs             		numeric,
       user_secs	       		numeric,
       percent_cpu             		numeric,
       max_rss	       	       		numeric,
       avg_rss	       			numeric,
       avg_tot_vm	       		numeric,
       avg_priv_data     		numeric,
       avg_priv_stack    		numeric,
       avg_shared_text   		numeric,
       page_size	       		numeric,
       major_pgfaults    		numeric,
       minor_pgfaults    		numeric,
       swaps	       			numeric,
       invol_context_switches		numeric,
       vol_waits			numeric,
       fs_reads				numeric,
       fs_writes			numeric,
       sock_recv			numeric,
       sock_send			numeric,
       signals				numeric,
       exit_status			numeric
);

create table file (
       file_id		bigserial,
       host		varchar(256), 
       name		varchar(256),       
       size		numeric,
       modify		numeric,
       unique		(host, name, size, modify)
);	

create table staged_in (
       application_execution_id		varchar(256),
       file_id 				varchar(1024) references file(id),
       source				varchar(256),
       duration 			numeric
);

create table staged_out (
       application_execution_id		varchar(256),
       file_id				varchar(1024) references file(id),
       source				varchar(256),
       duration 			numeric
);