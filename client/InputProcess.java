
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class InputProcess {
	private BufferedReader input_file;
	
	public InputProcess(String file_name) {
		Reader reader;
		try {
			reader = new InputStreamReader(new FileInputStream(file_name),Charset.forName("ISO-8859-9"));
			input_file = new BufferedReader(reader);
			
		} catch ( FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public InputProcess( BufferedReader reader) {
		input_file = reader;
	}
	
	public ArrayList<String> readFile(){		//this function read whole file
		String line;
		ArrayList<String> inputs = new ArrayList<String>();
        
		try {
			
			while ((line=input_file.readLine())!=null) {
				inputs.add(line);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return inputs;
	}
	
	public ArrayList<String> getParsedLine() throws IOException{	//this function read a line and parse it before return
		String line;
		ArrayList<String> inputs = null;

		if((line = input_file.readLine()) != null){
			inputs = parseLine(line);
		}
		
		return inputs;
	}
	
	public String getLine() throws IOException{	//this function read a line and return it
		return input_file.readLine();
	}
	
	private ArrayList<String> parseLine(String string){		//this function parse arguman string
		ArrayList<String> parsed_string = new ArrayList<String>();
		StringTokenizer parser = new StringTokenizer(string);
		
		if(parser != null){
			while(parser.hasMoreElements()){
				parsed_string.add( parser.nextToken() );
			}
		}

		return parsed_string;
	}
	
	public void closeFile() throws IOException{
			input_file.close();
			System.out.println("input");
	}
}
