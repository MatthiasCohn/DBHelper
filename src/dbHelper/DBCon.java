/**
 * 
 */
package dbHelper;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.*;
import java.util.*;

/**
 * Diese Klasse stellt eine Verbindung zur Datenbank her und <br>
 * ermöglicht die Verwaltung von Datensätzen.<br>
 * <b> Hinweis: Eine Datentypprüfung für Update und Insert sind NICHT vorhanden!
 * <b><br>
 * 
 * @author Cohn, Matthias (77210-565998)
 * @version 1.1.0, 2017-06-07
 */
public class DBCon {
	private Connection con;
	private MysqlDataSource mds;

	public DBCon() {

		mds=new MysqlDataSource();
	}

//	public static Connection getCon() {
		//return con;
//	}

	/**
	 * Verbindungsparameter zur DB festlegen
	 * 
	 * @param sURL,
	 *            String: URL zum DB-Server/DB (beginnend bei IP/Hostname
	 * @param sUser,
	 *            String: Name des DB-Nutzers
	 * @param sPsw,
	 *            String: Passwort (Klartext) des DB-Nutzers
	 */
	public void setConnection(String sURL, String sUser, String sPsw) {
		setConnection(sURL, sUser, sPsw, null);
	}

	/**
	 * * Verbindungsparameter zur DB festlegen (externer DB's)
	 * 
	 * @param sURL,
	 *            String: URL zum DB-Server/DB (beginnend bei IP/Hostname
	 * @param sUser,
	 *            String: Name des DB-Nutzers
	 * @param sPsw,
	 *            String: Passwort (Klartext) des DB-Nutzers
	 * @param sDB,
	 *            String: Name der Datenbank
	 */
	public void setConnection(String sURL, String sUser, String sPsw, String sDB) {
		mds.setURL("jdbc:mysql://" + sURL);
		mds.setUser(sUser);
		mds.setPassword(sPsw);
		if (sDB != null)
			mds.setDatabaseName(sDB);
	}

	/**
	 * Verbindung herstellen. <br>
	 * Vorher müssen mit {@code setConnection} die Parameter festgelegt werden.
	 * 
	 * @throws SQLException
	 *             : Wenn verbindung nicht erfolgreich
	 */
	public void connect() throws SQLException {
		try {
			con = mds.getConnection();
			if (mds.getDatabaseName() != null && mds.getDatabaseName() != "") {
				String schema = mds.getDatabaseName();
				con.setSchema(schema);
			}
			//System.out.println(con.getSchema());
			// return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			// return false;
		}
	}

