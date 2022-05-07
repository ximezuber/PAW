package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.ImageDao;
import ar.edu.itba.paw.interfaces.service.ImageService;
import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageDao imageDao;

    @Transactional
    @Override
    public Image createProfileImage(byte[] image, Doctor doctor) {
        return imageDao.createProfileImage(image, doctor);
    }

    @Transactional
    @Override
    public void deleteProfileImage(Image image) {
        imageDao.deleteProfileImage(image);
    }

    @Override
    public Optional<Image> getProfileImage(int id) {
        return imageDao.getProfileImageById(id);
    }

    @Override
    public Optional<Image> getImageByLicense(String license) {
        return imageDao.getProfileImageByLicense(license);
    }


}
