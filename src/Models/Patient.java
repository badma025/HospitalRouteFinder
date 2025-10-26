package Models;

/**
 * @author  badma025
 * @version 1.0.0
 * @since   2025-10-26
 * Copyright (c) 2025 badma025. ALL RIGHTS RESERVED.
 */

public class Patient
{
    private int ID;
    private String FirstName;
    private String LastName;
    private int Age;
    private char Sex;

    public Patient(int ID, String FirstName, String LastName, int Age, char Sex)
    {
        this.ID = ID;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Age = Age;
        this.Sex = Sex;
    }

    public Patient(String FirstName, String LastName, int Age, char Sex)
    {
        this.ID = ID;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Age = Age;
        this.Sex = Sex;
    }

    public String LastName()
    {
        return LastName;
    }

    public int ID()
    {
        return ID;
    }

    public char Sex()
    {
        return Sex;
    }

    public int Age()
    {
        return Age;
    }

    public String FirstName()
    {
        return FirstName;
    }
}
