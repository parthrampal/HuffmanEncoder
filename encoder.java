//package Huffman;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Parth Rampal
 * This class generates an Encoded file of the input using a Huffman Tree generation with Binary Tree
 */

public class encoder {

	/**
	 * Variable Declaration
	 */
	int count;
	HashMap <Integer, Integer> frequencyTable = new HashMap<Integer,Integer>();
	BinaryHeap bheap;
	int SymbolCount;
	HashMap <Integer, String> SymbolTable = new HashMap<Integer, String>();
	
	/**
	 * Constructor
	 * @param args
	 */
	
	public encoder(String[] args) {
        
		if(args.length < 1) {
                System.out.println("Invalid parameters. Usage is as follows: \n" +
                                "for compressing 'java encoder  input.txt'\n");
                System.exit(0);
        }
        
        String path = args[0];
        Encode(path);
        
        
	}
        
	/**
	 * Encode performs the Encoding
	 * @param path
	 */
        
	private void Encode(String path)
	{
		CalculateFrequency(path);
		CreateTree();
		generateCode(bheap.findMin(), ""); 
		System.out.println("Writing Code Table");
		WriteCodeTable(path,SymbolTable);
		System.out.println("Encoding File");
		EncodeFile(path,SymbolTable);
		//createEncodedFile(SymbolTable,path);
        System.out.println("done");
	}
	
	/**
	 * CalculateFrequency creates the Frequency Table
	 * @param path is the path of the input file
	 */

	private void CalculateFrequency(String path) {

    	System.out.println("Calculating frequencies");
        try {
        	
        		BufferedReader br = new BufferedReader(new FileReader(path));
	            String symbol = null;
	            count = 0;
	            while ((symbol = br.readLine()) != null) {			// As long as a symbol exists
	            		if(symbol.length()>0 && symbol != "" && symbol != "\r" && symbol != "\n")
	            		{
	            			if(frequencyTable.containsKey(Integer.parseInt(symbol)))		// If it's already in the HashMap, increment Count
	            			{
	            				frequencyTable.put(Integer.parseInt(symbol), frequencyTable.get(Integer.parseInt(symbol))+1);
	            			}
	            			else
	            			{
	            				frequencyTable.put(Integer.parseInt(symbol),1);			// Else add it to the HashMap
	            			}
	            			
	            			
	            			count ++;		// Just a debugging counter
	            		}
	            }
	            br.close();                        
        }
        catch (FileNotFoundException ex) {
                System.out.println("File not found");
                System.exit(0);
        }
        catch (IOException ex) {
                ex.printStackTrace();
        }
		
	}

	/**
	 * CreateTree Creates a binary heap of the given size and then arranges creates the HuffMan Tree in it
	 */

	private void CreateTree() {
		System.out.println("Creating Tree");
        createHeap(frequencyTable);					// Create Binary heap
        SymbolCount = bheap.getSize();				// Print number of unique symbols in file
        System.out.println("Number of different characters: " + SymbolCount);
        System.out.println("Creating Huffman tree");
        bheap = createHuffmanTree(bheap);        			// Create the huffman tree
		
	}
	
	/**
	 * createHeap creates the Binary heap
	 * @param frequency is Frequency Table
	 */
	
	private void createHeap(HashMap<Integer, Integer> frequency) {
        bheap = new BinaryHeap(frequency.size()+1);
        Iterator<Integer> keyIterator = frequency.keySet().iterator();
                while(keyIterator.hasNext())
                {
                	Integer key = keyIterator.next();
                	HuffmanNode node = new HuffmanNode(frequencyTable.get(key),key);
                	node.setToLeaf();
                	bheap.insert(node);
                }
        
}
	/**
	 * createHuffmanTree creates the relationships of the tree in the heap
	 * @param heap
	 * @return
	 */
	
	 private BinaryHeap createHuffmanTree(BinaryHeap heap) {
         int n = heap.getSize();
         for(int i = 0; i < (n-1); ++i) {
                 HuffmanNode z = new HuffmanNode();
                 z.setLeft(heap.deleteMin());
                 z.setRight(heap.deleteMin());
                 z.setfrequency(z.getLeft().getfrequency() + z.getRight().getfrequency());
                 heap.insert(z);
         }
         return heap;
 }
	 
