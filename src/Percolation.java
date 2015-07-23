/**
 * Created by guillermo on 6/24/15.
 */
public class Percolation {
    private int gridSize;
    private int dimensionLength;
    private boolean[] openSites;
    private boolean[] bottomConnected;
    private WeightedQuickUnionUF unionData;

    public Percolation(int N) {
        if (N < 1) {
            throw new IllegalArgumentException("N should be greater than 0");
        }
        setDimensions(N);
        createGrid(N);
    }

    private void setDimensions(int n) {
        dimensionLength = n + 1;  //grid includes one extra row and one extra column for virtual top and bottom
        gridSize = (dimensionLength) * (dimensionLength);
    }

    private void createGrid(int n) {
        unionData = new WeightedQuickUnionUF(gridSize); //we will use index 0 and N-1 as virtual top and bottom
        openSites = new boolean[gridSize]; //initialize all sites to false
        bottomConnected = new boolean[gridSize];
        createVirtualSites();
    }

    private void createVirtualSites() {
        openSites[0] = true;
        openSites[gridSize - 1] = true;
    }

    public void open(int i, int j) {
        validateInput(i, j);
        if (isOpen(i, j)) return;
        else openSite(i, j);
        boolean currentBottomStatus = getCurrentBottomStatus(i, j);
        connectToAdjacentSites(i, j);
        updateBottomStatus(i, j,currentBottomStatus);
    }

    private boolean getCurrentBottomStatus(int i, int j) {
        int siteIndex = rowColumnTo1D(i, j);
        int siteRoot = unionData.find(siteIndex);
        return bottomConnected[siteRoot];
    }

    private void updateBottomStatus(int i, int j, boolean currentBottomStatus) {
        int siteIndex = rowColumnTo1D(i, j);
        int siteRoot = unionData.find(siteIndex);
        if (j == dimensionLength - 1) {
            bottomConnected[siteRoot] = true;
        } else {
            bottomConnected[siteRoot] |= currentBottomStatus;
        }

    }

    private void openSite(int i, int j) {
        int siteIndex = rowColumnTo1D(i, j);
        openSites[siteIndex] = true;
    }

    private void connectToAdjacentSites(int i, int j) {
        int siteIndex = rowColumnTo1D(i, j);
        connectRight(i, j, siteIndex);
        connectLeft(i, j, siteIndex);
        connectUp(i, j, siteIndex);
        connectDown(i, j, siteIndex);
    }

    private void connectRight(int i, int j, int siteIndex) {
        if (j < dimensionLength - 1 && isOpen(i, j + 1))
            unionData.union(siteIndex, siteIndex + 1);
    }

    private void connectLeft(int i, int j, int siteIndex) {
        if (j > 1 && isOpen(i, j - 1))
            unionData.union(siteIndex, siteIndex - 1);
    }

    private void connectUp(int i, int j, int siteIndex) {
        if (i == 1) {
            unionData.union(siteIndex, 0);
        } else if (isOpen(i - 1, j))
            unionData.union(siteIndex, siteIndex - dimensionLength);
    }

    private void connectDown(int i, int j, int siteIndex) {
        int lastRow = dimensionLength - 1;
        if (i == lastRow)
            unionData.union(siteIndex, gridSize - 1);
        else if (isOpen(i+1, j))
            unionData.union(siteIndex, siteIndex + dimensionLength);
    }

    public boolean isOpen(int i, int j) {
        validateInput(i, j);
        int siteIndex = rowColumnTo1D(i, j);
        return openSites[siteIndex];
    }

    private void validateInput(int i, int j) {
        if (!indexIsValid(i, j))
            throw new IndexOutOfBoundsException("Index is out of bounds");
    }

    public boolean isFull(int i, int j) {
        validateInput(i, j);
        int topVirtualSiteIndex = 0;
        int siteIndex = rowColumnTo1D(i, j);
        return unionData.connected(siteIndex, topVirtualSiteIndex);
    }

    public boolean percolates() {
        return trueIfPercolates();
    }

    private boolean trueIfPercolates() {
        int topVirtualSiteIndex = 0;
        int bottomVirtualSiteIndex = gridSize - 1;
        return unionData.connected(bottomVirtualSiteIndex, topVirtualSiteIndex);
    }

    private int rowColumnTo1D(int row, int column) {
        return (row - 1) * (dimensionLength) + column;
    }

    private boolean indexIsValid(int row, int column) {
        if (row >= 1 && column >= 1 && row < dimensionLength && column < dimensionLength) {
            return true;
        }
        return false;
    }

    public static void main(String [] args) {
        int size = 20;
        Percolation grid = new Percolation(size);
        grid.open(1, 1);
        grid.open(1, 2);
        System.out.println(grid.unionData.connected(1, 2));
    }

}