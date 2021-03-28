package pl109_10627238;

import java.util.Vector;

public class Interpreter {
  private static Token sTmpToken;
  
  public static void ReadSexp() throws Throwable {
    Node rootNode = FindExp();
    // check
    if ( rootNode.mL_Child != null && rootNode.mL_Child.mToken.mContent.equals( "exit" )
        && rootNode.mR_Child == null ) {
      throw new FinishProgramException();
    } // if
    
    Printer( rootNode );
    MyScanner.Get_Instance().FinishReset();
  } // ReadSexp()
  
  private static Node FindExp() throws Throwable {
    Token tmp = PeekNextToken();
    
    if ( tmp.mType == Symbol.sQUOTE ) {
      ComfirmNextToken();
      Node retNode = FindExp();
      retNode.mIs_quote = true;
      return retNode;
      
    } // if
    else if ( tmp.mType == Symbol.sL_PAREN ) {
      // check the left PAREN
      ComfirmNextToken();
      // create new tree
      Node newSexp = new Node();
      // find first sEXP
      Node retNode = FindExp();
      Node bones = newSexp.Add_LeftChild( retNode );
      
      boolean Is_Skip = false;
      while ( retNode != null && !Is_Skip ) {
        tmp = PeekNextToken();
        
        if ( tmp.mType == Symbol.sDOT || tmp.mType == Symbol.sR_PAREN ) {
          Is_Skip = true;
        } // if
        else {
          retNode = FindExp();
          bones = bones.Add_NoDot_Child( retNode );
        } // else
        
      } // while
      
      tmp = PeekNextToken();
      if ( tmp.mType == Symbol.sDOT ) {
        ComfirmNextToken();
        retNode = FindExp();
        bones.Add_Dot_Child( retNode );
      } // if
      
      tmp = PeekNextToken();
      if ( tmp.mType == Symbol.sR_PAREN ) {
        ComfirmNextToken();
        
        return newSexp;
      } // if
      else {
        ComfirmNextToken();
        Vector<String> expects = new Vector<String>();
        expects.add( "')'" );
        int line = MyScanner.Get_Instance().CurLine();
        int col = MyScanner.Get_Instance().PreTokenCol();
        throw new UnexpectedError( expects, tmp, line, col );
      } // else
      
    } // else if
    else if ( tmp.mType == Symbol.sFLOAT || tmp.mType == Symbol.sINT || tmp.mType == Symbol.sNIL
        || tmp.mType == Symbol.sT || tmp.mType == Symbol.sSTRING || tmp.mType == Symbol.sSYMBOL ) {
      Node node = new Node( tmp );
      ComfirmNextToken();
      return node;
    } // else if
    else {
      ComfirmNextToken();
      Vector<String> expects = new Vector<String>();
      expects.add( "atom" );
      expects.add( "'('" );
      int line = MyScanner.Get_Instance().CurLine();
      int col = MyScanner.Get_Instance().PreTokenCol();
      throw new UnexpectedError( expects, tmp, line, col );
    } // else
    
  } // FindExp()
  
  public static void Printer( Node root ) {
    if ( root.Is_Dot() ) {
      System.out.print( "( " );
      SubPrinter( root, 1 );
      System.out.println( ")" );
    } // if
    else {
      if ( root.mIs_quote ) {
        System.out.println( "( quote" );
        System.out.println( "  " + Evaluate( root.mToken ) );
        System.out.println( ")" );
      } // if
      else {
        System.out.println( Evaluate( root.mToken ) );
      } // else
    } // else
  } // Printer()
  
