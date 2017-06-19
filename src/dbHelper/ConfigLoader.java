/**
 * 
 */
package dbHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Cohn, Matthias (77210-565998)
 * @version 1.1.3 - 2017-06-19
 * Ermöglicht das öffnen einer strukturierten Datei, welche <br>
 * Informationen für eine DB-Verbindung enthält, sowie die sicherer <br>
 * Rückgabe der Informationen URL, Account und Passwort (in exakt dieser Reihenfolge)<br>
 * als ArrayList.<p></p>
 * 
 * Die Datei muss 3 Zeilen beginnend mit (ohne ' ' ):<br>
 * <ul>
 * <li>'URL:'</li>
 * <li>'ACC:'</li>
 * <li>'PSW:'</li>
 * </ul>
 * enthalten. <br>
 * Sofern kein Dateipfad mit {@link #setsFilePath(String)} übergeben wird, <br>
 * öffnet sich eine Datei-öffnen-Dialog, mit der Einschränkung auf .cfg-Dateien.<br>
 * <br>
 * Die schnellste Implementation erfolgt mit {@link #getsArrLConfigItems()}: <br>
 * hierbei wird der Datei-öffnen-Dialog aufgerufen, sowie alle weiteren zwischenschritte automatisiert.
 * 
 */
public class ConfigLoader {
	private static ArrayList<String> sArrLConfigItems = new ArrayList<String>();
	private static String sFilePath;

	/**
	 * 
	 */
	public ConfigLoader() {

		sFilePath = null;
	}

	/**
	 * Gibt den Pfad der gewählten Konfigurationsdatei zurück
	 * @return String: Pfad der gewählten Konfigurationsdatei
	 * @throws Exception
	 */
	public String getsFilePath() throws Exception {
		if (sFilePath == "" || sFilePath == null) {
			sFilePath=null;
			try {
				setsFilePath("");
			} catch (Exception e) {
				throw new Exception("Kann Datei, bei angegebenem Pfad, nicht selektieren.");
			}
		}
		return sFilePath;
	}
	
	/**
	 * Manuelles setzen des Pfades einer Configurationsdatei
	 * @param sPath  Pfades einer Configurationsdatei
	 * @throws Exception
	 */
	public void setsFilePath(String sPath) throws Exception {
		
		if (sFilePath == null && sPath == "") {
			// https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
			JFileChooser fcFile = new JFileChooser();
			fcFile.setCurrentDirectory(new File(System.getProperty("user.dir")));
			fcFile.getCurrentDirectory();
			System.out.println(fcFile.getCurrentDirectory().getAbsolutePath());
			//fcFile.getCurrentDirectory();
			FileFilter ff = new FileNameExtensionFilter("Configurations-Datei", "cfg");
			fcFile.setFileFilter(ff);
			int returnVal = fcFile.showOpenDialog(null);
			fcFile.setDialogType(JFileChooser.OPEN_DIALOG);
			fcFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File fFile = fcFile.getSelectedFile();
				sFilePath = fFile.getPath().toString();
				// This is where a real application would open the file.
				sFilePath = sFilePath.replaceAll("\\\\", "\\\\\\\\");
				// http://stackoverflow.com/questions/13696461/replace-special-character-with-an-escape-preceded-special-character-in-java
				// System.out.println("Opening: " + sFilePath);
			} else {
				sFilePath = null;
			}
		}
		if (sPath!=""){
			sFilePath=sPath;
		}
		//System.out.println(sFilePath);
		try { // ausgewählter Pfad wird getestet
			FileReader frFile = null;
			frFile = new FileReader(sFilePath); // String mit Pfad
			if (frFile.ready())
				frFile.close();
			// System.out.println("Datei ("+ sFilePath+" bereit.");
		} catch (FileNotFoundException e) {
			sFilePath = null;
			e.printStackTrace();
			throw new Exception("Kann Datei, bei angegebenem Pfad, nicht selektieren.");
		}
	}
	

	/**
	 * Setzt die Einstellungen und Dateipfad auf Null zurück
	 * @param bItemsOnly, Wahr: nur KonfigurationsItems werden gelöscht, False: Dateipfad wird auch gelöscht
	 */
	public void clearConfig(boolean bItemsOnly){
		sArrLConfigItems.clear();
		if (bItemsOnly==false) sFilePath="";
	}
	
	

	/**
	 * Übergibt die für die Datenbankverbindung relevanten informationen zurück
	 * @return ArrayList <String> : 0- URL, 1-User, 2-PSW
	 * @throws Exception
	 */
	public ArrayList<String> getsArrLConfigItems() throws Exception {
		if (sArrLConfigItems == null || sArrLConfigItems.size() < 3) {
			sArrLConfigItems = null;
			try {
				setsArrLConfigItems();
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("Konfiguration konnte nicht geladen werden.");
			}
		}
		return sArrLConfigItems;
	}

	/**
	 * legt die für die Datenbankverbindung relevanten Informationen an
	 * @throws Exception
	 */
	private void setsArrLConfigItems() throws Exception {
		getsFilePath();
		try {
			fReadIn();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Konfigurationsdatei konnte nicht eingelesen werden.");
		}
	}

	/**
	 * (Konfigurations-)datei wird zeilenweise eingelesen 
	 * @throws Exception
	 */
	private static void fReadIn() throws Exception {
		// Quelle:
		// http://www.computer-masters.de/java-datei-zeilenweise-einlesen-bufferedreader.php
		ArrayList<String> ArrLBuffer = new ArrayList<String>();
		FileReader frFile = null;
		try {
			frFile = new FileReader(sFilePath); // String mit Pfad
			BufferedReader brPuffer = new BufferedReader(frFile);
			String sLine = "";
			try {
				while (null != (sLine = brPuffer.readLine())) {
					ArrLBuffer.add(sLine);
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new Exception("Datei kann nicht geöffnet werden.");
			} finally {
				if (null != brPuffer) {
					try {
						brPuffer.close();
					} catch (IOException e) {
						e.printStackTrace();
						throw new Exception("Datenpuffer kann nicht aufgehoben werden.");
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Exception("Kann Datei, bei angegebenem Pfad, nicht selektieren.");
		}
		if (ArrLBuffer != null)
			prepArrL(ArrLBuffer);
	}
	/**
	 * Durchsucht eine ArrayList auf Verbinungskeywords und überträgt diese in die Objekt ArrayList sArrLConfigItems<br>
	 * Reihenfolge: URL, ACCount, PSW und Datenbankname
	 * @param ArrLBuffer
	 */
	private static void prepArrL(ArrayList<String> ArrLBuffer) {
		ArrayList<String> ArrLVal = new ArrayList<String>();

		ArrLVal.add(sFindValue(ArrLBuffer, "URL:"));
		ArrLVal.add(sFindValue(ArrLBuffer, "ACC:"));
		ArrLVal.add(sFindValue(ArrLBuffer, "PSW:"));
		ArrLVal.add(sFindValue(ArrLBuffer, "DBN:"));
		if(ArrLVal.get(3)=="") ArrLVal.remove(3);
		sArrLConfigItems = ArrLVal;
	}
	
	/**
	 * Durchsucht den Inhalt von ArrayLists auf das vorkommen eines bestimmten Strings und gibt diesen zurück
	 * @param ArrLBuffer, ArrayList <String>: ArrayList mit Text und Konfiguratioen
	 * @param sCriteria, String - Suchkriterium
	 * @return
	 */
	private static String sFindValue(ArrayList<String> ArrLBuffer, String sCriteria) {
		String sVal = "";
		// https://stackoverflow.com/questions/6428005/check-if-arrayliststring-contains-part-of-a-string
		try {
			sVal = ArrLBuffer.get(ArrLBuffer.indexOf(new Object() {
				@Override
				public boolean equals(Object obj) {
					return obj.toString().contains(sCriteria);
				}
			}));
			sVal = sVal.substring(sVal.indexOf(":") + 1);
		} catch (Exception e) {
			return "";
		}
		return sVal;
	}

}