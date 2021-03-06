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

package dev.supasintatiyanupanwong.samples.android.kits.maps.utils;

import android.util.DisplayMetrics;
import android.widget.Toast;

import org.json.JSONException;

import java.io.InputStream;
import java.util.List;

import dev.supasintatiyanupanwong.libraries.android.kits.maps.MapKit;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.model.LatLng;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.utils.clustering.ClusterManager;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.utils.clustering.algo.NonHierarchicalViewBasedAlgorithm;
import dev.supasintatiyanupanwong.samples.android.kits.maps.utils.model.MyItem;

public class VisibleClusteringDemoActivity extends BaseDemoActivity {
    private ClusterManager<MyItem> mClusterManager;

    @Override
    protected void startDemo(boolean isRestore) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        if (!isRestore) {
            getMap().moveCamera(
                    MapKit.getCameraUpdateFactory()
                            .newLatLngZoom(MapKit.newLatLng(51.503186, -0.126446), 10)
            );
        }

        mClusterManager = new ClusterManager<>(this, getMap());
        mClusterManager.setAlgorithm(new NonHierarchicalViewBasedAlgorithm<MyItem>(
                metrics.widthPixels, metrics.heightPixels));

        getMap().setOnCameraIdleListener(mClusterManager);

        try {
            readItems();
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
        }
    }

    private void readItems() throws JSONException {
        InputStream inputStream = getResources().openRawResource(R.raw.radar_search);
        List<MyItem> items = new MyItemReader().read(inputStream);
        for (int i = 0; i < 100; i++) {
            double offset = i / 60d;
            for (MyItem item : items) {
                LatLng position = item.getPosition();
                double lat = position.getLatitude() + offset;
                double lng = position.getLongitude() + offset;
                MyItem offsetItem = new MyItem(lat, lng);
                mClusterManager.addItem(offsetItem);
            }
        }
    }
}
