// $ANTLR 3.3 Nov 30, 2010 12:45:30 tagql.g 2014-01-09 15:49:49

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class tagqlParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "TAG", "ID_STRING", "LOCATE", "INT", "FLOAT", "STRING", "AND", "OR", "OP", "SEMICOLON", "NEWLINE", "WS", "'('", "','", "')'", "'='"
    };
    public static final int EOF=-1;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__19=19;
    public static final int TAG=4;
    public static final int ID_STRING=5;
    public static final int LOCATE=6;
    public static final int INT=7;
    public static final int FLOAT=8;
    public static final int STRING=9;
    public static final int AND=10;
    public static final int OR=11;
    public static final int OP=12;
    public static final int SEMICOLON=13;
    public static final int NEWLINE=14;
    public static final int WS=15;

    // delegates
    // delegators


        public tagqlParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public tagqlParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return tagqlParser.tokenNames; }
    public String getGrammarFileName() { return "tagql.g"; }


    	String s;



    // $ANTLR start "query"
    // tagql.g:7:1: query : ( tag_query | locate_query );
    public final void query() throws RecognitionException {
        try {
            // tagql.g:7:7: ( tag_query | locate_query )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==TAG) ) {
                alt1=1;
            }
            else if ( (LA1_0==LOCATE) ) {
                alt1=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // tagql.g:7:9: tag_query
                    {
                    pushFollow(FOLLOW_tag_query_in_query16);
                    tag_query();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // tagql.g:7:21: locate_query
                    {
                    pushFollow(FOLLOW_locate_query_in_query20);
                    locate_query();

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
    // $ANTLR end "query"


    // $ANTLR start "tag_query"
    // tagql.g:9:1: tag_query : TAG '(' ID_STRING ',' tag_nvp_expr ( ',' tag_nvp_expr )* ')' ;
    public final void tag_query() throws RecognitionException {
        Token ID_STRING1=null;

        try {
            // tagql.g:9:11: ( TAG '(' ID_STRING ',' tag_nvp_expr ( ',' tag_nvp_expr )* ')' )
            // tagql.g:9:13: TAG '(' ID_STRING ',' tag_nvp_expr ( ',' tag_nvp_expr )* ')'
            {
            match(input,TAG,FOLLOW_TAG_in_tag_query28); 
            match(input,16,FOLLOW_16_in_tag_query31); 
            ID_STRING1=(Token)match(input,ID_STRING,FOLLOW_ID_STRING_in_tag_query33); 
             s = (ID_STRING1!=null?ID_STRING1.getText():null); 
            match(input,17,FOLLOW_17_in_tag_query37); 
            pushFollow(FOLLOW_tag_nvp_expr_in_tag_query39);
            tag_nvp_expr();

            state._fsp--;

            // tagql.g:10:4: ( ',' tag_nvp_expr )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==17) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // tagql.g:10:5: ',' tag_nvp_expr
            	    {
            	    match(input,17,FOLLOW_17_in_tag_query46); 
            	    pushFollow(FOLLOW_tag_nvp_expr_in_tag_query48);
            	    tag_nvp_expr();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            match(input,18,FOLLOW_18_in_tag_query51); 

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
    // $ANTLR end "tag_query"


    // $ANTLR start "locate_query"
    // tagql.g:12:1: locate_query : LOCATE '(' locate_expr ')' ;
    public final void locate_query() throws RecognitionException {
        try {
            // tagql.g:13:2: ( LOCATE '(' locate_expr ')' )
            // tagql.g:13:4: LOCATE '(' locate_expr ')'
            {
            match(input,LOCATE,FOLLOW_LOCATE_in_locate_query61); 

            			System.out.print("SELECT dataset_id FROM annotation WHERE ");
            		
            match(input,16,FOLLOW_16_in_locate_query69); 
            pushFollow(FOLLOW_locate_expr_in_locate_query71);
            locate_expr();

            state._fsp--;

            match(input,18,FOLLOW_18_in_locate_query73); 

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
    // $ANTLR end "locate_query"

    public static class numeric_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "numeric"
    // tagql.g:21:1: numeric : ( INT | FLOAT );
    public final tagqlParser.numeric_return numeric() throws RecognitionException {
        tagqlParser.numeric_return retval = new tagqlParser.numeric_return();
        retval.start = input.LT(1);

        try {
            // tagql.g:21:9: ( INT | FLOAT )
            // tagql.g:
            {
            if ( (input.LA(1)>=INT && input.LA(1)<=FLOAT) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
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
    // $ANTLR end "numeric"


    // $ANTLR start "tag_nvp_expr"
    // tagql.g:23:1: tag_nvp_expr : ID_STRING '=' ( STRING | numeric ) ;
    public final void tag_nvp_expr() throws RecognitionException {
        Token ID_STRING2=null;
        Token STRING3=null;

        try {
            // tagql.g:23:14: ( ID_STRING '=' ( STRING | numeric ) )
            // tagql.g:23:16: ID_STRING '=' ( STRING | numeric )
            {
            ID_STRING2=(Token)match(input,ID_STRING,FOLLOW_ID_STRING_in_tag_nvp_expr96); 
            match(input,19,FOLLOW_19_in_tag_nvp_expr98); 
            // tagql.g:23:30: ( STRING | numeric )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==STRING) ) {
                alt3=1;
            }
            else if ( ((LA3_0>=INT && LA3_0<=FLOAT)) ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // tagql.g:24:4: STRING
                    {
                    STRING3=(Token)match(input,STRING,FOLLOW_STRING_in_tag_nvp_expr105); 

                    				System.out.print("INSERT INTO annot_dataset_text (dataset_id, name, value) VALUES ("
                    						 + "'" + s + "'" + "," + "'" + (ID_STRING2!=null?ID_STRING2.getText():null) + "'" + "," + (STRING3!=null?STRING3.getText():null) + "); "); 
                    			

                    }
                    break;
                case 2 :
                    // tagql.g:30:4: numeric
                    {
                    pushFollow(FOLLOW_numeric_in_tag_nvp_expr121);
                    numeric();

                    state._fsp--;


                    				System.out.print("INSERT INTO annot_dataset_num (dataset_id, name, value) VALUES ("
                    						 + "'" + s + "'" + "," + "'" + (ID_STRING2!=null?ID_STRING2.getText():null) + "'" + "," + (STRING3!=null?STRING3.getText():null) + "); "); 
                    			

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
    // $ANTLR end "tag_nvp_expr"


    // $ANTLR start "locate_nvp_expr"
    // tagql.g:37:1: locate_nvp_expr : ID_STRING '=' ( numeric | STRING ) ;
    public final void locate_nvp_expr() throws RecognitionException {
        Token ID_STRING4=null;
        Token STRING6=null;
        tagqlParser.numeric_return numeric5 = null;


        try {
            // tagql.g:37:17: ( ID_STRING '=' ( numeric | STRING ) )
            // tagql.g:37:19: ID_STRING '=' ( numeric | STRING )
            {
            ID_STRING4=(Token)match(input,ID_STRING,FOLLOW_ID_STRING_in_locate_nvp_expr139); 
            	
            				System.out.print("(name=" + (ID_STRING4!=null?ID_STRING4.getText():null) + " ");
            			
            match(input,19,FOLLOW_19_in_locate_nvp_expr149); 
            // tagql.g:42:4: ( numeric | STRING )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( ((LA4_0>=INT && LA4_0<=FLOAT)) ) {
                alt4=1;
            }
            else if ( (LA4_0==STRING) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // tagql.g:43:4: numeric
                    {
                    pushFollow(FOLLOW_numeric_in_locate_nvp_expr161);
                    numeric5=numeric();

                    state._fsp--;


                    				System.out.print("AND numeric_value=" + (numeric5!=null?input.toString(numeric5.start,numeric5.stop):null) + ")");
                    			

                    }
                    break;
                case 2 :
                    // tagql.g:48:4: STRING
                    {
                    STRING6=(Token)match(input,STRING,FOLLOW_STRING_in_locate_nvp_expr178); 

                    				System.out.print("AND text_value=" + (STRING6!=null?STRING6.getText():null) + ")");
                    			

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
    // $ANTLR end "locate_nvp_expr"


    // $ANTLR start "locate_expr"
    // tagql.g:54:1: locate_expr : ( locate_nvp_expr | locate_nvp_expr ( AND | OR ) locate_expr );
    public final void locate_expr() throws RecognitionException {
        try {
            // tagql.g:55:2: ( locate_nvp_expr | locate_nvp_expr ( AND | OR ) locate_expr )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==ID_STRING) ) {
                int LA6_1 = input.LA(2);

                if ( (LA6_1==19) ) {
                    int LA6_2 = input.LA(3);

                    if ( ((LA6_2>=INT && LA6_2<=FLOAT)) ) {
                        int LA6_3 = input.LA(4);

                        if ( (LA6_3==18) ) {
                            alt6=1;
                        }
                        else if ( ((LA6_3>=AND && LA6_3<=OR)) ) {
                            alt6=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 6, 3, input);

                            throw nvae;
                        }
                    }
                    else if ( (LA6_2==STRING) ) {
                        int LA6_4 = input.LA(4);

                        if ( (LA6_4==18) ) {
                            alt6=1;
                        }
                        else if ( ((LA6_4>=AND && LA6_4<=OR)) ) {
                            alt6=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 6, 4, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 6, 2, input);

                        throw nvae;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // tagql.g:55:4: locate_nvp_expr
                    {
                    pushFollow(FOLLOW_locate_nvp_expr_in_locate_expr198);
                    locate_nvp_expr();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // tagql.g:56:3: locate_nvp_expr ( AND | OR ) locate_expr
                    {
                    pushFollow(FOLLOW_locate_nvp_expr_in_locate_expr204);
                    locate_nvp_expr();

                    state._fsp--;

                    // tagql.g:56:19: ( AND | OR )
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0==AND) ) {
                        alt5=1;
                    }
                    else if ( (LA5_0==OR) ) {
                        alt5=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 5, 0, input);

                        throw nvae;
                    }
                    switch (alt5) {
                        case 1 :
                            // tagql.g:56:20: AND
                            {
                            match(input,AND,FOLLOW_AND_in_locate_expr207); 
                             System.out.print(" AND "); 

                            }
                            break;
                        case 2 :
                            // tagql.g:56:57: OR
                            {
                            match(input,OR,FOLLOW_OR_in_locate_expr213); 
                             System.out.print(" OR "); 

                            }
                            break;

                    }

                    pushFollow(FOLLOW_locate_expr_in_locate_expr219);
                    locate_expr();

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
    // $ANTLR end "locate_expr"

    // Delegated rules


 

    public static final BitSet FOLLOW_tag_query_in_query16 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_locate_query_in_query20 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TAG_in_tag_query28 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_tag_query31 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_ID_STRING_in_tag_query33 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_tag_query37 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_tag_nvp_expr_in_tag_query39 = new BitSet(new long[]{0x0000000000060000L});
    public static final BitSet FOLLOW_17_in_tag_query46 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_tag_nvp_expr_in_tag_query48 = new BitSet(new long[]{0x0000000000060000L});
    public static final BitSet FOLLOW_18_in_tag_query51 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LOCATE_in_locate_query61 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_locate_query69 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_locate_expr_in_locate_query71 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_locate_query73 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_numeric0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_STRING_in_tag_nvp_expr96 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_tag_nvp_expr98 = new BitSet(new long[]{0x0000000000000380L});
    public static final BitSet FOLLOW_STRING_in_tag_nvp_expr105 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_numeric_in_tag_nvp_expr121 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_STRING_in_locate_nvp_expr139 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_locate_nvp_expr149 = new BitSet(new long[]{0x0000000000000380L});
    public static final BitSet FOLLOW_numeric_in_locate_nvp_expr161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_locate_nvp_expr178 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_locate_nvp_expr_in_locate_expr198 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_locate_nvp_expr_in_locate_expr204 = new BitSet(new long[]{0x0000000000000C00L});
    public static final BitSet FOLLOW_AND_in_locate_expr207 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_OR_in_locate_expr213 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_locate_expr_in_locate_expr219 = new BitSet(new long[]{0x0000000000000002L});

}