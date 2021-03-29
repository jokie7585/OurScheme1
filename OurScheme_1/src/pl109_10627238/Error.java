package pl109_10627238;

import java.util.Vector;

public class Error extends Throwable {
  
  private String mTypeString;
  private String mMsgString;
  
  public Error( String type ) {
    mTypeString = type;
  } // Error()
  
  public String Get_Msg() {
    StringBuffer tmpBuffer = new StringBuffer();
    tmpBuffer.append( "ERROR (" );
    tmpBuffer.append( mTypeString );
    tmpBuffer.append( ") : " );
    tmpBuffer.append( mMsgString );
    return tmpBuffer.toString();
  } // Get_Msg()
  
  public void Set_Msg( String msg ) {
    mMsgString = msg;
  } // Set_Msg()
  
} // class Error

class NoclosingQuoteError extends Error {
  public NoclosingQuoteError( int Line, int Col ) {
    super( "no closing quote" );
    
    // create message
    StringBuffer tmpBuffer = new StringBuffer();
    tmpBuffer.append( "END-OF-LINE encountered at Line " );
    tmpBuffer.append( Line );
    tmpBuffer.append( " Column " );
    tmpBuffer.append( Col );
    this.Set_Msg( tmpBuffer.toString() );
  } // NoclosingQuoteError()
  
} // class NoclosingQuoteError

class UnexpectedError extends Error {
  public UnexpectedError( Vector<String> expects, Token token, int line, int col ) {
    super( "unexpected token" );
    
    StringBuffer tmpBuffer = new StringBuffer();
    for ( int i = 0 ; i < expects.size() ; i++ ) {
      if ( i == 0 ) {
        tmpBuffer.append( expects.elementAt( i ) );
      } // if
      else {
        tmpBuffer.append( " or " );
        tmpBuffer.append( expects.elementAt( i ) );
      } // else
      
    } // for
    
    tmpBuffer.append( " expected when token at Line " );
    tmpBuffer.append( line );
    tmpBuffer.append( " Column " );
    tmpBuffer.append( col );
    tmpBuffer.append( " is >>" );
    tmpBuffer.append( token.mContent );
    tmpBuffer.append( "<<" );
    this.Set_Msg( tmpBuffer.toString() );
  } // UnexpectedError()
} // class UnexpectedError

class EOFEncounterError extends Error {
  public EOFEncounterError() {
    super( "no more input" );
    
    this.Set_Msg( "END-OF-FILE encountered" );
  } // EOFEncounterError()
} // class EOFEncounterError

class FinishProgramException extends Error {
  public FinishProgramException() {
    super( "End programe" );
    
  } // FinishProgramException()
} // class FinishProgramException