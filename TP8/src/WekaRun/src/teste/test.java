package teste;

import java.util.ArrayList;


import weka.*;
import weka.clusterers.AbstractClusterer;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Capabilities;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.neighboursearch.kdtrees.KMeansInpiredMethod;
import weka.gui.explorer.ClustererAssignmentsPlotInstances;
import weka.gui.explorer.ExplorerDefaults;

public class test {
	public static void main(String[] args) throws Exception {
		DataSource source = null;
		source = new DataSource("/home/willian/Desenvolvimento/arqsw_20182_grupo1/TP8/src/KMeans/kmeans.arff");
		Instances cpu = source.getDataSet();		
		SimpleKMeans kmeans = new SimpleKMeans();
		try {
			kmeans.setPreserveInstancesOrder(true);
			kmeans.setNumClusters(3);
			//kmeans.setSeed(10);
			kmeans.buildClusterer(cpu);
			cpu.setClassIndex(0);			
			ClusterEvaluation eval = new ClusterEvaluation(); 			
			eval.setClusterer(kmeans);
			eval.evaluateClusterer(cpu);			
			/*String[] options;
			options    = new String[2];
			options[0] = "-t";
			options[1] = "/home/willian/Desenvolvimento/arqsw_20172_grupo2/TP8/TP8-KMeans/src/dependencias.arff";
			ClusterEvaluation.evaluateClusterer(kmeans,options);*/
			ClustererAssignmentsPlotInstances plotInstances = ExplorerDefaults.getClustererAssignmentsPlotInstances();
			
			System.out.println(eval.clusterResultsToString());
			
			int [] assignments = kmeans.getAssignments();
			int i = 0; 
			
			for (int clusterNum: assignments) {
				System.out.printf("Instance %d -> Cluster %d \n", i, clusterNum);
				i++;
			}			
			
			//weka.clusterers.SimpleKMeans -N 3 -A "weka.core.EuclideanDistance -R first-last" -I 500 -S 10
			//System.out.println(eval.cluster));
		}finally {
			
		}
	}
}
