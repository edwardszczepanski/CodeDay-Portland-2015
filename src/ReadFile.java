import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class ReadFile
{
	//Path to the file
	private String path;
	//number of lines in the file
	private int numberOfLines;
	
	public ReadFile(String file_path)
	{
		//Set the path to the file_path that was passed to the class
		path = file_path;
		//Initializes numberOfLines to 0
		numberOfLines = 0;
	}
	
	//Opens, reads, and returns the file info
	public String[] OpenFile() throws IOException
	{
		//Creates a FileReader and BufferedReader to read from the file that you pass it
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		
		//Set the variable to the number of lines in the text file through the function in this class that is defined below
		numberOfLines = readLines();
		//Creates an array of strings with size of how many lines of data there are in the file
		String[] textData = new String[numberOfLines];
		
		//Loop to read the lines from the text file for the song data
		for (int i = 0; i < numberOfLines; i++)
		{
			//Read data from song file and into the string list
			textData[i] = textReader.readLine();
		}
		
		//Close the BufferedReader that was opened
		textReader.close();
		
		//Return the read data from the text file in the form of a string array
		return textData;
	}
	
	//Returns how many lines of data there are in the file
	private int readLines() throws IOException
	{
		//Same as above, creating FileReader and BufferedReader for reading the file
		FileReader file_to_read = new FileReader(path);
		BufferedReader bf = new BufferedReader(file_to_read);
		
		//Temporary variable to hold the read line
		String aLine;
		//While the line is not null or there is nothing on the line
		while ((aLine = bf.readLine()) != null)
		{
			//Add to the numberOfLines to count how many lines there are
			numberOfLines++;
		}
		//Close the BufferedReader
		bf.close();
		
		//Return the number of lines in the text file
		return numberOfLines;
	}
}