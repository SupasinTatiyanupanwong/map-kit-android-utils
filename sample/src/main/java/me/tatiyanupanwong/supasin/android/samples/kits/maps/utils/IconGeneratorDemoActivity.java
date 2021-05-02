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

package me.tatiyanupanwong.supasin.android.samples.kits.maps.utils;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;

import me.tatiyanupanwong.supasin.android.libraries.kits.maps.MapKit;
import me.tatiyanupanwong.supasin.android.libraries.kits.maps.model.LatLng;
import me.tatiyanupanwong.supasin.android.libraries.kits.maps.model.Marker;
import me.tatiyanupanwong.supasin.android.libraries.kits.maps.utils.ui.IconGenerator;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.ITALIC;

public class IconGeneratorDemoActivity extends BaseDemoActivity {

    @Override
    protected void startDemo(boolean isRestore) {
        if (!isRestore) {
            getMap().moveCamera(
                    MapKit.getCameraUpdateFactory()
                            .newLatLngZoom(MapKit.newLatLng(-33.8696, 151.2094), 10)
            );
        }

        IconGenerator iconFactory = new IconGenerator(this);
        addIcon(iconFactory, "Default", MapKit.newLatLng(-33.8696, 151.2094));

        iconFactory.setColor(Color.CYAN);
        addIcon(iconFactory, "Custom color", MapKit.newLatLng(-33.9360, 151.2070));

        iconFactory.setRotation(90);
        iconFactory.setStyle(IconGenerator.STYLE_RED);
        addIcon(iconFactory, "Rotated 90 degrees", MapKit.newLatLng(-33.8858, 151.096));

        iconFactory.setContentRotation(-90);
        iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
        addIcon(iconFactory, "Rotate=90, ContentRotate=-90", MapKit.newLatLng(-33.9992, 151.098));

        iconFactory.setRotation(0);
        iconFactory.setContentRotation(90);
        iconFactory.setStyle(IconGenerator.STYLE_GREEN);
        addIcon(iconFactory, "ContentRotate=90", MapKit.newLatLng(-33.7677, 151.244));

        iconFactory.setRotation(0);
        iconFactory.setContentRotation(0);
        iconFactory.setStyle(IconGenerator.STYLE_ORANGE);
        addIcon(iconFactory, makeCharSequence(), MapKit.newLatLng(-33.77720, 151.12412));
    }

    private void addIcon(IconGenerator iconFactory, CharSequence text, LatLng position) {
        Marker.Options markerOptions = MapKit.newMarkerOptions().
                icon(MapKit.getBitmapDescriptorFactory().fromBitmap(iconFactory.makeIcon(text))).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        getMap().addMarker(markerOptions);
    }

    private CharSequence makeCharSequence() {
        String prefix = "Mixing ";
        String suffix = "different fonts";
        String sequence = prefix + suffix;
        SpannableStringBuilder ssb = new SpannableStringBuilder(sequence);
        ssb.setSpan(
                new StyleSpan(ITALIC),
                0,
                prefix.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(
                new StyleSpan(BOLD),
                prefix.length(),
                sequence.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }
}
