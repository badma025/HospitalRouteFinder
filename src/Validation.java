import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author  badma025
 * @version 1.0.0
 * @since   2025-10-26
 * Copyright (c) 2025 badma025. ALL RIGHTS RESERVED.
 */

public class Validation
{

    // validates that the user enters an integer within a specified range [min, max]
    public int validateIntegerInRange(Scanner scanner, int min, int max)
    {
        int input;

        if (scanner.hasNextInt())
        {
            input = scanner.nextInt();

            if (input >= min && input <= max)
            {
                return input;
            }
            else
            {
                System.out.println();
                System.out.print("Error: entry must be an integer between " + min + " and " + max + ": ");
            }
        }
        else
        {
            System.out.println();
            System.out.print("Enter a valid number: ");
            scanner.next();
        }

        return validateIntegerInRange(scanner, min, max);
    }

    // validates that the user enters an age greater than 0
    public int validateAge(Scanner scanner, int min)
    {
        int input;

        if (scanner.hasNextInt())
        {
            input = scanner.nextInt();

            if (input >= min)
            {
                return input;
            }
            else
            {
                System.out.println();
                System.out.print("Error: enter a positive number: ");
            }
        }
        else
        {
            System.out.println();
            System.out.print("Enter a valid number: ");
            scanner.next();
        }

        return validateAge(scanner, min);
    }

    // validates that the user enters a character contained in a given set of allowed characters
    public char validateCharacterSet(Scanner scanner, ArrayList<Character> characters)
    {
        String input;

        if (scanner.hasNext())
        {
            input = scanner.next();

            if (characters.contains(input.charAt(0)))
            {
                return input.charAt(0);
            }
            else
            {
                System.out.println();
                System.out.print("Please only use the characters allowed: ");
            }
        }

        return validateCharacterSet(scanner, characters);
    }

    // validates that the user enters a date in yyyy-MM-dd format and ensures it is not in the past
    public LocalDate validateDate(Scanner scanner)
    {
        String input;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        input = scanner.next();

        try
        {
            LocalDate date = LocalDate.parse(input, formatter);

            if (date.isBefore(LocalDate.now()))
            {
                System.out.println();
                System.out.println("Error: Date cannot be in the past.");
                System.out.print("Try again: ");
                return validateDate(scanner);
            }

            return date;
        } catch (DateTimeParseException e)
        {
            System.out.print("Error: Please enter the date in the correct format (yyyy-MM-dd): ");
            return validateDate(scanner);
        }
    }

    // validates that the user enters a valid integer (without restricting range)
    public int validateInteger(Scanner scanner)
    {
        if (scanner.hasNextInt())
        {
            return scanner.nextInt();
        }
        else
        {
            System.out.println("Error: Please enter a valid integer: ");
            System.out.println();
            scanner.next();
            return validateInteger(scanner);
        }
    }

}
