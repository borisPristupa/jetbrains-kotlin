// Generated from org\jetbrains\dummy\lang\DummyLanguage.g4 by ANTLR 4.7.2

package org.jetbrains.dummy.lang;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DummyLanguageLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		VAR=1, IF=2, ELSE=3, TRUE=4, FALSE=5, BEGIN=6, END=7, FUN=8, RETURN=9, 
		ID=10, NUMBER=11, DIGIT=12, WS=13, SINGLE_COMMENT=14, MULTI_COMMENT=15, 
		SEMICOLON=16, LEFT_BR=17, RIGHT_BR=18, ASSIGN=19, COMMA=20;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"VAR", "IF", "ELSE", "TRUE", "FALSE", "BEGIN", "END", "FUN", "RETURN", 
			"ID", "NUMBER", "DIGIT", "WS", "SINGLE_COMMENT", "MULTI_COMMENT", "SEMICOLON", 
			"LEFT_BR", "RIGHT_BR", "ASSIGN", "COMMA"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'var'", "'if'", "'else'", "'true'", "'false'", "'{'", "'}'", "'fun'", 
			"'return'", null, null, null, null, null, null, "';'", "'('", "')'", 
			"'='", "','"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "VAR", "IF", "ELSE", "TRUE", "FALSE", "BEGIN", "END", "FUN", "RETURN", 
			"ID", "NUMBER", "DIGIT", "WS", "SINGLE_COMMENT", "MULTI_COMMENT", "SEMICOLON", 
			"LEFT_BR", "RIGHT_BR", "ASSIGN", "COMMA"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	    public static final int COMMENTS = 1;


	public DummyLanguageLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "DummyLanguage.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\26\u0096\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\4\3"+
		"\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\b"+
		"\3\b\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\5\13S\n\13\3\13"+
		"\7\13V\n\13\f\13\16\13Y\13\13\3\f\5\f\\\n\f\3\f\3\f\5\f`\n\f\3\f\3\f\7"+
		"\fd\n\f\f\f\16\fg\13\f\5\fi\n\f\3\r\3\r\3\16\6\16n\n\16\r\16\16\16o\3"+
		"\16\3\16\3\17\3\17\3\17\3\17\7\17x\n\17\f\17\16\17{\13\17\3\17\3\17\3"+
		"\20\3\20\3\20\3\20\7\20\u0083\n\20\f\20\16\20\u0086\13\20\3\20\3\20\3"+
		"\20\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\u0084"+
		"\2\26\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35"+
		"\20\37\21!\22#\23%\24\'\25)\26\3\2\b\4\2aac|\5\2\62;aac|\3\2\63;\3\2\62"+
		";\5\2\13\f\17\17\"\"\4\2\f\f\17\17\2\u009d\2\3\3\2\2\2\2\5\3\2\2\2\2\7"+
		"\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2"+
		"\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2"+
		"\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2"+
		"\2)\3\2\2\2\3+\3\2\2\2\5/\3\2\2\2\7\62\3\2\2\2\t\67\3\2\2\2\13<\3\2\2"+
		"\2\rB\3\2\2\2\17D\3\2\2\2\21F\3\2\2\2\23J\3\2\2\2\25R\3\2\2\2\27h\3\2"+
		"\2\2\31j\3\2\2\2\33m\3\2\2\2\35s\3\2\2\2\37~\3\2\2\2!\u008c\3\2\2\2#\u008e"+
		"\3\2\2\2%\u0090\3\2\2\2\'\u0092\3\2\2\2)\u0094\3\2\2\2+,\7x\2\2,-\7c\2"+
		"\2-.\7t\2\2.\4\3\2\2\2/\60\7k\2\2\60\61\7h\2\2\61\6\3\2\2\2\62\63\7g\2"+
		"\2\63\64\7n\2\2\64\65\7u\2\2\65\66\7g\2\2\66\b\3\2\2\2\678\7v\2\289\7"+
		"t\2\29:\7w\2\2:;\7g\2\2;\n\3\2\2\2<=\7h\2\2=>\7c\2\2>?\7n\2\2?@\7u\2\2"+
		"@A\7g\2\2A\f\3\2\2\2BC\7}\2\2C\16\3\2\2\2DE\7\177\2\2E\20\3\2\2\2FG\7"+
		"h\2\2GH\7w\2\2HI\7p\2\2I\22\3\2\2\2JK\7t\2\2KL\7g\2\2LM\7v\2\2MN\7w\2"+
		"\2NO\7t\2\2OP\7p\2\2P\24\3\2\2\2QS\t\2\2\2RQ\3\2\2\2SW\3\2\2\2TV\t\3\2"+
		"\2UT\3\2\2\2VY\3\2\2\2WU\3\2\2\2WX\3\2\2\2X\26\3\2\2\2YW\3\2\2\2Z\\\7"+
		"/\2\2[Z\3\2\2\2[\\\3\2\2\2\\]\3\2\2\2]i\7\62\2\2^`\7/\2\2_^\3\2\2\2_`"+
		"\3\2\2\2`a\3\2\2\2ae\t\4\2\2bd\5\31\r\2cb\3\2\2\2dg\3\2\2\2ec\3\2\2\2"+
		"ef\3\2\2\2fi\3\2\2\2ge\3\2\2\2h[\3\2\2\2h_\3\2\2\2i\30\3\2\2\2jk\t\5\2"+
		"\2k\32\3\2\2\2ln\t\6\2\2ml\3\2\2\2no\3\2\2\2om\3\2\2\2op\3\2\2\2pq\3\2"+
		"\2\2qr\b\16\2\2r\34\3\2\2\2st\7\61\2\2tu\7\61\2\2uy\3\2\2\2vx\n\7\2\2"+
		"wv\3\2\2\2x{\3\2\2\2yw\3\2\2\2yz\3\2\2\2z|\3\2\2\2{y\3\2\2\2|}\b\17\3"+
		"\2}\36\3\2\2\2~\177\7\61\2\2\177\u0080\7,\2\2\u0080\u0084\3\2\2\2\u0081"+
		"\u0083\13\2\2\2\u0082\u0081\3\2\2\2\u0083\u0086\3\2\2\2\u0084\u0085\3"+
		"\2\2\2\u0084\u0082\3\2\2\2\u0085\u0087\3\2\2\2\u0086\u0084\3\2\2\2\u0087"+
		"\u0088\7,\2\2\u0088\u0089\7\61\2\2\u0089\u008a\3\2\2\2\u008a\u008b\b\20"+
		"\3\2\u008b \3\2\2\2\u008c\u008d\7=\2\2\u008d\"\3\2\2\2\u008e\u008f\7*"+
		"\2\2\u008f$\3\2\2\2\u0090\u0091\7+\2\2\u0091&\3\2\2\2\u0092\u0093\7?\2"+
		"\2\u0093(\3\2\2\2\u0094\u0095\7.\2\2\u0095*\3\2\2\2\r\2RUW[_ehoy\u0084"+
		"\4\b\2\2\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}