	/**
	 * Verbindung zur DB trennen
	 * 
	 * @throws SQLException
	 *             : Wenn aufheben der DB-Verbindung nicht möglich
	 */
	public void disconnect() throws SQLException {
		try {
			con.commit();
			con.clearWarnings();
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Prüft den Verbinungsstatus zur DB
	 * 
	 * @return Boolean: Status der DB-Verbindung
	 */
	public boolean isConnected() {
		boolean connected = false;
		try {
			connected = !con.isClosed();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connected;
	}

	/**
	 * Gibt die Namen aller, in der DB vorhandenen, Tabellen zurück
	 * 
	 * @return String[] Namen aller, in der DB vorhandenen, Tabellen
	 * @throws Exception
	 */
	public String[] getTables() throws Exception {
		ArrayList<String> sTableInf = getTablesList();
		try {
			return sTableInf.toArray(new String[sTableInf.size()]);
		} catch (Exception ex) {
			throw new Exception("Fehler bei der Umwandlung (List ->Array)");
		}
	}

	/**
	 * Gibt die Namen aller, in der DB vorhandenen, Tabellen zurück
	 * 
	 * @return ArrayList <String> : Namen aller, in der DB vorhandenen, Tabellen
	 * @throws Exception
	 */
	public ArrayList<String> getTablesList() throws Exception {
		ArrayList<String> sTableInf = new ArrayList<String>();
		String sQuery = "SHOW TABLES";
		Statement stm = con.createStatement();
		try {
			ResultSet rs = stm.executeQuery(sQuery);
			if (rs != null) {
				rs.beforeFirst();
				while (rs.next()) {
					sTableInf.add(rs.getString(1));
				}
				return sTableInf;
			} else {
				throw new Exception("Keine Tabellen in Datenbank vorhanden.");
			}
		} catch (SQLException ex) {
			throw new Exception("Fehler bei der SQL-Abfrage. \n" + sQuery);
		}

	}

	/**
	 * Gibt die Namen aller, in der Tabelle vorhandenen, Tabellenspalten zurück
	 * 
	 * @return String[] Namen aller, in der Tabelle vorhandenen, Tabellenspalten
	 * @param sTableName,
	 *            String: Tabellenname
	 * @throws Exception
	 */
	public String[] getColumns(String sTableName) throws Exception {
		ArrayList<String> sTableInf = getColumnsList(sTableName);
		try {
			return sTableInf.toArray(new String[sTableInf.size()]);
		} catch (Exception ex) {
			throw new Exception("Fehler bei der Umwandlung (List ->Array)");
		}
	}

	/**
	 * Gibt die Namen aller, in der Tabelle vorhandenen, Tabellenspalten zurück
	 * 
	 * @return ArrayList<String> Namen aller, in der Tabelle vorhandenen,
	 *         Tabellenspalten
	 * @param sTableName,
	 *            String: Tabellenname
	 * @throws Exception
	 */
	public ArrayList<String> getColumnsList(String sTableName) throws Exception {
		ArrayList<String> sTableInf = new ArrayList<String>();
		Statement stm = con.createStatement();
		String sQuery = "SHOW COLUMNS FROM " + sTableName;
		try {
			ResultSet rs = stm.executeQuery(sQuery);
			if (rs != null) {
				while (rs.next()) {
					sTableInf.add(rs.getString(1));
				}
				return sTableInf;
			} else {
				throw new Exception("Keine Tabellenspalten vorhanden.");
			}
		} catch (SQLException ex) {
			throw new Exception("Fehler bei der SQL-Abfrage. \n" + sQuery);
		}
	}

	/**
	 * Gibt den Inhalt aller, in der Tabellenspalte vorhandenen, Zellen zurück
	 * 
	 * @param sTable
	 *            , String : Tabellenname
	 * @param sColName
	 *            , String : Spaltenname
	 * @return String[] Inhalt aller, in der Tabellenspalte vorhandenen, Zellen
	 */
	public String[] getContentOfColumn(String sTable, String sColName) throws Exception {
		ArrayList<String> sTableInf = getContentOfColumnList(sTable, sColName);
		// http://stackoverflow.com/questions/5374311/convert-arrayliststring-to-string-array
		try {
			return sTableInf.toArray(new String[sTableInf.size()]);
		} catch (Exception ex) {
			throw new Exception("Fehler bei der Umwandlung (List ->Array)");
		}
	}

	/**
	 * Gibt den Inhalt aller, in der Tabellenspalte vorhandenen, Zellen zurück
	 * 
	 * @param sTable
	 *            , String : Tabellenname
	 * @param sColName
	 *            , String : Spaltenname
	 * @return ArrayList<String> Inhalt aller, in der Tabellenspalte
	 *         vorhandenen, Zellen
	 * @throws Exception
	 */
	public ArrayList<String> getContentOfColumnList(String sTable, String sColName) throws Exception {
		ArrayList<String> sTableInf = new ArrayList<String>();
		Statement stm = con.createStatement();
		String sQuery = "SELECT " + sColName + " FROM " + sTable;
		try {
			ResultSet rs = stm.executeQuery(sQuery);
			if (rs != null) {
				while (rs.next()) {
					sTableInf.add(rs.getString(1));
				}
				return sTableInf;
			} else {
				throw new Exception("Kein Inhalt in Spalten.");
			}
		} catch (SQLException ex) {
			throw new Exception("Fehler bei der SQL-Abfrage. \n" + sQuery);
		}
	}

	/**
	 * 
	 * @param sTable
	 *            Name der Tabelle, deren Inhalt zurückgegeben werden soll
	 * @param bHeadline
	 *            Spaltenname wird mit übergeben
	 * @return String[][] Inhalt der übergebenen Tabelle
	 * @throws Exception
	 */
	public String[][] getContentOfTable(String sTable, boolean bHeadline) throws Exception {
		String[] sArrCols = getColumns(sTable);
		String[] sArrRow = getContentOfColumn(sTable, sArrCols[0]);
		String[][] sArrContent;
		if (bHeadline) {
			sArrContent = new String[sArrCols.length][sArrRow.length + 1];
		} else {
			sArrContent = new String[sArrCols.length][sArrRow.length];
		}

		for (int m = 0; m <= sArrCols.length - 1; m++) {
			sArrRow = getContentOfColumn(sTable, sArrCols[m].toString()); // Aktuelle
																			// Spalte
																			// einlesen
			if (bHeadline)
				sArrContent[m][0] = sArrCols[m].toString();
			for (int n = 0; n <= sArrRow.length - 1; n++) {
				if (sArrRow[n] == null)
					sArrRow[n] = ""; // Vorsorge gegen NULL-Einträge in der DB
				if (bHeadline) {
					sArrContent[m][n + 1] = sArrRow[n].toString();
				} else {
					sArrContent[m][n] = sArrRow[n].toString();
				}
			}
		}
		return sArrContent;
	}

	/**
	 * Gibt Ergebnisse einfacher Abfragen zurück
	 * 
	 * @param sTable
	 *            String, Name der Tabelle
	 * @param sCols
	 *            String[], Name(n) der zu liefernden Spalten Wenn NULL, dann
	 *            werden alle (*) selektiert
	 * @param sCriteria
	 *            String[][], String{{[Name der Spalte],[Kriterium]}, ...} Wenn
	 *            NULL, dann wird keine WHERE-Clause erzeugt
	 * @param bSelection
	 *            Relation zw. Kriterien (true= AND; false= OR)
	 * @param bHeadline
	 *            Spaltenname wird mit übergeben
	 * @return String[][] Inhalt des Ergebnisses der Abfrage
	 * @throws Exception
	 */
	public String[][] getDataSets(String sTable, String[] sCols, String[][] sCriteria, boolean bSelection,
			boolean bHeadline) throws Exception {
		String sQuery = "";
		if (sCols == null) {
			sQuery = "SELECT * FROM " + sTable + ArrToSQLString(sCriteria, true, bSelection);
		} else {
			sQuery = "SELECT " + ArrToSQLString(sCols, false) + " FROM " + sTable
					+ ArrToSQLString(sCriteria, true, bSelection);
		}

		return getDataSets(sQuery, bHeadline);
	}

	/**
	 * Gibt Ergebnisse einfacher Abfragen zurück
	 * 
	 * @param sQuery
	 *            , String: SQL-Konforme "Select ..." - Abfrage
	 * @param bHeadline,
	 *            Spaltenname wird mit übergeben
	 * @return String[][] Inhalt des Ergebnisses der Abfrage
	 * @throws Exception
	 */
	public String[][] getDataSets(String sQuery, boolean bHeadline) throws Exception {
		int iColCount = 0, iRowCount = 0;
		String[][] sArrContent;
		Statement stm = con.createStatement();
		ResultSet rs;

		try {
			rs = stm.executeQuery(sQuery);
		} catch (SQLException ex) {
			throw new Exception("Fehler bei der SQL-Abfrage. \n" + sQuery);
		}

		if (rs != null) {
			iColCount = rs.getMetaData().getColumnCount();
			while (rs.next())
				iRowCount++;
			rs.beforeFirst();
			if (bHeadline) {
				iRowCount++;
			}
			sArrContent = new String[iColCount][iRowCount];
			int m = 0, n = 0;

			if (bHeadline) {
				for (m = 0; m <= iColCount - 1; m++) {
					sArrContent[m][n] = rs.getMetaData().getColumnLabel(m + 1);
				}
			}
			while (rs.next()) {
				for (m = 0; m <= iColCount - 1; m++) {
					String s = "";
					if (rs.getString(m + 1) == null)
						s = "";
					else
						s = rs.getString(m + 1);

					if (bHeadline) {
						sArrContent[m][n + 1] = s;
					} else {
						sArrContent[m][n] = s;
					}
				}
				n++;
			}
		} else {
			sArrContent = null;
		}
		return sArrContent;
	}

	/**
	 * Anfügen eines neuen Datensatzes in einer Tabelle
	 * 
	 * @param Table,
	 *            String - Name der Tabelle
	 * @param ColNames
	 *            String[] - Namen der Spalten
	 * @param Content
	 *            String[] - Inhalt zu Spalten
	 * @return Integer: Anzahl der ausgeführten Reihen (Erfolgreiches Einfügen:=
	 *         >0)
	 * @throws Exception
	 */
	public int insertDataSet(String Table, String[] ColNames, String[] Content) throws Exception {
		String sQuery = "INSERT INTO  " + Table + "  (" + ArrToSQLString(ColNames, false) + ") VALUES ("
				+ ArrToSQLString(Content, true) + ");";
		return insertDataSet(sQuery);
	}

	/**
	 * Anfügen eines neuen Datensatzes in einer Tabelle
	 * 
	 * @param sQuery
	 *            , String: SQL-Konforme "Insert Into ..." - Abfrage
	 * @return Integer: Anzahl der ausgeführten Reihen (Erfolgreiches Einfügen:=
	 *         >0
	 * @throws Exception
	 */
	public int insertDataSet(String sQuery) throws Exception {
		Statement stm = con.createStatement();
		int iExecutedRows = 0;
		try {
			iExecutedRows = stm.executeUpdate(sQuery);
			iExecutedRows++;
		} catch (SQLException ex) {
			throw new Exception("Fehler bei der SQL-Abfrage. \n" + sQuery);
		}
		return iExecutedRows;
	}

	/**
	 * Löschen eines oder mehrerer Datensätze
	 * 
	 * @param sTable,
	 *            String: Tabellenname
	 * @param sCriteria,
	 *            String[][]: String{{[Name der Spalte],[Kriterium]}, ...}
	 *            WHERE-CLAUSE
	 * @return Integer: Anzahl der ausgeführten Reihen (Erfolgreiches Einfügen:=
	 *         >0
	 * @throws Exception
	 */
	public int deleteDataSet(String sTable, String[][] sCriteria) throws Exception {
		String sQuery = "SELECT * FROM " + sTable + ArrToSQLString(sCriteria, true, true);
		return deleteDataSet(sQuery);
	}

	/**
	 * Löschen eines oder mehrerer Datensätze
	 * 
	 * @param sQuery,
	 *            String: SQL-Konforme "DELETE ..." - Abfrage
	 * @return Integer: Anzahl der ausgeführten Reihen (Erfolgreiches Einfügen:=
	 *         >0
	 * @throws Exception
	 */
	public int deleteDataSet(String sQuery) throws Exception {
		// https://www.dpunkt.de/java/Programmieren_mit_Java/Java_Database_Connectivity/32.html
		Statement stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		int iExecutedRows = 0;
		ResultSet rs;
		// + ArrToSQLString(sCriteria, bSelection)
		try {
			rs = stm.executeQuery(sQuery);
			// iExecutedRows=stm.executeUpdate(sQuery);
		} catch (SQLException ex) {
			throw new Exception("Fehler bei der SQL-Abfrage. \n" + sQuery);
		}
		try {
			while (rs.next()) {
				rs.deleteRow();
				iExecutedRows++;
			}
		} catch (SQLException ex) {
			throw new Exception("Fehler beim löschen");
		}
		return iExecutedRows;
	}

	/**
	 * Aktualisieren eines oder mehrerer Datensätze
	 * 
	 * @param Table,
	 *            String: Tabellenname
	 * @param sColContent
	 *            , String[][]: String{{[Name der Spalte],[neuer Wert]}, ...}
	 *            SET-Clause
	 * @param sCriteria,
	 *            String[][]: String{{[Name der Spalte],[Kriterium]}, ...}
	 *            WHERE-CLAUSE
	 * @return Integer: Anzahl der ausgeführten Reihen (Erfolgreiches Einfügen:=
	 *         >0
	 * @throws Exception
	 */
	public int updateDataSet(String Table, String[][] sColContent, String[][] sCriteria) throws Exception {
		String sQuery = "UPDATE " + Table + "  SET " + ArrToSQLString(sColContent, false, false)
				+ ArrToSQLString(sCriteria, true, true);
		return updateDataSet(sQuery);
	}

	/**
	 * Aktualisieren eines oder mehrerer Datensätze
	 * 
	 * @param sQuery,
	 *            String: SQL-Konforme "UPDATE ..." - Abfrage
	 * @return Integer: Anzahl der ausgeführten Reihen (Erfolgreiches Einfügen:=
	 *         >0
	 * @throws Exception
	 */
	public int updateDataSet(String sQuery) throws Exception {
		Statement stm = con.createStatement();
		int iExecutedRows = 0;
		try {
			iExecutedRows = stm.executeUpdate(sQuery);
		} catch (SQLException ex) {
			throw new Exception("Fehler bei der SQL-Abfrage. \n" + sQuery);
		}
		return iExecutedRows;
	}

	/**
	 * SQL-Konforme Aufbereitung der Arrays zu String
	 * 
	 * @param sArr
	 *            String[] Spaltennamen
	 * @param bContent,
	 *            true= Inhalt; false=Spaltennamen
	 * @return String: Spaltennamen (Select-Clause)
	 */
	private String ArrToSQLString(String[] sArr, boolean bContent) {
		String s = "";
		int count = sArr.length - 1;
		for (String sPart : sArr) {
			if (bContent)
				s += "'" + sPart + "'";
			else
				s += "`" + sPart + "`";
			if (count > 0) {
				s += ", ";
			}
			count--;
		}
		;
		return s;
	}

	/**
	 * SQL-Konforme Aufbereitung der Arrays zu String
	 * 
	 * @param sCriteria
	 *            String[][]: String{{[Name der Spalte],[Kriterium]}, ...}
	 *            WHERE-CLAUSE
	 * @param bSelection
	 *            Relation zw. Kriterien (true= AND; false= OR)
	 * @param bWhere
	 *            Es handelt sich um eine WHERE-Clause
	 * @return String, Condition: SET oder WHERE-Clause
	 */
	private static String ArrToSQLString(String[][] sCriteria, boolean bWhere, boolean bSelection) {
		if (sCriteria == null)
			return "";
		else {
			String sSelection = " OR ";
			if (bSelection)
				sSelection = " AND ";
			String sCondition = "";
			if (sCriteria.length > 0) {
				if (bWhere)
					sCondition = " WHERE ";
				// int a =sCriteria.length;
				for (int m = 0; m <= sCriteria.length - 1; m++) {
					// int b= sCriteria[m].length;
					for (int n = 0; n <= sCriteria[m].length - 1; n++) {
						if (n == 0)
							sCondition += "`" + sCriteria[m][n].toString() + "`";
						else
							sCondition += "'" + sCriteria[m][n].toString() + "'";
						if (n != sCriteria[m].length - 1)
							sCondition += " = ";
					}
					if (m != sCriteria.length - 1)
						if (bWhere)
							sCondition += sSelection;
						else
							sCondition += ", ";
				}
			}
			return sCondition;
		}
	}

	// Zum testen ob wert in Spalte eingefügt werden kann
	// Nicht fertig!!!!
	/*
	 * public void test(String sTable) { String sQuery = "SELECT * FROM " +
	 * sTable;
	 * 
	 * try { Statement stm = con.createStatement(); ResultSet rs =
	 * stm.executeQuery(sQuery);
	 * 
	 * int iColCount = rs.getMetaData().getColumnCount(); for (int i = 1; i <=
	 * iColCount; i++) { isConvertableValue(rs, i); }
	 * 
	 * } catch (SQLException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * private boolean isConvertableValue(ResultSet rs, int iCol) { try {
	 * System.out.print(rs.getMetaData().getColumnLabel(iCol) + ": ");
	 * System.out.print(rs.getMetaData().getColumnTypeName(iCol) + ": ");
	 * System.out.println(rs.getMetaData().getColumnType(iCol)); } catch
	 * (SQLException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * return true; }
	 */
}