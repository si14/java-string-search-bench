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
public class InternedBench {
    @Param({"1", "2", "3", "5", "8", "13", "21", "33", "54", "87"})
    int arraySize;

    int toSearchIdx;
    String[] unsortedArray;
    String toSearch;
    String toSearchInterned;
    int foundIdx;

    @Setup
    public void prepare() {
        String[] allStrings = Misc.longStrings;

        unsortedArray = new String[arraySize];
        System.arraycopy(allStrings, 0, unsortedArray, 0, arraySize);
        toSearchIdx = arraySize / 2;
        toSearch = allStrings[toSearchIdx].substring(0); // make sure this isn't interned
        toSearchInterned = toSearch.intern();
        assert toSearch != toSearchInterned : "interned string should differ";
        assert toSearchInterned == allStrings[toSearchIdx] : "string constants are interned";
    }

    @TearDown
    public void checkFound() {
        assert toSearchIdx == foundIdx : "didn't find the right element";
    }

    private int linearSearchUninterned(String s) {
        for (int i = 0; i < unsortedArray.length; i++) {
            if (unsortedArray[i].equals(s)) {
                return i;
            }
        }
        return -1;
    }

    private int linearSearchInterned(String s) {
        for (int i = 0; i < unsortedArray.length; i++) {
            if (unsortedArray[i] == s) {
                return i;
            }
        }
        return -1;
    }

    private int linearSearchInternedEquals(String s) {
        for (int i = 0; i < unsortedArray.length; i++) {
            if (unsortedArray[i].equals(s)) {
                return i;
            }
        }
        return -1;
    }

//    @Benchmark
//    public void benchBaseline() {
//        // blank method
//    }

    @Benchmark
    public void benchUninterned() {
        foundIdx = linearSearchUninterned(toSearch);
    }

    @Benchmark
    public void benchInterned() {
        foundIdx = linearSearchInterned(toSearchInterned);
    }

    @Benchmark
    public void benchInternedEquals() {
        foundIdx = linearSearchInternedEquals(toSearchInterned);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + InternedBench.class.getSimpleName() + ".*")
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
