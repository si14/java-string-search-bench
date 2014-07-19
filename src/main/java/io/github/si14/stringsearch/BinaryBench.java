package io.github.si14.stringsearch;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class BinaryBench {
//    @Param({"1", "2", "3", "5", "8", "13", "21", "33", "54", "87"})
    @Param({"87", "99"})
    int arraySize;

    int toSearchIdx;
    String[] unsortedArray;
    String[] sortedArray;
    int[] indices;
    String toSearchInterned;
    int foundIdx;

    @Setup
    public void prepare() {
        String[] allStrings = Misc.longStrings;

        unsortedArray = new String[arraySize];
        System.arraycopy(allStrings, 0, unsortedArray, 0, arraySize);
        toSearchIdx = arraySize / 2;
        toSearchInterned = allStrings[toSearchIdx].intern();
        assert toSearchInterned == allStrings[toSearchIdx] : "string constants are interned";

        sortedArray = new String[arraySize];
        System.arraycopy(allStrings, 0, sortedArray, 0, arraySize);
        indices = Misc.sorted(sortedArray);
        Misc.reorder(sortedArray, indices);
    }

    @TearDown
    public void checkFound() {
        assert toSearchIdx == foundIdx || foundIdx == -424242 : "didn't find the right element";
    }

    private int linearSearchInterned(String s) {
        for (int i = 0; i < unsortedArray.length; i++) {
            if (unsortedArray[i] == s) {
                return i;
            }
        }
        return -1;
    }

    private int binarySearch(String s) {
        final int idx = Arrays.binarySearch(sortedArray, toSearchInterned);
        if (idx >= 0) {
            return indices[idx];
        } else {
            return -1;
        }
    }

    private int binarySearchRaw(String s) {
        return Arrays.binarySearch(sortedArray, toSearchInterned);
    }

//    @Benchmark
//    public void benchBaseline() {
//        // blank method
//    }


    @Benchmark
    public void benchInterned() {
        foundIdx = linearSearchInterned(toSearchInterned);
    }

    @Benchmark
    public void benchBinary() {
        foundIdx = binarySearch(toSearchInterned);
    }

    @Benchmark
    public int benchBinaryRaw() {
        foundIdx = -424242;
        return binarySearchRaw(toSearchInterned);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + BinaryBench.class.getSimpleName() + ".*")
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}

