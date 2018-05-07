-- this is the schema definition used for the main relational provenance
-- implementation (in both sqlite3 and postgres)

drop view  script_run;
drop view  function_call;
drop view  application_execution;
drop view  runtime_info;
drop view  dataset;
drop view  dataset_io;
drop view  provenance_graph_edge;
drop table annot_script_run_num cascade;
drop table annot_script_run_text cascade;
drop table annot_function_call_num cascade;
drop table annot_function_call_text cascade;
drop table annot_app_exec_num cascade;
drop table annot_app_exec_text cascade;
drop table annot_dataset_num cascade;
drop table annot_dataset_text cascade;
drop table rt_info cascade;
drop table app_exec cascade;
drop table app_fun_call cascade;
drop table dataset_in cascade;
drop table dataset_out cascade;
drop table fun_call cascade;
drop table run cascade;
drop table tc_file cascade;
drop table sites_file cascade;
drop table script cascade;
drop table mapped cascade;
drop table primitive cascade;
drop table dataset_containment cascade;
drop table ds cascade;
drop table stage_in cascade;
drop table stage_out cascade;
drop view script_and_fun_call;
drop view script_to_app_fun_call;
drop view script_to_app_exec;
drop view script_to_app_exec_with_runtime_stats;
drop view dataset_primitive_and_mapped; 
drop view dataset_all;
drop view dataset_all_with_annotations;
drop view dataset_all_with_annotations_and_parameter_names;
drop view function_call_dataflow;
drop view function_call_dataflow_with_input_details;
drop view function_call_dataflow_with_dataset_details;
drop view provenance_all;
drop view provenance_summary;
drop view app_exec_stage_in;
drop view app_exec_stage_out;
drop view app_exec_staging;
drop view app_exec_staging_all;

-- application_catalog stores tc.file
create table tc_file (
       hash_value		 varchar(256) primary key,
       content			 text
);

-- application_catalog stores tc.file
create table sites_file (
	hash_value      varchar(256) primary key,
	content         text
);

-- script stores Swift script source codes
create table script (
	hash_value      varchar(256) primary key,
	content         text
);

-- run stores information about each script run log that has
-- been seen by the importer: the log filename, swift version and import
-- status.
-- Might be interesting to store xml translation of the Swift script
-- here for prospective provenance and versioning.
create table run (
     id                 varchar(256) primary key,
     log_filename       varchar(2048),
     swift_version      varchar(16),
     cog_version        varchar(16),
     final_state        varchar(32),
     start_time         numeric,
     duration           numeric,
     script_filename    varchar(2048),
     script_hash        varchar(256) references script (hash_value),
     tc_file_hash   	varchar(256) references tc_file (hash_value),
     sites_file_hash    varchar(256) references sites_file (hash_value)
);

create view script_run as
  select id, log_filename, swift_version, cog_version, final_state, 
         to_timestamp(start_time) as start_time, duration, script_filename,
	 script_hash, tc_file_hash, sites_file_hash
  from   run;

	
create view script_run_summary as 
       select id,swift_version,cog_version,final_state,
       	      start_time,duration,script_filename 
       from script_run;

-- process gives information about each process (in the OPM sense)
-- it is augmented by information in other tables
-- specifies the type of process. for any type, it
-- must be the case that the specific type table
-- has an entry for this process.
-- process types: internal, rootthread, execute, function, compound, scope, operator
create table fun_call (
     id      	    varchar(256) primary key, 
     run_id  	    varchar(256) references run (id) on delete cascade,  
     type    	    varchar(16),
     name    	    varchar(256) -- in the case of an execute this refers to the transformation name in tc.data
);

-- this gives information about each execute.
-- each execute is identified by a unique URI. other information from
-- swift logs is also stored here. an execute is an OPM process.
create table app_fun_call (
     id             varchar(256) primary key references fun_call (id),  
     name      	    varchar(256), -- name of the app procedure that invokes the transformation
     start_time     numeric,
     duration       numeric,
     final_state    varchar(32),
     scratch        varchar(2048)
);

create view function_call as 
    select fun_call.id, fun_call.name, fun_call.type, app_fun_call.name as app_catalog_name, fun_call.run_id as script_run_id,  
           to_timestamp(app_fun_call.start_time) as start_time, app_fun_call.duration, app_fun_call.final_state
    from
      fun_call
    left outer join
      app_fun_call 
    on fun_call.id=app_fun_call.id;

