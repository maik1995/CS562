package tool;

import java.util.*;
import java.util.regex.*;

public class TranslateEmf {
	/**
	 * 
	 * @param attributes
	 * @return a HashMap with GROUPING VARIABLES as key, and function are 2*i and column are 2*i+1, the Group by columns are stored by the key GroupBy, and the function like avg(quant) are stored in key 0
	 */
	public static HashMap<String,String> translateAttributes(String[] attributes){
		HashMap<String,String> t_attributes = new HashMap<>();
		String temp = "";
		String variableName = "";
		String groupByAttri = "";
		for(int i=0; i<attributes.length;i++) {
			if(attributes[i].contains("(")) {
				temp = attributes[i].substring(0, attributes[i].indexOf("(")) + ",";
				if(attributes[i].contains(".")) {
					variableName = attributes[i].substring(attributes[i].indexOf("(")+1 , attributes[i].indexOf("."));
					temp = temp + attributes[i].substring(attributes[i].indexOf(".")+1 ,attributes[i].indexOf(")"));
					//if there are multiple attribute link to same GROUPING VARIABLES
					if(t_attributes.containsKey(variableName)) {
						temp = t_attributes.get(variableName) + "," + temp;
					}
					t_attributes.put(variableName, temp);
				}
				else {
					temp = temp + attributes[i].substring(attributes[i].indexOf("(")+1 ,attributes[i].indexOf(")"));
					//if there are multiple attribute link to same GROUPING VARIABLES
					if(t_attributes.containsKey("0")) {
						temp = t_attributes.get("0") + "," + temp;
					}
					t_attributes.put("0", temp);
					
				}
			}
			else {
				groupByAttri = groupByAttri+attributes[i]+",";
			}
		}
		t_attributes.put("GroupBy", groupByAttri);
		return t_attributes;
	}
	
	/**
	 * 
	 * @param attributes
	 * @return a HashMap with GROUPING VARIABLES as key, and function are 2*i and column are 2*i+1, and the function like avg(quant) are stored in key 0
	 */
	public static HashMap<String,String> translateFVECT(String[] attributes){
		HashMap<String,String> t_attributes = new HashMap<>();
		t_attributes = translateAttributes(attributes);
		return t_attributes;
	}
	
	/**
	 * 
	 * @param Conditions
	 * @return a string that can just be used in if logic and you can use groupingVariable or 0 to retrive it
	 */
	public static HashMap<String,String> translateCondition(String[] Conditions){
		HashMap<String,String> t_Conditions = new HashMap<>();
		if(Conditions[0].isEmpty())
			return t_Conditions;
		String groupingVariable = "";
		String column = "";
		String sub = "";
		List<String> logic = new ArrayList<>();
		String[] subJudge = {};
		Pattern gv = Pattern.compile("(.*)([A-Za-z0-9]+)([.])(.*)");
		Matcher m = null;
		for(int i=0; i<Conditions.length; i++) {
			Conditions[i] = Conditions[i].replaceAll("=", "==").replaceAll(">==", ">=").replaceAll("<==", "<=").replaceAll("<>", "!=").replaceAll(" and | AND ", "&&").replaceAll(" or | OR ", "||").replaceAll(" ", "");
			//find the logic and save it
			for(int j=0; j<Conditions[i].length()-2;j++) {
				if(Conditions[i].substring(j, j+2).matches("&&")||Conditions[i].substring(j, j+2).matches("||")) {
					logic.add(Conditions[i].substring(j, j+2));
				}
			}
			subJudge = Conditions[i].split("&&|\\|\\|");
			for(int j = 0; j<subJudge.length; j++) {
				sub = subJudge[j];
				if(sub.matches("(.*)[A-Za-z0-9][.][A-Za-z](.*)")) {
					m = gv.matcher(sub);
					m.find();
					groupingVariable = m.group(2);
					if(j < logic.size())
						column = groupingVariable + "_" +  m.group(1)+m.group(4) + logic.get(j);
					else
						column = groupingVariable + "_" +  m.group(1)+m.group(4);
					if(t_Conditions.containsKey(groupingVariable)) {
						column = t_Conditions.get(groupingVariable) + column;
					}
					t_Conditions.put(groupingVariable,column);
				}
				else {
					if(j < logic.size())
						column = sub + logic.get(j);
					else
						column = sub;
					if(t_Conditions.containsKey("0")) {
						column = t_Conditions.get("0") + column;
					}
					t_Conditions.put("0",column);
				}
			}
			logic.clear();
		}
		return t_Conditions;
	}
	
