package treeCalculator;

//import java.util.ArrayList;
//import java.util.List;
//
//public class Tree<T> {
//    private Node<T> root;
//
//    public Tree(T rootData) {
//        root = new Node<T>();
//        root.data = rootData;
//        root.children = new ArrayList<Node<T>>();
//    }
//
//    public static class Node<T> {
//        private T data;
//        private Node<T> parent;
//        private List<Node<T>> children;
//    }
//}


/*
 * Copyright 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


public class Tree<T> {

  private T headParent;

  private ArrayList<Tree<T>> leafs = new ArrayList<Tree<T>>();

  private Tree<T> parent = null;

  private HashMap<T, Tree<T>> key_value_parent_Child = new HashMap<T, Tree<T>>();

  //
  private HashMap<Integer, HashMap<T, ArrayList<T>>> levelLocate = new HashMap<Integer, HashMap<T, ArrayList<T>>>();
 
  public void addLeafForLevel(T root, T leaf, Integer level) {
	  if (levelLocate.containsKey(level))
	  {
		  HashMap<T, ArrayList<T>> mapUnderLevel =  levelLocate.get(level);
		  if (mapUnderLevel.containsKey(root))
		  {
			  ArrayList<T> listOfChilds = mapUnderLevel.get(root);
			  listOfChilds.add(leaf);
			  mapUnderLevel.put(root, listOfChilds);
		  }
		  else
		  {
			  ArrayList<T> listOfChilds = new ArrayList<T>();
			  listOfChilds.add(leaf);
			  mapUnderLevel.put(root, listOfChilds);
		  }
	  }
	  else
	  {
		  ArrayList<T> listOfChilds = new ArrayList<T>();
		  listOfChilds.add(leaf);
		  HashMap<T, ArrayList<T>> mapUnderLevel = new HashMap<T, ArrayList<T>>();
		  mapUnderLevel.put(root, listOfChilds);
		  levelLocate.put(level, mapUnderLevel);
	  }
	    if (key_value_parent_Child.containsKey(root)) {
	      key_value_parent_Child.get(root).addLeaf(leaf);
	    } else {
	      addLeaf(root).addLeaf(leaf);
	    }
	  }
  
  public ArrayList<T> getSuccessorsForLevel (T root, int level)
  {
	  if ( ! (levelLocate.containsKey(level)) )
	  {
		  return new ArrayList<T>();
	  }
	  HashMap<T, ArrayList<T>> mapUnderLevel = new HashMap<T, ArrayList<T>>();
	  try
	  {
		  mapUnderLevel = levelLocate.get(level);
		  if (mapUnderLevel == null)
		  {
			  mapUnderLevel = new HashMap<T, ArrayList<T>>();
			  mapUnderLevel.put(root, new ArrayList<T>());
		  }
	  }
	  catch (Exception e)
	  {
		  mapUnderLevel = new HashMap<T, ArrayList<T>>();
		  mapUnderLevel.put(root, new ArrayList<T>());
	  }
	  if (mapUnderLevel.containsKey(root))
	  {
		  return mapUnderLevel.get(root);
	  }
	  else
	  {
		  return new ArrayList<T>();
	  }
	  
  }
  
  public void printLevelAndElementsTree()
  {
	  for (int singleLevel : levelLocate.keySet())
	  {
	  System.out.println(singleLevel+"     :     "+levelLocate.get(singleLevel));
	  }
  }
  //
  public Tree(T head) {
    this.headParent = head;
    key_value_parent_Child.put(head, this);
  }

  public void addLeaf(T root, T leaf) {
    if (key_value_parent_Child.containsKey(root)) {
      key_value_parent_Child.get(root).addLeaf(leaf);
    } else {
      addLeaf(root).addLeaf(leaf);
    }
  }

  public Tree<T> addLeaf(T leaf) {
    Tree<T> t = new Tree<T>(leaf);
    leafs.add(t);
    t.parent = this;
    t.key_value_parent_Child = this.key_value_parent_Child;
    key_value_parent_Child.put(leaf, t);
    return t;
  }

  public Tree<T> setAsParent(T parentRoot) {
    Tree<T> t = new Tree<T>(parentRoot);
    t.leafs.add(this);
    this.parent = t;
    t.key_value_parent_Child = this.key_value_parent_Child;
    t.key_value_parent_Child.put(headParent, this);
    t.key_value_parent_Child.put(parentRoot, t);
    return t;
  }

  public T getHead() {
    return headParent;
  }

  public Tree<T> getTree(T element) {
    return key_value_parent_Child.get(element);
  }

  public Tree<T> getParent() {
    return parent;
  }

  public Collection<T> getSuccessors(T root) {
    Collection<T> successors = new ArrayList<T>();
    Tree<T> tree = getTree(root);
    if (null != tree) {
      for (Tree<T> leaf : tree.leafs) {
        successors.add(leaf.headParent);
      }
    }
    return successors;
  }

  public Collection<Tree<T>> getSubTrees() {
    return leafs;
  }

  public static <T> Collection<T> getSuccessors(T of, Collection<Tree<T>> in) {
    for (Tree<T> tree : in) {
      if (tree.key_value_parent_Child.containsKey(of)) {
        return tree.getSuccessors(of);
      }
    }
    return new ArrayList<T>();
  }

  @Override
  public String toString() {
    return printTree(0);
  }

  private static final int indent = 2;

  private String printTree(int increment) {
    String s = "";
    String inc = "";
    for (int i = 0; i < increment; ++i) {
      inc = inc + " ";
    }
    s = inc + headParent;
    for (Tree<T> child : leafs) {
      s += "\n" + child.printTree(increment + indent);
    }
    return s;
  }
  
  
  public static <T> Collection<T> getSuccessorsForLevel(T of, Collection<Tree<T>> in, int level) {
	    for (Tree<T> tree : in) {
	      if (tree.key_value_parent_Child.containsKey(of)) {
	        return tree.getSuccessors(of);
	      }
	    }
	    return new ArrayList<T>();
	  }
  
}