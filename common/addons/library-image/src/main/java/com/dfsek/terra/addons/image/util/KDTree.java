package com.dfsek.terra.addons.image.util;

import com.dfsek.terra.api.util.vector.Vector2;

import java.util.*;

public class KDTree {
    private static class Node {
        Vector2 point;
        Node left, right;
        int axis;

        Node(Vector2 point, int axis) {
            this.point = point;
            this.axis = axis;
        }
    }

    private Node root;

    public KDTree(List<Vector2> points) {
        root = build(points, 0);
    }

    private Node build(List<Vector2> pts, int depth) {
        if (pts.isEmpty()) return null;

        int axis = depth % 2;
        pts.sort(Comparator.comparingDouble(p -> axis == 0 ? p.getX() : p.getZ()));
        int median = pts.size() / 2;

        Node node = new Node(pts.get(median), axis);
        node.left = build(pts.subList(0, median), depth + 1);
        node.right = build(pts.subList(median + 1, pts.size()), depth + 1);
        return node;
    }

    public Vector2 nearest(Vector2 target) {
        return nearest(root, target, root.point, Double.POSITIVE_INFINITY);
    }

    private Vector2 nearest(Node node, Vector2 target, Vector2 bestPoint, double bestDistSq) {
        if (node == null) return bestPoint;

        double d = distSq(target, node.point);
        if (d < bestDistSq) {
            bestDistSq = d;
            bestPoint = node.point;
        }

        Node near = (getAxis(target, node.axis) < getAxis(node.point, node.axis)) ? node.left : node.right;
        Node far = (near == node.left) ? node.right : node.left;

        bestPoint = nearest(near, target, bestPoint, bestDistSq);
        bestDistSq = distSq(target, bestPoint);

        double axisDist = getAxis(target, node.axis) - getAxis(node.point, node.axis);
        if (axisDist * axisDist < bestDistSq) {
            bestPoint = nearest(far, target, bestPoint, bestDistSq);
        }

        return bestPoint;
    }

    public List<Vector2> kNearest(Vector2 target, int k) {
        PriorityQueue<Neighbor> best = new PriorityQueue<>(Comparator.comparingDouble(n -> -n.distSq));
        kNearest(root, target, k, best);
        List<Neighbor> sorted = new ArrayList<>(best);
        sorted.sort(Comparator.comparingDouble(n -> n.distSq));

        List<Vector2> result = new ArrayList<>();
        for (Neighbor n : sorted) result.add(n.point);
        return result;
    }


    private void kNearest(Node node, Vector2 target, int k, PriorityQueue<Neighbor> best) {
        if (node == null) return;

        double dSq = distSq(target, node.point);
        if (best.size() < k) {
            best.add(new Neighbor(node.point, dSq));
        } else if (dSq < best.peek().distSq) {
            best.poll();
            best.add(new Neighbor(node.point, dSq));
        }

        Node near = (getAxis(target, node.axis) < getAxis(node.point, node.axis)) ? node.left : node.right;
        Node far = (near == node.left) ? node.right : node.left;

        kNearest(near, target, k, best);

        double axisDist = getAxis(target, node.axis) - getAxis(node.point, node.axis);
        if (best.size() < k || axisDist * axisDist < best.peek().distSq) {
            kNearest(far, target, k, best);
        }
    }

    private double distSq(Vector2 a, Vector2 b) {
        double dx = a.getX() - b.getX();
        double dy = a.getZ() - b.getZ();
        return dx * dx + dy * dy;
    }

    private double getAxis(Vector2 v, int axis) {
        return axis == 0 ? v.getX() : v.getZ();
    }

    private static class Neighbor {
        Vector2 point;
        double distSq;

        Neighbor(Vector2 point, double distSq) {
            this.point = point;
            this.distSq = distSq;
        }
    }
}
