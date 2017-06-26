/**
 * @author Matthias Cohn
 * @version 1.1.5 (2017-06-26)
 *
 * Liest Datenbankverbindungskonfigurationen aus .cfg-Files (Configloader)
 * 
 * Strukurbeispiel:
 * -------------------------------------------------
 * Das Datenbankpasswort des Datenbanknutzers:
 * PSW:[Passwort]
 *
 * Der Pfad (URL) zum Server/zu DB
 * Beginnend bei IP/Hostname / DBName
 * Bsp:localhost/bibliothek
 * URL:[URL]
 * 
 * Der Nutzername
 * ACC:[Account]
 *
 * Datenbankname (optional)
 * DBN:[Schema-Name]
 * ----------------------------------------------------
 * 
 * Stellt die Datenbankverbindung und wesentliche Funktionen mit Rückgabewerten
 * in Array und ArrayList als Objekt dar
 *
 *Version DBCon: 1.1.5 (2017-06-26)
 *Version ConfigLoader: 1.1.5 (2017-06-36)
 */
package dbHelper;