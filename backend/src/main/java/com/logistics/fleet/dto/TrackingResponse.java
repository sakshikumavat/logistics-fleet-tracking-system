
package com.logistics.fleet.dto;

import java.util.List;
import java.util.Map;

public class TrackingResponse {

    private Map<String, Object> shipment;
    private List<Map<String, Object>> stops;

    public TrackingResponse() {
    }

    public TrackingResponse(
            Map<String, Object> shipment,
            List<Map<String, Object>> stops) {
        this.shipment = shipment;
        this.stops = stops;
    }

    public Map<String, Object> getShipment() {
        return shipment;
    }

    public void setShipment(Map<String, Object> shipment) {
        this.shipment = shipment;
    }

    public List<Map<String, Object>> getStops() {
        return stops;
    }

    public void setStops(List<Map<String, Object>> stops) {
        this.stops = stops;
    }
}
