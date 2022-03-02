package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.PrepaidDao;
import ar.edu.itba.paw.interfaces.service.PatientService;
import ar.edu.itba.paw.model.Prepaid;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class PrepaidServiceImplTest {
    private final static String name = "name";

    @InjectMocks
    private PrepaidServiceImpl prepaidService = new PrepaidServiceImpl();

    @Mock
    private PrepaidDao prepaidDao;

    @Mock
    private PatientService patientService;

    @Test
    public void testCreate() throws DuplicateEntityException {
        // Set up
        Mockito.when(prepaidDao.getPrepaidByName(name))
                .thenReturn(null);
        Mockito.when(prepaidDao.createPrepaid(name))
                .thenReturn(new Prepaid(name));

        // Execute
        Prepaid prepaid = prepaidService.createPrepaid(name);

        // Assert
        Assert.assertEquals(name, prepaid.getName());
    }

    @Test(expected = DuplicateEntityException.class)
    public void testCreateDuplicate() throws DuplicateEntityException {
        // Set up
        Mockito.when(prepaidDao.getPrepaidByName(name))
                .thenReturn(new Prepaid(name));

        // Execute
        prepaidService.createPrepaid(name);
    }

    @Test
    public void testDelete() throws EntityNotFoundException {
        // Set up
        Mockito.when(prepaidDao.getPrepaidByName(name))
                .thenReturn(new Prepaid(name));
        Mockito.when(patientService.getPatientsByPrepaid(name))
                .thenReturn(Collections.emptyList());

        // Execute
       prepaidService.deletePrepaid(name);

    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteNoPrepaid() throws EntityNotFoundException {
        // Set up
        Mockito.when(prepaidDao.getPrepaidByName(name))
                .thenReturn(null);

        // Execute
        prepaidService.deletePrepaid(name);

    }
}
