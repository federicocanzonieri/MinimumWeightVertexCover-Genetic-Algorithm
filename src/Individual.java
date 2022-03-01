import java.util.ArrayList;
import java.util.Arrays;


//CLASSE INDIVIDUAL
public class Individual {

	private int[] solution; 	 //ARRAY
	private int fitness;		 //FITNESS
	public static int NUMBER_FE; //NUMBER OF FE CURRENT
	
	public Individual(int [] solution,int[] weight,boolean calculate_fitness) throws Exception {
		this.solution=solution;
		if (calculate_fitness)this.fitness=this.fitness_score(weight);
	}
	public Individual() {}
	public int[] get_solution() {
		return this.solution;
	}
	public int get_solution_value(int idx) {
		return this.solution[idx];
	}
	public void set_bit_solution(int idx,int value) {
		this.solution[idx]=value;
	}
	public int get_solution_length() {

		return solution.length;
	}
	public String toString() {
		return "Fitness: "+this.fitness;//+", Solution:"+Arrays.toString(this.solution) ;
	}
	public int get_fitness() {
		return this.fitness;
	}
	
	//FITNESS SCORE
	public int fitness_score(int[] weight) throws Exception {

		if (NUMBER_FE==GeneticAlgorithm.MAX_NUMBER_FE) 	throw new Exception();
		NUMBER_FE+=1;
		int sum=0;
		for(int i=0;i<solution.length;i++) if(solution[i]==1) sum+=weight[i];	
		return sum;
	}
	//TESTING ALTRA FITNESS PROVATA
	public int testing_new_fitness(int[] weight,int[][] matrix) {
		int sum=0;
		for(int i=0;i<solution.length;i++) {
			if(solution[i]==1) {
				sum+=weight[i];
			}
			if(solution[i]==0) {
				sum+=single_edge_missing(matrix,i)*100;
			}
		}	
		return sum;
	}
	
	//TESTING
	public int bypass_fitness(int[] weight) {
		int sum=0;
		for(int i=0;i<solution.length;i++) {
			if(solution[i]==1) {
				sum+=weight[i];
			}
		}
			
		return sum;
	}
	
	//RICALCOLA FITNESS
	public void recalc_fitness(int[] weight) throws Exception {
		this.fitness=this.fitness_score(weight);
	}
	
	//CONTROLLO SE LA SOLUZIONE é VALIDA, CONTROLLA SOLO LE RIGHE E COLONNE DEGLI INDICI NON INCLUSI
	//SE TROVO UN ELEMENTO PARI A =1 NON é UNA VERTEX COVER ALTRIMENTI SI
	public boolean is_valid_solution(int[][] matrix) {
		ArrayList<Integer>non_inclusive_index=this.get_index_non_inclusive();
		for(Integer idx:non_inclusive_index) {
			for(int col=0;col<non_inclusive_index.size();col++) {
				if (idx!=non_inclusive_index.get(col)) {
					if(matrix[idx][non_inclusive_index.get(col)]==1) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	
	
	
	//TESTING ARCHI TOTALI
	public int sum_matrix(int[][] matrix) {
		 int sum=0;
		 for(int i=0;i<matrix.length;i++) {
			 for(int j=0;j<matrix.length;j++) {
				 sum+=matrix[i][j];
			 }
		 }
		 return sum;
	}
	//TESTING 
	public int sum_matrix_inclusive(int[][] matrix,ArrayList<Integer> index) {
		 int sum=0;
		 for (Integer idx: index) {
			 for(int j=0;j<matrix.length;j++) {
				 sum+=matrix[idx][j];
			 }
		 }
		 return sum;
	}
	//RESTITUISCE GLI INDICI DEGLI ARCHI INCLUSI
	public ArrayList<Integer> get_index_inclusive() {
		ArrayList<Integer> index=new ArrayList<Integer>();
		for(int i=0;i<this.solution.length;i++) {
			if(this.solution[i]==1) {
				index.add(i);
			}
		}
		return index;
	}
	//RESTITUISCCE IL NUMERO DI ARCHI RELATIVI AD UN DATO  INDICE
	public int single_edge_missing(int[][] matrix,int idx) {

		int sum=0;
		for(int index_col=0;index_col<matrix.length;index_col++) {
				sum+=matrix[idx][index_col];
		}
		return sum;
	}
	
	//COMPARE SORT
	public int compareTo(Individual other) {
        int fitness_other=((Individual)other).get_fitness();
        return this.fitness-fitness_other;
    }
	//RESTITUISCE GLI INDICI DEGLI ARCHI NON INCLUSI
	public ArrayList<Integer> get_index_non_inclusive() {
		ArrayList<Integer> index=new ArrayList<Integer>();
		for(int i=0;i<this.solution.length;i++) {
			if(this.solution[i]==0) {
				index.add(i);
			}
		}
		return index;
	}
}
