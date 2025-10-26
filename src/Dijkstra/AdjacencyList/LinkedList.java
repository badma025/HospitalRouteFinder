package Dijkstra.AdjacencyList;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author  badma025
 * @version 1.0.0
 * @since   2025-10-26
 * Copyright (c) 2025 badma025. ALL RIGHTS RESERVED.
 */

public class LinkedList
{   //The front of the priority queue, e.g the 1st element
    private Element front;
    private int length = 0;

    public static void main(String[] args)
    {
    }

    public String[] asArray()
    {//Converts the priority queue into a an Array List
        ArrayList<String> a = new ArrayList<String>();
        Element e = front;

        while (e != null)
        {//Iterate through elements adding to the array
            a.add(e.Value());
            e = e.Next();
        }

        return a.toArray(new String[a.size()]);
    }

    public Element Front()
    {
        return front;
    }

    public boolean isEmpty()
    {//Return if the queue is empty
        return length == 0;
    }

    public void append(String value, int weight)
    {//Append to the end of the list
        Element current;
        Element tail;

        if (front != null)
        {
            current = front;// Start at the front of the LinkedList

            while (current.Next() != null)
            {//Iterate through elements in the LinkedList
                current = current.Next();
            }

            tail = new Element(value, weight, current, null);// Create new tail Element pointing back to the previous Tail
            current.Next(tail);//Update the end of the LinkedList to point to this new Element
        }
        else
        {//Add the front of the linked list
            front = new Element(value, weight, null, null);
        }

        length++;
    }

    public String remove(String value)
    {//Remove the first element that has a matching value, and reorganise the queue      
        //Throw an error if the value is not present
        Element current = front;

        // iterate to the current value
        while (!Objects.equals(current.Value(), value))
        {
            current = current.Next();

            // throwing an error if the element we want to remove cannot be found
            if (current.Next() == null && !current.Value().equals(value))
            {
                throw new IllegalArgumentException();
            }
        }


        // Update the next node's previous pointer (points to the node before our current one)
        if (current.Next() != null)
        {
            // if removing the element causes us to have only one element left, we have to set its previous pointer to null
            if (current.Previous() == null)
            {
                current.Next().Previous(null);
            }
            else
            {
                current.Next().Previous(current.Previous());
            }

        }

        // Update the previous node's next pointer (points to the node after our current one)
        if (current.Previous() != null)
        {
            // if removing the element causes us to have only one element left, we have to set its next pointer to null
            if (current.Next() == null)
            {
                current.Previous().Next(null);
            }
            else
            {
                current.Previous().Next(current.Next());
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
            front.Next().Previous(null);
        }

        length--;
        return frontElement;
    }

    public String pop(int index)
    {
        //Remove from the queue the element at the specified index reorganise the queue

        // Throw an error if the index is invalid
        if (index < 0 || index >= length) throw new IndexOutOfBoundsException();

        Element current = front;
        String popped;

        // Iterate to the index of the element that needs to be popped
        for (int i = 0; i < index; i++)
        {
            current = current.Next();
        }

        // If the index is the first, we can just call the pop() function
        if (index == 0)
        {
            popped = pop();
        }
        // If we are popping the last element, the value before it becomes the new tail, and should not have any .Next() value
        else if (index == length - 1)
        {
            current.Previous().Next(null);
            popped = current.Value();
        }
        // Otherwise, update pointers as normal
        else
        {
            current.Next().Previous(current.Previous());
            current.Previous().Next(current.Next());
            popped = current.Value();
        }

        length--;
        return popped;
    }

    public void insert(String value, int weight, int index)
    {//Insert the item into the correct position in the linked list      

        Element toInsert;

        // Throw an error if the index is invalid
        if (index >= length || index < 0)
        {
            throw new IndexOutOfBoundsException();
        }

        Element current = front;

        // Iterate to the index of the element to be inserted
        for (int i = 0; i < index; i++)
        {
            current = current.Next();
        }

        // Adding to the front of the queue
        if (index == 0)
        {
            // If it's the first element added to the queue
            if (length == 0)
            {
                toInsert = new Element(value, weight, null, null);
            }
            else
            {
                toInsert = new Element(value, weight, null, current);
                toInsert.Next().Previous(toInsert);
            }
            front = toInsert;

        }

        // Adding to the middle/end of the queue
        else
        {
            toInsert = new Element(value, weight, current.Previous(), current);
            current.Previous(toInsert);
            toInsert.Previous().Next(toInsert);
        }

        length++;
    }

    public int index(String value)
    {
        // Return the position of the first occurrence of the value in the linked list

        Element current = front;
        int index = 0;

        // Iterate until the value of current is what we are looking for
        while (!current.Value().equals(value))
        {
            if (current.Next() == null)
            {
                // Throw an error if the value does not exist
                throw new IllegalArgumentException();
            }
            current = current.Next();
            index++;
        }

        return index;
    }

    public int length()
    {//Return the number of elements in the Linked List
        return length;
    }

    public boolean search(String value)
    {//Return true if the value exists in the Linked List
        Element current = front;

        // Iterate until the value of current is what we are looking for
        while (!current.Value().equals(value))
        {
            current = current.Next();

            // If the element cannot be found
            if (current.Next() == null && !current.Value().equals(value))
            {
                return false;
            }
        }

        return true;
    }

    public void reverse()
    {
        Element current = front;
        Element temp = null;

        // Swap the next and previous pointers for all nodes
        while (current != null)
        {
            temp = current.Previous();
            current.Previous(current.Next());
            current.Next(temp);
            current = current.Previous(); // Move to the next node (which was previous)
        }

        // Adjust the front pointer
        if (temp != null)
        {
            front = temp.Previous();
        }
    }

    public Element get(int index)
    {
        // Throw an error if the index is out of bounds
        if (index < 0 || index >= length)
        {
            throw new IndexOutOfBoundsException();
        }

        Element current = front;

        // Traverse to the nth element
        for (int i = 0; i < index; i++)
        {
            current = current.Next();
        }

        return current;
    }

}
