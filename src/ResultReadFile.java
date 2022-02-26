
public class ResultReadFile {

	private int n;
	private int[][] matrix;
	private int[] m;
	private Individual c;
	private Individual d;

	public  ResultReadFile(int n,int[] m,int[][] matrix){
		this.n=n;
		this.m=m;
		this.matrix=matrix;
	}
	public ResultReadFile(Individual c,Individual d) {
		this.c=c;
		this.d=d;
	}
	public Individual getC() {
		return this.c;
	}
	public Individual getD() {
		return this.d;
	}
	public int getN() {
		return this.n;
	}

	public int[] getM() {
		return this.m;
	}
	
	public int[][] getMatrix() {
		return this.matrix;
	}
	
	
}
