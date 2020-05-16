package com.gitlab.unchuckable.paleo;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Predicates {
    
    public static Predicate<Object> matchesString(String string) {
        return new StringMatchPredicate(string);
    }

    public static Predicate<Object> matchesRegex(String regex) {
        return new RegexMatchPredicate(regex);
    }

    private static class StringMatchPredicate implements Predicate<Object> {
        private final String match;

        public StringMatchPredicate(String match) {
            this.match = match;
        }

        @Override
        public boolean test(Object object) {
            return object != null && object.toString().equals(match);
        }

        @Override
        public int hashCode() {
            return match.hashCode();
        }

        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (object instanceof StringMatchPredicate) {
                return ((StringMatchPredicate)object).match.equals(match);
            }
            return false;
        }

        @Override
        public String toString() {
            return "matches string \"" + match + "\"";
        }
    }

 
    private static class RegexMatchPredicate implements Predicate<Object> {
        private final Pattern pattern;
        private final String regex;

        public RegexMatchPredicate(String regex) {
            this.regex = regex;
            this.pattern = Pattern.compile(regex);
        }

        @Override
        public boolean test(Object object) {
            return object != null && pattern.matcher(object.toString()).matches();
        }

        @Override
        public int hashCode() {
            return regex.hashCode();
        }

        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (object instanceof RegexMatchPredicate) {
                return ((RegexMatchPredicate)object).regex.equals(regex);
            }
            return false;
        }

        @Override
        public String toString() {
            return "matches regex \"" + regex + "\"";
        }
    }


}