-- this gives information about each application execution attempt, including
-- aggregate resource consumption. the app_exec_id is tied to per-execution-attempt
-- information such as wrapper logs
create table app_exec (
     id                varchar(256) primary key,
     app_fun_call_id   varchar(256) references app_fun_call (id), 
     start_time        numeric,
     duration          numeric,
     final_state       varchar(32),
     site              varchar(256),
     real_secs	       numeric,
     kernel_secs       numeric,
     user_secs	       numeric,
     percent_cpu       numeric,
     max_rss	       numeric,
     avg_rss	       numeric,
     avg_tot_vm	       numeric,
     avg_priv_data     numeric,
     avg_priv_stack    numeric,
     avg_shared_text   numeric,
     page_size	       numeric,
     major_pgfaults    numeric,
     minor_pgfaults    numeric,
     swaps	       numeric,
     invol_context_switches	numeric,
     vol_waits			numeric,
     fs_reads			numeric,
     fs_writes			numeric,
     sock_recv			numeric,
     sock_send			numeric,
     signals			numeric,
     exit_status		numeric
);

create table stage_in (
     app_exec_id	varchar(256) references app_exec (id),
     filename		varchar(1024),
     primary key	(app_exec_id, filename)
);

create table stage_out (
     app_exec_id	varchar(256) references app_exec (id),
     filename		varchar(1024),
     primary key	(app_exec_id, filename)
);

create view application_execution as
  select id, app_fun_call_id as function_call_id, to_timestamp(start_time) as start_time, duration, final_state, site
  from   app_exec;

create view application_runtime_info as
  select id, real_secs as real, kernel_secs as kernel, user_secs as user, percent_cpu as cpu, max_rss as maxrss, 
         avg_rss as avgrss, avg_tot_vm as avgtotvm, avg_priv_data as avgprdata, avg_priv_stack as avgprvstck, 
         avg_shared_text as avgshtxt, page_size, major_pgfaults as majpf, minor_pgfaults as minpf, swaps, 
         invol_context_switches as invcs, vol_waits as volwait, fs_reads as fsread, fs_writes as fswrite, 
         sock_recv as sockrcv, sock_send as socksnd, signals, exit_status as exit_st from app_exec;

-- app execution runtime info extracted from the /proc filesystem (assumes the app executed
-- in a Linux host) 
create table rt_info ( 
     app_exec_id        varchar(256) references app_exec (id), 
     timestamp		numeric,
     cpu_usage          numeric,
     max_phys_mem	numeric,
     max_virt_mem	numeric,
     io_read		numeric,
     io_write		numeric,
     primary key (app_exec_id, timestamp)
);

create view runtime_info as
  select app_exec_id, 
	 to_timestamp(timestamp) as timestamp, 
	 cpu_usage, 
	 max_phys_mem, 
	 max_virt_mem, 
	 io_read, 
	 io_write
  from rt_info;

-- ds stores all dataset identifiers.
create table ds (
      id	 varchar(256) primary key
);

-- file stores the filename mapped to each dataset. 
create table mapped ( 
      id	 varchar(256) primary key references ds (id) on delete cascade,
      filename   varchar(2048)
);

-- dataset_values stores the value for each dataset which is known to have
-- a value (which is all assigned primitive types). No attempt is made here
-- to expose that value as an SQL type other than a string, and so (for
-- example) SQL numerical operations should not be expected to work, even
-- though the user knows that a particular dataset stores a numeric value.
create table primitive ( 
       id    varchar(256) primary key references ds (id) on delete cascade,
       value varchar(2048)
);

-- dataset_containment stores the containment hierarchy between
-- container datasets (arrays and structs) and their contents.
-- out_id contains in_id
-- TODO this should perhaps be replaced with a more OPM-like model of
-- constructors and accessors, rather than, or in addition to,
-- a containment hierarchy. The relationship (such as array index or
-- structure member name) should also be stored in this table.
create table dataset_containment ( 
       out_id varchar(256) references ds (id) on delete cascade,
       in_id  varchar(256) references ds (id) on delete cascade,
       primary key (out_id,in_id)
);

-- dataset_usage records usage relationships between processes and datasets;
-- in SwiftScript terms, the input and output parameters for each
-- application procedure invocation; in OPM terms, the artificts which are
-- input to and output from each process that is a Swift execution
create table dataset_in (
       function_call_id	varchar(256) references fun_call (id), 
       dataset_id   	varchar(256) references ds (id) on delete cascade,
       parameter   	varchar(256), -- the name of the parameter in this execute that
                             	  -- this dataset was bound to. sometimes this must
                              	  -- be contrived (for example, in positional varargs)
       primary key (function_call_id, dataset_id, parameter)
 );

