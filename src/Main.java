public class Main {

    public static void main(String[] args) {
        int[][] costs = {
                {2, 3, 0, 4},
                {0, 6, 5, 2},
                {8, 0, 3, 7},
                {2, 0, 4, 2}
        };

        System.out.println("Best cost in the maze is: " +
                minCostPath(0, 0, costs.length - 1, costs[0].length - 1, costs));
        System.out.println("Best cost in the maze is: " +
                minCostPathMemoized(0, 0, costs.length - 1, costs[0].length - 1,
                        costs, new int[costs.length][costs[0].length]));
        System.out.println("Recalculations saved: " + counter);

        System.out.println("Best cost in maze is: " +
                minCostPathTabulated(costs));
    }

    public static int minCostPath(int srcX, int srcY, int destX, int destY, int[][] costs) {
        if (srcX == destX && srcY == destY) {
            return costs[srcX][srcY];    // this is the cost of the last cell
        }

        //
        int minCostFromSrcToDest = 0;
        int minCostInterToDestHoriz = Integer.MAX_VALUE;
        int minCostInterToDestVert = Integer.MAX_VALUE;

        if (srcY < destY) {
            minCostInterToDestHoriz = minCostPath(srcX, srcY + 1, destX, destY, costs);
        }

        if (srcX < destX) {
            minCostInterToDestVert = minCostPath(srcX + 1, srcY, destX, destY, costs);
        }
        // cost of current cell + min cost of horizontal and vertical calls
        minCostFromSrcToDest = costs[srcX][srcY] + Math.min(minCostInterToDestHoriz, minCostInterToDestVert);

        return minCostFromSrcToDest;
    }

    static int counter = 0;

    public static int minCostPathMemoized(int srcX, int srcY, int destX, int destY, int[][] costs, int[][] memArray) {
        if (srcX == destX && srcY == destY) {
            return costs[srcX][srcY];    // this is the cost of the last cell
        }

        if (memArray[srcX][srcY] != 0) {
            counter++;
            return memArray[srcX][srcY];
        }

        int minCostFromSrcToDest = 0;
        int minCostInterToDestHoriz = Integer.MAX_VALUE;
        int minCostInterToDestVert = Integer.MAX_VALUE;

        if (srcY < destY) {
            minCostInterToDestHoriz = minCostPathMemoized(srcX, srcY + 1, destX, destY, costs, memArray);
        }

        if (srcX < destX) {
            minCostInterToDestVert = minCostPathMemoized(srcX + 1, srcY, destX, destY, costs, memArray);
        }

        minCostFromSrcToDest = costs[srcX][srcY] + Math.min(minCostInterToDestHoriz, minCostInterToDestVert);

        memArray[srcX][srcY] = minCostFromSrcToDest;
        return minCostFromSrcToDest;
    }

    public static int minCostPathTabulated(int[][] costs) {
        int dRow = costs.length - 1;
        int dCol = costs[0].length - 1;

        int[][] f = new int[costs.length][costs[0].length];
//        String[][] path = new String[costs.length][costs[0].length];
        //
        for (int r = dRow; r >= 0; r--) {
            for (int c = dCol; c >= 0; c--) {
                int rp1 = r + 1;
                int cp1 = c + 1;

                if (r == dRow && c == dCol) { // last cell
                    f[r][c] = costs[r][c];
//                    path[r][c] = ".";
                } else if (r == dRow) { // when in the last row, no vertical motion
                    f[r][c] = costs[r][c] + f[r][cp1];
//                    path[r][r] = "H" + path[r][cp1];
                } else if (c == dCol) { // when in the last column, no horizontal motion
                    f[r][c] = costs[r][c] + f[rp1][c];
//                    path[r][c] = "V" + path[rp1][c];
                } else { // in all other cells, add cost to the minimum of either motions
                    if (f[rp1][c] < f[r][cp1]) {
                        f[r][c] = costs[r][c] + f[rp1][c];
//                        path[r][c] = "V" + path[rp1][c];
                    } else {
                        f[r][c] = costs[r][c] + f[r][cp1];
//                        path[r][r] = "H" + path[r][cp1];
                    }
                }
            }
        }
//        System.out.println(path[0][0]);

        return f[0][0];
    }
}
