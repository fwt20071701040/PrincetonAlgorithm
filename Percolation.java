import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

	private final WeightedQuickUnionUF backwash;
	/*used to avoid backwash brought by the introduction of virtual sites,but this
	 * trick may consumes more space
	*/
	private int n;//the number of rows or columns
	private byte[] flag;
	//used to decide whether the top and buttom sites are connected
	private final byte OPEN = 1;
	private final byte CONNECTTHETOP = 2;
	private final byte CONNECTTHEBOTTOM = 4;
	private final byte PERCOLATION = 7;
	private boolean isPercolated;
	private int count;
	public Percolation(int n) {
		
		if (n <= 0) {
			
			throw new IllegalArgumentException();
		}
		this.n = n;
		flag = new byte[n * n];
		//here the matrix is n+1*n+1, other than n*n.The reason for this is making
		//the two dimensional array correspond to the pecolation model which is n*n 
		//and the rows&cols is range from 1 to n.
		backwash = new WeightedQuickUnionUF(n * n);
		//the backwash object should not have buttom site
		isPercolated = false;
	} 
	// create n-by-n grid, with all sites blocked
	private int xyTo1D(int row, int col) {
		
		return (row - 1) * n + col - 1;
	}
	public void open(int row, int col) {
		
		isValidBounds(row, col);
		if (!isOpen(row, col)) {
			
			count++;
			int[] dx = {-1, 1, 0, 0};
			int[] dy = {0, 0, -1, 1};
			int idx = xyTo1D(row, col);
			flag[idx] = OPEN;
			if (row == 1) {
				
				flag[idx] = (byte) (flag[idx] | CONNECTTHETOP);
			}
			if (row == n) {
				
				flag[idx] = (byte) (flag[idx] | CONNECTTHEBOTTOM);
			}
			for (int i = 0; i < 4; i++) {
				
				int posX = row + dx[i];
				int posY = col + dy[i];
				if (isPosValid(posX, posY) && isOpen(posX, posY)) {
					
					int rootidx = backwash.find(xyTo1D(posX, posY));
					flag[idx] = (byte) (flag[rootidx] | flag[idx]);
					backwash.union(xyTo1D(posX, posY), idx);
				}
			}
			int rootidx = backwash.find(xyTo1D(row, col));
			flag[rootidx] = (byte) (flag[idx] | flag[rootidx]);
			if (flag[rootidx] == 7) {
				
				isPercolated = true;
			}
			/*if (row - 1 >= 1 && isOpenFlag[row - 1][col]) {
				
				wqf.union(xyTo1D(row - 1, col), xyTo1D(row, col));
				backwash.union(xyTo1D(row - 1, col), xyTo1D(row, col));
			}
			if (row + 1 <= numPerRowOrCol && isOpenFlag[row + 1][col]) {
				
				wqf.union(xyTo1D(row + 1, col), xyTo1D(row, col));
				backwash.union(xyTo1D(row + 1, col), xyTo1D(row, col));
			}
			if (col - 1 >= 1 && isOpenFlag[row][col - 1]) {
				
				wqf.union(xyTo1D(row, col - 1), xyTo1D(row, col));
				backwash.union(xyTo1D(row, col - 1), xyTo1D(row, col));
			}
			if (col + 1 <= numPerRowOrCol && isOpenFlag[row][col + 1]) {
				
				wqf.union(xyTo1D(row, col + 1), xyTo1D(row, col));
				backwash.union(xyTo1D(row, col + 1), xyTo1D(row, col));
			}*/
		}
	}  
	// open site (row, col) if it is not open already
	private boolean isPosValid(int pRow, int pCol) {
		
		return pRow >= 1 && pRow <= n && pCol >= 1 && pCol <= n;
	}
	public boolean isOpen(int row, int col) {
		
		isValidBounds(row, col);
		return flag[xyTo1D(row, col)] != 0;
	}
	private void isValidBounds(int row, int col) {
        if (row < 1 || row > n)
            throw new IllegalArgumentException("Row index out of bounds");
        if (col < 1 || col > n)
            throw new IllegalArgumentException("column index out of bounds");
    }   

	// is site (row, col) open?
	public boolean isFull(int row, int col) {
		
		isValidBounds(row, col);
		int idx = xyTo1D(row, col);
		int rootidx = backwash.find(idx);
		return flag[rootidx] == 5 || flag[rootidx] == 7;
	}
	// is site (row, col) full?
	public int numberOfOpenSites() {
		
		return count;
	}  
	// number of open sites
	public boolean percolates() {
		
        return isPercolated;  
	} 
	// does the system percolate?
	public static void main(String[] args) {
		
		int trialsNum = 30;
		int[] numOfOpenSites = new int[trialsNum];
		for (int i = 0; i < trialsNum; i++) {
			
			Percolation perco = new Percolation(20);
			int count = 0;
			int[] rand = StdRandom.permutation(20 * 20);
			while (!perco.percolates()) {
				
				int row = rand[count] / 20 + 1;
				int col = rand[count] % 20 + 1;
				perco.open(row, col);
				count++;
			}
			numOfOpenSites[i] = perco.numberOfOpenSites();
		}
		for (int i = 0; i < trialsNum; i++) {
			
			System.out.println(numOfOpenSites[i]);
		}
	}
//	private class WeightedQuickUnionUF {
//		
//		private int[] parent;
//		private int[] size;
//		private int count;
//		private WeightedQuickUnionUF(int n) {
//			
//			count = n;
//			parent = new int[n];
//			size = new int[n];
//			for (int i = 0; i < n; i++) {
//				
//				parent[i] = i;
//				size[i] = 1;
//			}
//		}
//		
//		private void union(int p, int q) {
//			
//			int rootP = find(p);
//			int rootQ = find(q);
//			if (rootP == rootQ) {
//				
//				return;
//			}
//			if (size[rootP] > size[rootQ]) {
//				
//				parent[rootQ] = rootP;
//				size[rootP] += size[rootQ];
//			} else {
//				
//				parent[rootP] = rootQ;
//				size[rootQ] += size[rootP];
//			}
//			count--;
//		}
//		
//		private int find(int p) {
//			
//			validate(p);
//			while (parent[p] != p) {
//				
////				parent[p] = parent[parent[p]];
//				p = parent[p];
//			}
//			return p;
//		}
//		
//		private boolean connected(int p, int q) {
//			
//			return find(p) == find(q);
//		}
//		
//		private int count() {
//			
//			return count;
//		}
//		
//		private void validate(int p) {
//	        int n = parent.length;
//	        if (p < 0 || p >= n) {
//	            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n-1));  
//	        }
//	    }
////		private static void main(String[] args) {
////			
////			WeightedQuickUnionUF wqf = new WeightedQuickUnionUF(10);
////			if (!wqf.connected(1, 3)) {
////				
////				wqf.union(1, 3);
////			}
////			for (int i = 0; i < 5; i++) {
////				
////				StdOut.println(wqf.parent[i]);
////			}
////		}
//	}
}


