package general;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class FileGenerate {
	public static FileGenerate instance;

	public FileGenerate() {
	}

	public static FileGenerate getInstance() {
		if (instance == null) {
			instance = new FileGenerate();
		}
		return instance;
	}

	public void criarArquivo(Set<String> allDependencies, Map<String, Set<String>> dependencies) throws Exception {

		Set<String> dependeciesProject = allDependencies;
		File file = new File("/home/willian/Desenvolvimento/arqsw_20182_grupo1/TP8/src/KMeans/src/kmeans.arff");
		file.createNewFile();
		FileWriter fileWriter = new FileWriter(file, false);
		PrintWriter writer = new PrintWriter(fileWriter);
		int count = 1;
		writer.print("@relation cluster-test \n\n");
		writer.print("@attribute nome {");
		for (String attribute : dependencies.keySet()) { 
			if (count<dependencies.size()) {
				writer.print(attribute+",");
			}else {
				writer.print(attribute);
			}
			count+=1;
			
		}
		writer.print("}\n");
	
		for (String attribute : dependeciesProject) {
			writer.print("@attribute " + attribute + "{TRUE,FALSE} \n");
		}
		writer.print("\n");
		writer.print("@data \n");		
		for (String types : dependencies.keySet()) { // invert for
			count=1;
			writer.print(types + ",");
			for (String dp : dependeciesProject) {
				if (count<dependeciesProject.size()) {
					if (dependencies.get(types).contains(dp)) {
						writer.print("TRUE,");

					} else {
						writer.print("FALSE,");
					}
				}else {
					if (dependencies.get(types).contains(dp)) {
						writer.print("TRUE");

					} else {
						writer.print("FALSE");
					}
				}
				count+=1;
				
			}
			writer.println();
		}
		writer.close();
		
		/*DataSource source = null;
		source = new DataSource("dependencias.arff");
		Instances cpu = source.getDataSet();
		SimpleKMeans kmeans = new SimpleKMeans();
		kmeans.setSeed(10);
		try {
			kmeans.setPreserveInstancesOrder(true);
			kmeans.setNumClusters(3);
			kmeans.buildClusterer(cpu);
			//ClusterEvaluation eval; 
			String[] options;
			options    = new String[2];
			options[0] = "-t";
			options[1] = "dependencias.arff";
			//ClusterEvaluation.evaluateClusterer(kmeans,options);
			//System.out.println(kmeans.);
		}finally {
			
		}*/
		
		/*ClusterEvaluation eval;
	    Instances               data;
	    String[]                options;
	    DensityBasedClusterer   cl;    
	    double                  logLikelyhood;

	    data = DataSource.read("dependencias.arff");*/
	}
}
