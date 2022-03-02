package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.LocationDao;
import ar.edu.itba.paw.interfaces.service.ClinicService;
import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityDependencyException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class LocationServiceImplTest {
    private static final String name = "location";

    private static final String name2 = "location2";

    @InjectMocks
    private LocationServiceImpl locationService = new LocationServiceImpl();

    @Mock
    private LocationDao mockDao;

    @Mock
    private ClinicService clinicService;

    @Test
    public void testCreate() throws DuplicateEntityException {
        //Set Up
        Mockito.when(mockDao.getLocationByName(Mockito.eq(name)))
                        .thenReturn(null);
        Mockito.when(mockDao.createLocation(Mockito.eq(name)))
                .thenReturn(new Location(name));

        //Execute
        Location location = locationService.createLocation(name);

        //Assert
        Assert.assertNotNull(location);
        Assert.assertEquals(name, location.getLocationName());
    }

    @Test(expected = DuplicateEntityException.class)
    public void testCreateDuplicate() throws DuplicateEntityException {
        //Set Up
        Mockito.when(mockDao.getLocationByName(Mockito.eq(name)))
                .thenReturn(new Location(name));

        //Execute
        locationService.createLocation(name);
    }

    @Test
    public void testGetLocationByName(){
        //Set Up
        Mockito.when(mockDao.getLocationByName(Mockito.eq(name)))
                .thenReturn(new Location(name));

        //Execute
        Location location = locationService.getLocationByName(name);

        //Assert
        Assert.assertNotNull(location);
        Assert.assertEquals(name, location.getLocationName());
    }

    @Test
    public void testDelete() throws EntityDependencyException, EntityNotFoundException {
        //Set Up
        Location location = new Location(name);
        Mockito.when(mockDao.getLocationByName(Mockito.eq(name)))
                .thenReturn(location);
        Mockito.when(clinicService.getClinicsByLocation(location))
                .thenReturn(Collections.emptyList());

        // Execute
        locationService.deleteLocation(name);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteNoLocation() throws EntityDependencyException, EntityNotFoundException {
        //Set Up
        Mockito.when(mockDao.getLocationByName(Mockito.eq(name)))
                .thenReturn(null);

        // Execute
        locationService.deleteLocation(name);
    }

    @Test(expected = EntityDependencyException.class)
    public void testDeleteClinicsWithLocation() throws EntityDependencyException, EntityNotFoundException {
        //Set Up
        Location location = new Location(name);
        Mockito.when(mockDao.getLocationByName(Mockito.eq(name)))
                .thenReturn(location);
        Clinic clinic = new Clinic(0, "name", "address", location);
        Mockito.when(clinicService.getClinicsByLocation(location))
                .thenReturn(Collections.singletonList(clinic));

        // Execute
        locationService.deleteLocation(name);
    }

}
