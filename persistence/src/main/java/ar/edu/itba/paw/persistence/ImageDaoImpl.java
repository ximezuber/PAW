package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.ImageDao;
import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.Image;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ImageDaoImpl implements ImageDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void createProfileImage(byte[] image, Doctor doctor) {
        Doctor doc = entityManager.find(Doctor.class, doctor.getLicense());
        Image im = new Image(doc, image);
        entityManager.persist(im);
    }

    @Override
    public void deleteProfileImage(Image profileImage) {
        entityManager.remove(profileImage);
    }

    @Override
    public Image getProfileImage(String doctor){
        TypedQuery<Image> query = entityManager.createQuery("from Image as image where image.doctor.license = :doctor",Image.class);
        query.setParameter("doctor",doctor);
        List<Image> list = query.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
}
