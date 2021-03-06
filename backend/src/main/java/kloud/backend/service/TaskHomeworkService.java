package kloud.backend.service;

import kloud.backend.config.Constants;
import kloud.backend.entity.TaskHomework;
import kloud.backend.repository.CourseRepository;
import kloud.backend.repository.TaskHomeworkRepository;
import kloud.backend.repository.UserCourseRepository;
import kloud.backend.service.dto.HomeworkFileDTO;
import kloud.backend.service.dto.HomeworkStatistics;
import kloud.backend.service.dto.UserCourseDTO2;
import kloud.backend.util.MinioUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class TaskHomeworkService {

    @Resource
    private TaskHomeworkRepository taskHomeworkRepository;

    @Resource
    private UserCourseRepository userCourseRepository;

    @Resource
    private MinioUtil minioUtil;

    public void upload(String userId, String login, String realName, String taskId, String taskName, MultipartFile file) {
        Optional<TaskHomework> taskHomework = taskHomeworkRepository.findByUserIdAndTaskId(Long.valueOf(userId), Long.valueOf(taskId));
        if (taskHomework.isPresent()) {
            updateHomework(taskHomework.get(), login, realName, taskId, taskName,file);
        }
        else {
            uploadHomework(userId, login, realName, taskId, taskName, file);
        }
    }

    public void uploadHomework(String userId, String login, String realName, String taskId, String taskName, MultipartFile file) {
        String originFileName = file.getOriginalFilename();
        String suffix = originFileName.substring(originFileName.lastIndexOf('.'));
        String fileName = minioFileName(login, realName, taskName, suffix);
        String buckName = Constants.BUCKET_PREFIX + taskId;
        String url = minioUtil.upload(buckName, file, fileName);
        TaskHomework taskHomework = new TaskHomework();
        taskHomework.setUserId(Long.valueOf(userId));
        taskHomework.setTaskId(Long.valueOf(taskId));
        taskHomework.setUrl(url);
        taskHomework.setFileName(fileName);
        taskHomework.setOriginFileName(originFileName);
        taskHomework.setCreatedBy(realName);
        taskHomeworkRepository.save(taskHomework);
    }

    public void updateHomework(TaskHomework taskHomework, String login, String realName, String taskId, String taskName, MultipartFile file) {
        String originFileName = file.getOriginalFilename();
        String suffix = originFileName.substring(originFileName.lastIndexOf('.'));
        String fileName = minioFileName(login, realName, taskName, suffix);
        String buckName = Constants.BUCKET_PREFIX + taskId;
        minioUtil.removeObject(buckName, taskHomework.getFileName());
        String url = minioUtil.upload(buckName, file, fileName);
        taskHomework.setOriginFileName(originFileName);
        taskHomework.setFileName(fileName);
        taskHomework.setUrl(url);
        taskHomework.setLastModifiedBy(realName);
        taskHomeworkRepository.save(taskHomework);
    }

    private String minioFileName(String login, String realName, String taskName, String suffix) {
        return login + '_' + realName + '_' + taskName + suffix;
    }

    public boolean isSubmitted(@RequestParam Long userId, @RequestParam Long taskId) {
        Optional<TaskHomework> taskHomework = taskHomeworkRepository.findByUserIdAndTaskId(userId, taskId);
        return taskHomework.isPresent();
    }

    public List<HomeworkFileDTO> getAllUrlByTaskId(Long taskId) {
        return taskHomeworkRepository.getAllUrlByTaskId(taskId);
    }

    public HomeworkStatistics getHomeworkStatistics(Long taskId, Long courseId) {
        HomeworkStatistics homeworkStatistics = new HomeworkStatistics();
        homeworkStatistics.setSubmitted(taskHomeworkRepository.countAllByTaskId(taskId));
        homeworkStatistics.setNotSubmitted(userCourseRepository.countAllByCourseId(courseId) - homeworkStatistics.getSubmitted());
        return homeworkStatistics;
    }

    public List<UserCourseDTO2> getAllStudentNotSubmitted(Long taskId, Long courseId) {
        return taskHomeworkRepository.getAllStudentNotSubmitted(taskId, courseId);
    }

    public void deleteAllByTaskId(Long taskId) {
        taskHomeworkRepository.deleteAllByTaskId(taskId);
    }

}
