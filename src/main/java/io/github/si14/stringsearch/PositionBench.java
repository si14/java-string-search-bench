package io.github.si14.stringsearch;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class PositionBench {
    @Param({"20"})
    int arraySize;

    @Param({"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19"})
    int toSearchIdx;

    String[] unsortedArray;
    String toSearchInterned;
    int foundIdx;

    @Setup
    public void prepare() {
        String[] allStrings = Misc.shortStrings;

        unsortedArray = new String[arraySize];
        System.arraycopy(allStrings, 0, unsortedArray, 0, arraySize);
        toSearchInterned = allStrings[toSearchIdx].intern();
        assert toSearchInterned == allStrings[toSearchIdx] : "string constants are interned";
    }

    @TearDown
    public void checkFound() {
        assert toSearchIdx == foundIdx : "didn't find the right element";
    }

    private int linearSearchInterned(String s) {
        for (int i = 0; i < unsortedArray.length; i++) {
            if (unsortedArray[i] == s) {
                return i;
            }
        }
        return -1;
    }

    @Benchmark
    public void benchInterned() {
        foundIdx = linearSearchInterned(toSearchInterned);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + PositionBench.class.getSimpleName() + ".*")
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
