grammar SPQL;

@header {
	import java.util.HashSet;
	import java.util.HashMap;
	import java.util.Iterator;
	import org.jgrapht.*;
	import org.jgrapht.alg.DijkstraShortestPath;
	import org.jgrapht.graph.*;
}

@members {
	String selectClause = new String();
	String fromClause = new String();
	String whereClauseJoinExpressions = new String();
	String whereClause = new String(); 
	boolean hasWhereJoinExpression;
	boolean hasWhereExpression = false;
	static boolean hasCompareRunCall = false;
	HashSet<String> relations = new HashSet<String>();
	UndirectedGraph<String,DefaultEdge> schemaGraph;
	HashSet<DefaultEdge> joinEdges;
	static HashSet<String> compareRunParams = new HashSet<String>();;
	
	// Ideally it could receive a DB schema in SQL and build the graph automatically
	public static UndirectedGraph<String,DefaultEdge> buildGraph() {
		UndirectedGraph<String,DefaultEdge> schemaGraph = new Multigraph<String,DefaultEdge>(DefaultEdge.class);
		schemaGraph.addVertex("annotation");
		schemaGraph.addVertex("script_run");
		schemaGraph.addVertex("function_call");
		schemaGraph.addVertex("dataset");
		schemaGraph.addVertex("application_execution");
		schemaGraph.addVertex("runtime_info");
		schemaGraph.addVertex("dataset_containment");
		schemaGraph.addVertex("dataset_out");
		schemaGraph.addVertex("dataset_in");
		schemaGraph.addVertex("compare_run");
		//schemaGraph.addEdge("annotation", "script_run");
		//schemaGraph.addEdge("annotation", "function_call");
		//schemaGraph.addEdge("annotation", "variable");
		schemaGraph.addEdge("script_run", "function_call");
		schemaGraph.addEdge("function_call", "dataset_in");
		schemaGraph.addEdge("function_call", "dataset_out");
		schemaGraph.addEdge("function_call", "application_execution");
		schemaGraph.addEdge("application_execution", "runtime_info");
		schemaGraph.addEdge("dataset", "dataset_containment");
		schemaGraph.addEdge("dataset", "dataset_containment");
		schemaGraph.addEdge("dataset", "dataset_in");
		schemaGraph.addEdge("dataset", "dataset_out");

		return schemaGraph;
	}

	private static HashSet<DefaultEdge> computeJoinEdges(
			UndirectedGraph<String, DefaultEdge> schemaGraph,
			HashSet<String> relations) {
		HashSet<DefaultEdge> jEdges = new HashSet<DefaultEdge>();
		Iterator<String> i = relations.iterator();
		String first = new String();
		if(i.hasNext())
			first += i.next();
		while(i.hasNext()) {
			DijkstraShortestPath<String, DefaultEdge> sP = new DijkstraShortestPath<String, DefaultEdge>(schemaGraph, first, i.next());
			Iterator<DefaultEdge> j = (sP.getPathEdgeList()).iterator();
			while(j.hasNext())
				jEdges.add(j.next());
		}	
		return jEdges;
	}

	public static String computeFrom(UndirectedGraph<String,DefaultEdge> schemaGraph, HashSet<DefaultEdge> joinEdges, HashSet<String> qrels) {
		HashSet<String> fromRels = new HashSet<String>();
		String fromq = " FROM ";
		Iterator<DefaultEdge> i = joinEdges.iterator();
		Iterator<String> k = qrels.iterator();
		if(qrels.size() == 1) 
			fromRels.add(k.next()); 
		else 
			while(i.hasNext()) {
				DefaultEdge aux = i.next();
				// If ds_in or ds_out were not in the original select clause's relations and they are on the the joinEdges
				// then one has to make sure that both consumed and produced datasets are considered in the join so there
				// is no loss of information. One alternative, implemented here, is to replace these occurrences by the ds
				// view, which is an union of ds_in and ds_out.
				if(qrels.contains("dataset_out") || qrels.contains("dataset_in")) {
					fromRels.add(schemaGraph.getEdgeSource(aux));
					fromRels.add(schemaGraph.getEdgeTarget(aux));				
				}
				else {
					if(aux.equals(schemaGraph.getEdge("dataset_in","function_call")) || 
							aux.equals(schemaGraph.getEdge("dataset_in","dataset")) ||
							aux.equals(schemaGraph.getEdge("dataset_out","function_call")) ||
							aux.equals(schemaGraph.getEdge("dataset_out","dataset"))) {
						fromRels.add("dataset");
						fromRels.add("dataset_io");
						fromRels.add("function_call");
					}
					else {
						fromRels.add(schemaGraph.getEdgeSource(aux));
						fromRels.add(schemaGraph.getEdgeTarget(aux));				
					}
				}
			}
		Iterator<String> j = fromRels.iterator();
		if(j.hasNext())
			fromq += j.next();
		while(j.hasNext())
			fromq += "," + j.next();
		if(hasCompareRunCall) {
			if(fromRels.size() > 0)
				fromq += ",";
			fromq += "(" + computeCompareRunQuery(compareRunParams) + ") AS compare_run";
		}
		return fromq;
	}


	public static String computeJoinExpressions(UndirectedGraph<String,DefaultEdge> schemaGraph, HashSet<DefaultEdge> jEdges, HashSet<String> qrels) {

		HashMap<DefaultEdge,String> joinExpressions = new HashMap<DefaultEdge, String>();
		String joinExpressionsString = new String();

		//joinExpressions.put(schemaGraph.getEdge("annotation", "script_run"), "annotation.script_run_id=script_run.id");
		joinExpressions.put(schemaGraph.getEdge("script_run", "function_call"), "script_run.id=function_call.script_run_id");
		//joinExpressions.put(schemaGraph.getEdge("function_call", "annotation"), "function_call.id=annotation.function_call_id");
		joinExpressions.put(schemaGraph.getEdge("function_call", "dataset_out"), "function_call.id=dataset_out.function_call_id");
		joinExpressions.put(schemaGraph.getEdge("function_call", "dataset_in"), "function_call.id=dataset_in.function_call_id");
		joinExpressions.put(schemaGraph.getEdge("function_call", "application_execution"), "function_call.id=application_execution.function_call_id");
		joinExpressions.put(schemaGraph.getEdge("application_execution", "runtime_info"), "application_execution.id=runtime_info.application_execution_id");
		joinExpressions.put(schemaGraph.getEdge("dataset", "dataset_in"), "dataset.id=dataset_in.dataset_id");
		joinExpressions.put(schemaGraph.getEdge("dataset", "dataset_out"), "dataset.id=dataset_out.dataset_id");
		//joinExpressions.put(schemaGraph.getEdge("dataset", "annotation"), "dataset.id=annotation.dataset_id");
		joinExpressions.put(schemaGraph.getEdge("dataset", "containment"), "dataset.id=containment.containee");
		joinExpressions.put(schemaGraph.getEdge("dataset", "containment"), "dataset.id=containment.container");

		Iterator<DefaultEdge> i = jEdges.iterator();
		if(i.hasNext()) {
			DefaultEdge aux = i.next();
			if(qrels.contains("dataset_in") || qrels.contains("dataset_out")) {
				joinExpressionsString = joinExpressions.get(aux);
			}
			else {
				if(aux.equals(schemaGraph.getEdge("dataset_in","function_call")) || aux.equals(schemaGraph.getEdge("dataset_out","function_call")))
					joinExpressionsString = "dataset_io.function_call_id=function_call.id";
				else if(aux.equals(schemaGraph.getEdge("dataset_in","dataset")) || aux.equals(schemaGraph.getEdge("dataset_out","dataset"))) 
					joinExpressionsString = "dataset_io.dataset_id=dataset.id";
				else {
					joinExpressionsString = joinExpressions.get(aux);
				}

			}    		
		}


		while(i.hasNext()) {
			DefaultEdge aux = i.next();
			if(qrels.contains("dataset_in") || qrels.contains("dataset_out")) {
				joinExpressionsString += " AND " + joinExpressions.get(aux);
			}
			else {
				if(aux.equals(schemaGraph.getEdge("dataset_in","function_call")) || aux.equals(schemaGraph.getEdge("dataset_out","function_call")))
					joinExpressionsString += " AND " + "dataset_io.function_call_id=function_call.id";
				else if(aux.equals(schemaGraph.getEdge("dataset_in","dataset")) || aux.equals(schemaGraph.getEdge("dataset_out","dataset"))) 
					joinExpressionsString += " AND " + "dataset_io.dataset_id=dataset.id";
				else {
					joinExpressionsString += " AND " + joinExpressions.get(aux);
				}

			}    		
		}
		return joinExpressionsString;
	}

	public static String computeCompareRunQuery(HashSet<String> atoms) {
		String compareRunSelectClause = "SELECT script_run_id";
		String compareRunFromClause = "FROM";
		Iterator<String> i = atoms.iterator();
		int nId = 0;
		for(String arg: atoms) {
			String[] argTokens = arg.split("=");
			if(argTokens[0].equals("key_numeric") ||
			   argTokens[0].equals("key_text")    ||
			   argTokens[0].equals("parameter"))
			{
				String key = argTokens[1].split("'")[1];
				nId++;
				String sId = "j" + nId;
				compareRunSelectClause+=", " + sId + ".value as " + key;
				if(nId>1)
					compareRunFromClause += " INNER JOIN";
				compareRunFromClause += " compare_run_by_" + argTokens[0] + "(\'" + key + "\') as " + sId;
				if(nId>1)
					compareRunFromClause += " USING (script_run_id)";
			}
		}
		String compareRunQuery = compareRunSelectClause + " " + compareRunFromClause;
		return compareRunQuery;
	}
	
}

