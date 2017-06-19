package dbHelper;

import java.util.ArrayList;

public class Sample {
	static DBCon dbHD =new DBCon();
	static DBCon dbStatus=new DBCon();
	static ArrayList<String> ArrLDBConf=new ArrayList<String>();
	static ConfigLoader dbConf =new ConfigLoader();
	
	public Sample() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		try {
			
			dbConf.clearConfig(false);
			ArrLDBConf=dbConf.getsArrLConfigItems();
			if (ArrLDBConf.size()==3){
			dbHD.setConnection(ArrLDBConf.get(0),ArrLDBConf.get(1), ArrLDBConf.get(2));
			}
			if(ArrLDBConf.size()==4){
				dbHD.setConnection(ArrLDBConf.get(0),ArrLDBConf.get(1), 
						ArrLDBConf.get(2),ArrLDBConf.get(3));
			}
			dbHD.connect();
			System.out.println("Verbunden mit HD");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrLDBConf.clear();
		dbConf.clearConfig(false);
		try {

			ArrLDBConf=dbConf.getsArrLConfigItems();
			if (ArrLDBConf.size()==3){
			dbStatus.setConnection(ArrLDBConf.get(0),ArrLDBConf.get(1), ArrLDBConf.get(2));
			}
			if(ArrLDBConf.size()==4){
				dbStatus.setConnection(ArrLDBConf.get(0),ArrLDBConf.get(1), 
						ArrLDBConf.get(2),ArrLDBConf.get(3));
			}
			dbStatus.connect();
			System.out.println("Verbunden mit Status");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		try {
			dbConf.setsFilePath("");
			ArrLDBConf=dbConf.getsArrLConfigItems();
			dbStatus.setConnection(ArrLDBConf.get(0),ArrLDBConf.get(1), ArrLDBConf.get(2));			
			dbStatus.connect();
			System.out.println("Verbunden mit Status");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		String content[] = null;
		try {
			
			if (dbHD.isConnected()==false)
				dbHD.connect();
			content = dbHD.getTables();
			for (int i = 0; i <= content.length - 1; i++) {
				System.out.println(content[i]);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		content=null;
		try {
			content = dbStatus.getTables();
			for (int i = 0; i <= content.length - 1; i++) {
				System.out.println(content[i]);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		content=null;
		try {
			content=dbHD.getSingleDataSet("SELECT * FROM Ticket");
			System.out.println("");
			for (int i = 0; i <= content.length - 1; i++) {
				System.out.print(content[i]);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		try {
			content=dbStatus.getTables();
			for (int i=0; i<=content.length-1; i++){
				System.out.println(content[i]);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
/*
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

*/		
	}
	
	

}
