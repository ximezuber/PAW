package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.ImageDao;
import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.Image;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class ImageDaoImpl implements ImageDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Image createProfileImage(byte[] image, Doctor doctor) {
        Doctor doc = entityManager.merge(doctor);
        Image im = new Image(doc, image);
        entityManager.persist(im);
        return im;
    }

    @Override
    public void deleteProfileImage(Image profileImage) {
        Image contextImage = entityManager.merge(profileImage);
        entityManager.remove(contextImage);
    }

    @Override
    public Optional<Image> getProfileImageByLicense(String license){
        TypedQuery<Image> query = entityManager.createQuery("FROM Image AS image " +
                "WHERE image.doctor.license = :license", Image.class);
        query.setParameter("license", license);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<Image> getProfileImageById(int id) {
        Image image = entityManager.find(Image.class, id);
        return Optional.ofNullable(image);
    }
}