create table dataset_out (
       function_call_id	varchar(256) references fun_call (id), 
       dataset_id   	varchar(256) references ds (id) on delete cascade,
       parameter   	varchar(256), -- the name of the parameter in this execute that
                              	  -- this dataset was bound to. sometimes this must
                              	  -- be contrived (for example, in positional varargs)
       primary key (function_call_id, dataset_id, parameter)
);

create view dataset_io as
 select dataset_in.function_call_id, dataset_in.dataset_id, dataset_in.parameter, 'I' as type
 from   dataset_in
union all 
 select dataset_out.function_call_id, dataset_out.dataset_id, dataset_out.parameter, 'O' as type
 from   dataset_out;

create table annot_script_run_num ( 
       script_run_id    varchar(256) references run (id) on delete cascade, 
       name      varchar(256),
       value     numeric,
       primary key (script_run_id, name)
);

create table annot_script_run_text ( script_run_id    varchar(256) references run (id) on delete cascade, 
     name      varchar(256),
     value text,
     primary key (script_run_id, name)
);

create table annot_function_call_num ( 
       function_call_id	varchar(256) references fun_call (id) on delete cascade, 
       name       	varchar(256),
       value      	numeric,
       primary key (function_call_id, name)
);

create table annot_function_call_text ( 
       function_call_id	varchar(256) references fun_call (id) on delete cascade, 
       name       	varchar(256),
       value      	text,
       primary key (function_call_id, name)
);

create table annot_app_exec_num ( 
       app_exec_id            varchar(256) references app_exec (id) on delete cascade, 
       name      		    varchar(256),
       value     		    numeric,
       primary key (app_exec_id, name)
);

create table annot_app_exec_text ( 
       app_exec_id            varchar(256) references app_exec (id) on delete cascade, 
       name      		    varchar(256),
       value     		    text,
       primary key (app_exec_id, name)
);

create table annot_dataset_num ( 
       dataset_id varchar(256) references ds (id) on delete cascade, 
       name  varchar(256),
       value numeric,
       primary key (dataset_id, name)
);

create table annot_dataset_text( 
       dataset_id varchar(256) references ds (id) on delete cascade, 
       name  varchar(256),
       value text,
       primary key (dataset_id, name)
);

create view provenance_graph_edge as 
       	    select function_call_id as parent, dataset_id as child 
       	    from dataset_out
       union all
       	    select dataset_id as parent, function_call_id as child 
	    from dataset_in;

create view annotation_text as 
       select dataset_id as entity_id, name as key, value as text_value, 'dataset' as entity_type from annot_dataset_text
       union all
       select function_call_id as entity_id, name as key, value as text_value, 'function_call' as entity_type from annot_function_call_text
       union all
       select app_exec_id as entity_id, name as key, value as text_value, 'app_exec' as entity_type from annot_app_exec_text
       union all
       select script_run_id as entity_id, name as key, value as text_value, 'script_run' as entity from annot_script_run_text;

create view annotation_numeric as 
       select dataset_id as entity_id, name as key, value as numeric_value, 'dataset' as entity_type from annot_dataset_num
       union all
       select function_call_id as entity_id, name as key, value as numeric_value, 'function_call' as entity_type from annot_function_call_num
       union all
       select app_exec_id as entity_id, name as key, value as numeric_value, 'app_exec' as entity_type from annot_app_exec_num
       union all
       select script_run_id as entity_id, name as key, value as numeric_value, 'script_run' as entity from annot_script_run_num;


create view annotation as
       select entity_id, entity_type, key, NULL as numeric_value, text_value from annotation_text
       union all
       select entity_id, entity_type, key, numeric_value, NULL as text_value from annotation_numeric;
       
create view script_and_fun_call as
       select script_run_summary.id as script_run_id, script_filename, swift_version, cog_version,
       	      script_run_summary.final_state as script_run_final_state, 
       	      script_run_summary.start_time as script_run_start_time,
       	      script_run_summary.duration as script_run_duration, 
      	      fun_call.id as function_call_id, 
       	      fun_call.type as function_call_type, 
       	      fun_call.name as function_call_name 
       from   script_run_summary,fun_call 
       where  fun_call.run_id=script_run_summary.id;


