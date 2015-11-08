package org.nla.guava;


import static com.google.common.base.Predicates.notNull;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;
import java.util.Collection;
import org.junit.Test;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;

/**
 * http://code.google.com/p/guava-libraries/wiki/CollectionUtilitiesExplained
 * http://blog.solidcraft.eu/2010/10/googole-guava-v07-examples.html
 */
public class CollectionsTest {

    @Test
    public void testMultiMap() {
        Multimap<Integer, String> multimap = HashMultimap.create();
        multimap.put(1, "a");
        multimap.put(1, "b");
        multimap.put(1, "c");
        multimap.put(2, "c");
        multimap.put(3, "c");
        multimap.put(3, "x");

        Collection<String> str1 = multimap.get(1);
        assertThat(str1.size(), equalTo(3));
        assertTrue(str1.containsAll(Lists.newArrayList("a", "b", "c")));
    }
}