/*
 * Copyright 2020 Supasin Tatiyanupanwong
 * Copyright 2020 Google Inc.
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

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import dev.supasintatiyanupanwong.libraries.android.kits.maps.MapKit;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.model.LatLng;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.model.LatLngBounds;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.model.MapClient;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.utils.collections.GroundOverlayManager;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.utils.collections.MarkerManager;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.utils.collections.PolygonManager;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.utils.collections.PolylineManager;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.utils.data.Feature;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.utils.data.Layer;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.utils.data.Renderer;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.utils.data.kml.KmlContainer;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.utils.data.kml.KmlLayer;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.utils.data.kml.KmlPlacemark;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.utils.data.kml.KmlPolygon;

public class KmlDemoActivity extends BaseDemoActivity {

    private MapClient mMap;
    private boolean mIsRestore;

    protected int getLayoutId() {
        return R.layout.kml_demo;
    }

    public void startDemo (boolean isRestore) {
        mIsRestore = isRestore;
        try {
            mMap = getMap();
            //retrieveFileFromResource();
            retrieveFileFromUrl();
        } catch (Exception e) {
            Log.e("Exception caught", e.toString());
        }
    }

    private void retrieveFileFromResource() {
        new LoadLocalKmlFile(R.raw.campus).execute();
    }

    private void retrieveFileFromUrl() {
        new DownloadKmlFile(getString(R.string.kml_url)).execute();
    }

    private void moveCameraToKml(KmlLayer kmlLayer) {
        if (mIsRestore) return;
        try {
            //Retrieve the first container in the KML layer
            KmlContainer container = kmlLayer.getContainers().iterator().next();
            //Retrieve a nested container within the first container
            container = container.getContainers().iterator().next();
            //Retrieve the first placemark in the nested container
            KmlPlacemark placemark = container.getPlacemarks().iterator().next();
            //Retrieve a polygon object in a placemark
            KmlPolygon polygon = (KmlPolygon) placemark.getGeometry();
            //Create LatLngBounds of the outer coordinates of the polygon
            LatLngBounds.Builder builder = MapKit.newLatLngBoundsBuilder();
            for (LatLng latLng : polygon.getOuterBoundaryCoordinates()) {
                builder.include(latLng);
            }

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            getMap().moveCamera(
                    MapKit.getCameraUpdateFactory()
                            .newLatLngBounds(builder.build(), width, height, 1));
        } catch (Exception e) {
            // may fail depending on the KML being shown
            e.printStackTrace();
        }
    }

    private Renderer.ImagesCache getImagesCache() {
        final RetainFragment retainFragment =
                RetainFragment.findOrCreateRetainFragment(getSupportFragmentManager());
        return retainFragment.mImagesCache;
    }

    /**
     * Fragment for retaining the bitmap cache between configuration changes.
     */
    public static class RetainFragment extends Fragment {
        private static final String TAG = RetainFragment.class.getName();
        Renderer.ImagesCache mImagesCache;

        static RetainFragment findOrCreateRetainFragment(FragmentManager fm) {
            RetainFragment fragment = (RetainFragment) fm.findFragmentByTag(TAG);
            if (fragment == null) {
                fragment = new RetainFragment();
                fm.beginTransaction().add(fragment, TAG).commit();
            }
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }
    }

    private class LoadLocalKmlFile extends AsyncTask<String, Void, KmlLayer> {
        private final int mResourceId;

        LoadLocalKmlFile(int resourceId) {
            mResourceId = resourceId;
        }

        @Override
        protected KmlLayer doInBackground(String... strings) {
            try {
                return new KmlLayer(mMap, mResourceId, KmlDemoActivity.this);
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(KmlLayer kmlLayer) {
            addKmlToMap(kmlLayer);
        }
    }

    private class DownloadKmlFile extends AsyncTask<String, Void, KmlLayer> {
        private final String mUrl;

        DownloadKmlFile(String url) {
            mUrl = url;
        }

        protected KmlLayer doInBackground(String... params) {
            try {
                InputStream is =  new URL(mUrl).openStream();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[16384];
                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                try {
                    return new KmlLayer(mMap,
                            new ByteArrayInputStream(buffer.toByteArray()),
                            KmlDemoActivity.this,
                            new MarkerManager(mMap),
                            new PolygonManager(mMap),
                            new PolylineManager(mMap),
                            new GroundOverlayManager(mMap),
                            getImagesCache());
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(KmlLayer kmlLayer) {
            addKmlToMap(kmlLayer);
        }
    }

    private void addKmlToMap(KmlLayer kmlLayer) {
        if (kmlLayer != null) {
            kmlLayer.addLayerToMap();
            kmlLayer.setOnFeatureClickListener(new Layer.OnFeatureClickListener() {
                @Override
                public void onFeatureClick(Feature feature) {
                    Toast.makeText(KmlDemoActivity.this,
                            "Feature clicked: " + feature.getId(),
                            Toast.LENGTH_SHORT).show();
                }
            });
            moveCameraToKml(kmlLayer);
        }
    }
}
