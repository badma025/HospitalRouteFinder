package Models;

/**
 * @author  badma025
 * @version 1.0.0
 * @since   2025-10-26
 * Copyright (c) 2025 badma025. ALL RIGHTS RESERVED.
 */

public class Doctor
{
    private int ID;
    private String FirstName;
    private String LastName;
    private String Specialisation;
    private char Sex;

    public Doctor(int ID, String FirstName, String LastName, String Specialisation, char Sex)
    {
        this.ID = ID;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Specialisation = Specialisation;
        this.Sex = Sex;
    }

    public Doctor(String FirstName, String LastName, String Specialisation, char Sex)
    {
        this.ID = ID;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Specialisation = Specialisation;
        this.Sex = Sex;
    }

    public char Sex()
    {
        return Sex;
    }

    public int ID()
    {
        return ID;
    }

    public String LastName()
    {
        return LastName;
    }

    public String FirstName()
    {
        return FirstName;
    }

    public String Specialisation()
    {
        return Specialisation;
    }
}
