/**
 * 
 */
package org.luc.tangle.main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {
    // map is used to found links with node
    private static Map<Node, Set<Link>> map = new HashMap<Node, Set<Link>>();
    // one node in a rectangle
    private static Node lastFound = null;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input;
        /* every line of inputs presents one line.*/
        while (!(input = sc.nextLine()).equals("exit")) {
            if (null == lastFound) // if there is a rectangle?
                caclRectangle(input);
            else
                printRectangle(lastFound);
        }
        sc.close();
    }
    /*
     * Calculate if there is a rectangle related with the input line
     */
    private static void caclRectangle(String input) {
        String[] ins = input.split(" ");
        // get 2 nodes and 1 link
        Node n1 = new Node(Integer.valueOf(ins[0]), Integer.valueOf(ins[1]));
        Node n2 = new Node(Integer.valueOf(ins[2]), Integer.valueOf(ins[3]));
        Link link = new Link(n1, n2);
        
        // nodes set used to find all lines related, and the start node is n1
        Set<Node> lastSearched = new HashSet<Node>();
        lastSearched.add(n1);
        n1.lastNode = n2;
        // except the input line, we need to find other three line of rectangle
        for (int i = 0; i < 3; i++) {
            /* currentSearched will added with all nodes linked with nodes in
               lastSearched  */
            Set<Node> currentSearched = new HashSet<Node>(lastSearched.size() << 2);
            for (Node node : lastSearched) {
                // find nodes linked with a node and add them to currentSearched
                currentSearched.addAll(getLinkedNode(node));
            }
            // next cycle will use nodes in currentSearched to find next nodes
            lastSearched = currentSearched;
            // lastSearched is empty means there is no rectangle until now
            if (lastSearched.isEmpty())
                break;
        }
        if (lastSearched.isEmpty()) {
            System.out.println("no tangle");
        } else {
            for (Node node : lastSearched) {
                /* if there is a node in lastSearched, and that node is equal
                 * with n2, it means there must be three lines with the input
                 * line they can form a rectangle.
                 */
                if (node.equals(n2)) {
                    lastFound = node;
                    printRectangle(node);
                    break;
                }
            }
        }
        
        // add the input line to node-to-link map
        Set<Link> sLink = map.get(n1);
        if (null == sLink) {
            sLink = new HashSet<Link>();
            map.put(n1, sLink);
        }
        sLink.add(link);
        sLink = map.get(n2);
        if (null == sLink) {
            sLink = new HashSet<Link>();
            map.put(n2, sLink);
        }
        sLink.add(link);
    }
    
    /*
     * print a rectangle points
     */
    private static void printRectangle(Node node) {
        for (int i = 0; i < 4; i++, node = node.lastNode) {
            System.out.print(node.toString());
            if (i != 3) {
                System.out.print(" ---> ");
            }
        }
        System.out.println();
    }
    
    /*
     * get the linked nodes with the given node
     */
    private static Set<Node> getLinkedNode(Node node) {
        Set<Link> links = new HashSet<Link>();
        if (null != map.get(node)) {
            links.addAll(map.get(node));
        }
        Set<Node> nodes = new HashSet<Node>();
        for (Link link : links) {
            Node another = link.anotherNode(node);
            if (!another.equals(node.lastNode)) {
                nodes.add(another);
                /* set the lastNode element to the given node, it will be
                 * useful when print the rectangle
                 */
                another.lastNode = node;
            }
        }
        return nodes;
    }
}

/**
 * node class
 */
class Node {
    public int x; // x coordinate
    public int y; // y coordinate
    // present which node that this node is found according to.
    // maybe should be called preNode
    public Node lastNode;
    
    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        int result = 31;
        result += x * 7 + 1;
        result += y * 7 + 1; 
        return result;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if (!(obj instanceof Node)) {
            return false;
        }
        if (this != obj) {
            Node other = (Node) obj;
            return (this.x == other.x) && (this.y == other.y);
        }
        return true;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "(" + x + ", " + y + ")";
    }
}

/**
 * link class, maybe should be renamed to line
 */
class Link {
    public Node node1;
    public Node node2;
    
    public Link(Node n1, Node n2) {
        node1 = n1;
        node2 = n2;
    }
    
    public Node anotherNode(Node node) {
        if (node1.equals(node)) {
            return node2;
        } else {
            return node1;
        }
    }
}
