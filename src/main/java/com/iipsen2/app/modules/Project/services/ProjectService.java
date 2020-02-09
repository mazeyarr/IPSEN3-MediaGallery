package com.iipsen2.app.modules.Project.services;

import com.iipsen2.app.interfaces.enums.LikeType;
import com.iipsen2.app.modules.Project.ProjectModule;
import com.iipsen2.app.modules.Project.dao.ProjectDao;
import com.iipsen2.app.modules.Project.models.Project;
import com.iipsen2.app.modules.Project.models.ProjectSimple;
import com.iipsen2.app.modules.Tag.models.Tag;
import com.iipsen2.app.modules.Tag.models.TagSimple;
import com.iipsen2.app.modules.Upload.services.ResourceService;
import com.iipsen2.app.modules.User.models.User;
import com.iipsen2.app.modules.User.services.UserService;
import com.iipsen2.app.services.CoreService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProjectService extends CoreService {
    public static Project createProject(Project newProject) {
        long projectId = getDao().createProject(
                newProject,
                newProject.getCreatedBy(),
                newProject.getEducation()
        );

        return getDao().findProjectById(projectId);
    }

    public static List<Project> findProjectAll() {
        return getDao().findProjectAll();
    }

    public static List<ProjectSimple> findSimpleProjectAll() {
        return getDao().findSimpleProjectAll();
    }

    public static Project findProjectById(long id) {
        return getDao().findProjectById(id);
    }

    public static List<Project> searchProjectByTitle(String title, boolean caseSensitive) {
        if (caseSensitive) {
            return getDao().searchProjectByTitleCaseSensitive("%" + title + "%");
        }
        return getDao().searchProjectByTitle("%" + title + "%");
    }

    public static List<Tag> getTagsOfProject(Project project) {
        return getDao().getTagsOfProject(
            project.getId()
        );
    }

    public static List<TagSimple> getSimpleTagsOfProject(ProjectSimple project) {
        return getDao().getSimpleTagsOfProject(
            project.getId()
        );
    }

    public static Map<LikeType, List<User>> getLikesOfProject(Project project) {
        Map<LikeType, List<User>> allLikes = new HashMap<>();

        for (LikeType likeType : LikeType.values()) {
            allLikes.put(
                likeType,
                getDao().getLikesOfProject(
                    project.getId(),
                    likeType.toString()
                )
            );
        }

        return allLikes;
    }

    public static Map<LikeType, Long> getSimpleLikes(ProjectSimple projectSimple) {
        Map<LikeType, Long> allLikes = new HashMap<>();

        for (LikeType likeType : LikeType.values()) {
            allLikes.put(
                likeType,
                getDao().getCountOfLikesOfProject(
                    projectSimple.getId(),
                    likeType.toString()
                )
            );
        }

        return allLikes;
    }

    public static void updateProject(Project project) {
        getDao().updateProject(
                project,
                project.getCreatedBy(),
                project.getEducation()
        );
    }

    public static void addTagToProject(Project project, Tag tag) {
        getDao().addTagToProject(
                tag,
                project
        );
    }

    public static void removeTagFromProject(Project project, Tag tag) {
        getDao().removeTagFromProject(
                tag,
                project
        );
    }

    public static void addLikeToProject(Project project, LikeType likeType) {
        Map<LikeType, List<User>> allLikesOfProject = getLikesOfProject(project);

        if (isAlreadyLikedByUser(allLikesOfProject, UserService.getAuthenticatedUser().getId())) {
            getDao().removeLikeFromProject(UserService.getAuthenticatedUser(), project);

            if (isSameTypeOfLike(allLikesOfProject, UserService.getAuthenticatedUser().getId(), likeType)) {
                getDao().removeLikeFromProject(UserService.getAuthenticatedUser(), project);
                return;
            }
        }

        getDao().addLikeToProject(UserService.getAuthenticatedUser(), likeType, project);
    }

    public static void addLikeToProjectFromUser(Project project, User user, LikeType likeType) {
        Map<LikeType, List<User>> allLikesOfProject = getLikesOfProject(project);

        if (isAlreadyLikedByUser(allLikesOfProject, user.getId())) {
            getDao().removeLikeFromProject(user, project);

            if (isSameTypeOfLike(allLikesOfProject, user.getId(), likeType)) {
                getDao().removeLikeFromProject(user, project);
                return;
            }
        }

        getDao().addLikeToProject(user, likeType, project);
    }

    public static void addLikeToProjectIdFromUserId(long projectId, long userId, LikeType likeType) {
        Project project = ProjectService.findProjectById(projectId);
        User user = UserService.getUserById(userId);
        Map<LikeType, List<User>> allLikesOfProject = getLikesOfProject(project);

        if (isAlreadyLikedByUser(allLikesOfProject, user.getId())) {
            getDao().removeLikeFromProject(user, project);

            if (isSameTypeOfLike(allLikesOfProject, user.getId(), likeType)) {
                getDao().removeLikeFromProject(user, project);
                return;
            }
        }

        getDao().addLikeToProject(user, likeType, project);
    }

    public static boolean deleteProjectById(long id) {
        Project project = ProjectService.findProjectById(id);

        if (project.isValid()) {
            //TODO: try / catch
            project.hasTags().forEach(tag -> ProjectService.removeTagFromProject(project, tag));

            project.hasLikes().forEach((likeType, users) ->
                users.forEach(user -> getDao().removeLikeFromProject(user, project))
            );

            ResourceService.deleteResourceByProjectId(id);
            getDao().deleteProjectById(id);

            return true;
        } else {
            return false;
        }
    }

    public static boolean isAlreadyLikedByUser(
        Map<LikeType, List<User>> allLikes,
        long userId
    ) {
        AtomicBoolean hasLiked = new AtomicBoolean(false);

        allLikes.forEach(((likeType, users) -> {
            boolean check = allLikes.get(likeType)
                .stream()
                .anyMatch(user -> user.getId() == userId);

            if (check) hasLiked.set(true);
        }));

        return hasLiked.get();
    }

    public static boolean isSameTypeOfLike(
        Map<LikeType, List<User>> allLikes,
        long userId,
        LikeType checkLikeType
    ) {
        return allLikes.get(checkLikeType)
            .stream()
            .anyMatch(user -> user.getId() == userId);
    }

    private static ProjectDao getDao() {
        return getDao(ProjectModule.MODULE_TYPE, ProjectDao.class);
    }
}
