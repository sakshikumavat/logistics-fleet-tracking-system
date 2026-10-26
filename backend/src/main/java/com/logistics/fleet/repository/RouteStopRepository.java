
package com.logistics.fleet.repository;

import com.logistics.fleet.entity.RouteStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteStopRepository
        extends JpaRepository<RouteStop, Long> {

    List<RouteStop> findByRouteIdOrderByStopOrderAsc(Long routeId);

    @Query("SELECT COALESCE(MAX(s.stopId), 0) FROM RouteStop s")
    Long findMaxStopId();
}
