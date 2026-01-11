package com.starfutures.maven;

import java.util.List;

public class App {

	public static void main(String[] args) {
		String choresPath; // the path/name of the chore list file.
		String peoplePath;
		ChoreIO choresInput;
		List<String[]> chorePool;
		List<String[]> peoplePool;
		List<String[]> choreList;
		List<ChoreBoy> choreBoyList;
		boolean includesWorkload;
		
		try
		{
			System.out.print("\nSTATUS: Reading inputs.\n");
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
			
			includesWorkload = choresInput.getIncludesWorkload();
			
			System.out.print("\nSTATUS: Generating new chore list.\n");
			GenerateList list = new GenerateList(peoplePool, chorePool, includesWorkload);
			//choreList = list.getChoreList(); //FIXME: instead of using the preformatted list of chores, in order to have formatting the list of choreboy objects is used.
			choreBoyList = list.getChoreBoyList();
			
			System.out.print("\nSTATUS: Saving new chore list.\n");
			choresInput.outputList(choreBoyList);
		}
		catch(Exception e)
		{
			System.out.println(e);
			System.out.println("\nWARNING: Please type the name of the chore file and people file after the program name in the following format surounded by quotation marks.");
			System.out.println("ChoreList \"ChoreSelection.csv\" \"PeopleSelection.csv\"");
		}

	}

}
