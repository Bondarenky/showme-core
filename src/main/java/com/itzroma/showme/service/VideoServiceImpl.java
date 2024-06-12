package com.itzroma.showme.service;

import com.itzroma.showme.domain.FileType;
import com.itzroma.showme.domain.entity.User;
import com.itzroma.showme.domain.entity.Video;
import com.itzroma.showme.domain.entity.VideoType;
import com.itzroma.showme.repository.UserRepository;
import com.itzroma.showme.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {
    private final AmazonS3Service s3Service;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    @Override
    public Video uploadVideo(MultipartFile video, MultipartFile preview,
                             User author, String title, String description, List<VideoType> videoTypes) {
        String videoUrl = s3Service.uploadFile(video, FileType.VIDEO, author.getId());
        String previewUrl = s3Service.uploadFile(preview, FileType.PREVIEW, author.getId());
        return videoRepository.save(new Video(videoUrl, previewUrl, title, description, author));
    }

    @Override
    public Optional<Video> findVideoById(String id) {
        return videoRepository.findById(id);
    }

    @Override
    public void toggleLike(User user, Video video) {
        if (video.getDislikes().contains(user)) {
            video.getDislikes().remove(user);
            user.getDislikedVideos().remove(video);
        }

        if (video.getLikes().contains(user)) {
            video.getLikes().remove(user);
            user.getLikedVideos().remove(video);
        } else {
            video.getLikes().add(user);
            user.getLikedVideos().add(video);
        }
        videoRepository.save(video);
        userRepository.save(user);
    }

    @Override
    public void toggleDislike(User user, Video video) {
        if (video.getLikes().contains(user)) {
            video.getLikes().remove(user);
            user.getLikedVideos().remove(video);
        }

        if (video.getDislikes().contains(user)) {
            video.getDislikes().remove(user);
            user.getDislikedVideos().remove(video);
        } else {
            video.getDislikes().add(user);
            user.getDislikedVideos().add(video);
        }
        videoRepository.save(video);
        userRepository.save(user);
    }

    @Override
    public List<Video> findBySearchTextAndTypes(String searchText, VideoType videoType) {
        return videoRepository.findBySearchTextAndType(searchText, videoType);
    }
}
