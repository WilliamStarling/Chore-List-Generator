package com.starfutures.maven;

import java.util.List;

public class App {

	/*
	 * 4. seperate the chore from the name.
	 * 2. for each item listed, compare the name in  the 4th column against each chore person's name until you find a match. if no match, add a new person.\
	 * 3. add the chore to that person. 
	 */
	
	public static void main(String[] args) {
		String choresPath; // the path/name of the chore list file.
		String peoplePath;
		ChoreIO choresInput;
		List<String[]> chorePool;
		List<String[]> peoplePool;
		List<String[]> choreList;
		List<String[]> prepickedChores;
		List<ChoreBoy> choreBoyList;
		boolean includesWorkload;
		
		try
		{
			System.out.println("\nSTATUS: Reading inputs.");
			if (args.length == 2)
			{
				choresPath = args[0];
				peoplePath = args[1];
				choresInput = new ChoreIO(choresPath, peoplePath);
			} else
			{
				choresInput = new ChoreIO();
			}
			peoplePool = choresInput.getPeoplePool();
			chorePool = choresInput.getChorePool();
			prepickedChores = choresInput.getPrepickedChores();
			
			includesWorkload = choresInput.getIncludesWorkload();
			
			System.out.println("\nSTATUS: Generating new chore list.\n");
			GenerateList list = new GenerateList(peoplePool, chorePool, prepickedChores, includesWorkload);
			//choreList = list.getChoreList(); //FIXME: instead of using the preformatted list of chores, in order to have formatting the list of choreboy objects is used.
			choreBoyList = list.getChoreBoyList();
			
			System.out.println("\nSTATUS: Saving new chore list.");
			choresInput.outputList(choreBoyList);
			System.out.println("\nSTATUS: Finished!");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			/*
			System.out.println("\nWARNING: Please type the name of the chore file and people file after the program name in the following format surounded by quotation marks.");
			System.out.println("ChoreList \"ChoreSelection.csv\" \"PeopleSelection.csv\"");
			*/
			System.out.println("ERROR: Unkown issue occured. Please contact William Starling at williamjonas@comcast.net with as many details you can about what you were doing when this occured.");
		}

	}

}
