package treeCalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import dataSorter.DataStore;

public class TreeOperations {
	
	public void findTargetEntropy()
	{
		DataStore dataStore = DataStore.get();
		HashMap<Integer, ArrayList<String>> attributeValueMap = dataStore.getAttributeNumber_listOfValuesMap();
		HashMap<Integer,HashMap<String, HashMap<String, Object>>>countOfAttribValues = dataStore.getattributeNumber_PropertiesMap();
		int targetVariableColumnNumber = dataStore.getTargetVariableColumnNumber();		
		int totalnumOfTargetVariables = attributeValueMap.get(targetVariableColumnNumber).size();
		HashMap<String, Object> countOfValuesMap = new HashMap<String, Object>( countOfAttribValues.get(targetVariableColumnNumber).get("numberOfRepetitions"));
		float Entropy = (float) 0.0;
		for ( String singleValueinAttr : countOfValuesMap.keySet())
		{
			int countOfCurrentValue = (int) countOfValuesMap.get(singleValueinAttr);
			float probability =  ((float)countOfCurrentValue/totalnumOfTargetVariables);
			Entropy = (float) (Entropy + ((probability*(-1))* ( (Math.log(probability) )/ (Math.log(2))  ) ));
		}
		dataStore.setTargetEntropy(Entropy);
	}
	
