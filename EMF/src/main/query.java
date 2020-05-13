package main;
import java.sql.*;
import java.util.*;
import java.io.*;
import org.apache.poi.ss.usermodel.Cell; 
import org.apache.poi.ss.usermodel.Row; 
import org.apache.poi.xssf.usermodel.XSSFSheet; 
import org.apache.poi.xssf.usermodel.XSSFWorkbook; 

public class query {
	public static class Grouping_variable_x{
		public List<java.lang.String> prod = new ArrayList<>();
		public List<java.lang.Integer> month = new ArrayList<>();
		public List<java.lang.Integer> year = new ArrayList<>();
		public List<java.lang.String> state = new ArrayList<>();
		public List<java.lang.Integer> quant = new ArrayList<>();
		public List<java.lang.String> cust = new ArrayList<>();
		public List<java.lang.Integer> day = new ArrayList<>();
		public List<java.lang.Integer> max_quant = new ArrayList<>();
	}
	public static class Grouping_variable_O{
		public List<java.lang.String> prod = new ArrayList<>();
		public List<java.lang.Integer> month = new ArrayList<>();
		public List<java.lang.Integer> year = new ArrayList<>();
		public List<java.lang.String> state = new ArrayList<>();
		public List<java.lang.Integer> quant = new ArrayList<>();
		public List<java.lang.String> cust = new ArrayList<>();
		public List<java.lang.Integer> day = new ArrayList<>();
	}
	public static void main(String[] args) {
	Grouping_variable_O O = new Grouping_variable_O();
	Grouping_variable_x x = new Grouping_variable_x();
	ResultSet rs = null;

	//creating mfs-structure
	Map<String, List> mfs = new HashMap<>();
	mfs.put("cust", new ArrayList<java.lang.String>());
	mfs.put("prod", new ArrayList<java.lang.String>());
	mfs.put("month", new ArrayList<java.lang.Integer>());
	mfs.put("x_max_quant", new ArrayList<java.lang.Integer>());

	// the user name, password and database url of postgreSQL
		String usr ="postgres";
		String pwd ="19951229";
		String url ="jdbc:postgresql://localhost:5432/HW";

		// load database class, and initialize it
		try {
			Class.forName("org.postgresql.Driver");
			System.out.println("Database: Loading successful.");
		}
		catch(Exception e) {
			System.out.println("Database: Loading failed.");
			e.printStackTrace();
		}

		try {
			Connection conn = DriverManager.getConnection(url, usr, pwd);
			System.out.println("Database: Connecting successful.\n");

			Statement stat = conn.createStatement();

			rs = stat.executeQuery("SELECT * FROM sales");
			while(rs.next()){
			//load data to attribute x
				x.prod.add((java.lang.String)rs.getObject("prod"));
				x.month.add((java.lang.Integer)rs.getObject("month"));
				x.year.add((java.lang.Integer)rs.getObject("year"));
				x.state.add((java.lang.String)rs.getObject("state"));
				x.quant.add((java.lang.Integer)rs.getObject("quant"));
				x.cust.add((java.lang.String)rs.getObject("cust"));
				x.day.add((java.lang.Integer)rs.getObject("day"));
			//load data to attribute O 
				O.prod.add((java.lang.String)rs.getObject("prod"));
				O.month.add((java.lang.Integer)rs.getObject("month"));
				O.year.add((java.lang.Integer)rs.getObject("year"));
				O.state.add((java.lang.String)rs.getObject("state"));
				O.quant.add((java.lang.Integer)rs.getObject("quant"));
				O.cust.add((java.lang.String)rs.getObject("cust"));
				O.day.add((java.lang.Integer)rs.getObject("day"));
			//load data to grouping_variable 
				mfs.get("cust").add((java.lang.String)rs.getObject("cust")); 
				mfs.get("prod").add((java.lang.String)rs.getObject("prod")); 
				mfs.get("month").add((java.lang.Integer)rs.getObject("month")); 
			}
		}
		catch(SQLException e) {
			System.out.println("Database: URL or username or password or table name error!");
			e.printStackTrace();
		}




		for(int i = 0; i < O.prod.size(); i++) { 
			Integer sum = null; 
			Integer count = null; 
			Integer avg = null; 
			Integer max = null; 
			java.lang.String cust = (java.lang.String)mfs.get("cust").get(i); 
			java.lang.String prod = (java.lang.String)mfs.get("prod").get(i); 
			java.lang.Integer month = (java.lang.Integer)mfs.get("month").get(i); 
			Integer x_max_quant = 0;
			if(!mfs.get("x_max_quant").isEmpty()&&i<mfs.get("x_max_quant").size()) { 
				x_max_quant= (java.lang.Integer)mfs.get("x_max_quant").get(i); 
			 }
			for(int j = 0; j < O.prod.size(); j++) {
				java.lang.String x_prod =  O.prod.get(j); 
				java.lang.Integer x_month =  O.month.get(j); 
				java.lang.Integer x_year =  O.year.get(j); 
				java.lang.String x_state =  O.state.get(j); 
				java.lang.Integer x_quant =  O.quant.get(j); 
				java.lang.String x_cust =  O.cust.get(j); 
				java.lang.Integer x_day =  O.day.get(j); 
			if(x_max_quant!=null&&true) {
				if(x_cust.equals(cust)&&x_prod.equals(prod)) { 
					 if(sum==null) 
						 sum=0; 
					 if(count==null) 
						 count=0; 
					 if(max ==null) 
						 max =0; 
					 sum +=x_quant; 
					count++; 
 					if(max<x_quant) {
						 max = x_quant; 
				} 
				} 
				} 
			} 
			if(count != null&&count != 0) {
				avg = sum/count;
			} 
				mfs.get("x_max_quant").add(max); 
 		}
	//creating result-structure
	Map<String, List> result = new HashMap<>();
	result.put("cust", new ArrayList<java.lang.String>());
	result.put("prod", new ArrayList<java.lang.String>());
	result.put("month", new ArrayList<java.lang.Integer>());
	result.put("x_max_quant", new ArrayList<java.lang.Integer>());
	boolean flag = true; 
	for(int i = 0; i<O.prod.size();i++) { 
 		for(int j = 0; j<result.get("cust").size();j++) { 
 			 if(result.get("cust").get(j).equals(mfs.get("cust").get(i))&&result.get("prod").get(j).equals(mfs.get("prod").get(i))&&result.get("month").get(j).equals(mfs.get("month").get(i))) {
				flag = false; 
				break; 
			}else { 
				 flag = true;			}; 
		} 
		java.lang.Integer x_max_quant=(		java.lang.Integer)mfs.get("x_max_quant").get(i);
		try{
		if(flag) { 
			result.get("cust").add(mfs.get("cust").get(i)); 
			result.get("prod").add(mfs.get("prod").get(i)); 
			result.get("month").add(mfs.get("month").get(i)); 
			result.get("x_max_quant").add(mfs.get("x_max_quant").get(i)); 
		} 
		}catch(NullPointerException e){
			continue;
		}
		 flag = true; 
	} 
		 try {
			XSSFWorkbook workbook = new XSSFWorkbook(); 
			XSSFSheet sheet = workbook.createSheet("table result"); 

			FileOutputStream out = new FileOutputStream(new File("result.xlsx")); 
			List<String> column_name = new ArrayList<>(); 
			List<List<Object>> data = new ArrayList<>(); 
 			column_name.add("cust"); 
			data.add(result.get("cust")); 
			column_name.add("prod"); 
			data.add(result.get("prod")); 
			column_name.add("month"); 
			data.add(result.get("month")); 
			column_name.add("x_max_quant"); 
			data.add(result.get("x_max_quant")); 
			int rownum = 0;
			int cellnum = 0;
			int flag_null_value = 0;
			Row row = sheet.createRow(rownum++);
			for(int i=0;i<column_name.size();i++) {
				Cell cell = row.createCell(cellnum++);
				cell.setCellValue(column_name.get(i));
			}
			cellnum=0;
			for(int i=0;i<data.get(0).size();i++) {
				row = sheet.createRow(rownum++);
				for(int j=0;j<column_name.size();j++) {
					if(flag_null_value==1)
						continue;
					if(data.get(j).get(i)==null) {
						sheet.removeRow(row);
						rownum--;
						flag_null_value=1;
						continue;
					}
					Cell cell = row.createCell(cellnum++);
					if(data.get(j).get(i) instanceof java.lang.String)
						cell.setCellValue((String)data.get(j).get(i));
					else if(data.get(j).get(i) instanceof java.lang.Integer)
						cell.setCellValue((Integer)data.get(j).get(i));
				}
				cellnum = 0;
				flag_null_value = 0;
			}
			workbook.write(out);
			out.close();
			System.out.println("result.xlsx written successfully on disk.");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
