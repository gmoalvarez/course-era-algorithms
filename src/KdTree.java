import java.util.Comparator;

public class KdTree {

    private int size;
    private Node root;
    private boolean compareX;

    private enum Orientation {
        compareX, compareY,
    }

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
    }

    public KdTree() {
        size = 0;
        root = null;
        compareX = true;
    } // construct an empty set of points

    public boolean isEmpty() {
        return size() == 0;
    }                      // is the set empty?

    public int size() {
        return size;
    }                         // number of points in the set

    public void insert(Point2D p) {
        checkForNullArgument(p);
        root = insert(root, p, Orientation.compareX);
        size++;
    }              // add the point to the set (if it is not already in the set)

    private Node insert(Node node, Point2D point, Orientation orientation) {
        if (node == null) {
            Node tempNode = new Node();
            tempNode.p = point;
//            tempNode.rect=???
            return tempNode;
        }
//        if (node.p.equals(point)) {
//            StdOut.println("Attempting to insert equal point, return");
//            return null;
//        }
        switch (orientation) {
            case compareX:
                Comparator<Point2D> cmpX = Point2D.X_ORDER;
                int compX = cmpX.compare(point, node.p);
                if (compX < 0) node.lb = insert(node.lb, point, Orientation.compareY);
                else if (compX >= 0) node.rt = insert(node.rt, point, Orientation.compareY);
                break;
            case compareY:
                Comparator<Point2D> cmpY = Point2D.Y_ORDER;
                int compY = cmpY.compare(point, node.p);
                if (compY < 0) node.lb = insert(node.lb, point, Orientation.compareX);
                else if (compY >= 0) node.rt = insert(node.rt, point, Orientation.compareX);
                break;
        }
        return node;
    }

    public boolean contains(Point2D p) {
        checkForNullArgument(p);

        return false;
    }            // does the set contain point p?

    public void draw() {
//        StdDraw.setPenColor(StdDraw.BLACK);
//        StdDraw.setPenRadius(POINT_SIZE);
//        for (Point2D p : points)
//            StdDraw.point(p.x(),p.y());
    }                         // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        return null;
    }             // all points that are inside the rectangle

    public Point2D nearest(Point2D p) {
        return null;
    }             // a nearest neighbor in the set to point p; null if the set is empty

    private void checkForNullArgument(Point2D p) {
        if (p == null) throw new NullPointerException("Argument cannot be null");
    }

    public static void main(String[] args) {
        int N = 5;

        double[] xValues = {.1, .4, .6, .3, .2};
        double[] yValues = {.5, .4, .2, .7, .1};

        Point2D[] arrayOfPoints = new Point2D[N];
        for (int i = 0; i < N; i++) {
            arrayOfPoints[i] = new Point2D(xValues[i], yValues[i]);
        }

        KdTree drawPoints = new KdTree();
        for (int i = 0; i < N; i++) {
            System.out.println(arrayOfPoints[i]);
            drawPoints.insert(arrayOfPoints[i]);
        }
        System.out.println("The size is(should be 20): " + drawPoints.size());
        System.out.println("Is the set empty? (should be false):" + drawPoints.isEmpty());
        System.out.println("Does the set contain (0.43212,0.121)? (probably false):"
                + drawPoints.contains(new Point2D(0.43212, 0.121)));
        System.out.println("Does the set contain (.6,.2) (should be true):"
                + drawPoints.contains(new Point2D(.6,.2)));
        System.out.println("Draw points");
        drawPoints.draw();
//
//        //Create a new rectangle and test range()
//        RectHV rectangle = new RectHV(0.1, 0.4, 0.5, 0.7);
//        System.out.println("The points in the rectangle " + rectangle + " are:");
//        for (Point2D p : drawPoints.range(rectangle))
//            System.out.println(p);
//        rectangle.draw();
//
//        //Find the point closest to (0.1,0.1)
//        Point2D testNearest = new Point2D(0.1, 0.1);
//        Point2D nearestPoint = drawPoints.nearest(testNearest);
//        System.out.println("Find the point closest to (0.1,0.1)");
//        System.out.println(nearestPoint);
//        StdDraw.setPenColor(StdDraw.RED);
//        StdDraw.setPenRadius(0.01);
//        StdDraw.point(testNearest.x(),testNearest.y());
//        StdDraw.setPenColor(StdDraw.BLUE);
//        StdDraw.setPenRadius(0.03);
//        StdDraw.point(nearestPoint.x(),nearestPoint.y());

    } // unit testing of the methods (optional)
}