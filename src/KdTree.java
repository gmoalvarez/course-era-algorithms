import java.util.Comparator;

public class KdTree {
    private final double POINT_SIZE = 0.01;
    private final RectHV UNIT_RECT = new RectHV(0, 0, 1, 1);

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

        private Node(Point2D p, RectHV rect, Node left, Node right) {
            this.p = p;
            this.rect = rect;
            this.left = left;
            this.right = right;
        }
        private Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }

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
        root = insert(root, p, UNIT_RECT,Orientations.compareX);
        size++;
    }              // add the point to the set (if it is not already in the set)

    private Node insert(Node node, Point2D point, RectHV rect,Orientations orientation) {
        if (node == null) {
            Node tempNode = new Node(point, rect);
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
        draw(root);//recursively draw points inOrder
        drawRectangles(root, Orientations.compareX);
    }                         // draw all points to standard draw

    private void drawRectangles(Node node, Orientations orientation) {
        if (node != null) {
            switch (orientation) {
                case compareX:
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.setPenRadius();
                    StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
                    drawRectangles(node.left,Orientations.compareY);
                    drawRectangles(node.right,Orientations.compareY);
                    break;
                case compareY:
                    StdDraw.setPenColor(StdDraw.BLUE);
                    StdDraw.setPenRadius();
                    StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
                    drawRectangles(node.left,Orientations.compareX);
                    drawRectangles(node.right,Orientations.compareX);
                    break;
            }
        }
    }

    private void draw(Node node) {
        if (node != null) {
            draw(node.left);
            node.p.draw();
            draw(node.right);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        checkForNullArgument(rect);
        Queue<Point2D> pointsInRange = new Queue<>();
        if (isEmpty()) return pointsInRange;
        addPointsTo(pointsInRange, rect, root);
        return pointsInRange;
    }             // all points that are inside the rectangle



    private void addPointsTo(Queue<Point2D> points, RectHV that, Node node) {
        if (pointIsInRectangle(node.p,that))
            points.enqueue(node.p);
        if (node.left != null && node.left.rect.intersects(that))
            addPointsTo(points, that, node.left);
        if (node.right != null && node.right.rect.intersects(that))
            addPointsTo(points,that,node.right);
    }

    private boolean pointIsInRectangle(Point2D p, RectHV rect) {
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
        return nearest(root, p, root.p);
    }             // a nearest neighbor in the set to point p; null if the set is empty

    private Point2D nearest(Node n, Point2D p, Point2D best) {
        if (n== null)
            return best;

        Point2D min = best;
        double dst = p.distanceSquaredTo(best);
        if(dst < n.rect.distanceSquaredTo(p))//don't explore if node distance is greater than best so far
            return best;
        if(dst > n.p.distanceSquaredTo(p))
            min = n.p;
        if (n.left != null && n.left.rect.contains(p)) {
            min = nearest(n.left, p, min);
            min = nearest(n.right, p, min);
        } else if (n.right != null && n.right.rect.contains(p)) {
            min = nearest(n.right, p, min);
            min = nearest(n.left, p, min);
        } else {
            min = nearest(n.left, p, min);
            min = nearest(n.right, p, min);
        }
        return min;
    }

    private void checkForNullArgument(Point2D p) {
        if (p == null) throw new NullPointerException("Argument cannot be null");
    }

    private void checkForNullArgument(RectHV rect) {
        if (rect == null) throw new NullPointerException("Argument cannot be null");
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

        //Create a new rectangle and test range()
        RectHV rectangle = new RectHV(0.0, 0.0, .3, .4);
        System.out.println("The points in the rectangle " + rectangle + " are:");
        Iterable<Point2D> pointsInRectangle =drawPoints.range(rectangle);
        for (Point2D p : pointsInRectangle)
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