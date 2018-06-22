//package Huffman;

/**
 * @author Parth Rampal
 *
 */
public class HuffmanNode {

	private HuffmanNode parent;
    private HuffmanNode left;
    private HuffmanNode right;
    private int key = -1;
    private int frequency = -1;
    private boolean isLeaf = false;
    
    public HuffmanNode() { 
    }
    
    public HuffmanNode(int frequency, int i) {  
            this.key = i;
            this.frequency = frequency;
    }
    
    public HuffmanNode getParent() {
            return parent;
    }

    public void setParent(HuffmanNode parent) {
            this.parent = parent;
    }

    public HuffmanNode getLeft() {
            return left;
    }

    public void setLeft(HuffmanNode left) {
            this.left = left;
    }

    public HuffmanNode getRight() {
            return right;
    }

    public void setRight(HuffmanNode right) {
            this.right = right;
    }

    public int getKey() {
            return key;
    }

    public void setKey(int key) {
            this.key = key;
    }

    public void setfrequency(int frequency) {
            this.frequency = frequency;
    }

    public int getfrequency() {
            return frequency;
    }
    
    public void setToLeaf() {
            isLeaf = true;
    }
    
    public boolean isLeaf() {
            return isLeaf;
    }
}
