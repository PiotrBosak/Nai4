import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class KMeans {
    private static final int dimension = 4;
    private static Random random = new Random();

    private static List<Point> points;
    private static List<Centroid> centroids;

    public static void main(String[] args) {
        int k = getK();
        cluster(k);
    }

    public static int getK() {
        var scanner = new Scanner(System.in);
        System.out.println("pass your k");
        return scanner.nextInt();
    }

    public static void cluster(int k) {
        try {
            points = getPoints();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        centroids = getRandomCentroids(k);
        for (int i = 0; i < 100; ++i) {
            assignPointsToCentroids();
            recalculateCentroids();
            printAverage();
        }
        printClusters();

    }

    private static void printClusters() {
        for (Centroid c : centroids) {
            System.out.println("Cluster");
            for(Point p: c.assignedPoints)
                p.printCoordinates();
        }
    }

    private static List<Point> getPoints() throws FileNotFoundException {
        var points = new ArrayList<Point>();
        var file = new File("data/iris.data");
        var scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String[] a = scanner.next().split(",");
            double[] coordinates = new double[dimension];
            for (int i = 0; i < dimension; ++i)
                coordinates[i] = Double.parseDouble(a[i]);
            points.add(new Point(coordinates));
        }
        return points;

    }

    private static List<Centroid> getRandomCentroids(int k) {
        List<Centroid> centroids = new ArrayList<>();
        for (int i = 0; i < k; ++i) {
            double[] coordinates = new double[dimension];
            for (int j = 0; j < coordinates.length; ++j)
                coordinates[j] = random.nextDouble() * 10;
            centroids.add(new Centroid(coordinates));
        }
        return centroids;
    }

    private static void assignPointsToCentroids() {
        for (Centroid c : centroids)
            c.assignedPoints = new ArrayList<>();
        for (Point p : points)
            assignPointToCentroid(p, getNearestCentroid(p));
    }


    private static Centroid getNearestCentroid(Point p) {
        Centroid nearest = centroids.get(0);
        double distance = calculateDistance(p, nearest);
        for (int i = 1; i < centroids.size(); ++i) {
            if (calculateDistance(p, centroids.get(i)) < distance) {
                distance = calculateDistance(p, centroids.get(i));
                nearest = centroids.get(i);
            }
        }
        return nearest;
    }

    private static void assignPointToCentroid(Point p, Centroid c) {
        c.assignedPoints.add(p);
    }

    private static void printAverage() {
        for (Centroid c : centroids)
            System.out.println("distance is " + c.assignedPoints.stream().mapToDouble(p -> calculateDistance(p, c))
                    .average().orElse(100));
    }

    private static double calcAverage(double[] doubles) {
        double sum = 0;
        for (Double d : doubles)
            sum += d;
        return sum / doubles.length;
    }


    private static double calculateDistance(Point a, Point b) {
        double distance;
        double distanceBeforeSquare = 0;
        for (int i = 0; i < a.getCoordinates().length; ++i)
            distanceBeforeSquare += Math.pow((a.getCoordinates()[i] - b.getCoordinates()[i]), 2);
        distance = Math.sqrt(distanceBeforeSquare);
        return distance;
    }

    private static void recalculateCentroids() {
        for (Centroid c : centroids) {
            if (c.assignedPoints != null && !c.assignedPoints.isEmpty())
                for (int i = 0; i < c.getCoordinates().length; ++i)
                    c.getCoordinates()[i] = getAverageForDimension(i, c);
        }

    }

    private static double getAverageForDimension(int dimension, Centroid c) {
        return c.assignedPoints.stream().map(Point::getCoordinates)
                .mapToDouble(coordinates -> coordinates[dimension])
                .average().orElse(100);
    }


}
