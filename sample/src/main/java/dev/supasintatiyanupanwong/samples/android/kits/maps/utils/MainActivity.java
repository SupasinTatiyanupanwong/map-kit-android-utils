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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewGroup mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mListView = findViewById(R.id.list);

        addDemo("Clustering", ClusteringDemoActivity.class);
        addDemo("Clustering: Custom Look", CustomMarkerClusteringDemoActivity.class);
        addDemo("Clustering: 2K markers", BigClusteringDemoActivity.class);
        addDemo("Clustering: 20K only visible markers", VisibleClusteringDemoActivity.class);
        addDemo("Clustering: ViewModel", ClusteringViewModelDemoActivity.class);
        addDemo("PolyUtil.decode", PolyDecodeDemoActivity.class);
        addDemo("PolyUtil.simplify", PolySimplifyDemoActivity.class);
        addDemo("IconGenerator", IconGeneratorDemoActivity.class);
        addDemo("SphericalUtils.computeDistanceBetween", DistanceDemoActivity.class);
        addDemo("Generating tiles", TileProviderAndProjectionDemo.class);
        addDemo("Heatmaps", HeatmapsDemoActivity.class);
        addDemo("GeoJSON Layer", GeoJsonDemoActivity.class);
        addDemo("KML Layer Overlay", KmlDemoActivity.class);
        addDemo("Multi Layer", MultiLayerDemoActivity.class);
    }

    private void addDemo(String demoName, Class<? extends Activity> activityClass) {
        Button button = new Button(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        button.setLayoutParams(layoutParams);
        button.setText(demoName);
        button.setTag(activityClass);
        button.setOnClickListener(this);
        mListView.addView(button);
    }

    @Override
    public void onClick(View view) {
        Class<?> activityClass = (Class<?>) view.getTag();
        startActivity(new Intent(this, activityClass));
    }
}