query	:	squery ( 
		( 
		UNION { System.out.println(" UNION "); }
		| 
		INTERSECT { System.out.println(" INTERSECT "); }
		| 
		EXCEPT { System.out.println(" EXCEPT "); }
		)
		(
		ALL { System.out.println(" ALL "); }
		)? 
		squery 
		)* 		
		SEMICOLON 
		{
			System.out.print(";");
		}
;

squery	:	SELECT 
		{
			System.out.print("SELECT ");
		}
		(
		DISTINCT
		{
			System.out.print("DISTINCT ");
		}
		)? 
		selectExpression 
		{ 
			System.out.print(selectClause);
		}
		(WHERE whereExpression
		{
						hasWhereExpression=true;
		}
		)?
		{
			schemaGraph = buildGraph();
			joinEdges = computeJoinEdges(schemaGraph, relations);
			hasWhereJoinExpression=false;

			fromClause += computeFrom(schemaGraph, joinEdges, relations);

			System.out.print(fromClause);
			
			whereClauseJoinExpressions += computeJoinExpressions(schemaGraph, joinEdges, relations);
			
			if(!whereClauseJoinExpressions.isEmpty()) {
				hasWhereJoinExpression=true;
				System.out.print(" WHERE " + whereClauseJoinExpressions);
			}

			if(hasWhereExpression) {
				if(hasWhereJoinExpression) 
					System.out.print(" AND ");
				else 
					System.out.print(" WHERE ");
				System.out.print(whereClause);
			}
		}
 		(
 		GROUP BY 
 		{
 			System.out.print(" GROUP BY ");
 		}
 		a=entityAndAttribute
 		{
 			System.out.print($a.text);
 		}
 		(
 		COLON
 		b=entityAndAttribute
 		{
 			System.out.print(",");
 			System.out.print($b.text);
 		}
 		)*
 		(
 		HAVING { System.out.print(" HAVING "); }
 		havingExpression
 		)?
 		)?
 		(
 		ORDER BY 
 		{
 			System.out.print(" ORDER BY ");
 		}
 		(
 		c=entityAndAttribute
 		{
 			System.out.print($c.text);
 		}
 		|
 		COUNT { System.out.print(" COUNT "); }
 		|
 		e=AGGRFUN { System.out.print(" " + $e.text + " "); }
 		)
 		(
 		COLON { System.out.print(","); }
 		(
 		d=entityAndAttribute
 		{
 			System.out.print($d.text);
 		}
 		 		|
 		COUNT { System.out.print(" COUNT "); }
 		|
 		f=AGGRFUN { System.out.print(" " + $f.text + " "); }

 		)
 		)*
 		(
 			DESC { System.out.print(" DESC "); }
 			| 
 			ASC  { System.out.print(" ASC "); }
 		)?
 		)?
		|
		'(' { System.out.print("("); }
		squery
		')' { System.out.print(")"); }
		;     


