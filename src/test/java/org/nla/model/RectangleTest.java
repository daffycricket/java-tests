package org.nla.model;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;

public class RectangleTest {

    @Test(expected = RuntimeException.class)
    public void ensureItsImpossibleToCreateARectangleWithNegativeLength() {
        new Rectangle(-10, 0);
    }

    @Test(expected = RuntimeException.class)
    public void ensureItsImpossibleToCreateARectangleWithNegativeWidth() {
        new Rectangle(1, -100);
    }

    @Test
    public void testArea() {
        Rectangle rectangle = new Rectangle(5, 10);
        int expectedArea = rectangle.getArea();
        Assert.assertThat(expectedArea, equalTo(50));
    }
}