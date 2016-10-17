package com.nelkinda.javax.swing;

import javax.swing.ImageIcon;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class IconCacheTest {

    private static final String ICON_ABOUT_SMALL_URL = "toolbarButtonGraphics/general/About16.gif";

    private final IconCache iconCache = new IconCache();

    @Test
    public void unavailableImage_returnsNull() {
        assertNull(iconCache.getImageIcon("this.does.not.exist"));
    }

    @Test
    public void emptyCache_loadsImage() {
        final ImageIcon imageIcon = iconCache.getImageIcon(ICON_ABOUT_SMALL_URL);
        assertNotNull(imageIcon);
    }

    @Test
    public void filledCache_returnsSameImage() {
        final ImageIcon imageIcon1 = iconCache.getImageIcon(ICON_ABOUT_SMALL_URL);
        final ImageIcon imageIcon2 = iconCache.getImageIcon(ICON_ABOUT_SMALL_URL);
        assertSame(imageIcon1, imageIcon2);
    }

    @Test
    public void garbageCollection_clearsCache() {
        final int hashCode1 = getImageHashCode(ICON_ABOUT_SMALL_URL);
        assertEquals(hashCode1, getImageHashCode(ICON_ABOUT_SMALL_URL));
        System.gc();
        final int hashCode2 = getImageHashCode(ICON_ABOUT_SMALL_URL);
        assertNotEquals(hashCode1, hashCode2);
    }

    private int getImageHashCode(final String url) {
        return iconCache.getImageIcon(ICON_ABOUT_SMALL_URL).hashCode();
    }
}
