package dbHelper;

import java.sql.SQLException;
import java.util.ArrayList;

public class Sample {

	public Sample() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
				
		
		DBCon mycon =new DBCon();
		ConfigLoader myconf=new ConfigLoader();
		ArrayList<String> conflist=new ArrayList<String>();
		try {
			conflist=myconf.getsArrLConfigItems();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		//mycon.setConnection("localhost/testDB", "ich", "nichtmeinpasswort");
		mycon.setConnection(conflist.get(0).toString(), 
				conflist.get(1).toString(), 
				conflist.get(2).toString());
		try{
			mycon.connect();
			System.out.println("Datenbankverbindung erfolgreich");
		}catch (SQLException ex){
			System.out.println("Datenbankverbindung nicht erfolgreich:");
			while (ex!=null){
				ex.printStackTrace();
				ex.getNextException();
			}
		}
		
		try {
			for (String Tables:mycon.getTables()){
				System.out.println("Tabelle: "+Tables+" :");
				try{
					for (String Columns:mycon.getColumns(Tables)){
						System.out.println("-> Spalte: "+Columns+" :");
						try{
							for (String Content:mycon.getContentOfColumn(Tables, Columns)){
								System.out.println("-> -> Content: "+Content+" :");
							}
						}catch (Exception e) {
							System.out.println("Fehler auf Spaltenebene");
							e.printStackTrace();					
						}
					}
				}catch (Exception e) {
					System.out.println("Fehler auf Spaltenebene");
					e.printStackTrace();					
				}
			}
		} catch (Exception e) {
			System.out.println("Fehler auf Tabellenebene");
			e.printStackTrace();
		}	
		String[] Cols={"titel", "autor", "signature"};
		String[] Vals={"PRINCE", "McAutor", "000010"};
		try {
			mycon.insertDataSet("buch", Cols, Vals);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("");
		try {
			String [][] sDBTable=mycon.getContentOfTable("buch", true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		try {
			String[][] t ={{"signature", "7249"}, {"autor", "ich"}};
			//String[][] s ={{"autor", "ich"},{"titel", "bin toll"}};
			String[][] s ={{"id", "30"}};
			System.out.println("Es wurden " + mycon.updateDataSet("buch", t, s) +" Datensätze geändert.");
			
			//String[][] sDBDataSet1=mycon.getDataSets("buch", t, s, false, true);
			//String[][] sDBDataSet2=mycon.getDataSets("SELECT * FROM buch", true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
		
		String[][] s={{"autor", "mcautor"}};
		try {
			//System.out.println("Es wurden " + mycon.deleteDataSet("buch", s) + " Datensätze gelöscht.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//mycon.test("test");
		
	}
	
	

}
