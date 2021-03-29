package pl109_10627238;

public class Main {
  public static int suTestNum;
  
  public static void main( String[] args ) throws Throwable {
    try {
      System.out.println( "Welcome to OurScheme!" );
      System.out.print( "> " );
      
      while ( true ) {
        try {
          Interpreter.ReadSexp();
          System.out.print( "> " );
        } // tru
        catch ( NoclosingQuoteError e ) {
          System.out.println( e.Get_Msg() );
          MyScanner.Get_Instance().ErrorReset();
        } // catch
        catch ( UnexpectedError e ) {
          System.out.println( e.Get_Msg() );
          MyScanner.Get_Instance().ErrorReset();
        }
        // catch
      } // while
      
    } // try
    catch ( EOFEncounterError e ) {
      System.out.println( e.Get_Msg() );
      System.out.println( "Thanks for using OurScheme!" );
      
    } // catch
    catch ( FinishProgramException e ) {
      
      System.out.println( "Thanks for using OurScheme!" );
      
    } // catch
    
  } // main()
  
} // class Main
