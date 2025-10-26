/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dijkstra.AdjacencyList;

/**
 * @author  badma025
 * @version 1.0.0
 * @since   2025-10-26
 * Copyright (c) 2025 badma025. ALL RIGHTS RESERVED.
 */

public class Element
{
    private String value;
    private Element previous;
    private Element next;
    private int weight;
    private long priority;

    public Element(String value, int weight, Element previous, Element next)
    {
        this.value = value;
        this.previous = previous;
        this.next = next;
        this.weight = weight;
    }

    public Element(String value, Element previous, Element next, long priority)
    {
        this.value = value;
        this.previous = previous;
        this.next = next;
        this.priority = priority;
    }

    @Override
    public String toString()
    {
        return value;
    }

    public String Value()
    {
        return value;
    }

    public Element Previous()
    {
        return previous;
    }

    public void Previous(Element value)
    {
        previous = value;
    }

    public Element Next()
    {
        return next;
    }

    public int Weight()
    {
        return weight;
    }

    public void Next(Element value)
    {
        next = value;
    }

    public long Priority()
    {
        return priority;
    }
}

