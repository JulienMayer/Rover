package com.nasa;

import org.junit.jupiter.api.Test;
// + les imports déjà présents


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Unit tests for the Rover class.
 */
public class RoverTest {

    private List<String> runWithInput(String input) throws IOException {
        Path tempFile = Files.createTempFile("input", ".txt");
        Files.writeString(tempFile, input);
        List<String> output = Rover.executeFromFile(tempFile.toString());
        Files.deleteIfExists(tempFile);
        return output;
    }

    @Test public void testStandardInstructions() throws IOException {
        String input = """
                5 5
                1 2 N
                LMLMLMLMM
                3 3 E
                MMRMMRMRRM
                """;
        List<String> output = runWithInput(input);
        assertEquals(List.of("1 3 N", "5 1 E"), output);
    }

    @Test public void testNoInstructions() throws IOException {
        String input = """
                5 5
                0 0 N
                """;
        List<String> output = runWithInput(input);
        assertEquals(List.of("0 0 N"), output);
    }

    @Test public void testOnlyRotationsL() throws IOException {
        String input = """
                5 5
                2 2 N
                L
                """;
        List<String> output = runWithInput(input);
        assertEquals(List.of("2 2 W"), output);
    }

        @Test public void testOnlyRotationsR() throws IOException {
        String input = """
                5 5
                2 2 N
                R
                """;
        List<String> output = runWithInput(input);
        assertEquals(List.of("2 2 E"), output);
    }

        @Test public void testOnlyMove() throws IOException {
        String input = """
                5 5
                2 2 N
                M
                """;
        List<String> output = runWithInput(input);
        assertEquals(List.of("2 3 N"), output);
    }

    @Test public void testEdgeMovements() throws IOException {
        String input = """
                5 5
                0 0 N
                MMMMM
                5 5 S
                MMMMM
                0 5 E
                MMMMM
                5 0 W
                MMMMM
                """;
        List<String> output = runWithInput(input);
        assertEquals(List.of("0 5 N", "5 0 S", "5 5 E", "0 0 W"), output);
    }

    @Test public void testOutOfBoundsNorth() throws IOException {
        String input = """
                5 5
                0 5 N
                M
                """;
        assertThrows(IllegalArgumentException.class, () -> runWithInput(input));
    }

    @Test public void testOutOfBoundsSouth() throws IOException {
        String input = """
                5 5
                0 0 S
                M
                """;
        assertThrows(IllegalArgumentException.class, () -> runWithInput(input));
    }

    @Test public void testOutOfBoundsEast() throws IOException {
        String input = """
                5 5
                5 0 E
                M
                """;
        assertThrows(IllegalArgumentException.class, () -> runWithInput(input));
    }

    @Test public void testOutOfBoundsWest() throws IOException {
        String input = """
                5 5
                0 0 W
                M
                """;
        assertThrows(IllegalArgumentException.class, () -> runWithInput(input));
    }

    @Test public void testInvalidInstructionCharacter() throws IOException {
        String input = """
                5 5
                1 1 N
                MXR
                """;
        assertThrows(IllegalArgumentException.class, () -> runWithInput(input));
    }

    @Test public void testInvalidStartDirection() throws IOException {
        String input = """
                5 5
                1 1 Z
                M
                """;
        assertThrows(IllegalArgumentException.class, () -> runWithInput(input));
    }

    @Test public void testInvalidStartPosition() throws IOException {
        String input = """
                5 5
                6 6 N
                M
                """;
        assertThrows(IllegalArgumentException.class, () -> runWithInput(input));
    }

    @Test public void testInvalidPlateauCoordinates1() throws IOException {
        String input = """
                -5 -5
                0 0 N
                M
                """;
        assertThrows(IllegalArgumentException.class, () -> runWithInput(input));
    }

        @Test public void testInvalidPlateauCoordinates2() throws IOException {
        String input = """
                0 0
                0 0 N
                M
                """;
        assertThrows(IllegalArgumentException.class, () -> runWithInput(input));
    }

    @Test public void testMinimalPlateauSize() throws IOException {
        String input = """
                1 1
                0 0 N
                """;
        List<String> output = runWithInput(input);
        assertEquals(List.of("0 0 N"), output);
    }

    @Test public void testSequentialRoverExecution() throws IOException {
        String input = """
                5 5
                0 0 N
                M
                0 0 E
                M
                """;
        List<String> output = runWithInput(input);
        assertEquals(List.of("0 1 N", "1 0 E"), output);
    }

    @Test public void testMultipleLRBeforeMove() throws IOException {
        String input = """
                5 5
                1 1 N
                LLRRLLRRMM
                """;
        List<String> output = runWithInput(input);
        assertEquals(List.of("1 3 N"), output);
    }
}

