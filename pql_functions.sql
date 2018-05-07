-- Set of PosrgreSQL-specific SQL functions and PL/pgSQL procedures 
-- to query provenance.

-- SQL Functions

-- lists variations in a parameter's value across workflows, for parameters that are in-memory variables

drop type compare_run_by_parameter_type cascade;
create type compare_run_by_parameter_type as (run_id varchar, parameter varchar, value varchar);

create or replace function compare_run_by_parameter(parameter_name varchar)
returns setof compare_run_by_parameter_type
as $$
   select run_id, parameter, value
   from   dataset_io,fun_call,primitive
   where  fun_call.id=dataset_io.function_call_id and dataset_io.dataset_id=primitive.id and parameter=$1;
$$ language sql;

-- PostgreSQL >= 9.0
-- CREATE OR REPLACE FUNCTION compare_run_by_parametereter(param_name VARCHAR) 
-- RETURNS TABLE (
--   run_id VARCHAR, 
--   param VARCHAR, 
--   value VARCHAR
-- )
-- AS $$
--    SELECT   fun_call.run_id, ds_out.parameter, primitive.value
--    FROM     primitive, ds_out, fun_call
--    WHERE    primitive.id=ds_out.dataset_id AND ds_out.fun_call_id=fun_call.id AND 
--             ds_out.parameter=$1 
--    GROUP BY fun_call.run_id, ds_out.parameter, primitive.value
--  UNION
--    SELECT   fun_call.run_id, ds_in.parameter, primitive.value
--    FROM     primitive, ds_in, fun_call
--    WHERE    primitive.id=ds_in.dataset_id AND ds_in.fun_call_id=fun_call.id AND 
--             ds_in.parameter=$1 
--    GROUP BY fun_call.run_id, ds_in.parameter, primitive.value	
--$$ LANGUAGE SQL;


--CREATE OR REPLACE FUNCTION compare_run_by_parametereter(parameter_name1 VARCHAR, parameter_name2 VARCHAR) 
--RETURNS TABLE (
--  workflow_id VARCHAR, 
--  parameter_name1 VARCHAR, 
--  value1 VARCHAR, 
--  parameter_name2 VARCHAR, 
--  value2 VARCHAR
--) 
--AS $$
--  SELECT * 
--  FROM   compare_run_by_parametereter($1) as t 
--         INNER JOIN 
--         compare_run_by_parametereter($2) as s 
--         USING (workflow_id); 
--$$ LANGUAGE SQL;

DROP TYPE compare_run_by_annot_num_type CASCADE;
CREATE TYPE compare_run_by_annot_num_type as (run_id VARCHAR, name VARCHAR, value NUMERIC);

CREATE OR REPLACE FUNCTION compare_run_by_annot_num(name VARCHAR)
RETURNS SETOF compare_run_by_annot_num_type
AS $$
    SELECT fun_call.run_id, annot_dataset_num.name, annot_dataset_num.value
    FROM   annot_dataset_num,dataset_io,dataset_containment,fun_call
    WHERE  annot_dataset_num.dataset_id=dataset_containment.in_id AND dataset_containment.out_id=dataset_io.dataset_id AND
           dataset_io.function_call_id=fun_call.id AND annot_dataset_num.name=$1
  UNION
    SELECT fun_call.run_id, annot_dataset_num.name, annot_dataset_num.value 
    FROM   fun_call, dataset_io, annot_dataset_num
    WHERE  fun_call.id=dataset_io.function_call_id and dataset_io.dataset_id=annot_dataset_num.dataset_id and
           annot_dataset_num.name=$1
  UNION
    SELECT fun_call.run_id, annot_function_call_num.name, annot_function_call_num.value 
    FROM   fun_call, annot_function_call_num
    WHERE  fun_call.id=annot_function_call_num.function_call_id and annot_function_call_num.name=$1
  UNION
    SELECT run.id as run_id, annot_script_run_num.name, annot_script_run_num.value 
    FROM   run, annot_script_run_num
    WHERE  run.id=annot_script_run_num.script_run_id and annot_script_run_num.name=$1
$$ LANGUAGE SQL;

