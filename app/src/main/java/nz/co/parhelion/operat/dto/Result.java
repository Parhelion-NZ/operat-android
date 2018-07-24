package nz.co.parhelion.operat.dto;

public class Result {

    int resultId;
    int meshblockId;
    double operatScore;
    double naturalElementsScore;
    double incivilitiesScore;
    double navigationScore;
    double territorialScore;
    String centroid;
    String geom;
    double lat;
    double lng;

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getMeshblockId() {
        return meshblockId;
    }

    public void setMeshblockId(int meshblockId) {
        this.meshblockId = meshblockId;
    }

    public double getOperatScore() {
        return operatScore;
    }

    public void setOperatScore(double operatScore) {
        this.operatScore = operatScore;
    }

    public double getNaturalElementsScore() {
        return naturalElementsScore;
    }

    public void setNaturalElementsScore(double naturalElementsScore) {
        this.naturalElementsScore = naturalElementsScore;
    }

    public double getIncivilitiesScore() {
        return incivilitiesScore;
    }

    public void setIncivilitiesScore(double incivilitiesScore) {
        this.incivilitiesScore = incivilitiesScore;
    }

    public double getNavigationScore() {
        return navigationScore;
    }

    public void setNavigationScore(double navigationScore) {
        this.navigationScore = navigationScore;
    }

    public double getTerritorialScore() {
        return territorialScore;
    }

    public void setTerritorialScore(double territorialScore) {
        this.territorialScore = territorialScore;
    }

    public String getCentroid() {
        return centroid;
    }

    public void setCentroid(String centroid) {
        this.centroid = centroid;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
