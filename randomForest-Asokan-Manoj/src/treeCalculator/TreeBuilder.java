package treeCalculator;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Stack;

import dataSorter.DataStore;
import dataSorter.InputDataHandler;

public class TreeBuilder {
	
	int firstCall = 1;
	int level = 1;
	boolean dive = false;
	boolean backTrackLevel = false;
	boolean parentBackTracker = false;
	boolean popTheSubTree = false;
	String currentRoot = "";
	Object treeObject = null;
	Stack<String> forgottenParents = new Stack<String>();
	
	boolean continueBuilding = true;

	boolean firstTimeCalled = true;
    Stack<String> previousParents = new Stack<String>();
	
    public void callBuildTree(Object parentNode)
    {
    	buildTree(parentNode, false, "");
    //	System.out.println(treeObject.toString());
		DataStore dataStore = DataStore.get();
    	dataStore.setId3Tree((Tree<String>) treeObject);
   // 	System.out.println("Total count :: "+numberOfTotalPossibleCounts);
    //	System.out.println("Total calls :: "+totalNumberOfFunctionCalls);
	//	System.out.println("number Of Paths :: "+listOfPaths.size());
		dataStore.setListOfPaths(listOfPaths);
		try
		{
			HashMap<Tree<String>, ArrayList<String>> tree_listOfPathsMap = new HashMap (dataStore.getTree_listOfPathsMap());
			tree_listOfPathsMap.put((Tree<String>) treeObject, listOfPaths);
			dataStore.setTree_listOfPathsMap(tree_listOfPathsMap);
		}
		catch (Exception e)
		{
			HashMap<Tree<String>, ArrayList<String>> tree_listOfPathsMap = new HashMap<Tree<String>, ArrayList<String>>();
			tree_listOfPathsMap.put((Tree<String>) treeObject, listOfPaths);
			dataStore.setTree_listOfPathsMap(tree_listOfPathsMap);
		}
    }
	
    int numberOfTotalPossibleCounts = 0;
    int totalNumberOfFunctionCalls  = 0;
    int currentLevel = 0;
    ArrayList<String> listOfPaths = new ArrayList<String>();
    String currentPath = "";
    
