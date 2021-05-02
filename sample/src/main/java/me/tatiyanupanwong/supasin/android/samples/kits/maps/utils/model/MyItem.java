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

package me.tatiyanupanwong.supasin.android.samples.kits.maps.utils.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import me.tatiyanupanwong.supasin.android.libraries.kits.maps.MapKit;
import me.tatiyanupanwong.supasin.android.libraries.kits.maps.model.LatLng;
import me.tatiyanupanwong.supasin.android.libraries.kits.maps.utils.clustering.ClusterItem;

public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    private String mTitle;
    private String mSnippet;

    public MyItem(double lat, double lng) {
        mPosition = MapKit.newLatLng(lat, lng);
        mTitle = null;
        mSnippet = null;
    }

    public MyItem(double lat, double lng, String title, String snippet) {
        mPosition = MapKit.newLatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Nullable
    @Override
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return mSnippet;
    }

    /**
     * Set the title of the marker
     * @param title string to be set as title
     */
    public void setTitle(@Nullable String title) {
        mTitle = title;
    }

    /**
     * Set the description of the marker
     * @param snippet string to be set as snippet
     */
    public void setSnippet(@Nullable String snippet) {
        mSnippet = snippet;
    }
}
