package com.gradescope.hw8;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * An unbalanced binary search tree, which implements the Map interface (i.e.
 * maps keys to values). The map is sorted according to the natural ordering of
 * its keys (as defined by the Comparable interface).
 */
public class BinarySearchTree<KeyType extends Comparable<KeyType>, ValueType> implements Map<KeyType, ValueType> {

	private BSTNode root;
	private int size;
	/**
	 * Constructs an empty binary search tree
	 */
	public BinarySearchTree() {
		this.root = null;
	}

	/**
	 * A node in a binary search tree that contains a key, a value, and references
	 * to the left and right nodes.
	 */
	private class BSTNode {
		// NOTE: BSTNode has only fields and constructors
		private KeyType key;
		private ValueType value;
		private BSTNode left;
		private BSTNode right;

		private BSTNode(KeyType key, ValueType value) {
			this(key,value,null,null);
		}

		private BSTNode(KeyType key, ValueType value, BSTNode leftTree, BSTNode rightTree) {
			if (key == null || value == null) {
				throw new IllegalArgumentException("Inserted keys and values must be non-null");
			}
			this.key = key;
			this.value = value;
			this.left = leftTree;
			this.right = rightTree;
		}
	}

	////////////////////////////////////////////////////////////////////
	// Query Operations
	// Methods: isEmpty, size, height, containsKey, containsValue, get, getMinKey
	////////////////////////////////////////////////////////////////////

	/**
	 * Returns true if this binary search tree contains no items.
	 * 
	 * @return true if this collection contains no elements
	 */
	public boolean isEmpty() {
		return this.root == null;
	}

	/**
	 * @see java.util.Map#size()
	 */
	@Override
	public int size() {
		// TODO: revise size() to return the private instance variable this.size
		return this.size;
	}

	/**
	 * Returns the number of key-value mappings for the subtree rooted at root.
	 * 
	 * @return the number of key-value mappings in the map rooted at root.
	 */
	private int size(BSTNode root) {
		// TODO: remove this method after refactoring the public size() method
		if (root == null) {
			return 0;
		} else {
			return 1 + this.size(root.left) + this.size(root.right);
		}
	}

	/**
	 * Returns the height of this tree.
	 * 
	 * The height is defined as the number of edges on the longest path from the
	 * root to a leaf. The height of an empty tree is -1.
	 * 
	 * @return the height of this tree
	 */
	public int height() {
		return this.height(this.root);
	}

	/**
	 * Returns the height in a subtree.
	 * 
	 * @param root - root of the tree
	 * @return the height of the tree rooted at root
	 */
	private int height(BSTNode root) {
		if (root == null) {
			return -1;
		}

		int leftHeight = this.height(root.left);
		int rightHeight = this.height(root.right);
		return 1 + Math.max(leftHeight, rightHeight);
	}

	/**
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key) {
		return this.get(key) != null;
	}

	/**
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value) {
		// search through all keys
		// TODO: containsValue(object) requires getAllKeysInOrder() to be implemented
		for (KeyType key : this.getAllKeysInOrder()) {
			ValueType rootValue = this.get(key);
			if (rootValue.equals(value)) {
				return true;
			}
		}

		return false; // key not found
	}

	/**
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ValueType get(Object key) {
		return this.get((KeyType) key, this.root);
	}

	/**
	 * Returns the value to which the specified key is mapped in a subtree.
	 * 
	 * @param key  - key whose associated value is to be returned
	 * @param root - root of the tree to search
	 * @return the value to which the specified key is mapped, or null if this map
	 *         contains no mapping for the key
	 */
	private ValueType get(KeyType key, BSTNode root) {
		// base case - empty tree: return null
		if (root == null) {
			return null;
		}

		KeyType rootKey = root.key;

		// base case - found key at root: return associated value
		if (key.equals(rootKey)) {
			return root.value;
		}

		// key < rootKey: search the left subtree
		if (this.inOrderKeys(key, rootKey)) {
			return this.get(key, root.left);
		}

		// rootKey < key: search the right subtree
		return this.get(key, root.right);
	}

	/**
	 * Returns the minimum key in this tree.
	 * 
	 * @return the value of the smallest key in this tree
	 
	 * @throws IllegalArgumentException if this tree is empty
	 */
	public KeyType getMinKey(){
		// TODO: getMinKey() requires getMinKey(root) to be implemented
		return this.getMinKey(this.root);
	}

	/**
	 * Returns the minimum key in a subtree.
	 * 
	 * @param root - root of the tree to search
	 * @return the value of the smallest key in the tree rooted at root
	 * 
	 * @throws IllegalArgumentException if root is null
	 */
	private KeyType getMinKey(BSTNode root){
		if(root==null) {
			throw new IllegalArgumentException("null input");
				
			
		}
		if(root.left==null) {
			return root.key;
		}else {
			return getMinKey(root.left);
		}
		 
	}

	////////////////////////////////////////////////////////////////////
	// Modification Operations
	// Methods: clear, put, putAll, remove
	////////////////////////////////////////////////////////////////////

