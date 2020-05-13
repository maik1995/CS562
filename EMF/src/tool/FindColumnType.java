package tool;

import java.sql.*;
import java.util.*;

public class FindColumnType {
	//change the type to java type
//	private static String translateType(String type) {
//		switch(type) {
//		case "CHAR": case "VARCHAR": case "LONGVARCHAR":
//			type = "String";
//			break;
//		case "NUMERIC": case "DECIMAL":
//			type = "java.math.BigDecimal";
//			break;
//		case "BIT":
//			type = "Boolean";
//			break;
//		case "TINYINT":
//			type = "Byte";
//			break;
//		case "SMALLINT":
//			type = "Short";
//			break;
//		case "INTEGER":
//			type = "Integer";
//			break;
//		case "BIGINT":
//			type = "Long";
//			break;
//		case "REAL": case "FLOAT":
//			type = "Float";
//			break;
//		case "DOUBLE":
//			type = "Double";
//			break;
//		case "BINARY": case "VARBINARY": case "LONGVARBINARY":
//			type = "Byte[]";
//			break;
//		case "DATE":
//			type = "java.sql.Date";
//			break;
//		case "TIME":
//			type = "java.sql.Time";
//			break;
//		case "TIMESTAMP":
//			type = "java.sql.Timestamp";
//			break;
//		default:
//			break;
//		}
//		return type;
//	}
	
	/** 
	 * @param db type Connection
	 * @param tableName type String
	 * 
	 * @return return a HashMap with column name as key and type(the type has been translated into java type not SQL type) as data
	 */
	public static HashMap<String,String> columnNameAndType(Connection db, String tableName){
		HashMap<String,String> Name_Type = new HashMap<>();
		String query = "select * from " + tableName;
		String type = "";
		try {
			Statement stat = db.createStatement();
			ResultSet rs = stat.executeQuery(query);
			ResultSetMetaData mt = rs.getMetaData();
			for(int i=1; i<=mt.getColumnCount(); i++) {
				
				type = mt.getColumnClassName(i);
				Name_Type.put(mt.getColumnName(i), type);
			}
		}catch(SQLException e) {
			System.out.print(e.getMessage());
		}
		
		return Name_Type;
	}
}
