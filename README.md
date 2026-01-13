# Chore-List-Generator
Randomly creates a chore list where everyone listed receives and equal workload of chores.

## How to Install
It's recommended to place the application in a folder in your desktop, due to how it's interacted with in it's current stage. This makes it easier to find the outputted chore list, as well as make changes to the input chores and people! Otherwise, it might make folders inside your Applications folder in your mac, or it might fail to make these folders at all on Windows and won't work.

### Windows
1. Download the "ChoreListGenerator_Installer_Windows.exe" file inside of the "installers" folder.
2. Double click to install, and pick a location to install the application to! You don't want to use the default location of Program Files, since the app won't have permissions here. Instead, pick the desktop or another place it will!

### Mac
1. Download the "ChoreListGenerator_Installer_Mac.dmg" file inside of the "installers" folder.
2. Double click to install, and drag the application to where you want it to be.
3. Instead of dragging it into the "Applications" folder like it asks, it's recommended to instead drag it into a dedicated folder for the app on your desktop!

## How to Use
1. Double click the app to run it. The first time you run it, it will automatically create folders labeled "ChoreListGenerator_Inputs" and "ChoreListGenerator_Output". These are how you interact with the application!
2. Inside the "ChoreListGenerator_Inputs" folder, there will be three files, one labeled "ChorePool.csv", one labeled "PeoplePool.csv", and another labeled "PrepickedChores.csv". You can open and edit these with your normal spreadsheet software.
    - ChorePool.csv will store the list of all the chores that need to be done. The first column has the names of the chores, the second column has additional notes you can add, and the Third column has the difficulty level of the chore. It's recommended to give each chore a difficulty ranking between 1 and 10.
      - If you choose not to add a workload value, the application will randomly assign chores so everyone has close to the same number (+- a chore), and some people might have a bunch of hard chores while others might have a bunch of easy ones.
    - Inside PeoplePool.csv will be the list of all people you want doing chores. Add or remove names as needed!
    - PrepickedChores.csv is for giving people chores ahead of time. If a chore is inside of here with a person's name, they will automatically recieve it before the rest of the chores are divvied up. Simply delete the entry that's there by default if you don't want to do this (but do keep the file there with the header!).
    - Please keep the header names! Those being "Chore To Do", "Extra Notes", and "Workload" in that order in ChorePool.csv, and "Housekeepers" in PeoplePool.csv. These are to the application be easier to understand, and the application won't work without them!
3. Make sure the input files are saved and closed, and then run the application. After a moment, look inside the ChoreListGenerator_Output folder and there should be your randomly generated chore list! It will be called "ChoreList.csv". It will automatically be formatted to a ready-to-print format, but make changes as desired, and then print it out!
