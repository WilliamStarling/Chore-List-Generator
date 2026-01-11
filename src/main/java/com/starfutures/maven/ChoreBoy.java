package com.starfutures.maven;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//This class is probably the only class that actually needs to be nonstatic. Each chore boy object represents one person, who has their own list of chores.
public class ChoreBoy  implements Comparable<ChoreBoy>{
	//default is Hazel because lol
	private String name = "Hazel";
	private String note = "";
	private String[] choreBoyDetails = new String[2]; //only 2 details are saved to person details.
	private List<String[]> personalChoreList; //list of one persons chores.
	private int currentWorkload = 0; //current sum of chore difficulties
	private final int DEFAULT_WORKLOAD = 1; //default chore workload should be 1.
	
	//constructor
	ChoreBoy(String[] choreBoy)
	{
		try
		{
			this.name = choreBoy[0]; //name should be in the first cell.
			if(choreBoy.length > 1)
			{
				this.note = choreBoy[1]; //if there is a not, it should be in the second cell.
			}
			
			this.choreBoyDetails[0] = this.name;
			this.choreBoyDetails[1] = this.note;
			
			personalChoreList = new ArrayList<String[]>(); //initialize list as blank	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//add a chore to this persons chore list.
	//workload cell is the cell that the chore difficulty would be in.
	public void addChore (String[] newChore, boolean includesWorkload)
	{
		int choreDetailsLength = newChore.length; //length of the details of the chore
		try
		{
			//if this chore does include the workload, copy it directly into the choreList and increase the workload by the default workload.
			if(!includesWorkload)
			{
				personalChoreList.add(newChore);
				this.currentWorkload += DEFAULT_WORKLOAD;
			}
			//else it does include the workload, so we need to copy the chore into the chore list without the workload, and add the workload to the total.
			else
			{
				String[] pureChore = Arrays.copyOfRange(newChore, 0, choreDetailsLength-1); //makes a new array without the workload. exclusive of choreDetailsLength.
				//FIXME: above should have choreDetailsLength-1, if not it will include the workload in the printed list.
				personalChoreList.add(pureChore); //adds the current chore to the list.
				this.currentWorkload += Integer.parseInt(newChore[choreDetailsLength-1]); //adds the current chores workload to the total.
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//GETTER method: gets the current workload for the person.
	public int getWorkload()
	{
		return this.currentWorkload;
	}
	
	//GETTER method: gets the name of this person.
	public String getName()
	{
		return this.name;
	}
	
	//GETTER method: gets the personal chore list of someone.
	public List<String[]> getPersonalList()
	{
		return this.personalChoreList;
	}
	
	//GETTER method: gets the string of the person details.
	public String[] getBoyDetails()
	{
		return this.choreBoyDetails;
	}
	
	//implements Comparable class to allow for sorting of ChoreBoy objects.
	public int compareTo(ChoreBoy cb)
	{
		return this.name.compareTo(cb.getName());
	}

}
