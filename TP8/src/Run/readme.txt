função para chamar o jar 

Process p = Runtime.getRuntime().exec("java -jar /home/ubuntu/eclipse-workspace/Run/WekaRun.jar FILE:/home/ubuntu/project/arqsw_20182_grupo1/TP8/src/dataset/datasetTP8.arff C:3");
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));		
		String linha; 
		while ((linha = in.readLine()) != null) {
			System.out.println(linha);
		}
		