	 /**
	  * generateCode Generates the Huffman Codes for the nodes recursively
	  * @param node root Node
	  * @param code Huffman Code
	  */
	 
	 private void generateCode(HuffmanNode node, String code) {             
         if(node != null) {                      
                 if(node.isLeaf())
                 		SymbolTable.put(node.getKey(), code);
                 else {
                         generateCode(node.getLeft(), code + "0");
                         generateCode(node.getRight(), code + "1");
                 }
         }
 }
	 
	 /**
	  * WriteCodeTable Writes the Code Tabe to file
	  * @param path
	  * @param SymbolTable
	  */
	 
	 private void WriteCodeTable(String path, HashMap<Integer,String> SymbolTable)
	 {
		 try{
			 path = "code_table.txt";
			 BufferedWriter bcw = new BufferedWriter(new FileWriter(path));
			 String CodeToWrite = "";
			 Integer writecounter = 0;
			 Iterator<Integer> keyIterator = SymbolTable.keySet().iterator();
             while(keyIterator.hasNext())
             {
             	Integer key = keyIterator.next();
             	writecounter ++;
             	CodeToWrite = CodeToWrite + key.toString() + "\n" + SymbolTable.get(key).toString() + "\r\n";
             	if(writecounter % 100 == 0)		// Write in batches of 100 for fast writing
             	{
             		bcw.write(CodeToWrite);
             		CodeToWrite = "";			// Clear buffer
             		//System.out.println(writecounter + " values written.");
             	}
             	
             }
             bcw.write(CodeToWrite);		// Write the remainder
             bcw.close();   				// Close writer
		 }
		 catch(Exception e)
		 {
			 System.out.println(e);
		 }
	 }	 
	
	 /**
	  *  Encodes the Input file to encoded.bin
	  *  This code assumes the bits are all divisible by 8
	  * @param path	path of input file
	  * @param SymbolTable	code table
	  */

	 private void EncodeFile(String path, HashMap<Integer,String> SymbolTable)
	 {
		 try
		 {
			 BufferedReader br = new BufferedReader(new FileReader(path));
			 String opath = "encoded.bin";
			 BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(opath));	 
			 String buffer = "";
             boolean[] bits = new boolean[8];
             String byteRead;
             int byteToWrite = 0;


             while ((byteRead = br.readLine()) != null) 
             {
            	 if(byteRead.length()>0 && byteRead != "" && byteRead != "\r" && byteRead != "\n")
            	 {
                     String charToWrite = SymbolTable.get(Integer.parseInt(byteRead));             
                     buffer = buffer + charToWrite;
                     while(buffer.length() >= 8) 
                     {                       	 
                             for(int i = 0; i < 8; ++i) 
                             {
                                     if(buffer.charAt(i) == '1') 
                                             bits[i] = true;
                                     else
                                             bits[i] = false;
                             }
                             buffer = buffer.substring(8);
                             
                             byteToWrite = bitsToByte(bits);
                             bw.write(byteToWrite);
                     } 
            	 }
             }		 
			 
			 br.close();
			 bw.close();
		 }
		 catch(Exception e)
		 {
			 System.out.println(e);
		 }
	 }
	 
	 /**
	  * Converts bits to Bytes for writing
	  * @param bits
	  * @return
	  */
	 
	    private static int bitsToByte(boolean[] bits) {
            if (bits == null || bits.length != 8) {
                    throw new IllegalArgumentException();
            }
            int data = 0;
            for (int i = 0; i < 8; i++) {
                    if (bits[i]) data += (1 << (7-i));
            }
            return data;
    }
	 
	 
	public static void main(String[] args) {
		
		long startTime = System.nanoTime();
		new encoder(args);
		long stopTime = System.nanoTime();
		System.out.println("\nElapsed time is " + ((stopTime - startTime)/1000000) + " miliseconds.");
	}

}
