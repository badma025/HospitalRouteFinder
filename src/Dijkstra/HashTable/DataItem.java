package Dijkstra.HashTable;

/**
 * @author  badma025
 * @version 1.0.0
 * @since   2025-10-26
 * Copyright (c) 2025 badma025. ALL RIGHTS RESERVED.
 */

public class DataItem
{
    private String key;
    private String value;

    public DataItem(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    public String Value()
    {
        return value;
    }

    public String Key()
    {
        return key;
    }

    public void Key(String key)
    {
        this.key = key;
    }

    public void Value(String value)
    {
        this.value = value;
    }
}