selectAtom
	:	a=entityAttribute 
		{ 
			selectClause += $a.text; 
			relations.add($a.text.split("\\.")[0]);
			if($a.text.split("\\.").length == 1)
				selectClause +=  ".*";
		}
		|
		b=AGGRFUN
		{
			selectClause+=$b.text;
		} 
		'(' 	{ selectClause+="("; }
		c=entityAndAttribute  
		{ 
			selectClause += $c.text; 
			relations.add($c.text.split("\\.")[0]);
			if($c.text.split("\\.").length == 1)
				selectClause +=  ".*";
		}
		')' 	{ selectClause+=")"; }
		|
		d=COUNT
		{
			selectClause+=$d.text;
		} 
		'(' 	{ selectClause+="("; }
		(
		e=entityAttribute  
		{ 
			selectClause += $e.text; 
			relations.add($e.text.split("\\.")[0]);
			if($e.text.split("\\.").length == 1)
				selectClause +=  ".*";
		}
		|
		'*' { selectClause+="*"; }
		)
		')' 	{ selectClause+=")"; }
		|
		builtInProcedureAttribute
	;

selectExpression
	:	(
			selectAtom		
		)
		(COLON { selectClause+=","; }
		(
			selectAtom
		)
		)*
	;

whereExpression	
	:	whereAtom
		(
			(AND 
			{
				whereClause += " AND ";
			}
			| OR
			{
				whereClause += " OR ";
			}
			) whereAtom
		)* 
	;

