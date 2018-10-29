package net.alberane;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
 
public class Cluster {
 
	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;
 
		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}
 
		return inputReader;
	}
 
	public static void main(String[] args) throws Exception {
		SimpleKMeans kmeans = new SimpleKMeans();
		kmeans.setSeed(10);
 
		//important parameter to set: preserver order, number of cluster.
		kmeans.setPreserveInstancesOrder(true);
		kmeans.setNumClusters(3);
		
		BufferedReader datafile = readDataFile("/home/alberane/eclipse-workspace/Kmeans/src/net/alberane/datasetTP8.arff"); 
		Instances data = new Instances(datafile);
		
		kmeans.buildClusterer(data);
 
		// This array returns the cluster number (starting with 0) for each instance
		// The array has as many elements as the number of instances
		int[] assignments = kmeans.getAssignments();
		int i=0;
		for(int clusterNum : assignments) {
		    System.out.printf("Move \"%s\" to -> \"Cluster_%d\" \n", data.instance(i).stringValue(0), clusterNum);
		    i++;
		}
	}
}
