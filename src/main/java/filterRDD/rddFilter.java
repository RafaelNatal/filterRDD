package filterRDD;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.Function;

import java.util.ArrayList;
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
		
		groupByName();
		
		/**
		 * Exmeplo 5
		 * Agrupa elementos de uma lista de inteiros
		 * Cada item da lista é uma idade.
		 * Funcao deve receber um Array de idades e escrever na tela
		 * os grupos "Adulto"(>= 18 && <65), "Criancas"(<18) e "idoso"(<65)
		 * 
		 */
		Integer myAgesArray[] = {2,52,44,23,17,14,18,82,51,64,71,67};
		groupByAge(myAgesArray);
		
		

	}
	
	private static void groupByAge(Integer myAgeArray[]) {
		
		JavaRDD<Integer> myAges = sc.parallelize(Arrays.asList(myAgeArray));
		
		JavaPairRDD<String, Iterable<Integer>> agrupamento = myAges.
				groupBy(age -> {
					if(age >= 18 && age <65) {return "Adulto";}
					else if(age < 18){return "Criancas";}
					else {return "idoso";}
					});
		
		System.out.println(agrupamento.collect());
		
	}

	private static void groupByName() {
		JavaRDD<String> myNames = sc.parallelize( Arrays.asList("Bruno","Cesar",
				"Juan","Bianca", "Joseph", " Budda", "Jonny","Carla","Dani",
				"Douglas", "Jeff", "Duda", "Pietra","Bruno"),2);
		
		JavaPairRDD<Character, Iterable<String>> agrupamento = myNames.groupBy(
				
					name -> name.charAt(0)
				
				
				);
		
		System.out.println(agrupamento.collect());
		// Filtra a lista que contem os nome comecados com 'B'
		agrupamento = agrupamento.filter(name -> name._1 == 'B');
		System.out.println(agrupamento.collect());
	}

	private static Function<String, Boolean> filterString = (line -> (
			(line.contains("Ipanema")) || (line.contains("dourado")) || (line.contains("mar"))));
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
		
		System.out.println("Número de linhas que passaram pelo filtro: " + filteredLines.count());
		
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
		JavaRDD<Integer> myNumbers = sc.parallelize(Arrays.asList(myIntArray), 1);
		myNumbers.cache();
		
		// Executando o filtro
		JavaRDD<Integer> filteredRDD = myNumbers.filter(filterDiv);
		filteredRDD.foreach(f -> System.out.print(" " + f));
		System.out.print(" ");
		
		// Filtrando o Texto
			
		
		
	}

}