whereAtom 
	:	(a=entityAndAttribute 
		{
			relations.add($a.text.split("\\.")[0]);
			whereClause += $a.text;
		}
		|
		j=COMPARERUN { whereClause+="comapare_run"; }
		DOT 
		k=ID { whereClause+="."+$k.text; } 
		)
			(
		NOT
		{
			whereClause += " NOT ";
		}
	)? 

	(
	b=OP
	{
		whereClause += $b.text;
	} 
	(
	c=STRING
	{
		whereClause += $c.text;
	} 
	| 
	d=INT
	{
		whereClause += $d.text;
	} 
	|
	e=FLOAT
	{
		whereClause += $e.text;
	} 
	)
	|
	BETWEEN 
	{
		whereClause += " BETWEEN ";
	} 
	f=STRING 
	{
		whereClause += $f.text;
	} 
	AND 
	{
		whereClause += " AND ";
	} 
	g=STRING
	{
		whereClause += $g.text;
	} 
	|
	LIKE
	{
		whereClause += " LIKE ";
	} 
	h=STRING 
	{
		whereClause += $h.text;
	} 
		|
	(
	IN 
	{
		whereClause += " IN ";
	}
	|
		i=OP 
	{
		whereClause += $i.text;
	} 

	(
		ALL 
		{
			whereClause += " ALL ";
		}
		| 
		ANY
		{
			whereClause += " ANY ";
		}
	)

	)
		'(' { System.out.print("("); }
		squery
		')' { System.out.print(")"); }
	)
	;
	
havingExpression	
	:	havingAtom
		(
			(AND 
			{
				System.out.print(" AND ");
			}
			| OR
			{
				System.out.print(" OR ");
			}
			) havingAtom
		)* 
	;

	
