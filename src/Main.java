import java.util.Scanner;

/**
 * @author  badma025
 * @version 1.0.0
 * @since   2025-10-26
 * Copyright (c) 2025 badma025. ALL RIGHTS RESERVED.
 */

public class Main
{
    public static void main(String args[])
    {
        Scanner in = new Scanner(System.in);
        DatabaseConnect conn = new DatabaseConnect();
        Menu menu = new Menu();
        Validation validation = new Validation();

        final int RETURN_CHOICE = 0;
        final int minDigit = 0;
        final int maxDigit = 9;

        int response;
        System.out.println("Welcome to Guy's Hospital!");
        do
        {
            menu.display(menu.mainMenu());
            System.out.println();
            System.out.println();
            System.out.print("Press '0' to return to the main menu, or any other key to exit: ");
            response = validation.validateIntegerInRange(in, minDigit, maxDigit);

        } while (response == RETURN_CHOICE);


        if (conn != null)
        {
            conn.close();
        }
    }
}