DROP TYPE compare_run_by_key_numeric_type CASCADE;
CREATE TYPE compare_run_by_key_numeric_type as (run_id VARCHAR, name VARCHAR, value NUMERIC);

CREATE OR REPLACE FUNCTION compare_run_by_key_numeric(name VARCHAR)
RETURNS SETOF compare_run_by_key_numeric_type
AS $$
    SELECT fun_call.run_id, annot_dataset_num.name, annot_dataset_num.value
    FROM   annot_dataset_num,dataset_io,dataset_containment,fun_call
    WHERE  annot_dataset_num.dataset_id=dataset_containment.in_id AND dataset_containment.out_id=dataset_io.dataset_id AND
           dataset_io.function_call_id=fun_call.id AND annot_dataset_num.name=$1
  UNION
    SELECT fun_call.run_id, annot_dataset_num.name, annot_dataset_num.value 
    FROM   fun_call, dataset_io, annot_dataset_num
    WHERE  fun_call.id=dataset_io.function_call_id and dataset_io.dataset_id=annot_dataset_num.dataset_id and
           annot_dataset_num.name=$1
  UNION
    SELECT fun_call.run_id, annot_function_call_num.name, annot_function_call_num.value 
    FROM   fun_call, annot_function_call_num
    WHERE  fun_call.id=annot_function_call_num.function_call_id and annot_function_call_num.name=$1
  UNION
    SELECT run.id as run_id, annot_script_run_num.name, annot_script_run_num.value 
    FROM   run, annot_script_run_num
    WHERE  run.id=annot_script_run_num.script_run_id and annot_script_run_num.name=$1
$$ LANGUAGE SQL;


DROP TYPE compare_run_by_annot_txt_type CASCADE;
CREATE TYPE compare_run_by_annot_txt_type as (run_id VARCHAR, name VARCHAR, value VARCHAR);

CREATE OR REPLACE FUNCTION compare_run_by_annot_txt(name VARCHAR)
RETURNS SETOF compare_run_by_annot_txt_type
AS $$
    SELECT fun_call.run_id, annot_dataset_text.name, annot_dataset_text.value
    FROM   annot_dataset_text,dataset_io,dataset_containment,fun_call
    WHERE  annot_dataset_text.dataset_id=dataset_containment.in_id AND dataset_containment.out_id=dataset_io.dataset_id AND
           dataset_io.function_call_id=fun_call.id AND annot_dataset_text.name=$1
  UNION
    SELECT fun_call.run_id, annot_dataset_text.name, annot_dataset_text.value 
    FROM   fun_call, dataset_io, annot_dataset_text
    WHERE  fun_call.id=dataset_io.function_call_id and dataset_io.dataset_id=annot_dataset_text.dataset_id and
           annot_dataset_text.name=$1
  UNION
    SELECT fun_call.run_id, annot_function_call_text.name, annot_function_call_text.value 
    FROM   fun_call, annot_function_call_text
    WHERE  fun_call.id=annot_function_call_text.function_call_id and annot_function_call_text.name=$1
  UNION
    SELECT run.id as run_id, annot_script_run_text.name, annot_script_run_text.value 
    FROM   run, annot_script_run_text
    WHERE  run.id=annot_script_run_text.script_run_id and annot_script_run_text.name=$1
$$ LANGUAGE SQL;

DROP TYPE compare_run_by_key_text_type CASCADE;
CREATE TYPE compare_run_by_key_text_type as (run_id VARCHAR, name VARCHAR, value VARCHAR);

