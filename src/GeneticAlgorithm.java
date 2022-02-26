
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GeneticAlgorithm {

	//PARAMETRI LOGGING
	public static final int MAX_NUMBER_FE = 20000;
	private static final int GEN_LOG = 50;
	private static final boolean VERBOSE = false;
	
	//PARAMETRI ALGORITMO
	//NON TOCCARE I PARAMETRI 11842 sulle 200_3000 BUONO COME RISULTATO 11600 
	//public static  int ELITE_SELECTED = 10;
	private static final int THRESHOLD_INCEST_PREVENTING =3;
	private static final int LOCAL_MINIMA_THRESHOLD = 25;
	private static final int PERTURB_SOLUTION = 95;
	private static String TYPE_SELECTION = "mu_lambda";
	public static int PROB_POPULATION = 80;
	public static double  CROSSOVER_PROBA=0.85;
	public static double  MUTATION_PROBA=0.15;
	public static int     POP_SIZE=150;
	private static  int MUTATION_SIZE = 1;
	
	//PER RIPETIBILITa
	public static Random random_generator=new Random(10);
	
	//VARIABILI DI UTILITa
	public static Individual min_solution_overall =null;
	public static int min_solution_overall_epoch=0;
	public static HashMap<String, ArrayList<Double>> epoch_mean_for_file = new HashMap<String, ArrayList<Double>>();
	public static HashMap<String, ArrayList<Double>> epoch_min_for_file = new HashMap<String, ArrayList<Double>>();
	public static HashMap<String, ArrayList<Double>> epoch_div_for_file=new HashMap<String, ArrayList<Double>>();
	public static ArrayList<Double> tmp_epoch_list=new ArrayList<Double>();
	public static ArrayList<Double> tmp_min_list=new ArrayList<Double>();
	public static ArrayList<Double> tmp_div_list=new ArrayList<Double>();
	
	private static final DecimalFormat df = new DecimalFormat("0.00");
	private static int number_fe_to_find_min=0;
	private static int media_it=0;
    public static void main(String[] args) {
        
    	
    	//small_instances_run();    	
    	medium_instances_run();
    	large_instaces_run();
    	//medium_instance_("C:\\Users\\user\\Desktop\\istanze\\medium_instances", "tipo_200_3000");
    	//medium_instance_("C:\\Users\\user\\Desktop\\istanze\\medium_instances", "tipo_100_500");
    	
    	
    	try {
			
    		log_data("log_epoch.csv",epoch_mean_for_file);
    		log_data("log_min.csv",epoch_min_for_file);
    		log_data("log_div.csv",epoch_div_for_file);
    		
		} 
    	catch (IOException e) {
			System.out.println("error log");
			e.printStackTrace();
		}
    }
    
    private static void log_data(String type_data, HashMap<String, ArrayList<Double>> tmp_list) throws IOException {
    	File file = new File("C:\\Users\\user\\Desktop\\" + type_data);
		BufferedWriter bf = new BufferedWriter( new FileWriter(file));
		bf.close();
		try {		
            bf = new BufferedWriter(new FileWriter(file));
            for (Map.Entry<String, ArrayList<Double>> entry :	tmp_list.entrySet()) {
            	String buffer="";
            	for(Object tmp : entry.getValue()) {
            		buffer+=df.format(tmp)+";";
            	}
                bf.write(entry.getKey() + ";"+ buffer);//entry.getValue());

                bf.newLine();
            }
            bf.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                bf.close();
            }
            catch (Exception e) {
            	System.out.println("ook");
            }
        }
    }
    /*
	private static void log_min_data_on_file() throws IOException {
		// TODO Auto-generated method stub
		File file = new File("C:\\Users\\user\\Desktop\\min_epoch.csv");
		BufferedWriter bf = new BufferedWriter( new FileWriter(file));
		bf.close();
		try {		
            bf = new BufferedWriter(new FileWriter(file));
            for (Map.Entry<String, ArrayList<Double>> entry :	epoch_min_for_file.entrySet()) {
            	String buffer="";
            	for(Double tmp : entry.getValue()) {
            		buffer+=df.format(tmp)+";";
            	}
                bf.write(entry.getKey() + ";"+ buffer);//entry.getValue());

                bf.newLine();
            }
            bf.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                bf.close();
            }
            catch (Exception e) {
            }
        }
	}
	
	private static void log_div_data_on_file() throws IOException {
			// TODO Auto-generated method stub
		File file = new File("C:\\Users\\user\\Desktop\\log_div.csv");
		BufferedWriter bf = new BufferedWriter( new FileWriter(file));
		bf.close();
		try {		
            bf = new BufferedWriter(new FileWriter(file));
            for (Map.Entry<String, ArrayList<Double>> entry :	epoch_div_for_file.entrySet()) {
            	String buffer="";
            	for(Integer tmp : entry.getValue()) {
            		buffer+= tmp+";";
            	}
                bf.write(entry.getKey() + ";"+ buffer);//entry.getValue());

                bf.newLine();
            }
            bf.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                bf.close();
            }
            catch (Exception e) {
            }
        }
	}
	
	private static void log_mean_data_on_file() throws IOException {
		
		File file = new File("C:\\Users\\user\\Desktop\\log_epoch.csv");
		BufferedWriter bf = new BufferedWriter( new FileWriter(file));
		bf.close();
		try {
			  
            // create new BufferedWriter for the output file
            bf = new BufferedWriter(new FileWriter(file));
  
            // iterate map entries
            for (Map.Entry<String, ArrayList<Double>> entry :	epoch_mean_for_file.entrySet()) {
  
                // put key and value separated by a colon
            	String buffer="";
            	for(Double tmp : entry.getValue()) {
            		buffer+=df.format(tmp)+";";
            	}
                bf.write(entry.getKey() + ";"+ buffer);//entry.getValue());
  
                // new line
                bf.newLine();
            }
           
  
            bf.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
  
            try {
  
                // always close the writer
                bf.close();
            }
            catch (Exception e) {
            }
        }
		
	}
*/

	public static void large_instaces_run() {
		String dir_name="C:\\Users\\user\\Desktop\\istanze\\large_instances";
		
    	File dir = new File(dir_name);
        int sum=0;
        int run_large=10;
    	String[] pathnames=dir.list();
    	for (String pathname : pathnames) {
 
	        ResultReadFile result= read_file(dir_name+"\\"+pathname);
	        int numero_nodi=result.getN();
	        int[] vertex_weight=result.getM();
	        int[][] matrix=result.getMatrix();
	        for(int i=0;i<run_large;i++) {
	        	System.out.println(pathname);
		        sum+=(run_genetic_algorithm(numero_nodi, vertex_weight, matrix,pathname)).get_fitness();
		       
	        }
	        System.out.println("Media large instances 10 run: "+pathname+","+ 1.0*sum/run_large);
	        //mean+=tmp.get_fitness();
    	}  
    	
		//run_instance(dir_name, "tipo_800_10000");
		
	}


	public static void medium_instances_run() {
		String dir_name="C:\\Users\\user\\Desktop\\istanze\\medium_instances";
		/*
		File dir = new File(dir_name);
    	double mean=0.0;
    	String[] pathnames=dir.list();
    	System.out.println(pathnames);
    	for (String dir_type: pathnames) {
    		mean = genetic_algorithm_files(dir_name,  dir_type); 
	    	System.out.println("Media medium di "+dir_type +": " +  +mean+"\n");
    	}
    	*/
    	
    	//String[] tipo_medium={"tipo_200_3000","tipo_100_500"};
    	
		//run_instance(dir_name, "tipo_100_500");
		//run_instance(dir_name,"tipo_100_2000");
		//run_instance(dir_name,"tipo_200_750");
		run_instance(dir_name, "tipo_200_3000");
		//run_instance(dir_name, "tipo_20_120");
   	    	
	}
    
    
	public static void small_instances_run() {
		//String dir_name="C:\\Users\\user\\Desktop\\wvcp-instances\\small_instances";
		String dir_name="C:\\Users\\user\\Desktop\\test";
		/*
    	File dir = new File(dir_name);
        double mean=0.0;
    	String[] pathnames=dir.list();
    	
    	for (String dir_type: pathnames) {
    		long startTime = System.currentTimeMillis();
    		mean = genetic_algorithm_files(dir_name,  dir_type); 
    		long endTime = System.currentTimeMillis();
    		//System.out.println("Ci ha impiegato " + (endTime - startTime) + " milliseconds");
	    	System.out.println("Media small di "+dir_type +": " +  +mean+", Media Iterazioni "+media_it/10 +", ci ha impiegato " + (endTime - startTime)/1000.0 + " s\n");
	    	
	    	media_it=0;
    	}
    	*/
	}

	public static void run_instance(String dir_name,String dir_base) {
		
		File dir = new File(dir_name+"\\"+dir_base);
		String[] pathnames=dir.list();
		int sum=0;
		
		long tmp_=0;
		for (String pathname : pathnames) {
			
		    ResultReadFile result= read_file(dir_name+"\\"+dir_base+"\\"+pathname);
		    int numero_nodi=result.getN();
		    int[] vertex_weight=result.getM();
		    int[][] matrix=result.getMatrix();
		    long startTime = System.currentTimeMillis();
		    sum+=(run_genetic_algorithm(numero_nodi, vertex_weight, matrix,pathname)).get_fitness();
		    long endTime = System.currentTimeMillis();
		    tmp_+=endTime-startTime;
		    //sum+=tmp.get_fitness();
		}
		
		//return 1.0*sum/pathnames.length;
		System.out.println("Media large instances 10 run: "+dir_base+", "+ 1.0*sum/pathnames.length+", Tempo:"+(tmp_)/1000.0);
	}

	public static double  genetic_algorithm_files(String dir_name, String dir_base) {
		
		File dir = new File(dir_name+"\\"+dir_base);
		String[] pathnames=dir.list();
		int sum=0;
		for (String pathname : pathnames) {
			
		    ResultReadFile result= read_file(dir_name+"\\"+dir_base+"\\"+pathname);
		    int numero_nodi=result.getN();
		    int[] vertex_weight=result.getM();
		    int[][] matrix=result.getMatrix();
		   
		    sum+=(run_genetic_algorithm(numero_nodi, vertex_weight, matrix,pathname)).get_fitness();
		    //sum+=tmp.get_fitness();
		}
		
		return 1.0*sum/pathnames.length;
	}
	
	
	public static Individual run_genetic_algorithm(int numero_nodi, int[] vertex_weight, int[][] matrix,String pathname) {
        //int sum=0;
		
        try {

        	genetic_algorithm(numero_nodi,vertex_weight,matrix,pathname);
        	
		} 
        catch (TypeSelectionException e) {
        	System.out.println("Il type è invalido, deve essere tra questi (val a destra)");
        	for(TypeSelection env : TypeSelection.values()){
        	    System.out.println(env.name() + " : " + env.get_label());
        	}
        }
        catch (ArrayIndexOutOfBoundsException e) {
        	System.out.println(e);
        	System.out.println("di");
        }
        catch (Exception e) {
			//System.out.println("RUN:"+i+",  FE LIMITED");
        	//System.out.println(e);
			System.out.println("File:"+pathname+", "+min_solution_overall+", Trovata all'iterazione:"+min_solution_overall_epoch+", FE richieste:"+number_fe_to_find_min);
			//System.out.println("Trovata all'iterazione:"+min_solution_overall_epoch+"\n\n");
			//sum+=min_solution_overall.get_fitness();
			Individual tmp=min_solution_overall;
			epoch_mean_for_file.put(pathname,tmp_epoch_list);
			epoch_min_for_file.put(pathname, tmp_min_list);
			epoch_div_for_file.put(pathname,tmp_div_list);
			
			media_it+=number_fe_to_find_min;
			
			reset_FE();
			reset_MINIMUM_OVERALL();
			reset_tmp_epoch_list();
			reset_tmp_min_list();
			reset_tmp_div_list();
			
			return tmp;
			
		}
        //System.out.println("RUN FINITE, VALORE MEDIO FITNESS:"+1.0*sum/run);
        
		return min_solution_overall;
	}
    private static void reset_tmp_div_list() {
    	tmp_div_list=new ArrayList<Double>();		
    }
	private static void reset_tmp_epoch_list() {
    	tmp_epoch_list=new ArrayList<Double>();	
	}
    private static void reset_tmp_min_list() {
    	tmp_min_list=new ArrayList<Double>();	
	}

	private static void reset_MINIMUM_OVERALL() {
    	GeneticAlgorithm.min_solution_overall_epoch=0;
    	GeneticAlgorithm.min_solution_overall=null;
    		
	}
	private static void reset_FE() {
		Individual.NUMBER_FE=0;	
	}
	public static void genetic_algorithm(int n,int[] m,int[][] matrix,String pathname) throws Exception {
    	
		//epoch_mean_for_file.put(pathname,new ArrayList<Double>());
    	Population popu=new Population(matrix,m,POP_SIZE,n,true,TYPE_SELECTION);
    
    	int gen=0;
    	int local_minima_counter=0;
    	Individual tmp_last_min=null;
    	
    	while (true){
    		ArrayList<Individual> new_generation=new ArrayList<Individual>();
        	//System.out.println("prova");
    		for (int i=0;i<popu.get_population().size();i++) {
	    		//SELEZIONE        CROSSOVER	MUTAZIONE
	    		if (random_generator.nextDouble()<=CROSSOVER_PROBA){
	
	    			Individual a=popu.get_individual(popu.choice_prop_fitness());
	    			Individual b=popu.get_individual(popu.choice_prop_fitness());
	    			if (incest_preventing(a,b)) {
			    		ResultReadFile res_crossover=crossover(a,b,m);
			    		
			    		Individual c=res_crossover.getC();
			    		Individual d=res_crossover.getD();
			    		
			    		c=res_mutazione(c,m);
			    		d=res_mutazione(d,m);
			    		
			    		if(!duplicate_checking(c,new_generation)) {
			    			
			    			c=popu.repair_individual(c, m);
			    			new_generation.add(c);
			    		}
			    			
			    		if(!duplicate_checking(d,new_generation)) {
			    			d=popu.repair_individual(d, m);
			    			new_generation.add(d);
			    		}
			    		
	    			}
	    			
	    		 }   
	    		
    		}
    		
    		
    		//REPLACEMENT
    		popu.replacement(new_generation);
    		
    		double mean_population=popu.get_mean_population();
    		Individual min_epoch=popu.get_min_solution_epoch();
    		tmp_epoch_list.add(mean_population);
    		tmp_min_list.add(1.0*min_epoch.get_fitness());
    		int diversity_measure=popu.get_diversity_population();
    		tmp_div_list.add(1.0*diversity_measure);
    		
    		Individual tmp=min_solution_overall;
    		min_solution_overall = (min_solution_overall==null)?min_epoch:(min_epoch.get_fitness()<min_solution_overall.get_fitness())?min_epoch:min_solution_overall;
    		if (tmp==null){
    			if (min_solution_overall!=null){
    				min_solution_overall_epoch=gen;
    				number_fe_to_find_min=Individual.NUMBER_FE;
    			}
    		}
    		else if (min_solution_overall.get_fitness()<tmp.get_fitness()) {
    			min_solution_overall_epoch=gen;
    			number_fe_to_find_min=Individual.NUMBER_FE;
			}
    		
    		//LOGICA LOCAL MINIMA
    		//PROVIAMO AD USCIRE DAL MINIMO AUMENTANO LA PROB DI MUTAZIONE
    		if(tmp_last_min!=null) {
    			if(tmp_last_min.get_fitness()==min_epoch.get_fitness()) {
    				local_minima_counter++;
    			}
    			else {
    				local_minima_counter=0;
    				GeneticAlgorithm.MUTATION_PROBA=0.1;
    				MUTATION_SIZE = 5;
    				
    				//TYPE_SELECTION=new String("mu_lambda");
    				
    			}
    		}
    		//System.out.println("ssse");
    		if(local_minima_counter==GeneticAlgorithm.LOCAL_MINIMA_THRESHOLD) {
    			GeneticAlgorithm.MUTATION_PROBA=0.55;
    			 MUTATION_SIZE = 5;
    			//PROB_POPULATION=90;
    			//TYPE_SELECTION=new String("general_selection");
    		
    			pertub_solution(popu,m);
    		}
    		
        	
    		//END LOGICA LOCAL MINIMA
    		
    		if (( gen % GEN_LOG==0)&& VERBOSE)print_log(gen,mean_population,min_epoch,min_solution_overall,diversity_measure);
    		
    		gen++;	
    		tmp_last_min=min_epoch;
    	}
    	
    	
    }
    
    private static void pertub_solution(Population population,int[] weight) throws Exception {
		//Prende le soluzioni migliori e le toglie, ho poca diversità
    	Collections.sort(population.get_population(), new ComparatorIndividual());
    	for (int i=0;i<GeneticAlgorithm.PERTURB_SOLUTION;i++) {
    		Individual tmp=new Individual(population.random_initialize(),null,false);
    		tmp=population.repair_individual(tmp, weight);
    		population.get_population().set(i, tmp); 	
    	}
		
	}
	private static boolean duplicate_checking(Individual c, ArrayList<Individual> new_generation) {
		// TODO Auto-generated method stub
    	int threshold_tolerance=5;
    	for(int i=0;i<new_generation.size();i++) {
    		if (hamming_distance(c,new_generation.get(i))==threshold_tolerance) {
    			return true;
    			
    		}
    	}
    	
		return false;
	}
	private static boolean incest_preventing(Individual a, Individual b) {
    	return hamming_distance(a,b)>GeneticAlgorithm.THRESHOLD_INCEST_PREVENTING;
	}
	public static int hamming_distance(Individual a, Individual b) {
		// TODO Auto-generated method stub
		int sum=0;
		for(int i=0;i<a.get_solution_length();i++) {
			sum+=a.get_solution_value(i)!=b.get_solution_value(i)?1:0;
		}
		return sum;
	}
	private static void print_log(int gen, double mean_population, Individual min_epoch, Individual min_solution_overall,int diversity_measure) {
		System.out.println("Generazione:"+gen+", Media pop:"+df.format(mean_population)+", Minimo epoca:"+min_epoch+", Minimo overall:"+min_solution_overall+", MUTATION_PROBA:"+GeneticAlgorithm.MUTATION_PROBA+", Div measure:"+diversity_measure);	
	}
	
	private static Individual res_mutazione(Individual c,int[] weight) throws Exception {
    	
    	for(int i=0;i<MUTATION_SIZE;i++) {
    		double p=random_generator.nextDouble();
	    	if (p<=MUTATION_PROBA) {
	    		int index=(int)(Math.floor((c.get_solution_length()*random_generator.nextDouble())));
	    		c.set_bit_solution(index, 1-(c.get_solution_value(index)));
	    	}
    	}
    	/*
    	if (p<=MUTATION_PROBA) {
    		int index=(int)(Math.floor((c.get_solution_length()*random_generator.nextDouble())));
    		c.set_bit_solution(index, 1-(c.get_solution_value(index)));
    	}
    	*/
    	return c;
	}
	public static ResultReadFile crossover(Individual a,Individual b,int[] weight) throws Exception {
    	int[] cc=new int[a.get_solution_length()];
    	int[] dd=new int[a.get_solution_length()];
    	
    	
    	for(int i=0;i<a.get_solution_length();i++) {
    		double p=random_generator.nextDouble();
    		cc[i]=p<0.5?a.get_solution()[i]:b.get_solution()[i];
    		dd[i]=p<0.5?b.get_solution()[i]:a.get_solution()[i];
    		
    	}
    	
    	Individual c=new Individual(cc,weight,false);
    	Individual d=new Individual(dd,weight,false);
    	
    	return new ResultReadFile(c,d);
    	
    	
    }

    public static ResultReadFile read_file(String file) {
		BufferedReader reader;
		int numero_nodi=0;
        int[] vertex_weight= {0};
        int[][] matrix= {{0}};
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
            numero_nodi=Integer.parseInt(line);
            line = reader.readLine();
            vertex_weight=new int[numero_nodi];
            matrix=new int[numero_nodi][numero_nodi];
            
            String[] vertex = line.trim().split(" ");
           
            //vertex_weight=Arrays.stream(line.strip().trim().split("  ")).mapToInt(Integer::parseInt).toArray();  
			
			int index=0;
			for (String vertex_: vertex){ 
				if (!vertex_.equals("")){
					//System.out.println(vertex_);
					vertex_weight[index++]=Integer.parseInt(vertex_.trim().strip());
				 	
				}
				//vertex_weight[index++]=Integer.parseInt(vertex_.trim().strip());
			 }
			
			//System.out.println(vertex_weight);
			line = reader.readLine();
            int index_row=0;
			while (line != null) {
				//System.out.println(Arrays.toString(line.trim().split(" ")));
				String[] Adj=line.trim().split(" ");
				int index_col=0;
				for (String edge: Adj){ 
					if (!edge.equals("")){
						//System.out.println(edge);
						matrix[index_row][index_col++]=Integer.parseInt(edge.trim().strip());
					 	
					}
				}
				index_row++;
				
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("dio");
			e.printStackTrace();
		}
		
		
		ResultReadFile result=new ResultReadFile(numero_nodi,vertex_weight,matrix);
		
		return result;
		
		
	}

}