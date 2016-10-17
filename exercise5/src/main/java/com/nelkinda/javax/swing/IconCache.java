package com.nelkinda.javax.swing;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import javax.swing.ImageIcon;

public class IconCache {
    private final Map<URL, WeakReference<ImageIcon>> iconCache = new WeakHashMap<>();
    public ImageIcon getImageIcon(final String urlString) {
        return Optional
                .ofNullable(getClass().getClassLoader().getResource(urlString))
                .map(url -> iconCache.computeIfAbsent(url, (u) -> new WeakReference<>(new ImageIcon(u))).get())
                .orElse(null);
    }
}
