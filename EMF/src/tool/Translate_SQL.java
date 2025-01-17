package tool;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.*;
import java.io.*;


public class Translate_SQL {
	/**
	 * 
	 * @param querry Lines of SQL sentence to be translated 
	 * @implNote it is designed to translate SQL into EMF form
	 */
	public static List<List<String>> translate(List<String> querry) {
		Matcher m = null;
		String SA = "";
		int NG = 0;
		String GV = "";
		String FV = "";
		String CV = "";
		String HC = "";
		String[] temp = {};
		List<String> Order = new ArrayList<>();
		List<String> S_Order = new ArrayList<>();
		List<List<String>> All_Order = new ArrayList<>();
		Order.add("O");
		Matcher n = null;
		for(String line:querry) {
			if(line.toLowerCase().contains("select")) {
				m = Pattern.compile("(select\\s+)(.*)").matcher(line);
				m.find();
				SA = m.group(2);
				temp = m.group(2).replaceAll(" ", "").split(",");
				for(String i:temp) {
					n = Pattern.compile("()([A-Za-z0-9]+)([(]*)([A-Za-z0-9]*)([.]*)([a-zA-Z0-9|*]*)([)]*)()").matcher(i);
					if(n.find()) {
						//if pattern match avg(x.quant)
						n = Pattern.compile("()([A-Za-z0-9]+)([(])([A-Za-z0-9|*]+)([.]*)([a-zA-Z0-9|*]*)([)])()").matcher(i);
						if(n.find()) {
							if(n.group(5).equals("."))
								S_Order.add(n.group(4)+"_"+n.group(2)+"_"+n.group(6));
							else
								S_Order.add("O_"+n.group(2)+"_"+n.group(4));
						}
						else {
							n = Pattern.compile("()([A-Za-z0-9]+)([.])([a-zA-Z0-9|*]+)()").matcher(i);
							if(n.find()) {
								S_Order.add(n.group(2)+"_"+n.group(4));
							}
							else {
								S_Order.add(i);
							}
						}
					}
					

				}
			}
			else if(line.toLowerCase().contains("group by")) {
				m = Pattern.compile("(group by\\s+)(.*)([;])(.*)").matcher(line);
				m.find();
				GV = m.group(2);
				NG = m.group(4).split(",").length;
				temp = m.group(4).replaceAll(" ", "").split(",");
				for(String i:temp) {
					Order.add(i);
				}
			}
			else if(line.toLowerCase().contains("such that")) {
				m = Pattern.compile("(such that\\s+)(.*)").matcher(line);
				m.find();
				CV = m.group(2);
			}
			else if(line.toLowerCase().contains("having")) {
				m = Pattern.compile("(having\\s+)(.*)").matcher(line);
				m.find();
				HC = m.group(2);
			}
		}
		//find all the grouping variable aggregate in such as
		m = Pattern.compile("([^A-Za-z0-9]*)([A-Za-z0-9]+[(][A-Za-z0-9]*[.]*[A-Za-z0-9]+[)])([^A-Za-z0-9]*)").matcher(CV);
		while(m.find()) {
			if(m.hitEnd()) {
				if(!FV.contains(m.group(2)))
					FV = FV+m.group(2)+",";
			}
			else {
				if(!FV.contains(m.group(2)))
					FV = FV+m.group(2)+",";
			}
		}
		//find all the grouping variable aggregate in having
		m = Pattern.compile("([^A-Za-z0-9]*)([A-Za-z0-9]+[(][A-Za-z0-9]*[.]*[A-Za-z0-9]+[)])([^A-Za-z0-9]*)").matcher(HC);
		while(m.find()) {
			if(m.hitEnd()) {
				if(!FV.contains(m.group(2)))
					FV = FV+m.group(2)+",";
			}
			else {
				if(!FV.contains(m.group(2)))
					FV = FV+m.group(2)+",";
			}
		}
		//find all the grouping variable aggregate in select
		m = Pattern.compile("([^A-Za-z0-9]*)([A-Za-z0-9]+[(][A-Za-z0-9]*[.]*[A-Za-z0-9|*]+[)])([^A-Za-z0-9]*)").matcher(SA);
		while(m.find()) {
			if(m.hitEnd()) {
				if(!FV.contains(m.group(2)))
					FV = FV+m.group(2);
			}
			else {
				if(!FV.contains(m.group(2)))
					FV = FV+m.group(2)+",";
			}
		}
		CV = CV.replaceAll("'", "\"");
		HC = HC.replaceAll("'", "\"");
		File file = new File("Example 1.txt");
		try {
			file.createNewFile();
			FileWriter writer = new FileWriter(file); 
			writer.write("// SELECT ATTRIBUTE(S):\n");
			writer.write(SA+"\n");
			writer.write("// NUMBER OF GROUPING VARIABLES(n):\n");
			writer.write(NG+"\n");
			writer.write("// GROUPING ATTRIBUTES(V):\n");
			writer.write(GV+"\n");
			writer.write("// F-VECT([F]):\n");
			writer.write(FV+"\n");
			writer.write("// SELECT CONDITION-VECT([Sigma]):\n");
			writer.write(CV+"\n");
			writer.write("// HAVING CONDITION(G):\n");
			writer.write(HC+"\n");
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		All_Order.add(S_Order);
		All_Order.add(Order);
		return All_Order;
	}
}