CREATE OR REPLACE FUNCTION compare_run_by_key_text(name VARCHAR)
RETURNS SETOF compare_run_by_key_text_type
AS $$
    SELECT fun_call.run_id, annot_dataset_text.name, annot_dataset_text.value
    FROM   annot_dataset_text,dataset_io,dataset_containment,fun_call
    WHERE  annot_dataset_text.dataset_id=dataset_containment.in_id AND dataset_containment.out_id=dataset_io.dataset_id AND
           dataset_io.function_call_id=fun_call.id AND annot_dataset_text.name=$1
  UNION
    SELECT fun_call.run_id, annot_dataset_text.name, annot_dataset_text.value 
    FROM   fun_call, dataset_io, annot_dataset_text
    WHERE  fun_call.id=dataset_io.function_call_id and dataset_io.dataset_id=annot_dataset_text.dataset_id and
           annot_dataset_text.name=$1
  UNION
    SELECT fun_call.run_id, annot_function_call_text.name, annot_function_call_text.value 
    FROM   fun_call, annot_function_call_text
    WHERE  fun_call.id=annot_function_call_text.function_call_id and annot_function_call_text.name=$1
  UNION
    SELECT run.id as run_id, annot_script_run_text.name, annot_script_run_text.value 
    FROM   run, annot_script_run_text
    WHERE  run.id=annot_script_run_text.script_run_id and annot_script_run_text.name=$1
$$ LANGUAGE SQL;

-- CREATE OR REPLACE FUNCTION compare_run_by_annot_num(name VARCHAR)
-- RETURNS TABLE (
--   workflow_id VARCHAR, 
--   name VARCHAR, 
--   value NUMERIC
-- )
-- AS $$
--     SELECT fun_call.workflow_id, annot_dataset_num.name, annot_dataset_num.value
--     FROM   annot_dataset_num,dataset_io,dataset_containment,fun_call
--     WHERE  annot_dataset_num.id=dataset_containment.in_id AND dataset_containment.out_id=dataset_io.dataset_id AND
--            dataset_io.function_call_id=fun_call.id AND annot_dataset_num.name=$1
--   UNION
--     SELECT fun_call.workflow_id, annot_dataset_num.name, annot_dataset_num.value 
--     FROM   fun_call, dataset_io, annot_dataset_num
--     WHERE  fun_call.id=dataset_io.function_call_id and dataset_io.dataset_id=annot_dataset_num.id and
--            annot_dataset_num.name=$1
--   UNION
--     SELECT fun_call.workflow_id, annot_p_num.name, annot_p_num.value 
--     FROM   fun_call, annot_p_num
--     WHERE  fun_call.id=annot_p_num.id and annot_p_num.name=$1
--   UNION
--     SELECT script_run.id as workflow_id, annot_wf_num.name, annot_wf_num.value 
--     FROM   workflow, annot_wf_num
--     WHERE  script_run.id=annot_wf_num.id and annot_wf_num.name=$1
-- $$ LANGUAGE SQL;


-- CREATE OR REPLACE FUNCTION compare_run_by_annot_txt(name VARCHAR)
-- RETURNS TABLE (
--   workflow_id VARCHAR, 
--   name VARCHAR, 
--   value VARCHAR) 
-- AS $$
--     SELECT   fun_call.workflow_id, annot_dataset_txt.name, annot_dataset_txt.value 
--     FROM     fun_call, dataset_io, annot_dataset_txt
--     WHERE    fun_call.id=dataset_io.function_call_id and dataset_io.dataset_id=annot_dataset_txt.id and
--              annot_dataset_txt.name=$1
--   UNION
--     SELECT   fun_call.workflow_id, annot_p_txt.name, annot_p_txt.value 
--     FROM     fun_call, annot_p_txt
--     WHERE    fun_call.id=annot_p_txt.id and annot_p_txt.name=$1
--   UNION
--     SELECT   script_run.id as workflow_id, annot_wf_txt.name, annot_wf_txt.value 
--     FROM     workflow, annot_wf_txt
--     WHERE    script_run.id=annot_wf_txt.id and annot_wf_txt.name=$1
-- $$ LANGUAGE SQL;


--CREATE OR REPLACE FUNCTION compare_run_by_annot_bool(name VARCHAR)
--RETURNS TABLE (
--  workflow_id VARCHAR,
--  name VARCHAR, 
--  value BOOLEAN
--) 
--AS $$
--    SELECT   fun_call.workflow_id, annot_dataset_bool.name, annot_dataset_bool.value 
--    FROM     fun_call, dataset_io, annot_dataset_bool
--    WHERE    fun_call.id=dataset_io.function_call_id and dataset_io.dataset_id=annot_dataset_bool.id and
--             annot_dataset_bool.name=$1
--  UNION
--    SELECT   fun_call.workflow_id, annot_p_bool.name, annot_p_bool.value 
--    FROM     fun_call, annot_p_bool
--    WHERE    fun_call.id=annot_p_bool.id and annot_p_bool.name=$1
--  UNION
--    SELECT   script_run.id as workflow_id, annot_wf_bool.name, annot_wf_bool.value 
--    FROM     workflow, annot_wf_bool
--   WHERE    script_run.id=annot_wf_bool.id and annot_wf_bool.name=$1
--$$ LANGUAGE SQL;


