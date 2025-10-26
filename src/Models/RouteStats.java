package Models;

/**
 * @author  badma025
 * @version 1.0.0
 * @since   2025-10-26
 * Copyright (c) 2025 badma025. ALL RIGHTS RESERVED.
 */

public class RouteStats
{
    private int FromLocationID;
    private int ToLocationID;
    private int RequestCount;

    public RouteStats(int FromLocationID, int ToLocationID, int RequestCount)
    {
        this.FromLocationID = FromLocationID;
        this.ToLocationID = ToLocationID;
        this.RequestCount = RequestCount;
    }

    public int FromLocationID()
    {
        return FromLocationID;
    }

    public int RequestCount()
    {
        return RequestCount;
    }

    public int ToLocationID()
    {
        return ToLocationID;
    }
}
