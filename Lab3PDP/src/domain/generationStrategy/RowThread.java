package domain.generationStrategy;

import domain.models.Matrix;

import java.util.AbstractMap;

public class RowThread extends MatrixThread {

    public RowThread(int startingRow, int startingColumn, int numberOfElements, Matrix matrixA, Matrix matrixB, Matrix matrixC) {
        super(startingRow, startingColumn, numberOfElements, matrixA, matrixB, matrixC);
    }

    public void populateElements() {
        int i = startingRow, j = startingColumn;
        int count = numberOfElements;
        while (count > 0 && i < matrixC.rows && j < matrixC.columns) {
            elements.add(new AbstractMap.SimpleEntry<>(i, j));
            j++;
            count--;
            if (j == matrixC.rows) {
                j = 0;
                i++;
            }
        }
    }
}