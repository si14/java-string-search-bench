package io.github.si14.stringsearch;

import com.google.common.collect.ComparisonChain;

import java.util.stream.IntStream;

public class Misc {
    static public String[] longStrings = new String[] {
            "MARY", "PATRICIA", "LINDA", "BARBARA", "ELIZABETH", "JENNIFER",
            "MARIA", "SUSAN", "MARGARET","DOROTHY", "LISA", "NANCY", "KAREN",
            "BETTY", "HELEN", "SANDRA", "DONNA", "CAROL", "RUTH", "SHARON",
            "MICHELLE", "LAURA", "SARAH", "KIMBERLY", "DEBORAH", "JESSICA",
            "SHIRLEY", "CYNTHIA", "ANGELA", "MELISSA", "BRENDA", "AMY", "ANNA",
            "REBECA","VIRGINIA", "KATHLEEN", "PAMELA", "MARTHA","DEBRA","AMANDA",
            "STEPHANIE", "CAROLYN", "CHRISTINE", "MARIE", "JANET", "CATHERINE",
            "FRANCES", "ANN", "JOYCE", "DIANE","ALICE","JULIE","HEATHER","TERESA",
            "DORIS", "GLORIA", "EVELYN", "JEAN", "CHERYL", "MILDRED", "KATHERINE",
            "JOAN", "ASHLEY", "JUDITH", "ROSE", "JANICE","KELLY","NICOLE", "JUDY",
            "CHRISTINA", "KATHY", "THERESA", "BEVERLY", "DENISE", "TAMMY", "IRENE",
            "JANE", "LORI", "RACHEL", "MARILYN", "ANDREA", "KATHRYN", "LOUISE",
            "SARA", "ANNE", "JACQUELINE","WANDA", "BONNIE", "JULIA", "RUBY", "LOIS",
            "TINA", "PHYLLIS", "NORMA","PAULA","DIANA","ANNIE", "LILLIAN", "EMILY",
            "ROBIN"};

    static public String[] shortStrings = new String[] {
            "t", "s", "p", "b", "e", "w", "r", "y", "z", "x", "q", "o", "k", "c", "a",
            "v", "f", "j", "n", "u", "m", "i", "h", "l", "g", "d", "tt", "ts", "tp", "tb",
            "te", "tw", "tr", "ty", "tz", "tx", "tq", "to", "tk", "tc", "ta", "tv", "tf",
            "tj", "tn", "tu", "tm", "ti", "th", "tl", "tg", "td", "st", "ss", "sp", "sb",
            "se", "sw", "sr", "sy", "sz", "sx", "sq", "so", "sk", "sc", "sa", "sv", "sf",
            "sj", "sn", "su", "sm", "si", "sh", "sl", "sg", "sd", "pt", "ps", "pp", "pb",
            "pe", "pw", "pr", "py", "pz", "px", "pq", "po", "pk", "pc", "pa", "pv", "pf",
            "pj", "pn", "pu", "pm", "pi"
    };

    static final class StringIntPair implements Comparable<StringIntPair> {
        final String first;
        final int second;

        StringIntPair(final String first, final int second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public int compareTo(final StringIntPair other) {
            return ComparisonChain.start()
                    .compare(first, other.first)
                    .compare(second, other.second)
                    .result();
        }
    }

    public static void reorder(final String[] values, final int[] indices) {
        for (int i = 0; i < values.length; i++) {
            final int j = indices[i];
            final String tmp = values[i];
            values[i] = values[j];
            values[j] = tmp;
        }
    }

    public static int[] sorted(final String[] values) {
        return IntStream.range(0, values.length)
                .mapToObj(i -> new StringIntPair(values[i], i))
                .parallel().sorted()
                .mapToInt(p -> p.second)
                .toArray();
    }

}
