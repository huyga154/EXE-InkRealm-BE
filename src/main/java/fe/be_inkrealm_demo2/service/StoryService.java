package fe.be_inkrealm_demo2.service;

import fe.be_inkrealm_demo2.dto.request.StoryRequestDTO;
import fe.be_inkrealm_demo2.dto.response.StoryResponseDTO;
import fe.be_inkrealm_demo2.entity.Status;
import fe.be_inkrealm_demo2.entity.Story;
import fe.be_inkrealm_demo2.repository.StatusRepository;
import fe.be_inkrealm_demo2.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoryService {

    private final StoryRepository storyRepository;

    private final StatusRepository statusRepository;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public StoryService(StoryRepository storyRepository, StatusRepository statusRepository, CloudinaryService cloudinaryService) {
        this.storyRepository = storyRepository;
        this.statusRepository = statusRepository;
        this.cloudinaryService = cloudinaryService;
    }

    /**
     * Chuyển đổi Story entity thành StoryResponseDTO.
     * @param story Entity Story
     * @return StoryResponseDTO tương ứng
     */
    private StoryResponseDTO convertToDto(Story story) {
        if (story == null) return null;
//        System.out.println(story);
//        StoryResponseDTO storyResponseDTO = new StoryResponseDTO();
//        storyResponseDTO.setStoryDescription(story.getStoryDescription());
//        storyResponseDTO.set
        StoryResponseDTO storyResponseDTO = StoryResponseDTO.builder()
                .storyId(story.getStoryId())
                .storyName(story.getStoryName())
                .storyAuthor(story.getStoryAuthor())
                .storyImg(story.getStoryImg())
                .storyConverter(story.getStoryConverter())
                .storyDescription(story.getStoryDescription())
                .build();

        return storyResponseDTO;
    }

    public List<StoryResponseDTO> getAllStories() {

        List<Story> storyList = storyRepository.findAll();
        List<StoryResponseDTO> responseDTOList = new ArrayList<>();
        for (Story story : storyList) {
            StoryResponseDTO storyResponseDTO = convertToDto(story);
            responseDTOList.add(storyResponseDTO);
        }
        return responseDTOList;
    }

    public Optional<StoryResponseDTO> getStoryById(Long id) {
        return storyRepository.findById(id)
                .map(this::convertToDto);
    }
    @Transactional
    public StoryResponseDTO createStory(StoryRequestDTO storyRequestDTO) {
        Status status = statusRepository.findById(storyRequestDTO.getStatusCode())
                .orElseThrow(() -> new RuntimeException("Status with code " + storyRequestDTO.getStatusCode() + " not found."));

        Story story = new Story();
        story.setStoryName(storyRequestDTO.getStoryName());
        // storyImg initially null or empty, will be updated after Cloudinary upload
        story.setStoryAuthor(storyRequestDTO.getStoryAuthor());
        story.setStoryConverter(storyRequestDTO.getStoryConverter());
        story.setStoryDescription(storyRequestDTO.getStoryDescription());
        story.setStatus(status);

        // First, save the story without the Cloudinary URL to get the ID
        Story savedStory = storyRepository.save(story);

        // If Base64 image is provided, upload to Cloudinary and update the story
        if (storyRequestDTO.getStoryImg() != null && !storyRequestDTO.getStoryImg().isEmpty()) {
            try {
                // "story_covers" is the folder name in Cloudinary
                String imageUrl = cloudinaryService.uploadImage(storyRequestDTO.getStoryImg(), "story_covers");
                savedStory.setStoryImg(imageUrl); // Set the Cloudinary URL
                storyRepository.save(savedStory); // Update the story with the image URL
            } catch (IOException e) {
                // Log the error and potentially handle rollback or set a default image
                System.err.println("Failed to upload image to Cloudinary for story " + savedStory.getStoryId() + ": " + e.getMessage());
                // Depending on criticality, you might throw an exception, delete the story, or set a default image URL
                // For now, we'll just proceed without the image URL in DB if upload fails
            }
        }

        return convertToDto(savedStory);
    }

    @Transactional
    public Optional<StoryResponseDTO> updateStory(Long id, StoryRequestDTO storyRequestDTO) {
        return storyRepository.findById(id)
                .map(existingStory -> {
                    existingStory.setStoryName(storyRequestDTO.getStoryName());
                    existingStory.setStoryAuthor(storyRequestDTO.getStoryAuthor());
                    existingStory.setStoryConverter(storyRequestDTO.getStoryConverter());
                    existingStory.setStoryDescription(storyRequestDTO.getStoryDescription());

                    if (storyRequestDTO.getStatusCode() != null) {
                        Status newStatus = statusRepository.findById(storyRequestDTO.getStatusCode())
                                .orElseThrow(() -> new RuntimeException("Status with code " + storyRequestDTO.getStatusCode() + " not found."));
                        existingStory.setStatus(newStatus);
                    }

                    // Handle image update:
                    // If a new image is provided: delete old one (if exists) and upload new one
                    if (storyRequestDTO.getStoryImg() != null && !storyRequestDTO.getStoryImg().isEmpty()) {
                        try {
                            // Delete old image from Cloudinary if it exists
                            if (existingStory.getStoryImg() != null && !existingStory.getStoryImg().isEmpty()) {
                                String publicId = cloudinaryService.extractPublicIdFromUrl(existingStory.getStoryImg());
                                if (publicId != null) {
                                    cloudinaryService.deleteImage(publicId);
                                }
                            }
                            String newImageUrl = cloudinaryService.uploadImage(storyRequestDTO.getStoryImg(), "story_covers");
                            existingStory.setStoryImg(newImageUrl);
                        } catch (IOException e) {
                            System.err.println("Failed to update image for story " + existingStory.getStoryId() + ": " + e.getMessage());
                            // Keep old image URL or set to null on failure, depending on desired behavior
                        }
                    }
                    Story updatedStory = storyRepository.save(existingStory);
                    return convertToDto(updatedStory);
                });
    }

    @Transactional
    public boolean logicallyDeleteStory(Long id) {
        return storyRepository.findById(id)
                .map(story -> {
                    // Optional: Delete image from Cloudinary when story is logically deleted
                    if (story.getStoryImg() != null && !story.getStoryImg().isEmpty()) {
                        try {
                            String publicId = cloudinaryService.extractPublicIdFromUrl(story.getStoryImg());
                            if (publicId != null) {
                                cloudinaryService.deleteImage(publicId);
                            }
                        } catch (IOException e) {
                            System.err.println("Failed to delete Cloudinary image during logical delete for story " + story.getStoryId() + ": " + e.getMessage());
                        }
                    }

                    Optional<Status> archivedStatus = statusRepository.findById("ARCHIVED");
                    if (archivedStatus.isPresent()) {
                        story.setStatus(archivedStatus.get());
                        // Consider also clearing storyImg in DB if logically deleted
                        // story.setStoryImg(null); // Set URL to null
                        storyRepository.save(story);
                        return true;
                    } else {
                        throw new RuntimeException("Status 'ARCHIVED' not found. Please ensure it's in the status table.");
                    }
                }).orElse(false);
    }
}