	/*
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear() {
		this.size = 0;
		this.root = null;
	}

	/**
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public ValueType put(KeyType key, ValueType value) {
		ValueType oldValue = this.get(key);
		  if (this.containsKey(key)==false){
	        	this.size++;
	        }
		this.root = this.put(key, value, this.root);
		return oldValue;
	}

	/**
	 * Associates the specified value with the specified key in a subtree. If the
	 * map previously contained a mapping for the key, the old value is replaced.
	 * 
	 * @param key   - key with which the specified value is to be associated
	 * @param value - value to be associated with the specified key
	 * @param root  - root of the tree in which to insert the new key-value pair
	 * @return the (possibly new) root of the tree
	 */
	private BSTNode put(KeyType key, ValueType value, BSTNode root) {
		// base case - empty tree: create a new node for the root and return it
		if (root == null) {
			BSTNode newNode = new BSTNode(key, value);
			return newNode;
		}

		KeyType rootKey = root.key;

		// base case - found key at root: update with the new value
		if (key.equals(rootKey)) {
			root.value = value;
		}

		// key < rootKey: put in the left subtree
		else if (this.inOrderKeys(key, rootKey)) {
			root.left = this.put(key, value, root.left);
		}

		// rootKey < key: put in the right subtree
		else {
			root.right = this.put(key, value, root.right);
		}

		return root;
	}

	/**
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(Map<? extends KeyType, ? extends ValueType> map) {
		for (KeyType key : map.keySet()) {
			ValueType value = map.get(key);
			this.put(key, value);
		}
	}

	/**
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ValueType remove(Object key) {
		// TODO: remove(key) requires remove(key, root) to be implemented
		ValueType value = this.get(key);
		if (value != null) { // only try to remove keys that are in the tree
			this.root = this.remove((KeyType) key, this.root);
			this.size--;
		}
		return value;
	}

	/**
	 * Removes the mapping for a key from this map if it is present (optional
	 * operation).
	 * 
	 * @param key  - key for which mapping should be removed
	 * @param root - root of the tree in which to remove the key
	 * @return the (possibly new) root of the tree
	 */
	private BSTNode remove(KeyType key, BSTNode root) {
		if(root==null) {
			return root;
		}
		if (inOrderKeys(key,root.key)) {
        	root.left=remove(key,root.left);
        }
		else if (inOrderKeys(root.key,key)) {
        	root.right=remove(key,root.right);
        }
		
        
        else {
        	if (root.left==null && root.right==null) {
        		root=null;
        		
        	}
        	else if (root.left==null) {
        		root=root.right;
        	}
        	else if (root.right==null) {
        		root=root.left;
        	}
        	
 	
        	else {
        		KeyType min = getMinKey(root.right);
        		ValueType value=get(min,root.right);
        		root.key=min;
        		root.value=value;
        		root.right = remove(min,root.right);
        	}
        }
        return root;
		
	}

	////////////////////////////////////////////////////////////////////
	// Debugging Methods
	// Methods: printTreeStructure, toString
	////////////////////////////////////////////////////////////////////

	/**
	 * Prints an indented tree structure of this tree.
	 */
	public void printTreeStructure() {
		printTreeStructure(this.root, 0);
	}

	/**
	 * Prints an indented tree structure of a subtree.
	 * 
	 * @param root  - the root of the tree to print
	 * @param depth - the indentation level
	 */
	private void printTreeStructure(BSTNode root, int depth) {
		if (root != null) {
			String s = "[" + root.key.toString() + " , " + root.value.toString() + "]";
			for (int count = 1; count <= depth; count++) {
				System.out.print("\t");
			}
			System.out.println(s);
			printTreeStructure(root.left, depth + 1);
			printTreeStructure(root.right, depth + 1);
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO: toString() requires addKeysToArrayList(keys, root) to be implemented
		ArrayList<KeyType> allKeys = this.getAllKeysInOrder();
		return allKeys.toString();
	}

	////////////////////////////////////////////////////////////////////
	// Helper Methods
	// Methods: inOrderKeys, getAllKeysInOrder
	////////////////////////////////////////////////////////////////////

	/**
	 * Returns true if key1 is less than key2.
	 * 
	 * @param key1 - key to compare
	 * @param key2 - key to compare
	 * @return true if key1 is less than key2
	 */
	private boolean inOrderKeys(KeyType key1, KeyType key2) {
		return key1.compareTo(key2) < 0;
	}

	/**
	 * Returns an ordered list of the keys in this tree.
	 * 
	 * @return an ordered list of the keys in this tree
	 */
	private ArrayList<KeyType> getAllKeysInOrder() {
		// TODO: getAllKeysInOrder() requires addKeysToArrayList(keys, root) to be
		// implemented
		ArrayList<KeyType> keys = new ArrayList<KeyType>();
		this.addKeysToArrayList(keys, this.root);
		return keys;
	}

	/**
	 * Adds the keys in a subtree to a list of keys.
	 * 
	 * @param keys - an ordered list of keys
	 * @param root - the root of the tree from which to add keys
	 */
	private void addKeysToArrayList(ArrayList<KeyType> keys, BSTNode root) {
		if (root==null) {
    		return;
    	}
		else if (root.left==null && root.right==null){
    		keys.add(root.key);
    	}
		else {
			addKeysToArrayList(keys,root.left);
			keys.add(root.key);
			addKeysToArrayList(keys, root.right);
		}
	}

	////////////////////////////////////////////////////////////////////
	// Unimplemented Methods
	// Methods: entrySet, keySet, values
	////////////////////////////////////////////////////////////////////

	/**
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public Set<Entry<KeyType, ValueType>> entrySet() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.util.Map#keySet()
	 */
	@Override
	public Set<KeyType> keySet() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.util.Map#values()
	 */
	@Override
	public Collection<ValueType> values() {
		throw new UnsupportedOperationException();
	}

}
