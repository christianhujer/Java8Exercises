package com.nelkinda.training.java8.exercise3;

import org.junit.Test;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CounterTest {
    @Test
    public void givenCounter_whenClickingButton_thenIncrements() throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Given
        final Counter counter = new Counter();
        final AbstractButton button = (AbstractButton) ComponentFinder.findComponent(counter.getComponentName("Button"));

        // Then
        assertNotNull(button);
        assertEquals(0, counter.getCounter());

        // When
        button.doClick();

        // Then
        assertEquals(1, counter.getCounter());

        // When
        button.doClick();

        // Then
        assertEquals(2, counter.getCounter());
    }

}