havingAtom 
	:	a=entityAndAttribute 
		{ 
			System.out.print($a.text);
		}
			(
		NOT
		{
			System.out.print(" NOT ");
		}
	)? 

	(
	b=OP
	{
		System.out.print($b.text);
	} 
	(
	c=STRING
	{
		System.out.print($c.text);
	} 
	| 
	d=INT
	{
		System.out.print($d.text);
	} 
	|
	e=FLOAT
	{
		System.out.print($e.text);
	} 
	)
	|
	BETWEEN 
	{
		System.out.print(" BETWEEN ");
	} 
	f=STRING 
	{
		System.out.print($f.text);
	} 
	AND 
	{
		System.out.print(" AND ");
	} 
	g=STRING
	{
		System.out.print($g.text);
	} 
	|
	LIKE
	{
		System.out.print(" BETWEEN ");
	} 
	h=STRING 
	{
		System.out.print($h.text);
	} 
		|
	(
	IN 
	{
		System.out.print(" IN ");
	}
	|
		i=OP 
	{
		System.out.print($i.text);
	} 

	(
		ALL 
		{
			System.out.print(" ALL ");
		}
		| 
		ANY
		{
			System.out.print(" ANY ");
		}
	)

	)
		'(' { System.out.print("("); }
		squery
		')' { System.out.print(")"); }
	)
	;


entityAttribute	:	ID (DOT ID)?;

entityAndAttribute
	:	ID DOT ID;


builtInProcedureAttribute
	:	COMPARERUN {
			boolean hasAttribute = false;
		}
		{
			hasCompareRunCall=true;
		}
		'(' 
		a=builtInAtom
		{ 
			compareRunParams.add($a.text);
			if(relations.size() > 0)
				relations.add("script_run");
		}
	 	(COLON 
	 	b=builtInAtom
	 	{ 
			compareRunParams.add($b.text);
			if(relations.size() > 0)
				relations.add("script_run");
		} 		
	 	)* ')' ( 
	 	DOT
	 	{
	 		hasAttribute = true;
	 	}
	 	 (
	 	c=ID
	 	{
	 		selectClause += "compare_run." + $c.text;
	 	}
	 		 | '{' 
	 	d=ID
	 	{
	 		selectClause += "compare_run." + $d.text;
	 	}
	 	(COLON 
	 	e=ID 
	 	{ 
	 		selectClause += ", compare_run." + $e.text; 
	 	}
	 	)* '}'))?
	 	{
	 		if(!hasAttribute)
	 			selectClause += "compare_run.*";
	 	}
;

builtInAtom 
	:	('parameter' | 'key_numeric' | 'key_text') OP STRING;

OP	:	'=' | '>' | '>=' | '<' | '<=';

GROUP	:	'group';

ORDER	:	'order';

COMPARERUN
	:	'compare_run';

ANCESTOR:	'ancestor';

BY	:	'by';

AGGRFUN	:	'avg' | 'max' | 'min' | 'sum';

COUNT	:	'count';

SELECT 	:	'select';

DESC	:	'desc';

ASC	:	'asc';


DISTINCT 
	:	'distinct';
	
WHERE	:	'where';

AND	:	'and';

OR	:	'or';

NOT	:	'not';

IN	:	'in';

ANY	:	'any';

UNION	:	'union';

INTERSECT 
	:	'intersect';

EXCEPT	:	'except';

ALL	:	'all';

DOT	:	'.';

COLON	:	',';

BETWEEN	:	'between';

HAVING	:	'having';

LIKE 	:	'like';

SEMICOLON	:	';';

ID  :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-')*
    ;

INT :	'0'..'9'+
    ;

FLOAT
    :   ('0'..'9')+ '.' ('0'..'9')* 
    |   '.' ('0'..'9')+ 
    |   ('0'..'9')+ 
    ;

STRING
    :  '\'' ( 'a'..'z' | 'A'..'Z' | '_' | '-' | '0'..'9' | '.' | '%' | '/' | ':')* '\''
    ;
    
NEWLINE	:	'\r' ?	'\n';

WS	:	(' ' |'\t' |'\n' |'\r' )+	
	{
		skip();
	}
	;
