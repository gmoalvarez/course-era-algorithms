/*************************************************************************
 *  Compilation:  javac PointSET.java
 *  Execution:    none
 *  Dependencies: Point2D.java and either SET or java.util.TreeSet
 *
 *  represents a set of points in the unit square
 *
 *************************************************************************/

public class PointSET {
    private int numOfPoints;
    private SET<Point2D> points;

    public         PointSET() {
        points = new SET<>();
        numOfPoints = 0;
    }                         // construct an empty set of points

    public boolean isEmpty() {
        return points.isEmpty();
    }                      // is the set empty?

    public int size() {
        return points.size();
    }                         // number of points in the set

    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException("Cannot insert null");
        if(points.contains(p)) return;
        points.add(p);
    }              // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException("Cannot check for null");
        return points.contains(p);
    }            // does the set contain point p?

    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        for (Point2D p : points)
            StdDraw.point(p.x(),p.y());
    }                         // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        return null;
    }             // all points that are inside the rectangle

    public Point2D nearest(Point2D p) {
        return null;
    }             // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {
        int N = 20;

        Point2D[] arrayOfPoints = new Point2D[N];
        for (int i = 0; i < N; i++) {
            arrayOfPoints[i] = new Point2D(StdRandom.uniform(0.0,1.0),StdRandom.uniform(0.0,1.0));
        }

        PointSET drawPoints = new PointSET();
        for (int i = 0; i < N; i++) {
            System.out.println(arrayOfPoints[i]);
            drawPoints.insert(arrayOfPoints[i]);
        }
        System.out.println("The size is(should be 20): " + drawPoints.size());
        System.out.println("Is the set empty? (should be false):" + drawPoints.isEmpty());
        System.out.println("Does the set contain (0.43212,0.121)? (probably false):"
                + drawPoints.contains(new Point2D(0.43212, 0.121)));
        System.out.println("Does the set contain arrayOfPoints[3] (should be true):"
                + drawPoints.contains(arrayOfPoints[3]));
        System.out.println("Draw points");
        drawPoints.draw();
    } // unit testing of the methods (optional)
}