  private static void SubPrinter( Node root, int level ) {
    int indentCounter = level * 2;
    StringBuffer indent = new StringBuffer();
    for ( int i = 0 ; i < indentCounter ; i++ ) {
      indent.append( " " );
    } // for
    
    // remove quote
    if ( root.mIs_quote ) {
      root.mIs_quote = false;
      System.out.println( "quote" );
      System.out.print( indent.toString() + "( " );
      SubPrinter( root, ++level );
      System.out.println( indent.toString() + ")" );
    } // if
    else {
      if ( root.mL_Child.Is_Dot() ) {
        System.out.print( "( " );
        SubPrinter( root.mL_Child, level + 1 );
        System.out.println( indent.toString() + ")" );
      } // if
      else {
        // TODO if quote or not
        if ( root.mL_Child.mIs_quote ) {
          System.out.println( "( quote" );
          System.out.println( IndentGenerator( level + 1 ) + Evaluate( root.mL_Child.mToken ) );
          System.out.println( IndentGenerator( level ) + ")" );
        } // if
        else {
          System.out.println( Evaluate( root.mL_Child.mToken ) );
        } // else
      } // else
      
      // print through the bones
      while ( root.mR_Child != null ) {
        root = root.mR_Child;
        
        if ( root.Is_Dot() ) {
          if ( root.mL_Child.Is_Dot() ) {
            System.out.print( IndentGenerator( level ) + "( " );
            SubPrinter( root.mL_Child, level + 1 );
            System.out.println( IndentGenerator( level ) + ")" );
          } // if
          else {
            if ( !root.mL_Child.mIs_quote ) {
              System.out.println( IndentGenerator( level ) + Evaluate( root.mL_Child.mToken ) );
            } // if
            else {
              System.out.println( IndentGenerator( level ) + "( quote" );
              System.out.println( IndentGenerator( level + 1 ) + Evaluate( root.mL_Child.mToken ) );
              System.out.println( IndentGenerator( level ) + ")" );
            } // else
          } // else
        } // if
        else {
          // last node
          if ( !root.mIs_quote ) {
            System.out.println( IndentGenerator( level ) + "." );
            System.out.println( IndentGenerator( level ) + Evaluate( root.mToken ) );
          } // if
          else {
            System.out.println( IndentGenerator( level ) + "quote" );
            System.out.println( IndentGenerator( level ) + Evaluate( root.mToken ) );
          } // else
        } // else
        
      } // while
    } // else
    
  } // SubPrinter()
  
  private static String IndentGenerator( int level ) {
    int indentCounter = level * 2;
    StringBuffer indent = new StringBuffer();
    for ( int i = 0 ; i < indentCounter ; i++ ) {
      indent.append( " " );
    } // for
    
    return indent.toString();
  } // IndentGenerator()
  
  private static Token PeekNextToken() throws Throwable {
    if ( sTmpToken == null ) {
      sTmpToken = MyScanner.Get_Instance().Next();
      // System.out.println( "read in : " + sTmpToken.mContent );
      return sTmpToken;
    } // if
    
    return sTmpToken;
  } // PeekNextToken()
  
  private static void ComfirmNextToken() {
    sTmpToken = null;
  } // ComfirmNextToken()
  
  private static String Evaluate( Token token ) {
    if ( token.mType == Symbol.sINT ) {
      return Integer.toString( Integer.parseInt( token.mContent ) );
    } // if
    else if ( token.mType == Symbol.sFLOAT ) {
      return String.format( "%.3f", Float.parseFloat( token.mContent ) );
    } // else if
    else if ( token.mType == Symbol.sSTRING || token.mType == Symbol.sSYMBOL ) {
      return token.mContent;
    } // else if
    else if ( token.mType == Symbol.sNIL ) {
      return "nil";
    } // else if
    else if ( token.mType == Symbol.sT ) {
      return "#t";
    } // else if
    return null;
  } // Evaluate()
  
} // class Interpreter

class Node {
  public Node mL_Child;
  public Node mR_Child;
  public Token mToken;
  public boolean mIs_quote;
  
  public Node() {
    mToken = new Token( ".", Symbol.sDOT );
    mIs_quote = false;
  } // Node()
  
  public Node( Token token ) {
    mToken = token;
    mIs_quote = false;
  } // Node()
  
  public boolean Is_Dot() {
    if ( mToken.mType == Symbol.sDOT ) {
      return true;
    } // if
    
    return false;
  } // Is_Dot()
  
  public Node Add_LeftChild( Node node ) {
    mL_Child = node;
    return this;
  } // Add_LeftChild()
  
  public Node Add_NoDot_Child( Node node ) {
    mR_Child = new Node();
    mR_Child.mL_Child = node;
    return mR_Child;
  } // Add_No_Dot_Child()
  
  public Node Add_Dot_Child( Node node ) {
    mR_Child = node;
    return this;
  } // Add_Dot_Child()
  
} // class Node