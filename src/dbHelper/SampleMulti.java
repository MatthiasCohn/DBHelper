/**
 * 
 */
package dbHelper;

import java.sql.SQLException;
import java.util.ArrayList;

import dbHelper.ConfigLoader;
import dbHelper.DBCon;

/**
 * @author Ich
 *
 */
public class SampleMulti {

	static dbHelper.ConfigLoader dbConfStatus, dbConfHD;
	static dbHelper.DBCon dbStatus, dbHD;
	static ArrayList<String> AL = new ArrayList<String>();
	/**
	 * 
	 */
	public SampleMulti() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/**
		 * Step-by-Step:
		 */
		dbConfStatus=loadDBsConf("user");
		dbStatus=dbConSetup(dbConfStatus);
		System.out.println("Verbunden mit Status: "+dbConnect(true, dbStatus));

		//dbConfHD=loadDBsConf("admin");
		//dbHD=dbConSetup(dbConfHD);
		
		/**
		 * admin - ist der Dateiname  (ohne Endung)
		 * Wenn kein korrekter Pfad übergeben wird, wird Dateiöffnen-Dialog angezeigt
		 * Beispiel:
		 */
		dbConfHD=loadDBsConf("admin");
		dbHD=dbConSetup(dbConfHD);
		System.out.println("Verbunden mit Status: "+dbConnect(true, dbHD));
		
		
		/**
		 * Kurzform:
		 * !!! Theoretisch - wirft aber Fehler 
		 * !!! Grund Unknown
		 */
		//System.out.println("Verbunden mit HD: "+dbConnect(true, dbConSetup(loadDBsConf("admin"))));

		
		/**
		 * Ausgabebeispiel:
		 */
		System.out.println("Datenbank Status: ");
		
		printTables(dbStatus);
		System.out.println("Datenbank HelpDesk: ");
		printTables(dbHD);		
		
		try {
			dbStatus.disconnect();
			dbHD.disconnect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static ConfigLoader loadDBsConf(String DBConfFile) {
		// https://www.java-forum.org/thema/aktuellen-pfad-der-anwendung-ermitteln.21044/
		dbHelper.ConfigLoader dbConf = new ConfigLoader();
		String working_dir = System.getProperty("user.dir");
		working_dir += "\\" + DBConfFile + ".cfg"; // preselect file
		try {
			dbConf.setsFilePath(working_dir);
		} catch (Exception e) {
			dbConf.clearConfig(false);
			try {
				dbConf.setsFilePath("");
			} catch (Exception e1) {
				System.out.println("Es konnte keine Config-File eingerichtet werden.");
				e1.printStackTrace();
			}
			System.out.println("Es wurde kein passendes Config-File gefunden ... neues wählen");
			e.printStackTrace();
		}
		return dbConf;
		// System.out.println(working_dir);
		// dbConnect(true);
	}

	public static DBCon dbConSetup(ConfigLoader dbConf) {
		DBCon dbCon = new DBCon();
		AL=null;
			try {
				AL = dbConf.getsArrLConfigItems();
				switch (AL.size()) {
				case 3:
					dbCon.setConnection(AL.get(0), AL.get(1), AL.get(2));
					break;
				case 4:
					dbCon.setConnection(AL.get(0), AL.get(1), AL.get(2), AL.get(3));
					break;
				default:
					throw new Exception("Datenbankkonfiguration unvollständig.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Konfiguration der Datenbankverbindung nicht möglich");
				dbConf.clearConfig(false);
				return null;
			}
		return dbCon;
	}
	
	public static boolean dbConnect(boolean bDoConnect, DBCon dbCon) {
		if (bDoConnect == true) {
			try {
				dbCon.connect();
				System.out.println("Verbunden mit DB");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Datenbankverbindung nicht möglich");
				return false;
			}
		} else {
			try {
				dbCon.disconnect();
				System.out.println("Datenbankverbindung erfolgreich getrennt");
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Trennen der Datenbankverbindung nicht möglich");
				return true;
			}
		}
		return dbCon.isConnected();
	}
	
	private static void printTables(DBCon dbCon){
		ArrayList<String> ArrLTables=new ArrayList<String>();
		try {
			ArrLTables=dbCon.getTablesList();
		} catch (Exception e) {
			ArrLTables=null;
			e.printStackTrace();
		}
		if(ArrLTables!=null){
		for (String s:ArrLTables){
			System.out.println(" - Tabelle "+s+ " : ");
			printColumns(dbCon, s);
		}
		}else{
			System.out.println("Keine Tabellen in der Datenbank");
		}
	}
	
	private static void printColumns(DBCon dbCon, String sTable){
		ArrayList<String> ArrLCols=new ArrayList<String>();
		try {
			ArrLCols=dbCon.getColumnsList(sTable);
		} catch (Exception e) {
			ArrLCols=null;
			e.printStackTrace();
		}
		for (String c:ArrLCols){
			System.out.println(" - - "+c);
		}
	}
}
