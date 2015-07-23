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
    private final double POINT_SIZE = 0.01;

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
        checkForNullArgument(p);
        if(points.contains(p)) return;
        points.add(p);
    }              // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        checkForNullArgument(p);
        return points.contains(p);
    }            // does the set contain point p?

    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(POINT_SIZE);
        for (Point2D p : points)
            StdDraw.point(p.x(),p.y());
    }                         // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        checkForNullArgument(rect);
        Queue<Point2D> pointList = new Queue<>();
        for (Point2D p : points)
            if (pointIsInRectangle(p, rect))
                pointList.enqueue(p);
        return pointList;
    }             // all points that are inside the rectangle

    private boolean pointIsInRectangle(Point2D p, RectHV rect) {
        checkForNullArgument(p);
        checkForNullArgument(rect);
        if (p.x() >= rect.xmin() &&
                p.x() <= rect.xmax() &&
                p.y() >= rect.ymin() &&
                p.y() <= rect.ymax()) {
            return true;
        }
        return false;
    }

    public Point2D nearest(Point2D p) {
        checkForNullArgument(p);
        Point2D nearestPoint = null;
        double currentDistance;
        double currentMinDistance = 2;
        for (Point2D point : points) {
            currentDistance = squareDistanceBetweenPoints(point, p);
            if (currentDistance < currentMinDistance) {
                nearestPoint = point;
                currentMinDistance = currentDistance;
            }
        }
        return nearestPoint;
    }             // a nearest neighbor in the set to point p; null if the set is empty

    private double squareDistanceBetweenPoints(Point2D point, Point2D p) {
        double dx = point.x() - p.x();
        double dy = point.y() - p.y();
        return (dx*dx + dy*dy);
    }

    private void checkForNullArgument(Point2D p) {
        if (p == null) throw new NullPointerException("Argument cannot be null");
    }

    private void checkForNullArgument(RectHV rect) {
        if (rect == null) throw new NullPointerException("Argument cannot be null");
    }

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

        //Create a new rectangle and test range()
        RectHV rectangle = new RectHV(0.1, 0.4, 0.5, 0.7);
        System.out.println("The points in the rectangle " + rectangle + " are:");
        for (Point2D p : drawPoints.range(rectangle))
            System.out.println(p);
        rectangle.draw();

        //Find the point closest to (0.1,0.1)
        Point2D testNearest = new Point2D(0.1, 0.1);
        Point2D nearestPoint = drawPoints.nearest(testNearest);
        System.out.println("Find the point closest to (0.1,0.1)");
        System.out.println(nearestPoint);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(testNearest.x(),testNearest.y());
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius(0.03);
        StdDraw.point(nearestPoint.x(),nearestPoint.y());

    } // unit testing of the methods (optional)
}

