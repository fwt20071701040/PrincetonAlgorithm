package Percolation;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private static final byte OPEN = 1;
    private static final byte CONNECT_THE_TOP = 2;
    private static final byte CONNECT_THE_BOTTOM = 4;
    private static final byte PERCOLATION = 7;
    private static final int FOUR_DIRECTIONS = 4;
    private final WeightedQuickUnionUF backwash;
    /* used to avoid backwash brought by the introduction of virtual sites,but this
     * trick may consumes more space
     */
    private final int n; // the number of rows or columns
    private byte[] flag;
    // used to decide whether the top and buttom sites are connected
    private boolean isPercolated;
    private int count;
    public Percolation(int n) {

        if (n <= 0) {

            throw new IllegalArgumentException();
        }
        this.n = n;
        flag = new byte[n * n];
        // here the matrix is n+1*n+1, other than n*n.The reason for this is making
        // the two dimensional array correspond to the pecolation model which is n*n 
        // and the rows&cols is range from 1 to n.
        backwash = new WeightedQuickUnionUF(n * n);
        // the backwash object should not have buttom site
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

                flag[idx] = (byte) (flag[idx] | CONNECT_THE_TOP);
            }
            if (row == n) {

                flag[idx] = (byte) (flag[idx] | CONNECT_THE_BOTTOM);
            }
            for (int i = 0; i < FOUR_DIRECTIONS; i++) {

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
            if (flag[rootidx] == PERCOLATION) {

                isPercolated = true;
            }
        }
    }  
    // open site (row, col) if it is not open already
    private boolean isPosValid(int pRow, int pCol) {

        return pRow >= 1 && pRow <= n && pCol >= 1 && pCol <= n;
    }
    public boolean isOpen(int row, int col) {

        isValidBounds(row, col);
        return (flag[xyTo1D(row, col)] & OPEN) == OPEN;
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
        return (flag[rootidx] & CONNECT_THE_TOP) == CONNECT_THE_TOP || 
                (flag[rootidx] & PERCOLATION) == PERCOLATION;
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
}


