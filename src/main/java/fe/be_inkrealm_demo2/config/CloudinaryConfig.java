package fe.be_inkrealm_demo2.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    // load cloud name
    @Value("${cloudinary.cloud.name}")
    private String cloud_name;

    // load api key
    @Value("${cloudinary.api.key}")
    private String api_key;

    // load api secret key
    @Value("${cloudinary.api.secret}")
    private String api_secret;


    // verify Cloudinary credential
    @Bean
    public Cloudinary getCloudinary() {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloud_name,
                "api_key", api_key,
                "api_secret", api_secret,
                "secure", true));
        return cloudinary;
    }

}
