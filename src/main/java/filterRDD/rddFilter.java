package filterRDD;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.Function;

import java.util.Arrays;
import java.util.List;

public class rddFilter {
	
	private static JavaSparkContext sc;
	
	public static void main(String[] args) {
		/**
		 * Configurando um context Spark
		 * .setAppName("filterRDD") é o nome do job
		 * .setMaster("local") é nome do servidor master
		 */
		
		//Configurando um contexto Spark
		SparkConf conf = new SparkConf().setAppName("filterRDD").setMaster("local");
		
//		Instanciando um contexto Spark
		sc = new JavaSparkContext(conf);
		
		/**
		 * Exemplo 2: Filtrando dados (interiros)
		 * Neste ecemplo iremos criar um RDD com uma
		 * squencia de numeros inteiros aleatorios.
		 * 1: Criando um Array de inteiros
		 */
		Integer myIntArray[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,130};
		filterNumbers(myIntArray);
		/**
		 *  Emplo 3: Filtrandi dados (Strings)
		 *  Neste ecemplo iremos criar um RDD com o 
		 *  conteudo do arquivo
		 *  Instancioando um contexto do spark
		 */
		
		filterText();
		
		/**
		 * Exemplo 4
		 * Agrupar elemntos de uma lista.
		 * Neste exemplo iremos agrupar Strings pelo primenrio caractere
		 * groupBy
		 */
		
		
		

	}
	
	private static Function<String, Boolean> filterString = (line -> (
			(line.contains("Ipanema")) || (line.contains("dgit git sdfgsdfgsfdgsdfgsdfgsdfgsdfgsdfggsfdgsgiourado")) || (line.contains("mar"))));
	/**
	 * Neste exemplo desejamos filtrar o texto lido
	 * gerando um novo rdd apenas  com as alinhas que contem
	 * uma palavra especifica. Neste caso, "Ipanema".
	 */
	private static void filterText() {
		String filterPath = new String("hdfs://quickstart.cloudera/user/cloudera/garota.txt");
		JavaRDD<String> file = sc.textFile(filterPath);
		file.cache();
		
		System.out.println("Número de linhas no arquivo: " + file.count());
		
		JavaRDD<String> filteredLines = file.filter(filterString);
		
		System.out.println("Númer de linhas que passaram pelo filtro: " + filteredLines.count());
		
		filteredLines.foreach(line -> System.out.println(">> " + line));
		
		int index = 0;
		JavaRDD<String> words = filteredLines.map(line -> Arrays.asList(line.split(" ")).get(index));
		
		words.foreach(w -> System.out.println(">> " + w));
		System.out.println(">> " + words.collect());
		
	}

	// Filtrando o Texto
	private static Function<Integer, Boolean> filterDiv = (numb -> numb % 2 == 0);
	
	private static void filterNumbers(Integer[] myIntArray) {
		/**
		 * Criando nosso RDD com um Array de numeros interios 
		 * O segundo parametro de parellelize() é o numero de particoes
		 * desejado. Quando omitido, o Spark ira criar o numero de
		 * Particoes que ele determinar mais adequado para o sistema
		 */
		JavaRDD<Integer> myNumbers = sc.parallelize(Arrays.asList(myIntArray), 5);
		myNumbers.cache();
		
		// Executando o filtro
		JavaRDD<Integer> filteredRDD = myNumbers.filter(filterDiv);
		filteredRDD.foreach(f -> System.out.print(" " + f));
		System.out.print(" ");
		
		// Filtrando o Texto
			
		
		
	}

}
