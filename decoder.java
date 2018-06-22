//package Huffman;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Parth Rampal
 * @UFID 48948698
 * This Class Takes the Encoded.bin and Code_Table files as input and decrypts the Huffman Encoded binary file to its original Decrypted form
 *
 *
 */
public class decoder {

	/** Variables Declared
	 * Count is a counter used while reading Code_Table.txt
	 * SymbolTable is a HashMap containing all the Symbols and their codes as read from file **/
	int count;
	HashMap <Integer, String> SymbolTable = new HashMap<Integer, String>();
	
	/**
	 * Constructor
	 * filepath is the path of Encoded.bin
	 * codetablepath contains path of Code_Table.txt
	 * **/
	
	public decoder(String[] args) {
        
		if(args.length < 2) {
                System.out.println("Invalid parameters. Usage is as follows: \n" +
                                "for compressing 'java decoder  encoded.bin code_file.txt'\n");
                System.exit(0);
        }
        
        String filepath = args[0];
        String codetablepath = args[1];
        decode(filepath,codetablepath);
        
        
	}
	
	/**
	 * Decode is the function which performs the decoding
	 * @param filepath is path of Encoded.bin
	 * @param codetablepath is path of Code Table file
	 */
	
	private void decode(String filepath, String codetablepath) {
        try {
        	
        		CreateCodeTable(codetablepath);		// Create HashMap of Symbols and their codes from code_table.txt
        		HuffmanNode root = new HuffmanNode(Integer.MAX_VALUE, -1);  // Create Empty HuffmanNode with Max possible Frequency as Root
        		String opath = "Decoded.txt";	// Output file will be called Decoded.txt
        		String EncodedBitStream= readEncoded(filepath);		// Read encoded.bin
            
            for(Map.Entry<Integer, String> symbol : SymbolTable.entrySet())
            {
            	HuffmanNode node = root;
                
                char[] encodedValue = symbol.getValue().toCharArray();
                
                for(char ch : encodedValue)
                {
                    if(ch == '0')		// If code is 0
                    {
                        if(node.getLeft() == null)	// Check if Left Node exists
                        {
                        	HuffmanNode temp = new HuffmanNode(Integer.MAX_VALUE, -1);
                            node.setLeft(temp);
                            temp = null;
                            
                        }
                        node = node.getLeft();
                    }
                    else		// If code is 1
                    {
                        if(node.getRight() == null)		// Check if right node exists
                        {
                        	HuffmanNode temp = new HuffmanNode(Integer.MAX_VALUE, -1);
                            node.setRight(temp);
                            
                        }
                        node = node.getRight();
                    }
                }
                node.setKey(symbol.getKey());		// Assign Symbol value to Key of HuffmanNode
            }
            
            HuffmanNode node = root;				// Reassign root
            PrintWriter filewriter = new PrintWriter(opath, "UTF-8");		// Create UTF-8 writer
            
            for(char ch : EncodedBitStream.toCharArray())	// Read Encoded bitstream
            {
                if(ch == '0')
                {
                    node = node.getLeft();
                }
                else
                {
                    node = node.getRight();
                }
                
                if(node.getLeft() == null && node.getRight() == null)
                {
                	filewriter.print(node.getKey());
                	filewriter.print("\n");
                    node = root;
                }
            }
            filewriter.close();						// Close writer
            System.out.println("Decryption done");
        }
        catch(Exception e){
                System.out.println("File not found");
                System.exit(0);
        }
        
}
	/**
	 * CreateCodeTable reads Code_table.txt file and assigns all symbols and their Codes to a HashMap
	 * @param codetablepath is the path of code_table.txt
	 * */


	private void CreateCodeTable(String codetablepath) {
		
		try{
			
    	BufferedReader br = new BufferedReader(new FileReader(codetablepath));		// File Reading handler
    	String symbol = null;
    	int key = 0;
        count = 0;
        while ((symbol = br.readLine()) != null) {									// For each word that is read
        		if(symbol.length()>0 && symbol != "" && symbol != "\r" && symbol != "\n")
        		{
        			if(count %2 == 0)	// the first word is always the key
        			{
        				key = Integer.parseInt(symbol);
        			}
        			else				// the next word is the code
        			{
        				SymbolTable.put(key,symbol);
        			}
        			
        			
        			count ++;
        		}
        }
        br.close();   // Close the reader
		}
		catch(Exception e)
		{
			System.out.println("File not found");
            System.exit(0);
		}
		
	}
	
	/**
	 * 
	 * @param filepath is the path of Encoded.bin
	 * @returns the converted bitstream
	 * @throws IOException
	 */
	
	private String readEncoded(String filepath) 
			throws IOException 
	{                             
       byte[] bytes;
        StringBuilder ConvertedString = new StringBuilder();
        bytes = Files.readAllBytes(new File(filepath).toPath());
        for(byte b: bytes)	//bytes conversion
        {
        	ConvertedString.
                append(Integer.toBinaryString(b & 255 | 256).substring(1));
        }
        return ConvertedString.toString();
    }
		


	public static void main(String[] args) 
	{
		long startTime = System.nanoTime();
		System.out.println("Starting Decoding");
		new decoder(args);	
		long stopTime = System.nanoTime();
		System.out.println("\nDecoding took " + ((stopTime - startTime)/1000000) + " miliseconds.");

	}

}
