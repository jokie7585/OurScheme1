package pl109_10627238;

public class InputLineProcessor {
  
  private StringBuffer mInput;
  private int mCol;
  private int mPreTokenCol;
  
  public InputLineProcessor( String input ) {
    mCol = 1;
    mInput = new StringBuffer( input );
    
  } // InputLineProcessor()
  
  public Token Next() throws Throwable {
    
    if ( !Is_Empty() ) {
      mPreTokenCol = mCol;
      
      if ( mInput.charAt( 0 ) == ';' ) {
        // clean the input buffer
        mInput = new StringBuffer();
        return null;
      } // if
      else if ( mInput.charAt( 0 ) == '"' ) {
        // String
        return StringPicker();
      } // else if
      else if ( mInput.charAt( 0 ) == '\'' ) {
        String tmpString = "" + ReadInAChar();
        return new Token( tmpString, Symbol.sQUOTE );
      } // else if
      else if ( mInput.charAt( 0 ) == ')' ) {
        String tmpString = "" + ReadInAChar();
        return new Token( tmpString, Symbol.sR_PAREN );
      } // else if
      else if ( mInput.charAt( 0 ) == '(' ) {
        // if nil or left Paren
        if ( mInput.length() > 1 && mInput.charAt( 1 ) == ')' ) {
          String tmpString = "" + ReadInAChar() + ReadInAChar();
          return new Token( tmpString, Symbol.sNIL );
        } // if
        else {
          String tmpString = "" + ReadInAChar();
          return new Token( tmpString, Symbol.sL_PAREN );
        } // else
        
      } // else if
      else {
        // integer or symbol or float or dot or t or nil
        StringBuffer buffer = new StringBuffer();
        while ( mInput.length() > 0
            && ( !Is_Seperator( mInput.charAt( 0 ) ) && !Is_Whatespace( mInput.charAt( 0 ) ) ) ) {
          buffer.append( ReadInAChar() );
        } // while
        
        // judge type
        if ( buffer.toString().equals( "t" ) ) {
          return new Token( buffer.toString(), Symbol.sT );
        } // if
        else if ( buffer.toString().equals( "#t" ) ) {
          return new Token( buffer.toString(), Symbol.sT );
        } // else if
        else if ( buffer.toString().equals( "nil" ) ) {
          return new Token( buffer.toString(), Symbol.sNIL );
        } // else if
        else if ( buffer.toString().equals( "#f" ) ) {
          return new Token( buffer.toString(), Symbol.sNIL );
        } // else if
        else if ( buffer.toString().equals( "." ) ) {
          return new Token( buffer.toString(), Symbol.sDOT );
        } // else if
        else {
          // integer or symbol or float
          Symbol tmpType = Symbol.sINT;
          boolean findNumber = false;
          
          for ( int i = 0 ; i < buffer.length() ; i++ ) {
            
            if ( buffer.charAt( i ) == '.' ) {
              if ( tmpType == Symbol.sINT ) {
                tmpType = Symbol.sFLOAT;
              } // if
              else {
                tmpType = Symbol.sSYMBOL;
              } // else
              
            } // if
            else if ( buffer.charAt( i ) < '0' || buffer.charAt( i ) > '9' ) {
              // process add and sub operator
              if ( i == 0 ) {
                if ( buffer.charAt( i ) == '+' || buffer.charAt( i ) == '-' ) {
                  // do nothing
                } // if
                else {
                  // not dot or add or sub or 0 to 9 when at char i
                  tmpType = Symbol.sSYMBOL;
                } // else
                
              } // if
              else {
                tmpType = Symbol.sSYMBOL;
              } // else
            } // else if
            else if ( buffer.charAt( i ) > '0' || buffer.charAt( i ) < '9' ) {
              findNumber = true;
            } // else if
          } // for
          
          if ( tmpType == Symbol.sFLOAT || tmpType == Symbol.sINT ) {
            if ( findNumber ) {
              if ( buffer.charAt( 0 ) == '+' ) {
                buffer.deleteCharAt( 0 );
              } // if
              
              return new Token( buffer.toString(), tmpType );
            } // if
            else {
              return new Token( buffer.toString(), Symbol.sSYMBOL );
            } // else
          } // if
          else {
            return new Token( buffer.toString(), tmpType );
          } // else
          
        } // else
        
      } // else
      
    } // if
    
    // only when comment or endofline encounter return null
    return null;
  } // Next()
  
