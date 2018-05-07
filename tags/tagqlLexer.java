// $ANTLR 3.3 Nov 30, 2010 12:45:30 tagql.g 2014-01-09 15:49:49

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class tagqlLexer extends Lexer {
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

    public tagqlLexer() {;} 
    public tagqlLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public tagqlLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "tagql.g"; }

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // tagql.g:3:7: ( '(' )
            // tagql.g:3:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // tagql.g:4:7: ( ',' )
            // tagql.g:4:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // tagql.g:5:7: ( ')' )
            // tagql.g:5:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // tagql.g:6:7: ( '=' )
            // tagql.g:6:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "OP"
    public final void mOP() throws RecognitionException {
        try {
            int _type = OP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // tagql.g:59:4: ( '=' | '>' | '>=' | '<' | '<=' )
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
                    // tagql.g:59:6: '='
                    {
                    match('='); 

                    }
                    break;
                case 2 :
                    // tagql.g:59:12: '>'
                    {
                    match('>'); 

                    }
                    break;
                case 3 :
                    // tagql.g:59:18: '>='
                    {
                    match(">="); 


                    }
                    break;
                case 4 :
                    // tagql.g:59:25: '<'
                    {
                    match('<'); 

                    }
                    break;
                case 5 :
                    // tagql.g:59:31: '<='
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

    // $ANTLR start "TAG"
    public final void mTAG() throws RecognitionException {
        try {
            int _type = TAG;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // tagql.g:61:5: ( 'tag' )
            // tagql.g:61:7: 'tag'
            {
            match("tag"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TAG"

    // $ANTLR start "LOCATE"
    public final void mLOCATE() throws RecognitionException {
        try {
            int _type = LOCATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // tagql.g:63:8: ( 'locate' )
            // tagql.g:63:10: 'locate'
            {
            match("locate"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LOCATE"

    // $ANTLR start "AND"
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // tagql.g:65:5: ( 'and' )
            // tagql.g:65:7: 'and'
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
            // tagql.g:67:4: ( 'or' )
            // tagql.g:67:6: 'or'
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

    // $ANTLR start "SEMICOLON"
    public final void mSEMICOLON() throws RecognitionException {
        try {
            int _type = SEMICOLON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // tagql.g:69:11: ( ';' )
            // tagql.g:69:13: ';'
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

    // $ANTLR start "ID_STRING"
    public final void mID_STRING() throws RecognitionException {
        try {
            int _type = ID_STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // tagql.g:71:12: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )* )
            // tagql.g:71:14: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // tagql.g:71:38: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='-'||(LA2_0>='0' && LA2_0<='9')||(LA2_0>='A' && LA2_0<='Z')||LA2_0=='_'||(LA2_0>='a' && LA2_0<='z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // tagql.g:
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
            	    break loop2;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ID_STRING"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // tagql.g:73:5: ( ( '0' .. '9' )+ )
            // tagql.g:73:7: ( '0' .. '9' )+
            {
            // tagql.g:73:7: ( '0' .. '9' )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // tagql.g:73:7: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
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
            // tagql.g:77:5: ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* | '.' ( '0' .. '9' )+ | ( '0' .. '9' )+ )
            int alt8=3;
            alt8 = dfa8.predict(input);
            switch (alt8) {
                case 1 :
                    // tagql.g:77:9: ( '0' .. '9' )+ '.' ( '0' .. '9' )*
                    {
                    // tagql.g:77:9: ( '0' .. '9' )+
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
                    	    // tagql.g:77:10: '0' .. '9'
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

                    match('.'); 
                    // tagql.g:77:25: ( '0' .. '9' )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( ((LA5_0>='0' && LA5_0<='9')) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // tagql.g:77:26: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // tagql.g:78:9: '.' ( '0' .. '9' )+
                    {
                    match('.'); 
                    // tagql.g:78:13: ( '0' .. '9' )+
                    int cnt6=0;
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( ((LA6_0>='0' && LA6_0<='9')) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // tagql.g:78:14: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt6 >= 1 ) break loop6;
                                EarlyExitException eee =
                                    new EarlyExitException(6, input);
                                throw eee;
                        }
                        cnt6++;
                    } while (true);


                    }
                    break;
                case 3 :
                    // tagql.g:79:9: ( '0' .. '9' )+
                    {
                    // tagql.g:79:9: ( '0' .. '9' )+
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
                    	    // tagql.g:79:10: '0' .. '9'
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
            // tagql.g:83:5: ( '\\'' ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' | '.' | '%' | '/' | ':' )* '\\'' )
            // tagql.g:83:8: '\\'' ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' | '.' | '%' | '/' | ':' )* '\\''
            {
            match('\''); 
            // tagql.g:83:13: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' | '.' | '%' | '/' | ':' )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0=='%'||(LA9_0>='-' && LA9_0<=':')||(LA9_0>='A' && LA9_0<='Z')||LA9_0=='_'||(LA9_0>='a' && LA9_0<='z')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // tagql.g:
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
            	    break loop9;
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
            // tagql.g:86:9: ( ( '\\r' )? '\\n' )
            // tagql.g:86:11: ( '\\r' )? '\\n'
            {
            // tagql.g:86:11: ( '\\r' )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0=='\r') ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // tagql.g:86:11: '\\r'
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
            // tagql.g:88:4: ( ( ' ' | '\\t' | '\\n' | '\\r' )+ )
            // tagql.g:88:6: ( ' ' | '\\t' | '\\n' | '\\r' )+
            {
            // tagql.g:88:6: ( ' ' | '\\t' | '\\n' | '\\r' )+
            int cnt11=0;
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( ((LA11_0>='\t' && LA11_0<='\n')||LA11_0=='\r'||LA11_0==' ') ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // tagql.g:
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
            	    if ( cnt11 >= 1 ) break loop11;
                        EarlyExitException eee =
                            new EarlyExitException(11, input);
                        throw eee;
                }
                cnt11++;
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
        // tagql.g:1:8: ( T__16 | T__17 | T__18 | T__19 | OP | TAG | LOCATE | AND | OR | SEMICOLON | ID_STRING | INT | FLOAT | STRING | NEWLINE | WS )
        int alt12=16;
        alt12 = dfa12.predict(input);
        switch (alt12) {
            case 1 :
                // tagql.g:1:10: T__16
                {
                mT__16(); 

                }
                break;
            case 2 :
                // tagql.g:1:16: T__17
                {
                mT__17(); 

                }
                break;
            case 3 :
                // tagql.g:1:22: T__18
                {
                mT__18(); 

                }
                break;
            case 4 :
                // tagql.g:1:28: T__19
                {
                mT__19(); 

                }
                break;
            case 5 :
                // tagql.g:1:34: OP
                {
                mOP(); 

                }
                break;
            case 6 :
                // tagql.g:1:37: TAG
                {
                mTAG(); 

                }
                break;
            case 7 :
                // tagql.g:1:41: LOCATE
                {
                mLOCATE(); 

                }
                break;
            case 8 :
                // tagql.g:1:48: AND
                {
                mAND(); 

                }
                break;
            case 9 :
                // tagql.g:1:52: OR
                {
                mOR(); 

                }
                break;
            case 10 :
                // tagql.g:1:55: SEMICOLON
                {
                mSEMICOLON(); 

                }
                break;
            case 11 :
                // tagql.g:1:65: ID_STRING
                {
                mID_STRING(); 

                }
                break;
            case 12 :
                // tagql.g:1:75: INT
                {
                mINT(); 

                }
                break;
            case 13 :
                // tagql.g:1:79: FLOAT
                {
                mFLOAT(); 

                }
                break;
            case 14 :
                // tagql.g:1:85: STRING
                {
                mSTRING(); 

                }
                break;
            case 15 :
                // tagql.g:1:92: NEWLINE
                {
                mNEWLINE(); 

                }
                break;
            case 16 :
                // tagql.g:1:100: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA8 dfa8 = new DFA8(this);
    protected DFA12 dfa12 = new DFA12(this);
    static final String DFA8_eotS =
        "\1\uffff\1\4\3\uffff";
    static final String DFA8_eofS =
        "\5\uffff";
    static final String DFA8_minS =
        "\2\56\3\uffff";
    static final String DFA8_maxS =
        "\2\71\3\uffff";
    static final String DFA8_acceptS =
        "\2\uffff\1\2\1\1\1\3";
    static final String DFA8_specialS =
        "\5\uffff}>";
    static final String[] DFA8_transitionS = {
            "\1\2\1\uffff\12\1",
            "\1\3\1\uffff\12\1",
            "",
            "",
            ""
    };

    static final short[] DFA8_eot = DFA.unpackEncodedString(DFA8_eotS);
    static final short[] DFA8_eof = DFA.unpackEncodedString(DFA8_eofS);
    static final char[] DFA8_min = DFA.unpackEncodedStringToUnsignedChars(DFA8_minS);
    static final char[] DFA8_max = DFA.unpackEncodedStringToUnsignedChars(DFA8_maxS);
    static final short[] DFA8_accept = DFA.unpackEncodedString(DFA8_acceptS);
    static final short[] DFA8_special = DFA.unpackEncodedString(DFA8_specialS);
    static final short[][] DFA8_transition;

    static {
        int numStates = DFA8_transitionS.length;
        DFA8_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA8_transition[i] = DFA.unpackEncodedString(DFA8_transitionS[i]);
        }
    }

    class DFA8 extends DFA {

        public DFA8(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 8;
            this.eot = DFA8_eot;
            this.eof = DFA8_eof;
            this.min = DFA8_min;
            this.max = DFA8_max;
            this.accept = DFA8_accept;
            this.special = DFA8_special;
            this.transition = DFA8_transition;
        }
        public String getDescription() {
            return "76:1: FLOAT : ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* | '.' ( '0' .. '9' )+ | ( '0' .. '9' )+ );";
        }
    }
    static final String DFA12_eotS =
        "\6\uffff\4\13\2\uffff\1\27\2\uffff\1\21\1\30\2\uffff\3\13\1\34\2"+
        "\uffff\1\35\1\13\1\37\2\uffff\1\13\1\uffff\1\13\1\42\1\uffff";
    static final String DFA12_eofS =
        "\43\uffff";
    static final String DFA12_minS =
        "\1\11\5\uffff\1\141\1\157\1\156\1\162\2\uffff\1\56\2\uffff\1\12"+
        "\1\11\2\uffff\1\147\1\143\1\144\1\55\2\uffff\1\55\1\141\1\55\2\uffff"+
        "\1\164\1\uffff\1\145\1\55\1\uffff";
    static final String DFA12_maxS =
        "\1\172\5\uffff\1\141\1\157\1\156\1\162\2\uffff\1\71\2\uffff\1\12"+
        "\1\40\2\uffff\1\147\1\143\1\144\1\172\2\uffff\1\172\1\141\1\172"+
        "\2\uffff\1\164\1\uffff\1\145\1\172\1\uffff";
    static final String DFA12_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\4\uffff\1\12\1\13\1\uffff\1\15\1\16"+
        "\2\uffff\1\20\1\4\4\uffff\1\14\1\17\3\uffff\1\11\1\6\1\uffff\1\10"+
        "\2\uffff\1\7";
    static final String DFA12_specialS =
        "\43\uffff}>";
    static final String[] DFA12_transitionS = {
            "\1\21\1\20\2\uffff\1\17\22\uffff\1\21\6\uffff\1\16\1\1\1\3\2"+
            "\uffff\1\2\1\uffff\1\15\1\uffff\12\14\1\uffff\1\12\1\5\1\4\1"+
            "\5\2\uffff\32\13\4\uffff\1\13\1\uffff\1\10\12\13\1\7\2\13\1"+
            "\11\4\13\1\6\6\13",
            "",
            "",
            "",
            "",
            "",
            "\1\23",
            "\1\24",
            "\1\25",
            "\1\26",
            "",
            "",
            "\1\15\1\uffff\12\14",
            "",
            "",
            "\1\20",
            "\2\21\2\uffff\1\21\22\uffff\1\21",
            "",
            "",
            "\1\31",
            "\1\32",
            "\1\33",
            "\1\13\2\uffff\12\13\7\uffff\32\13\4\uffff\1\13\1\uffff\32\13",
            "",
            "",
            "\1\13\2\uffff\12\13\7\uffff\32\13\4\uffff\1\13\1\uffff\32\13",
            "\1\36",
            "\1\13\2\uffff\12\13\7\uffff\32\13\4\uffff\1\13\1\uffff\32\13",
            "",
            "",
            "\1\40",
            "",
            "\1\41",
            "\1\13\2\uffff\12\13\7\uffff\32\13\4\uffff\1\13\1\uffff\32\13",
            ""
    };

    static final short[] DFA12_eot = DFA.unpackEncodedString(DFA12_eotS);
    static final short[] DFA12_eof = DFA.unpackEncodedString(DFA12_eofS);
    static final char[] DFA12_min = DFA.unpackEncodedStringToUnsignedChars(DFA12_minS);
    static final char[] DFA12_max = DFA.unpackEncodedStringToUnsignedChars(DFA12_maxS);
    static final short[] DFA12_accept = DFA.unpackEncodedString(DFA12_acceptS);
    static final short[] DFA12_special = DFA.unpackEncodedString(DFA12_specialS);
    static final short[][] DFA12_transition;

    static {
        int numStates = DFA12_transitionS.length;
        DFA12_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA12_transition[i] = DFA.unpackEncodedString(DFA12_transitionS[i]);
        }
    }

    class DFA12 extends DFA {

        public DFA12(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 12;
            this.eot = DFA12_eot;
            this.eof = DFA12_eof;
            this.min = DFA12_min;
            this.max = DFA12_max;
            this.accept = DFA12_accept;
            this.special = DFA12_special;
            this.transition = DFA12_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__16 | T__17 | T__18 | T__19 | OP | TAG | LOCATE | AND | OR | SEMICOLON | ID_STRING | INT | FLOAT | STRING | NEWLINE | WS );";
        }
    }
 

}