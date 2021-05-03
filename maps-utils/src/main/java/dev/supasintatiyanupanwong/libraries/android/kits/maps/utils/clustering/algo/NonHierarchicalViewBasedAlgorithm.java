/*
 * Copyright 2020 Supasin Tatiyanupanwong
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.supasintatiyanupanwong.libraries.android.kits.maps.utils.clustering.algo;

import java.util.ArrayList;
import java.util.Collection;

import dev.supasintatiyanupanwong.libraries.android.kits.maps.model.CameraPosition;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.model.LatLng;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.utils.clustering.ClusterItem;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.utils.geometry.Bounds;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.utils.geometry.Point;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.utils.geometry.PointQuadTree;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.utils.projection.SphericalMercatorProjection;

/**
 * This algorithm works the same way as {@link NonHierarchicalDistanceBasedAlgorithm} but works,
 * only in visible area. It requires to be reclustered on camera movement because clustering is
 * done only for visible area.
 *
 * @param <T>
 */
public class NonHierarchicalViewBasedAlgorithm<T extends ClusterItem>
        extends NonHierarchicalDistanceBasedAlgorithm<T> implements ScreenBasedAlgorithm<T> {

    private static final SphericalMercatorProjection PROJECTION =
            new SphericalMercatorProjection(1);

    private int mViewWidth;
    private int mViewHeight;

    private LatLng mMapCenter;

    public NonHierarchicalViewBasedAlgorithm(int screenWidth, int screenHeight) {
        mViewWidth = screenWidth;
        mViewHeight = screenHeight;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        mMapCenter = cameraPosition.getTarget();
    }

    @Override
    protected Collection<QuadItem<T>> getClusteringItems(
            PointQuadTree<QuadItem<T>> quadTree, float zoom) {
        Bounds visibleBounds = getVisibleBounds(zoom);
        Collection<QuadItem<T>> items = new ArrayList<>();

        // Handle wrapping around international date line
        if (visibleBounds.minX < 0) {
            Bounds wrappedBounds =
                    new Bounds(visibleBounds.minX + 1, 1, visibleBounds.minY, visibleBounds.maxY);
            items.addAll(quadTree.search(wrappedBounds));
            visibleBounds =
                    new Bounds(0, visibleBounds.maxX, visibleBounds.minY, visibleBounds.maxY);
        }
        if (visibleBounds.maxX > 1) {
            Bounds wrappedBounds =
                    new Bounds(0, visibleBounds.maxX - 1, visibleBounds.minY, visibleBounds.maxY);
            items.addAll(quadTree.search(wrappedBounds));
            visibleBounds =
                    new Bounds(visibleBounds.minX, 1, visibleBounds.minY, visibleBounds.maxY);
        }
        items.addAll(quadTree.search(visibleBounds));

        return items;
    }

    @Override
    public boolean shouldReclusterOnMapMovement() {
        return true;
    }

    /**
     * Update view width and height in case map size was changed.
     * You need to recluster all the clusters, to update view state after view size changes.
     *
     * @param width  map width
     * @param height map height
     */
    public void updateViewSize(int width, int height) {
        mViewWidth = width;
        mViewHeight = height;
    }

    private Bounds getVisibleBounds(float zoom) {
        if (mMapCenter == null) {
            return new Bounds(0, 0, 0, 0);
        }

        Point p = PROJECTION.toPoint(mMapCenter);

        final double halfWidthSpan = mViewWidth / Math.pow(2, zoom) / 256 / 2;
        final double halfHeightSpan = mViewHeight / Math.pow(2, zoom) / 256 / 2;

        return new Bounds(
                p.x - halfWidthSpan, p.x + halfWidthSpan,
                p.y - halfHeightSpan, p.y + halfHeightSpan);
    }
}