	public String buildTree (Object parentNode, boolean takeRoot, 	String currentPreviousParentNode)
	{		
		totalNumberOfFunctionCalls = totalNumberOfFunctionCalls+1;
		if (firstTimeCalled)
		{
			treeObject = new Tree<String>( String.valueOf(parentNode+":"+parentNode) );
			currentRoot = String.valueOf( parentNode );
			currentLevel = 0;
			currentPath = String.valueOf( parentNode+":"+parentNode );
		}
		firstTimeCalled 																		 = false;
		DataStore dataStore 																     = DataStore.get();
		HashMap <Integer, ArrayList<String>> mapOfColumnsAndAttributeValues 				     = new HashMap (dataStore.getAttributeNumber_listOfValuesMap());
		HashMap<Integer, HashMap<String, HashMap<String, Object>>> attributeNumber_PropertiesMap = dataStore.getattributeNumber_PropertiesMap();
		int targetVariablecolumnNumber 															 = dataStore.getTargetVariableColumnNumber();
		HashMap<String, ArrayList<String>> mapOfIndices											 = new HashMap (attributeNumber_PropertiesMap.get(parentNode).get("indexList") );	

		String toBePoppedParent 																 = currentRoot;
		int toBePoppedLevel																		 = currentLevel;
		String tempPathHolder																	 = currentPath;
		String backTrackHolder																	 = currentPath;
		
		for ( String eachValueInsideHighGainAttribute : new LinkedHashSet<String> (mapOfColumnsAndAttributeValues.get(parentNode) ) )
		{
			numberOfTotalPossibleCounts = numberOfTotalPossibleCounts+1;
			
			try
			{
				ArrayList<Integer> listOfIndices = new ArrayList (mapOfIndices.get(eachValueInsideHighGainAttribute) );			
			}
			catch (Exception e)
			{
				continue;
			}
			ArrayList<Integer> listOfIndices = new ArrayList (mapOfIndices.get(eachValueInsideHighGainAttribute) );			
			HashSet<String> listOfUniqueTargets = new HashSet<String>();
			
			for ( int currentIndex : listOfIndices )
			{
				
				String currentTargetVariable = mapOfColumnsAndAttributeValues.get(targetVariablecolumnNumber).get(currentIndex);
				listOfUniqueTargets.add(currentTargetVariable);
			}
			
			if (listOfUniqueTargets.size() == 1)
			{
				if (! takeRoot)
				{
					( (Tree<String>) treeObject).addLeafForLevel(parentNode+":"+currentRoot, parentNode+":"+eachValueInsideHighGainAttribute, currentLevel);
					currentPath = currentPath+"-*-"+parentNode+":"+eachValueInsideHighGainAttribute;
				}
				if( takeRoot )
				{	
					( (Tree<String>) treeObject).addLeafForLevel(currentPreviousParentNode+currentRoot, parentNode+":"+eachValueInsideHighGainAttribute, currentLevel );
					currentPath = currentPath+"-*-"+parentNode+":"+eachValueInsideHighGainAttribute;

				}
				( (Tree<String>) treeObject).addLeafForLevel(parentNode+":"+eachValueInsideHighGainAttribute, (new ArrayList<String>(listOfUniqueTargets).get(0)), currentLevel+1 );
				tempPathHolder = currentPath+"-*-"+(new ArrayList<String>(listOfUniqueTargets).get(0));
				listOfPaths.add(tempPathHolder);
				currentPath = backTrackHolder;
			}
			else
			{
			//	System.out.println("entered else for ::"+eachValueInsideHighGainAttribute);
				
				if (! takeRoot)
				{
					( (Tree<String>) treeObject).addLeafForLevel(parentNode+":"+currentRoot, parentNode+":"+eachValueInsideHighGainAttribute, currentLevel);
					currentPath = currentPath+"-*-"+parentNode+":"+eachValueInsideHighGainAttribute;

				}
				if (takeRoot)
				{
					( (Tree<String>) treeObject).addLeafForLevel(currentPreviousParentNode+currentRoot, parentNode+":"+eachValueInsideHighGainAttribute,currentLevel);
					currentPath = currentPath+"-*-"+parentNode+":"+eachValueInsideHighGainAttribute;
				}
				int newHighGainColumn = findSubTree(eachValueInsideHighGainAttribute, listOfIndices, mapOfColumnsAndAttributeValues, (Integer) parentNode);
				
				if (newHighGainColumn == 9999)
				{
			//		System.out.println("OUT OF COLUMNS!! CHECK THIS ::");
					continue;
				}
				// everything is a new tree at this part !! 
				currentRoot = eachValueInsideHighGainAttribute;
				currentLevel = currentLevel+1;
				buildTree(newHighGainColumn, true, parentNode+":");
				// need to reset our treee !!
//				InputDataHandler inputHandler = new InputDataHandler();
//				inputHandler.createSubTreeOfAttributes(mapOfColumnsAndAttributeValues);
				currentRoot = toBePoppedParent;
				currentLevel = toBePoppedLevel;
				currentPath = backTrackHolder;

			}
		}

		return "\n";
	}
	
	public int findSubTree (String subTreeSplitterValue, ArrayList<Integer> listOfIndices, HashMap <Integer, ArrayList<String>> mapOfColumnsAndAttributeValues, int parentNode )
	{
		HashMap <Integer, ArrayList<String>> newMapOfColumnsAndAttributeValues = new HashMap <Integer, ArrayList<String>>();
		for (int singleColumn : mapOfColumnsAndAttributeValues.keySet())
		{
			if (singleColumn == parentNode)
			{
				continue;
			}
			ArrayList<String> newListOfValuesForSubTree = new ArrayList<String>();
			ArrayList<String> currentListOfValues = new ArrayList (mapOfColumnsAndAttributeValues.get(singleColumn));
			for ( int singleIndex : listOfIndices)
			{
				newListOfValuesForSubTree.add(currentListOfValues.get(singleIndex));
			}
			newMapOfColumnsAndAttributeValues.put(singleColumn, newListOfValuesForSubTree);
		}
		InputDataHandler inputHandler = new InputDataHandler();
		inputHandler.createSubTreeOfAttributes(newMapOfColumnsAndAttributeValues);
		TreeOperations treeOperation = new TreeOperations();
		int newHighGainColumn = treeOperation.findBestClassifier();
		return newHighGainColumn;
	}
	
	
}
