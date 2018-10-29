import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.gui.explorer.ClustererAssignmentsPlotInstances;
import weka.gui.explorer.ExplorerDefaults;

public class Run {

	public static void main(String[] args) throws Exception {
		String file = null;//"/home/ubuntu/project/arqsw_20182_grupo1/TP8/src/dataset/datasetTP8.arff";
		int cluster = 0;
		for(String arg : args) {
			String[] arq = arg.split(":");
			System.out.println(arg);
			if (arg.contains("FILE:")){
				file = arq[1]; 
			}else if (arg.contains("C:")) {
				cluster =Integer.parseInt(arq[1]);
			}
		}
		
		DataSource source = null;
		//source = new DataSource("/home/willian/Desenvolvimento/arqsw_20182_grupo1/TP8/src/KMeans/kmeans.arff");
		source = new DataSource(file);
		Instances cpu = source.getDataSet();		
		SimpleKMeans kmeans = new SimpleKMeans();
		try {
			kmeans.setPreserveInstancesOrder(true);
			//kmeans.setNumClusters(3);
			kmeans.setNumClusters(cluster);
			
			//kmeans.setSeed(10);
			kmeans.buildClusterer(cpu);
			cpu.setClassIndex(0);			
			ClusterEvaluation eval = new ClusterEvaluation(); 			
			eval.setClusterer(kmeans);
			eval.evaluateClusterer(cpu);			
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
