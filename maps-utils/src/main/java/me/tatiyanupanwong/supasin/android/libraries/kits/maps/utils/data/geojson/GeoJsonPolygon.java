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

package me.tatiyanupanwong.supasin.android.libraries.kits.maps.utils.data.geojson;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.tatiyanupanwong.supasin.android.libraries.kits.maps.MapKit;
import me.tatiyanupanwong.supasin.android.libraries.kits.maps.model.LatLng;
import me.tatiyanupanwong.supasin.android.libraries.kits.maps.model.PatternItem;
import me.tatiyanupanwong.supasin.android.libraries.kits.maps.model.Polygon;
import me.tatiyanupanwong.supasin.android.libraries.kits.maps.utils.data.DataPolygon;

/**
 * A GeoJsonPolygon geometry contains an array of arrays of {@link LatLng}s. The first array is the
 * polygon exterior boundary. Subsequent arrays are holes.
 */

public class GeoJsonPolygon implements DataPolygon {

    private final static String GEOMETRY_TYPE = "Polygon";

    private final List<? extends List<LatLng>> mCoordinates;

    private final static int POLYGON_OUTER_COORDINATE_INDEX = 0;

    private final static int POLYGON_INNER_COORDINATE_INDEX = 1;

    /**
     * Creates a new GeoJsonPolygon object
     *
     * @param coordinates list of list of coordinates of GeoJsonPolygon to store
     */
    public GeoJsonPolygon(
            List<? extends List<LatLng>> coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }
        mCoordinates = coordinates;
    }

    /**
     * Gets the type of geometry. The type of geometry conforms to the GeoJSON 'type'
     * specification.
     *
     * @return type of geometry
     */
    public String getType() {
        return GEOMETRY_TYPE;
    }

    /**
     * Gets a list of a list of coordinates of the GeoJsonPolygons
     *
     * @return list of a list of coordinates of the GeoJsonPolygon
     */
    public List<? extends List<LatLng>> getCoordinates() {
        return mCoordinates;
    }

    /**
     * Gets the stored geometry object
     *
     * @return geometry object
     */
    public List<? extends List<LatLng>> getGeometryObject() {
        return getCoordinates();
    }

    /**
     * Gets the type of geometry
     *
     * @return type of geometry
     */
    public String getGeometryType() {
        return getType();
    }


    /**
     * Gets an array of outer boundary coordinates
     *
     * @return array of outer boundary coordinates
     */
    public ArrayList<LatLng> getOuterBoundaryCoordinates() {
        // First array of coordinates are the outline
        return (ArrayList<LatLng>) getCoordinates().get(POLYGON_OUTER_COORDINATE_INDEX);
    }

    /**
     * Gets an array of arrays of inner boundary coordinates
     *
     * @return array of arrays of inner boundary coordinates
     */
    public ArrayList<ArrayList<LatLng>> getInnerBoundaryCoordinates() {
        // Following arrays are holes
        ArrayList<ArrayList<LatLng>> innerBoundary = new ArrayList<>();
        for (int i = POLYGON_INNER_COORDINATE_INDEX; i < getCoordinates().size();
             i++) {
            innerBoundary.add((ArrayList<LatLng>) getCoordinates().get(i));
        }
        return innerBoundary;
    }

    @NonNull
    @Override
    public String toString() {
        return GEOMETRY_TYPE + "{" +
                "\n coordinates=" + mCoordinates +
                "\n}\n";
    }


    /**
     * A class that allows for GeoJsonPolygon objects to be styled and for these styles to be
     * translated into a PolygonOptions object.
     */
    public static class Style extends
            me.tatiyanupanwong.supasin.android.libraries.kits.maps.utils.data.Style implements
            GeoJsonStyle {
        private final static String[] GEOMETRY_TYPE =
                { "Polygon", "MultiPolygon", "GeometryCollection" };

        /**
         * Creates a new PolygonStyle object
         */
        public Style() {
            mPolygonOptions = MapKit.newPolygonOptions();
            mPolygonOptions.clickable(true);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String[] getGeometryType() {
            return GEOMETRY_TYPE;
        }

        /**
         * Gets the fill color of the GeoJsonPolygon as a 32-bit ARGB color
         *
         * @return fill color of the GeoJsonPolygon
         */
        public int getFillColor() {
            return mPolygonOptions.getFillColor();
        }

        /**
         * Sets the fill color of the GeoJsonPolygon as a 32-bit ARGB color
         *
         * @param fillColor fill color value of the GeoJsonPolygon
         */
        public void setFillColor(int fillColor) {
            setPolygonFillColor(fillColor);
            styleChanged();
        }

        /**
         * Gets whether the GeoJsonPolygon is geodesic.
         *
         * @return {@code true} if GeoJsonPolygon is geodesic, {@code false} if not geodesic
         */
        public boolean isGeodesic() {
            return mPolygonOptions.isGeodesic();
        }

        /**
         * Sets whether the GeoJsonPolygon is geodesic.
         *
         * @param geodesic {@code true} if GeoJsonPolygon is geodesic, {@code false} if not geodesic
         */
        public void setGeodesic(boolean geodesic) {
            mPolygonOptions.geodesic(geodesic);
            styleChanged();
        }

        /**
         * Gets the stroke color of the GeoJsonPolygon as a 32-bit ARGB color
         *
         * @return stroke color of the GeoJsonPolygon
         */
        public int getStrokeColor() {
            return mPolygonOptions.getStrokeColor();
        }

        /**
         * Sets the stroke color of the GeoJsonPolygon as a 32-bit ARGB color
         *
         * @param strokeColor stroke color value of the GeoJsonPolygon
         */
        public void setStrokeColor(int strokeColor) {
            mPolygonOptions.strokeColor(strokeColor);
            styleChanged();
        }

        /**
         * Gets the stroke joint type of the GeoJsonPolygon
         *
         * @return stroke joint type of the GeoJsonPolygon
         */
        public int getStrokeJointType() {
            return mPolygonOptions.getStrokeJointType();
        }

        /**
         * Sets the stroke joint type of the GeoJsonPolygon
         *
         * @param strokeJointType stroke joint type value of the GeoJsonPolygon
         */
        public void setStrokeJointType(int strokeJointType) {
            mPolygonOptions.strokeJointType(strokeJointType);
            styleChanged();
        }

        /**
         * Gets the stroke pattern of the GeoJsonPolygon as a list of pattern items
         *
         * @return stroke pattern of the GeoJsonPolygon
         */
        public List<PatternItem> getStrokePattern() {
            return mPolygonOptions.getStrokePattern();
        }

        /**
         * Sets the stroke pattern of the GeoJsonPolygon as a list of pattern items
         *
         * @param strokePattern stroke pattern value of the GeoJsonPolygon
         */
        public void setStrokePattern(List<PatternItem> strokePattern) {
            mPolygonOptions.strokePattern(strokePattern);
            styleChanged();
        }

        /**
         * Gets the stroke width of the GeoJsonPolygon in screen pixels
         *
         * @return stroke width of the GeoJsonPolygon
         */
        public float getStrokeWidth() {
            return mPolygonOptions.getStrokeWidth();
        }

        /**
         * Sets the stroke width of the GeoJsonPolygon in screen pixels
         *
         * @param strokeWidth stroke width value of the GeoJsonPolygon
         */
        public void setStrokeWidth(float strokeWidth) {
            setPolygonStrokeWidth(strokeWidth);
            styleChanged();
        }

        /**
         * Gets the z index of the GeoJsonPolygon
         *
         * @return z index of the GeoJsonPolygon
         */
        public float getZIndex() {
            return mPolygonOptions.getZIndex();
        }

        /**
         * Sets the z index of the GeoJsonPolygon
         *
         * @param zIndex z index value of the GeoJsonPolygon
         */
        public void setZIndex(float zIndex) {
            mPolygonOptions.zIndex(zIndex);
            styleChanged();
        }

        /**
         * Gets whether the GeoJsonPolygon is visible.
         *
         * @return {@code true} if GeoJsonPolygon is visible, {@code false} if not visible
         */
        @Override
        public boolean isVisible() {
            return mPolygonOptions.isVisible();
        }

        /**
         * Sets whether the GeoJsonPolygon is visible.
         *
         * @param visible {@code true} if GeoJsonPolygon is visible, {@code false} if not visible
         */
        @Override
        public void setVisible(boolean visible) {
            mPolygonOptions.visible(visible);
            styleChanged();
        }

        /**
         * Notifies the observers, GeoJsonFeature objects, that the style has changed. Indicates
         * to the GeoJsonFeature that it should check whether a redraw is needed for the feature.
         */
        private void styleChanged() {
            setChanged();
            notifyObservers();
        }

        /**
         * Gets a new PolygonOptions object containing styles for the GeoJsonPolygon
         *
         * @return new PolygonOptions object
         */
        public Polygon.Options toPolygonOptions() {
            Polygon.Options polygonOptions = MapKit.newPolygonOptions();
            polygonOptions.fillColor(mPolygonOptions.getFillColor());
            polygonOptions.geodesic(mPolygonOptions.isGeodesic());
            polygonOptions.strokeColor(mPolygonOptions.getStrokeColor());
            polygonOptions.strokeJointType(mPolygonOptions.getStrokeJointType());
            polygonOptions.strokePattern(mPolygonOptions.getStrokePattern());
            polygonOptions.strokeWidth(mPolygonOptions.getStrokeWidth());
            polygonOptions.visible(mPolygonOptions.isVisible());
            polygonOptions.zIndex(mPolygonOptions.getZIndex());
            polygonOptions.clickable(mPolygonOptions.isClickable());
            return polygonOptions;
        }

        @NonNull
        @Override
        public String toString() {
            return "PolygonStyle{" + "\n geometry type=" + Arrays.toString(GEOMETRY_TYPE) +
                    ",\n fill color=" + getFillColor() +
                    ",\n geodesic=" + isGeodesic() +
                    ",\n stroke color=" + getStrokeColor() +
                    ",\n stroke joint type=" + getStrokeJointType() +
                    ",\n stroke pattern=" + getStrokePattern() +
                    ",\n stroke width=" + getStrokeWidth() +
                    ",\n visible=" + isVisible() +
                    ",\n z index=" + getZIndex() +
                    ",\n clickable=" + isClickable() +
                    "\n}\n";
        }

        /**
         * Specifies whether this GeoJsonPolygon is clickable
         *
         * @param clickable - new clickability setting for the GeoJsonPolygon
         */
        public void setClickable(boolean clickable) {
            mPolygonOptions.clickable(clickable);
            styleChanged();
        }

        /**
         * Gets the clickability setting for this Options object.
         *
         * @return {@code true} if the GeoJsonPolygon is clickable; {@code false} if it is not
         */
        public boolean isClickable() {
            return mPolygonOptions.isClickable();
        }
    }

}