create view script_to_app_fun_call as
       select script_and_fun_call.*, app_fun_call.name as app_fun_call_name, app_fun_call.start_time as app_fun_call_start_time,
       	      app_fun_call.duration as app_fun_call_duration, app_fun_call.final_state as app_fun_call_final_state, 
	      app_fun_call.scratch as app_fun_call_scratch 
       from script_and_fun_call 
       	    left outer join 
       	    app_fun_call 
	    on (app_fun_call.id=script_and_fun_call.function_call_id);

create view script_to_app_exec as
       select script_to_app_fun_call.*, app_exec.id as app_exec_id, app_exec.start_time as app_exec_start_time,
       	      app_exec.duration as app_exec_duration, app_exec.final_state as app_exec_final_state, app_exec.site as app_exec_site 
       from script_to_app_fun_call 
       	    left outer join 
	    app_exec
	    on (script_to_app_fun_call.function_call_id=app_exec.app_fun_call_id);

create view script_to_app_exec_with_function_call_annotations  as
       select script_to_app_exec.*, annotation.key as function_call_annotation_key, annotation.numeric_value as function_call_annotation_numeric_value, 
              annotation.text_value as function_call_annotation_text_value 
       from   script_to_app_exec
              left outer join 
	      annotation 
       	      on (function_call_id=entity_id);

create view script_to_app_exec_with_runtime_stats as
       select script_to_app_fun_call.*, app_exec.id as app_exec_id, app_exec.start_time as app_exec_start_time,
       	      app_exec.duration as app_exec_duration, app_exec.final_state as app_exec_final_state, app_exec.site as app_exec_site,
	      app_exec.real_secs as app_exec_real_secs, app_exec.kernel_secs as app_exec_kernel_secs, 
	      app_exec.user_secs as app_exec_user_secs, app_exec.percent_cpu as app_exec_percent_cpu, app_exec.max_rss as app_exec_max_rss,
	      app_exec.avg_rss as app_exec_avg_rss, app_exec.avg_tot_vm as app_exec_avg_tot_vm, app_exec.avg_priv_data as app_exec_avg_priv_data, 
	      app_exec.avg_priv_stack as app_exec_avg_priv_stack, app_exec.avg_shared_text as app_exec_avg_shared_text,
	      app_exec.page_size as app_exec_page_size, app_exec.major_pgfaults as  app_exec_major_pgfaults, app_exec.minor_pgfaults as app_exec_minor_pgfaults,
	      app_exec.swaps as app_exec_swaps, app_exec.invol_context_switches as app_exec_invol_context_switches, 
	      app_exec.vol_waits as app_exec_vol_waits, app_exec.fs_reads as app_exec_fs_reads, app_exec.fs_writes as app_exec_fs_writes, 
	      app_exec.sock_recv as app_exec_sock_recv, app_exec.sock_send as app_exec_sock_send, app_exec.signals as app_exec_signals, 
	      app_exec.exit_status as app_exec_exit_status
       from script_to_app_fun_call 
       	    left outer join 
	    app_exec
	    on (script_to_app_fun_call.function_call_id=app_exec.app_fun_call_id);


create view dataset_primitive_and_mapped as 
       select mapped.id as dataset_id, 'mapped' as dataset_type, NULL as dataset_value, mapped.filename as dataset_filename
       from mapped
       union all
       select primitive.id as dataset_id, 'primitive' as dataset_type, primitive.value as dataset_value, NULL as dataset_filename
       from primitive;

create view dataset_all as
       select * from dataset_primitive_and_mapped
       union all
       select distinct dataset_containment.out_id as dataset_id, 'collection' as dataset_type, NULL as dataset_value, NULL as dataset_filename 
       from dataset_containment
       union all
       select collection.id as dataset_id, 'other' as dataset_type, NULL as dataset_value, NULL as dataset_filename
       from (
       	    select distinct * 
       	    from ds 
       	    where id 
	    not in (
	     select dataset_id 
	     from dataset_primitive_and_mapped 
	     union 
	     select dataset_containment.out_id as dataset_id 
	     from dataset_containment)) as collection;

create view dataset_all_with_annotations as
       select dataset_all.*, annotation.key as annotation_key, annotation.numeric_value as annotation_numeric_value, 
              annotation.text_value as annotation_text_value 
       from   dataset_all 
              left outer join 
	      annotation 
       	      on (dataset_id=entity_id);

create view dataset_all_with_annotations_and_parameter_names as
       select distinct dataset_all_with_annotations.dataset_id, dataset_all_with_annotations.dataset_type, dataset_io.parameter AS script_variable, 
       	      	       dataset_all_with_annotations.dataset_value, dataset_all_with_annotations.dataset_filename,
		       dataset_all_with_annotations.annotation_key, dataset_all_with_annotations.annotation_numeric_value, 
		       dataset_all_with_annotations.annotation_text_value
       from dataset_io
            natural join
	    dataset_all_with_annotations;

