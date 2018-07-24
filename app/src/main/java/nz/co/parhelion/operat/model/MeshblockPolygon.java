package nz.co.parhelion.operat.model;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import java.util.ArrayList;
import java.util.List;

import nz.co.parhelion.operat.DisplayActivity;
import nz.co.parhelion.operat.R;
import nz.co.parhelion.operat.dto.Result;

public class MeshblockPolygon  {

    private PolygonOptions polygon;

    private Result result;

    private static final int GREEN = Color.argb(150,0,200,0);
    private static final int ORANGE = Color.argb(150, 200,160,0);
    private static final int RED = Color.argb(150, 255,0,0);


    public MeshblockPolygon(Result result) throws ParseException {
        this.result = result;
        polygon = createPoly();
    }

    private PolygonOptions createPoly() throws ParseException {
        GeometryFactory geometryFactory = new GeometryFactory();
        WKTReader reader = new WKTReader(geometryFactory);
        PolygonOptions polyOptions = null;
        List<String> featureList = new ArrayList<>();
        Geometry geom = null;
        Polygon polygon = null;
        Coordinate[] outerCoordinates = null;
        Coordinate[] innerCoordinates = null;
        ArrayList<LatLng> outer = null;
        ArrayList<LatLng> inner = null;

        geom = reader.read(result.getGeom());

        //Gets each polygon of a multipolygon
        for (int i = 0; i < geom.getNumGeometries(); i++) {
            outer = new ArrayList<LatLng>();
            polyOptions = new PolygonOptions();
            polygon = (Polygon) geom.getGeometryN(i);

            //Gets each polygon outer coordinates
            outerCoordinates = polygon.getExteriorRing().getCoordinates();
            for (Coordinate outerCoordinate : outerCoordinates) {
                outer.add(new LatLng(outerCoordinate.y, outerCoordinate.x));
            }
            polyOptions.addAll(outer);

            //Getting each polygon interior coordinates (hole)  if they exist
            if (polygon.getNumInteriorRing() > 0) {
                for (int j = 0; j < polygon.getNumInteriorRing(); j++) {
                    inner = new ArrayList<LatLng>();
                    innerCoordinates = polygon.getInteriorRingN(j).getCoordinates();
                    for (Coordinate innerCoordinate : innerCoordinates) {
                        inner.add(new LatLng(innerCoordinate.y, innerCoordinate.x));
                    }
                    polyOptions.addHole(inner);
                }

            }

            polyOptions.strokeWidth(2);

            return polyOptions;
        }
        //TODO we're assuming that they'll always be a single poly... hopefully!!
        return null;
    }

    public void showOperatScore() {
        int color = RED;

        if (result.getOperatScore() < 33) {
            color = GREEN;
        } else if (result.getOperatScore() < 66) {
            color = ORANGE;
        }

        System.out.println("Operat score is "+result.getOperatScore()+" so color is "+color);
        polygon.strokeColor(R.color.black);
        polygon.fillColor(color);

    }

    public void showNaturalElementsScore() {
        int color = RED;

        if (result.getNaturalElementsScore() < 7) {
            color = GREEN;
        } else if (result.getNaturalElementsScore() < 14) {
            color = ORANGE;
        }

        System.out.println("Natural elements score is "+result.getNaturalElementsScore());
        System.out.println("So color is "+color);

        polygon.strokeColor(R.color.black);
        polygon.fillColor(color);

    }

    public void showIncivilitiesScore() {
        int color = RED;

        if (result.getIncivilitiesScore() < 7) {
            color = GREEN;
        } else if (result.getIncivilitiesScore() < 14) {
            color = ORANGE;
        }
        polygon.strokeColor(R.color.black);
        polygon.fillColor(color);
    }

    public void showNavigationScore() {


        int color = RED;

        if (result.getNavigationScore() < 13) {
            color = GREEN;
        } else if (result.getNavigationScore() < 27) {
            color = ORANGE;
        }
        polygon.strokeColor(R.color.black);
        polygon.fillColor(color);
        System.out.println("Navigation score is "+result.getNavigationScore()+" so color is "+color);

    }

    public void showTerritorialScore() {
        int color = RED;

        if (result.getTerritorialScore() < 7) {
            color = GREEN;
        } else if (result.getTerritorialScore() < 14) {
            color = ORANGE;
        }
        polygon.strokeColor(R.color.black);
        polygon.fillColor(color);

    }

    public PolygonOptions getPolygon() {
        return polygon;
    }

    public void showScore(DisplayActivity.Scores showingScores) {
        switch (showingScores) {
            case OPERAT:
                showOperatScore();
                return;
            case NATURAL_ELEMENTS:
                showNaturalElementsScore();
                return;
            case INCIVILITIES:
                showIncivilitiesScore();
                return;
            case NAVIGATION:
                showNavigationScore();
                return;
            case TERRITORIAL:
                showTerritorialScore();
                return;

        }
    }
}
