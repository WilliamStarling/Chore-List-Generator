package com.starfutures.maven;

import javax.swing.JOptionPane; //FIXME: delete, library used for debugging

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class ChoreIO {
	FileReader fileReader; //create a reference to a file reader
	CSVReader csvReader; //create a reference to a csv reader.
	private List<String[]> itemList = new ArrayList<>();
	private String basePath; //string to store the path to the app.
	private String chorePath = "/ChoreListGenerator_Inputs/ChorePool.csv"; // the path/name of the chore list file.
	private String peoplePath = "/ChoreListGenerator_Inputs/PeoplePool.csv"; //the path for the list of people.
	private String prepickedPath = "/ChoreListGenerator_Inputs/PrepickedChores.csv"; //people with chores predetermined.
	private String outputPath = "/ChorelistGenerator_Output/ChoreList.xlsx"; //updated extension to be xlsx to support formatting.
	private static String templateChorePath = "/templates/ChorePool.csv";
	private static String templatePeoplePath = "/templates/PeoplePool.csv";
	private static String templatePrepickedPath = "/templates/PrepickedChores.csv";
	private List<String[]> peoplePool;
	private List<String[]> chorePool;
	private List<String[]> prepickedChores;
	private boolean includesWorkload;
	private File outputFile;
	private FileWriter fileWriter;
	private CSVWriter csvWriter;
	
	//default constructor with predefined paths.
	ChoreIO()
	{	
		basePath = findApplicationDirectory();
	    
		this.chorePath = basePath.concat(chorePath); //adds the location of the folders onto the relative path.
		this.peoplePath = basePath.concat(peoplePath);
		this.prepickedPath = basePath.concat(prepickedPath);
		this.outputPath = basePath.concat(outputPath); //goes ahead and updates the output path too.
		//System.out.println("relative Paths: " + chorePath + "\n" + peoplePath);
		/*JOptionPane.showMessageDialog(null, "relative Paths: " + chorePath + "\n" + peoplePath, "Chore List Generator", 
                JOptionPane.INFORMATION_MESSAGE);*/ //FIXME: this and previous lines are for debug.
		
		enforceIOFolders(chorePath, peoplePath, prepickedPath, outputPath);
		
		//uses the default file paths.
		this.peoplePool = readCSV(peoplePath);
		this.chorePool = readCSV(chorePath);
		this.prepickedChores = readCSV(prepickedPath);
		
		sanitizeChoreInput();
		sanitizePeopleInput();
		/*
		 * FIXME In hind sight, I realize rather then this class tell the other classes if the workload is included, and if not the other classes add in default values,
		 * it would make much more sense to add the default values in here, like what I do with the prepicked input.
		 */
		sanitizePrepickedInput();
	} 
	
	//overloaded constructor where file paths are specified.
	ChoreIO(String chorePath, String peoplePath)
	{	
		this.chorePath = chorePath;
		this.peoplePath = peoplePath;
		
		this.peoplePool = readCSV(peoplePath);
		this.chorePool = readCSV(chorePath);
		
		sanitizeChoreInput();
		sanitizePeopleInput();
	}
	
	//called from main class to actually create the output file.
	//This method is no longer used, The other version below is instead in favor of having formatting.
	//it might not work, since it tries to write a csv object to a file with .xlsx extension.
	//tried to overload the methods, but didn't work since both parameters were of type list.
	public void outputListOld(List<String[]> choreList)
	{
		String[] listHeader = {"Chores To Do", "Extra Description", "Housekeeper"};
		String[] blankHeader = {"", "", "", ""};
		
		choreList.add(0, blankHeader);
		choreList.add(0, listHeader);
		
		try
		{
			//create file writing code.
			outputFile = new File(outputPath);
			fileWriter = new FileWriter(outputFile, StandardCharsets.UTF_8);
			csvWriter = new CSVWriter(fileWriter);
			
			
			csvWriter.writeAll(choreList); //write to the csv file.
			csvWriter.close();  //saves and closes the csv file.
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	//called from main class to actually create the output file.
	// version that uses a list of Choreboy objects instead of list of strings, to allow for formatting.
	public void outputList(List<ChoreBoy> choreBoyList)
	{
		
		//create a workbook
		Workbook workbook = new XSSFWorkbook();
		//create a sheet
		Sheet sheet = workbook.createSheet("ChoreList");
		
		ListFormatting formattedList = new ListFormatting(workbook, sheet, choreBoyList);
		
		try(FileOutputStream fileOut = new FileOutputStream(outputPath))
		{
			workbook.write(fileOut);
			workbook.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	private String findApplicationDirectory()
	{
		String directoryPath;
		String osName;
		try
		{
			String jarPath = App.class.getProtectionDomain()
		            .getCodeSource()
		            .getLocation()
		            .toURI()
		            .getPath();
			File jarFile =  new File(jarPath);
			
			//if it's a directory, this means it's running in an IDE. in Eclipse, it needs to go a level up from target.
			if(jarFile.isDirectory())
			{
				directoryPath = jarFile.getParentFile().getParent(); //go up from taget/classes
				return directoryPath; //go ahead and return the path, don't need to check other conditions.
			}
			
			//get the OS name.
			osName = System.getProperty("os.name").toLowerCase();
			
			/*
			 * On Mac's. the javaFile is going to read as being in
			 * ChoreListGenerator.app/Contents/App, when we need it to just be in the same folder as ChoreListGenerator.
			 */
			if (osName.contains("mac"))
			{
				directoryPath = jarFile.getParentFile() //enters app/
						.getParentFile() //enters Contents/
						.getParentFile() // enters ChoreListGenerator.app/
						.getParent(); // enters parent folder
			}
			//in Windows computers, the .exe is just the normal directory.
			else if(osName.contains("windows"))
			{
				directoryPath = jarFile.getParent();
			}
			//Unknown other OS (probably Linux)
			else
			{
				throw new IOException ("ERROR: Unknown OS in use. Application only supports Windows and MacOS computers.");
			}
			
			return directoryPath; //return the path.
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			directoryPath = System.getProperty("user.dir");
			return directoryPath;
		}

		
	}
	
	private static void enforceIOFolders(String chorePath, String peoplePath, String prepickedPath, String outputPath)
	{
		File choreFile = new File(chorePath);
		File peopleFile = new File(peoplePath);
		File prepickedFile = new File(prepickedPath);
		File inputFolder = choreFile.getParentFile();
		File outputFolder = new File(outputPath).getParentFile(); //it says getParentFile but it gets folders too.
		boolean createdFile = true;
		
		try
		{
			if(choreFile.exists() && peopleFile.exists() && prepickedFile.exists() && outputFolder.exists())
			{
				return; //If both the input files and the output folder already exist, just return, do not need to bother making new ones.
			}
			
			//make the outputs folder if it doesn't exist.
			if(!outputFolder.exists())
			{
				createdFile = outputFolder.mkdir();
				if(!createdFile)
				{
					throw new IOException("ERROR: failed to create output folder. Application likely installed in location where it does not have permissions. Please install in different location, like desktop.");
				}
			}
			
			//if the input folder doesn't exist, make a new folder and add the csv files into it.
			if(!inputFolder.exists())
			{
				createdFile = inputFolder.mkdirs();
				if(!createdFile)
				{
					throw new IOException("ERROR: failed to create input folder. Application likely installed in location where it does not have permissions. Please install in different location, like desktop.");
				}
				
				copyFile(templateChorePath, choreFile);
				copyFile(templatePeoplePath, peopleFile);
				copyFile(templatePrepickedPath, prepickedFile);
				
				
				return; //if this has been done, then nothing is left missing.
			}
			
			if(!choreFile.exists())
			{
				copyFile(templateChorePath, choreFile);
			}
			if(!peopleFile.exists())
			{
				copyFile(templatePeoplePath, peopleFile);
			}	
			if(!prepickedFile.exists())
			{
				copyFile(templatePrepickedPath, prepickedFile);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return;
	}
	
	private static void copyFile(String originalPath, File cloneFile)
	{
		try(InputStream in = App.class.getResourceAsStream(originalPath);
				FileOutputStream out = new FileOutputStream(cloneFile))
		{
			byte[] buffer = new byte[1024];
			int bytesRead;
			while((bytesRead = in.read(buffer)) != -1)
			{
				out.write(buffer, 0, bytesRead); 
			}
		}
		catch (Exception e)
		{
			System.out.println("ERROR: failed to create template files. Application likely installed in location where it does not have permissions. Please install in different location, like desktop.");
			e.printStackTrace();
		}
	}
	
	//Reads the specified CSV file.
	private List<String[]> readCSV(String fileToRead)
	{
		try
		{
			fileReader = new FileReader(fileToRead, StandardCharsets.UTF_8); //create a file reader for the file to read.
			
			//allow the csvfilereader to read the csv file.
			csvReader = new CSVReader(fileReader);
			
			itemList = csvReader.readAll();
			
			//printList(itemList);
			
			return itemList;
		}
				
		catch(Exception e)
		{
			e.printStackTrace();
			return itemList;
		}
	}
	
	//Prints the specified list to console, for debug purposes.
	//made it static public so it can be accessed by other classes.
	public static void printList(List<String[]> printThis)
	{
		for (String[] row : printThis)
		{
			for (String cell : row)
			{
				System.out.print(cell + "\t");
			}
			System.out.println();
		}
	}
	
	private void sanitizeChoreInput()
	{
		String[] choresHeader = this.chorePool.get(0);
		this.includesWorkload = true;
		try
		{
			//First, try to make sure the headers match expectations.
			if(choresHeader.length != 2 && choresHeader.length != 3)
			{
				throw new IOException("ERROR: improper ChorePool.csv formatting. Expected 2 or 3 columns.");
			}
			
			if(!(choresHeader[0].toLowerCase().equals("chore to do")) || !(choresHeader[1].toLowerCase().equals("extra notes")))
			{
				throw new IOException("ERROR: 'Chores to Do' and/or 'Extra Description' columns in ChorePool.csv improperly named.");
			}
			
			if(choresHeader.length == 2)
			{
				this.includesWorkload = false;
			}
			else if(choresHeader[2].toLowerCase().equals("Workload"))
			{
				throw new IOException("ERROR: 'Workload' column improperly named.");
			}
			
			//then remove the headers from input.
			this.chorePool.remove(0);
			//I'm going to add an extra blank header to between the header and actual values, to improve readability.
			//This will need to be removed too for it to function, but I want it to work without it so check if it's even there.
			if(this.chorePool.get(0)[0].equals("") || this.chorePool.get(0)[0].isBlank())
			{
				this.chorePool.remove(0);
			}
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void sanitizePeopleInput()
	{
		String[] peopleHeader = peoplePool.get(0);
		
		try
		{
			if(peopleHeader.length < 1)
			{
				throw new IOException ("ERROR: Need at least 1 column for PeoplePool.csv input.");
			}
			
			if(!(peopleHeader[0].toLowerCase().equals("housekeepers")))
			{
				throw new IOException ("ERROR: Expected first column in PeoplePool.csv to be named 'Housekeepers'.");
			}
			
			peoplePool.remove(0); //remove the header row.
			//I'm going to add an extra blank header to between the header and actual values, to improve readability.
			//This will need to be removed too for it to function, but I want it to work without it so check if it's even there.
			if(this.peoplePool.get(0)[0].equals("") || this.peoplePool.get(0)[0].isBlank())
			{
				this.peoplePool.remove(0);
			}
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void sanitizePrepickedInput()
	{
		String[] prepickedHeader = this.prepickedChores.get(0);
		try
		{
			//First, try to make sure the headers match expectations.
			if(prepickedHeader.length != 3 && prepickedHeader.length != 4)
			{
				throw new IOException("ERROR: improper PrepickedChores.csv formatting. Expected 3 or 4 columns.");
			}
			
			//check that the column headers are properly named
			if(!(prepickedHeader[0].toLowerCase().equals("chore to do")) || !(prepickedHeader[1].toLowerCase().equals("extra notes") || !(prepickedHeader[-1].toLowerCase().equals("housekeeper"))))
			{
				throw new IOException("ERROR: 'Chores to Do' and/or 'Extra Description' and/or 'Housekeeper' columns in PrepickedChores.csv improperly named.");
			}
			if(this.includesWorkload == true && prepickedHeader[2].toLowerCase().equals("Workload"))
			{
				throw new IOException("ERROR: 'Workload' column improperly named.");
			}
			
			//then remove the headers from input.
			this.prepickedChores.remove(0);
			//I'm going to add an extra blank header to between the header and actual values, to improve readability.
			//This will need to be removed too for it to function, but I want it to work without it so check if it's even there.
			if(this.prepickedChores.get(0)[0].equals("") || this.prepickedChores.get(0)[0].isBlank())
			{
				this.prepickedChores.remove(0);
			}
			
			//first, if everything looks good then just leave.
			if((this.includesWorkload == true && prepickedHeader.length == 4) || (this.includesWorkload == false && prepickedHeader.length == 3))
			{
				//if the chore pool includes the workload, check the preadded chores is big enough to as well. 
				//if it doens't include the header, check the preadded chores is small enough to as well.
				return;
			}
			
			//now if it's supposed to include the workload, insert a default value of 1
			if((this.includesWorkload == true && prepickedHeader.length == 3))
			{
				for(int choreNumb = 0; choreNumb < prepickedChores.size(); choreNumb++)
				{
					List<String> fixedChore = new ArrayList<>(Arrays.asList(prepickedChores.get(choreNumb)));
					fixedChore.add(2, "1");
					prepickedChores.set(choreNumb, fixedChore.toArray(new String[fixedChore.size()]));
				}
			}
			//now if it's NOT supposed to include the workload, remove the workload.
			else if((this.includesWorkload == false && prepickedHeader.length == 4))
			{
				for(int choreNumb = 0; choreNumb < prepickedChores.size(); choreNumb++)
				{
					List<String> fixedChore = new ArrayList<>(Arrays.asList(prepickedChores.get(choreNumb)));
					fixedChore.remove(2);
					prepickedChores.set(choreNumb, fixedChore.toArray(new String[fixedChore.size()]));
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//GETTER method: returns a list of each person.
	public List<String[]> getPeoplePool()
	{
		return this.peoplePool;
	}
	
	//GETTER method: returns the list of chores.
	public List<String[]> getChorePool()
	{
		return this.chorePool;
	}
	
	//GETTER method: returns whether the  csv file includes a workload.
	public boolean getIncludesWorkload()
	{
		return this.includesWorkload;
	}
	
	//GETTER method: returns the extra prepicked chores.
	public List<String[]> getPrepickedChores()
	{
		return this.prepickedChores;
	}
}
