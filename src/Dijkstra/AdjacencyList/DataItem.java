package Dijkstra.AdjacencyList;

/**
 * @author  badma025
 * @version 1.0.0
 * @since   2025-10-26
 * Copyright (c) 2025 badma025. ALL RIGHTS RESERVED.
 */

public class DataItem
{
    private String key;
    private LinkedList value;
    private boolean isDeleted;

    public DataItem(String key, LinkedList value)
    {
        this.key = key;
        this.value = new LinkedList();
        this.isDeleted = false;
    }

    public LinkedList Value()
    {
        return value;
    }

    public String Key()
    {
        return key;
    }

    public boolean IsDeleted()
    {
        return isDeleted;
    }

    public void IsDeleted(boolean value)
    {
        isDeleted = value;
    }
}
