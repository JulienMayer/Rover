package com.nasa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Simulates the navigation of robotic rovers on a rectangular plateau.
 */
public class Rover {

    enum Direction {
        N, E, S, W;

        public Direction turnLeft() {
            return values()[(this.ordinal() + 3) % 4];
        }

        public Direction turnRight() {
            return values()[(this.ordinal() + 1) % 4];
        }
    }

    static class Position {
        int x, y;
        Direction dir;

        Position(int x, int y, Direction dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }

        void move() {
            switch (dir) {
                case N -> y++;
                case E -> x++;
                case S -> y--;
                case W -> x--;
            }
        }

        @Override
        public String toString() {
            return x + " " + y + " " + dir;
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java -jar rover.jar input.txt");
            return;
        }

        try {
            List<String> outputs = executeFromFile(args[0]);
            outputs.forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }

    public static List<String> executeFromFile(String filename) throws IOException {
        List<String> allLines;

        // Lire toutes les lignes et fermer le fichier immédiatement
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            allLines = reader.lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .toList();
        }

        if (allLines.isEmpty()) {
            throw new IllegalArgumentException("Fichier vide ou mal formaté");
        }

        // Lecture des dimensions du plateau
        String[] plateauDims = allLines.get(0).split(" ");
        if (plateauDims.length != 2) {
            throw new IllegalArgumentException("Dimensions du plateau incorrectes");
        }

        int maxX = Integer.parseInt(plateauDims[0]);
        int maxY = Integer.parseInt(plateauDims[1]);

        if (maxX <= 0 || maxY <= 0) {
            throw new IllegalArgumentException("Les dimensions du plateau doivent être strictement positives");
        }

        List<String> results = new ArrayList<>();

        // Lecture des rovers (position + commandes éventuelles)
        for (int i = 1; i < allLines.size(); i += 2) {
            String positionLine = allLines.get(i);
            String commandsLine = (i + 1 < allLines.size()) ? allLines.get(i + 1) : "";            

            Position pos = parseInitialPosition(positionLine);
            validateStartPosition(pos, maxX, maxY);
            executeCommands(pos, commandsLine, maxX, maxY);
            results.add(pos.toString());
        }

        return results;
    }

    private static Position parseInitialPosition(String line) {
        String[] parts = line.trim().split(" ");
        if (parts.length != 3)
            throw new IllegalArgumentException("Position initiale mal formatée : " + line);

        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        Direction dir = Direction.valueOf(parts[2]);
        return new Position(x, y, dir);
    }

    private static void validateStartPosition(Position pos, int maxX, int maxY) {
        if (pos.x < 0 || pos.y < 0 || pos.x > maxX || pos.y > maxY) {
            throw new IllegalArgumentException("Position initiale hors du plateau : " + pos);
        }
    }

    private static void executeCommands(Position pos, String commands, int maxX, int maxY) {
        for (char command : commands.toCharArray()) {
            switch (command) {
                case 'L' -> pos.dir = pos.dir.turnLeft();
                case 'R' -> pos.dir = pos.dir.turnRight();
                case 'M' -> {
                    pos.move();
                    if (pos.x < 0 || pos.y < 0 || pos.x > maxX || pos.y > maxY) {
                        throw new IllegalArgumentException("Rover sorti du plateau : " + pos);
                    }
                }
                default -> throw new IllegalArgumentException("Invalid command: " + command);
            }
        }
    }
}
