package Models;

/**
 * @author  badma025
 * @version 1.0.0
 * @since   2025-10-26
 * Copyright (c) 2025 badma025. ALL RIGHTS RESERVED.
 */

public class Location
{
    private int ID;
    private String LocationName;
    private String Type;

    public Location(int ID, String LocationName, String Type)
    {
        this.ID = ID;
        this.LocationName = LocationName;
        this.Type = Type;
    }

    public int ID()
    {
        return ID;
    }

    public String LocationName()
    {
        return LocationName;
    }

    public String Type()
    {
        return Type;
    }
}
