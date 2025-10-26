package Dijkstra.AdjacencyList;

/**
 * @author  badma025
 * @version 1.0.0
 * @since   2025-10-26
 * Copyright (c) 2025 badma025. ALL RIGHTS RESERVED.
 */

public class AdjacencyList
{
    private int size = 0;
    private final int MAX_SIZE = 60; // max expected locations + buffer
    private DataItem[] HashArray = new DataItem[MAX_SIZE];

    public LinkedList getNodes()
    {
        LinkedList elements = new LinkedList();
        for (DataItem i : HashArray)
        {
            if (i != null) elements.append(i.Key(), 0);
        }

        return elements;
    }

    public void add(String sourceNode, String destinationNode, int weight)
    {

//      we set up our new element to be added, along with the index it needs to be hashed at
        int hashIndex = hashFunction(sourceNode);

//      if there is an element already present at the given index, we iterate until we find a null element or a deleted element
        while (HashArray[hashIndex] != null && !HashArray[hashIndex].IsDeleted() && !HashArray[hashIndex].Key().equals(sourceNode))
        {
//           if the hash table is full
            if (size == MAX_SIZE)
            {
                throw new UnsupportedOperationException();
            }


            hashIndex = (hashIndex + 1) % MAX_SIZE;
        }

//      if there is a free space
        if (HashArray[hashIndex] == null)
        {
            HashArray[hashIndex] = new DataItem(sourceNode, new LinkedList());
            HashArray[hashIndex].Value().append(destinationNode, weight);
            size++;
        }
        else HashArray[hashIndex].Value().append(destinationNode, weight);

        HashArray[hashIndex].IsDeleted(false);


    }

    //  adds an edge both ways; useful for undirected graphs
    public void addTwoWayEdge(String sourceNode, String destinationNode, int weight)
    {
        add(sourceNode, destinationNode, weight);
        add(destinationNode, sourceNode, weight);
    }

    //  returns the value mapped to the key (in this case, a LinkedList)
    public LinkedList item(String Key)
    {
        int hashIndex = hashFunction(Key);

//     if the hashIndex does not contain the element being searched for, we iterate until the element stored at hashIndex is null,
//     at which point we know to stop searching
        while (HashArray[hashIndex] != null)
        {
//         if we find the key being searched for
            if (HashArray[hashIndex].Key().equals(Key))
            {
                break;
            }

            hashIndex = (hashIndex + 1) % MAX_SIZE;

//         if after doing an entire loop of the array, the key is not found then we throw this exception
            if (hashIndex == hashFunction(Key))
            {
                throw new IllegalArgumentException();
            }
        }

//      when we stop iterating, the element is either null or the corresponding element to the key
        if (HashArray[hashIndex] != null && HashArray[hashIndex].Key().equals(Key))
        {
            return HashArray[hashIndex].Value();
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    public boolean contains(String Key)
    {
        boolean isPresent = false;
        int hashIndex = hashFunction(Key);

//      we iterate to find the element and exit the loop as soon as it is found
        while (HashArray[hashIndex] != null)
        {
//          if the element is the one being looked for
            if (HashArray[hashIndex].Key().equals(Key))
            {
                isPresent = true;
                break;
            }

            hashIndex = (hashIndex + 1) % MAX_SIZE;

//          if we've done a whole loop of the array and the element is not found
            if (hashIndex == hashFunction(Key))
            {
                isPresent = false;
                break;
            }
        }

//      if the key matches the corresponding element
        if (HashArray[hashIndex] != null && HashArray[hashIndex].Key().equals(Key))
        {
            isPresent = true;
        }

        if (isPresent)
        {
            return true;
        }
        else return false;
    }

    public int hashFunction(String key)
    {
        int total = 0;

        for (int i = 0; i < key.length(); i++)
        {
            total += key.charAt(i);
        }

        return total % MAX_SIZE;
    }
}
