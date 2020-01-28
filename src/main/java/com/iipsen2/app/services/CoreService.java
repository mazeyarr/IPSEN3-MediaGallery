package com.iipsen2.app.services;

import com.iipsen2.app.MainDao;
import com.iipsen2.app.MainService;
import com.iipsen2.app.helpers.CoreHelper;
import com.iipsen2.app.interfaces.enums.ModuleType;
import com.iipsen2.app.modules.Education.EducationModule;
import com.iipsen2.app.modules.Institute.InstituteModule;
import com.iipsen2.app.modules.Project.ProjectModule;
import com.iipsen2.app.modules.Tag.TagModule;
import com.iipsen2.app.modules.Upload.ResourceModule;
import com.iipsen2.app.modules.User.UserModule;
import com.iipsen2.app.services.Amazon.S3EncryptedService;
import com.iipsen2.app.services.Amazon.S3Service;

public class CoreService extends CoreHelper {
    protected static <T extends MainDao> T getDao(ModuleType moduleType, Class<T> tDaoClass) {
        switch (moduleType) {
            case USER:
                return tDaoClass.cast(UserModule.getDao());

            case INSTITUTE:
                return tDaoClass.cast(InstituteModule.getDao());

            case EDUCATION:
                return tDaoClass.cast(EducationModule.getDao());

            case TAG:
                return tDaoClass.cast(TagModule.getDao());

            case PROJECT:
                return tDaoClass.cast(ProjectModule.getDao());

            case RESOURCE:
                return tDaoClass.cast(ResourceModule.getDao());

            default:
                throw new IllegalStateException("ModuleType: " + moduleType);
        }
    }

    protected static S3Service getS3Service() {
        return MainService.amazonS3;
    }

    protected static S3EncryptedService getS3EncryptedService() {
        return MainService.amazonS3Encrypted;
    }
}
