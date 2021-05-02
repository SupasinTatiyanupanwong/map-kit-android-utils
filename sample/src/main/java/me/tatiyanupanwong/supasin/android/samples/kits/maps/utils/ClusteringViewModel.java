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

package me.tatiyanupanwong.supasin.android.samples.kits.maps.utils;

import android.content.res.Resources;

import androidx.lifecycle.ViewModel;

import org.json.JSONException;

import java.io.InputStream;
import java.util.List;

import me.tatiyanupanwong.supasin.android.libraries.kits.maps.model.LatLng;
import me.tatiyanupanwong.supasin.android.libraries.kits.maps.utils.clustering.algo.NonHierarchicalViewBasedAlgorithm;
import me.tatiyanupanwong.supasin.android.samples.kits.maps.utils.model.MyItem;

public class ClusteringViewModel extends ViewModel {

    private NonHierarchicalViewBasedAlgorithm<MyItem> mAlgorithm =
            new NonHierarchicalViewBasedAlgorithm<>(0, 0);

    NonHierarchicalViewBasedAlgorithm<MyItem> getAlgorithm() {
        return mAlgorithm;
    }

    void readItems(Resources resources) throws JSONException {
        InputStream inputStream = resources.openRawResource(R.raw.radar_search);
        List<MyItem> items = new MyItemReader().read(inputStream);
        mAlgorithm.lock();
        try {
            for (int i = 0; i < 100; i++) {
                double offset = i / 60d;
                for (MyItem item : items) {
                    LatLng position = item.getPosition();
                    double lat = position.getLatitude() + offset;
                    double lng = position.getLongitude() + offset;
                    MyItem offsetItem = new MyItem(lat, lng);
                    mAlgorithm.addItem(offsetItem);
                }
            }
        } finally {
            mAlgorithm.unlock();
        }
    }
}
