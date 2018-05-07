grammar tagql;

@members {
	String s;
}

query	:	tag_query | locate_query;

tag_query	:	TAG  '(' ID_STRING { s = $ID_STRING.text; } ',' tag_nvp_expr 
			(',' tag_nvp_expr)*')';

locate_query 
	:	LOCATE
		{
			System.out.print("SELECT dataset_id FROM annotation WHERE ");
		}
		'(' locate_expr ')';



numeric :	INT | FLOAT;	

tag_nvp_expr	:	ID_STRING '=' (
			STRING
			{
				System.out.print("INSERT INTO annot_dataset_text (dataset_id, name, value) VALUES ("
						 + "'" + s + "'" + "," + "'" + $ID_STRING.text + "'" + "," + $STRING.text + "); "); 
			}
			| 
			numeric
			{
				System.out.print("INSERT INTO annot_dataset_num (dataset_id, name, value) VALUES ("
						 + "'" + s + "'" + "," + "'" + $ID_STRING.text + "'" + "," + $STRING.text + "); "); 
			}
			);

locate_nvp_expr	:	ID_STRING
			{	
				System.out.print("(name=" + $ID_STRING.text + " ");
			}
			'=' 
			( 
			numeric
			{
				System.out.print("AND numeric_value=" + $numeric.text + ")");
			} 
			| 
			STRING
			{
				System.out.print("AND text_value=" + $STRING.text + ")");
			}
			);

locate_expr 
	:	locate_nvp_expr |
		locate_nvp_expr (AND { System.out.print(" AND "); } | OR { System.out.print(" OR "); } ) locate_expr ;

	
OP	:	'=' | '>' | '>=' | '<' | '<=';

TAG	:	'tag';

LOCATE	:	'locate';

AND	:	'and';

OR	:	'or';

SEMICOLON	:	';';

ID_STRING  :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-')*;

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
