import java.io.*;

public class ReadTextFile{
 public static void main(String[] args){
	try{
		InputStream fis=new FileInputStream("ReadTextFile.java"); 
		InputStreamReader lecture=new InputStreamReader(fis);
		BufferedReader textBuff=new BufferedReader(lecture);
		String ligne;
		while ((ligne=textBuff.readLine())!=null){
			System.out.println(ligne);
		}
		textBuff.close(); 
	}		
	catch (Exception e){
		e.printStackTrace();
	}
 }
}
