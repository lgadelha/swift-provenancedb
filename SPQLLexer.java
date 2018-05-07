// $ANTLR 3.2 debian-7 SPQL.g 2013-11-04 17:00:24

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class SPQLLexer extends Lexer {
    public static final int WHERE=11;
    public static final int ORDER=16;
    public static final int COUNT=17;
    public static final int FLOAT=30;
    public static final int NOT=26;
    public static final int COMPARERUN=23;
    public static final int ID=25;
    public static final int AND=21;
    public static final int EXCEPT=6;
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

    public SPQLLexer() {;} 
    public SPQLLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public SPQLLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "SPQL.g"; }

    // $ANTLR start "T__38"
    public final void mT__38() throws RecognitionException {
        try {
            int _type = T__38;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:3:7: ( '(' )
            // SPQL.g:3:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__38"

    // $ANTLR start "T__39"
    public final void mT__39() throws RecognitionException {
        try {
            int _type = T__39;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:4:7: ( ')' )
            // SPQL.g:4:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__39"

    // $ANTLR start "T__40"
    public final void mT__40() throws RecognitionException {
        try {
            int _type = T__40;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:5:7: ( '*' )
            // SPQL.g:5:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__40"

    // $ANTLR start "T__41"
    public final void mT__41() throws RecognitionException {
        try {
            int _type = T__41;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:6:7: ( '{' )
            // SPQL.g:6:9: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__41"

    // $ANTLR start "T__42"
    public final void mT__42() throws RecognitionException {
        try {
            int _type = T__42;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:7:7: ( '}' )
            // SPQL.g:7:9: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__42"

    // $ANTLR start "T__43"
    public final void mT__43() throws RecognitionException {
        try {
            int _type = T__43;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:8:7: ( 'parameter' )
            // SPQL.g:8:9: 'parameter'
            {
            match("parameter"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__43"

    // $ANTLR start "T__44"
    public final void mT__44() throws RecognitionException {
        try {
            int _type = T__44;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:9:7: ( 'key_numeric' )
            // SPQL.g:9:9: 'key_numeric'
            {
            match("key_numeric"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__44"

    // $ANTLR start "T__45"
    public final void mT__45() throws RecognitionException {
        try {
            int _type = T__45;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:10:7: ( 'key_text' )
            // SPQL.g:10:9: 'key_text'
            {
            match("key_text"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__45"

    // $ANTLR start "OP"
    public final void mOP() throws RecognitionException {
        try {
            int _type = OP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:658:4: ( '=' | '>' | '>=' | '<' | '<=' )
            int alt1=5;
            switch ( input.LA(1) ) {
            case '=':
                {
                alt1=1;
                }
                break;
            case '>':
                {
                int LA1_2 = input.LA(2);

                if ( (LA1_2=='=') ) {
                    alt1=3;
                }
                else {
                    alt1=2;}
                }
                break;
            case '<':
                {
                int LA1_3 = input.LA(2);

                if ( (LA1_3=='=') ) {
                    alt1=5;
                }
                else {
                    alt1=4;}
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }

            switch (alt1) {
                case 1 :
                    // SPQL.g:658:6: '='
                    {
                    match('='); 

                    }
                    break;
                case 2 :
                    // SPQL.g:658:12: '>'
                    {
                    match('>'); 

                    }
                    break;
                case 3 :
                    // SPQL.g:658:18: '>='
                    {
                    match(">="); 


                    }
                    break;
                case 4 :
                    // SPQL.g:658:25: '<'
                    {
                    match('<'); 

                    }
                    break;
                case 5 :
                    // SPQL.g:658:31: '<='
                    {
                    match("<="); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OP"

    // $ANTLR start "GROUP"
    public final void mGROUP() throws RecognitionException {
        try {
            int _type = GROUP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:660:7: ( 'group' )
            // SPQL.g:660:9: 'group'
            {
            match("group"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GROUP"

    // $ANTLR start "ORDER"
    public final void mORDER() throws RecognitionException {
        try {
            int _type = ORDER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:662:7: ( 'order' )
            // SPQL.g:662:9: 'order'
            {
            match("order"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ORDER"

    // $ANTLR start "COMPARERUN"
    public final void mCOMPARERUN() throws RecognitionException {
        try {
            int _type = COMPARERUN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:665:2: ( 'compare_run' )
            // SPQL.g:665:4: 'compare_run'
            {
            match("compare_run"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMPARERUN"

    // $ANTLR start "ANCESTOR"
    public final void mANCESTOR() throws RecognitionException {
        try {
            int _type = ANCESTOR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:667:9: ( 'ancestor' )
            // SPQL.g:667:11: 'ancestor'
            {
            match("ancestor"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ANCESTOR"

    // $ANTLR start "BY"
    public final void mBY() throws RecognitionException {
        try {
            int _type = BY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:669:4: ( 'by' )
            // SPQL.g:669:6: 'by'
            {
            match("by"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BY"

    // $ANTLR start "AGGRFUN"
    public final void mAGGRFUN() throws RecognitionException {
        try {
            int _type = AGGRFUN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:671:9: ( 'avg' | 'max' | 'min' | 'sum' )
            int alt2=4;
            switch ( input.LA(1) ) {
            case 'a':
                {
                alt2=1;
                }
                break;
            case 'm':
                {
                int LA2_2 = input.LA(2);

                if ( (LA2_2=='a') ) {
                    alt2=2;
                }
                else if ( (LA2_2=='i') ) {
                    alt2=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 2, input);

                    throw nvae;
                }
                }
                break;
            case 's':
                {
                alt2=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // SPQL.g:671:11: 'avg'
                    {
                    match("avg"); 


                    }
                    break;
                case 2 :
                    // SPQL.g:671:19: 'max'
                    {
                    match("max"); 


                    }
                    break;
                case 3 :
                    // SPQL.g:671:27: 'min'
                    {
                    match("min"); 


                    }
                    break;
                case 4 :
                    // SPQL.g:671:35: 'sum'
                    {
                    match("sum"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AGGRFUN"

    // $ANTLR start "COUNT"
    public final void mCOUNT() throws RecognitionException {
        try {
            int _type = COUNT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:673:7: ( 'count' )
            // SPQL.g:673:9: 'count'
            {
            match("count"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COUNT"

    // $ANTLR start "SELECT"
    public final void mSELECT() throws RecognitionException {
        try {
            int _type = SELECT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:675:9: ( 'select' )
            // SPQL.g:675:11: 'select'
            {
            match("select"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SELECT"

    // $ANTLR start "DESC"
    public final void mDESC() throws RecognitionException {
        try {
            int _type = DESC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:677:6: ( 'desc' )
            // SPQL.g:677:8: 'desc'
            {
            match("desc"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DESC"

    // $ANTLR start "ASC"
    public final void mASC() throws RecognitionException {
        try {
            int _type = ASC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:679:5: ( 'asc' )
            // SPQL.g:679:7: 'asc'
            {
            match("asc"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ASC"

    // $ANTLR start "DISTINCT"
    public final void mDISTINCT() throws RecognitionException {
        try {
            int _type = DISTINCT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:683:2: ( 'distinct' )
            // SPQL.g:683:4: 'distinct'
            {
            match("distinct"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DISTINCT"

    // $ANTLR start "WHERE"
    public final void mWHERE() throws RecognitionException {
        try {
            int _type = WHERE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:685:7: ( 'where' )
            // SPQL.g:685:9: 'where'
            {
            match("where"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WHERE"

    // $ANTLR start "AND"
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:687:5: ( 'and' )
            // SPQL.g:687:7: 'and'
            {
            match("and"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AND"

    // $ANTLR start "OR"
    public final void mOR() throws RecognitionException {
        try {
            int _type = OR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:689:4: ( 'or' )
            // SPQL.g:689:6: 'or'
            {
            match("or"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OR"

    // $ANTLR start "NOT"
    public final void mNOT() throws RecognitionException {
        try {
            int _type = NOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:691:5: ( 'not' )
            // SPQL.g:691:7: 'not'
            {
            match("not"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NOT"

    // $ANTLR start "IN"
    public final void mIN() throws RecognitionException {
        try {
            int _type = IN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:693:4: ( 'in' )
            // SPQL.g:693:6: 'in'
            {
            match("in"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IN"

    // $ANTLR start "ANY"
    public final void mANY() throws RecognitionException {
        try {
            int _type = ANY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:695:5: ( 'any' )
            // SPQL.g:695:7: 'any'
            {
            match("any"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ANY"

    // $ANTLR start "UNION"
    public final void mUNION() throws RecognitionException {
        try {
            int _type = UNION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:697:7: ( 'union' )
            // SPQL.g:697:9: 'union'
            {
            match("union"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "UNION"

    // $ANTLR start "INTERSECT"
    public final void mINTERSECT() throws RecognitionException {
        try {
            int _type = INTERSECT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:700:2: ( 'intersect' )
            // SPQL.g:700:4: 'intersect'
            {
            match("intersect"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INTERSECT"

    // $ANTLR start "EXCEPT"
    public final void mEXCEPT() throws RecognitionException {
        try {
            int _type = EXCEPT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:702:8: ( 'except' )
            // SPQL.g:702:10: 'except'
            {
            match("except"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EXCEPT"

    // $ANTLR start "ALL"
    public final void mALL() throws RecognitionException {
        try {
            int _type = ALL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:704:5: ( 'all' )
            // SPQL.g:704:7: 'all'
            {
            match("all"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ALL"

    // $ANTLR start "DOT"
    public final void mDOT() throws RecognitionException {
        try {
            int _type = DOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:706:5: ( '.' )
            // SPQL.g:706:7: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOT"

    // $ANTLR start "COLON"
    public final void mCOLON() throws RecognitionException {
        try {
            int _type = COLON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:708:7: ( ',' )
            // SPQL.g:708:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COLON"

    // $ANTLR start "BETWEEN"
    public final void mBETWEEN() throws RecognitionException {
        try {
            int _type = BETWEEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:710:9: ( 'between' )
            // SPQL.g:710:11: 'between'
            {
            match("between"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BETWEEN"

    // $ANTLR start "HAVING"
    public final void mHAVING() throws RecognitionException {
        try {
            int _type = HAVING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:712:8: ( 'having' )
            // SPQL.g:712:10: 'having'
            {
            match("having"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "HAVING"

    // $ANTLR start "LIKE"
    public final void mLIKE() throws RecognitionException {
        try {
            int _type = LIKE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:714:7: ( 'like' )
            // SPQL.g:714:9: 'like'
            {
            match("like"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LIKE"

    // $ANTLR start "SEMICOLON"
    public final void mSEMICOLON() throws RecognitionException {
        try {
            int _type = SEMICOLON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:716:11: ( ';' )
            // SPQL.g:716:13: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SEMICOLON"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:718:5: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )* )
            // SPQL.g:718:7: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // SPQL.g:718:31: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0=='-'||(LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // SPQL.g:
            	    {
            	    if ( input.LA(1)=='-'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:721:5: ( ( '0' .. '9' )+ )
            // SPQL.g:721:7: ( '0' .. '9' )+
            {
            // SPQL.g:721:7: ( '0' .. '9' )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='0' && LA4_0<='9')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // SPQL.g:721:7: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "FLOAT"
    public final void mFLOAT() throws RecognitionException {
        try {
            int _type = FLOAT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:725:5: ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* | '.' ( '0' .. '9' )+ | ( '0' .. '9' )+ )
            int alt9=3;
            alt9 = dfa9.predict(input);
            switch (alt9) {
                case 1 :
                    // SPQL.g:725:9: ( '0' .. '9' )+ '.' ( '0' .. '9' )*
                    {
                    // SPQL.g:725:9: ( '0' .. '9' )+
                    int cnt5=0;
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( ((LA5_0>='0' && LA5_0<='9')) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // SPQL.g:725:10: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt5 >= 1 ) break loop5;
                                EarlyExitException eee =
                                    new EarlyExitException(5, input);
                                throw eee;
                        }
                        cnt5++;
                    } while (true);

                    match('.'); 
                    // SPQL.g:725:25: ( '0' .. '9' )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( ((LA6_0>='0' && LA6_0<='9')) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // SPQL.g:725:26: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // SPQL.g:726:9: '.' ( '0' .. '9' )+
                    {
                    match('.'); 
                    // SPQL.g:726:13: ( '0' .. '9' )+
                    int cnt7=0;
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( ((LA7_0>='0' && LA7_0<='9')) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // SPQL.g:726:14: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt7 >= 1 ) break loop7;
                                EarlyExitException eee =
                                    new EarlyExitException(7, input);
                                throw eee;
                        }
                        cnt7++;
                    } while (true);


                    }
                    break;
                case 3 :
                    // SPQL.g:727:9: ( '0' .. '9' )+
                    {
                    // SPQL.g:727:9: ( '0' .. '9' )+
                    int cnt8=0;
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( ((LA8_0>='0' && LA8_0<='9')) ) {
                            alt8=1;
                        }


                        switch (alt8) {
                    	case 1 :
                    	    // SPQL.g:727:10: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt8 >= 1 ) break loop8;
                                EarlyExitException eee =
                                    new EarlyExitException(8, input);
                                throw eee;
                        }
                        cnt8++;
                    } while (true);


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FLOAT"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:731:5: ( '\\'' ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' | '.' | '%' | '/' | ':' )* '\\'' )
            // SPQL.g:731:8: '\\'' ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' | '.' | '%' | '/' | ':' )* '\\''
            {
            match('\''); 
            // SPQL.g:731:13: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' | '.' | '%' | '/' | ':' )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0=='%'||(LA10_0>='-' && LA10_0<=':')||(LA10_0>='A' && LA10_0<='Z')||LA10_0=='_'||(LA10_0>='a' && LA10_0<='z')) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // SPQL.g:
            	    {
            	    if ( input.LA(1)=='%'||(input.LA(1)>='-' && input.LA(1)<=':')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);

            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "NEWLINE"
    public final void mNEWLINE() throws RecognitionException {
        try {
            int _type = NEWLINE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:734:9: ( ( '\\r' )? '\\n' )
            // SPQL.g:734:11: ( '\\r' )? '\\n'
            {
            // SPQL.g:734:11: ( '\\r' )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0=='\r') ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // SPQL.g:734:11: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }

            match('\n'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NEWLINE"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // SPQL.g:736:4: ( ( ' ' | '\\t' | '\\n' | '\\r' )+ )
            // SPQL.g:736:6: ( ' ' | '\\t' | '\\n' | '\\r' )+
            {
            // SPQL.g:736:6: ( ' ' | '\\t' | '\\n' | '\\r' )+
            int cnt12=0;
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( ((LA12_0>='\t' && LA12_0<='\n')||LA12_0=='\r'||LA12_0==' ') ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // SPQL.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt12 >= 1 ) break loop12;
                        EarlyExitException eee =
                            new EarlyExitException(12, input);
                        throw eee;
                }
                cnt12++;
            } while (true);


            		skip();
            	

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
        // SPQL.g:1:8: ( T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | OP | GROUP | ORDER | COMPARERUN | ANCESTOR | BY | AGGRFUN | COUNT | SELECT | DESC | ASC | DISTINCT | WHERE | AND | OR | NOT | IN | ANY | UNION | INTERSECT | EXCEPT | ALL | DOT | COLON | BETWEEN | HAVING | LIKE | SEMICOLON | ID | INT | FLOAT | STRING | NEWLINE | WS )
        int alt13=42;
        alt13 = dfa13.predict(input);
        switch (alt13) {
            case 1 :
                // SPQL.g:1:10: T__38
                {
                mT__38(); 

                }
                break;
            case 2 :
                // SPQL.g:1:16: T__39
                {
                mT__39(); 

                }
                break;
            case 3 :
                // SPQL.g:1:22: T__40
                {
                mT__40(); 

                }
                break;
            case 4 :
                // SPQL.g:1:28: T__41
                {
                mT__41(); 

                }
                break;
            case 5 :
                // SPQL.g:1:34: T__42
                {
                mT__42(); 

                }
                break;
            case 6 :
                // SPQL.g:1:40: T__43
                {
                mT__43(); 

                }
                break;
            case 7 :
                // SPQL.g:1:46: T__44
                {
                mT__44(); 

                }
                break;
            case 8 :
                // SPQL.g:1:52: T__45
                {
                mT__45(); 

                }
                break;
            case 9 :
                // SPQL.g:1:58: OP
                {
                mOP(); 

                }
                break;
            case 10 :
                // SPQL.g:1:61: GROUP
                {
                mGROUP(); 

                }
                break;
            case 11 :
                // SPQL.g:1:67: ORDER
                {
                mORDER(); 

                }
                break;
            case 12 :
                // SPQL.g:1:73: COMPARERUN
                {
                mCOMPARERUN(); 

                }
                break;
            case 13 :
                // SPQL.g:1:84: ANCESTOR
                {
                mANCESTOR(); 

                }
                break;
            case 14 :
                // SPQL.g:1:93: BY
                {
                mBY(); 

                }
                break;
            case 15 :
                // SPQL.g:1:96: AGGRFUN
                {
                mAGGRFUN(); 

                }
                break;
            case 16 :
                // SPQL.g:1:104: COUNT
                {
                mCOUNT(); 

                }
                break;
            case 17 :
                // SPQL.g:1:110: SELECT
                {
                mSELECT(); 

                }
                break;
            case 18 :
                // SPQL.g:1:117: DESC
                {
                mDESC(); 

                }
                break;
            case 19 :
                // SPQL.g:1:122: ASC
                {
                mASC(); 

                }
                break;
            case 20 :
                // SPQL.g:1:126: DISTINCT
                {
                mDISTINCT(); 

                }
                break;
            case 21 :
                // SPQL.g:1:135: WHERE
                {
                mWHERE(); 

                }
                break;
            case 22 :
                // SPQL.g:1:141: AND
                {
                mAND(); 

                }
                break;
            case 23 :
                // SPQL.g:1:145: OR
                {
                mOR(); 

                }
                break;
            case 24 :
                // SPQL.g:1:148: NOT
                {
                mNOT(); 

                }
                break;
            case 25 :
                // SPQL.g:1:152: IN
                {
                mIN(); 

                }
                break;
            case 26 :
                // SPQL.g:1:155: ANY
                {
                mANY(); 

                }
                break;
            case 27 :
                // SPQL.g:1:159: UNION
                {
                mUNION(); 

                }
                break;
            case 28 :
                // SPQL.g:1:165: INTERSECT
                {
                mINTERSECT(); 

                }
                break;
            case 29 :
                // SPQL.g:1:175: EXCEPT
                {
                mEXCEPT(); 

                }
                break;
            case 30 :
                // SPQL.g:1:182: ALL
                {
                mALL(); 

                }
                break;
            case 31 :
                // SPQL.g:1:186: DOT
                {
                mDOT(); 

                }
                break;
            case 32 :
                // SPQL.g:1:190: COLON
                {
                mCOLON(); 

                }
                break;
            case 33 :
                // SPQL.g:1:196: BETWEEN
                {
                mBETWEEN(); 

                }
                break;
            case 34 :
                // SPQL.g:1:204: HAVING
                {
                mHAVING(); 

                }
                break;
            case 35 :
                // SPQL.g:1:211: LIKE
                {
                mLIKE(); 

                }
                break;
            case 36 :
                // SPQL.g:1:216: SEMICOLON
                {
                mSEMICOLON(); 

                }
                break;
            case 37 :
                // SPQL.g:1:226: ID
                {
                mID(); 

                }
                break;
            case 38 :
                // SPQL.g:1:229: INT
                {
                mINT(); 

                }
                break;
            case 39 :
                // SPQL.g:1:233: FLOAT
                {
                mFLOAT(); 

                }
                break;
            case 40 :
                // SPQL.g:1:239: STRING
                {
                mSTRING(); 

                }
                break;
            case 41 :
                // SPQL.g:1:246: NEWLINE
                {
                mNEWLINE(); 

                }
                break;
            case 42 :
                // SPQL.g:1:254: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA9 dfa9 = new DFA9(this);
    protected DFA13 dfa13 = new DFA13(this);
    static final String DFA9_eotS =
        "\1\uffff\1\3\3\uffff";
    static final String DFA9_eofS =
        "\5\uffff";
    static final String DFA9_minS =
        "\2\56\3\uffff";
    static final String DFA9_maxS =
        "\2\71\3\uffff";
    static final String DFA9_acceptS =
        "\2\uffff\1\2\1\3\1\1";
    static final String DFA9_specialS =
        "\5\uffff}>";
    static final String[] DFA9_transitionS = {
            "\1\2\1\uffff\12\1",
            "\1\4\1\uffff\12\1",
            "",
            "",
            ""
    };

    static final short[] DFA9_eot = DFA.unpackEncodedString(DFA9_eotS);
    static final short[] DFA9_eof = DFA.unpackEncodedString(DFA9_eofS);
    static final char[] DFA9_min = DFA.unpackEncodedStringToUnsignedChars(DFA9_minS);
    static final char[] DFA9_max = DFA.unpackEncodedStringToUnsignedChars(DFA9_maxS);
    static final short[] DFA9_accept = DFA.unpackEncodedString(DFA9_acceptS);
    static final short[] DFA9_special = DFA.unpackEncodedString(DFA9_specialS);
    static final short[][] DFA9_transition;

    static {
        int numStates = DFA9_transitionS.length;
        DFA9_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA9_transition[i] = DFA.unpackEncodedString(DFA9_transitionS[i]);
        }
    }

    class DFA9 extends DFA {

        public DFA9(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 9;
            this.eot = DFA9_eot;
            this.eof = DFA9_eof;
            this.min = DFA9_min;
            this.max = DFA9_max;
            this.accept = DFA9_accept;
            this.special = DFA9_special;
            this.transition = DFA9_transition;
        }
        public String getDescription() {
            return "724:1: FLOAT : ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* | '.' ( '0' .. '9' )+ | ( '0' .. '9' )+ );";
        }
    }
    static final String DFA13_eotS =
        "\6\uffff\2\33\1\uffff\15\33\1\67\1\uffff\2\33\2\uffff\1\73\1\uffff"+
        "\1\40\1\74\1\uffff\3\33\1\101\5\33\1\112\11\33\1\125\2\33\2\uffff"+
        "\2\33\2\uffff\4\33\1\uffff\3\33\1\141\1\142\1\143\1\144\1\145\1"+
        "\uffff\1\33\3\143\4\33\1\153\1\33\1\uffff\13\33\5\uffff\2\33\1\173"+
        "\2\33\1\uffff\4\33\1\u0082\3\33\1\u0086\1\u0087\1\33\1\u0089\3\33"+
        "\1\uffff\1\33\1\u008e\1\33\1\u0090\2\33\1\uffff\3\33\2\uffff\1\33"+
        "\1\uffff\2\33\1\u0099\1\33\1\uffff\1\33\1\uffff\1\u009c\1\u009d"+
        "\5\33\1\u00a3\1\uffff\2\33\2\uffff\2\33\1\u00a8\1\33\1\u00aa\1\uffff"+
        "\1\u00ab\1\33\1\u00ad\1\33\1\uffff\1\33\2\uffff\1\u00b0\1\uffff"+
        "\2\33\1\uffff\1\u00b3\1\u00b4\2\uffff";
    static final String DFA13_eofS =
        "\u00b5\uffff";
    static final String DFA13_minS =
        "\1\11\5\uffff\1\141\1\145\1\uffff\2\162\1\157\1\154\1\145\1\141"+
        "\2\145\1\150\1\157\2\156\1\170\1\60\1\uffff\1\141\1\151\2\uffff"+
        "\1\56\1\uffff\1\12\1\11\1\uffff\1\162\1\171\1\157\1\55\1\155\1\143"+
        "\1\147\1\143\1\154\1\55\1\164\1\170\1\156\1\155\1\154\2\163\1\145"+
        "\1\164\1\55\1\151\1\143\2\uffff\1\166\1\153\2\uffff\1\141\1\137"+
        "\1\165\1\145\1\uffff\1\160\1\156\1\145\5\55\1\uffff\1\167\3\55\1"+
        "\145\1\143\1\164\1\162\1\55\1\145\1\uffff\1\157\1\145\1\151\1\145"+
        "\1\155\1\156\1\160\1\162\1\141\1\164\1\163\5\uffff\1\145\1\143\1"+
        "\55\1\151\1\145\1\uffff\1\162\1\156\1\160\1\156\1\55\1\145\1\165"+
        "\1\145\2\55\1\162\1\55\1\164\1\145\1\164\1\uffff\1\156\1\55\1\163"+
        "\1\55\1\164\1\147\1\uffff\1\164\1\155\1\170\2\uffff\1\145\1\uffff"+
        "\1\157\1\156\1\55\1\143\1\uffff\1\145\1\uffff\2\55\2\145\1\164\1"+
        "\137\1\162\1\55\1\uffff\1\164\1\143\2\uffff\2\162\1\55\1\162\1\55"+
        "\1\uffff\1\55\1\164\1\55\1\151\1\uffff\1\165\2\uffff\1\55\1\uffff"+
        "\1\143\1\156\1\uffff\2\55\2\uffff";
    static final String DFA13_maxS =
        "\1\175\5\uffff\1\141\1\145\1\uffff\2\162\1\157\1\166\1\171\1\151"+
        "\1\165\1\151\1\150\1\157\2\156\1\170\1\71\1\uffff\1\141\1\151\2"+
        "\uffff\1\71\1\uffff\1\12\1\40\1\uffff\1\162\1\171\1\157\1\172\1"+
        "\165\1\171\1\147\1\143\1\154\1\172\1\164\1\170\1\156\1\155\1\154"+
        "\2\163\1\145\1\164\1\172\1\151\1\143\2\uffff\1\166\1\153\2\uffff"+
        "\1\141\1\137\1\165\1\145\1\uffff\1\160\1\156\1\145\5\172\1\uffff"+
        "\1\167\3\172\1\145\1\143\1\164\1\162\1\172\1\145\1\uffff\1\157\1"+
        "\145\1\151\1\145\1\155\1\164\1\160\1\162\1\141\1\164\1\163\5\uffff"+
        "\1\145\1\143\1\172\1\151\1\145\1\uffff\1\162\1\156\1\160\1\156\1"+
        "\172\1\145\1\165\1\145\2\172\1\162\1\172\1\164\1\145\1\164\1\uffff"+
        "\1\156\1\172\1\163\1\172\1\164\1\147\1\uffff\1\164\1\155\1\170\2"+
        "\uffff\1\145\1\uffff\1\157\1\156\1\172\1\143\1\uffff\1\145\1\uffff"+
        "\2\172\2\145\1\164\1\137\1\162\1\172\1\uffff\1\164\1\143\2\uffff"+
        "\2\162\1\172\1\162\1\172\1\uffff\1\172\1\164\1\172\1\151\1\uffff"+
        "\1\165\2\uffff\1\172\1\uffff\1\143\1\156\1\uffff\2\172\2\uffff";
    static final String DFA13_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\2\uffff\1\11\16\uffff\1\40\2\uffff"+
        "\1\44\1\45\1\uffff\1\50\2\uffff\1\52\26\uffff\1\37\1\47\2\uffff"+
        "\1\46\1\51\4\uffff\1\27\10\uffff\1\16\12\uffff\1\31\13\uffff\1\26"+
        "\1\32\1\17\1\23\1\36\5\uffff\1\30\17\uffff\1\22\6\uffff\1\43\3\uffff"+
        "\1\12\1\13\1\uffff\1\20\4\uffff\1\25\1\uffff\1\33\10\uffff\1\21"+
        "\2\uffff\1\35\1\42\5\uffff\1\41\4\uffff\1\10\1\uffff\1\15\1\24\1"+
        "\uffff\1\6\2\uffff\1\34\2\uffff\1\7\1\14";
    static final String DFA13_specialS =
        "\u00b5\uffff}>";
    static final String[] DFA13_transitionS = {
            "\1\40\1\37\2\uffff\1\36\22\uffff\1\40\6\uffff\1\35\1\1\1\2\1"+
            "\3\1\uffff\1\27\1\uffff\1\26\1\uffff\12\34\1\uffff\1\32\3\10"+
            "\2\uffff\32\33\4\uffff\1\33\1\uffff\1\14\1\15\1\13\1\20\1\25"+
            "\1\33\1\11\1\30\1\23\1\33\1\7\1\31\1\16\1\22\1\12\1\6\2\33\1"+
            "\17\1\33\1\24\1\33\1\21\3\33\1\4\1\uffff\1\5",
            "",
            "",
            "",
            "",
            "",
            "\1\41",
            "\1\42",
            "",
            "\1\43",
            "\1\44",
            "\1\45",
            "\1\51\1\uffff\1\46\4\uffff\1\50\2\uffff\1\47",
            "\1\53\23\uffff\1\52",
            "\1\54\7\uffff\1\55",
            "\1\57\17\uffff\1\56",
            "\1\60\3\uffff\1\61",
            "\1\62",
            "\1\63",
            "\1\64",
            "\1\65",
            "\1\66",
            "\12\70",
            "",
            "\1\71",
            "\1\72",
            "",
            "",
            "\1\70\1\uffff\12\34",
            "",
            "\1\37",
            "\2\40\2\uffff\1\40\22\uffff\1\40",
            "",
            "\1\75",
            "\1\76",
            "\1\77",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\3\33"+
            "\1\100\26\33",
            "\1\102\7\uffff\1\103",
            "\1\104\1\105\24\uffff\1\106",
            "\1\107",
            "\1\110",
            "\1\111",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\113",
            "\1\114",
            "\1\115",
            "\1\116",
            "\1\117",
            "\1\120",
            "\1\121",
            "\1\122",
            "\1\123",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\23\33"+
            "\1\124\6\33",
            "\1\126",
            "\1\127",
            "",
            "",
            "\1\130",
            "\1\131",
            "",
            "",
            "\1\132",
            "\1\133",
            "\1\134",
            "\1\135",
            "",
            "\1\136",
            "\1\137",
            "\1\140",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "",
            "\1\146",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\147",
            "\1\150",
            "\1\151",
            "\1\152",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\154",
            "",
            "\1\155",
            "\1\156",
            "\1\157",
            "\1\160",
            "\1\161",
            "\1\162\5\uffff\1\163",
            "\1\164",
            "\1\165",
            "\1\166",
            "\1\167",
            "\1\170",
            "",
            "",
            "",
            "",
            "",
            "\1\171",
            "\1\172",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\174",
            "\1\175",
            "",
            "\1\176",
            "\1\177",
            "\1\u0080",
            "\1\u0081",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\u0083",
            "\1\u0084",
            "\1\u0085",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\u0088",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\u008a",
            "\1\u008b",
            "\1\u008c",
            "",
            "\1\u008d",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\u008f",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\u0091",
            "\1\u0092",
            "",
            "\1\u0093",
            "\1\u0094",
            "\1\u0095",
            "",
            "",
            "\1\u0096",
            "",
            "\1\u0097",
            "\1\u0098",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\u009a",
            "",
            "\1\u009b",
            "",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\u009e",
            "\1\u009f",
            "\1\u00a0",
            "\1\u00a1",
            "\1\u00a2",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "",
            "\1\u00a4",
            "\1\u00a5",
            "",
            "",
            "\1\u00a6",
            "\1\u00a7",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\u00a9",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\u00ac",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\u00ae",
            "",
            "\1\u00af",
            "",
            "",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "",
            "\1\u00b1",
            "\1\u00b2",
            "",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\33\2\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "",
            ""
    };

    static final short[] DFA13_eot = DFA.unpackEncodedString(DFA13_eotS);
    static final short[] DFA13_eof = DFA.unpackEncodedString(DFA13_eofS);
    static final char[] DFA13_min = DFA.unpackEncodedStringToUnsignedChars(DFA13_minS);
    static final char[] DFA13_max = DFA.unpackEncodedStringToUnsignedChars(DFA13_maxS);
    static final short[] DFA13_accept = DFA.unpackEncodedString(DFA13_acceptS);
    static final short[] DFA13_special = DFA.unpackEncodedString(DFA13_specialS);
    static final short[][] DFA13_transition;

    static {
        int numStates = DFA13_transitionS.length;
        DFA13_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA13_transition[i] = DFA.unpackEncodedString(DFA13_transitionS[i]);
        }
    }

    class DFA13 extends DFA {

        public DFA13(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 13;
            this.eot = DFA13_eot;
            this.eof = DFA13_eof;
            this.min = DFA13_min;
            this.max = DFA13_max;
            this.accept = DFA13_accept;
            this.special = DFA13_special;
            this.transition = DFA13_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | OP | GROUP | ORDER | COMPARERUN | ANCESTOR | BY | AGGRFUN | COUNT | SELECT | DESC | ASC | DISTINCT | WHERE | AND | OR | NOT | IN | ANY | UNION | INTERSECT | EXCEPT | ALL | DOT | COLON | BETWEEN | HAVING | LIKE | SEMICOLON | ID | INT | FLOAT | STRING | NEWLINE | WS );";
        }
    }
 

}