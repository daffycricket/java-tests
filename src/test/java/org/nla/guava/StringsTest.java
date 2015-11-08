package org.nla.guava;

import java.util.Iterator;
import org.junit.Test;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * http://code.google.com/p/guava-libraries/wiki/StringsExplained
 */
public class StringsTest {

    @Test
    public void testJoinerWithSkipNulls() {
        Joiner joiner = Joiner.on("; ").skipNulls();
        String result = joiner.join("Harry", null, "Ron", "Hermione");

        assertThat(result, equalTo("Harry; Ron; Hermione"));
    }

    @Test
    public void testJoinerWithUseForNulls() {
        Joiner joiner = Joiner.on("; ").useForNull("NIL");
        String result = joiner.join("Harry", null, "Ron", "Hermione");

        assertThat(result, equalTo("Harry; NIL; Ron; Hermione"));
    }

    @Test
    public void testSplitter() {

        String toSplit = "foo,bar,,   qux";
        Iterator<String> iterator;

        iterator = Splitter.on(',').split(toSplit).iterator();
        assertThat(iterator.next(), equalTo("foo"));
        assertThat(iterator.next(), equalTo("bar"));
        assertThat(iterator.next(), equalTo(""));
        assertThat(iterator.next(), equalTo("   qux"));
        assertThat(iterator.hasNext(), equalTo(false));

        iterator = Splitter.on(',')
                .trimResults()
                .split(toSplit)
                .iterator();

        assertThat(iterator.next(), equalTo("foo"));
        assertThat(iterator.next(), equalTo("bar"));
        assertThat(iterator.next(), equalTo(""));
        assertThat(iterator.next(), equalTo("qux"));
        assertThat(iterator.hasNext(), equalTo(false));

        iterator = Splitter.on(',')
                .trimResults()
                .omitEmptyStrings()
                .split(toSplit)
                .iterator();

        assertThat(iterator.next(), equalTo("foo"));
        assertThat(iterator.next(), equalTo("bar"));
        assertThat(iterator.next(), equalTo("qux"));
        assertThat(iterator.hasNext(), equalTo(false));

        iterator = Splitter.on(',')
                .trimResults(CharMatcher.is('x'))
                .omitEmptyStrings()
                .split(toSplit)
                .iterator();

        assertThat(iterator.next(), equalTo("foo"));
        assertThat(iterator.next(), equalTo("bar"));
        assertThat(iterator.next(), equalTo("   qu"));
        assertThat(iterator.hasNext(), equalTo(false));

        iterator = Splitter.on(',')
                .limit(2)
                .split(toSplit)
                .iterator();

        assertThat(iterator.next(), equalTo("foo"));
        assertThat(iterator.next(), equalTo("bar,,   qux"));
        assertThat(iterator.hasNext(), equalTo(false));
    }

    @Test
    public void testCharMatcher() {
        String theDigits = CharMatcher.DIGIT.retainFrom("a45tt87"); // only the digits
        assertThat(theDigits, equalTo("4587"));

        String theLetters = CharMatcher.DIGIT.removeFrom("a45tt87"); // only the digits
        assertThat(theLetters, equalTo("att"));

        String theUps = CharMatcher.JAVA_UPPER_CASE.retainFrom("A45Tt87");
        assertThat(theUps, equalTo("AT"));

        String noDigits = CharMatcher.JAVA_DIGIT.replaceFrom("a45tt87", "*"); // star out all digits
        assertThat(noDigits, equalTo("a**tt**"));

        String noAtAll = CharMatcher.ANY.replaceFrom("a45tt87", "*");
        assertThat(noAtAll, equalTo("*******"));

        String singleWhitespaces = CharMatcher.WHITESPACE.collapseFrom("  too   too   you   tooo ", ' ');
        assertThat(singleWhitespaces, equalTo(" too too you tooo "));
    }
}