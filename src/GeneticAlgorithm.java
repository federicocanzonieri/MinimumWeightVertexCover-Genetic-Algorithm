
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
	private static final int GEN_LOG = 5;
	private static final boolean VERBOSE = false;
	
	//PARAMETRI ALGORITMO
	
	
	static final int THRESHOLD_INCEST_PREVENTING =5;
	private static final boolean INCEST_PREVENTING = false;
	private static final int LOCAL_MINIMA_THRESHOLD = 6;
	static final int PERTURB_SOLUTION = 3;
	private static String TYPE_SELECTION = "mu_lambda";
	public static double  CROSSOVER_PROBA=0.85;
	public static double  MUTATION_PROBA_START=0.15;
	static  int MUTATION_SIZE_START = 3;
	public static double  MUTATION_PROBA=MUTATION_PROBA_START;
	static  int MUTATION_SIZE = MUTATION_SIZE_START;
	public static int     POP_SIZE = 100;
	static 	int MUTATION_SIZE_BOOST = 4;
	static  double MUTATION_PROB_BOOST=0.25;
	
	public static int PROB_POPULATION = 80;
	
	
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
        
    	
    	// IL PRIMO PARAMETRO è il PATH ALLE INSTANCE es istanze/small_instances
    	// IL SECONDO PARAMETRO è il tipo di istanza
    	
    	
    	run_instance(".."+ File.separator+"istanze"+ File.separator +"small_instances", "tipo_20_60",1);
    	
    	/*
    	run_instance(".."+ File.separator +"istanze"+ File.separator +"small_instances", "tipo_20_120",1);
		run_instance(".."+ File.separator +"istanze"+ File.separator +"small_instances", "tipo_25_150",1);
		run_instance(".."+ File.separator +"istanze"+ File.separator +"medium_instances", "tipo_100_500",1);
		run_instance(".."+ File.separator +"istanze"+ File.separator +"medium_instances", "tipo_100_2000",1);
		run_instance(".."+ File.separator +"istanze"+ File.separator +"medium_instances", "tipo_200_750",1);
		run_instance(".."+ File.separator +"istanze"+ File.separator +"medium_instances", "tipo_200_3000",1);
		run_instance(".."+ File.separator +"istanze"+ File.separator +"large_instances", "tipo_800_10000",1);
		*/
		
    	//small_instances_run();    	
    	//medium_instances_run();
    	//large_instaces_run();
    	
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
    
    private static void print_parameter_value() {
    	System.out.println("POP_SIZE:"+POP_SIZE+", MT_PROB_START:"+MUTATION_PROBA_START+", MT_SIZE_START:"+MUTATION_SIZE_START+", CR_PROBA:"+CROSSOVER_PROBA+", TYPE_SELECTION:"+TYPE_SELECTION+", PERTURB_SOLUTION:"
    			+ PERTURB_SOLUTION+", LOCAL_MINIMA_THRESHOLD:"+LOCAL_MINIMA_THRESHOLD+", THRESHOLD_INCEST_PREVENTING:"+THRESHOLD_INCEST_PREVENTING
    			+ ", INCEST_PREVENTING_ACTIVE:"+INCEST_PREVENTING
    			+ ", MUTATION_SIZE_BOOST:"+MUTATION_SIZE_BOOST+", MUTATION_PROB_BOOST:"+MUTATION_PROB_BOOST
    			
    	);
		
	}
    
    private static void log_data(String type_data, HashMap<String, ArrayList<Double>> tmp_list) throws IOException {
    	File file = new File( type_data);
    	System.out.println(type_data);
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
 
	public static void large_instaces_run() {
		String dir_name=".."+ File.separator +"istanze"+ File.separator + "large_instances";
		
		run_instance(dir_name, "tipo_800_10000",10);
		
	}


	public static void medium_instances_run() {
		String dir_name=".."+ File.separator +"istanze"+ File.separator + "medium_instances";
		
		run_instance(dir_name, "tipo_100_500",1);
		run_instance(dir_name,"tipo_100_2000",1);
		run_instance(dir_name,"tipo_200_750",1);
		run_instance(dir_name, "tipo_200_3000",1);
   	    	
	}
    
    
	public static void small_instances_run() {
		//String dir_name="C:\\Users\\user\\Desktop\\wvcp-instances\\small_instances";
		String dir_name=".."+ File.separator +"istanze"+ File.separator +"small_instances";

		run_instance(dir_name, "tipo_20_60",1);
		run_instance(dir_name, "tipo_20_120",1);
		run_instance(dir_name, "tipo_25_150",1);

		
	}

	public static void run_instance(String dir_name,String dir_base, int run) {
		
		File dir = new File(dir_name+File.separator+dir_base);
		String[] pathnames=dir.list();
		int sum=0;
		System.out.println(dir_name+File.separator+dir_base+Arrays.toString(pathnames));
		long tmp_=0;
		for (String pathname : pathnames) {
			
		    ResultReadFile result= read_file(dir_name+File.separator+dir_base+File.separator+pathname);
		    int numero_nodi=result.getN();
		    int[] vertex_weight=result.getM();
		    int[][] matrix=result.getMatrix();
		    long startTime = System.currentTimeMillis();
		    for(int i=0;i<run;i++) {
		    	
		    	String curr_path= run==10?"":pathname;
		    	if (run==10) {
		    		String tmp_string= i==9?"":"0";
		    		curr_path="vc_800_10000"+"_"+tmp_string+Integer.toString(i+1)+".txt";
		    		//System.out.println(curr_path);
		    	}
		    	sum+=(run_genetic_algorithm(numero_nodi, vertex_weight, matrix,curr_path)).get_fitness();
		    }
		    long endTime = System.currentTimeMillis();
		    tmp_+=endTime-startTime;
		}
		
		System.out.println("Media instances 10 run: "+dir_base+", "+ 1.0*sum/(pathnames.length*run)+", Tempo:"+(tmp_)/1000.0+", FE MEDIE:"+ media_it/10 + "\n");
		print_parameter_value();
	}

	

	public static double  genetic_algorithm_files(String dir_name, String dir_base) {
		
		File dir = new File(dir_name+File.separator+dir_base);
		String[] pathnames=dir.list();
		int sum=0;
		for (String pathname : pathnames) {
			
		    ResultReadFile result= read_file(dir_name+File.separator+dir_base+File.separator+pathname);
		    int numero_nodi=result.getN();
		    int[] vertex_weight=result.getM();
		    int[][] matrix=result.getMatrix();
		   
		    sum+=(run_genetic_algorithm(numero_nodi, vertex_weight, matrix,pathname)).get_fitness();
		}
		
		return 1.0*sum/pathnames.length;
	}
	
	
	public static Individual run_genetic_algorithm(int numero_nodi, int[] vertex_weight, int[][] matrix,String pathname) {
        
        try {

        	genetic_algorithm(numero_nodi,vertex_weight,matrix,pathname);
        	
		} 
        catch (TypeSelectionException e) {
        	System.out.println("Il type e invalido, deve essere tra questi (val a destra)");
        	for(TypeSelection env : TypeSelection.values()){
        	    System.out.println(env.name() + " : " + env.get_label());
        	}
        }
        catch (ArrayIndexOutOfBoundsException e) {
        	System.out.println(e);
        	System.out.println("di");
        }
        catch (Exception e) {
			
        	//System.out.println(e);
			System.out.println("FE LIMITED,   File:"+pathname+", "+min_solution_overall+", Trovata all'iterazione:"+min_solution_overall_epoch+", FE richieste:"+number_fe_to_find_min);
			
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
			System.out.println(" ");
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
	    			if (INCEST_PREVENTING) {
		    			if (popu.incest_preventing(a,b)) {
				    		ResultReadFile res_crossover=popu.crossover(a,b,m);
				    		
				    		Individual c=res_crossover.getC();
				    		Individual d=res_crossover.getD();
				    		
				    		c=popu.mutazione(c,m);
				    		d=popu.mutazione(d,m);
				    		
				    		if(!popu.duplicate_checking(c,new_generation)) {
				    			c=popu.repair_individual(c, m);
				    			new_generation.add(c);
				    		}
				    			
				    		if(!popu.duplicate_checking(d,new_generation)) {
				    			d=popu.repair_individual(d, m);
				    			new_generation.add(d);
				    		}
		    			}
	    			}
	    			else {
	    				ResultReadFile res_crossover=popu.crossover(a,b,m);
			    		
			    		Individual c=res_crossover.getC();
			    		Individual d=res_crossover.getD();
			    		
			    		c=popu.mutazione(c,m);
			    		d=popu.mutazione(d,m);
			    		c=popu.repair_individual(c, m);
		    			new_generation.add(c);
		    			d=popu.repair_individual(d, m);
		    			new_generation.add(d);
		    		}
	    			
	    		 }   
    		}
    		
    		//REPLACEMENT
    		popu.replacement(new_generation);
    		
    		double mean_population=popu.get_mean_population();
    		Individual min_epoch=popu.get_min_solution_epoch();
    		int diversity_measure=popu.get_diversity_population();
    		tmp_epoch_list.add(mean_population);
    		tmp_min_list.add(1.0*min_epoch.get_fitness());
    		tmp_div_list.add(1.0*diversity_measure+0.01);
    		
    		Individual tmp=min_solution_overall; //PREVIOUS MIN
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
    		//PROVIAMO AD USCIRE DAL MINIMO AUMENTANO LA PROB DI MUTAZIONE e la MUTATION_SIZE
    		if(tmp_last_min!=null) {
    			if(tmp_last_min.get_fitness()==min_epoch.get_fitness()) {
    				local_minima_counter++;
    			}
    			else {
    				
    				local_minima_counter=0;
    				GeneticAlgorithm.MUTATION_PROBA = GeneticAlgorithm.MUTATION_PROBA_START;
        			MUTATION_SIZE = GeneticAlgorithm.MUTATION_SIZE_START;
    				//GeneticAlgorithm.MUTATION_PROBA=0.1;
    				//MUTATION_SIZE = 5;
    			}
    		}
    		
    		if(local_minima_counter==GeneticAlgorithm.LOCAL_MINIMA_THRESHOLD) {
    			
    			//GeneticAlgorithm.MUTATION_PROBA=0.55;
    			//MUTATION_SIZE = 5;
    			GeneticAlgorithm.MUTATION_PROBA = GeneticAlgorithm.MUTATION_PROB_BOOST;
    			GeneticAlgorithm.MUTATION_SIZE  = GeneticAlgorithm.MUTATION_SIZE_BOOST;
    			popu.pertub_solution(popu,m);
    		}
    		
    		
    		//END LOGICA LOCAL MINIMA
    		
    		if (( gen % GEN_LOG==0)&& VERBOSE)print_log(gen,mean_population,min_epoch,min_solution_overall,diversity_measure);
    		
    		gen++;	
    		tmp_last_min=min_epoch;
    	}
    	
    	
    }
    
   
	private static void print_log(int gen, double mean_population, Individual min_epoch, Individual min_solution_overall,int diversity_measure) {
		System.out.println("Generazione:"+gen+", Media pop:"+df.format(mean_population)+", Minimo epoca:"+min_epoch+", Minimo overall:"+min_solution_overall+", MUTATION_PROBA:"+GeneticAlgorithm.MUTATION_PROBA+", Div measure:"+diversity_measure);	
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
       
			int index=0;
			for (String vertex_: vertex){ 
				if (!vertex_.equals("")){
					vertex_weight[index++]=Integer.parseInt(vertex_.trim().strip());
				 	
				}
			 }
			line = reader.readLine();
            int index_row=0;
			while (line != null) {
				String[] Adj=line.trim().split(" ");
				int index_col=0;
				for (String edge: Adj){ 
					if (!edge.equals("")){
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