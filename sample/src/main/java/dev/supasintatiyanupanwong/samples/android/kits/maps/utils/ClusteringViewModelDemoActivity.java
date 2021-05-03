/*
 * Copyright 2020 Supasin Tatiyanupanwong
 * Copyright 2019 Google Inc.
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

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;

import dev.supasintatiyanupanwong.libraries.android.kits.maps.MapKit;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.utils.clustering.ClusterManager;
import dev.supasintatiyanupanwong.samples.android.kits.maps.utils.model.MyItem;

public class ClusteringViewModelDemoActivity extends BaseDemoActivity {
    private ClusterManager<MyItem> mClusterManager;
    private ClusteringViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.Factory() {
                    @NonNull
                    @Override
                    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                        //noinspection unchecked
                        return (T) new ClusteringViewModel();
                    }
                })
                .get(ClusteringViewModel.class);

        if (savedInstanceState == null) {
            try {
                mViewModel.readItems(getResources());
            } catch (JSONException e) {
                Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void startDemo(boolean isRestore) {
        if (!isRestore) {
            getMap().moveCamera(
                    MapKit.getCameraUpdateFactory()
                            .newLatLngZoom(MapKit.newLatLng(51.503186, -0.126446), 10)
            );
        }

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mViewModel.getAlgorithm().updateViewSize(metrics.widthPixels, metrics.heightPixels);

        mClusterManager = new ClusterManager<>(this, getMap());
        mClusterManager.setAlgorithm(mViewModel.getAlgorithm());

        getMap().setOnCameraIdleListener(mClusterManager);
    }
}
