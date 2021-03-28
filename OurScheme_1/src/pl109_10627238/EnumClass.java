package pl109_10627238;

public class EnumClass {
  
} // class EnumClass

class Symbol {
  public String mTypeString;
  
  public Symbol( String type ) {
    mTypeString = type;
  } // Symbol
  
  public static Symbol sL_PAREN = new Symbol( "sL_PAREN" );
  public static Symbol sR_PAREN = new Symbol( "sR_PAREN" );
  public static Symbol sINT = new Symbol( "sINT" );
  public static Symbol sSTRING = new Symbol( "sSTRING" );
  public static Symbol sDOT = new Symbol( "sDOT" );
  public static Symbol sFLOAT = new Symbol( "sFLOAT" );
  public static Symbol sNIL = new Symbol( "sNIL" );
  public static Symbol sT = new Symbol( "sT" );
  public static Symbol sQUOTE = new Symbol( "sQUOTE" );
  public static Symbol sSYMBOL = new Symbol( "sSYMBOL" );
} // class Symbol
