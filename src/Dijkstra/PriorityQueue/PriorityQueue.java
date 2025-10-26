package Dijkstra.PriorityQueue;

import Dijkstra.AdjacencyList.Element;

/**
 * @author  badma025
 * @version 1.0.0
 * @since   2025-10-26
 * Copyright (c) 2025 badma025. ALL RIGHTS RESERVED.
 */

public class PriorityQueue
{   //The front of the priority queue, e.g the 1st element
    private Element front;
    private int length = 0;

    public boolean isEmpty()
    {//Return if the queue is empty
        return length == 0;
    }


    public String remove(String value)
    {
        Element current = front;

        // Iterate until we find the matching element or reach the end of the list
        while (current != null && !current.Value().equals(value))
        {
            current = current.Next();
        }
        if (current == null)
        {
            throw new IllegalArgumentException("Value not found in priority queue");
        }

        // If the element to remove is the front, update the front pointer
        if (current == front)
        {
            front = current.Next();
            if (front != null)
            {
                front.Previous(null);
            }
        }
        else
        {
            // Re-link the previous element's next pointer
            if (current.Previous() != null)
            {
                current.Previous().Next(current.Next());
            }
            // Re-link the next element's previous pointer
            if (current.Next() != null)
            {
                current.Next().Previous(current.Previous());
            }
        }

        length--;
        return value;
    }


    public String pop()
    {//Remove the 1st element from the queue and reorganise the queue

        // Throw an error if the queue is empty
        if (length == 0)
        {
            throw new UnsupportedOperationException();
        }

        // retrieve the front element
        String frontElement = front.Value();

        // update the next element (if it exists) to be the new front element
        if (front.Next() != null)
        {
            front = front.Next();
            front.Previous(null);
        }
        else
        {
            front = null;
        }


        length--;
        return frontElement;
    }

    public void add(String value, long priority)
    {
        length++;
        Element current = front;
        Element toAdd = new Element(value, null, null, priority);

        // if there are no elements
        if (front == null)
        {
            front = toAdd;
            return;
        }

        // iterate through the array to find an element with lesser priority
        while (current != null)
        {
            if (toAdd.Priority() < current.Priority())
            {
                // inserting toAdd before current
                toAdd.Next(current);
                toAdd.Previous(current.Previous());

                if (current.Previous() != null)
                {
                    current.Previous().Next(toAdd);
                }
                // if toAdd has the most priority (i.e. inserted at the front)
                else
                {
                    front = toAdd;
                }

                current.Previous(toAdd);
                // stop iterating here
                return;
            }

            // if we have reached the end of the queue, break
            if (current.Next() == null) break;
            current = current.Next();
        }

        // if we found no elements with lesser priority, we insert at the end of the queue
        current.Next(toAdd);
        toAdd.Previous(current);
    }
}