create view function_call_dataflow as
select dataset_in.dataset_id as input_dataset_id, dataset_in.parameter as input_parameter_name, dataset_in.function_call_id, 
       dataset_out.dataset_id as output_dataset_id, dataset_out.parameter as output_parameter_name
from   dataset_in, dataset_out 
where  dataset_in.function_call_id=dataset_out.function_call_id
union all
select NULL as input_dataset_id, NULL as input_parameter_name, dataset_out.function_call_id,
       dataset_out.dataset_id as output_dataset_id, dataset_out.parameter as output_parameter_name
from   dataset_out
where  dataset_out.function_call_id not in (select function_call_id from dataset_in)
union all
select dataset_in.dataset_id as input_dataset_id, dataset_in.parameter as input_parameter_name, dataset_in.function_call_id,
       NULL as output_dataset_id, NULL as output_parameter_name 
from   dataset_in
where  dataset_in.function_call_id not in (select function_call_id from dataset_out);


create view function_call_dataflow_with_input_details as
select dataset_all_with_annotations.dataset_id as input_dataset_id, dataset_all_with_annotations.dataset_type as input_dataset_type,
       function_call_dataflow.input_parameter_name, dataset_all_with_annotations.dataset_value as input_dataset_value,  
       dataset_all_with_annotations.dataset_filename as input_dataset_filename,
       dataset_all_with_annotations.annotation_key as input_dataset_annotation_key, 
       dataset_all_with_annotations.annotation_numeric_value as input_dataset_annotation_numeric_value,
       dataset_all_with_annotations.annotation_text_value as input_dataset_annotation_text_value, function_call_dataflow.function_call_id,
       function_call_dataflow.output_dataset_id, function_call_dataflow.output_parameter_name 
from 
dataset_all_with_annotations 
right outer join 
function_call_dataflow 
on (dataset_all_with_annotations.dataset_id=function_call_dataflow.input_dataset_id);

create view function_call_dataflow_with_dataset_details as
select function_call_dataflow_with_input_details.*,  dataset_all_with_annotations.dataset_type as output_dataset_type,
       dataset_all_with_annotations.dataset_value as output_dataset_value,  
       dataset_all_with_annotations.dataset_filename as output_dataset_filename,
       dataset_all_with_annotations.annotation_key as output_dataset_annotation_key, 
       dataset_all_with_annotations.annotation_numeric_value as output_dataset_annotation_numeric_value,
       dataset_all_with_annotations.annotation_text_value as output_dataset_annotation_text_value
from   function_call_dataflow_with_input_details
       left outer join 
       dataset_all_with_annotations 
       on (dataset_all_with_annotations.dataset_id=function_call_dataflow_with_input_details.output_dataset_id);
         
create view provenance_all as
select script_run_id, swift_version, cog_version, script_run_final_state, script_run_start_time, script_run_duration, script_filename,
       input_dataset_id, input_dataset_type, input_parameter_name, input_dataset_value, input_dataset_filename, 
       input_dataset_annotation_key, input_dataset_annotation_numeric_value, input_dataset_annotation_text_value,
       script_to_app_exec.function_call_id, function_call_type, function_call_name, app_fun_call_name, app_fun_call_start_time,
       app_fun_call_duration, app_fun_call_final_state, app_fun_call_scratch, app_exec_id, app_exec_start_time, app_exec_duration, 
       app_exec_final_state, app_exec_site, output_dataset_id, output_parameter_name, output_dataset_type, output_dataset_value, 
       output_dataset_filename, output_dataset_annotation_key, output_dataset_annotation_numeric_value, output_dataset_annotation_text_value 
from   function_call_dataflow_with_dataset_details
       right outer join
       script_to_app_exec on (function_call_dataflow_with_dataset_details.function_call_id=script_to_app_exec.function_call_id);

create view provenance_summary as
select script_run_id, script_filename, input_dataset_id, input_dataset_type, input_parameter_name, input_dataset_value, 
       input_dataset_filename, function_call_id, function_call_type, function_call_name, output_dataset_id, output_parameter_name, 
       output_dataset_type, output_dataset_value, output_dataset_filename 
from   provenance_all;

