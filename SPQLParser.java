// $ANTLR 3.2 debian-7 SPQL.g 2013-11-04 17:00:24

	import java.util.HashSet;
	import java.util.HashMap;
	import java.util.Iterator;
	import org.jgrapht.*;
	import org.jgrapht.alg.DijkstraShortestPath;
	import org.jgrapht.graph.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class SPQLParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "UNION", "INTERSECT", "EXCEPT", "ALL", "SEMICOLON", "SELECT", "DISTINCT", "WHERE", "GROUP", "BY", "COLON", "HAVING", "ORDER", "COUNT", "AGGRFUN", "DESC", "ASC", "AND", "OR", "COMPARERUN", "DOT", "ID", "NOT", "OP", "STRING", "INT", "FLOAT", "BETWEEN", "LIKE", "IN", "ANY", "ANCESTOR", "NEWLINE", "WS", "'('", "')'", "'*'", "'{'", "'}'", "'parameter'", "'key_numeric'", "'key_text'"
    };
    public static final int WHERE=11;
    public static final int ORDER=16;
    public static final int COUNT=17;
    public static final int FLOAT=30;
    public static final int COMPARERUN=23;
    public static final int NOT=26;
    public static final int EXCEPT=6;
    public static final int AND=21;
    public static final int ID=25;
    public static final int EOF=-1;
    public static final int IN=33;
    public static final int ALL=7;
    public static final int ANCESTOR=35;
    public static final int DOT=24;
    public static final int AGGRFUN=18;
    public static final int SELECT=9;
    public static final int LIKE=32;
    public static final int T__42=42;
    public static final int T__43=43;
    public static final int T__40=40;
    public static final int BY=13;
    public static final int T__41=41;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int ASC=20;
    public static final int SEMICOLON=8;
    public static final int HAVING=15;
    public static final int INT=29;
    public static final int UNION=4;
    public static final int COLON=14;
    public static final int INTERSECT=5;
    public static final int GROUP=12;
    public static final int WS=37;
    public static final int ANY=34;
    public static final int NEWLINE=36;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int OP=27;
    public static final int OR=22;
    public static final int DESC=19;
    public static final int DISTINCT=10;
    public static final int BETWEEN=31;
    public static final int STRING=28;

    // delegates
    // delegators


        public SPQLParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public SPQLParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return SPQLParser.tokenNames; }
    public String getGrammarFileName() { return "SPQL.g"; }


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
    	



    // $ANTLR start "query"
    // SPQL.g:202:1: query : squery ( ( UNION | INTERSECT | EXCEPT ) ( ALL )? squery )* SEMICOLON ;
    public final void query() throws RecognitionException {
        try {
            // SPQL.g:202:7: ( squery ( ( UNION | INTERSECT | EXCEPT ) ( ALL )? squery )* SEMICOLON )
            // SPQL.g:202:9: squery ( ( UNION | INTERSECT | EXCEPT ) ( ALL )? squery )* SEMICOLON
            {
            pushFollow(FOLLOW_squery_in_query22);
            squery();

            state._fsp--;

            // SPQL.g:202:16: ( ( UNION | INTERSECT | EXCEPT ) ( ALL )? squery )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>=UNION && LA3_0<=EXCEPT)) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // SPQL.g:203:3: ( UNION | INTERSECT | EXCEPT ) ( ALL )? squery
            	    {
            	    // SPQL.g:203:3: ( UNION | INTERSECT | EXCEPT )
            	    int alt1=3;
            	    switch ( input.LA(1) ) {
            	    case UNION:
            	        {
            	        alt1=1;
            	        }
            	        break;
            	    case INTERSECT:
            	        {
            	        alt1=2;
            	        }
            	        break;
            	    case EXCEPT:
            	        {
            	        alt1=3;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 1, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt1) {
            	        case 1 :
            	            // SPQL.g:204:3: UNION
            	            {
            	            match(input,UNION,FOLLOW_UNION_in_query34); 
            	             System.out.println(" UNION "); 

            	            }
            	            break;
            	        case 2 :
            	            // SPQL.g:206:3: INTERSECT
            	            {
            	            match(input,INTERSECT,FOLLOW_INTERSECT_in_query45); 
            	             System.out.println(" INTERSECT "); 

            	            }
            	            break;
            	        case 3 :
            	            // SPQL.g:208:3: EXCEPT
            	            {
            	            match(input,EXCEPT,FOLLOW_EXCEPT_in_query56); 
            	             System.out.println(" EXCEPT "); 

            	            }
            	            break;

            	    }

            	    // SPQL.g:210:3: ( ALL )?
            	    int alt2=2;
            	    int LA2_0 = input.LA(1);

            	    if ( (LA2_0==ALL) ) {
            	        alt2=1;
            	    }
            	    switch (alt2) {
            	        case 1 :
            	            // SPQL.g:211:3: ALL
            	            {
            	            match(input,ALL,FOLLOW_ALL_in_query70); 
            	             System.out.println(" ALL "); 

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_squery_in_query82);
            	    squery();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            match(input,SEMICOLON,FOLLOW_SEMICOLON_in_query95); 

            			System.out.print(";");
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "query"


    // $ANTLR start "squery"
    // SPQL.g:221:1: squery : ( SELECT ( DISTINCT )? selectExpression ( WHERE whereExpression )? ( GROUP BY a= entityAndAttribute ( COLON b= entityAndAttribute )* ( HAVING havingExpression )? )? ( ORDER BY (c= entityAndAttribute | COUNT | e= AGGRFUN ) ( COLON (d= entityAndAttribute | COUNT | f= AGGRFUN ) )* ( DESC | ASC )? )? | '(' squery ')' );
    public final void squery() throws RecognitionException {
        Token e=null;
        Token f=null;
        SPQLParser.entityAndAttribute_return a = null;

        SPQLParser.entityAndAttribute_return b = null;

        SPQLParser.entityAndAttribute_return c = null;

        SPQLParser.entityAndAttribute_return d = null;


        try {
            // SPQL.g:221:8: ( SELECT ( DISTINCT )? selectExpression ( WHERE whereExpression )? ( GROUP BY a= entityAndAttribute ( COLON b= entityAndAttribute )* ( HAVING havingExpression )? )? ( ORDER BY (c= entityAndAttribute | COUNT | e= AGGRFUN ) ( COLON (d= entityAndAttribute | COUNT | f= AGGRFUN ) )* ( DESC | ASC )? )? | '(' squery ')' )
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==SELECT) ) {
                alt14=1;
            }
            else if ( (LA14_0==38) ) {
                alt14=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;
            }
            switch (alt14) {
                case 1 :
                    // SPQL.g:221:10: SELECT ( DISTINCT )? selectExpression ( WHERE whereExpression )? ( GROUP BY a= entityAndAttribute ( COLON b= entityAndAttribute )* ( HAVING havingExpression )? )? ( ORDER BY (c= entityAndAttribute | COUNT | e= AGGRFUN ) ( COLON (d= entityAndAttribute | COUNT | f= AGGRFUN ) )* ( DESC | ASC )? )?
                    {
                    match(input,SELECT,FOLLOW_SELECT_in_squery109); 

                    			System.out.print("SELECT ");
                    		
                    // SPQL.g:225:3: ( DISTINCT )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0==DISTINCT) ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // SPQL.g:226:3: DISTINCT
                            {
                            match(input,DISTINCT,FOLLOW_DISTINCT_in_squery122); 

                            			System.out.print("DISTINCT ");
                            		

                            }
                            break;

                    }

                    pushFollow(FOLLOW_selectExpression_in_squery136);
                    selectExpression();

                    state._fsp--;

                     
                    			System.out.print(selectClause);
                    		
                    // SPQL.g:235:3: ( WHERE whereExpression )?
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0==WHERE) ) {
                        alt5=1;
                    }
                    switch (alt5) {
                        case 1 :
                            // SPQL.g:235:4: WHERE whereExpression
                            {
                            match(input,WHERE,FOLLOW_WHERE_in_squery146); 
                            pushFollow(FOLLOW_whereExpression_in_squery148);
                            whereExpression();

                            state._fsp--;


                            						hasWhereExpression=true;
                            		

                            }
                            break;

                    }


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
                    		
                    // SPQL.g:264:4: ( GROUP BY a= entityAndAttribute ( COLON b= entityAndAttribute )* ( HAVING havingExpression )? )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==GROUP) ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // SPQL.g:265:4: GROUP BY a= entityAndAttribute ( COLON b= entityAndAttribute )* ( HAVING havingExpression )?
                            {
                            match(input,GROUP,FOLLOW_GROUP_in_squery171); 
                            match(input,BY,FOLLOW_BY_in_squery173); 

                             			System.out.print(" GROUP BY ");
                             		
                            pushFollow(FOLLOW_entityAndAttribute_in_squery186);
                            a=entityAndAttribute();

                            state._fsp--;


                             			System.out.print((a!=null?input.toString(a.start,a.stop):null));
                             		
                            // SPQL.g:273:4: ( COLON b= entityAndAttribute )*
                            loop6:
                            do {
                                int alt6=2;
                                int LA6_0 = input.LA(1);

                                if ( (LA6_0==COLON) ) {
                                    alt6=1;
                                }


                                switch (alt6) {
                            	case 1 :
                            	    // SPQL.g:274:4: COLON b= entityAndAttribute
                            	    {
                            	    match(input,COLON,FOLLOW_COLON_in_squery201); 
                            	    pushFollow(FOLLOW_entityAndAttribute_in_squery208);
                            	    b=entityAndAttribute();

                            	    state._fsp--;


                            	     			System.out.print(",");
                            	     			System.out.print((b!=null?input.toString(b.start,b.stop):null));
                            	     		

                            	    }
                            	    break;

                            	default :
                            	    break loop6;
                                }
                            } while (true);

                            // SPQL.g:281:4: ( HAVING havingExpression )?
                            int alt7=2;
                            int LA7_0 = input.LA(1);

                            if ( (LA7_0==HAVING) ) {
                                alt7=1;
                            }
                            switch (alt7) {
                                case 1 :
                                    // SPQL.g:282:4: HAVING havingExpression
                                    {
                                    match(input,HAVING,FOLLOW_HAVING_in_squery229); 
                                     System.out.print(" HAVING "); 
                                    pushFollow(FOLLOW_havingExpression_in_squery236);
                                    havingExpression();

                                    state._fsp--;


                                    }
                                    break;

                            }


                            }
                            break;

                    }

                    // SPQL.g:286:4: ( ORDER BY (c= entityAndAttribute | COUNT | e= AGGRFUN ) ( COLON (d= entityAndAttribute | COUNT | f= AGGRFUN ) )* ( DESC | ASC )? )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==ORDER) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // SPQL.g:287:4: ORDER BY (c= entityAndAttribute | COUNT | e= AGGRFUN ) ( COLON (d= entityAndAttribute | COUNT | f= AGGRFUN ) )* ( DESC | ASC )?
                            {
                            match(input,ORDER,FOLLOW_ORDER_in_squery258); 
                            match(input,BY,FOLLOW_BY_in_squery260); 

                             			System.out.print(" ORDER BY ");
                             		
                            // SPQL.g:291:4: (c= entityAndAttribute | COUNT | e= AGGRFUN )
                            int alt9=3;
                            switch ( input.LA(1) ) {
                            case ID:
                                {
                                alt9=1;
                                }
                                break;
                            case COUNT:
                                {
                                alt9=2;
                                }
                                break;
                            case AGGRFUN:
                                {
                                alt9=3;
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("", 9, 0, input);

                                throw nvae;
                            }

                            switch (alt9) {
                                case 1 :
                                    // SPQL.g:292:4: c= entityAndAttribute
                                    {
                                    pushFollow(FOLLOW_entityAndAttribute_in_squery278);
                                    c=entityAndAttribute();

                                    state._fsp--;


                                     			System.out.print((c!=null?input.toString(c.start,c.stop):null));
                                     		

                                    }
                                    break;
                                case 2 :
                                    // SPQL.g:297:4: COUNT
                                    {
                                    match(input,COUNT,FOLLOW_COUNT_in_squery293); 
                                     System.out.print(" COUNT "); 

                                    }
                                    break;
                                case 3 :
                                    // SPQL.g:299:4: e= AGGRFUN
                                    {
                                    e=(Token)match(input,AGGRFUN,FOLLOW_AGGRFUN_in_squery307); 
                                     System.out.print(" " + (e!=null?e.getText():null) + " "); 

                                    }
                                    break;

                            }

                            // SPQL.g:301:4: ( COLON (d= entityAndAttribute | COUNT | f= AGGRFUN ) )*
                            loop11:
                            do {
                                int alt11=2;
                                int LA11_0 = input.LA(1);

                                if ( (LA11_0==COLON) ) {
                                    alt11=1;
                                }


                                switch (alt11) {
                            	case 1 :
                            	    // SPQL.g:302:4: COLON (d= entityAndAttribute | COUNT | f= AGGRFUN )
                            	    {
                            	    match(input,COLON,FOLLOW_COLON_in_squery324); 
                            	     System.out.print(","); 
                            	    // SPQL.g:303:4: (d= entityAndAttribute | COUNT | f= AGGRFUN )
                            	    int alt10=3;
                            	    switch ( input.LA(1) ) {
                            	    case ID:
                            	        {
                            	        alt10=1;
                            	        }
                            	        break;
                            	    case COUNT:
                            	        {
                            	        alt10=2;
                            	        }
                            	        break;
                            	    case AGGRFUN:
                            	        {
                            	        alt10=3;
                            	        }
                            	        break;
                            	    default:
                            	        NoViableAltException nvae =
                            	            new NoViableAltException("", 10, 0, input);

                            	        throw nvae;
                            	    }

                            	    switch (alt10) {
                            	        case 1 :
                            	            // SPQL.g:304:4: d= entityAndAttribute
                            	            {
                            	            pushFollow(FOLLOW_entityAndAttribute_in_squery338);
                            	            d=entityAndAttribute();

                            	            state._fsp--;


                            	             			System.out.print((d!=null?input.toString(d.start,d.stop):null));
                            	             		

                            	            }
                            	            break;
                            	        case 2 :
                            	            // SPQL.g:309:4: COUNT
                            	            {
                            	            match(input,COUNT,FOLLOW_COUNT_in_squery356); 
                            	             System.out.print(" COUNT "); 

                            	            }
                            	            break;
                            	        case 3 :
                            	            // SPQL.g:311:4: f= AGGRFUN
                            	            {
                            	            f=(Token)match(input,AGGRFUN,FOLLOW_AGGRFUN_in_squery370); 
                            	             System.out.print(" " + (f!=null?f.getText():null) + " "); 

                            	            }
                            	            break;

                            	    }


                            	    }
                            	    break;

                            	default :
                            	    break loop11;
                                }
                            } while (true);

                            // SPQL.g:315:4: ( DESC | ASC )?
                            int alt12=3;
                            int LA12_0 = input.LA(1);

                            if ( (LA12_0==DESC) ) {
                                alt12=1;
                            }
                            else if ( (LA12_0==ASC) ) {
                                alt12=2;
                            }
                            switch (alt12) {
                                case 1 :
                                    // SPQL.g:316:5: DESC
                                    {
                                    match(input,DESC,FOLLOW_DESC_in_squery395); 
                                     System.out.print(" DESC "); 

                                    }
                                    break;
                                case 2 :
                                    // SPQL.g:318:5: ASC
                                    {
                                    match(input,ASC,FOLLOW_ASC_in_squery410); 
                                     System.out.print(" ASC "); 

                                    }
                                    break;

                            }


                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // SPQL.g:322:3: '(' squery ')'
                    {
                    match(input,38,FOLLOW_38_in_squery433); 
                     System.out.print("("); 
                    pushFollow(FOLLOW_squery_in_squery439);
                    squery();

                    state._fsp--;

                    match(input,39,FOLLOW_39_in_squery443); 
                     System.out.print(")"); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "squery"


    // $ANTLR start "selectAtom"
    // SPQL.g:328:1: selectAtom : (a= entityAttribute | b= AGGRFUN '(' c= entityAndAttribute ')' | d= COUNT '(' (e= entityAttribute | '*' ) ')' | builtInProcedureAttribute );
    public final void selectAtom() throws RecognitionException {
        Token b=null;
        Token d=null;
        SPQLParser.entityAttribute_return a = null;

        SPQLParser.entityAndAttribute_return c = null;

        SPQLParser.entityAttribute_return e = null;


        try {
            // SPQL.g:329:2: (a= entityAttribute | b= AGGRFUN '(' c= entityAndAttribute ')' | d= COUNT '(' (e= entityAttribute | '*' ) ')' | builtInProcedureAttribute )
            int alt16=4;
            switch ( input.LA(1) ) {
            case ID:
                {
                alt16=1;
                }
                break;
            case AGGRFUN:
                {
                alt16=2;
                }
                break;
            case COUNT:
                {
                alt16=3;
                }
                break;
            case COMPARERUN:
                {
                alt16=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;
            }

            switch (alt16) {
                case 1 :
                    // SPQL.g:329:4: a= entityAttribute
                    {
                    pushFollow(FOLLOW_entityAttribute_in_selectAtom465);
                    a=entityAttribute();

                    state._fsp--;

                     
                    			selectClause += (a!=null?input.toString(a.start,a.stop):null); 
                    			relations.add((a!=null?input.toString(a.start,a.stop):null).split("\\.")[0]);
                    			if((a!=null?input.toString(a.start,a.stop):null).split("\\.").length == 1)
                    				selectClause +=  ".*";
                    		

                    }
                    break;
                case 2 :
                    // SPQL.g:337:3: b= AGGRFUN '(' c= entityAndAttribute ')'
                    {
                    b=(Token)match(input,AGGRFUN,FOLLOW_AGGRFUN_in_selectAtom480); 

                    			selectClause+=(b!=null?b.getText():null);
                    		
                    match(input,38,FOLLOW_38_in_selectAtom489); 
                     selectClause+="("; 
                    pushFollow(FOLLOW_entityAndAttribute_in_selectAtom498);
                    c=entityAndAttribute();

                    state._fsp--;

                     
                    			selectClause += (c!=null?input.toString(c.start,c.stop):null); 
                    			relations.add((c!=null?input.toString(c.start,c.stop):null).split("\\.")[0]);
                    			if((c!=null?input.toString(c.start,c.stop):null).split("\\.").length == 1)
                    				selectClause +=  ".*";
                    		
                    match(input,39,FOLLOW_39_in_selectAtom508); 
                     selectClause+=")"; 

                    }
                    break;
                case 3 :
                    // SPQL.g:351:3: d= COUNT '(' (e= entityAttribute | '*' ) ')'
                    {
                    d=(Token)match(input,COUNT,FOLLOW_COUNT_in_selectAtom521); 

                    			selectClause+=(d!=null?d.getText():null);
                    		
                    match(input,38,FOLLOW_38_in_selectAtom530); 
                     selectClause+="("; 
                    // SPQL.g:356:3: (e= entityAttribute | '*' )
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==ID) ) {
                        alt15=1;
                    }
                    else if ( (LA15_0==40) ) {
                        alt15=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 15, 0, input);

                        throw nvae;
                    }
                    switch (alt15) {
                        case 1 :
                            // SPQL.g:357:3: e= entityAttribute
                            {
                            pushFollow(FOLLOW_entityAttribute_in_selectAtom543);
                            e=entityAttribute();

                            state._fsp--;

                             
                            			selectClause += (e!=null?input.toString(e.start,e.stop):null); 
                            			relations.add((e!=null?input.toString(e.start,e.stop):null).split("\\.")[0]);
                            			if((e!=null?input.toString(e.start,e.stop):null).split("\\.").length == 1)
                            				selectClause +=  ".*";
                            		

                            }
                            break;
                        case 2 :
                            // SPQL.g:365:3: '*'
                            {
                            match(input,40,FOLLOW_40_in_selectAtom557); 
                             selectClause+="*"; 

                            }
                            break;

                    }

                    match(input,39,FOLLOW_39_in_selectAtom567); 
                     selectClause+=")"; 

                    }
                    break;
                case 4 :
                    // SPQL.g:369:3: builtInProcedureAttribute
                    {
                    pushFollow(FOLLOW_builtInProcedureAttribute_in_selectAtom578);
                    builtInProcedureAttribute();

                    state._fsp--;


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "selectAtom"


    // $ANTLR start "selectExpression"
    // SPQL.g:372:1: selectExpression : ( selectAtom ) ( COLON ( selectAtom ) )* ;
    public final void selectExpression() throws RecognitionException {
        try {
            // SPQL.g:373:2: ( ( selectAtom ) ( COLON ( selectAtom ) )* )
            // SPQL.g:373:4: ( selectAtom ) ( COLON ( selectAtom ) )*
            {
            // SPQL.g:373:4: ( selectAtom )
            // SPQL.g:374:4: selectAtom
            {
            pushFollow(FOLLOW_selectAtom_in_selectExpression594);
            selectAtom();

            state._fsp--;


            }

            // SPQL.g:376:3: ( COLON ( selectAtom ) )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==COLON) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // SPQL.g:376:4: COLON ( selectAtom )
            	    {
            	    match(input,COLON,FOLLOW_COLON_in_selectExpression605); 
            	     selectClause+=","; 
            	    // SPQL.g:377:3: ( selectAtom )
            	    // SPQL.g:378:4: selectAtom
            	    {
            	    pushFollow(FOLLOW_selectAtom_in_selectExpression616);
            	    selectAtom();

            	    state._fsp--;


            	    }


            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "selectExpression"


    // $ANTLR start "whereExpression"
    // SPQL.g:383:1: whereExpression : whereAtom ( ( AND | OR ) whereAtom )* ;
    public final void whereExpression() throws RecognitionException {
        try {
            // SPQL.g:384:2: ( whereAtom ( ( AND | OR ) whereAtom )* )
            // SPQL.g:384:4: whereAtom ( ( AND | OR ) whereAtom )*
            {
            pushFollow(FOLLOW_whereAtom_in_whereExpression637);
            whereAtom();

            state._fsp--;

            // SPQL.g:385:3: ( ( AND | OR ) whereAtom )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( ((LA19_0>=AND && LA19_0<=OR)) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // SPQL.g:386:4: ( AND | OR ) whereAtom
            	    {
            	    // SPQL.g:386:4: ( AND | OR )
            	    int alt18=2;
            	    int LA18_0 = input.LA(1);

            	    if ( (LA18_0==AND) ) {
            	        alt18=1;
            	    }
            	    else if ( (LA18_0==OR) ) {
            	        alt18=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 18, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt18) {
            	        case 1 :
            	            // SPQL.g:386:5: AND
            	            {
            	            match(input,AND,FOLLOW_AND_in_whereExpression647); 

            	            				whereClause += " AND ";
            	            			

            	            }
            	            break;
            	        case 2 :
            	            // SPQL.g:390:6: OR
            	            {
            	            match(input,OR,FOLLOW_OR_in_whereExpression660); 

            	            				whereClause += " OR ";
            	            			

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_whereAtom_in_whereExpression672);
            	    whereAtom();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "whereExpression"


    // $ANTLR start "whereAtom"
    // SPQL.g:398:1: whereAtom : (a= entityAndAttribute | j= COMPARERUN DOT k= ID ) ( NOT )? (b= OP (c= STRING | d= INT | e= FLOAT ) | BETWEEN f= STRING AND g= STRING | LIKE h= STRING | ( IN | i= OP ( ALL | ANY ) ) '(' squery ')' ) ;
    public final void whereAtom() throws RecognitionException {
        Token j=null;
        Token k=null;
        Token b=null;
        Token c=null;
        Token d=null;
        Token e=null;
        Token f=null;
        Token g=null;
        Token h=null;
        Token i=null;
        SPQLParser.entityAndAttribute_return a = null;


        try {
            // SPQL.g:399:2: ( (a= entityAndAttribute | j= COMPARERUN DOT k= ID ) ( NOT )? (b= OP (c= STRING | d= INT | e= FLOAT ) | BETWEEN f= STRING AND g= STRING | LIKE h= STRING | ( IN | i= OP ( ALL | ANY ) ) '(' squery ')' ) )
            // SPQL.g:399:4: (a= entityAndAttribute | j= COMPARERUN DOT k= ID ) ( NOT )? (b= OP (c= STRING | d= INT | e= FLOAT ) | BETWEEN f= STRING AND g= STRING | LIKE h= STRING | ( IN | i= OP ( ALL | ANY ) ) '(' squery ')' )
            {
            // SPQL.g:399:4: (a= entityAndAttribute | j= COMPARERUN DOT k= ID )
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==ID) ) {
                alt20=1;
            }
            else if ( (LA20_0==COMPARERUN) ) {
                alt20=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;
            }
            switch (alt20) {
                case 1 :
                    // SPQL.g:399:5: a= entityAndAttribute
                    {
                    pushFollow(FOLLOW_entityAndAttribute_in_whereAtom693);
                    a=entityAndAttribute();

                    state._fsp--;


                    			relations.add((a!=null?input.toString(a.start,a.stop):null).split("\\.")[0]);
                    			whereClause += (a!=null?input.toString(a.start,a.stop):null);
                    		

                    }
                    break;
                case 2 :
                    // SPQL.g:405:3: j= COMPARERUN DOT k= ID
                    {
                    j=(Token)match(input,COMPARERUN,FOLLOW_COMPARERUN_in_whereAtom708); 
                     whereClause+="comapare_run"; 
                    match(input,DOT,FOLLOW_DOT_in_whereAtom714); 
                    k=(Token)match(input,ID,FOLLOW_ID_in_whereAtom721); 
                     whereClause+="."+(k!=null?k.getText():null); 

                    }
                    break;

            }

            // SPQL.g:409:4: ( NOT )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==NOT) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // SPQL.g:410:3: NOT
                    {
                    match(input,NOT,FOLLOW_NOT_in_whereAtom737); 

                    			whereClause += " NOT ";
                    		

                    }
                    break;

            }

            // SPQL.g:416:2: (b= OP (c= STRING | d= INT | e= FLOAT ) | BETWEEN f= STRING AND g= STRING | LIKE h= STRING | ( IN | i= OP ( ALL | ANY ) ) '(' squery ')' )
            int alt25=4;
            switch ( input.LA(1) ) {
            case OP:
                {
                int LA25_1 = input.LA(2);

                if ( (LA25_1==ALL||LA25_1==ANY) ) {
                    alt25=4;
                }
                else if ( ((LA25_1>=STRING && LA25_1<=FLOAT)) ) {
                    alt25=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 25, 1, input);

                    throw nvae;
                }
                }
                break;
            case BETWEEN:
                {
                alt25=2;
                }
                break;
            case LIKE:
                {
                alt25=3;
                }
                break;
            case IN:
                {
                alt25=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;
            }

            switch (alt25) {
                case 1 :
                    // SPQL.g:417:2: b= OP (c= STRING | d= INT | e= FLOAT )
                    {
                    b=(Token)match(input,OP,FOLLOW_OP_in_whereAtom755); 

                    		whereClause += (b!=null?b.getText():null);
                    	
                    // SPQL.g:421:2: (c= STRING | d= INT | e= FLOAT )
                    int alt22=3;
                    switch ( input.LA(1) ) {
                    case STRING:
                        {
                        alt22=1;
                        }
                        break;
                    case INT:
                        {
                        alt22=2;
                        }
                        break;
                    case FLOAT:
                        {
                        alt22=3;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 22, 0, input);

                        throw nvae;
                    }

                    switch (alt22) {
                        case 1 :
                            // SPQL.g:422:2: c= STRING
                            {
                            c=(Token)match(input,STRING,FOLLOW_STRING_in_whereAtom767); 

                            		whereClause += (c!=null?c.getText():null);
                            	

                            }
                            break;
                        case 2 :
                            // SPQL.g:427:2: d= INT
                            {
                            d=(Token)match(input,INT,FOLLOW_INT_in_whereAtom780); 

                            		whereClause += (d!=null?d.getText():null);
                            	

                            }
                            break;
                        case 3 :
                            // SPQL.g:432:2: e= FLOAT
                            {
                            e=(Token)match(input,FLOAT,FOLLOW_FLOAT_in_whereAtom792); 

                            		whereClause += (e!=null?e.getText():null);
                            	

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // SPQL.g:438:2: BETWEEN f= STRING AND g= STRING
                    {
                    match(input,BETWEEN,FOLLOW_BETWEEN_in_whereAtom805); 

                    		whereClause += " BETWEEN ";
                    	
                    f=(Token)match(input,STRING,FOLLOW_STRING_in_whereAtom815); 

                    		whereClause += (f!=null?f.getText():null);
                    	
                    match(input,AND,FOLLOW_AND_in_whereAtom823); 

                    		whereClause += " AND ";
                    	
                    g=(Token)match(input,STRING,FOLLOW_STRING_in_whereAtom833); 

                    		whereClause += (g!=null?g.getText():null);
                    	

                    }
                    break;
                case 3 :
                    // SPQL.g:455:2: LIKE h= STRING
                    {
                    match(input,LIKE,FOLLOW_LIKE_in_whereAtom843); 

                    		whereClause += " LIKE ";
                    	
                    h=(Token)match(input,STRING,FOLLOW_STRING_in_whereAtom852); 

                    		whereClause += (h!=null?h.getText():null);
                    	

                    }
                    break;
                case 4 :
                    // SPQL.g:464:2: ( IN | i= OP ( ALL | ANY ) ) '(' squery ')'
                    {
                    // SPQL.g:464:2: ( IN | i= OP ( ALL | ANY ) )
                    int alt24=2;
                    int LA24_0 = input.LA(1);

                    if ( (LA24_0==IN) ) {
                        alt24=1;
                    }
                    else if ( (LA24_0==OP) ) {
                        alt24=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 24, 0, input);

                        throw nvae;
                    }
                    switch (alt24) {
                        case 1 :
                            // SPQL.g:465:2: IN
                            {
                            match(input,IN,FOLLOW_IN_in_whereAtom867); 

                            		whereClause += " IN ";
                            	

                            }
                            break;
                        case 2 :
                            // SPQL.g:470:3: i= OP ( ALL | ANY )
                            {
                            i=(Token)match(input,OP,FOLLOW_OP_in_whereAtom880); 

                            		whereClause += (i!=null?i.getText():null);
                            	
                            // SPQL.g:475:2: ( ALL | ANY )
                            int alt23=2;
                            int LA23_0 = input.LA(1);

                            if ( (LA23_0==ALL) ) {
                                alt23=1;
                            }
                            else if ( (LA23_0==ANY) ) {
                                alt23=2;
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 23, 0, input);

                                throw nvae;
                            }
                            switch (alt23) {
                                case 1 :
                                    // SPQL.g:476:3: ALL
                                    {
                                    match(input,ALL,FOLLOW_ALL_in_whereAtom893); 

                                    			whereClause += " ALL ";
                                    		

                                    }
                                    break;
                                case 2 :
                                    // SPQL.g:481:3: ANY
                                    {
                                    match(input,ANY,FOLLOW_ANY_in_whereAtom907); 

                                    			whereClause += " ANY ";
                                    		

                                    }
                                    break;

                            }


                            }
                            break;

                    }

                    match(input,38,FOLLOW_38_in_whereAtom922); 
                     System.out.print("("); 
                    pushFollow(FOLLOW_squery_in_whereAtom928);
                    squery();

                    state._fsp--;

                    match(input,39,FOLLOW_39_in_whereAtom932); 
                     System.out.print(")"); 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "whereAtom"


    // $ANTLR start "havingExpression"
    // SPQL.g:494:1: havingExpression : havingAtom ( ( AND | OR ) havingAtom )* ;
    public final void havingExpression() throws RecognitionException {
        try {
            // SPQL.g:495:2: ( havingAtom ( ( AND | OR ) havingAtom )* )
            // SPQL.g:495:4: havingAtom ( ( AND | OR ) havingAtom )*
            {
            pushFollow(FOLLOW_havingAtom_in_havingExpression950);
            havingAtom();

            state._fsp--;

            // SPQL.g:496:3: ( ( AND | OR ) havingAtom )*
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( ((LA27_0>=AND && LA27_0<=OR)) ) {
                    alt27=1;
                }


                switch (alt27) {
            	case 1 :
            	    // SPQL.g:497:4: ( AND | OR ) havingAtom
            	    {
            	    // SPQL.g:497:4: ( AND | OR )
            	    int alt26=2;
            	    int LA26_0 = input.LA(1);

            	    if ( (LA26_0==AND) ) {
            	        alt26=1;
            	    }
            	    else if ( (LA26_0==OR) ) {
            	        alt26=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 26, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt26) {
            	        case 1 :
            	            // SPQL.g:497:5: AND
            	            {
            	            match(input,AND,FOLLOW_AND_in_havingExpression960); 

            	            				System.out.print(" AND ");
            	            			

            	            }
            	            break;
            	        case 2 :
            	            // SPQL.g:501:6: OR
            	            {
            	            match(input,OR,FOLLOW_OR_in_havingExpression973); 

            	            				System.out.print(" OR ");
            	            			

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_havingAtom_in_havingExpression985);
            	    havingAtom();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop27;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "havingExpression"


    // $ANTLR start "havingAtom"
    // SPQL.g:510:1: havingAtom : a= entityAndAttribute ( NOT )? (b= OP (c= STRING | d= INT | e= FLOAT ) | BETWEEN f= STRING AND g= STRING | LIKE h= STRING | ( IN | i= OP ( ALL | ANY ) ) '(' squery ')' ) ;
    public final void havingAtom() throws RecognitionException {
        Token b=null;
        Token c=null;
        Token d=null;
        Token e=null;
        Token f=null;
        Token g=null;
        Token h=null;
        Token i=null;
        SPQLParser.entityAndAttribute_return a = null;


        try {
            // SPQL.g:511:2: (a= entityAndAttribute ( NOT )? (b= OP (c= STRING | d= INT | e= FLOAT ) | BETWEEN f= STRING AND g= STRING | LIKE h= STRING | ( IN | i= OP ( ALL | ANY ) ) '(' squery ')' ) )
            // SPQL.g:511:4: a= entityAndAttribute ( NOT )? (b= OP (c= STRING | d= INT | e= FLOAT ) | BETWEEN f= STRING AND g= STRING | LIKE h= STRING | ( IN | i= OP ( ALL | ANY ) ) '(' squery ')' )
            {
            pushFollow(FOLLOW_entityAndAttribute_in_havingAtom1007);
            a=entityAndAttribute();

            state._fsp--;

             
            			System.out.print((a!=null?input.toString(a.start,a.stop):null));
            		
            // SPQL.g:515:4: ( NOT )?
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==NOT) ) {
                alt28=1;
            }
            switch (alt28) {
                case 1 :
                    // SPQL.g:516:3: NOT
                    {
                    match(input,NOT,FOLLOW_NOT_in_havingAtom1021); 

                    			System.out.print(" NOT ");
                    		

                    }
                    break;

            }

            // SPQL.g:522:2: (b= OP (c= STRING | d= INT | e= FLOAT ) | BETWEEN f= STRING AND g= STRING | LIKE h= STRING | ( IN | i= OP ( ALL | ANY ) ) '(' squery ')' )
            int alt32=4;
            switch ( input.LA(1) ) {
            case OP:
                {
                int LA32_1 = input.LA(2);

                if ( ((LA32_1>=STRING && LA32_1<=FLOAT)) ) {
                    alt32=1;
                }
                else if ( (LA32_1==ALL||LA32_1==ANY) ) {
                    alt32=4;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 1, input);

                    throw nvae;
                }
                }
                break;
            case BETWEEN:
                {
                alt32=2;
                }
                break;
            case LIKE:
                {
                alt32=3;
                }
                break;
            case IN:
                {
                alt32=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;
            }

            switch (alt32) {
                case 1 :
                    // SPQL.g:523:2: b= OP (c= STRING | d= INT | e= FLOAT )
                    {
                    b=(Token)match(input,OP,FOLLOW_OP_in_havingAtom1039); 

                    		System.out.print((b!=null?b.getText():null));
                    	
                    // SPQL.g:527:2: (c= STRING | d= INT | e= FLOAT )
                    int alt29=3;
                    switch ( input.LA(1) ) {
                    case STRING:
                        {
                        alt29=1;
                        }
                        break;
                    case INT:
                        {
                        alt29=2;
                        }
                        break;
                    case FLOAT:
                        {
                        alt29=3;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 29, 0, input);

                        throw nvae;
                    }

                    switch (alt29) {
                        case 1 :
                            // SPQL.g:528:2: c= STRING
                            {
                            c=(Token)match(input,STRING,FOLLOW_STRING_in_havingAtom1051); 

                            		System.out.print((c!=null?c.getText():null));
                            	

                            }
                            break;
                        case 2 :
                            // SPQL.g:533:2: d= INT
                            {
                            d=(Token)match(input,INT,FOLLOW_INT_in_havingAtom1064); 

                            		System.out.print((d!=null?d.getText():null));
                            	

                            }
                            break;
                        case 3 :
                            // SPQL.g:538:2: e= FLOAT
                            {
                            e=(Token)match(input,FLOAT,FOLLOW_FLOAT_in_havingAtom1076); 

                            		System.out.print((e!=null?e.getText():null));
                            	

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // SPQL.g:544:2: BETWEEN f= STRING AND g= STRING
                    {
                    match(input,BETWEEN,FOLLOW_BETWEEN_in_havingAtom1089); 

                    		System.out.print(" BETWEEN ");
                    	
                    f=(Token)match(input,STRING,FOLLOW_STRING_in_havingAtom1099); 

                    		System.out.print((f!=null?f.getText():null));
                    	
                    match(input,AND,FOLLOW_AND_in_havingAtom1107); 

                    		System.out.print(" AND ");
                    	
                    g=(Token)match(input,STRING,FOLLOW_STRING_in_havingAtom1117); 

                    		System.out.print((g!=null?g.getText():null));
                    	

                    }
                    break;
                case 3 :
                    // SPQL.g:561:2: LIKE h= STRING
                    {
                    match(input,LIKE,FOLLOW_LIKE_in_havingAtom1127); 

                    		System.out.print(" BETWEEN ");
                    	
                    h=(Token)match(input,STRING,FOLLOW_STRING_in_havingAtom1136); 

                    		System.out.print((h!=null?h.getText():null));
                    	

                    }
                    break;
                case 4 :
                    // SPQL.g:570:2: ( IN | i= OP ( ALL | ANY ) ) '(' squery ')'
                    {
                    // SPQL.g:570:2: ( IN | i= OP ( ALL | ANY ) )
                    int alt31=2;
                    int LA31_0 = input.LA(1);

                    if ( (LA31_0==IN) ) {
                        alt31=1;
                    }
                    else if ( (LA31_0==OP) ) {
                        alt31=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 31, 0, input);

                        throw nvae;
                    }
                    switch (alt31) {
                        case 1 :
                            // SPQL.g:571:2: IN
                            {
                            match(input,IN,FOLLOW_IN_in_havingAtom1151); 

                            		System.out.print(" IN ");
                            	

                            }
                            break;
                        case 2 :
                            // SPQL.g:576:3: i= OP ( ALL | ANY )
                            {
                            i=(Token)match(input,OP,FOLLOW_OP_in_havingAtom1164); 

                            		System.out.print((i!=null?i.getText():null));
                            	
                            // SPQL.g:581:2: ( ALL | ANY )
                            int alt30=2;
                            int LA30_0 = input.LA(1);

                            if ( (LA30_0==ALL) ) {
                                alt30=1;
                            }
                            else if ( (LA30_0==ANY) ) {
                                alt30=2;
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 30, 0, input);

                                throw nvae;
                            }
                            switch (alt30) {
                                case 1 :
                                    // SPQL.g:582:3: ALL
                                    {
                                    match(input,ALL,FOLLOW_ALL_in_havingAtom1177); 

                                    			System.out.print(" ALL ");
                                    		

                                    }
                                    break;
                                case 2 :
                                    // SPQL.g:587:3: ANY
                                    {
                                    match(input,ANY,FOLLOW_ANY_in_havingAtom1191); 

                                    			System.out.print(" ANY ");
                                    		

                                    }
                                    break;

                            }


                            }
                            break;

                    }

                    match(input,38,FOLLOW_38_in_havingAtom1206); 
                     System.out.print("("); 
                    pushFollow(FOLLOW_squery_in_havingAtom1212);
                    squery();

                    state._fsp--;

                    match(input,39,FOLLOW_39_in_havingAtom1216); 
                     System.out.print(")"); 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "havingAtom"

    public static class entityAttribute_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "entityAttribute"
    // SPQL.g:601:1: entityAttribute : ID ( DOT ID )? ;
    public final SPQLParser.entityAttribute_return entityAttribute() throws RecognitionException {
        SPQLParser.entityAttribute_return retval = new SPQLParser.entityAttribute_return();
        retval.start = input.LT(1);

        try {
            // SPQL.g:601:17: ( ID ( DOT ID )? )
            // SPQL.g:601:19: ID ( DOT ID )?
            {
            match(input,ID,FOLLOW_ID_in_entityAttribute1232); 
            // SPQL.g:601:22: ( DOT ID )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==DOT) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // SPQL.g:601:23: DOT ID
                    {
                    match(input,DOT,FOLLOW_DOT_in_entityAttribute1235); 
                    match(input,ID,FOLLOW_ID_in_entityAttribute1237); 

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "entityAttribute"

    public static class entityAndAttribute_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "entityAndAttribute"
    // SPQL.g:603:1: entityAndAttribute : ID DOT ID ;
    public final SPQLParser.entityAndAttribute_return entityAndAttribute() throws RecognitionException {
        SPQLParser.entityAndAttribute_return retval = new SPQLParser.entityAndAttribute_return();
        retval.start = input.LT(1);

        try {
            // SPQL.g:604:2: ( ID DOT ID )
            // SPQL.g:604:4: ID DOT ID
            {
            match(input,ID,FOLLOW_ID_in_entityAndAttribute1248); 
            match(input,DOT,FOLLOW_DOT_in_entityAndAttribute1250); 
            match(input,ID,FOLLOW_ID_in_entityAndAttribute1252); 

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "entityAndAttribute"


    // $ANTLR start "builtInProcedureAttribute"
    // SPQL.g:607:1: builtInProcedureAttribute : COMPARERUN '(' a= builtInAtom ( COLON b= builtInAtom )* ')' ( DOT (c= ID | '{' d= ID ( COLON e= ID )* '}' ) )? ;
    public final void builtInProcedureAttribute() throws RecognitionException {
        Token c=null;
        Token d=null;
        Token e=null;
        SPQLParser.builtInAtom_return a = null;

        SPQLParser.builtInAtom_return b = null;


        try {
            // SPQL.g:608:2: ( COMPARERUN '(' a= builtInAtom ( COLON b= builtInAtom )* ')' ( DOT (c= ID | '{' d= ID ( COLON e= ID )* '}' ) )? )
            // SPQL.g:608:4: COMPARERUN '(' a= builtInAtom ( COLON b= builtInAtom )* ')' ( DOT (c= ID | '{' d= ID ( COLON e= ID )* '}' ) )?
            {
            match(input,COMPARERUN,FOLLOW_COMPARERUN_in_builtInProcedureAttribute1262); 

            			boolean hasAttribute = false;
            		

            			hasCompareRunCall=true;
            		
            match(input,38,FOLLOW_38_in_builtInProcedureAttribute1272); 
            pushFollow(FOLLOW_builtInAtom_in_builtInProcedureAttribute1279);
            a=builtInAtom();

            state._fsp--;

             
            			compareRunParams.add((a!=null?input.toString(a.start,a.stop):null));
            			if(relations.size() > 0)
            				relations.add("script_run");
            		
            // SPQL.g:621:4: ( COLON b= builtInAtom )*
            loop34:
            do {
                int alt34=2;
                int LA34_0 = input.LA(1);

                if ( (LA34_0==COLON) ) {
                    alt34=1;
                }


                switch (alt34) {
            	case 1 :
            	    // SPQL.g:621:5: COLON b= builtInAtom
            	    {
            	    match(input,COLON,FOLLOW_COLON_in_builtInProcedureAttribute1289); 
            	    pushFollow(FOLLOW_builtInAtom_in_builtInProcedureAttribute1297);
            	    b=builtInAtom();

            	    state._fsp--;

            	     
            	    			compareRunParams.add((b!=null?input.toString(b.start,b.stop):null));
            	    			if(relations.size() > 0)
            	    				relations.add("script_run");
            	    		

            	    }
            	    break;

            	default :
            	    break loop34;
                }
            } while (true);

            match(input,39,FOLLOW_39_in_builtInProcedureAttribute1313); 
            // SPQL.g:628:11: ( DOT (c= ID | '{' d= ID ( COLON e= ID )* '}' ) )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==DOT) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // SPQL.g:629:4: DOT (c= ID | '{' d= ID ( COLON e= ID )* '}' )
                    {
                    match(input,DOT,FOLLOW_DOT_in_builtInProcedureAttribute1321); 

                    	 		hasAttribute = true;
                    	 	
                    // SPQL.g:633:5: (c= ID | '{' d= ID ( COLON e= ID )* '}' )
                    int alt36=2;
                    int LA36_0 = input.LA(1);

                    if ( (LA36_0==ID) ) {
                        alt36=1;
                    }
                    else if ( (LA36_0==41) ) {
                        alt36=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 36, 0, input);

                        throw nvae;
                    }
                    switch (alt36) {
                        case 1 :
                            // SPQL.g:634:4: c= ID
                            {
                            c=(Token)match(input,ID,FOLLOW_ID_in_builtInProcedureAttribute1339); 

                            	 		selectClause += "compare_run." + (c!=null?c.getText():null);
                            	 	

                            }
                            break;
                        case 2 :
                            // SPQL.g:638:8: '{' d= ID ( COLON e= ID )* '}'
                            {
                            match(input,41,FOLLOW_41_in_builtInProcedureAttribute1353); 
                            d=(Token)match(input,ID,FOLLOW_ID_in_builtInProcedureAttribute1361); 

                            	 		selectClause += "compare_run." + (d!=null?d.getText():null);
                            	 	
                            // SPQL.g:643:4: ( COLON e= ID )*
                            loop35:
                            do {
                                int alt35=2;
                                int LA35_0 = input.LA(1);

                                if ( (LA35_0==COLON) ) {
                                    alt35=1;
                                }


                                switch (alt35) {
                            	case 1 :
                            	    // SPQL.g:643:5: COLON e= ID
                            	    {
                            	    match(input,COLON,FOLLOW_COLON_in_builtInProcedureAttribute1372); 
                            	    e=(Token)match(input,ID,FOLLOW_ID_in_builtInProcedureAttribute1380); 
                            	     
                            	    	 		selectClause += ", compare_run." + (e!=null?e.getText():null); 
                            	    	 	

                            	    }
                            	    break;

                            	default :
                            	    break loop35;
                                }
                            } while (true);

                            match(input,42,FOLLOW_42_in_builtInProcedureAttribute1394); 

                            }
                            break;

                    }


                    }
                    break;

            }


            	 		if(!hasAttribute)
            	 			selectClause += "compare_run.*";
            	 	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "builtInProcedureAttribute"

    public static class builtInAtom_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "builtInAtom"
    // SPQL.g:655:1: builtInAtom : ( 'parameter' | 'key_numeric' | 'key_text' ) OP STRING ;
    public final SPQLParser.builtInAtom_return builtInAtom() throws RecognitionException {
        SPQLParser.builtInAtom_return retval = new SPQLParser.builtInAtom_return();
        retval.start = input.LT(1);

        try {
            // SPQL.g:656:2: ( ( 'parameter' | 'key_numeric' | 'key_text' ) OP STRING )
            // SPQL.g:656:4: ( 'parameter' | 'key_numeric' | 'key_text' ) OP STRING
            {
            if ( (input.LA(1)>=43 && input.LA(1)<=45) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

            match(input,OP,FOLLOW_OP_in_builtInAtom1425); 
            match(input,STRING,FOLLOW_STRING_in_builtInAtom1427); 

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "builtInAtom"

    // Delegated rules


 

    public static final BitSet FOLLOW_squery_in_query22 = new BitSet(new long[]{0x0000000000000170L});
    public static final BitSet FOLLOW_UNION_in_query34 = new BitSet(new long[]{0x0000004000000280L});
    public static final BitSet FOLLOW_INTERSECT_in_query45 = new BitSet(new long[]{0x0000004000000280L});
    public static final BitSet FOLLOW_EXCEPT_in_query56 = new BitSet(new long[]{0x0000004000000280L});
    public static final BitSet FOLLOW_ALL_in_query70 = new BitSet(new long[]{0x0000004000000280L});
    public static final BitSet FOLLOW_squery_in_query82 = new BitSet(new long[]{0x0000000000000170L});
    public static final BitSet FOLLOW_SEMICOLON_in_query95 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SELECT_in_squery109 = new BitSet(new long[]{0x0000000002860400L});
    public static final BitSet FOLLOW_DISTINCT_in_squery122 = new BitSet(new long[]{0x0000000002860400L});
    public static final BitSet FOLLOW_selectExpression_in_squery136 = new BitSet(new long[]{0x0000000000011802L});
    public static final BitSet FOLLOW_WHERE_in_squery146 = new BitSet(new long[]{0x0000000002800000L});
    public static final BitSet FOLLOW_whereExpression_in_squery148 = new BitSet(new long[]{0x0000000000011002L});
    public static final BitSet FOLLOW_GROUP_in_squery171 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_BY_in_squery173 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_entityAndAttribute_in_squery186 = new BitSet(new long[]{0x000000000001C002L});
    public static final BitSet FOLLOW_COLON_in_squery201 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_entityAndAttribute_in_squery208 = new BitSet(new long[]{0x000000000001C002L});
    public static final BitSet FOLLOW_HAVING_in_squery229 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_havingExpression_in_squery236 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_ORDER_in_squery258 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_BY_in_squery260 = new BitSet(new long[]{0x0000000002060000L});
    public static final BitSet FOLLOW_entityAndAttribute_in_squery278 = new BitSet(new long[]{0x0000000000184002L});
    public static final BitSet FOLLOW_COUNT_in_squery293 = new BitSet(new long[]{0x0000000000184002L});
    public static final BitSet FOLLOW_AGGRFUN_in_squery307 = new BitSet(new long[]{0x0000000000184002L});
    public static final BitSet FOLLOW_COLON_in_squery324 = new BitSet(new long[]{0x0000000002060000L});
    public static final BitSet FOLLOW_entityAndAttribute_in_squery338 = new BitSet(new long[]{0x0000000000184002L});
    public static final BitSet FOLLOW_COUNT_in_squery356 = new BitSet(new long[]{0x0000000000184002L});
    public static final BitSet FOLLOW_AGGRFUN_in_squery370 = new BitSet(new long[]{0x0000000000184002L});
    public static final BitSet FOLLOW_DESC_in_squery395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASC_in_squery410 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_38_in_squery433 = new BitSet(new long[]{0x0000004000000280L});
    public static final BitSet FOLLOW_squery_in_squery439 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_39_in_squery443 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_entityAttribute_in_selectAtom465 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AGGRFUN_in_selectAtom480 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_38_in_selectAtom489 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_entityAndAttribute_in_selectAtom498 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_39_in_selectAtom508 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COUNT_in_selectAtom521 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_38_in_selectAtom530 = new BitSet(new long[]{0x0000010002000000L});
    public static final BitSet FOLLOW_entityAttribute_in_selectAtom543 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_40_in_selectAtom557 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_39_in_selectAtom567 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_builtInProcedureAttribute_in_selectAtom578 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_selectAtom_in_selectExpression594 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_COLON_in_selectExpression605 = new BitSet(new long[]{0x0000000002860400L});
    public static final BitSet FOLLOW_selectAtom_in_selectExpression616 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_whereAtom_in_whereExpression637 = new BitSet(new long[]{0x0000000000600002L});
    public static final BitSet FOLLOW_AND_in_whereExpression647 = new BitSet(new long[]{0x0000000002800000L});
    public static final BitSet FOLLOW_OR_in_whereExpression660 = new BitSet(new long[]{0x0000000002800000L});
    public static final BitSet FOLLOW_whereAtom_in_whereExpression672 = new BitSet(new long[]{0x0000000000600002L});
    public static final BitSet FOLLOW_entityAndAttribute_in_whereAtom693 = new BitSet(new long[]{0x000000038C000000L});
    public static final BitSet FOLLOW_COMPARERUN_in_whereAtom708 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_DOT_in_whereAtom714 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_ID_in_whereAtom721 = new BitSet(new long[]{0x000000038C000000L});
    public static final BitSet FOLLOW_NOT_in_whereAtom737 = new BitSet(new long[]{0x0000000388000000L});
    public static final BitSet FOLLOW_OP_in_whereAtom755 = new BitSet(new long[]{0x0000000070000000L});
    public static final BitSet FOLLOW_STRING_in_whereAtom767 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_whereAtom780 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_whereAtom792 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BETWEEN_in_whereAtom805 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_STRING_in_whereAtom815 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_AND_in_whereAtom823 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_STRING_in_whereAtom833 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LIKE_in_whereAtom843 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_STRING_in_whereAtom852 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IN_in_whereAtom867 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_OP_in_whereAtom880 = new BitSet(new long[]{0x0000000400000080L});
    public static final BitSet FOLLOW_ALL_in_whereAtom893 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ANY_in_whereAtom907 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_38_in_whereAtom922 = new BitSet(new long[]{0x0000004000000280L});
    public static final BitSet FOLLOW_squery_in_whereAtom928 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_39_in_whereAtom932 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_havingAtom_in_havingExpression950 = new BitSet(new long[]{0x0000000000600002L});
    public static final BitSet FOLLOW_AND_in_havingExpression960 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_OR_in_havingExpression973 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_havingAtom_in_havingExpression985 = new BitSet(new long[]{0x0000000000600002L});
    public static final BitSet FOLLOW_entityAndAttribute_in_havingAtom1007 = new BitSet(new long[]{0x000000038C000000L});
    public static final BitSet FOLLOW_NOT_in_havingAtom1021 = new BitSet(new long[]{0x0000000388000000L});
    public static final BitSet FOLLOW_OP_in_havingAtom1039 = new BitSet(new long[]{0x0000000070000000L});
    public static final BitSet FOLLOW_STRING_in_havingAtom1051 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_havingAtom1064 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_havingAtom1076 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BETWEEN_in_havingAtom1089 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_STRING_in_havingAtom1099 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_AND_in_havingAtom1107 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_STRING_in_havingAtom1117 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LIKE_in_havingAtom1127 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_STRING_in_havingAtom1136 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IN_in_havingAtom1151 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_OP_in_havingAtom1164 = new BitSet(new long[]{0x0000000400000080L});
    public static final BitSet FOLLOW_ALL_in_havingAtom1177 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_ANY_in_havingAtom1191 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_38_in_havingAtom1206 = new BitSet(new long[]{0x0000004000000280L});
    public static final BitSet FOLLOW_squery_in_havingAtom1212 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_39_in_havingAtom1216 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_entityAttribute1232 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_DOT_in_entityAttribute1235 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_ID_in_entityAttribute1237 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_entityAndAttribute1248 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_DOT_in_entityAndAttribute1250 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_ID_in_entityAndAttribute1252 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COMPARERUN_in_builtInProcedureAttribute1262 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_38_in_builtInProcedureAttribute1272 = new BitSet(new long[]{0x0000380000000000L});
    public static final BitSet FOLLOW_builtInAtom_in_builtInProcedureAttribute1279 = new BitSet(new long[]{0x0000008000004000L});
    public static final BitSet FOLLOW_COLON_in_builtInProcedureAttribute1289 = new BitSet(new long[]{0x0000380000000000L});
    public static final BitSet FOLLOW_builtInAtom_in_builtInProcedureAttribute1297 = new BitSet(new long[]{0x0000008000004000L});
    public static final BitSet FOLLOW_39_in_builtInProcedureAttribute1313 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_DOT_in_builtInProcedureAttribute1321 = new BitSet(new long[]{0x0000020002000000L});
    public static final BitSet FOLLOW_ID_in_builtInProcedureAttribute1339 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_41_in_builtInProcedureAttribute1353 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_ID_in_builtInProcedureAttribute1361 = new BitSet(new long[]{0x0000040000004000L});
    public static final BitSet FOLLOW_COLON_in_builtInProcedureAttribute1372 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_ID_in_builtInProcedureAttribute1380 = new BitSet(new long[]{0x0000040000004000L});
    public static final BitSet FOLLOW_42_in_builtInProcedureAttribute1394 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_builtInAtom1413 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_OP_in_builtInAtom1425 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_STRING_in_builtInAtom1427 = new BitSet(new long[]{0x0000000000000002L});

}