package fe.be_inkrealm_demo2;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;

@SpringBootApplication
public class BeInkrealmDemo2Application {

    public static void main(String[] args) {

//        try (final Connection connection =
//                     DriverManager.getConnection("jdbc:postgresql://ink-realm-test-huyga154-exe.b.aivencloud.com:23573/defaultdb?ssl=require&user=avnadmin&password=************************");
//             final Statement statement = connection.createStatement();
//             final ResultSet resultSet = statement.executeQuery("SELECT version()")) {
//
//            while (resultSet.next()) {
//                System.out.println("Version: " + resultSet.getString("version"));
//            }
//        } catch (SQLException e) {
//            System.out.println("Connection failure.");
//            e.printStackTrace();
//        }

        Dotenv dotenv = Dotenv.load();
//        spring.application.name=${APPLICATION_NAME}
        System.setProperty("spring.application.name", dotenv.get("APPLICATION_NAME"));
//        spring.datasource.url=${DATASOURCE_URL}
        System.setProperty("spring.datasource.url", dotenv.get("DATASOURCE_URL"));
//        cloudinary.cloud.name=${CLOUDINARY_NAME}
        System.setProperty("cloudinary.api.key", dotenv.get("CLOUDINARY_KEY"));
//        cloudinary.api.key=${CLOUDINARY_KEY}
        System.setProperty("cloudinary.api.secret", dotenv.get("CLOUDINARY_SECRET"));
//        cloudinary.api.secret=${CLOUDINARY_SECRET}
        SpringApplication.run(BeInkrealmDemo2Application.class, args);
    }
}