package com.tsystems.javaschool.tasks.pyramid;

import java.util.List;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {
        if(inputNumbers.contains(null)){
            throw new CannotBuildPyramidException();
        }

        int[][] result = getFilledArray(inputNumbers.size());
        int counter = 0;

        inputNumbers.sort(Integer::compareTo);

        for(int i = 0; i < result.length; i++) {
            for (int j = 0; j <= i ; j++) {
                result[i][result[0].length/2-i+j*2] = inputNumbers.get(counter++);
            }
        }

        return result;
    }

    /**
     * @return array filled with zeroes.
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input length
     */
    private int[][] getFilledArray(int length){
        int x = 0;
        long z = 0;

        while (z < length) {
            z += ++x;
        }

        if (z > length) {
            throw new CannotBuildPyramidException();
        } else {
            return new int[x][x * 2 - 1];
        }
    }
}
