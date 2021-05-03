/*
 * Copyright 2020 Supasin Tatiyanupanwong
 * Copyright 2013 Google Inc.
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

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.json.JSONException;

import java.io.InputStream;
import java.util.List;

import dev.supasintatiyanupanwong.libraries.android.kits.maps.MapKit;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.model.MapClient;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.model.Marker;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.utils.clustering.ClusterManager;
import dev.supasintatiyanupanwong.samples.android.kits.maps.utils.model.MyItem;

/**
 * Simple activity demonstrating ClusterManager.
 */
public class ClusteringDemoActivity extends BaseDemoActivity {
    private ClusterManager<MyItem> mClusterManager;

    @Override
    protected void startDemo(boolean isRestore) {
        if (!isRestore) {
            getMap().moveCamera(
                    MapKit.getCameraUpdateFactory()
                            .newLatLngZoom(MapKit.newLatLng(51.503186, -0.126446), 10)
            );
        }

        mClusterManager = new ClusterManager<>(this, getMap());
        getMap().setOnCameraIdleListener(mClusterManager);

        // Add a custom InfoWindowAdapter by setting it to the MarkerManager.Collection object from
        // ClusterManager rather than from GoogleMap.setInfoWindowAdapter
        mClusterManager.getMarkerCollection().setInfoWindowAdapter(
                new MapClient.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(@NonNull Marker marker) {
                        final View view =
                                getLayoutInflater().inflate(R.layout.custom_info_window, null);
                        final TextView textView = view.findViewById(R.id.textViewTitle);
                        String text =
                                (marker.getTitle() != null) ? marker.getTitle() : "Cluster Item";
                        textView.setText(text);
                        return view;
                    }

                    @Override
                    public View getInfoContents(@NonNull Marker marker) {
                        return null;
                    }
                });
        mClusterManager.getMarkerCollection().setOnInfoWindowClickListener(
                new MapClient.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(@NonNull Marker marker) {
                        Toast.makeText(
                                ClusteringDemoActivity.this,
                                "Info window clicked.",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                });

        try {
            readItems();
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
        }
    }

    private void readItems() throws JSONException {
        InputStream inputStream = getResources().openRawResource(R.raw.radar_search);
        List<MyItem> items = new MyItemReader().read(inputStream);
        mClusterManager.addItems(items);
    }
}
