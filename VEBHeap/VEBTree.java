import java.lang.Math;
/**********************************************************************/
// author: crt
// cs420 homework
// van Emde Boas trees:  supports priority queue operations on elements
// in a fixed range [i, i+n-1] in O(log log n) time.
/**********************************************************************/

public class VEBTree
{
   private int storedItemsVal;      // number of items stored
   private int minVal;              // current minimum
   private int leftEndVal;          // smallest integer in range that can be stored
   private int lengthVal;           // length of range of ints there can be
   private NaiveHeap BaseCaseHeap;  // trivial structure used in base case
   private VEBTree[] SubTrees;      // subtrees of root
   private VEBTree SideHeap;        // side heap for managing indices

   public int length()              // length of tree (number of items in range)
   {
       return lengthVal;
   }

   public int storedItems()         // number of items currently stored
   {
       return storedItemsVal;
   }

   public int findMin()             // current minimum (-1 if nothing stored)
   {
       if(isBaseCase()) return BaseCaseHeap.findMin();
       return minVal;
   }

   public int leftEnd()             // first integer in the tree's range
   {
       return leftEndVal;
   }

   public boolean isEmpty()         // true if nothing currently stored
   {
       return storedItems() == 0;
   }

   public int subtreeBucket(int i)  // which subtree does i hash to?
   {
        if(!inRange(i)) return -1;
        return (int) Math.floor((i-leftEnd()) / subtreeLength());
   }

   // length of each subtree (except last, which may be an oddball)
   public int subtreeLength()
   {
       return (int) Math.floor(Math.sqrt((double) length()));
   }

   // number of children of the root
   public int rootDegree()
   {
         int temp = length() / subtreeLength();
         if (length() % subtreeLength() > 0)
             return temp+1;
         else
             return temp;
   }

   // true if length puts the tree in the trivial base case
   public boolean isBaseCase()
   {
       return length() <= 3;
   }

   // true if i is in the tree's range of integers it can store
   public boolean inRange(int i)
   {
        if(i < leftEnd() || i >= (leftEnd()+length())) return false;
        return true;
   }

   /******************************************************************/
   //  The procedure returns false if the item cannot be stored, either
   //  because it is out of range or because the integer is already stored.
   /******************************************************************/
   public boolean insert(int i)                                     // insert a new element i in O(lglg n)
   {
       if(!inRange(i) || i == findMin()) return false;
       else if(isBaseCase()) {                                      // naive heap insert O(1)
            if(!BaseCaseHeap.insert(i)) return false;
       }
       else if(!inRange(findMin())) minVal = i;                     // set min O(1)
       else {                                                       // insert i into it's bucket T(n)=T(sqrt(n))+O(1) worst case
            if(i < findMin()) {                                     // swap i and min O(1)
                int temp    = i;
                i           = findMin();
                minVal      = temp;
            }
            if(SubTrees[subtreeBucket(i)].isEmpty())                // if bucket going non-empty, update SideHeap
                SideHeap.insert(subtreeBucket(i));                  // T(sqrt(n)) worst-case

            // insert i in it's bucket, consider the 2 possible cases at this point;
            // case1:   bucket(i) is empty, O(1) to set min for that bucket
            //          T(sqrt(n)): to insert bucket(i) into the side heap
            // case2:   bucket(i) is already non-empty, no update for the SideHeap,
            //          T(sqrt(n)): insert i into bucket(i).
            if(!SubTrees[subtreeBucket(i)].insert(i)) return false; // check if element already in a bucket
       }
       storedItemsVal++;
       return true;
   }

   /******************************************************************/
   // extract the minimum element in O(lglg n) time
   // -1 if empty or the element does not exist
   /******************************************************************/
   public int extractMin()
   {
        if(storedItems() == 0) return -1;                       // no elements in tree
        int result = findMin();
        storedItemsVal--;
        if(isBaseCase())                                        // naive heap extract-min O(1)
            return BaseCaseHeap.extractMin();
        else if(isEmpty())                                      // minVal is only element, turn light out on this tree
            minVal = leftEnd()-1;
        else {                                                  // vEB tree with values in 1 or more of it's buckets
            int i   = SideHeap.findMin();
            minVal  = SubTrees[i].extractMin();
            // case1:   O(1): extract last element from bucket(i)
            //          T(sqrt(n)): extract-min on SideHeap
            // case2:   T(sqrt(n)): if bucket(i) does not go empty
            //          no operations to side heap
            if(SubTrees[i].isEmpty()) SideHeap.extractMin();
        }
        return result;
   }

   // print out current contents of side heap ..
   public void printSideHeap()
   {
        if(!isBaseCase()) {
            System.out.println("***** vEB Tree Side Heap*****");
            SideHeap.printContents();
        } else {
            System.out.println("No SideHeap in base case");
        }
   }

   public void printContents(int numIndents)
   {
        for(int i=0; i < numIndents; i++)
            System.out.print("\t");
        System.out.print("***** vEB Tree *****\n");

        if(isBaseCase()) BaseCaseHeap.printContents(numIndents);
        else {
            for(int i=0; i < numIndents; i++)
                System.out.print("\t");
            System.out.print(   "leftEndVal:"       +leftEnd()+
                                " lengthVal:"       +length()+
                                " storedItemsVal:"  +storedItems()+
                                " minVal:"          +findMin()+"\n");
            printContentsRecursive(numIndents);
        }

        System.out.println();
   }

   // print out current set of items stored in the tree ...
   // keep this around for the grading script
   public void printContents() {
        printContents(0);
   }

   private void printContentsRecursive(int numIndents)
   {
        for(int i=0; i < SubTrees.length; i++) {
            for(int j=0; j < numIndents; j++)
                System.out.print("\t");
            System.out.print("Bucket:"+i+"\n");
            SubTrees[i].printContents(numIndents+1);
        }
   }

   /*****************************************************************/
   // The ranges of the O(sqrt(n)) recursive subtrees are a partition of the
   // range of the root into ranges of length O(sqrt(n)).  In addition, we
   // keep a recursive "side heap" that keeps the indices of the nonempty
   // subtrees.
   /*****************************************************************/
   public VEBTree(int leftRange, int len)
   {
        leftEndVal       = leftRange;
        lengthVal        = len;
        storedItemsVal   = 0;
        minVal           = leftEnd() - 1;   // can't be mistaken for a legitimate value
        if (isBaseCase()) {
            BaseCaseHeap = new NaiveHeap(leftRange,len);
            SideHeap     = null;
            SubTrees     = null;
        }
        else
        {
            BaseCaseHeap    = null;
            SideHeap        = new VEBTree(0, rootDegree());
            SubTrees        = new VEBTree[rootDegree()];
            int i           = 0;
            int low         = leftEnd();
            int highest     = (leftEnd()+length())-1;
            while(low <= highest) {
                int high        = Math.min(highest, low+subtreeLength()-1); // catch runt bucket
                int range       = (high-low)+1;                             // size of next bucket
                SubTrees[i++]   = new VEBTree(low, range);
                low += subtreeLength();
            }
        }
   }
}
