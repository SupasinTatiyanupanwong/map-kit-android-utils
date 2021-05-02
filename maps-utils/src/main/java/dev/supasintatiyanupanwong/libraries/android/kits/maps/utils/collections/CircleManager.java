/*
 * Copyright 2020 Supasin Tatiyanupanwong
 * Copyright 2017 Google Inc.
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

package dev.supasintatiyanupanwong.libraries.android.kits.maps.utils.collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import dev.supasintatiyanupanwong.libraries.android.kits.maps.model.Circle;
import dev.supasintatiyanupanwong.libraries.android.kits.maps.model.MapClient;

/**
 * Keeps track of collections of circles on the map. Delegates all {@link Circle}-related events
 * to each collection's individually managed listeners.
 * <p/>
 * All circle operations (adds and removes) should occur via its {@linkplain Collection collection}
 * class. That is, don't add a circle via a collection, then remove it via {@link Circle#remove()}
 */
public class CircleManager extends
        MapObjectManager<Circle, CircleManager.Collection> implements
        MapClient.OnCircleClickListener {

    public CircleManager(@NonNull MapClient map) {
        super(map);
    }

    @Override
    void setListenersOnUiThread() {
        if (mMap != null) {
            mMap.setOnCircleClickListener(this);
        }
    }

    @Override
    public Collection newCollection() {
        return new Collection();
    }

    @Override
    protected void removeObjectFromMap(@NonNull Circle object) {
        object.remove();
    }

    @Override
    public void onCircleClick(@NonNull Circle circle) {
        Collection collection = mAllObjects.get(circle);
        if (collection != null && collection.mCircleClickListener != null) {
            collection.mCircleClickListener.onCircleClick(circle);
        }
    }


    public class Collection extends MapObjectManager.Collection {
        private MapClient.OnCircleClickListener mCircleClickListener;

        public Collection() {}

        public Circle addCircle(Circle.Options opts) {
            Circle circle = mMap.addCircle(opts);
            super.add(circle);
            return circle;
        }

        public void addAll(java.util.Collection<Circle.Options> opts) {
            for (Circle.Options opt : opts) {
                addCircle(opt);
            }
        }

        public void addAll(java.util.Collection<Circle.Options> opts, boolean defaultVisible) {
            for (Circle.Options opt : opts) {
                addCircle(opt).setVisible(defaultVisible);
            }
        }

        public void showAll() {
            for (Circle circle : getCircles()) {
                circle.setVisible(true);
            }
        }

        public void hideAll() {
            for (Circle circle : getCircles()) {
                circle.setVisible(false);
            }
        }

        public boolean remove(Circle circle) {
            return super.remove(circle);
        }

        public java.util.Collection<Circle> getCircles() {
            return getObjects();
        }

        public void setOnCircleClickListener(
                @Nullable MapClient.OnCircleClickListener circleClickListener) {
            mCircleClickListener = circleClickListener;
        }
    }

}