-- correlate a parameter with workflow runtime statistics
CREATE OR REPLACE FUNCTION correlate_parameter_runtime(parameter_name VARCHAR) 
RETURNS TABLE (
    run VARCHAR,  
    starttime TIMESTAMP WITH TIME ZONE, 
    duration NUMERIC, 
    parameter VARCHAR, 
    value VARCHAR
) 
AS $$
	SELECT script_run.id,script_run.start_time,script_run.duration,dataset_io.parameter,dataset_all.dataset_value
	FROM   dataset_all,dataset_io,fun_call,script_run
	WHERE  dataset_all.dataset_id=dataset_io.dataset_id AND dataset_io.function_call_id=fun_call.id AND 
	       fun_call.run_id=script_run.id AND dataset_io.parameter=$1 
$$ LANGUAGE SQL;

-- recursive query to find ancestor entities in a provenance graph
CREATE OR REPLACE FUNCTION ancestors(varchar) 
RETURNS SETOF varchar AS $$
  WITH RECURSIVE anc(ancestor,descendant) AS
  (    
       SELECT parent AS ancestor, child AS descendant 
       FROM   provenance_graph_edge 
       WHERE child=$1
     UNION
       SELECT provenance_graph_edge.parent AS ancestor, 
              anc.descendant AS descendant
       FROM   anc, provenance_graph_edge
       WHERE  anc.ancestor=provenance_graph_edge.child
  )
  SELECT ancestor FROM anc
$$ LANGUAGE SQL;


-- recursive query to find ancestor entities in a provenance graph
CREATE OR REPLACE FUNCTION ancestors_coarse(varchar) 
RETURNS SETOF varchar AS $$
  WITH RECURSIVE anc(ancestor,descendant) AS
  (    
       SELECT parent AS ancestor, child AS descendant 
       FROM   provenance_graph_edge_coarse
       WHERE child=$1
     UNION
       SELECT provenance_graph_edge_coarse.parent AS ancestor, 
              anc.descendant AS descendant
       FROM   anc, provenance_graph_edge_coarse
       WHERE  anc.ancestor=provenance_graph_edge_coarse.child
  )
  SELECT ancestor FROM anc
$$ LANGUAGE SQL;



-- compare(<entity>, <list of parameter_names, annotations keys>
CREATE OR REPLACE FUNCTION compare_run(VARIADIC args VARCHAR[])
RETURNS SETOF RECORD AS $$
DECLARE 
  i             INTEGER;
  q             VARCHAR;
  selectq       VARCHAR;
  fromq         VARCHAR;
  property      VARCHAR;
  property_type VARCHAR;
  function_name VARCHAR;
BEGIN 
  selectq := 'SELECT *';
  FOR i IN array_lower(args, 1)..array_upper(args, 1) LOOP
    property_type := split_part(args[i], '=', 1);
    property := split_part(args[i], '=', 2);
    CASE property_type
    WHEN 'parameter_name' THEN
      function_name := 'compare_run_by_parameter';
    WHEN 'annot_num' THEN
      function_name := 'compare_run_by_annot_num';
    WHEN 'annot_text' THEN
      function_name := 'compare_run_by_annot_text';
    END CASE;
    IF i = 1 THEN
      fromq := function_name || '(''' || property || ''') as t' || i;
    ELSE
      fromq := fromq || ' INNER JOIN ' || function_name || '(''' || property || ''') as t' || i || ' USING (run_id)';
    END IF;
  END LOOP;
  q := selectq || ' FROM ' || fromq;
  RETURN QUERY EXECUTE q;
END;
$$ LANGUAGE plpgsql;


