
public class KdTree {

    private int size;
    private Node root;


    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
    }

    public KdTree() {
    }

    // construct an empty set of points
    public boolean isEmpty() {
        return size() == 0;
    }                      // is the set empty?

    public int size() {
        return size;
    }                         // number of points in the set

    public void insert(Point2D p) {

    }              // add the point to the set (if it is not already in the set)

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