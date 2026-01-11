/**
 * 
 */
package com.starfutures.maven;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author william
 *
 */

public class GenerateList 
{
	private List<String[]> peoplePool;
	private List<String[]> chorePool;
	private List<String[]> choreList;
	private List<ChoreBoy> choreBoyList;
	private boolean includesWorkload;
	
	GenerateList(List<String[]> peoplePool, List<String[]> chorePool, boolean includesWorkload)
	{
		this.includesWorkload = includesWorkload;
		this.peoplePool = peoplePool;
		this.chorePool = chorePool;
		
		//instantiate the lists as empty.
		this.choreList = new ArrayList<String[]>(); 
		this.choreBoyList = new ArrayList<ChoreBoy>();
		
		try
		{
			//randomize the chores.
			randomizeChorePool();
			
			//generate a chorelist.
			generateList();
			
			//turn the collection of chore people into a single list of string arrays.
			//FIXME: It is worth noting that after enabling formatting, the combing of chore boy lists happens inside the output class now. 
			//So the formatting functions and involved variables are no longer needed.
			formatChoreList();			
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//randomizes the order of chores to induce random assignment.
	private void randomizeChorePool()
	{
		//the reason we shuffle and then sort the list is so that items of the same difficulty level are randomly placed.
		//so even if the ranks of chores that get mixed together are the same, the specific chores that are combined is different.
		
		Collections.shuffle(this.chorePool); //randomly shuffle the chore pool
		//if there is a workload, we need to reorder the list so it's in order of decreasing workload.
		if(this.includesWorkload)
		{
			//the workload should be in the last cell of the chore, so use it to compare.
			this.chorePool.sort(Comparator.comparing(arr -> Integer.parseInt(arr[arr.length-1]), Comparator.reverseOrder()));
		}
	}
	
	//generates a list of chores using the Greedy Number Partitioning algorithm (aka Largest Possible TIme) so that they're assigned evenly.
	private void generateList()
	{
		//creates chore boys objects for each person, to hold their chores.
		for(String[] choreBoy: this.peoplePool)
		{
			this.choreBoyList.add(new ChoreBoy(choreBoy));
		}
		
		//this should cycle through each chore, and give it to the person with the smallest workload.
		for(String[] chore : this.chorePool)
		{
			Collections.shuffle(choreBoyList); //shuffle the chore boy list, so that people get different groupings of chores.
			//shuffle the list each time, so one or two people don't get completely bogged down with single chores.
			System.out.println("Giving " + chore[0] + " To " + hasSmallestWorkload().getName() + " with workload " + hasSmallestWorkload().getWorkload());
			hasSmallestWorkload().addChore(chore, this.includesWorkload);
		}
		
		//re-alphabetizes the chore list.
		Collections.sort(choreBoyList);
	}
	
	//gets the choreboy with the smallest workload.
	private ChoreBoy hasSmallestWorkload()
	{
		ChoreBoy currentSmallest = this.choreBoyList.get(0); //set the smallest chore boy to be the first one.
		
		//searches through each chore boy to find the smallest one.
		for(ChoreBoy currentBoy : choreBoyList)
		{
			if(currentBoy.getWorkload() < currentSmallest.getWorkload())
			{
				currentSmallest = currentBoy;
			}
		}
		return currentSmallest;
	}
	
	private void formatChoreList()
	{
		//calculate the size of the chore entries by taking the size of the first chore of the first chore boy,
		//and adding that with the length of the chore boy details.
		int choreEntrySize = this.choreBoyList.get(0).getPersonalList().get(0).length + this.choreBoyList.get(0).getBoyDetails().length;
		String[] tempEntry = new String[choreEntrySize];
		
		//cycle through each chore person.
		for(ChoreBoy choreBoy: this.choreBoyList)
		{
			String[] currentBoyDetails = choreBoy.getBoyDetails();
			//cycle through each chore the person has, and create a string[] with that chore and the person details.
			//Then add it to the total chore list.
			for(String[] chore: choreBoy.getPersonalList())
			{
				tempEntry = ArrayUtils.addAll(chore, currentBoyDetails);
				this.choreList.add(tempEntry);
			}
		}
	}
	
	//GETTER method: returns the finished chore list.
	public List<String[]> getChoreList()
	{
		return this.choreList;
	}
	
	//GETTER method: returns the group of chore boys, each with a set of chores.
	public List<ChoreBoy> getChoreBoyList()
	{
		return this.choreBoyList;
	}
	
}
