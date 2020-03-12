// donner le nom du fichier en parametre

import java.io.*;
import java.util.*;


class CompterMots{
public static void main(String[] arg) throws IOException{
	
	Hashtable tableH = new Hashtable();
	String ligne, mot;
	StringTokenizer st;
	
	
	BufferedReader fichier=new BufferedReader(new FileReader(arg[0]));
	
	while ((ligne=fichier.readLine())!=null){
		st=new StringTokenizer(ligne, " ,;.:+-=*/{}?!()\"[]\t");
		while (st.hasMoreTokens()){
			mot=st.nextToken();
			Object o=tableH.get(mot);
			if (o==null)
			 tableH.put(mot,new Integer(1));
			else {
				Integer i=(Integer) o;
				tableH.put(mot, new Integer(i.intValue()+1));
				//System.out.println(i.intValue()+1);
            //System.out.println(mot+"=>"+((Integer)tableH.get(mot)).intValue());
			} 
			}
		}
	fichier.close();
	Enumeration listeMots=tableH.keys();
	while (listeMots.hasMoreElements()){
	 String m = (String)listeMots.nextElement();
	 Object o=tableH.get(m);
	 Integer freq=(Integer)o;
	 System.out.println(m+"=>"+freq);
	}
		
	}
}
