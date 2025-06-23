package fe.be_inkrealm_demo2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Lớp Entity cho bảng 'status'
 * Ánh xạ tới cột status_code làm khóa chính
 */
@Entity
@Table(name = "status")
@Data // Tự động tạo getters, setters, toString, equals và hashCode
@NoArgsConstructor // Tự động tạo constructor không đối số
@AllArgsConstructor // Tự động tạo constructor với tất cả các đối số
public class Status {

    @Id // Đánh dấu là khóa chính
    @Column(name = "status_code", length = 50) // Ánh xạ tới cột 'status_code'
    private String statusCode;

    @Column(name = "status_description", nullable = false) // Ánh xạ tới cột 'status_description'
    private String statusDescription;
}
