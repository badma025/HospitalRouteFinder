package Dijkstra.HashTable;

/**
 * @author  badma025
 * @version 1.0.0
 * @since   2025-10-26
 * Copyright (c) 2025 badma025. ALL RIGHTS RESERVED.
 */

public class HashTable
{
    private int size = 0;
    private final int MAX_SIZE = 60; // max expected locations + buffer
    private Dijkstra.HashTable.DataItem[] HashArray = new DataItem[MAX_SIZE];
    private final String DELETED_KEY_MARKER = "-1";
    private final String DELETED_VALUE_MARKER = "DELETED";

    public String[] asArray()
    {
        String[] result = new String[MAX_SIZE];
        for (int i = 0; i < MAX_SIZE; i++)
        {
            if (HashArray[i] == null) result[i] = null;
            else result[i] = HashArray[i].Value();
        }

        return result;
    }


    public void add(String Key, String Value)
    {

//      we set up our new element to be added, along with the index it needs to be hashed at
        DataItem toAdd = new DataItem(Key, Value);
        int hashIndex = hashFunction(Key);

//      if there is an element already present at the given index, we iterate until we find a null element or a deleted element
        while (HashArray[hashIndex] != null && !HashArray[hashIndex].Value().equals("DELETED"))
        {
//           if the hash table is full
            if (size == MAX_SIZE)
            {
                throw new UnsupportedOperationException();
            }

//          if there is already an element present with the given key
            if (HashArray[hashIndex] != null && HashArray[hashIndex].Key().equals(Key))
            {
                update(Key, toAdd.Value());
            }

            hashIndex = (hashIndex + 1) % MAX_SIZE;
        }

//      if there is already an element present with the given key
        if (HashArray[hashIndex] != null && HashArray[hashIndex].Key().equals(Key))
        {
            throw new IllegalArgumentException();
        }

//      if there is a free space
        HashArray[hashIndex] = toAdd;

        size++;
    }

    //  returns the value to the corresponding key, or throws an error if it doesn't exist
    public String item(String Key)
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

    //  deletes an element
    public void delete(String Key)
    {
        int hashIndex = hashFunction(Key);

//      if the element stored at hashIndex is not the one being looked for, we keep iterating until we find it, or a whole loop of the array to see that it doesn't exist
        while (HashArray[hashIndex] != null)
        {
//           if the key is the one being looked for
            if (HashArray[hashIndex].Key().equals(Key))
            {
                break;
            }

            hashIndex = (hashIndex + 1) % MAX_SIZE;

//          if we've done a whole loop of the array and not found the key
            if (hashIndex == hashFunction(Key))
            {
                throw new IllegalArgumentException();
            }
        }

//      if we've reached a null element, or the value stored in the initial hashIndex was null
        if (HashArray[hashIndex] == null)
        {
            throw new IllegalArgumentException();
        }

//      we delete the element
        HashArray[hashIndex].Key(DELETED_KEY_MARKER);
        HashArray[hashIndex].Value(DELETED_VALUE_MARKER);
        size--;
    }

    //  this is basically identical to the add() method; it just updates the key with the given value
    public void update(String Key, String value)
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
            HashArray[hashIndex].Value(value);
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    //  checks the hashtable to see if the given key is present
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


    public int length()
    {
        return size;
    }


    public boolean isEmpty()
    {
        return size == 0;
    }

    public int hashFunction(String key)
    {
        int sum = 0;
        for (int i = 0; i < key.length(); i++)
        {
            sum += key.charAt(i);
        }
        return sum % MAX_SIZE;
    }
}
