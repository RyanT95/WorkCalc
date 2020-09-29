import java.util.Scanner;
import java.util.InputMismatchException;

import java.lang.NumberFormatException;
import java.lang.Integer;
import java.lang.String;
import java.lang.Float;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

// *******************************************************
// Program to calculate what time you need to leave work 
// based on starting time and length of lunch break.
// *******************************************************
public class WorkCalc
{
    // ************
    // Constructor
    // ************
    public static void main(String[] args)
    {
        WorkCalc calc = new WorkCalc();
        calc.start();
    }

    // ************************
    // Application entry point
    // ************************
    public void start()
    {
		mainMenu();
	}
	
	// *************************
    // Application menu system
    // *************************
	public void mainMenu()
	{
		// Display app title
        System.out.println("\n-----------------------------------------\n"
                + "             Work Calculator"
                + "\n-----------------------------------------");
		
		Scanner menu = new Scanner(System.in);		// Open user input
		
		int menuOption;								// Selected menu item
		boolean tryAgain = true;					// Did the user make a bad input?
		
		// Main menu system
		while (tryAgain == true)
		{
			System.out.println("1. Time you need to leave.\n" + "2. Time spent working today.\n");
			System.out.print("\nPlease select an option - ");
			
			menuOption = menu.nextInt();
		
			switch(menuOption)
			{
				case 1: 
					timeToLeave();
					tryAgain = false;
					break;
				case 2: 
					timeSpentWorking();
					tryAgain = false;
					break;
				default:
					System.out.println(errorMessage("Please try again. Select an option by typing 1 or 2...", 62));
					tryAgain = true;
					break;
			}
		}
		
		// Close user input
		menu.close();		
	}
	
	// **************************************
    // Calculate time you need to leave work
    // **************************************
	public void timeToLeave()
	{
		// Display menu option title
        System.out.println("\n-----------------------------------------\n"
						+ "        - Time you should leave -"
						+ "\n-----------------------------------------");
				
		Scanner input = new Scanner(System.in);		// Open user input
		
		final int SHIFT_LENGTH = 480; 				// Length of shift in minutes minus break
		String startTime = "00:00";					// Time started work
		int breakLength = 0;						// Length of lunch break
		int numTries = 0;							// Number of unsuccessful attempts at inputting values
		boolean tryAgain = true;					// Did the user make a bad input?
	
		// 
		while (startingTimeMins(startTime) == 0)
		{	
			try
			{
				System.out.print("\nWhat time did you start? (00:00) - ");
				// User inputs time they started
				startTime = input.nextLine();
	
				System.out.print("\nHow long was your break? (minutes) - ");
				// User inputs duration of break in minutes
				breakLength = input.nextInt();
	
				// If user inputs negative number for break length
				if (breakLength < 0)
				{
					System.out.print(errorMessage(2));
	
					tryAgain = true;
					startTime = "00:00";                                    // Reset start time to "00:00"
				}
	
			}
			catch (NumberFormatException | InputMismatchException e)        // If user enters invalid input (letters)
			{
				System.out.print(errorMessage(3));
	
				tryAgain = true;
				startTime = "00:00";                                        // Reset start time to "00:00"
				input.nextLine();											// Clear the buffer, so the first "nextLine" at the top of loop isn't skipped
				continue;													// Go to the next loop iteration
			}
			
			// If input is good, set boolean to break out of loop
			if (startingTimeMins(startTime) > 0)							
			{
				tryAgain = false;
				continue;
			}
			
			// Display error message informing user to try again if input is bad
			if (tryAgain == true)											
			{
				System.out.print(errorMessage(1));
			}
			
			input.nextLine();		// Clear the buffer, so the first "nextLine" at the top of loop isn't skipped

		}
	
		float outTimeMinutes = startingTimeMins(startTime) + SHIFT_LENGTH + breakLength;    // Calculate time to leave in minutes
	
		// Print time user should leave work in hours and minutes (00:00)
		System.out.println("\n-----------------------------------------\n"
						+ "You should leave work at: "
						+ calcTime(outTimeMinutes)
						+ "\n-----------------------------------------");
	
		// Create new string array to capture return values of timeLeft()
		String[] timeLeft = timeLeft(calcTime(outTimeMinutes));
	
		// Leaving time is in the past
		if (timeLeft[0] == "false")		
		{
			System.out.println("You have already finished!"
					+ "\n-----------------------------------------");
		}
		
		// Leaving time is not in the past
		else		
		{
			System.out.println("Only: "
					+ timeLeft[0]
					+ " Hours and "
					+ timeLeft[1]
					+ " Minutes"
					+ " to go!"
					+ "\n-----------------------------------------");
		}
		
		// Close input
		input.close();		
	}
    
	// ***********************************
    // Calculate time spent working today
    // ***********************************
	public void timeSpentWorking()
	{
		// Display menu option title
        System.out.println("\n-----------------------------------------\n"
						+ "         - Time spent working -"
						+ "\n-----------------------------------------");
						
		// TODO: Calculate time spent working during the day (minus break)
	}
	
