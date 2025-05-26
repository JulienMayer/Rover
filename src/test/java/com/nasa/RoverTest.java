package com.nasa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

public class RoverTest {

    private List<String> runWithInput(String input) throws IOException {
        Path tempFile = Files.createTempFile("input", ".txt");
        Files.writeString(tempFile, input);
        List<String> output = Rover.executeFromFile(tempFile.toString());
        Files.deleteIfExists(tempFile);
        return output;
    }

    // Fonctionnels valides
    @Test public void testStandardInstructions() throws IOException {
        String input = """
            5 5
            1 2 N
            LMLMLMLMM
            3 3 E
            MMRMMRMRRM
            """;
        assertEquals(List.of("1 3 N", "5 1 E"), runWithInput(input));
    }

    @Test public void testOnlyRotationsL() throws IOException {
        String input = """
            5 5
            2 2 N
            L
            """;
        assertEquals(List.of("2 2 W"), runWithInput(input));
    }

    @Test public void testOnlyRotationsR() throws IOException {
        String input = """
            5 5
            2 2 N
            R
            """;
        assertEquals(List.of("2 2 E"), runWithInput(input));
    }

    @Test public void testOnlyMove() throws IOException {
        String input = """
            5 5
            2 2 N
            M
            """;
        assertEquals(List.of("2 3 N"), runWithInput(input));
    }

    @Test public void testNoInstructions() throws IOException {
        String input = """
            5 5
            0 0 N
            """;
        assertEquals(List.of("0 0 N"), runWithInput(input));
    }

    @Test public void testMinimalPlateauSize() throws IOException {
        String input = """
            1 1
            0 0 N
            """;
        assertEquals(List.of("0 0 N"), runWithInput(input));
    }

    // Bords
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

    // Erreurs de validation
    @Test public void testInvalidInstructionCharacter() throws IOException {
        String input = """
            5 5
            1 1 N
            MX
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

    @Test public void testEmptyLinesIgnored() throws IOException {
        String input = """
            5 5

            1 1 N

            M

            2 2 E

            M
            """;
        assertEquals(List.of("1 2 N", "3 2 E"), runWithInput(input));
    }

    @Test public void testCaseSensitivity() throws IOException {
        String input = """
            5 5
            1 1 N
            mrl
            """;
        assertThrows(IllegalArgumentException.class, () -> runWithInput(input));
    }

    @Test public void testMultipleLRBeforeMove() throws IOException {
        String input = """
            5 5
            1 1 N
            LLRRLLRRMM
            """;
        assertEquals(List.of("1 3 N"), runWithInput(input));
    }

    @Test public void testSequentialRoverExecution() throws IOException {
        String input = """
            5 5
            0 0 N
            M
            0 0 E
            M
            """;
        assertEquals(List.of("0 1 N", "1 0 E"), runWithInput(input));
    }

    // Plateau vide
    @Test public void testEmptyFile() {
        String input = "";
        assertThrows(IllegalArgumentException.class, () -> runWithInput(input));
    }
}
