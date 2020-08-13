/*
 * Copyright 2020 Supasin Tatiyanupanwong
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.tatiyanupanwong.supasin.android.samples.kits.maps.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import me.tatiyanupanwong.supasin.android.libraries.kits.maps.MapKit;
import me.tatiyanupanwong.supasin.android.libraries.kits.maps.model.LatLng;
import me.tatiyanupanwong.supasin.android.libraries.kits.maps.model.Tile;
import me.tatiyanupanwong.supasin.android.libraries.kits.maps.model.TileProvider;
import me.tatiyanupanwong.supasin.android.libraries.kits.maps.utils.geometry.Point;
import me.tatiyanupanwong.supasin.android.libraries.kits.maps.utils.projection.SphericalMercatorProjection;

public class TileProviderAndProjectionDemo extends BaseDemoActivity {

    @Override
    protected void startDemo(boolean isRestore) {
        PointTileOverlay pto = new PointTileOverlay();
        pto.addPoint(MapKit.newLatLng(0, 0));
        pto.addPoint(MapKit.newLatLng(21, -10));
        getMap().addTileOverlay(MapKit.newTileOverlayOptions().tileProvider(pto));
    }

    private class PointTileOverlay implements TileProvider {
        private List<Point> mPoints = new ArrayList<>();
        private int mTileSize = 256;
        private SphericalMercatorProjection mProjection = new SphericalMercatorProjection(mTileSize);
        private int mScale = 2;
        private int mDimension = mScale * mTileSize;

        @Override
        public Tile getTile(int x, int y, int zoom) {
            Matrix matrix = new Matrix();
            float scale = (float) Math.pow(2, zoom) * mScale;
            matrix.postScale(scale, scale);
            matrix.postTranslate(-x * mDimension, -y * mDimension);

            Bitmap bitmap = Bitmap.createBitmap(mDimension, mDimension, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bitmap);
            c.setMatrix(matrix);

            for (Point p : mPoints) {
                c.drawCircle((float) p.x, (float) p.y, 1, new Paint());
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return MapKit.newTile(mDimension, mDimension, baos.toByteArray());
        }

        public void addPoint(LatLng latLng) {
            mPoints.add(mProjection.toPoint(latLng));
        }
    }
}
