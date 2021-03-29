package pl109_10627238;

import java.util.Scanner;

public class MyScanner {
  
  private static MyScanner sMyScanner_S;
  private Scanner mStdin;
  private int mLine;
  private boolean mIs_EOF;
  private InputLineProcessor mTokenStream;
  
  public MyScanner() {
    mStdin = new Scanner( System.in );
    // init pal
    // read UtestNumber
    Main.suTestNum = mStdin.nextInt();
    // read line enter
    mStdin.nextLine();
    
    mIs_EOF = false;
    mLine = 0;
  } // MyScanner()
  
  public Token Next() throws Throwable {
    
    while ( mTokenStream == null || mTokenStream.Is_Empty() ) {
      ReadInALine();
    } // while
    
    Token tmpToken = mTokenStream.Next();
    while ( tmpToken == null ) {
      tmpToken = Next();
    } // while
    
    return tmpToken;
  } // Next()
  
  public static MyScanner Get_Instance() {
    if ( sMyScanner_S == null ) {
      sMyScanner_S = new MyScanner();
      return sMyScanner_S;
    } // if
    else {
      return sMyScanner_S;
    } // else
  } // Get_Instance()
  
  public void ErrorReset() throws Throwable {
    // delete remained line
    mLine = 0;
    System.out.print( "> " );
    ReadInALine();
  } // ErrorReset()
  
  public void FinishReset() throws Throwable {
    // if cur token stream not empty reset m line to 1
    // else reset to 0
    if ( mTokenStream.Is_Empty() ) {
      mLine = 0;
    } // if
    else {
      mLine = 1;
    } // else
    
  } // FinishReset()
  
  public int CurLine() {
    return mLine;
  } // CurLine()
  
  public int PreTokenCol() {
    return mTokenStream.PreToken_Col();
  } // PreTokenCol()
  
  private void ReadInALine() throws Throwable {
    if ( mStdin.hasNext() ) {
      // read a line
      mTokenStream = new InputLineProcessor( mStdin.nextLine() );
      mLine++;
      
    } // if
    else {
      if ( mIs_EOF == false ) {
        mIs_EOF = true;
      } // if
      else {
        throw new EOFEncounterError();
      } // else
      
    } // else
  } // ReadInALine()
  
} // class MyScanner