	public  int findBestClassifier()
    {
		DataStore dataStore = DataStore.get();
		HashMap<Integer, ArrayList<String>> allAttributeColumnsWithValues = dataStore.getAttributeNumber_listOfValuesMap();
		float targetEntropy = dataStore.getTargetEntropy();
		int targetVariableColumnNumber = dataStore.getTargetVariableColumnNumber();

		HashMap <Integer, HashMap<String, ArrayList<String>>> ColumnNumbersWithValuesAndListOfTargets = new HashMap <Integer, HashMap<String, ArrayList<String>>>();
		HashMap<Integer,Float> infoGainForEachAttribute = new HashMap<Integer, Float>();
		for ( int columnNumberOfAttribute : allAttributeColumnsWithValues.keySet())
		{
			float informationGainForAttribute = (float) 0;
			float finalInformationGainForAttribute = (float) 0;

			if (targetVariableColumnNumber == columnNumberOfAttribute)
			{
				continue;
			}
			ArrayList<String> oneAttributeColumnValues= new ArrayList(allAttributeColumnsWithValues.get(columnNumberOfAttribute) );
			int ColumnLength = oneAttributeColumnValues.size();
			HashMap <String, ArrayList<String>> valueOfAttribWithTargetValueList= new HashMap<String,ArrayList<String>>();
			int indexCount = 0;
			for (String singleValueInsideAttribute : oneAttributeColumnValues)
			{
				int indexToSearch = indexCount;
				indexCount = indexCount+1;
				String targetVariableForCurrent = allAttributeColumnsWithValues.get(targetVariableColumnNumber).get(indexToSearch);
				try
				{
					ArrayList<String> numberOfValues = new ArrayList<String>();
					numberOfValues = valueOfAttribWithTargetValueList.get(singleValueInsideAttribute);
					numberOfValues.add(targetVariableForCurrent);
					valueOfAttribWithTargetValueList.put(singleValueInsideAttribute, numberOfValues);
				}
				catch(Exception e)
				{
					ArrayList<String> listOfTargetsForValue = new ArrayList<String>();
					listOfTargetsForValue.add(targetVariableForCurrent);
					valueOfAttribWithTargetValueList.put(singleValueInsideAttribute, listOfTargetsForValue);
				}
				ColumnNumbersWithValuesAndListOfTargets.put(columnNumberOfAttribute, valueOfAttribWithTargetValueList);
			}
		
			for ( String eachValue : valueOfAttribWithTargetValueList.keySet())
			{
				ArrayList<String> targetsForThisValue = new ArrayList(valueOfAttribWithTargetValueList.get(eachValue) );
				HashMap<String, Integer> countOfValueInEachAttrib = new HashMap<String, Integer>();
				
				for ( String singleTargetValue : targetsForThisValue )
				{
					int countOfValue = 0;
	    			try
	    			{
	    				countOfValue = countOfValueInEachAttrib.get(singleTargetValue);
	    			}
	    			catch (Exception e)
	    			{
	    				countOfValue = 0;
	    			}
	    			countOfValue = countOfValue + 1;	
	    			countOfValueInEachAttrib.put(singleTargetValue, countOfValue);	
				}
				float entropy = (float) 0.0;
				for ( String singleTargetValue : countOfValueInEachAttrib.keySet())
				{
					float probability = (float)( countOfValueInEachAttrib.get(singleTargetValue))/ ( valueOfAttribWithTargetValueList.get(eachValue).size());
					entropy = (float) (entropy + ((probability*(-1))* ( (Math.log(probability) )/ (Math.log(2))  ) ));
				}
				informationGainForAttribute =  informationGainForAttribute + ( (float) (valueOfAttribWithTargetValueList.get(eachValue).size())/ColumnLength )* entropy ;
			}
			finalInformationGainForAttribute = targetEntropy - informationGainForAttribute;
			infoGainForEachAttribute.put(columnNumberOfAttribute,finalInformationGainForAttribute);
		    
			
		}
		dataStore.setAttributeNumber_Values_listOfTargets(ColumnNumbersWithValuesAndListOfTargets);
		
		int columnOfLargestGain = 9999;
		if (infoGainForEachAttribute.isEmpty())
		{
			return 9999;
		}
		ArrayList<Float> listOfInfoGains = new ArrayList<Float>();
		for ( Integer columnNumber : infoGainForEachAttribute.keySet())
		{
			listOfInfoGains.add(infoGainForEachAttribute.get(columnNumber));
		}
		float MaxInfo = Collections.max(listOfInfoGains);
		for ( Integer columnNumber : infoGainForEachAttribute.keySet())
		{
			if (infoGainForEachAttribute.get(columnNumber) == MaxInfo)
			{
				columnOfLargestGain = columnNumber;
			}
		}
		return columnOfLargestGain;
    }
	
	

	
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> findResultOfTargets ( Tree treeObject, ArrayList<String> listOfAllPaths, ArrayList<ArrayList<String>> listOfTestData )
	{
		DataStore dataStore = DataStore.get();
		int targetValueColumnNumber = dataStore.getTargetVariableColumnNumber();
		ArrayList<ArrayList<String>> listOfPossiblePaths = new ArrayList<ArrayList<String>>();
		for(String singleUnsplittedLine : listOfAllPaths)
		{						
			ArrayList<String> singlePath= new ArrayList( Arrays.asList (singleUnsplittedLine.split("-\\*-") ) );
			listOfPossiblePaths.add(singlePath);
		}
	//	System.out.println("target variable columnNumber before traversing and TESTING : "+targetValueColumnNumber);
    	ArrayList<String> targetColumnPredicted = new ArrayList<String>();
    	
    	String root = ( (Tree<String>) treeObject ).getHead();
		root = root.substring(2);
	//	System.out.println("THIS IS THE ROOT :: "+root);
				
    	for (ArrayList<String> singleLineList : listOfTestData)
    	{
//    		if (lineNumber > 10)
//    		{
//    			break;
//    		}    		
    	//	System.out.println("entered for 1 line :: "+singleLineList);
    		ArrayList<ArrayList<String>> listOfRemainingPossiblePaths = new ArrayList<ArrayList<String>>();
    		listOfRemainingPossiblePaths = listOfPossiblePaths;
    		String requiredResult = "";
    		int travelDepth = 1 ;
    		while ( listOfRemainingPossiblePaths.size() > 1 )
    		{
    			listOfRemainingPossiblePaths = isolateTree(listOfRemainingPossiblePaths,singleLineList ,travelDepth, targetValueColumnNumber);
    			travelDepth = travelDepth+1;
    		}
    		if (listOfRemainingPossiblePaths.size() == 1)
    		{
    			 requiredResult = listOfRemainingPossiblePaths.get(0).get(listOfRemainingPossiblePaths.get(0).size()-1);
    		}
//    		if (requiredResult.equalsIgnoreCase(singleLineList.get(targetValueColumnNumber)))
//			{
    			targetColumnPredicted.add(requiredResult);
//			}
    	}
  //  	System.out.println("The total size of collected data ::"+targetColumnPredicted.size());
   // 	System.out.println("tota number of test data :: "+listOfTestData.size());
    	return targetColumnPredicted ;
	}
	
	
	public ArrayList<ArrayList<String>> isolateTree (ArrayList<ArrayList<String>> listOfPossiblePaths,ArrayList<String> singleLineList, int travelDepth, int targetVariableColumnNumber)
	{
		ArrayList<ArrayList<String>> listOfRemainingPossiblePaths = new ArrayList<ArrayList<String>>();
		for (ArrayList<String> singlePath : listOfPossiblePaths)
		{
			if (travelDepth > singlePath.size()-1)
			{
				continue;
			}
			String currentOpenChoice = singlePath.get(travelDepth) ;
			int columnNumber = 0;
			int subStringToTake = 2;
			try
			{
				columnNumber = Integer.parseInt( currentOpenChoice.substring(0,2));
				subStringToTake = 3;
			}
			catch (Exception e)
			{
				try
				{						
					columnNumber = Integer.parseInt( currentOpenChoice.substring(0,1));
				}
				catch(Exception e2)
				{
	//				System.out.println("number Format exception ::"+currentOpenChoice);   						
					break;
				}		
			}
			if (travelDepth == singlePath.size()-1)
			{
				listOfRemainingPossiblePaths = new ArrayList<ArrayList<String>>();
				listOfPossiblePaths.add(singlePath);
				return listOfRemainingPossiblePaths;
			}
			if ( singleLineList.get(columnNumber).equalsIgnoreCase(currentOpenChoice.substring(subStringToTake)))
			{
				listOfRemainingPossiblePaths.add(singlePath);
			} 			
		}
		return listOfRemainingPossiblePaths;
	}
	
