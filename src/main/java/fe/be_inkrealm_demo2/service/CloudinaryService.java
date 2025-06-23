package fe.be_inkrealm_demo2.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Service để tương tác với Cloudinary API.
 */
@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /**
     * Tải ảnh lên Cloudinary từ chuỗi Base64.
     * @param base64Image Chuỗi ảnh Base64 (không bao gồm "data:image/jpeg;base64," prefix)
     * @param folder Tên thư mục trên Cloudinary để lưu ảnh (ví dụ: "story_covers")
     * @return URL của ảnh đã tải lên
     * @throws IOException Nếu có lỗi trong quá trình tải lên
     */
    public String uploadImage(String base64Image, String folder) throws IOException {
        if (base64Image == null || base64Image.isEmpty()) {
            return null;
        }

        // Cloudinary expects the full data URI string, so re-add the prefix assuming frontend sends just the base64 part.
        // Adjust mime type if needed (e.g., image/png). Assuming JPEG for story covers.
        String dataUri = "data:image/jpeg;base64," + base64Image;

        Map<String, Object> uploadParams = new HashMap<>();
        uploadParams.put("folder", folder);
        uploadParams.put("resource_type", "image");
        // You can add more parameters here, e.g., "public_id", "overwrite", etc.

        // Use the upload method that accepts a data URI string
        Map uploadResult = cloudinary.uploader().upload(dataUri, uploadParams);

        return (String) uploadResult.get("url");
    }

    /**
     * Xóa ảnh khỏi Cloudinary bằng public ID.
     * @param publicId Public ID của ảnh trên Cloudinary
     * @throws IOException Nếu có lỗi trong quá trình xóa
     */
    public void deleteImage(String publicId) throws IOException {
        if (publicId == null || publicId.isEmpty()) {
            return;
        }
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    public String extractPublicIdFromUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }
        // Cloudinary URL structure often contains /upload/v<version>/<public_id_with_folder>.
        // We need to parse this to get the public ID.
        int uploadIndex = imageUrl.indexOf("/upload/");
        if (uploadIndex == -1) return null;

        String afterUpload = imageUrl.substring(uploadIndex + "/upload/".length());
        int versionIndex = afterUpload.indexOf("/v"); // Find version prefix
        if (versionIndex != -1) {
            String publicIdWithExtension = afterUpload.substring(versionIndex + 2); // Skip /v and version number
            int slashIndex = publicIdWithExtension.indexOf("/"); // find the first slash after v<version>
            if (slashIndex != -1) {
                // publicId could be like "folder/image_name.jpg"
                publicIdWithExtension = publicIdWithExtension.substring(slashIndex + 1);
            }
            int dotIndex = publicIdWithExtension.lastIndexOf('.');
            if (dotIndex != -1) {
                return publicIdWithExtension.substring(0, dotIndex); // Return without extension
            }
            return publicIdWithExtension;
        }
        return null;
    }
}