    // *************************************************
    // Converts time from Hrs & Mins (00:00) to minutes
    // *************************************************
    public int startingTimeMins(String inTime)
    {
        int inTimeMinutes;

        String[] splitTimeIn = inTime.split(":");		// Split time (00:00) into hours and minutes

        if (splitTimeIn.length == 1)                    // If there is only 1 element in the splitTime array, then an invalid input has been entered
        {
            return 0;
        }

        try
        {
            // If user inputs an invalid hour (More than 24, or a negative number)
            if (Integer.valueOf(splitTimeIn[0]) > 24 || Integer.valueOf(splitTimeIn[0]) < 0)
            {
                return 0;
            }
            // If user inputs an invalid minute (60 or higher, or a negative number)
            if (Integer.valueOf(splitTimeIn[1]) >= 60 || Integer.valueOf(splitTimeIn[1]) < 0)
            {
                return 0;
            }
        }
		// If user enters invalid input (letters)
        catch (InputMismatchException | NumberFormatException e)
        {
            System.out.print(errorMessage(3));
        }

        inTimeMinutes = (Integer.valueOf(splitTimeIn[0]) * 60) + Integer.valueOf(splitTimeIn[1]);	// Convert time into minutes
        return inTimeMinutes;		// Return the starting time in minutes
    }

    // *********************************************************************
    // Converts inputted time in minutes into formatted time string (00:00)
    // *********************************************************************
    public String calcTime(float timeInMins)
    {
        String outTime;
        String sHours;
        String sMinutes;

        float timeInHours = timeInMins / 60;					// Convert time from minutes to hours

        String sTimeInHours = String.valueOf(timeInHours);		// Convert time from float to string

        String[] splitTime = sTimeInHours.split("\\.");			// Split time to leave into hours and fraction of hours

        float hrs = Float.valueOf(splitTime[0]);				// Convert hours into float
        float mins = Float.valueOf("." + splitTime[1]) * 60;	// Convert fraction of hours into minutes

        // If hours is higher than 25, this means the time should be AM, so subtract 24 hours to change PM to AM.
        if (hrs >= 25.0f)
        {
            hrs -= 24.0f;
        }
		
        // If time is earlier than 10 past the hour, add a zero to the start of minutes
        if (mins < 10)
        {
            sMinutes = String.format("0" + String.format("%.00f", mins));		// Populate minutes string to two decimal places, with 0 at the start
        }
		
		// If time is later than 10 past the hour
        else
        {
            sMinutes = String.format("%.00f", mins);							// Populate minutes string to two decimal places
        }
		
        // If time is earlier than 10:00, add a zero to the start of hours
        if (hrs < 10)
        {
            sHours = String.format("0" + "%.00f", hrs);		// Populate hours string to two decimal places, with 0 at the start
        } 
		
		// If time is later than 10:00
        else
        {
            sHours = String.format("%.00f", hrs);			// Populate hours string to two decimal places
        }
		
        // If time is later than 10 past the hour
        outTime = sHours + ":" + sMinutes;					// Populate formatted time string (hrs:mins)

        return outTime;		// Return formatted time to leave (00:00)
    }
    
    // ****************************************
    // Calculates time left until end of shift
    // ****************************************
    public String[] timeLeft(String outTime)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");                                       // Format the time object to HH:mm
        LocalDateTime now = LocalDateTime.now();  															// Current time

        float result = (startingTimeMins(outTime) - startingTimeMins(String.valueOf(dtf.format(now))));		// Input time minus current time

        if (result <= 0) // If the leaving time is in the past, return "false" in element [0]
        {
            return new String[]
            {
                "false"
            };
        }

        String time = String.valueOf(result / 60);				// Convert minutes to hours
			
        String[] splitTime = time.split("\\.");					// Split hours at the decimal
			
        float hrs = Float.valueOf(splitTime[0]);				// Convert hours into float
        float mins = Float.valueOf("." + splitTime[1]) * 60;                       		// Convert fraction of hours into minutes
				
        String[] retTime = {String.valueOf((int) hrs), String.valueOf((int) mins)};		// Put hours and minutes into array so they can be separated
        return retTime;                                                            		// Return array containing the hours and minutes left
    }
	
	// ***************************************************
    // Handler for error messages to avoid duplicate code
    // ***************************************************
	public String errorMessage(int errorMsg)
	{
		String message;
		
		switch(errorMsg)
		{
			case 1: 
				message = "\n\n---------------------------------\n"
						+ "ERROR! Please enter a valid time."
						+ "\n---------------------------------";
				break;
			
			case 2: 
				message = "\n\n----------------------------------------\n"
						+ "ERROR! Please enter a valid break length."
						+ "\n----------------------------------------";
				break;
			
			case 3:
				message = "\n\n---------------------------------\n" 
						+ "ERROR! Please input numbers only."
						+ "\n---------------------------------";
				break;
			
			case 4:
				message = "\n\n---------------------------------\n"
						+ "ERROR! Please select a valid option."
						+ "\n---------------------------------";
				break;
			
			default:
				message = "\n\n---------------------------------\n"
						+ "ERROR! Please try again."
						+ "\n---------------------------------";
				break;
		}

		return message;
	}
	
	// *************************************************
    // Override of errorMessage that uses custom string
    // *************************************************
	public String errorMessage(String errorMsg, int hyphenCount)
	{
		String hyphens = "";
		
		// Build string with number of hyphens stated in method call
		// This means error messages will be correctly framed
		for (int i = 0; i < hyphenCount; i++)		
			hyphens += "-";
		
		// Build string for error message
		String message = "\n\n" + hyphens + "\n" 
						+ "ERROR! " + errorMsg 
						+ "\n" + hyphens;
		
		return message;
	}
}