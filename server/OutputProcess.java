import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class OutputProcess {
	PrintWriter output;
	
	public OutputProcess(String file_name) {
		try 
		{
			output = new PrintWriter(new BufferedWriter( new FileWriter(file_name)));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public OutputProcess(PrintWriter writer) {
		output = writer;
	}

	public void writeLine( String line) throws UnsupportedEncodingException{	//write a line to output file
		output.println( line );
	}

	public void closeFile() {
		output.close();
	}
	
	
}
