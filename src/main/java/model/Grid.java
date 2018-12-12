package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


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


    // TODO: Écrire une version correcte de cette méthode.
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

    // TODO: Écrire une version correcte de cette méthode.
    private int countAliveNeighbours(int rowIndex, int columnIndex) {
        List<Cell> neighbors = getNeighbours(rowIndex, columnIndex);
        int count = 0;

        for (Cell neighbor: neighbors)
            if (neighbor.isAlive())
                count++;

        return count;
    }

    // TODO: Écrire une version correcte de cette méthode.
    private CellState calculateNextState(int rowIndex, int columnIndex) {
        int aliveNeighbours = countAliveNeighbours(rowIndex, columnIndex);
        int deadNeighbours = 8 - aliveNeighbours;
        Cell cell = getCell(rowIndex, columnIndex);

        if (cell.isAlive()) {
            if (aliveNeighbours == 2 || aliveNeighbours == 3)
                return CellState.ALIVE;

            return CellState.DEAD;
        }

        if (aliveNeighbours == 3)
            return CellState.ALIVE;

        return CellState.DEAD;
    }

    // TODO: Écrire une version correcte de cette méthode.
    private CellState[][] calculateNextStates() {
        CellState[][] nextCellState = new CellState[getNumberOfRows()][getNumberOfColumns()];

        for (int y = 0; y < getNumberOfRows(); y++)
            for (int x = 0; x < getNumberOfColumns(); x++)
                nextCellState[y][x] = calculateNextState(y, x);

        return nextCellState;
    }

    // TODO: Écrire une version correcte de cette méthode.
    private void updateStates(CellState[][] nextState) {

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
    // TODO: Écrire une version correcte de cette méthode.
    void updateToNextGeneration() {

    }

    /**
     * Sets all {@link Cell}s in this {@code Grid} as dead.
     */
    // TODO: Écrire une version correcte de cette méthode.
    void clear() {

    }

    /**
     * Goes through each {@link Cell} in this {@code Grid} and randomly sets its state as ALIVE or DEAD.
     *
     * @param random {@link Random} instance used to decide if each {@link Cell} is ALIVE or DEAD.
     * @throws NullPointerException if {@code random} is {@code null}.
     */
    // TODO: Écrire une version correcte de cette méthode.
    void randomGeneration(Random random) {

    }
}
