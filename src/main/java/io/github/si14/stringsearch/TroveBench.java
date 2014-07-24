package io.github.si14.stringsearch;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class TroveBench {
    @Param({"1", "2", "3", "5", "8", "13", "21", "33", "54", "87", "99"})
    int arraySize;

    int toSearchIdx;
    String[] unsortedArray;
    String toSearch;
    String toSearchInterned;
    int foundIdx;
    TObjectIntMap<String> troveMap;

    @Setup
    public void prepare() {
        String[] allStrings = Misc.longStrings;

        unsortedArray = new String[arraySize];
        System.arraycopy(allStrings, 0, unsortedArray, 0, arraySize);
        troveMap = new TObjectIntHashMap<>(arraySize);
        for (int i = 0; i < unsortedArray.length; i++) {
            troveMap.put(unsortedArray[i], i);
        }
        toSearchIdx = arraySize / 2;
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

    private int troveSearch(String s) {
        return troveMap.get(s);
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
    public void benchTrove() {
        foundIdx = troveSearch(toSearchInterned);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + TroveBench.class.getSimpleName() + ".*")
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}