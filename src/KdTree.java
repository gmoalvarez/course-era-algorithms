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

        root = insert(root, p, Orientation.compareX);
    }              // add the point to the set (if it is not already in the set)

    private Node insert(Node node, Point2D point, Orientation orientation) {
        if (node == null) {
            Node tempNode = new Node();
            tempNode.p = point;
//            tempNode.rect=???
            return tempNode;
        }
        if (node.p.equals(point)) {
            StdOut.println("Attempting to insert equal point, return");
            return null;
        }
        switch (orientation) {
            case compareX:
                Comparator<Point2D> cmpX = Point2D.X_ORDER;
                int compX = cmpX.compare(point, node.p);
                if (compX < 0) node.lb = insert(node.lb, point, Orientation.compareY);
                else if (compX >= 0) node.rt = insert(node.rt, point, Orientation.compareY);
            case compareY:
                Comparator<Point2D> cmpY = Point2D.Y_ORDER;
                int compY = cmpY.compare(point, node.p);
                if (compY < 0) node.lb = insert(node.lb, point, Orientation.compareY);
                else if (compY >= 0) node.rt = insert(node.rt, point, Orientation.compareY);
        }
        return node;
    }

    public boolean contains(Point2D p) {
        return false;
    }            // does the set contain point p?

    public void draw() {

    }                         // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        return null;
    }             // all points that are inside the rectangle

    public Point2D nearest(Point2D p) {
        return null;
    }             // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {

    }                  // unit testing of the methods (optional)
}