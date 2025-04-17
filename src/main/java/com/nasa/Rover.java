package com.nasa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Simulates the navigation of robotic rovers on a rectangular plateau.
 * Each rover receives commands to rotate and move on a grid.
 */
public class Rover {

    /**
     * Represents the four cardinal directions.
     */
    enum Direction {
        N, E, S, W;

        /**
         * Returns the direction resulting from a 90-degree left turn.
         */
        public Direction turnLeft() {
            return values()[(this.ordinal() + 3) % 4];
        }

        /**
         * Returns the direction resulting from a 90-degree right turn.
         */
        public Direction turnRight() {
            return values()[(this.ordinal() + 1) % 4];
        }
    }

    /**
     * Represents a position on the plateau with coordinates and direction.
     */
    static class Position {
        int x, y;
        Direction dir;

        Position(int x, int y, Direction dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }

        /**
         * Moves the position one step forward in the current direction.
         */
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

    /**
     * Reads rover instructions from a file and executes them.
     * 
     * @param args expects a single argument: the input filename.
     * @throws IOException if file reading fails.
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java -jar rover.jar input.txt");
            return;
        }

        List<String> outputResults = executeFromFile(args[0]);
        outputResults.forEach(System.out::println);
    }

    /**
     * Parses instructions from an input file and returns the final positions of all rovers.
     *
     * @param filename input file containing plateau dimensions and rover commands
     * @return list of final rover positions
     * @throws IOException if file reading fails
     */
    public static List<String> executeFromFile(String filename) throws IOException {
        List<String> results = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = br.readLine();
        String[] plateau = line.split(" ");
        int maxX = Integer.parseInt(plateau[0]);
        int maxY = Integer.parseInt(plateau[1]);
        if (maxX < 0 || maxY < 0){
            throw new IllegalArgumentException("Plateau dimensions must be non-negative");
        }
        String positionLine;
        while ((positionLine = br.readLine()) != null) {
            String instructionLine = br.readLine();
            if (instructionLine == null) instructionLine = "";

            String[] parts = positionLine.split(" ");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            Direction dir = Direction.valueOf(parts[2]);
            Position pos = new Position(x, y, dir);

            for (char cmd : instructionLine.toCharArray()) {
                switch (cmd) {
                    case 'L' -> pos.dir = pos.dir.turnLeft();
                    case 'R' -> pos.dir = pos.dir.turnRight();
                    case 'M' -> {
                        pos.move();
                        if (pos.x < 0 || pos.y < 0 || pos.x > maxX || pos.y > maxY) {
                            throw new IllegalArgumentException("Rover moved out of bounds");
                        }
                    }
                    default -> throw new IllegalArgumentException("Invalid command: " + cmd);
                }
            }

            results.add(pos.toString());
        }

        br.close();
        return results;
    }
} 