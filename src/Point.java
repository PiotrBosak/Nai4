public class Point {

    public static final  int dimension = 4;
    private double[] coordinates;
    public String type;

    public Point(double[] coordinates) {
        this.coordinates = coordinates;
    }
    public Point(double [] coordinates,String type){
        this.coordinates = coordinates;
        this.type = type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void printCoordinates(){
        System.out.print("[ ");
        for (double coordinate : coordinates) {
            System.out.print(coordinate);
            System.out.print(", ");
        }
        System.out.println("]");
    }
}
