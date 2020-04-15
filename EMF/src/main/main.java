package main;

import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileReader;

import tool.*;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//need to change to your database file, user, password.
		String url = "jdbc:postgresql://localhost:5432/HW";
		String user = "postgres";
		String passWord = "19951229";
		
		DatabaseLoader loader = new DatabaseLoader(url,user,passWord);
		Connection db = loader.connectToDatabase();
//		loader.closeConnection();
		
		
		HashMap types = FindColumnType.columnNameAndType(db, "sales");
//		System.out.print(types.toString());
		
		String SA = "";
		String NG = "";
		String GV = "";
		String FV = "";
		String CV = "";
		String HC = "";
		
		List<String> EMFquery = ReadFile.readByLine("Example 1.txt");
		
		for(int i = 0; i < EMFquery.size(); i++) {
			if(EMFquery.get(i).contains("SELECT ATTRIBUTE")) {
				SA = EMFquery.get(i+1);
			}
			if(EMFquery.get(i).contains("NUMBER OF GROUPING VARIABLES")) {
				NG = EMFquery.get(i+1);
			}
			if(EMFquery.get(i).contains("GROUPING ATTRIBUTES")) {
				GV = EMFquery.get(i+1);
			}
			if(EMFquery.get(i).contains("F-VECT")) {
				FV = EMFquery.get(i+1);
			}
			if(EMFquery.get(i).contains("SELECT CONDITION-VECT")) {
				CV = EMFquery.get(i+1);
			}
			if(EMFquery.get(i).contains("HAVING CONDITION")) {
				HC = EMFquery.get(i+1);
			}
		}
		
		HashMap<String,String> select_attribute = TranslateEmf.translateAttributes(SA.split(","));
		HashMap<String,String> F_VECT = TranslateEmf.translateFVECT(FV.split(","));
		HashMap<String,String> condition_vect = TranslateEmf.translateCondition(CV.split(","));
		List<String> having_condition = TranslateEmf.translateHavingCondition(HC.split(","));
		
		System.out.println(select_attribute.toString());
		System.out.println(F_VECT.toString());
		System.out.println(condition_vect.toString());
		System.out.println(having_condition.toString());
	}

}
