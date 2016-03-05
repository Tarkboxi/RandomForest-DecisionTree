package dataSorter;

import java.util.ArrayList;
import java.util.HashMap;

import treeCalculator.Tree;

public class DataStore {

	private float targetEntropy;
	private int targetVariableColumnNumber;
	
	private HashMap<Integer, ArrayList<String>> attributeNumber_listOfValuesMap;
	private HashMap<Integer, HashMap<String, HashMap<String, Object>>> attributeNumber_PropertiesMap;
	private HashMap <Integer, HashMap<String, ArrayList<String>>> attributeNumber_Values_listOfTargets;
	private HashMap<Object, Object> DecisionTree = new HashMap<Object, Object>();
	private Tree Id3Tree;
	private ArrayList<ArrayList<String>> listOfTestData;
	private ArrayList<ArrayList<String>> listOfUnsplitTestData;

	private ArrayList<String> listOfTestDataTargets;
	private ArrayList<String> listOfPaths;
	private ArrayList<ArrayList<ArrayList<String>>> totalDataSplitIn5;
	private ArrayList<ArrayList<String>> listOfTrainingData;
	private ArrayList<ArrayList<String>> listOfUnsplitTrainingData;
	private HashMap<Tree<String>, ArrayList<String>> tree_listOfPathsMap;

	private ArrayList<ArrayList<ArrayList<String>>> collectionOfTotalShuffledData;
	private ArrayList<Float> listOfAccuracies;


	
	
	
	
	public ArrayList<Float> getListOfAccuracies() {
		return listOfAccuracies;
	}

	public void setListOfAccuracies(ArrayList<Float> listOfAccuracies) {
		this.listOfAccuracies = listOfAccuracies;
	}

	public ArrayList<ArrayList<ArrayList<String>>> getCollectionOfTotalShuffledData() {
		return collectionOfTotalShuffledData;
	}

	public void setCollectionOfTotalShuffledData(ArrayList<ArrayList<ArrayList<String>>> collectionOfTotalShuffledData) {
		this.collectionOfTotalShuffledData = collectionOfTotalShuffledData;
	}

	public ArrayList<ArrayList<String>> getListOfTrainingData() {
		return listOfTrainingData;
	}

	public void setListOfTrainingData(ArrayList<ArrayList<String>> listOfTrainingData) {
		this.listOfTrainingData = listOfTrainingData;
	}

	public ArrayList<ArrayList<ArrayList<String>>> getTotalDataSplitIn5() {
		return totalDataSplitIn5;
	}

	public void setTotalDataSplitIn5(ArrayList<ArrayList<ArrayList<String>>> totalDataSplitIn5) {
		this.totalDataSplitIn5 = totalDataSplitIn5;
	}

	public ArrayList<String> getListOfPaths() {
		return listOfPaths;
	}

	public void setListOfPaths(ArrayList<String> listOfPaths) {
		this.listOfPaths = listOfPaths;
	}

	public HashMap<Integer, HashMap<String, ArrayList<String>>> getAttributeNumber_Values_listOfTargets() {
		return attributeNumber_Values_listOfTargets;
	}

	public void setAttributeNumber_Values_listOfTargets(
			HashMap<Integer, HashMap<String, ArrayList<String>>> attributeNumber_Values_listOfTargets) {
		this.attributeNumber_Values_listOfTargets = attributeNumber_Values_listOfTargets;
	}
	private static DataStore self = new DataStore();
	public static DataStore get() { return self; }
	
	public int getTargetVariableColumnNumber() {
		return this.targetVariableColumnNumber;
	}
	public void setTargetVariableColumnNumber(int targetVariableColumnNumber) {
		this.targetVariableColumnNumber = targetVariableColumnNumber;
	}
	public float getTargetEntropy() {
		return this.targetEntropy;
	}
	public void setTargetEntropy(float targetEntropy) {
		this.targetEntropy = targetEntropy;
	}
	public HashMap<Integer, ArrayList<String>> getAttributeNumber_listOfValuesMap() {
		return attributeNumber_listOfValuesMap;
	}
	public void setAttributeNumber_listOfValuesMap(HashMap<Integer, ArrayList<String>> attributeNumber_listOfValuesMap) {
		this.attributeNumber_listOfValuesMap = attributeNumber_listOfValuesMap;
	}
	public HashMap<Integer, HashMap<String, HashMap<String, Object>>> getattributeNumber_PropertiesMap() {
		return attributeNumber_PropertiesMap;
	}
	public void setattributeNumber_PropertiesMap(HashMap<Integer,HashMap<String, HashMap<String, Object>>> attributeNumber_PropertiesMap) {
		this.attributeNumber_PropertiesMap = attributeNumber_PropertiesMap;
	}

	public HashMap<Object, Object> getDecisionTree() {
		return DecisionTree;
	}

	public void setDecisionTree(HashMap<Object, Object> decisionTree) {
		DecisionTree = decisionTree;
	}

	public Tree getId3Tree() {
		return Id3Tree;
	}

	public void setId3Tree(Tree id3Tree) {
		Id3Tree = id3Tree;
	}

	public ArrayList<ArrayList<String>> getListOfTestData() {
		return listOfTestData;
	}

	public void setListOfTestData(ArrayList<ArrayList<String>> listOfTestData) {
		this.listOfTestData = listOfTestData;
	}

	public ArrayList<String> getListOfTestDataTargets() {
		return listOfTestDataTargets;
	}

	public void setListOfTestDataTargets(ArrayList<String> listOfTestDataTargets) {
		this.listOfTestDataTargets = listOfTestDataTargets;
	}

	public ArrayList<ArrayList<String>> getListOfUnsplitTestData() {
		return listOfUnsplitTestData;
	}

	public void setListOfUnsplitTestData(ArrayList<ArrayList<String>> listOfUnsplitTestData) {
		this.listOfUnsplitTestData = listOfUnsplitTestData;
	}

	public ArrayList<ArrayList<String>> getListOfUnsplitTrainingData() {
		return listOfUnsplitTrainingData;
	}

	public void setListOfUnsplitTrainingData(ArrayList<ArrayList<String>> listOfUnsplitTrainingData) {
		this.listOfUnsplitTrainingData = listOfUnsplitTrainingData;
	}

	public HashMap<Tree<String>, ArrayList<String>> getTree_listOfPathsMap() {
		return tree_listOfPathsMap;
	}

	public void setTree_listOfPathsMap(HashMap<Tree<String>, ArrayList<String>> tree_listOfPathsMap) {
		this.tree_listOfPathsMap = tree_listOfPathsMap;
	}
	
	
	
	
	
}
