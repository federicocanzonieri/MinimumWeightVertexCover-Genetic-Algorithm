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
	
	
	public Population(int[][] matrix,int[] weight, int pop_size,int sol_size, boolean verbose,String type_selection) throws Exception {
		
		this.pop_size=pop_size;
		this.sol_size=sol_size;
		this.population= new ArrayList<>();
		this.matrix=matrix;
		this.verbose=verbose;
		this.weight=weight;
		this.type_selection=type_selection;
		
		
		for(int idx_individual=0;idx_individual<this.pop_size;idx_individual++) {
			this.population.add(new Individual(this.random_initialize(),weight,false));
		}
		
		this.population=this.repair_population(this.population,this.weight);
		
		
	}


	public List<Individual> repair_population(List<Individual> population2,int[] weight) throws Exception {
		for(Individual item : population2) {
			//item=repair_individual(item,weight);
			while (!item.is_valid_solution(this.matrix)) {
				ArrayList<Integer> index_list=item.get_index_non_inclusive();
				int index_change=(int) (GeneticAlgorithm.random_generator.nextDouble() * index_list.size());
				item.set_bit_solution(index_list.get(index_change),1);
			}
			item.recalc_fitness(this.weight);
			
		}
		return population2;
	}

	public Individual repair_individual(Individual individual,int[] weight) throws Exception {
		
		while (!individual.is_valid_solution(matrix)) {
			ArrayList<Integer> index_list=individual.get_index_non_inclusive();
			int index_change=(int) (GeneticAlgorithm.random_generator.nextDouble() * index_list.size());
			individual.set_bit_solution(index_list.get(index_change),1);
		}
		individual.recalc_fitness(this.weight);
		return individual;
	}
	
	
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
		/*
		if (this.type_selection.equals("elitism")){
			Collections.sort(new_generation, new ComparatorIndividual());
			List<Individual>best_new_generation=new_generation.subList(0, GeneticAlgorithm.ELITE_SELECTED);
			for(int i=0;i<GeneticAlgorithm.ELITE_SELECTED;i++) {
				int p=getRandomInt(this.pop_size);
				this.population.set(p,best_new_generation.get(i));
			}
		}*/
		if (this.type_selection.equals("mu_lambda")){
			
			//Collections.sort(new_generation, new ComparatorIndividual());
			//Collections.sort(this.population, new ComparatorIndividual());
		
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
				//System.out.println(new_generation.get(i)+" "+i+" "+this.population.size()+" "+new_generation.size());
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


	public double get_mean_population() {
		// TODO Auto-generated method stub
		int sum=0;
		for(Individual x : this.population) sum+=x.get_fitness();
		return 1.0*sum/this.population.size();
		
	}


	public Individual get_min_solution_epoch() {
		
		Individual min=this.population.get(0);
		for(Individual x : this.population) if (min.get_fitness()>x.get_fitness()){	min=x;}
		
		return min;
		
	}


	public int get_diversity_population() {
		// TODO Auto-generated method stub
		int diversity=0;
		for(int i=0;i<this.pop_size;i++) {
			for(int j=i+1;j<this.pop_size;j++) {
				diversity+=GeneticAlgorithm.hamming_distance(this.population.get(i), this.population.get(j));
			}
		}
		//System.out.println("Diversità:"+diversity);
		return diversity/(this.pop_size^2);
		
		
	}
	
	
	
	
}
