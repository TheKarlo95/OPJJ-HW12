package hr.fer.zemris.java.custom.scripting.lexer;

import static org.junit.Assert.*;

import org.junit.Test;


@SuppressWarnings("javadoc")
public class LexerTest {

	@Test
	public void testGetTokenType() {
		Lexer lex = new Lexer("{$123$}");
		assertEquals(TokenType.INTEGER, lex.getTokenType("123"));
		assertEquals(TokenType.DOUBLE, lex.getTokenType("123.312"));
		assertEquals(TokenType.INTEGER, lex.getTokenType("-123"));
		assertEquals(TokenType.VARIABLE, lex.getTokenType("asd2_23"));
		assertEquals(TokenType.FUNCTION, lex.getTokenType("@das"));
		assertEquals(TokenType.TAGNAME, lex.getTokenType("="));
		assertEquals(TokenType.INTEGER, lex.getTokenType("12"));
		assertEquals(TokenType.START, lex.getTokenType("{$"));
		assertEquals(TokenType.END, lex.getTokenType("$}"));
	}

	@Test
	public void testNextToken() {
		Lexer lex = new Lexer("{$-123 - dasd @fsd \"dasda\"$}");
		
		checkToken(lex.nextToken(), new Token(TokenType.INTEGER, "-123"));
		checkToken(lex.nextToken(), new Token(TokenType.OPERATOR, "-"));
		checkToken(lex.nextToken(), new Token(TokenType.VARIABLE, "dasd"));
		checkToken(lex.nextToken(), new Token(TokenType.FUNCTION, "@fsd"));
		checkToken(lex.nextToken(), new Token(TokenType.STRING, "\"dasda\""));
	}

	@Test
	public void test1() {
		Lexer lex = new Lexer("{$ FOR i -1 10 1 $}");
		
		checkToken(lex.nextToken(), new Token(TokenType.KEYWORD, "FOR"));
		checkToken(lex.nextToken(), new Token(TokenType.VARIABLE, "i"));
		checkToken(lex.nextToken(), new Token(TokenType.INTEGER, "-1"));
		checkToken(lex.nextToken(), new Token(TokenType.INTEGER, "10"));
		checkToken(lex.nextToken(), new Token(TokenType.INTEGER, "1"));
	}
	
	@Test
	public void testFinal() {
		String str = "This is sample text."
				+ "\n{$ FOR i 1 10 1 $}"
				+ "\n\tThis is {$= i $}-th time this message is generated."
				+ "\n{$END$}"
				+ "\n{$FOR i 0 10 2 $}"
				+ "\n\tsin({$=i$}^2) = {$= i i * @sin \"0.000\" @decfmt $}"
				+ "\n{$END$}";
		Lexer lex = new Lexer(str);
		
		checkToken(lex.nextToken(), new Token(TokenType.TEXT, "This"));
		checkToken(lex.nextToken(), new Token(TokenType.TEXT, "is"));
		checkToken(lex.nextToken(), new Token(TokenType.TEXT, "sample"));
		checkToken(lex.nextToken(), new Token(TokenType.TEXT, "text."));
		
		checkToken(lex.nextToken(), new Token(TokenType.START, "{$"));
		checkToken(lex.nextToken(), new Token(TokenType.KEYWORD, "FOR"));
		checkToken(lex.nextToken(), new Token(TokenType.VARIABLE, "i"));
		checkToken(lex.nextToken(), new Token(TokenType.INTEGER, "1"));
		checkToken(lex.nextToken(), new Token(TokenType.INTEGER, "10"));
		checkToken(lex.nextToken(), new Token(TokenType.INTEGER, "1"));
		checkToken(lex.nextToken(), new Token(TokenType.END, "$}"));
		
		checkToken(lex.nextToken(), new Token(TokenType.TEXT, "This"));
		checkToken(lex.nextToken(), new Token(TokenType.TEXT, "is"));
		checkToken(lex.nextToken(), new Token(TokenType.START, "{$"));
		checkToken(lex.nextToken(), new Token(TokenType.TAGNAME, "="));
		checkToken(lex.nextToken(), new Token(TokenType.VARIABLE, "i"));
		checkToken(lex.nextToken(), new Token(TokenType.END, "$}"));
		checkToken(lex.nextToken(), new Token(TokenType.TEXT, "-th"));
		checkToken(lex.nextToken(), new Token(TokenType.TEXT, "time"));
		checkToken(lex.nextToken(), new Token(TokenType.TEXT, "this"));
		checkToken(lex.nextToken(), new Token(TokenType.TEXT, "message"));
		checkToken(lex.nextToken(), new Token(TokenType.TEXT, "is"));
		checkToken(lex.nextToken(), new Token(TokenType.TEXT, "generated."));
		
		checkToken(lex.nextToken(), new Token(TokenType.START, "{$"));
		checkToken(lex.nextToken(), new Token(TokenType.KEYWORD, "END"));
		checkToken(lex.nextToken(), new Token(TokenType.END, "$}"));
		
		checkToken(lex.nextToken(), new Token(TokenType.START, "{$"));
		checkToken(lex.nextToken(), new Token(TokenType.KEYWORD, "FOR"));
		checkToken(lex.nextToken(), new Token(TokenType.VARIABLE, "i"));
		checkToken(lex.nextToken(), new Token(TokenType.INTEGER, "0"));
		checkToken(lex.nextToken(), new Token(TokenType.INTEGER, "10"));
		checkToken(lex.nextToken(), new Token(TokenType.INTEGER, "2"));
		checkToken(lex.nextToken(), new Token(TokenType.END, "$}"));
		
		checkToken(lex.nextToken(), new Token(TokenType.TEXT, "sin("));
		checkToken(lex.nextToken(), new Token(TokenType.START, "{$"));
		checkToken(lex.nextToken(), new Token(TokenType.TAGNAME, "="));
		checkToken(lex.nextToken(), new Token(TokenType.VARIABLE, "i"));
		checkToken(lex.nextToken(), new Token(TokenType.END, "$}"));
		checkToken(lex.nextToken(), new Token(TokenType.TEXT, "^2)"));
		checkToken(lex.nextToken(), new Token(TokenType.TEXT, "="));
		checkToken(lex.nextToken(), new Token(TokenType.START, "{$"));
		checkToken(lex.nextToken(), new Token(TokenType.TAGNAME, "="));
		checkToken(lex.nextToken(), new Token(TokenType.VARIABLE, "i"));
		checkToken(lex.nextToken(), new Token(TokenType.VARIABLE, "i"));
		checkToken(lex.nextToken(), new Token(TokenType.OPERATOR, "*"));
		checkToken(lex.nextToken(), new Token(TokenType.FUNCTION, "@sin"));
		checkToken(lex.nextToken(), new Token(TokenType.STRING, "\"0.000\""));
		checkToken(lex.nextToken(), new Token(TokenType.FUNCTION, "@decfmt"));
		checkToken(lex.nextToken(), new Token(TokenType.END, "$}"));
		
		checkToken(lex.nextToken(), new Token(TokenType.START, "{$"));
		checkToken(lex.nextToken(), new Token(TokenType.KEYWORD, "END"));
		checkToken(lex.nextToken(), new Token(TokenType.END, "$}"));

	}

	private void checkToken(Token actual, Token expected) {
		String msg = "Token are not equal.";
		assertEquals(msg, expected.getType(), actual.getType());
		assertEquals(msg, expected.getValue(), actual.getValue());
	}

}
