import java.util.Scanner;

public class VEBTest
{
   public static void main (String[] args)
   {
        Scanner Input = new Scanner(System.in);

        int leftEnd, length;
        System.out.print("Left end of the tree's range:  ");
        leftEnd = Input.nextInt();
        System.out.println(leftEnd);

        System.out.print("Length of the tree's range:  ");
        length = Input.nextInt();
        System.out.println(length);

        VEBTree heap = new VEBTree(leftEnd, length);
        NaiveHeap NH = new NaiveHeap(leftEnd, length);

        String [] MenuChoices =
        {
           "\n\n ------------------------------ \n",
           "\n\n     MENU  \n\n",
           "0.  Exit",
           "1.  Insert an integer to naive heap",
           "2.  Find min on naive heap",
           "3.  Extract min on naive heap",
           "4.  Print contents of a naive heap",
           "5.  Insert an integer to VEB heap",
           "6.  Find min on VEB heap",
           "7.  Extract min on VEB heap",
           "8.  Print contents of VEB heap",
           "9.  Print contents of side heap"
        };

        int choice; // menu option choice
        do
        {
            for (int i = 0; i < MenuChoices.length; i++)
               System.out.println(MenuChoices[i]);

            System.out.print ("\n\n     Which? ");
            choice = Input.nextInt();
            System.out.print ("\n");
            if(choice == 1) {
                int newVal;

                System.out.print ("\n\nInteger to insert: ");
                newVal = Input.nextInt();
                System.out.println("\n" + newVal);
                if (NH.insert(newVal))
                    System.out.println("Successful");
                else
                    System.out.println("Unsuccessful");
            }
            else if(choice == 2)
                System.out.println("Current min = " + NH.findMin());
            else if(choice == 3)
                System.out.println ("Extracted item: " + NH.extractMin());
            else if(choice == 4)
                NH.printContents();
            else if (choice == 5)
            {
                int newVal;

                System.out.print ("\n\nInteger to insert: ");
                newVal = Input.nextInt();
                System.out.println("\n" + newVal);
                if (heap.insert(newVal))
                    System.out.println("Successful");
                else
                    System.out.println("Unsuccessful");
            }
            else if (choice == 6)
                System.out.println("Current min = " + heap.findMin());
            else if (choice == 7)
                System.out.println ("Extracted item: " + heap.extractMin());
            else if (choice == 8)
                heap.printContents();
            else if (choice == 9)
                heap.printSideHeap();
        } while (choice != 0);
   }
}

