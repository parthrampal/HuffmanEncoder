//package Huffman;
import java.util.NoSuchElementException;
/**
 * @author Parth Rampal
 *
 *This Class Defines a Binary heap and its functions
 */
public class BinaryHeap {

	
	/** Number of children of a node **/
    private static final int d = 2;
    private int heapSize;
    private HuffmanNode[] heap;
 
    /** Constructor **/    
    public BinaryHeap(int capacity)
    {
        heapSize = 0;
        heap = new HuffmanNode[capacity];
    }
 
    /** Function to get Heap size **/
    public int getSize()
    {
    	return heapSize;
    }
    
    
    /** Function to check if heap is empty **/
    public boolean isEmpty( )
    {
        return heapSize == 0;
    }
 
    /** Check if heap is full **/
    public boolean isFull( )
    {
        return heapSize == heap.length;
    }
 
 
    /** Function to insert element */
    public void insert(HuffmanNode x)
    {
    	heapSize++;
        int i = heapSize;
        while(i > 1 && heap[parent(i)].getfrequency() > x.getfrequency()) {
                heap[i] = heap[parent(i)];
                i = parent(i);
        }
        heap[i] = x;
    }
    
    /** Function to  get index parent of ith node **/
    private int parent(int i) 
    {
        return i/d;
    }
 
    /** Function to get index of kth child of ith node **/
    private int kthChild(int i, int k) 
    {
        return d * i + k;
    }
 
    /** Function to find least element **/
    public HuffmanNode findMin( )
    {
        if (isEmpty() )
            throw new NoSuchElementException("Underflow Exception");           
        return heap[1];
    }
 
    /** Function to delete min element **/
    public HuffmanNode deleteMin()
    {
    	if(isEmpty())
    	{
    		throw new NoSuchElementException("Underflow Exception");
    	}
    	HuffmanNode min = heap[1];
        heap[1] = heap[heapSize];
        heapSize--;
        heapifyDown(1);
        return min;
    	
    }
 
 
    /** Function heapifyDown **/
    public void heapifyDown(int i) {
        int l = kthChild(i,0);
        int r = kthChild(i,1);
        int smallest;
        if(r <= heapSize) {
                if(heap[l].getfrequency() < heap[r].getfrequency())
                        smallest = l;
                else
                        smallest = r;
                if(heap[i].getfrequency() > heap[smallest].getfrequency()) {
                        swap(i, smallest);
                        heapifyDown(smallest);
                }
        }
        else if(l == heapSize && heap[i].getfrequency() > heap[l].getfrequency()) {
                swap(i, l);
        }               
}

private void swap(int i, int l) {
	HuffmanNode tmp = heap[i];
        heap[i] = heap[l];
        heap[l] = tmp;
}
 
}
