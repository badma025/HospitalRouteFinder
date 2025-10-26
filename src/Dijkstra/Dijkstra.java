package Dijkstra;

import Dijkstra.AdjacencyList.AdjacencyList;
import Dijkstra.AdjacencyList.LinkedList;
import Dijkstra.AdjacencyList.Element;
import Dijkstra.HashTable.HashTable;
import Dijkstra.PriorityQueue.PriorityQueue;

import java.util.NoSuchElementException;

/**
 * @author  badma025
 * @version 1.0.0
 * @since   2025-10-26
 * Copyright (c) 2025 badma025. ALL RIGHTS RESERVED.
 */

public class Dijkstra
{

    // finds the distance AND the shortest path to the node.
    // The last index of the returned linked list contains the shortest distance to the target node,
    // and the rest of the indices are the path itself.
    private static LinkedList dijkstra(AdjacencyList adj, String source, String target)
    {


        // Check if the target node exists in the graph
        if (!adj.contains(target))
        {
            throw new NoSuchElementException("That node does not exist");
        }

        // setting up our data structures.
        PriorityQueue pq = new PriorityQueue();
        LinkedList nodes = adj.getNodes();
        HashTable isVisited = new HashTable();
        HashTable distances = new HashTable();
        HashTable parent = new HashTable();

        distances.add(source, "0");
        final String NO_PARENT_MARKER = "-1";

        // adding the nodes to the priority queue and initialising hash tables.
        Element current = nodes.Front();
        while (current != null)
        {
            String nodeValue = current.Value();
            if (nodeValue.equals(source))
            {
                pq.add(source, 0);
                distances.add(nodeValue, "0");
            }
            else
            {
                pq.add(nodeValue, Integer.MAX_VALUE);
                distances.add(nodeValue, String.valueOf(Integer.MAX_VALUE));
            }
            isVisited.add(nodeValue, "false");
            parent.add(nodeValue, NO_PARENT_MARKER); // initialise parent to -1 (no parent yet)
            current = current.Next();
        }

        // process nodes until the priority queue is empty
        while (!pq.isEmpty())
        {
            String node = pq.pop();


            // skip if this node is already visited
            if (isVisited.item(node).equals("true")) continue;
            isVisited.update(node, "true");

            // get the neighbors for this node.
            LinkedList neighbours = adj.item(node);
            Element neighbour = neighbours.Front();
            while (neighbour != null)
            {

                if (isVisited.item(neighbour.Value()).equals("true"))
                {
                    neighbour = neighbour.Next();
                    continue;
                }

                long currentDistance = Long.valueOf(distances.item(node));
                long neighbourDistance = Long.valueOf(distances.item(neighbour.Value()));
                long weight = neighbour.Weight();

                // if a shorter path is found, update distance and parent
                if (currentDistance + weight < neighbourDistance)
                {
                    distances.update(neighbour.Value(), String.valueOf(currentDistance + weight));
                    pq.remove(neighbour.Value());  // remove the old entry in the priority queue
                    parent.update(neighbour.Value(), node);
                    pq.add(neighbour.Value(), currentDistance + weight); // re-add with updated priority
                }
                neighbour = neighbour.Next();
            }
        }

        // reconstruct the shortest path from target back to source.
        LinkedList path = new LinkedList();
        String retracedNode = target;
        while (retracedNode != null && !retracedNode.equals(NO_PARENT_MARKER))
        {
            path.append(retracedNode, 0);
            if (retracedNode.equals(source)) break;
            retracedNode = parent.item(retracedNode);
        }
        path.reverse();

        // append the shortest distance at the end of the path
        path.append(distances.item(target), 0);

        return path;
    }

    // returns the shortest distance to the target node
    public static String distance(AdjacencyList adj, String source, String target)
    {
        LinkedList path = dijkstra(adj, source, target);
        return path.get(path.length() - 1).Value();
    }

    // returns the shortest path (without the distance) to the target node
    public static String[] shortestPath(AdjacencyList adj, String source, String target)
    {
        LinkedList path = dijkstra(adj, source, target);
        path.pop(path.length() - 1); // remove the distance element
        return path.asArray();
    }
}
