import java.util.TreeSet;
import java.util.NoSuchElementException;

/**********************************************************************/
// NaiveHeap:  supports priority queue operations on elements
// in a fixed range [i, i+n-1] in O(log n) time.
/**********************************************************************/
public class NaiveHeap {

    TreeSet<Integer> heap;  // a set which maintains sorted order
    private int leftEndVal; // smallest integer in range that can be stored
    private int lengthVal;  // length of range of ints there can be

    public int getSize() {
        return heap.size();
    }

    /******************************************************************/
    // a naive heap which uses a modified TreeSet to implement heap functions
    // javadoc states add/remove/contain are all O(log n)
    /******************************************************************/
    public NaiveHeap(int leftRange, int len) {
        leftEndVal  = leftRange;
        lengthVal   = len;
        heap        = new TreeSet<Integer>();
    }

    /******************************************************************/
    // current minimum (-1 if nothing stored)
    /******************************************************************/
    public int findMin()
    {
        try { return heap.first(); }
        catch(NoSuchElementException e) {return -1;}
    }

    /******************************************************************/
    //  returns false if the item cannot be stored, either
    //  because it is out of range or because the integer is already stored.
    /******************************************************************/
    public boolean insert(int i) // insert a new element i
    {
        if(i < leftEndVal || i >= (leftEndVal+lengthVal)) return false;
        return heap.add(i); // returns true if set did _not_ already contain the element
    }

    /******************************************************************/
    // extract and return the minimum element (-1 if nothing stored)
    /******************************************************************/
    public int extractMin()
    {
        try {
            int f = heap.first();
            heap.remove(f);
            return f;
        } catch(NoSuchElementException e) {return -1;}
    }

    public void printContents(int numIndents) {
        for(int i=0; i < numIndents; i++)
            System.out.print("\t");
        System.out.print("Naive Heap\n");
        for(int i=0; i < numIndents; i++)
            System.out.print("\t");
        System.out.print(   "leftEndVal:"   +leftEndVal+
                            " lenVal:"      +lengthVal+
                            " elements:"    +heap+"\n");
    }
    public void printContents() {
        printContents(0);
    }
}
