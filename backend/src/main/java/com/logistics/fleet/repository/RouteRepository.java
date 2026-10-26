
package com.logistics.fleet.repository;

import com.logistics.fleet.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    @Query("SELECT COALESCE(MAX(r.routeId), 0) FROM Route r")
    Long findMaxRouteId();
}
