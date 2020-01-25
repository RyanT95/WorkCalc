
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
        // Display app title
        System.out.println("\n-----------------------------------------\n"
                + "                 Day Calc"
                + "\n-----------------------------------------");

        Scanner input = new Scanner(System.in);		// Open user input

        final int SHIFT_LENGTH = 420; 			// Length of shift in minutes minus break
        String startTime = "00:00";			// Time started work
        int breakLength = 0;				// Length of lunch break
        int numTries = 0;				// Number of unsuccessful attempts at inputting values

        while (startingTimeMins(startTime) == 0)
        {
            // Only show error message after 1 unsuccessful attempt
            if (numTries > 0)
            {
                System.out.print("\n\n---------------------------------\n"
                        + "ERROR! Please enter a valid time!"
                        + "\n---------------------------------");
            }

            numTries++;									// Increment attempts

            try
            {
                System.out.print("\nWhat time did you start? (00:00) - ");
                startTime = input.nextLine();						// User inputs time they started

                System.out.print("\nHow long was your break? (minutes) - ");
                breakLength = input.nextInt();						// User inputs duration of break in minutes

                // If user inputs negative number for break length
                if (breakLength < 0)
                {
                    System.out.print("\n\n----------------------------------------\n"
                            + "ERROR! Please enter a valid break length!"
                            + "\n----------------------------------------");

                    numTries = 0;                                                           // Reset attempts, so 'enter a valid time' message doesn't show again
                    startTime = "00:00";                                                    // Reset start time to "00:00", so while loop executes again
                }

            }
            catch (NumberFormatException | InputMismatchException e)                        // If user enters invalid input (letters)
            {
                System.out.print("\n\n---------------------------------\n"
                        + "ERROR! Please input numbers only!"
                        + "\n---------------------------------");

                numTries = 0;                                                               // Reset attempts, so 'enter a valid time' message doesn't show again
                startTime = "00:00";                                                        // Reset start time to "00:00", so while loop executes again
            }

            input.nextLine();                                                               // Skip next line, for when returning to top of while
        }

        float outTimeMinutes = startingTimeMins(startTime) + SHIFT_LENGTH + breakLength;    // Calculate time to leave in minutes

        // Print time user should leave work in hours and minutes (00:00)
        System.out.println("\n-----------------------------------------\n"
                + "You should leave work at: "
                + calcTime(outTimeMinutes)
                + "\n-----------------------------------------");

        String[] timeLeft = timeLeft(calcTime(outTimeMinutes));                             // Create new string array to capture return values of timeLeft()

        // timeLeft returns "false" in element [0] if the leaving time is in the past
        if (timeLeft[0] == "false")
        {
            System.out.println("You have already finished!"
                    + "\n-----------------------------------------");
        } // If leaving time is not in the past
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

        input.close();                                                                      // Close input
    }
    
    // *************************************************
    // Converts time from Hrs & Mins (00:00) to minutes
    // *************************************************
    public int startingTimeMins(String inTime)
    {
        int inTimeMinutes;

        String[] splitTimeIn = inTime.split(":");	// Split time (00:00) into hours and minutes

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
        catch (InputMismatchException | NumberFormatException e) // If user enters invalid input (letters)
        {
            System.out.print("\n\n---------------------------------\n"
                    + "ERROR! Please input numbers only!"
                    + "\n---------------------------------");
        }

        inTimeMinutes = (Integer.valueOf(splitTimeIn[0]) * 60) + Integer.valueOf(splitTimeIn[1]);	// Convert time into minutes
        return inTimeMinutes;										// Return the starting time in minutes
    }

    // *********************************************************************
    // Converts inputted time in minutes into formatted time string (00:00)
    // *********************************************************************
    public String calcTime(float timeInMins)
    {
        String outTime;
        String sHours;
        String sMinutes;

        float timeInHours = timeInMins / 60;				// Convert time from minutes to hours

        String sTimeInHours = String.valueOf(timeInHours);		// Convert time from float to string

        String[] splitTime = sTimeInHours.split("\\.");			// Split time to leave into hours and fraction of hours

        float hrs = Float.valueOf(splitTime[0]);			// Convert hours into float
        float mins = Float.valueOf("." + splitTime[1]) * 60;            // Convert fraction of hours into minutes

        // If hours is higher than 25, this means the time should be AM, so subtract 24 hours to change PM to AM.
        if (hrs >= 25.0f)
        {
            hrs -= 24.0f;
        }

        // If time is earlier than 10 past the hour, add a zero to the start of minutes
        if (mins < 10)
        {
            sMinutes = String.format("0" + String.format("%.00f", mins));		// Populate minutes string to two decimal places, with 0 at the start
        } // If time is later than 10 past the hour
        else
        {
            sMinutes = String.format("%.00f", mins);					// Populate minutes string to two decimal places
        }
        // If time is earlier than 10:00, add a zero to the start of hours
        if (hrs < 10)
        {
            sHours = String.format("0" + "%.00f", hrs);					// Populate hours string to two decimal places, with 0 at the start
        } // If time is later than 10:00
        else
        {
            sHours = String.format("%.00f", hrs);					// Populate hours string to two decimal places
        }
        // If time is later than 10 past the hour
        outTime = sHours + ":" + sMinutes;						// Populate formatted time string (hrs:mins)

        return outTime;									// Return formatted time to leave (00:00)
    }
    
    // ****************************************
    // Calculates time left until end of shift
    // ****************************************
    public String[] timeLeft(String outTime)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");                                           // Format the time object to HH:mm
        LocalDateTime now = LocalDateTime.now();  								// Current time

        float result = (startingTimeMins(outTime) - startingTimeMins(String.valueOf(dtf.format(now))));		// Input time minus current time

        if (result <= 0) // If the leaving time is in the past, return "false" in element [0]
        {
            return new String[]
            {
                "false"
            };
        }

        String time = String.valueOf(result / 60);								// Convert minutes to hours

        String[] splitTime = time.split("\\.");									// Split hours at the decimal

        float hrs = Float.valueOf(splitTime[0]);								// Convert hours into float
        float mins = Float.valueOf("." + splitTime[1]) * 60;                                                    // Convert fraction of hours into minutes
        
        String[] retTime = {String.valueOf((int) hrs), String.valueOf((int) mins)};                             // Put hours and minutes into array so they can be separated
        return retTime;                                                                                         // Return array containing the hours and minutes left
    }
}
