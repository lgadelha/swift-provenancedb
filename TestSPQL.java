// package org.globus.swift.provenance;

import java.io.IOException;
import org.antlr.runtime.*;

public class TestSPQL {
	public static void main(String[] args) throws IOException, RecognitionException {
		// TODO Auto-generated method stub
		// Create an input character stream from standard in
		ANTLRInputStream input = new ANTLRInputStream(System.in);
		// Create an ExprLexer that feeds from that stream
		SPQLLexer lexer = new SPQLLexer(input);
		// Create a stream of tokens fed by the lexer 
		CommonTokenStream tokens = new CommonTokenStream(lexer); 
		// Create a parser that feeds off the token stream 
		SPQLParser parser = new SPQLParser(tokens);
		// Begin parsing at rule prog
		parser.query();
		System.out.println();
	}
}

