import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Population {

	List<Individual> population;
	private int pop_size;
	private int[][] matrix;
	private boolean verbose;
	private int sol_size;
	private int[] weight;
	private String type_selection;
	
	//CONSTRUCTOR
	public Population(int[][] matrix,int[] weight, int pop_size,int sol_size, boolean verbose,String type_selection) throws Exception {
		
		this.pop_size=pop_size;
		this.sol_size=sol_size;
		this.population= new ArrayList<>();
		this.matrix=matrix;
		this.verbose=verbose;
		this.weight=weight;
		this.type_selection=type_selection;
		
		//INITIALIZE POPULATION
		for(int idx_individual=0;idx_individual<this.pop_size;idx_individual++) {
			this.population.add(new Individual(this.random_initialize(),weight,false));
		}
		//REPAIR INITAL SOLUTION
		this.population=this.repair_population(this.population,this.weight);
	
	}

	//REPAIR POPULATION FLIPPO DEI BIT A CASO FINO A QUANDO NON e UNA SOLUZIONE VALIDA
	public List<Individual> repair_population(List<Individual> population2,int[] weight) throws Exception {
		for(Individual item : population2) {
			repair_individual(item,weight);
		}
		return population2;
	}
	
	//SINGLE REPAIR
	public Individual repair_individual(Individual individual,int[] weight) throws Exception {
		
		while (!individual.is_valid_solution(matrix)) {
			ArrayList<Integer> index_list=individual.get_index_non_inclusive();
			int index_change=(int) (GeneticAlgorithm.random_generator.nextDouble() * index_list.size());
			individual.set_bit_solution(index_list.get(index_change),1);
		}
		individual.recalc_fitness(this.weight);
		return individual;
	}
	
	
	//UTILITY FOR SELECTION
	public double[] vector_probabilities(List<Individual> population2) {
		double tmp[]=new double[this.population.size()];
		int sum_fitness=this.calculate_sum_fitness(population2);
		int index_tmp=0;
		for (Individual tmp_indi: population2) 	tmp[index_tmp++]=1.0*(sum_fitness-tmp_indi.get_fitness())/sum_fitness;
		
		double sum=0;
		for(int i=0;i<tmp.length;i++) sum+=tmp[i];
		for(int i=0;i<tmp.length;i++) tmp[i]/=sum;
		return tmp;
	}
	public int get_population_size() {
		return this.pop_size;
	}
	private int calculate_sum_fitness(List<Individual> population2) {
		
		int sum=0;
		for(Individual tmp: population2)sum+=tmp.get_fitness();
		return sum;
	}

	//RANDOM UTILITY INITIAL
	public int[] random_initialize() {
		int[] tmp=new int[this.sol_size];
		for(int i=0;i<this.sol_size;i++) tmp[i]=this.getRandomInt(2);
		return tmp;
		
	}
	private int getRandomInt(int max) {
		  return (int)(Math.floor((max*GeneticAlgorithm.random_generator.nextDouble())));
	}

	public String toString() {	
		for(int i=0;i<this.pop_size;i++) System.out.println(this.population.get(i));
		return "";
	}
	public List<Individual> get_population() {
		return this.population;
		
	}
	public Individual get_individual(int indx) {
		return this.population.get(indx);		
	}
	public int[] vector_index() {
		int[] tmp=new int[this.pop_size];
		for(int i=0;i<this.pop_size;i++) {
			tmp[i]=i;
		}
		return tmp;
		
	}
	public double[] distribution_proba() {
		double[] proba=this.vector_probabilities(this.population);
		double[] dist=new double[proba.length];
		dist[0]=proba[0];
		for(int i=1;i<proba.length;i++) {
			dist[i]=dist[i-1]+proba[i];
		}
		return dist;
	}
	public int choice_prop_fitness() {
		double prob=GeneticAlgorithm.random_generator.nextDouble();
		double[] dist=this.distribution_proba();
		for(int i=0;i<dist.length;i++) {
			if (prob<=dist[i]) {
				return i;
			}
		}
		return dist.length-2;
	}
	public void replacement(ArrayList<Individual> new_generation) throws Exception {
		if (this.type_selection.equals("mu_lambda")){
			
			this.population.addAll(new_generation);
			Collections.sort(this.population, new ComparatorIndividual());
			this.population=this.population.subList(0, pop_size);
		}
		else if (this.type_selection.equals("general_selection")) {
			/*
			this.population.addAll(new_generation);
			Collections.sort(this.population, new ComparatorIndividual());
			this.population=this.population.subList(0, this.pop_size);
			System.out.println(this.population);
			new Exception();
			*/
			int limit= new_generation.size()<this.population.size()?new_generation.size():this.population.size();
			for(int i=0;i<limit;i++) {
				this.population.set(i, new_generation.get(i));
			}
		}
		
		else if (this.type_selection.equals("prop_size")) {
			Collections.sort(new_generation, new ComparatorIndividual());
			Collections.sort(this.population, new ComparatorIndividual());
			int size=GeneticAlgorithm.PROB_POPULATION*new_generation.size()/100;
			System.out.println(size);
			System.out.println(new_generation.size());
			
			List<Individual> new_prop=new_generation.subList(0, GeneticAlgorithm.PROB_POPULATION);
			List<Individual> pop_prop=new_generation.subList(0, this.pop_size- GeneticAlgorithm.PROB_POPULATION);

			pop_prop.addAll(new_prop);
			this.population=pop_prop.subList(0, this.population.size());
		}
		else {
			throw new TypeSelectionException();
		}
	}
	
	
	//OPERATORE CROSSOVER
	public  ResultReadFile crossover(Individual a,Individual b,int[] weight) throws Exception {
    	int[] cc=new int[a.get_solution_length()];
    	int[] dd=new int[a.get_solution_length()];
    	for(int i=0;i<a.get_solution_length();i++) {
    		double p=GeneticAlgorithm.random_generator.nextDouble();
    		cc[i]=p<0.5?a.get_solution()[i]:b.get_solution()[i];
    		dd[i]=p<0.5?b.get_solution()[i]:a.get_solution()[i];
    	}
    	
    	Individual c=new Individual(cc,weight,false);
    	Individual d=new Individual(dd,weight,false);
    	return new ResultReadFile(c,d);
    }
	//OPERATORE MUTAZIONE
	public Individual mutazione(Individual c,int[] weight) throws Exception {
    	for(int i=0;i<GeneticAlgorithm.MUTATION_SIZE;i++) {
    		//double p=random_generator.nextDouble();
	    	if (GeneticAlgorithm.random_generator.nextDouble()<=GeneticAlgorithm.MUTATION_PROBA) {
	    		int index=(int)(Math.floor((c.get_solution_length()*GeneticAlgorithm.random_generator.nextDouble())));
	    		c.set_bit_solution(index, 1-(c.get_solution_value(index)));
	    	}
    	}
    	return c;
	}
	//OPERATORE DUPLICATE CHECKING
	boolean duplicate_checking(Individual c, ArrayList<Individual> new_generation) {
		// TODO Auto-generated method stub
    	int threshold_tolerance=GeneticAlgorithm.THRESHOLD_INCEST_PREVENTING;
    	for(int i=0;i<new_generation.size();i++) {
    		if (this.hamming_distance(c,new_generation.get(i))<=threshold_tolerance) {
    			return true;
    			
    		}
    	}
		return false;
	}
	boolean incest_preventing(Individual a, Individual b) {
    	return hamming_distance(a,b)>GeneticAlgorithm.THRESHOLD_INCEST_PREVENTING;
	}
	public  int hamming_distance(Individual a, Individual b) {
		int sum=0;
		for(int i=0;i<a.get_solution_length();i++) {
			sum+=a.get_solution_value(i)!=b.get_solution_value(i)?1:0;
		}
		return sum;
	}
	//OPERATORE PERTURB
	void pertub_solution(Population population,int[] weight) throws Exception {
		//Prende le soluzioni migliori e le toglie, poca diversita
    	Collections.sort(population.get_population(), new ComparatorIndividual());
    	for (int i=0;i<GeneticAlgorithm.PERTURB_SOLUTION;i++) {
    		Individual tmp=new Individual(population.random_initialize(),null,false);
    		tmp=population.repair_individual(tmp, weight);
    		population.get_population().set(i, tmp); 	
    	}
			
	}
	//MEDIA FITNESS POPOLAZIONE
	public double get_mean_population() {
		int sum=0;
		for(Individual x : this.population) sum+=x.get_fitness();
		return 1.0*sum/this.population.size();
	}
	//MINIMO POPOLAZIONE
	public Individual get_min_solution_epoch() {
		Individual min=this.population.get(0);
		for(Individual x : this.population) if (min.get_fitness()>x.get_fitness()){	min=x;}
		return min;
	}
	//CaLCOLA LA DIVERSITa DELLA POPOLAZIONE
	public int get_diversity_population() {
		int diversity=0;
		for(int i=0;i<this.pop_size;i++) {
			for(int j=i+1;j<this.pop_size;j++) {
				diversity+=this.hamming_distance(this.population.get(i), this.population.get(j));
			}
		}
		return diversity/(this.pop_size^2);
	}
}