create view app_exec_stage_in as
select filename as staged_in_filename, id as app_exec_id, app_fun_call_id, start_time as app_exec_start_time, duration as app_exec_duration, 
       final_state as app_exec_final_state , site as app_exec_site, real_secs as app_exec_real_secs, kernel_secs as app_exec_kernel_secs,
       user_secs as app_exec_user_secs, percent_cpu as app_exec_percent_cpu, max_rss as app_exec_max_rss, avg_rss as app_exec_avg_rss,
       avg_tot_vm as app_exec_avg_tot_vm, avg_priv_data as app_exec_avg_priv_data, avg_priv_stack as app_exec_avg_priv_stack, 
       avg_shared_text as app_exec_avg_shared_text, page_size as app_exec_page_size, major_pgfaults as app_exec_major_pgfaults,	   
       minor_pgfaults as app_exec_minor_pgfaults, swaps as app_exec_swaps, invol_context_switches as app_exec_invol_context_switches,
       vol_waits as app_exec_vol_waits, fs_reads as app_exec_fs_reads, fs_writes as app_exec_fs_writes, sock_recv as app_exec_sock_recv,
       sock_send as app_exec_sock_send, signals as app_exec_signals, exit_status as app_exec_exit_status
from   stage_in,app_exec where stage_in.app_exec_id=app_exec.id;

create view app_exec_stage_out as
select filename as staged_in_filename, id as app_exec_id, app_fun_call_id, start_time as app_exec_start_time, duration as app_exec_duration, 
       final_state as app_exec_final_state , site as app_exec_site, real_secs as app_exec_real_secs, kernel_secs as app_exec_kernel_secs,
       user_secs as app_exec_user_secs, percent_cpu as app_exec_percent_cpu, max_rss as app_exec_max_rss, avg_rss as app_exec_avg_rss,
       avg_tot_vm as app_exec_avg_tot_vm, avg_priv_data as app_exec_avg_priv_data, avg_priv_stack as app_exec_avg_priv_stack, 
       avg_shared_text as app_exec_avg_shared_text, page_size as app_exec_page_size, major_pgfaults as app_exec_major_pgfaults,	   
       minor_pgfaults as app_exec_minor_pgfaults, swaps as app_exec_swaps, invol_context_switches as app_exec_invol_context_switches,
       vol_waits as app_exec_vol_waits, fs_reads as app_exec_fs_reads, fs_writes as app_exec_fs_writes, sock_recv as app_exec_sock_recv,
       sock_send as app_exec_sock_send, signals as app_exec_signals, exit_status as app_exec_exit_status
from   stage_out,app_exec where stage_out.app_exec_id=app_exec.id;

create view app_exec_staging as
select stage_in.filename as staged_in_filename, id as app_exec_id, app_fun_call_id, start_time as app_exec_start_time, duration as app_exec_duration,
       final_state as app_exec_final_state , site as app_exec_site, real_secs as app_exec_real_secs, kernel_secs as app_exec_kernel_secs,
       user_secs as app_exec_user_secs, percent_cpu as app_exec_percent_cpu, max_rss as app_exec_max_rss, avg_rss as app_exec_avg_rss,
       avg_tot_vm as app_exec_avg_tot_vm, avg_priv_data as app_exec_avg_priv_data, avg_priv_stack as app_exec_avg_priv_stack, 
       avg_shared_text as app_exec_avg_shared_text, page_size as app_exec_page_size, major_pgfaults as app_exec_major_pgfaults,	   
       minor_pgfaults as app_exec_minor_pgfaults, swaps as app_exec_swaps, invol_context_switches as app_exec_invol_context_switches,
       vol_waits as app_exec_vol_waits, fs_reads as app_exec_fs_reads, fs_writes as app_exec_fs_writes, sock_recv as app_exec_sock_recv,
       sock_send as app_exec_sock_send, signals as app_exec_signals, exit_status as app_exec_exit_status, stage_out.filename as staged_out_filename
from   stage_out,app_exec,stage_in where stage_in.app_exec_id=app_exec.id and app_exec.id=stage_out.app_exec_id;

create view app_exec_staging_all as
select run_id as script_run_id, fun_call.name as app_fun_call_name,  app_exec_staging.*
from   fun_call, app_exec_staging
where  fun_call.id=app_exec_staging.app_fun_call_id;

create view provenance_graph_edge_coarse as
       select app_exec_id as parent, filename as child 
       from   stage_out 
union all
       select filename as parent, app_exec_id as child 
       from   stage_in;

create view provenance_summary_coarse as
select script_run_id, staged_in_filename, app_fun_call_name, staged_out_filename 
from   app_exec_staging_all;