	public void findResultOfForestTargets()
	{
		DataStore dataStore = DataStore.get();
		HashMap<Tree<String>, ArrayList<String>> treeCollection = dataStore.getTree_listOfPathsMap();
		ArrayList<ArrayList<String>> listOfTestData =  dataStore.getListOfTestData();
		ArrayList<ArrayList<String>> listOfResultFromTrees = new ArrayList<ArrayList<String>>();
		
		for ( Tree<String> singleTree : treeCollection.keySet())
		{
			listOfResultFromTrees.add( findResultOfTargets(singleTree, treeCollection.get(singleTree), listOfTestData) );
		}
		int numberOfData = listOfTestData.size();
		int targetVariableColumnNumber = dataStore.getTargetVariableColumnNumber();
		findAccuracy(listOfResultFromTrees, listOfTestData, numberOfData, targetVariableColumnNumber);
	}
	
	public void findAccuracy(ArrayList<ArrayList<String>> listOfForestPredictions, ArrayList<ArrayList<String>> listOfTestData, int numberOfData, int targetVariableColumnNumber)
	{
		int NumOfErrors = 0;
		int numOfCorrectPredicts = 0;
		for (int i=0; i<numberOfData; i++)
		{
			String greatestRepeaterInRow = "";
			ArrayList<String> listOfPredictionsForRow = new ArrayList<String>();
			for (ArrayList<String> singlePredictedSet : listOfForestPredictions)
			{
				listOfPredictionsForRow.add( singlePredictedSet.get(i) );
			}
			int repeater = 0;
			for (String singlePrediction : new HashSet<String>(listOfPredictionsForRow) )
			{
				if ( repeater < (Collections.frequency(listOfPredictionsForRow, singlePrediction)) )
				{
					greatestRepeaterInRow = singlePrediction;
				}
			}
			if (greatestRepeaterInRow.equalsIgnoreCase(listOfTestData.get(i).get(targetVariableColumnNumber)) )
			{
				//	System.out.println("correctPrediction");
				numOfCorrectPredicts = numOfCorrectPredicts+1;
			}
			else
			{
				NumOfErrors = NumOfErrors+1;
			}
		}
		DataStore dataStore = DataStore.get();
//		System.out.println("the number of errors is :: "+NumOfErrors);
//		System.out.println("The number of correct predictions :: "+numOfCorrectPredicts);
//		System.out.println("Total Test Data size :: "+numberOfData);
		float percentage = (float) numOfCorrectPredicts/numberOfData * 100 ;
//		System.out.println("percentage : "+percentage);
		ArrayList<Float> listOfAccuracies = new ArrayList<Float>();
    	try
    	{
    		listOfAccuracies = dataStore.getListOfAccuracies();
			listOfAccuracies.add(percentage);
    		if (listOfAccuracies == null)
    		{
    			listOfAccuracies = new ArrayList<Float>();
    			listOfAccuracies.add(percentage);
    		}
    	}
    	catch (Exception e)
    	{
			listOfAccuracies = new ArrayList<Float>();
			listOfAccuracies.add(percentage);
    	}
    	dataStore.setListOfAccuracies(listOfAccuracies);
	}
	
}
    		



    	
    	

