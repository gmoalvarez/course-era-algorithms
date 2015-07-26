import java.util.Comparator;

public class KdTree {
    private final double POINT_SIZE = 0.01;

    private int size;
    private Node root;

    private enum Orientations {
        compareX, compareY,
    }

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node left;        // the left/bottom subtree
        private Node right;        // the right/top subtree
    }

    public KdTree() {
        size = 0;
        root = null;
    } // construct an empty set of points

    public boolean isEmpty() {
        return size() == 0;
    }                      // is the set empty?

    public int size() {
        return size;
    }                         // number of points in the set

    public void insert(Point2D p) {
        checkForNullArgument(p);
        if (contains(p))
            return;
        RectHV unitRect = new RectHV(0, 0, 1, 1);
        root = insert(root, p, unitRect,Orientations.compareX);
        size++;
    }              // add the point to the set (if it is not already in the set)

    private Node insert(Node node, Point2D point, RectHV rect,Orientations orientation) {
        if (node == null) {
            Node tempNode = new Node();
            tempNode.p = point;
            tempNode.rect = rect;
            return tempNode;
        }
        switch (orientation) {
            case compareX:
                Comparator<Point2D> cmpX = Point2D.X_ORDER;
                int compX = cmpX.compare(point, node.p);
                if (compX < 0) {
                    RectHV rectangle = new RectHV(rect.xmin(), rect.ymin(), node.p.x(), rect.ymax());
                    node.left = insert(node.left, point, rectangle, Orientations.compareY);
                }

                else if (compX >= 0) {
                    RectHV rectangle = new RectHV(node.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
                    node.right = insert(node.right, point, rectangle, Orientations.compareY);
                }
                break;
            case compareY:
                Comparator<Point2D> cmpY = Point2D.Y_ORDER;
                int compY = cmpY.compare(point, node.p);
                if (compY < 0){
                    RectHV rectangle = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.p.y());
                    node.left = insert(node.left, point,rectangle,Orientations.compareX);
                }
                else if (compY >= 0) {
                    RectHV rectangle = new RectHV(rect.xmin(), node.p.y(), rect.xmax(), rect.ymax());
                    node.right = insert(node.right, point,rectangle, Orientations.compareX);
                }
                break;
        }
        return node;
    }

    public boolean contains(Point2D p) {
        checkForNullArgument(p);
        return contains(root, p, Orientations.compareX);
    }            // does the set contain point p?

    private boolean contains(Node node, Point2D point, Orientations orientation) {
        if(node == null)
            return false;
        if (node.p.equals(point))
            return true;

        boolean found = false;
        switch (orientation) {
            case compareX:
                Comparator<Point2D> cmpX = Point2D.X_ORDER;
                int compX = cmpX.compare(point, node.p);
                if (compX < 0) found = contains(node.left, point, Orientations.compareY);
                else if (compX >= 0) found = contains(node.right, point, Orientations.compareY);
                break;
            case compareY:
                Comparator<Point2D> cmpY = Point2D.Y_ORDER;
                int compY = cmpY.compare(point, node.p);
                if (compY < 0) found = contains(node.left, point, Orientations.compareX);
                else if (compY >= 0) found = contains(node.right, point, Orientations.compareX);
                break;
        }
        return found;
    }

    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(POINT_SIZE);
        drawPoint(root);//recursively draw points inOrder
    }                         // draw all points to standard draw

    private void drawPoint(Node node) {
        if (node != null) {
            drawPoint(node.left);
            node.p.draw();
            drawPoint(node.right);
        }
    }

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

        double[] xValues = {.7, .5, .2, .4, .9};
        double[] yValues = {.2, .4, .3, .7, .6};

        Point2D[] arrayOfPoints = new Point2D[N];
        for (int i = 0; i < N; i++) {
            arrayOfPoints[i] = new Point2D(xValues[i], yValues[i]);
        }

        KdTree drawPoints = new KdTree();
        for (int i = 0; i < N; i++) {
            System.out.println(arrayOfPoints[i]);
            drawPoints.insert(arrayOfPoints[i]);
        }
        System.out.println("The size is(should be 5): " + drawPoints.size());
        System.out.println("Is the set empty? (should be false):" + drawPoints.isEmpty());
        System.out.println("Does the set contain (0.21, 0.11)? (should be false): "
                + drawPoints.contains(new Point2D(0.21, 0.11)));
        System.out.println("Does the set contain (.2,.3) (should be true): "
                + drawPoints.contains(new Point2D(.2,.3)));
        System.out.println("Draw points");
        drawPoints.draw();

        System.out.println("Try to insert duplicate");
        System.out.println("Insert (.2,.3):");
        drawPoints.insert(new Point2D(.2,.3));
        System.out.println("The size is(should be 5): " + drawPoints.size());



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