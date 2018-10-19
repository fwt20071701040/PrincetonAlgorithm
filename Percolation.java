import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

	private byte[] flag;
	private final WeightedQuickUnionUF wqf;
	//used to decide whether the top and buttom sites are connected
	private final WeightedQuickUnionUF backwash;
	/*used to avoid backwash brought by the introduction of virtual sites,but this
	 * trick may consumes more space
	*/
	private final int num;//the number of rows or columns
	private final int virtualTop;//the virtual top site
	private final int virtualBottom;//the virtual buttom site
	private boolean isPercolated;
	private int count;
	public Percolation(int n) {
		
		if (n <= 0) {
			
			throw new IllegalArgumentException();
		}
		num = n;
		flag = new byte[n * n];
		//here the matrix is n+1*n+1, other than n*n.The reason for this is making
		//the two dimensional array correspond to the pecolation model which is n*n 
		//and the rows&cols is range from 1 to n.
		wqf = new WeightedQuickUnionUF(n * n + 2);
		backwash = new WeightedQuickUnionUF(n * n + 1);
		//the backwash object should not have buttom site
		virtualTop = n * n;
		virtualBottom = n * n + 1;
		isPercolated = false;
		/*for (int i = 1; i < n + 1; i++) {
			
			for (int j = 1; j < n + 1; j++) {
				
				isOpenFlag[i][j] = false;
			}
		}boolean array is initialed to false
		int rowsOrCols = isOpenFlag.length - 1;
		for (int i = 0; i < rowsOrCols; i++) {
			
			wqf.union(n * n, i);
		}
		for (int i = rowsOrCols * rowsOrCols - 1; i > rowsOrCols * rowsOrCols - rowsOrCols - 1; i--) {
			
			wqf.union(n * n + 1, i);
		}*/
	} 
	// create n-by-n grid, with all sites blocked
	private int xyTo1D(int row, int col) {
		
		return (row - 1) * num + col - 1;
	}
	public void open(int row, int col) {
		
		isValidBounds(row, col);
		if (!isOpen(row, col)) {
			count++;
			int[] dx = {-1, 1, 0, 0};
			int[] dy = {0, 0, -1, 1};
			flag[xyTo1D(row, col)] = 1;
			if (row == 1) {
				
				wqf.union(virtualTop, xyTo1D(row, col));
				backwash.union(virtualTop, xyTo1D(row, col));
			}
			if (row == num) {
				
				wqf.union(virtualBottom, xyTo1D(row, col));
			}
			for (int i = 0; i < 4; i++) {
				
				int posX = row + dx[i];
				int posY = col + dy[i];
				if (isPosValid(posX, posY) && isOpen(posX, posY)) {
					
					wqf.union(xyTo1D(row, col), xyTo1D(posX, posY));
					backwash.union(xyTo1D(row, col), xyTo1D(posX, posY));
				}
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
		
		return pRow >= 1 && pRow <= num && pCol >= 1 && pCol <= num;
	}
	public boolean isOpen(int row, int col) {
		
		isValidBounds(row, col);
		return flag[xyTo1D(row, col)] == 1 || flag[xyTo1D(row, col)] == 2;
	}
	private void isValidBounds(int row, int col) {
        if (row < 1 || row > num)
            throw new IllegalArgumentException("Row index out of bounds");
        if (col < 1 || col > num)
            throw new IllegalArgumentException("column index out of bounds");
    }   

	// is site (row, col) open?
	public boolean isFull(int row, int col) {
		
		isValidBounds(row, col);
		return backwash.connected(virtualTop, xyTo1D(row, col));
	}
	// is site (row, col) full?
	public int numberOfOpenSites() {
		
		return count;
	}  
	// number of open sites
	public boolean percolates() {
		
		if (isPercolated) 
            return true;
        if (wqf.connected(virtualTop, virtualBottom)) {
        	
            isPercolated = true;
            return true;
        }
        return false;  
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
//				parent[p] = parent[parent[p]];
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