  public boolean Is_Empty() {
    SkipWhiteSapce();
    IngnoreComment();
    
    if ( mInput.length() > 0 ) {
      return false;
    } // if
    else {
      return true;
    } // else
  } // Is_Empty()
  
  public void ResetCol() {
    mCol = 1;
  } // ResetCol()
  
  public int PreToken_Col() {
    return mPreTokenCol;
  } // PreToken_Col()
  
  private char ReadInAChar() {
    char tmp = mInput.charAt( 0 );
    mInput.deleteCharAt( 0 );
    mCol++;
    return tmp;
  } // ReadInAChar()
  
  private void SkipWhiteSapce() {
    while ( mInput.length() > 0 && Is_Whatespace( mInput.charAt( 0 ) ) ) {
      ReadInAChar();
    } // while
  } // SkipWhiteSapce()
  
  private void IngnoreComment() {
    SkipWhiteSapce();
    if ( mInput.length() > 0 && mInput.charAt( 0 ) == ';' ) {
      mInput = new StringBuffer();
    } // if
  } // IngnoreComment()
  
  private boolean Is_Seperator( char s ) {
    if ( s == ' ' || s == '\t' || s == '(' || s == ')' || s == '\'' || s == '"' || s == ';' ) {
      return true;
    } // if
    
    return false;
  } // Is_Seperator()
  
  private boolean Is_Whatespace( char s ) {
    if ( s == ' ' || s == '\t' || s == '\r' ) {
      return true;
    } // if
    
    return false;
  } // Is_Whatespace()
  
  private Token StringPicker() throws Throwable {
    // read first quote
    StringBuffer tmpBuffer = new StringBuffer();
    tmpBuffer.append( ReadInAChar() );
    
    // append until separator or end of line
    while ( mInput.length() > 0 && mInput.charAt( 0 ) != '"' ) {
      if ( mInput.charAt( 0 ) == '\\' ) {
        if ( mInput.length() > 1 ) {
          
          if ( mInput.charAt( 1 ) == '"' ) {
            ReadInAChar();
            ReadInAChar();
            tmpBuffer.append( '"' );
          } // if
          else if ( mInput.charAt( 1 ) == '\'' ) {
            ReadInAChar();
            ReadInAChar();
            tmpBuffer.append( '\'' );
          } // else if
          else if ( mInput.charAt( 1 ) == 'n' ) {
            ReadInAChar();
            ReadInAChar();
            tmpBuffer.append( '\n' );
          } // else if
          else if ( mInput.charAt( 1 ) == 't' ) {
            ReadInAChar();
            ReadInAChar();
            tmpBuffer.append( '\t' );
          } // else if
          else if ( mInput.charAt( 1 ) == '\\' ) {
            ReadInAChar();
            ReadInAChar();
            tmpBuffer.append( '\\' );
          } // else if
          else {
            ReadInAChar();
            tmpBuffer.append( '\\' );
          } // else
          
        } // if
        else {
          tmpBuffer.append( ReadInAChar() );
        } // else
      } // if
      else {
        tmpBuffer.append( ReadInAChar() );
      } // else
      
    } // while
    
    // check if error occurs
    if ( mInput.length() > 0 && mInput.charAt( 0 ) == '"' ) {
      tmpBuffer.append( ReadInAChar() );
      return new Token( tmpBuffer.toString(), Symbol.sSTRING );
    } // if
    else {
      throw new NoclosingQuoteError( MyScanner.Get_Instance().CurLine(), mCol );
    } // else
    
  } // StringPicker()
  
  // the fallowing is test function
  public static void Tester( String input ) throws Throwable {
    InputLineProcessor tmProcessor = new InputLineProcessor( input );
    while ( !tmProcessor.Is_Empty() ) {
      Token tmpToken = tmProcessor.Next();
      if ( tmpToken == null ) {
        System.out.println( "null : comment or endofline " );
      } // if
      else {
        System.out.println( "content: " + tmpToken.mContent + " ==> type: " + tmpToken.mType.mTypeString );
      } // else
      
    } // while
  } // Tester()
  
} // class InputLineProcessor

class Token {
  public String mContent;
  public Symbol mType;
  
  public Token( String mContent, Symbol mType ) {
    this.mContent = mContent;
    this.mType = mType;
  } // Token()
  
} // class Token