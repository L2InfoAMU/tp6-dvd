package model;

import java.util.*;


/**
 * {@link Grid} instances represent the grid in <i>The Game of Life</i>.
 */
public class Grid implements Iterable<Cell> {

    private final int numberOfRows;
    private final int numberOfColumns;
    private final Cell[][] cells;

    /**
     * Creates a new {@code Grid} instance given the number of rows and columns.
     *
     * @param numberOfRows    the number of rows
     * @param numberOfColumns the number of columns
     * @throws IllegalArgumentException if {@code numberOfRows} or {@code numberOfColumns} are
     *                                  less than or equal to 0
     */
    public Grid(int numberOfRows, int numberOfColumns) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.cells = createCells();
    }

    /**
     * Returns an iterator over the cells in this {@code Grid}.
     *
     * @return an iterator over the cells in this {@code Grid}
     */

    @Override
    public Iterator<Cell> iterator() {
        return new GridIterator(this);
    }

    private Cell[][] createCells() {
        Cell[][] cells = new Cell[getNumberOfRows()][getNumberOfColumns()];
        for (int rowIndex = 0; rowIndex < getNumberOfRows(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < getNumberOfColumns(); columnIndex++) {
                cells[rowIndex][columnIndex] = new Cell();
            }
        }
        return cells;
    }

    /**
     * Returns the {@link Cell} at the given index.
     *
     * <p>Note that the index is wrapped around so that a {@link Cell} is always returned.
     *
     * @param rowIndex    the row index of the {@link Cell}
     * @param columnIndex the column index of the {@link Cell}
     * @return the {@link Cell} at the given row and column index
     */
    public Cell getCell(int rowIndex, int columnIndex) {
        return cells[getWrappedRowIndex(rowIndex)][getWrappedColumnIndex(columnIndex)];
    }

    private int getWrappedRowIndex(int rowIndex) {
        return (rowIndex + getNumberOfRows()) % getNumberOfRows();
    }

    private int getWrappedColumnIndex(int columnIndex) {
        return (columnIndex + getNumberOfColumns()) % getNumberOfColumns();
    }

    /**
     * Returns the number of rows in this {@code Grid}.
     *
     * @return the number of rows in this {@code Grid}
     */
    public int getNumberOfRows() {
        return numberOfRows;
    }

    /**
     * Returns the number of columns in this {@code Grid}.
     *
     * @return the number of columns in this {@code Grid}
     */
    public int getNumberOfColumns() {
        return numberOfColumns;
    }


    private List<Cell> getNeighbours(int rowIndex, int columnIndex) {
        List<Cell> results = new ArrayList<>();

        for (int y = rowIndex -1; y <= rowIndex + 1; y++) {
            for (int x = columnIndex - 1; x <= columnIndex + 1; x++) {
                if (x == columnIndex && y == rowIndex)
                    continue;

                results.add(getCell(y, x));
            }
        }

        return results;
    }

    private int countAliveNeighbours(int rowIndex, int columnIndex) {
        List<Cell> neighbors = getNeighbours(rowIndex, columnIndex);
        int count = 0;

        for (Cell neighbor: neighbors)
            if (neighbor.isAlive()) {
                count++;
            }

        return count;

        // OR: return getAliveNeighbours(rowIndex, columnIndex).size();
    }

    private CellState calculateNextState(int rowIndex, int columnIndex) {
        int aliveNeighbours = countAliveNeighbours(rowIndex, columnIndex);
        Cell cell = getCell(rowIndex, columnIndex);

        if (cell.isAlive()) {
            if (aliveNeighbours == 2 || aliveNeighbours == 3)
                return cell.getState();

            return CellState.DEAD;
        }

        if (aliveNeighbours == 3)
            return createCellStateFromAliveNeighbors(rowIndex, columnIndex);

        return CellState.DEAD;
    }

    private List<Cell> getAliveNeighbours(int rowIndex, int columnIndex) {
        List<Cell> neighbors = getNeighbours(rowIndex, columnIndex);
        List<Cell> alive = new ArrayList<>();

        for (Cell neighbor : neighbors) {
            if (neighbor.isAlive())
                alive.add(neighbor);
        }

        return alive;
    }

    private CellState createCellStateFromAliveNeighbors(int rowIndex, int columnIndex) {
        List<Cell> alive = getAliveNeighbours(rowIndex, columnIndex);

        if (alive.size() != 3)
            throw new IllegalArgumentException("The number of alive neighbors is not 3.");

        return getPredominantObject(getCellStates(alive));
    }

    private static List<CellState> getCellStates(List<Cell> cells) {
        List<CellState> states = new ArrayList<>();

        for (Cell cell : cells)
            states.add(cell.getState());

        return states;
    }

    /**
     * @implNote Assumes that the given type has a working equals method.
     */
    private static <T> HashMap<T, Integer> countObjects(List<T> objects) {
        HashMap<T, Integer> count = new HashMap<>();

        for (T obj : objects) {
            int value = count.getOrDefault(obj, 0) + 1;
            count.put(obj, value);
        }

        return count;
    }

    private static <T> T getPredominantObject(List<T> objects) {
        if (objects.size() == 0)
            return null;

        HashMap<T, Integer> count = countObjects(objects);
        Map.Entry<T, Integer> max = null;

        for (Map.Entry<T, Integer> entry : count.entrySet()) {
            if (max == null) {
                max = entry;
                continue;
            }

            if (entry.getValue() > max.getValue())
                max = entry;
        }

        return max.getKey();
    }

    private CellState[][] calculateNextStates() {
        CellState[][] nextCellState = new CellState[getNumberOfRows()][getNumberOfColumns()];

        for (int y = 0; y < getNumberOfRows(); y++)
            for (int x = 0; x < getNumberOfColumns(); x++)
                nextCellState[y][x] = calculateNextState(y, x);

        return nextCellState;
    }

    private void updateStates(CellState[][] nextState) {
        CellState[][] nextStates = calculateNextStates();

        for (int y = 0; y < getNumberOfRows(); y++)
            for (int x = 0; x < getNumberOfColumns(); x++)
                cells[y][x].setState(nextStates[y][x]);
    }

    /**
     * Transitions all {@link Cell}s in this {@code Grid} to the next generation.
     *
     * <p>The following rules are applied:
     * <ul>
     * <li>Any live {@link Cell} with fewer than two live neighbours dies, i.e. underpopulation.</li>
     * <li>Any live {@link Cell} with two or three live neighbours lives on to the next
     * generation.</li>
     * <li>Any live {@link Cell} with more than three live neighbours dies, i.e. overpopulation.</li>
     * <li>Any dead {@link Cell} with exactly three live neighbours becomes a live cell, i.e.
     * reproduction.</li>
     * </ul>
     */
    void updateToNextGeneration() {
        updateStates(calculateNextStates());
    }

    /**
     * Sets all {@link Cell}s in this {@code Grid} as dead.
     */
    void clear() {
        for (int y = 0; y < getNumberOfRows(); y++)
            for (int x = 0; x < getNumberOfColumns(); x++)
                cells[y][x].setState(CellState.DEAD);
    }

    /**
     * Goes through each {@link Cell} in this {@code Grid} and randomly sets its state as ALIVE or DEAD.
     *
     * @param random {@link Random} instance used to decide if each {@link Cell} is ALIVE or DEAD.
     * @throws NullPointerException if {@code random} is {@code null}.
     */
    void randomGeneration(Random random) {
        for (int y = 0; y < getNumberOfRows(); y++)
            for (int x = 0; x < getNumberOfColumns(); x++) {
                boolean alive = random.nextBoolean();
                CellState state;

                if (!alive) {
                    state = CellState.DEAD;
                } else {
                    state = (random.nextBoolean()) ? CellState.ALIVE_BLUE : CellState.ALIVE_RED;
                }

                cells[y][x].setState(state);
            }
    }
}
