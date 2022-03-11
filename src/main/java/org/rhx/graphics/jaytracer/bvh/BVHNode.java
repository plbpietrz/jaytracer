package org.rhx.graphics.jaytracer.bvh;

import org.rhx.graphics.jaytracer.core.Hitable;
import org.rhx.graphics.jaytracer.core.HitableList;
import org.rhx.graphics.jaytracer.core.Ray;
import org.rhx.graphics.jaytracer.material.Material;
import org.rhx.graphics.jaytracer.util.HitRecord;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BVHNode implements Hitable {

    private final Hitable left;
    private final Hitable right;
    private final AABB box;

    private BVHNode(Hitable left, Hitable right, AABB box) {
        this.left = left;
        this.right = right;
        this.box = box;
    }

    public static BVHNode of(HitableList hitableList, float time0, float time1) {
        List<Hitable> hitable = hitableList.getHitables();
        return new BVHNode(hitable, 0, 0, hitable.size(), time0, time1);
    }

    private BVHNode(List<Hitable> hitables, int axis, int start, int end, float time0, float time1) {
        List<Hitable> objects = new ArrayList<>(hitables);

        Comparator<Hitable> cmp = switch (axis) {
            case 0 -> BBOX_X_CMP;
            case 1 -> BBOX_Y_CMP;
            case 2 -> BBOX_Z_CMP;
            default -> throw new IllegalArgumentException();
        };

        int objectSpan = end - start;
        if (objectSpan == 1) {
            left = right = objects.get(start);
        } else if (objectSpan == 2) {
            if (cmp.compare(objects.get(start), objects.get(start + 1)) < 0) {
                left = objects.get(start);
                right = objects.get(start + 1);
            } else {
                left = objects.get(start + 1);
                right = objects.get(start);
            }
        } else {
            objects.subList(start, end).sort(cmp);

            int mid = start + objectSpan/2;
            int sortAxis = (axis + 1) % 3;
            left = new BVHNode(objects, sortAxis, start, mid, time0, time1);
            right = new BVHNode(objects, sortAxis, mid, end, time0, time1);
        }

        AABB boxLeft = left.boundingBox(time0, time1);
        AABB boxRight = right.boundingBox(time0, time1);

        box = AABB.of(boxLeft, boxRight);
    }

    @Override
    public HitRecord hit(Ray ray, float tMin, float tMax) {
        if (!box.hit(ray, tMin, tMax))
            return null;
        HitRecord leftHit = left.hit(ray, tMin, tMax);
        HitRecord rightHit = right.hit(ray, tMin, leftHit == null ? tMax : leftHit.t);
        return rightHit == null ? leftHit : rightHit;
    }

    @Override
    public AABB boundingBox(float t0, float t1) {
        return box;
    }

    @Override
    public Material getMaterial() {
        throw new UnsupportedOperationException("Y U material BVHNode?");
    }
}
