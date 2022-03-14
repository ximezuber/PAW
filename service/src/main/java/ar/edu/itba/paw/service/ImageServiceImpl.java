package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.ImageDao;
import ar.edu.itba.paw.interfaces.service.ImageService;
import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Component
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageDao imageDao;

    @Transactional
    @Override
    public void createProfileImage(byte[] image, Doctor doctor) {
        imageDao.createProfileImage(image, doctor);
    }

    @Transactional
    @Override
    public void deleteProfileImage(String license) {
        Image img = getProfileImage(license);
        if(img != null) {
            imageDao.deleteProfileImage(img);
        }
    }

    @Override
    public Image getProfileImage(String license) {
        return imageDao.getProfileImage(license);
    }
}