	/**
	 * 
	 * @param Conditions
	 * @return stringArray contain logic can be used in if, find by loop.
	 */
	public static List<String> translateHavingCondition(String[] Conditions){
		
		String t_Conditions = "";
		List<String> Array_Conditions = new ArrayList<String>();
		if(Conditions[0].isEmpty())
			return Array_Conditions;
		String[] subJudge = {};
		String[] sub = {};
		List<String> logic = new ArrayList<>();
		String left = "";
		String right = "";
		String oper = "";
		Pattern pattern = Pattern.compile(".*([=><!][=]|[<>]).*");
		Matcher m = null;
		for(int i=0; i<Conditions.length; i++) {
			Conditions[i] = Conditions[i].replaceAll("=", "==").replaceAll(">==", ">=").replaceAll("<==", "<=").replaceAll("<>", "!=").replaceAll(" and | AND ", "&&").replaceAll(" or | OR ", "||").replaceAll(" ", "");
			//find the logic and save it
			for(int j=0; j<Conditions[i].length()-2;j++) {
				if(Conditions[i].substring(j, j+2).matches("&&")||Conditions[i].substring(j, j+2).matches("||")) {
					logic.add(Conditions[i].substring(j, j+2));
				}
			}
			
			subJudge = Conditions[i].split("&&|\\|\\|");
			
			for(int j=0; j<subJudge.length; j++) {
				sub = subJudge[j].split("==|>=|<=|!=|>|<");
				m = pattern.matcher(subJudge[j]);
				m.find();
				oper = m.group(1);
				left = sub[0];
				right = sub[1];
				// if it is not a number
				if(left.contains("(")) {
					m = Pattern.compile("([a-zA-Z]+)\\(([A-Za-z0-9]+)([.]*)([a-zA-Z]+)\\)(.*)").matcher(left);
					m.find();
					left =  m.group(2) + "." + m.group(1) + "_" + m.group(4) + m.group(5);
				}
				else if(left.matches("(.*)[A-Za-z0-9][.][A-Za-z](.*)")) {
					m = Pattern.compile("([A-Za-z0-9]+)([.])([a-zA-Z]+)(.*)").matcher(left);
					m.find();
					left = m.group(1) + "." + m.group(3) + m.group(4);
				}
				
				if(right.contains("(")) {
					m = Pattern.compile("([a-zA-Z]+)\\(([A-Za-z0-9]+)([.]*)([a-zA-Z]+)\\)(.*)").matcher(right);
					m.find();
					right = m.group(2) + "." + m.group(1) + "_" + m.group(4) + m.group(5);
				}
				else if(right.matches("(.*)[A-Za-z0-9][.][A-Za-z](.*)")) {
					m = Pattern.compile("([A-Za-z0-9]+)([.])([a-zA-Z]+)(.*)").matcher(right);
					m.find();
					right = m.group(1) + "." + m.group(3) + m.group(4);
				}
				if(j < logic.size())
					t_Conditions = t_Conditions + left + oper + right + logic.get(j);
				else
					t_Conditions = t_Conditions + left + oper + right;
			}
			
			Array_Conditions.add(t_Conditions);
			t_Conditions = "";
			logic.clear();
		}
		return Array_Conditions;
	}
}
