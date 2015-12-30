import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
class Double_string{
	public String one;
	public String two;
	public Boolean flag;
}
class Triple_string{
	public String one;
	public String two;
	public String three;
	public Boolean flag;
}
class logic_base{
	public String source;
	public ArrayList<String> my_stack;
	private Double_string remove_brackets(String source, int id){
		String reg = "\\(([^\\(]*?)\\)";
		Pattern r = Pattern.compile(reg);
		Matcher m = r.matcher(source);
		if(! m.find()){
			Double_string tmp = new Double_string();
			tmp.flag = false;
			return tmp;
		}
		Double_string tmp = new Double_string();
		tmp.flag = true;
		tmp.one = m.replaceFirst(Integer.toString(id));
		tmp.two = m.group(1);
		return tmp;
	}
	public String my_join(String one, String[] array){
		String result = "";
		for(int i = 0; i < array.length-1;i++){
			result = result + array[i] + one;
		}
		return result + array[array.length-1];
	}
	public logic_base(String input){
		this.my_stack = new ArrayList<String>();
		String my_final = input;
		this.source = input;
		while(true){
			Double_string tmp = this.remove_brackets(input, this.my_stack.size());
			if(tmp.flag == false){
				break;
			}
			input = tmp.one;
			my_final = input;
			this.my_stack.add(tmp.two);
		}
		this.my_stack.add(my_final);
	}
	public String get_result(){
		String root = this.my_stack.get(this.my_stack.size()-1);
		Pattern r0 = Pattern.compile("^\\s*([0-9]+)\\s*$");
		Matcher m0 = r0.matcher(root);
		if(m0.find()){
			root = this.my_stack.get(Integer.parseInt(m0.group(1)));
		}
		while(true){
			Pattern r = Pattern.compile("(\\d+)");
			Matcher m = r.matcher(root);
			if(! m.find()){
				break;
			}
			String new_string = "("+ this.my_stack.get(Integer.parseInt(m.group(1))) + ")";
			root = m.replaceFirst(new_string);
		}
		return root;
	}
	public void merge_items(String logic){
		Pattern r0 = Pattern.compile("(\\d+)");
		Pattern r1 = Pattern.compile("neg\\s+(\\d+)");
		Boolean flag = false;
		for(int i=0;i<this.my_stack.size();i++){
			String target = this.my_stack.get(i);
			if(! target.contains(logic)){
				continue;
			}
			Matcher m1 = r1.matcher(target);
			if(m1.find()){
				continue;
			}
			Matcher m0 = r0.matcher(target);
			while(m0.find()){
				String j = m0.group(1);
				String child = this.my_stack.get(Integer.parseInt(j));
				if(! child.contains(logic)){
					continue;
				}
				Pattern new_r = Pattern.compile("(^|\\s)" + j + "(\\s|$)");
				Matcher new_m = new_r.matcher(this.my_stack.get(i));
				this.my_stack.set(i, new_m.replaceFirst(" "+child+" ").trim());
				flag = true;
			}
		}
		if(flag){
			this.merge_items(logic);
		}
	}
}
class ordering extends logic_base{
	public ordering(String input){
		super(input);
	}
	public Boolean run(){
		Boolean flag = false;
		for(int i=0;i<this.my_stack.size();i++){
			String old = this.my_stack.get(i);
			String new_string = this.add_brackets(old);
			if(!old.equals(new_string)){
				this.my_stack.set(i,new_string);
				flag = true;
			}
		}
		return flag;
	}
	private String add_brackets(String source){
		int count = 0;
		Pattern reg = Pattern.compile("\\s+(and|or|imp|iff)\\s+");
		Matcher m = reg.matcher(source);
		while(m.find()){ count+=1; }
		if(count < 2){
			return source;
		}
		Pattern reg_and = Pattern.compile("(neg\\s+)?\\S+\\s+and\\s+(neg\\s+)?\\S+");
		m = reg_and.matcher(source);
		if(m.find()){
			return m.replaceFirst("("+m.group(0)+")");
		}
		Pattern reg_or = Pattern.compile("(neg\\s+)?\\S+\\s+or\\s+(neg\\s+)?\\S+");
		m = reg_or.matcher(source);
		if(m.find()){
			return m.replaceFirst("("+m.group(0)+")");
		}
		Pattern reg_imp = Pattern.compile("(neg\\s+)?\\S+\\s+imp\\s+(neg\\s+)?\\S+");
		m = reg_imp.matcher(source);
		if(m.find()){
			return m.replaceFirst("("+m.group(0)+")");
		}
		Pattern reg_iff = Pattern.compile("(neg\\s+)?\\S+\\s+iff\\s+(neg\\s+)?\\S+");
		m = reg_iff.matcher(source);
		if(m.find()){
			return m.replaceFirst("("+m.group(0)+")");
		}
		return source;
	}
}
class replace_iff extends logic_base{
	public replace_iff(String input){
		super(input);
	}
	public Boolean run(){
		int my_final = this.my_stack.size() -1;
		Boolean flag = this.replace_all_iff();
		this.my_stack.add(this.my_stack.get(my_final));
		return flag;
	}
	private Boolean replace_all_iff(){
		Boolean flag = false;
		for(int i=0;i<this.my_stack.size();i++){
			Triple_string ans = this.replace_iff_inner(this.my_stack.get(i),this.my_stack.size());
			if(!ans.flag){
				continue;
			}
			this.my_stack.set(i, ans.one);
			this.my_stack.add(ans.two);
			this.my_stack.add(ans.three);
			flag = true;
		}
		return flag;
	}
	private Triple_string replace_iff_inner(String source, int id){
		Pattern r = Pattern.compile("^(.*?)\\s+iff\\s+(.*?)$");
		Matcher m = r.matcher(source);
		if(! m.find()){
			Triple_string tmp = new Triple_string();
			tmp.flag = false;
			return tmp;
		}
		String a = m.group(1);
		String b = m.group(2);
		Triple_string tmp = new Triple_string();
		tmp.flag = true;
		tmp.one = Integer.toString(id) + " and " + Integer.toString(id+1);
		tmp.two = a + " imp " + b;
		tmp.three = b + " imp " + a;
		return tmp;
	}
}
class replace_imp extends logic_base{
	public replace_imp(String input){
		super(input);
	}
	public Boolean run(){
		Boolean flag = false;
		for(int i=0;i<this.my_stack.size();i++){
			String ans = this.replace_imp_inner(this.my_stack.get(i));
			if(ans.length() == 0){
				continue;
			}
			this.my_stack.set(i,ans);
			flag = true;
		}
		return flag;
	}
	private String replace_imp_inner(String source){
		Pattern r = Pattern.compile("^(.*?)\\s+imp\\s+(.*?)$");
		Matcher m = r.matcher(source);
		if(! m.find()){
			return "";
		}
		String a = m.group(1);
		String b = m.group(2);
		if(a.contains("neg ")){
			return a.replace("neg ", "") + " or " + b;
		}
		return "neg " + a + " or " + b;
	}
}
class de_morgan extends logic_base{
	public de_morgan(String input){
		super(input);
	}
	public Boolean run(){
		Boolean flag = false;
		Pattern r = Pattern.compile("neg\\s+(\\d+)");
		int my_final = this.my_stack.size() - 1;
		for(int i=0;i<this.my_stack.size();i++){
			String target = this.my_stack.get(i);
			Matcher m = r.matcher(target);
			if(! m.find()){
				continue;
			}
			String child = this.my_stack.get(Integer.parseInt(m.group(1)));
			this.my_stack.set(i,m.replaceFirst(Integer.toString(this.my_stack.size())));
			this.my_stack.add(this.doing_de_morgan(child));
			flag = true;
			break;
		}
		this.my_stack.add(this.my_stack.get(my_final));
		return flag;
	}
	private String doing_de_morgan(String source){
		String[] items = source.split("\\s+");
		ArrayList<String> new_items = new ArrayList<String>();
		for(int i = 0;i<items.length;i++){
			if(items[i].contains("or")){
				new_items.add("and");
			}
			else if(items[i].contains("and")){
				new_items.add("or");
			}
			else if(items[i].contains("neg")){
				new_items.add("neg");
			}
			else if(items[i].trim().length() > 0){
				new_items.add("neg");
				new_items.add(items[i]);
			}
		}
		ArrayList<String> tmps = new ArrayList<String>();
		for(int i =0; i< new_items.size();i++){
			if(new_items.get(i).equals("neg")){
				if(new_items.get(i+1).equals("neg")){
					new_items.set(i,"");
					new_items.set(i+1,"");
				}
			}
			if(new_items.get(i).length() < 1){
				continue;
			}
			tmps.add(new_items.get(i));
		}
		String[] array = tmps.toArray(new String[tmps.size()]);
		return this.my_join(" ",array);
	}
}
class distributive extends logic_base{
	public distributive(String input){
		super(input);
	}
	public Boolean run(){
		Boolean flag = false;
		Pattern r = Pattern.compile("(\\d+)");
		int my_final = this.my_stack.size() -1;
		for(int i=0;i<this.my_stack.size();i++){
			String target = this.my_stack.get(i);
			if(! target.contains("or")){
				continue;
			}
			Matcher m = r.matcher(target);
			while(m.find()){
				String j = m.group(1);
				String child = this.my_stack.get(Integer.parseInt(j));
				if(!child.contains("and")){
					continue;
				}
				Pattern new_r = Pattern.compile("(^|\\s)" + j + "(\\s|$)");
				String[] items = child.split("\\s+and\\s+");
				String[] tmp_list = new String[items.length];
				for(int k = 0 ; k < items.length; k++){
					tmp_list[k] = Integer.toString(this.my_stack.size());
					Matcher new_m = new_r.matcher(target);
					this.my_stack.add(new_m.replaceAll(" "+items[k]+" ").trim());
				}
				this.my_stack.set(i,this.my_join(" and ",tmp_list));
				flag = true;
			}
			if(flag){
				break;
			}
		}
		this.my_stack.add(this.my_stack.get(my_final));
		return flag;
	}
}
class simplification extends logic_base{
	public simplification(String input){
		super(input);
	}
	public Boolean run(){
		String old = this.get_result();
		for(int i=0;i<this.my_stack.size();i++){
			this.my_stack.set(i,this.reducing_or(this.my_stack.get(i)));
		}
		String my_final = this.my_stack.get(this.my_stack.size()-1);
		int the_size = this.my_stack.size();
		this.my_stack.set(the_size-1,this.reducing_and(this.my_stack.get(the_size-1)));
		return old.length() != this.get_result().length();
	}
	public String reducing_and(String target){
		if(!target.contains("and")){
			return target;
		}
		Set<String> items = new HashSet<String>(Arrays.asList(target.split("\\s+and\\s+")));
		for(String item : items){
			if(items.contains("neg "+item)){
				return "";
			}
			Pattern r = Pattern.compile("\\d+$");
			Matcher m = r.matcher(item);
			if(!m.find()){
				continue;
			}
			String value = this.my_stack.get(Integer.parseInt(item));
			if(Collections.frequency(this.my_stack, value) > 1){
				this.my_stack.set(Integer.parseInt(item),"");
			}
		}
		for(int i=0;i<this.my_stack.size()-1;i++){
			
			if(this.my_stack.get(i).length() == 0){
				items.remove(Integer.toString(i));
			}
		}
		return this.my_join(" and ", items.toArray(new String[items.size()]));
	}
	public String reducing_or(String target){
		if(!target.contains("or")){
			return target;
		}
		Set<String> items = new HashSet<String>(Arrays.asList(target.split("\\s+or\\s+")));
		for(String item : items){
			if(items.contains("neg "+item)){
				return "";
			}
		}
		return this.my_join(" or ", items.toArray(new String[items.size()]));
	}
}
public class mainclass {
	static Boolean merging(logic_base source){
		String old = source.get_result();
		source.merge_items("or");
		source.merge_items("and");
		return !old.equals(source.get_result());
	}
	static ArrayList<String> run(String input){
		ArrayList<String> all = new ArrayList<String>();
		//all.add(input);

		ordering zero = new ordering(input);
		while(zero.run()){
			zero = new ordering(zero.get_result());
		}
		merging(zero);
		
		replace_iff one = new replace_iff(zero.get_result());
		one.run();
		all.add(one.get_result());
		merging(one);
		
		replace_imp two = new replace_imp(one.get_result());
		two.run();
		all.add(two.get_result());
		merging(two);
		
		de_morgan three = new de_morgan(two.get_result());
		while(three.run()){
			three = new de_morgan(three.get_result());
		}
		all.add(three.get_result());
		merging(three);
		
		distributive four = new distributive(three.get_result());
		while(four.run()){
			four = new distributive(four.get_result());
		}
		merging(four);
		
		simplification five = new simplification(four.get_result());
		five.run();
		all.add(five.get_result());
		
		return all;
	}
	
    public static void main(String[] args) {
    	String line;
    	try{
    		BufferedReader br = new BufferedReader(new FileReader(args[0]));
    		PrintWriter output = new PrintWriter(args[1]);
    		while ((line = br.readLine()) != null){
    			ArrayList<String> tmps = run(line);
    			for(String tmp:tmps){
    				output.println(tmp);
    			}
    		}
    		output.close();
    	}catch(FileNotFoundException ex){}catch(IOException ex){}
    }
}